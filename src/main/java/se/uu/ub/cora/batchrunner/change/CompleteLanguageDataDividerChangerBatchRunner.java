/*
 * Copyright 2018 Uppsala University Library
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
package se.uu.ub.cora.batchrunner.change;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.batchrunner.find.Finder;
import se.uu.ub.cora.client.CoraClient;
import se.uu.ub.cora.client.CoraClientConfig;
import se.uu.ub.cora.client.CoraClientFactory;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public class CompleteLanguageDataDividerChangerBatchRunner {
	protected static Finder finder;
	protected static DataUpdater dataUpdater;
	protected static CoraClientFactory coraClientFactory;
	protected static CoraClientConfig coraClientConfig;
	static List<String> errors = new ArrayList<>();

	private CompleteLanguageDataDividerChangerBatchRunner() {
	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String url = args[3];
		String dataUpdaterClassName = args[4];
		String httpFactoryClassName = args[5];
		String finderClassName = args[6];
		String newDataDivider = args[7];

		createCoraClientConfig(args);
		createCoraClientFactory(httpFactoryClassName);
		createFinder(finderClassName);
		CoraClient coraClient = coraClientFactory.factor(coraClientConfig.userId,
				coraClientConfig.appToken);

		RecordIdentifier recordIdentifier = RecordIdentifier.usingTypeAndId(args[8], args[9]);
		List<RecordIdentifier> refs = finder.findRecordsUsingRecordIdentifier(recordIdentifier);

		String readRecord = coraClient.read("metadataItemCollection", "completeLanguageCollection");

		ClientDataRecord jsonAsClientDataRecord = ConverterHelper
				.getJsonAsClientDataRecord(readRecord);
		ClientDataGroup clientDataGroup = jsonAsClientDataRecord.getClientDataGroup();
		ClientDataGroup collectionItemReferences = clientDataGroup
				.getFirstGroupWithNameInData("collectionItemReferences");

		createDataUpdater(dataUpdaterClassName, url);
		// List<ClientDataGroup> refs =
		// collectionItemReferences.getAllGroupsWithNameInData("ref");
		for (RecordIdentifier ref : refs) {
			String itemId = ref.id;
			String itemType = ref.type;
			String response = dataUpdater.updateDataDividerInRecordUsingTypeIdAndNewDivider(
					itemType, itemId, newDataDivider);
			System.out.println("recordId :" + itemId + " response" + response);
		}

		System.out.println("done ");
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

	private static void createFinder(String finderClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Class<?>[] cArg = new Class[2];
		cArg[0] = CoraClientFactory.class;
		cArg[1] = CoraClientConfig.class;
		Method constructor = Class.forName(finderClassName)
				.getMethod("usingCoraClientFactoryAndClientConfig", cArg);
		finder = (Finder) constructor.invoke(null, coraClientFactory, coraClientConfig);
	}

	private static void createDataUpdater(String updaterClassName, String url)
			throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException,
			InvocationTargetException {
		Class<?>[] cArg = new Class[2];
		cArg[0] = CoraClientFactory.class;
		cArg[1] = CoraClientConfig.class;
		Method constructor = Class.forName(updaterClassName)
				.getMethod("usingCoraClientFactoryAndClientConfig", cArg);
		dataUpdater = (DataUpdater) constructor.invoke(null, coraClientFactory, coraClientConfig);
	}

}
