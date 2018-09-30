package se.uu.ub.cora.batchrunner.change;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.batchrunner.HttpHandlerHelper;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class CompleteLanguageDataDividerChangerBatchRunner {
	protected static DataUpdater dataUpdater;
	protected static HttpHandlerFactory httpHandlerFactory;
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
		String newDataDivider = args[4];
		createHttpHandlerFactory(httpFactoryClassName);
		createDataUpdater(dataUpdaterClassName, url);

		HttpHandlerHelper httpHandlerHelper = HttpHandlerHelper.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		String readRecord = httpHandlerHelper.readRecord("metadataItemCollection",
				"completeLanguageCollection");

		ClientDataRecord jsonAsClientDataRecord = ConverterHelper
				.getJsonAsClientDataRecord(readRecord);
		ClientDataGroup clientDataGroup = jsonAsClientDataRecord.getClientDataGroup();
		ClientDataGroup collectionItemReferences = clientDataGroup
				.getFirstGroupWithNameInData("collectionItemReferences");

		List<ClientDataGroup> refs = collectionItemReferences.getAllGroupsWithNameInData("ref");
		for (ClientDataGroup ref : refs) {
			String itemId = ref.getFirstAtomicValueWithNameInData("linkedRecordId");
			String itemType = ref.getFirstAtomicValueWithNameInData("linkedRecordType");
			dataUpdater.updateDataDividerInRecordUsingTypeIdAndNewDivider(itemType, itemId,
					newDataDivider);
		}

		System.out.println("done ");
	}

	private static void createHttpHandlerFactory(String httpFactoryClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = Class.forName(httpFactoryClassName).getConstructor();
		httpHandlerFactory = (HttpHandlerFactory) constructor.newInstance();
	}

	private static void createDataUpdater(String updaterClassName, String url)
			throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException,
			InvocationTargetException {
		Class<?>[] cArg = new Class[2];
		cArg[0] = String.class;
		cArg[1] = HttpHandlerFactory.class;
		Method constructor = Class.forName(updaterClassName)
				.getMethod("usingURLAndHttpHandlerFactory", cArg);
		dataUpdater = (DataUpdater) constructor.invoke(null, url, httpHandlerFactory);
	}

}
