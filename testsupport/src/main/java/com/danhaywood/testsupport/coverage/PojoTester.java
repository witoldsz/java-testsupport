package com.danhaywood.testsupport.coverage;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import junit.framework.AssertionFailedError;

public final class PojoTester {

	public static interface FixtureDatumFactory<T> {
		T getNext();
	}

	private final Map<Class<?>, FixtureDatumFactory<?>> fixtureDataByType = new HashMap<Class<?>, FixtureDatumFactory<?>>();
	private final AtomicInteger counter = new AtomicInteger();

	public PojoTester() {
		
		FixtureDatumFactory<Boolean> booleanDatumFactory = new FixtureDatumFactory<Boolean>() {
			public Boolean getNext() {
				return counter.getAndIncrement() == 0;
			}
		};
		fixtureDataByType.put(boolean.class, booleanDatumFactory);
		fixtureDataByType.put(Boolean.class, booleanDatumFactory);

		
		FixtureDatumFactory<Byte> byteDatumFactory = new FixtureDatumFactory<Byte>() {
			public Byte getNext() {
				return (byte) counter.getAndIncrement();
			}
		};
		fixtureDataByType.put(byte.class, byteDatumFactory);
		fixtureDataByType.put(Byte.class, byteDatumFactory);
		
		
		FixtureDatumFactory<Short> shortDatumFactory = new FixtureDatumFactory<Short>() {
			public Short getNext() {
				return (short) counter.getAndIncrement();
			}
		};
		fixtureDataByType.put(short.class, shortDatumFactory);
		fixtureDataByType.put(Short.class, shortDatumFactory);
		
		
		FixtureDatumFactory<Character> charDatumFactory = new FixtureDatumFactory<Character>() {
			public Character getNext() {
				return (char) counter.getAndIncrement();
			}
		};
		fixtureDataByType.put(char.class, charDatumFactory);
		fixtureDataByType.put(Character.class, charDatumFactory);
		
		
		FixtureDatumFactory<Integer> intDatumFactory = new FixtureDatumFactory<Integer>() {
			public Integer getNext() {
				return counter.getAndIncrement();
			}
		};
		fixtureDataByType.put(int.class, intDatumFactory);
		fixtureDataByType.put(Integer.class, intDatumFactory);
		
		
		FixtureDatumFactory<Long> longDatumFactory = new FixtureDatumFactory<Long>() {
			public Long getNext() {
				return (long) counter.getAndIncrement();
			}
		};
		fixtureDataByType.put(long.class, longDatumFactory);
		fixtureDataByType.put(Long.class, longDatumFactory);
		
		
		FixtureDatumFactory<Float> floatDatumFactory = new FixtureDatumFactory<Float>() {
			public Float getNext() {
				return new Float(counter.getAndIncrement());
			}
		};
		fixtureDataByType.put(float.class, floatDatumFactory);
		fixtureDataByType.put(Float.class, floatDatumFactory);
		
		
		FixtureDatumFactory<Double> doubleDatumFactory = new FixtureDatumFactory<Double>() {
			public Double getNext() {
				return new Double(counter.getAndIncrement());
			}
		};
		fixtureDataByType.put(double.class, doubleDatumFactory);
		fixtureDataByType.put(Double.class, doubleDatumFactory);

		fixtureDataByType.put(String.class, new FixtureDatumFactory<String>() {
			public String getNext() {
				return "string" + counter.getAndIncrement();
			}
		});

		fixtureDataByType.put(BigDecimal.class, new FixtureDatumFactory<BigDecimal>() {
			public BigDecimal getNext() {
				return new BigDecimal(counter.getAndIncrement());
			}
		});

		fixtureDataByType.put(BigInteger.class, new FixtureDatumFactory<BigInteger>() {
			public BigInteger getNext() {
				return BigInteger.valueOf(counter.getAndIncrement());
			}
		});

		fixtureDataByType.put(Date.class, new FixtureDatumFactory<Date>() {
			public Date getNext() {
				return new Date(counter.getAndIncrement());
			}
		});
		
		fixtureDataByType.put(Timestamp.class, new FixtureDatumFactory<Timestamp>() {
			public Timestamp getNext() {
				return new Timestamp(counter.getAndIncrement());
			}
		});
		
		fixtureDataByType.put(Pattern.class, new FixtureDatumFactory<Pattern>() {
			public Pattern getNext() {
				return Pattern.compile("p" + counter.getAndIncrement());
			}
		});
		
		fixtureDataByType.put(File.class, new FixtureDatumFactory<File>() {
			public File getNext() {
				return new File("file" + counter.getAndIncrement());
			}
		});

		FixtureDatumFactory<List<?>> listDatumFactory = new FixtureDatumFactory<List<?>>() {
			public List<?> getNext() {
				final List<String> list = new ArrayList<String>();
				list.add("element" + counter.getAndIncrement());
				list.add("element" + counter.getAndIncrement());
				list.add("element" + counter.getAndIncrement());
				return list;
			}
		};
		fixtureDataByType.put(Iterable.class, listDatumFactory);
		fixtureDataByType.put(Collection.class, listDatumFactory);
		fixtureDataByType.put(List.class, listDatumFactory);

		fixtureDataByType.put(Set.class, new FixtureDatumFactory<Set<?>>() {
			public Set<?> getNext() {
				final Set<String> list = new HashSet<String>();
				list.add("element" + counter.getAndIncrement());
				list.add("element" + counter.getAndIncrement());
				list.add("element" + counter.getAndIncrement());
				return list;
			}
		});
	}

