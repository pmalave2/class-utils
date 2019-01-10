package com.general.utils.datatables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This Comparator will invoke other Comparators as necesary in an attempt to
 * sort on multiple fields from the same object.
 *
 * The first Comparator in the List is always invoked. The other Comparators are
 * only invoked as required when the previous Comparator test returns an "equal"
 * result.
 */
public class GroupComparator implements Comparator {
	private List<Comparator> comparators = new ArrayList<>();

	/*
	 * A GroupComparator will always have at least two Comparators
	 */
	public GroupComparator(Comparator first, Comparator second) {
		comparators.add(first);
		comparators.add(second);
	}

	public GroupComparator() {
	}

	/*
	 * Add another Comparator to the sort mix.
	 */
	public void addComparator(Comparator comparator) {
		comparators.add(comparator);
	}

	/*
	 * Implement the Comparator interface
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int compare(Object object1, Object object2) {
		// As long as the compare() result is equal (ie. 0),
		// invoke the next Comparator

		for (Comparator comparator : comparators) {
			int returnValue = comparator.compare(object1, object2);

			if (returnValue != 0)
				return returnValue;
		}

		return 0;
	}
}
