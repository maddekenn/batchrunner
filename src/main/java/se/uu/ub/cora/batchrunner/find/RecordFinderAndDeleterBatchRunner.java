package se.uu.ub.cora.batchrunner.find;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import se.uu.ub.cora.batchrunner.ResultHolder;
import se.uu.ub.cora.batchrunner.change.RecordDeleter;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.javaclient.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public class RecordFinderAndDeleterBatchRunner {

	protected static RecordFinder finder;
	protected static CoraClientFactory coraClientFactory;
	protected static CoraClientConfig coraClientConfig;
	protected static RecordsSeparator separator;
	protected static RecordDeleter deleter;

	private RecordFinderAndDeleterBatchRunner() {
	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		setUpInstancesOfClasses(args);
		RecordIdentifier recordIdentifier = RecordIdentifier.usingTypeAndId(args[8], args[9]);

		List<RecordIdentifier> records = finder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);

		ResultHolder resultHolder = separator.sortOutRecordIdentifiers(records);

		List<RecordIdentifier> result = resultHolder.recordIdentifiers;

		printResultFromSeparator(result);
		printMessageFromSeparator(resultHolder.messages);

		List<String> deletedResult = deleter.deleteByRecordIdentifiers(result);
		printResultFromDeleter(deletedResult);

		System.out.println("done " + result.size());

	}

	private static void setUpInstancesOfClasses(String[] args) throws NoSuchMethodException,
			ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		String finderClassName = args[4];
		String separatorClassName = args[5];
		String deleterClassName = args[6];
		String coraClientFactoryClassName = args[7];

		createCoraClientConfig(args);
		createCoraClientFactory(coraClientFactoryClassName);
		createFinder(finderClassName);
		createSeparator(separatorClassName);
		createDeleter(deleterClassName);
	}

	private static void printResultFromDeleter(List<String> deletedResult) {
		System.out.println("deleted " + deletedResult.size());

		for (String message : deletedResult) {
			System.out.println(message);
		}
	}

	private static void printMessageFromSeparator(List<String> messages) {
		System.out.println("messages ");
		for (String message : messages) {
			System.out.println(message);
		}
	}

	private static void printResultFromSeparator(List<RecordIdentifier> result) {
		System.out.println("withoutincomingLinks " + result.size());
		for (RecordIdentifier identifier : result) {
			System.out.println(identifier.type + " " + identifier.id);
		}
	}

	private static void createFinder(String finderClassName) throws NoSuchMethodException,
			ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		Method constructor = setUpArgumentsForClass(finderClassName);
		finder = (RecordFinder) constructor.invoke(null, coraClientFactory, coraClientConfig);
	}

	private static Method setUpArgumentsForClass(String className)
			throws NoSuchMethodException, ClassNotFoundException {
		Class<?>[] cArg = new Class[2];
		cArg[0] = CoraClientFactory.class;
		cArg[1] = CoraClientConfig.class;
		return Class.forName(className).getMethod("usingCoraClientFactoryAndClientConfig", cArg);
	}

	private static void createSeparator(String separatorClassName) throws NoSuchMethodException,
			ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		Method constructor = setUpArgumentsForClass(separatorClassName);
		separator = (RecordsSeparator) constructor.invoke(null, coraClientFactory,
				coraClientConfig);
	}

	private static void createDeleter(String deleterClassName) throws NoSuchMethodException,
			ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		Method constructor = setUpArgumentsForClass(deleterClassName);

		deleter = (RecordDeleter) constructor.invoke(null, coraClientFactory, coraClientConfig);
	}

	private static void createCoraClientConfig(String[] args) {
		String userId = args[0];
		String appToken = args[1];
		String appTokenVerifierUrl = args[2];
		String coraUrl = args[3];
		coraClientConfig = new CoraClientConfig(userId, appToken, appTokenVerifierUrl, coraUrl);
	}

	private static void createCoraClientFactory(String httpFactoryClassName)
			throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException,
			InvocationTargetException {
		Class<?>[] cArg = new Class[2];
		cArg[0] = String.class;
		cArg[1] = String.class;
		Method constructor = Class.forName(httpFactoryClassName)
				.getMethod("usingAppTokenVerifierUrlAndBaseUrl", cArg);
		coraClientFactory = (CoraClientFactory) constructor.invoke(null,
				coraClientConfig.appTokenVerifierUrl, coraClientConfig.coraUrl);

	}
}