	public AtomicInteger getCounter() {
		return counter;
	}

	public PojoTester withFixture(Class<?> c, final Object... fixtureData) {
		if (Enum.class.isAssignableFrom(c)) {
			throw new IllegalArgumentException("No need to provide fixture data for enums");
		} 
		if (fixtureData == null || fixtureData.length == 0) {
			throw new IllegalArgumentException("Test data is mandatory");
		}
		for (Object o : fixtureData) {
			if (!c.isAssignableFrom(o.getClass())) {
				throw new IllegalArgumentException("Different classes: "
						+ o.getClass().getName() + " is not a kind of "
						+ c.getName());
			}
		}
		fixtureDataByType.put(c, new FixtureDatumFactory<Object>() {
			private int index = fixtureData.length - 1;

			public Object getNext() {
				index = (index + 1) % fixtureData.length;
				return fixtureData[index];
			}
		});
		return this;
	}

	public <T> PojoTester withFixture(Class<T> c, FixtureDatumFactory<T> factory) {
		fixtureDataByType.put(c, factory);
		return this;
	}

	public void testAllSetters(Object bean) {
		exercise(bean, FilterSet.excluding());
	}

	public void exercise(Object bean, FilterSet filterSet) {
		// an array that fills as each property is tested, allowing
		// subsequent properties to be tested against them
		final List<Method> gettersDone = new ArrayList<Method>();
		final List<TestException> problems = new ArrayList<TestException>();

		final Map<String, Method> methods = getMethodsAsMap(bean);
		for (Entry<String, Method> e : methods.entrySet()) {
			final String methodName = e.getKey();
			if (methodName.startsWith("set")
					&& e.getValue().getParameterTypes().length == 1) {
				final char first = methodName.charAt(3);
				final String remainder = methodName.substring(4);
				final String property = Character.toLowerCase(first)
						+ remainder;
				if (filterSet.shouldInclude(property)) {
					try {
						testOne(bean, methods, property, gettersDone);
					} catch (TestException te) {
						problems.add(te);
					}
				}
			}
		}
		handleExceptions(problems);
	}

	private static void handleExceptions(List<TestException> problems) {
		if (!problems.isEmpty()) {
			Throwable lastCause = null;
			final StringBuilder b = new StringBuilder();
			String newline = "";
			for (TestException te : problems) {
				b.append(newline).append(te.getMessage());
				newline = "\n";
				if (te.getCause() != null) {
					lastCause = te.getCause();
				}
			}
			final AssertionFailedError err = new AssertionFailedError(
					b.toString());
			if (lastCause != null) {
				err.initCause(lastCause);
			}
			throw err;
		}
	}

	private static Map<String, Method> getMethodsAsMap(Object bean) {
		final Map<String, Method> methodMap = new HashMap<String, Method>();
		for (Method m : bean.getClass().getMethods()) {
			methodMap.put(m.getName(), m);
		}
		return methodMap;
	}

	private void testOne(final Object bean, final Map<String, Method> methods,
			String property, List<Method> earlierGetters) throws TestException {
		final String setterName = getAccessor("set", property);
		for (Method setterMethod : methods.values()) {
			final Class<?>[] parameterTypes = setterMethod.getParameterTypes();
			if (setterMethod.getName().equals(setterName)
					&& parameterTypes.length == 1) {
				exercise(bean, property, methods, setterMethod,
						parameterTypes[0], earlierGetters);
				return;
			}
		}
		throw new TestException("No matching setter found for " + property
				+ ".");
	}

