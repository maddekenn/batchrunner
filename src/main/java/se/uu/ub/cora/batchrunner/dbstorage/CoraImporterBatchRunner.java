/*
 * Copyright 2021 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
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

public class CoraImporterBatchRunner {

	protected static SqlConnectionProvider connectionProvider;
	protected static TableInserter tableInserter;
	protected static CoraClientFactory coraClientFactory;
	protected static CoraClientConfig coraClientConfig;
	protected static RecordFinder finder;

	private CoraImporterBatchRunner() {
	}

	public static void main(String[] args)
			throws NoSuchMethodException, SecurityException, ClassNotFoundException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		createCoraClientConfig(args);
		createCoraClientFactory(args);
		createConnectionProvider(args);

		createTableCreator(args);

		insertIntoTablesForAllRecordTypes();

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

		tableInserter = (TableInserter) constructor.invoke(null, connectionProvider);
	}

	private static void insertIntoTablesForAllRecordTypes() {
		CoraClient coraClient = coraClientFactory.factor(coraClientConfig.userId,
				coraClientConfig.appToken);
		List<ClientDataRecord> recordTypes = coraClient.readListAsDataRecords("recordType");
		List<String> recordTypeIds = createListWithImplementingRecordTypesIds(recordTypes);

		List<String> messages = tableInserter.insertIntoTables(recordTypeIds);
		for (String message : messages) {
			System.out.println(message);
		}
	}

	private static List<String> createListWithImplementingRecordTypesIds(
			List<ClientDataRecord> recordTypes) {
		List<String> recordTypeIds = new ArrayList<>();
		for (ClientDataRecord clientRecord : recordTypes) {
			ClientDataGroup clientDataGroup = clientRecord.getClientDataGroup();
			possiblyAddRecordTypeId(recordTypeIds, clientDataGroup);
		}
		return recordTypeIds;
	}

	private static void possiblyAddRecordTypeId(List<String> recordTypeIds,
			ClientDataGroup clientDataGroup) {
		if (recordTypeIsImplementing(clientDataGroup)) {
			String recordId = extractRecordId(clientDataGroup);
			recordTypeIds.add(recordId);
		}
	}

	private static boolean recordTypeIsImplementing(ClientDataGroup clientDataGroup) {
		return "false".equals(clientDataGroup.getFirstAtomicValueWithNameInData("abstract"));
	}

	private static String extractRecordId(ClientDataGroup clientDataGroup) {
		ClientDataGroup recordInfo = clientDataGroup.getFirstGroupWithNameInData("recordInfo");
		return recordInfo.getFirstAtomicValueWithNameInData("id");
	}

}
