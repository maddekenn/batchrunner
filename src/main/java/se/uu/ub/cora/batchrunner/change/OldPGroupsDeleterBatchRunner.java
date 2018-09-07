package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.batchrunner.find.Finder;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OldPGroupsDeleterBatchRunner {

	protected static Deleter deleter;

	protected static Finder finder;

	private OldPGroupsDeleterBatchRunner() {
	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String deleterClassName = args[0];
		String url = args[1];
		String httpFactoryClassName = args[2];
		String finderClassName = args[3];
		constructDeleter(deleterClassName);
		deleter.setUrlString(url);
		HttpHandlerFactory httpFactory = createHttpHandlerFactory(httpFactoryClassName);
		deleter.setHttpHandlerFactory(httpFactory);
		createFinder(finderClassName, url + "recordType", httpFactoryClassName);

		List<String> errors = new ArrayList<>();
		Collection<String> records = finder.findRecords();
		for (String recordType : records) {
			List<String> groupsToDelete = createListOfGroupsToBeDeleted(recordType);
			List<String> errorMessages = deleter.deletePGroups(groupsToDelete);
			errors.addAll(errorMessages);
		}

		System.out.println("done");
		errors.forEach(System.out::println);
	}

	private static List<String> createListOfGroupsToBeDeleted(String recordType) {
		List<String> groupsToDelete = new ArrayList<>();
		groupsToDelete.add(recordType + "FormPGroup");
		groupsToDelete.add(recordType + "FormNewPGroup");
		groupsToDelete.add(recordType + "ViewPGroup");
		return groupsToDelete;
	}

	private static void constructDeleter(String deleterClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = Class.forName(deleterClassName).getConstructor();
		deleter = (Deleter) constructor.newInstance();
	}

	private static HttpHandlerFactory createHttpHandlerFactory(String httpFactoryClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = Class.forName(httpFactoryClassName).getConstructor();
		return (HttpHandlerFactory) constructor.newInstance();
	}

	private static void createFinder(String finderClassName, String url,
			String httpFactoryClassName) throws NoSuchMethodException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		constructFinder(finderClassName);
		finder.setUrlString(url);
		HttpHandlerFactory httpFactory = createHttpHandlerFactory(httpFactoryClassName);
		finder.setHttpHandlerFactory(httpFactory);
	}

	private static void constructFinder(String finderClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = Class.forName(finderClassName).getConstructor();
		finder = (Finder) constructor.newInstance();
	}
}
