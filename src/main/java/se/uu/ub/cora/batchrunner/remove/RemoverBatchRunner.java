package se.uu.ub.cora.batchrunner.remove;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import se.uu.ub.cora.batchrunner.find.Finder;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class RemoverBatchRunner {

	protected static RecordRemover remover;

	private RemoverBatchRunner() {

	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String removerClassName = args[0];
		String finderClassName = args[1];
		String url = args[2];
		String httpFactoryClassName = args[3];

		Finder finder = createFinder(finderClassName, url, httpFactoryClassName);
		createRemover(removerClassName, url, httpFactoryClassName);
		remover.setFinder(finder);
		Collection<String> records = remover.removeRecordsFoundByFinder();
		System.out.println("records removed");
		records.forEach(System.out::println);

		System.out.println("done");

	}

	private static void createRemover(String removerClassName, String url,
			String httpFactoryClassName) throws NoSuchMethodException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		constructRemover(removerClassName);
		remover.setUrlString(url);
		setHttpFactoryInRemover(httpFactoryClassName);
	}

	private static void constructRemover(String removerClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = Class.forName(removerClassName).getConstructor();
		remover = (RecordRemover) constructor.newInstance();

	}

	private static Finder createFinder(String finderClassName, String url,
			String httpFactoryClassName) throws NoSuchMethodException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		Finder finder = constructFinder(finderClassName);
		finder.setUrlString(url);
		setHttpFactoryInFinder(finder, httpFactoryClassName);
		return finder;
	}

	private static Finder constructFinder(String finderClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = Class.forName(finderClassName).getConstructor();
		return (Finder) constructor.newInstance();
	}

	private static void setHttpFactoryInFinder(Finder finder, String httpFactoryClassName)
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<?> constructor = Class.forName(httpFactoryClassName).getConstructor();
		HttpHandlerFactory httpFactory = (HttpHandlerFactory) constructor.newInstance();
		finder.setHttpHandlerFactory(httpFactory);
	}

	private static void setHttpFactoryInRemover(String httpFactoryClassName)
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<?> constructor = Class.forName(httpFactoryClassName).getConstructor();
		HttpHandlerFactory httpFactory = (HttpHandlerFactory) constructor.newInstance();
		remover.setHttpHandlerFactory(httpFactory);
	}
}
