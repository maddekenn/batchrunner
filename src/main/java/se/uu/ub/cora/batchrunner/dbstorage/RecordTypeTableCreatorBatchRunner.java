package se.uu.ub.cora.batchrunner.dbstorage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.batchrunner.CoraClientConfig;
import se.uu.ub.cora.batchrunner.find.RecordFinder;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.connection.SqlConnectionProvider;
import se.uu.ub.cora.javaclient.cora.CoraClient;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public class RecordTypeTableCreatorBatchRunner {

	protected static SqlConnectionProvider connectionProvider;
	protected static TableCreator tableCreator;
	protected static CoraClientFactory coraClientFactory;
	protected static CoraClientConfig coraClientConfig;
	protected static RecordFinder finder;

	private RecordTypeTableCreatorBatchRunner() {
	}

	public static void main(String[] args)
			throws NoSuchMethodException, SecurityException, ClassNotFoundException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		createCoraClientConfig(args);
		createCoraClientFactory(args);
		createConnectionProvider(args);
		createTableCreator(args);

		createTablesForAllRecordTypes();

	}

	private static void createCoraClientConfig(String[] args) {
		String userId = args[0];
		String appToken = args[1];
		String appTokenVerifierUrl = args[2];
		String coraUrl = args[3];
		coraClientConfig = new CoraClientConfig(userId, appToken, appTokenVerifierUrl, coraUrl);
	}

	private static void createCoraClientFactory(String[] args) throws NoSuchMethodException,
			ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		String clientFactoryClassName = args[9];

		Class<?>[] cArg = new Class[2];
		cArg[0] = String.class;
		cArg[1] = String.class;
		Method constructor = Class.forName(clientFactoryClassName)
				.getMethod("usingAppTokenVerifierUrlAndBaseUrl", cArg);
		coraClientFactory = (CoraClientFactory) constructor.invoke(null,
				coraClientConfig.appTokenVerifierUrl, coraClientConfig.coraUrl);
	}

	private static void createConnectionProvider(String[] args) throws NoSuchMethodException,
			ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		String className = args[4];

		Class<?>[] cArg = new Class[3];
		cArg[0] = String.class;
		cArg[1] = String.class;
		cArg[2] = String.class;

		String uri = args[5];
		String user = args[6];
		String password = args[7];
		Method constructor = Class.forName(className).getMethod("usingUriAndUserAndPassword", cArg);

		connectionProvider = (SqlConnectionProvider) constructor.invoke(null, uri, user, password);
	}

	private static void createTableCreator(String[] args) throws NoSuchMethodException,
			ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		Class<?>[] cArg = new Class[1];
		cArg[0] = SqlConnectionProvider.class;

		Method constructor = Class.forName(args[8]).getMethod("usingConnectionProvider", cArg);

		tableCreator = (TableCreator) constructor.invoke(null, connectionProvider);
	}

	private static void createTablesForAllRecordTypes() {
		CoraClient coraClient = coraClientFactory.factor(coraClientConfig.userId,
				coraClientConfig.appToken);
		List<ClientDataRecord> recordTypes = coraClient.readListAsDataRecords("recordType");
		List<String> recordTypeIds = createListWithRecordTypesIds(recordTypes);

		tableCreator.createTables(recordTypeIds);
	}

	private static List<String> createListWithRecordTypesIds(List<ClientDataRecord> recordTypes) {
		List<String> recordTypeIds = new ArrayList<>();
		for (ClientDataRecord clientRecord : recordTypes) {
			String recordId = extractRecordId(clientRecord);
			recordTypeIds.add(recordId);
		}
		return recordTypeIds;
	}

	private static String extractRecordId(ClientDataRecord clientRecord) {
		ClientDataGroup clientDataGroup = clientRecord.getClientDataGroup();
		ClientDataGroup recordInfo = clientDataGroup.getFirstGroupWithNameInData("recordInfo");
		return recordInfo.getFirstAtomicValueWithNameInData("id");
	}

}
