/*
 * Copyright 2018, 2022 Uppsala University Library
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

import se.uu.ub.cora.batchrunner.find.RecordFinder;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.javaclient.cora.CoraClient;
import se.uu.ub.cora.javaclient.cora.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public class MetadataChangerBatchRunner {
	protected static RecordFinder finder;
	protected static DataUpdater dataUpdater;
	protected static CoraClientFactory coraClientFactory;
	protected static CoraClientConfig coraClientConfig;
	static List<String> errors = new ArrayList<>();
	protected static DataChangerFactory dataChangerFactory;

	private MetadataChangerBatchRunner() {
	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String dataChangerFactoryClassName = args[4];
		String httpFactoryClassName = args[5];
		String finderClassName = args[6];

		createCoraClientConfig(args);
		createCoraClientFactory(httpFactoryClassName);
		createFinder(finderClassName, args);

		RecordIdentifier recordIdentifier = RecordIdentifier.usingTypeAndId(args[7], args[8]);
		List<RecordIdentifier> resultFromFinder = finder
				.findRecordsRelatedToRecordIdentifier(recordIdentifier);
		constructDataChangerFactory(dataChangerFactoryClassName);

		CoraClient coraClient = coraClientFactory.factorUsingAuthToken(args[10]);
		for (RecordIdentifier relatedRecord : resultFromFinder) {

			ClientDataRecord clientDataRecord = coraClient.readAsDataRecord(relatedRecord.type,
					relatedRecord.id);
			DataChanger dataChanger = dataChangerFactory.factor(relatedRecord.type,
					clientDataRecord.getClientDataGroup());
			ClientDataGroup changedGroup = dataChanger.changeReference(args[7], args[8], args[9]);
			String updateMessage = coraClient.update(relatedRecord.type, relatedRecord.id,
					changedGroup);
			System.out.println("update message " + updateMessage);
			System.out.println("recordIdentifier2 " + relatedRecord.type + " " + relatedRecord.id);
		}

		System.out.println("done ");

	}

	private static void constructDataChangerFactory(String dataChangerFactoryClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = Class.forName(dataChangerFactoryClassName).getConstructor();
		dataChangerFactory = (DataChangerFactory) constructor.newInstance();
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

	private static void createFinder(String finderClassName, String[] args)
			throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException,
			InvocationTargetException {
		Class<?>[] cArg = new Class[3];
		cArg[0] = CoraClientFactory.class;
		cArg[1] = CoraClientConfig.class;
		cArg[2] = String.class;
		Method constructor = Class.forName(finderClassName)
				.getMethod("usingCoraClientFactoryAndClientConfig", cArg);
		finder = (RecordFinder) constructor.invoke(null, coraClientFactory, coraClientConfig,
				args[10]);
	}

}
