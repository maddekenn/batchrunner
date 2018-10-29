package se.uu.ub.cora.batchrunner.change;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.batchrunner.find.Finder;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class DataGroupCopierBatchRunner {

	protected static DataCopier dataCopier;
	static List<String> errors = new ArrayList<>();
	private static List<List<String>> presentationNames;
	protected static Finder finder;

	private DataGroupCopierBatchRunner() {
	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String dataCopierClassName = args[0];
		String url = args[1];
		String httpFactoryClassName = args[2];
		String finderClassName = args[3];

		setUpNameOfPresentations();

		createFinder(finderClassName, url, httpFactoryClassName);
		createDataGroupCopier(dataCopierClassName, url, httpFactoryClassName);
		Collection<String> records = finder.findRecords();
		for (String recordType : records) {
			createPresentationsForRecordType(recordType);
		}

		System.out.println("done ");
	}

	private static void setUpNameOfPresentations() {
		List<String> form = Arrays.asList("FormPGroup", "PGroup");
		List<String> newForm = Arrays.asList("FormNewPGroup", "NewPGroup");
		List<String> view = Arrays.asList("ViewPGroup", "OutputPGroup");

		presentationNames = Arrays.asList(form, newForm, view);
	}

	private static void createPresentationsForRecordType(String recordType) {
		for (List<String> presentation : presentationNames) {
			String message = dataCopier.copyTypeFromIdToNewId("presentationGroup",
					recordType + presentation.get(0), recordType + presentation.get(1));
			if (!message.startsWith("20")) {
				System.out.println(message);
			}

		}
	}

	private static void createFinder(String finderClassName, String url,
			String httpFactoryClassName) throws NoSuchMethodException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		constructFinder(finderClassName);
		finder.setUrlString(url + "recordType");
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
		HttpHandlerFactory httpFactory = createHttpHandlerFactory(httpFactoryClassName);
		finder.setHttpHandlerFactory(httpFactory);
	}

	private static HttpHandlerFactory createHttpHandlerFactory(String httpFactoryClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = Class.forName(httpFactoryClassName).getConstructor();
		return (HttpHandlerFactory) constructor.newInstance();
	}

	private static void createDataGroupCopier(String modifierClassName, String url,
			String httpFactoryClassName) throws NoSuchMethodException, ClassNotFoundException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		Class<?>[] cArg = new Class[2];
		cArg[0] = String.class;
		cArg[1] = HttpHandlerFactory.class;
		Method constructor = Class.forName(modifierClassName)
				.getMethod("usingURLAndHttpHandlerFactory", cArg);
		HttpHandlerFactory httpHandlerFactory = createHttpHandlerFactory(httpFactoryClassName);
		dataCopier = (DataCopier) constructor.invoke(null, url, httpHandlerFactory);
	}

}
