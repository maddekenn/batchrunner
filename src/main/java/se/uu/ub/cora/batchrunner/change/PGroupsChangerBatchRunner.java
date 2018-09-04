package se.uu.ub.cora.batchrunner.change;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.batchrunner.find.Finder;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class PGroupsChangerBatchRunner {

	protected static Finder finder;

	protected static Modifier modifier;

	private PGroupsChangerBatchRunner() {
	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String finderClassName = args[0];
		String url = args[1];
		String httpFactoryClassName = args[2];
		String modifierClassName = args[3];

		createFinder(finderClassName, url + "recordType", httpFactoryClassName);

		Collection<String> records = finder.findRecords();

		List<String> errors = new ArrayList<>();
		createModifier(modifierClassName, url, httpFactoryClassName);
		// for (String recordType : records) {
		// System.out.println("starting recordtype " + recordType);
		// List<String> errorMessages = modifier.modifyData(recordType);
		// errors.addAll(errorMessages);
		// }
		List<String> errorMessages = modifier.modifyData(records.iterator().next());

		System.out.println("done");
		errors.forEach(System.out::println);
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
		HttpHandlerFactory httpFactory = createHttpHandlerFactory(httpFactoryClassName);
		finder.setHttpHandlerFactory(httpFactory);
	}

	private static HttpHandlerFactory createHttpHandlerFactory(String httpFactoryClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = Class.forName(httpFactoryClassName).getConstructor();
		return (HttpHandlerFactory) constructor.newInstance();
	}

	private static void createModifier(String modifierClassName, String url,
			String httpFactoryClassName) throws NoSuchMethodException, ClassNotFoundException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		Class<?>[] cArg = new Class[2];
		cArg[0] = String.class;
		cArg[1] = HttpHandlerFactory.class;
		Method constructor = Class.forName(modifierClassName)
				.getMethod("usingURLAndHttpHandlerFactory", cArg);
		HttpHandlerFactory httpHandlerFactory = createHttpHandlerFactory(httpFactoryClassName);
		modifier = (Modifier) constructor.invoke(null, url, httpHandlerFactory);
	}

}