	private void exercise(final Object bean, String property,
			final Map<String, Method> methods, Method setterMethod,
			final Class<?> parameterType, List<Method> earlierGetters)
			throws AssertionFailedError, TestException {

		final String setterName = setterMethod.getName();
		FixtureDatumFactory<?> factory = fixtureDataByType.get(parameterType);
		if (factory == null) {
			// automatically populate for enums
			if (Enum.class.isAssignableFrom(parameterType)) {
				final Object[] testData = parameterType.getEnumConstants();
				factory = new FixtureDatumFactory<Object>() {
					private int index = testData.length - 1;
					public Object getNext() {
						index = (index + 1) % testData.length;
						return testData[index];
					}
				};
				fixtureDataByType.put(parameterType, factory);
			} else {
				throw new TestException("No fixture test data is available for "
						+ setterName + "( " + parameterType.getName() + " ).");
			}
		}

		checkMethodVisibility(property, setterName, setterMethod);

		String getterName;
		if (parameterType == boolean.class) {
			getterName = getAccessor("is", property);
			if (property.startsWith("Is") && !methods.containsKey(getterName)) {
				getterName = getAccessor("is", property.substring(2));
			}
		} else {
			getterName = getAccessor("get", property);
		}

		try {
			final Method getterMethod = bean.getClass().getMethod(getterName);
			if (getterMethod.getReturnType().equals(void.class)) {
				throw new TestException(getterName + "(...) is void return.");
			}
			checkMethodVisibility(property, getterName, getterMethod);

			Object value = null;
			for (int i = 0; i < 3; i++) {
				value = factory.getNext();
				invokeSetterAndGetter(bean, property, setterMethod,
						getterMethod, value);
			}

			// compare with the methods we have called earlier
			if (!getterMethod.getReturnType().equals(boolean.class)) {
				for (Method earlierGetter : earlierGetters) {
					final Object earlierValue = earlierGetter.invoke(bean);
					if (earlierValue.equals(value)) {
						throw new TestException(setterName
								+ " interferes with " + earlierGetter.getName());
					}
				}
			}

			// finally store this getter to be tested against the next property
			earlierGetters.add(getterMethod);

		} catch (Exception e) {
			final TestException error = new TestException(property + ": "
					+ e.getMessage());
			error.initCause(e);
			throw error;
		}
	}

	private static void checkMethodVisibility(String property,
			final String accessorName, final Method method)
			throws AssertionFailedError, TestException {
		if (!Modifier.isPublic(method.getModifiers())) {
			throw new TestException("Test failed for " + property + " because "
					+ accessorName + " is not publicly visible.");
		}
		if (!Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
			throw new TestException("Test failed for " + property + " because "
					+ accessorName
					+ " is declared in a class that is not publicly visible.");
		}
	}

	private static void invokeSetterAndGetter(final Object bean,
			String property, Method setterMethod, final Method getterMethod,
			Object t) throws IllegalAccessException, InvocationTargetException,
			AssertionFailedError, TestException {

		setterMethod.invoke(bean, t);
		final Object r = getterMethod.invoke(bean);
		if (!t.getClass().equals(r.getClass())) {
			throw new TestException("Test failed for " + property
					+ " because types do not match.");
		}

		if (!t.equals(r)) {
			throw new TestException("Test failed for " + property + " using "
					+ t.toString());
		}

		if (t instanceof Iterable<?>) {
			final Iterator<?> it = ((Iterable<?>) t).iterator();
			final Iterator<?> ir = ((Iterable<?>) r).iterator();
			while (it.hasNext() && ir.hasNext()) {
				final Object ti = it.next();
				final Object ri = ir.next();
				if (!ti.equals(ri)) {
					throw new TestException("Test failed for " + property
							+ " with iterator item " + ti.toString());
				}
			}
			if (it.hasNext() || ir.hasNext()) {
				throw new TestException("Test failed for " + property
						+ " because iteration lengths differ.");
			}
		}
	}

	private String getAccessor(String prefix, String property) {
		if (property.length() == 1) {
			return prefix + Character.toUpperCase(property.charAt(0));
		}
		return prefix + Character.toUpperCase(property.charAt(0))
				+ property.substring(1);
	}

	
	
	public static final class TestException extends Exception {

		private static final long serialVersionUID = 7870820619976334343L;

		public TestException(String message) {
			super(message);
		}

		public TestException(String message, Throwable t) {
			super(message, t);
		}
	}

	public static class FilterSet extends HashSet<String> {
		private static final long serialVersionUID = 1L;

		private boolean include = false;

		private FilterSet(String... string) {
			super.addAll(Arrays.asList(string));
		}

		private boolean shouldInclude(String x) {
			if (include) {
				return isEmpty() || contains(x);
			} else {
				return !contains(x);
			}
		}

		public static FilterSet includingOnly(String... property) {
			final FilterSet filterSet = new FilterSet(property);
			filterSet.include = true;
			return filterSet;
		}

		public static FilterSet excluding(String... property) {
			return new FilterSet(property);
		}
	}

}
