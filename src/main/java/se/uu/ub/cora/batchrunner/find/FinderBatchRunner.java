package se.uu.ub.cora.batchrunner.find;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class FinderBatchRunner {

	protected static Finder finder;

	private FinderBatchRunner() {

	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String finderClassName = args[1];
		String url = args[2];
		String httpFactoryClassName = args[3];

		createFinder(finderClassName, url, httpFactoryClassName);

		Collection<String> records = finder.findRecords();

		records.forEach(System.out::println);

		System.out.println("done");

	}

	private static void createFinder(String finderClassName, String url,
			String httpFactoryClassName) throws NoSuchMethodException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		constructFinder(finderClassName);
		finder.setUrlString(url);
		setHttpFactoryInFinder(httpFactoryClassName);
	}

	private static void constructFinder(String finderClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = Class.forName(finderClassName).getConstructor();
		finder = (Finder) constructor.newInstance();
	}

	private static void setHttpFactoryInFinder(String httpFactoryClassName)
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<?> constructor = Class.forName(httpFactoryClassName).getConstructor();
		HttpHandlerFactory httpFactory = (HttpHandlerFactory) constructor.newInstance();
		finder.setHttpHandlerFactory(httpFactory);
	}
}
