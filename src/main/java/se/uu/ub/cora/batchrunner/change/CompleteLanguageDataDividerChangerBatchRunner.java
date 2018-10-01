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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.batchrunner.HttpHandlerHelper;
import se.uu.ub.cora.client.CoraClient;
import se.uu.ub.cora.client.CoraClientConfig;
import se.uu.ub.cora.client.CoraClientFactory;
import se.uu.ub.cora.client.CoraClientFactoryImp;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class CompleteLanguageDataDividerChangerBatchRunner {
	protected static DataUpdater dataUpdater;
	protected static CoraClientFactory coraClientFactory;
	protected static HttpHandlerFactory httpHandlerFactory;
	protected static CoraClientConfig coraClientConfig;
	static List<String> errors = new ArrayList<>();

	// private static List<List<String>> presentationNames;
	// protected static Finder finder;

	private CompleteLanguageDataDividerChangerBatchRunner() {
	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String url = args[3];
		String dataUpdaterClassName = args[4];
		String httpFactoryClassName = args[5];
		String finderClassName = args[6];
		String newDataDivider = args[7];

		coraClientConfig = createCoraClientConfig(args);
		coraClientFactory = CoraClientFactoryImp.usingAppTokenVerifierUrlAndBaseUrl(
				coraClientConfig.appTokenVerifierUrl, coraClientConfig.coraUrl);

		CoraClient coraClient = coraClientFactory.factor(coraClientConfig.userId,
				coraClientConfig.appToken);

		createHttpHandlerFactory(httpFactoryClassName);
		createDataUpdater(dataUpdaterClassName, url);

		HttpHandlerHelper httpHandlerHelper = HttpHandlerHelper.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		// String readRecord = httpHandlerHelper.readRecord("metadataItemCollection",
		// "completeLanguageCollection");
		String readRecord = coraClient.read("metadataItemCollection", "completeLanguageCollection");

		ClientDataRecord jsonAsClientDataRecord = ConverterHelper
				.getJsonAsClientDataRecord(readRecord);
		ClientDataGroup clientDataGroup = jsonAsClientDataRecord.getClientDataGroup();
		ClientDataGroup collectionItemReferences = clientDataGroup
				.getFirstGroupWithNameInData("collectionItemReferences");

		List<ClientDataGroup> refs = collectionItemReferences.getAllGroupsWithNameInData("ref");
		for (ClientDataGroup ref : refs) {
			String itemId = ref.getFirstAtomicValueWithNameInData("linkedRecordId");
			String itemType = ref.getFirstAtomicValueWithNameInData("linkedRecordType");
			String response = dataUpdater.updateDataDividerInRecordUsingTypeIdAndNewDivider(
					itemType, itemId, newDataDivider);
			System.out.println("recordId :" + itemId + " response" + response);
		}

		System.out.println("done ");
	}

	private static CoraClientConfig createCoraClientConfig(String[] args) {
		String userId = args[0];
		String appToken = args[1];
		String appTokenVerifierUrl = args[2];
		String coraUrl = args[3];
		return new CoraClientConfig(userId, appToken, appTokenVerifierUrl, coraUrl);
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
