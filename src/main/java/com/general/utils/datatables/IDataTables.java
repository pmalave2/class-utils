package com.general.utils.datatables;

import java.util.List;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface IDataTables<T> {
	List<T> getDataList();

	DataTablesOutput<T> getDataTables(DataTablesInput dataTablesInput, List<T> data);

	List<T> order(DataTablesInput dataTablesInput, List<T> data);

	List<T> search(DataTablesInput dataTablesInput, List<T> data);
}
