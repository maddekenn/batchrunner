package se.uu.ub.cora.batchrunner.change;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CompleteLanguageDataDividerChangerBatchRunner {
	protected static DataUpdater dataUpdater;
	static List<String> errors = new ArrayList<>();

	// private static List<List<String>> presentationNames;
	// protected static Finder finder;

	private CompleteLanguageDataDividerChangerBatchRunner() {
	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String dataUpdaterClassName = args[0];
		String url = args[1];
		String httpFactoryClassName = args[2];
		String finderClassName = args[3];

		// setUpNameOfPresentations();
		//
		// createFinder(finderClassName, url, httpFactoryClassName);
		// createDataGroupCopier(dataCopierClassName, url, httpFactoryClassName);
		// Collection<String> records = finder.findRecords();
		// for (String recordType : records) {
		// createPresentationsForRecordType(recordType);
		// }

		System.out.println("done ");
	}

}
