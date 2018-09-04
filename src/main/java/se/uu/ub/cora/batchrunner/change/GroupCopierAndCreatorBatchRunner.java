package se.uu.ub.cora.batchrunner.change;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GroupCopierAndCreatorBatchRunner {

	protected static DataJsonCopier dataCopier;

	private GroupCopierAndCreatorBatchRunner() {
	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String deleterClassName = args[0];
		String url = args[1];
		String httpFactoryClassName = args[2];
		String copierClassName = args[3];
		constructDataCopier(deleterClassName);
		// deleter.setUrlString(url);
		// HttpHandlerFactory httpFactory =
		// createHttpHandlerFactory(httpFactoryClassName);
		// deleter.setHttpHandlerFactory(httpFactory);
		// createFinder(finderClassName, url + "recordType", httpFactoryClassName);
		//
		// List<String> errors = new ArrayList<>();
		// Collection<String> records = finder.findRecords();
		// for (String recordType : records) {
		// System.out.println("starting recordtype " + recordType);
		// List<String> groupsToDelete = new ArrayList<>();
		// groupsToDelete.add(recordType + "PGroup");
		// groupsToDelete.add(recordType + "NewPGroup");
		// groupsToDelete.add(recordType + "OutputPGroup");
		// List<String> errorMessages = deleter.deletePGroups(groupsToDelete);
		// errors.addAll(errorMessages);
		// }
		//
		// System.out.println("done");
		// errors.forEach(System.out::println);
	}

	private static void constructDataCopier(String dataCopierClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = Class.forName(dataCopierClassName).getConstructor();
		dataCopier = (DataJsonCopier) constructor.newInstance();
	}
}
