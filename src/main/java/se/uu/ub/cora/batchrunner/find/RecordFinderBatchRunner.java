package se.uu.ub.cora.batchrunner.find;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import se.uu.ub.cora.client.CoraClientConfig;
import se.uu.ub.cora.client.CoraClientFactory;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public class RecordFinderBatchRunner {

	protected static RecordFinder finder;
	protected static CoraClientFactory coraClientFactory;
	protected static CoraClientConfig coraClientConfig;

	private RecordFinderBatchRunner() {

	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String finderClassName = args[4];
		String coraClientFactoryClassName = args[5];

		createCoraClientConfig(args);
		createCoraClientFactory(coraClientFactoryClassName);
		createFinder(finderClassName);
		RecordIdentifier recordIdentifier = RecordIdentifier.usingTypeAndId(args[6], args[7]);

		Collection<RecordIdentifier> records = finder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);

		Collection<RecordIdentifier> presentations = new ArrayList<>();

		for (RecordIdentifier identifier : records) {
			if (identifier.type.startsWith("presentation")) {
				presentations.add(identifier);
				System.out.println(identifier.type + " " + identifier.id);
			}
		}

		System.out.println("done");

	}

	private static void createFinder(String finderClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Class<?>[] cArg = new Class[2];
		cArg[0] = CoraClientFactory.class;
		cArg[1] = CoraClientConfig.class;
		Method constructor = Class.forName(finderClassName)
				.getMethod("usingCoraClientFactoryAndClientConfig", cArg);
		finder = (RecordFinder) constructor.invoke(null, coraClientFactory, coraClientConfig);
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
