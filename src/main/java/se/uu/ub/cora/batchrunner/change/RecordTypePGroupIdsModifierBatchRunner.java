package se.uu.ub.cora.batchrunner.change;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.batchrunner.find.Finder;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class RecordTypePGroupIdsModifierBatchRunner {

	static List<String> errors = new ArrayList<>();
	protected static Finder finder;
	protected static Modifier modifier;

	private RecordTypePGroupIdsModifierBatchRunner() {
	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String url = args[0];
		String httpFactoryClassName = args[1];
		String finderClassName = args[2];
		String modifierClassName = args[3];

		createFinder(finderClassName, url, httpFactoryClassName);
		createModifier(modifierClassName, url, httpFactoryClassName);
		Collection<String> records = finder.findRecords();
		for (String recordType : records) {
			List<String> errorMessages = modifier.modifyData(recordType);
			for (String error : errorMessages) {
				System.out.println(error);
			}
		}

		System.out.println("done ");
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
