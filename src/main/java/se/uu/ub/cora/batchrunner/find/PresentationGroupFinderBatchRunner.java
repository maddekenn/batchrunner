package se.uu.ub.cora.batchrunner.find;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class PresentationGroupFinderBatchRunner {

	protected static Finder recordTypeFinder;
	protected static Finder pGroupFinder;
	private static String httpFactoryClassName;
	private static String url;

	private PresentationGroupFinderBatchRunner() {
	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		url = args[2];
		httpFactoryClassName = args[3];

		String recordTypeFinderClassName = args[0];
		createRecordTypeFinder(recordTypeFinderClassName);

		List<String> records = (List<String>) recordTypeFinder.findRecords();

		String pGroupFinderClassName = args[1];
		createPresentationGroupFinder(pGroupFinderClassName, records);
		Collection<String> foundRecords = pGroupFinder.findRecords();

		foundRecords.forEach(System.out::println);

		System.out.println("done");

	}

	private static void createPresentationGroupFinder(String pGroupFinderClassName,
			List<String> records) throws NoSuchMethodException, ClassNotFoundException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		Class<?>[] cArg = new Class[1];
		cArg[0] = List.class;
		Method constructor = Class.forName(pGroupFinderClassName)
				.getMethod("usingListOfRecordTypes", cArg);
		pGroupFinder = (Finder) constructor.invoke(null, records);
		pGroupFinder.setUrlString(url + "presentationGroup");
		setHttpFactoryInFinder(pGroupFinder);
	}

	private static void createRecordTypeFinder(String finderClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		constructFinder(finderClassName);
		recordTypeFinder.setUrlString(url + "recordType");
		setHttpFactoryInFinder(recordTypeFinder);
	}

	private static void constructFinder(String finderClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = Class.forName(finderClassName).getConstructor();
		recordTypeFinder = (Finder) constructor.newInstance();
	}

	private static void setHttpFactoryInFinder(Finder finder)
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<?> constructor = Class.forName(httpFactoryClassName).getConstructor();
		HttpHandlerFactory httpFactory = (HttpHandlerFactory) constructor.newInstance();
		finder.setHttpHandlerFactory(httpFactory);
	}
}
