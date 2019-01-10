package com.general.utils.datatables;

import java.lang.reflect.Method;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

/**
 * A comparator to sort on the specified field of a given class.
 *
 * Reflection is used to retrieve the data to be sorted, therefore you must
 * provide the Class and the method name within the class that will be used to
 * retrieve the data.
 *
 * Several sort properties can be set:
 *
 * a) ascending (default true) b) ignore case (default true) c) nulls last
 * (default true)
 */
public class BeanPredicate implements Predicate {
	private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[] {};
	private static final Object[] EMPTY_OBJECT_ARRAY = new Object[] {};

	private Method method;
	private boolean isIgnoreCase;
	private String value;

	/*
	 * Sort in the specified order with the remaining default properties
	 */
	public BeanPredicate(Class<?> beanClass, String methodName, String value) {
		this(beanClass, methodName, value, true);
	}

	/*
	 * Sort in the specified order and case sensitivity with the remaining default
	 * properties
	 */
	public BeanPredicate(Class<?> beanClass, String methodName, String value, boolean isIgnoreCase) {
		setIgnoreCase(isIgnoreCase);
		setValue(value);

		// Make sure the method exists in the given bean class

		try {
			method = beanClass.getMethod(methodName, EMPTY_CLASS_ARRAY);
		} catch (NoSuchMethodException nsme) {
			String message = methodName + "() method does not exist";
			throw new IllegalArgumentException(message);
		}

		// Make sure the method returns a value

		Class<?> returnClass = method.getReturnType();

		if (returnClass.getName().equals("void")) {
			String message = methodName + " has a void return type";
			throw new IllegalArgumentException(message);
		}
	}

	/*
	 * Set whether case should be ignored when sorting Strings
	 */
	public void setIgnoreCase(boolean isIgnoreCase) {
		this.isIgnoreCase = isIgnoreCase;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * Implement the Comparable interface
	 */
	public boolean test(Object t) {
		Object field1 = null;

		try {
			field1 = method.invoke(t, EMPTY_OBJECT_ARRAY);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// Treat empty strings like nulls

		if (field1 instanceof String && ((String) field1).length() == 0) {
			field1 = null;
		}

		// Handle sorting of null values

		if (field1 == null)
			return false;

		// Compare objects

		Object c1 = field1;

		if (isIgnoreCase)
			return StringUtils.containsIgnoreCase(c1.toString(), value);
		else
			return StringUtils.containsAny(c1.toString(), value);
	}

}
