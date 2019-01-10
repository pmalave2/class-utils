package com.general.utils.datatables;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BeanDataSource<T> implements IBeanDataTables<T> {
	private List<T> data;
	private List<T> dataFiltered;
	private Class<T> beanClass;

	public BeanDataSource(Class<T> beanClass) {
		super();
		this.beanClass = beanClass;
	}

	public DataTablesOutput<T> getDataTables(DataTablesInput dti, List<T> data) {
		DataTablesOutput<T> dto = new DataTablesOutput<>();
		List<T> l, l2;

		if (StringUtils.isEmpty(dti.getSearch().getValue())) {
			if (!dti.getOrder().isEmpty() && dti.getStart() == 0)
				setData(order(dti, data));

			l = getData();
		} else {
			if (!StringUtils.isBlank(dti.getSearch().getValue()) && dti.getStart() == 0) {

				setDataFiltered(search(dti, getData()));

				l = getDataFiltered();
			} else
				l = getDataFiltered();

			if (!dti.getOrder().isEmpty() && dti.getStart() == 0)
				setDataFiltered(order(dti, getDataFiltered()));
		}

		l2 = l.subList(dti.getStart(),
				(dti.getStart() + dti.getLength()) > l.size() ? l.size() : dti.getStart() + dti.getLength());

		dto.setRecordsFiltered(l.size());
		dto.setData(l2);
		dto.setDraw(dti.getDraw());
		dto.setRecordsTotal(getData().size());

		return dto;
	}

	public List<T> order(DataTablesInput dti, List<T> data) {
		GroupComparator cg = new GroupComparator();

		dti.getOrder().forEach(e -> {
			String name = dti.getColumns().get(e.getColumn()).getName();
			cg.addComparator(new BeanComparator(beanClass,
					String.format("get%s", name.substring(0, 1).toUpperCase() + name.substring(1)),
					Objects.equals(e.getDir(), null) ? true : e.getDir().equals("asc") ? true : false));
		});

		data.sort(cg);

		return data;
	}

	public List<T> search(DataTablesInput dti, List<T> data) {
		List<BeanPredicate> lp = new ArrayList<>();

		dti.getColumns().stream().filter(e -> e.getSearchable()).collect(Collectors.toList()).forEach(e -> {
			lp.add(new BeanPredicate(beanClass,
					String.format("get%s", e.getName().substring(0, 1).toUpperCase() + e.getName().substring(1)),
					StringUtils.trimToEmpty(dti.getSearch().getValue()).toLowerCase()));
		});

		return data.stream().filter(e -> lp.stream().anyMatch(e2 -> e2.test(e))).collect(Collectors.toList());
	}

}
