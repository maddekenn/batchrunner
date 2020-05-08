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
package se.uu.ub.cora.batchrunner.remove;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.batchrunner.change.RecordDeleter;
import se.uu.ub.cora.batchrunner.find.RecordFinder;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.javaclient.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClientException;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public class RecordDeleterBatchRunner {
	protected static RecordFinder finder;
	protected static RecordDeleter recordDeleter;
	protected static CoraClientFactory coraClientFactory;
	protected static CoraClientConfig coraClientConfig;
	static List<String> errors = new ArrayList<>();

	private RecordDeleterBatchRunner() {
	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String recordDeleterClassName = args[4];
		String httpFactoryClassName = args[5];
		String finderClassName = args[6];

		createCoraClientConfig(args);
		createCoraClientFactory(httpFactoryClassName);
		createFinder(finderClassName);

		RecordIdentifier recordIdentifier = RecordIdentifier.usingTypeAndId(args[7], args[8]);
		List<RecordIdentifier> refs = finder.findRecordsRelatedToRecordIdentifier(recordIdentifier);

		createRecordDeleter(recordDeleterClassName);
		for (RecordIdentifier ref : refs) {
			tryToDeleteRecord(ref.type, ref.id);
		}
		errors.forEach(System.out::println);
		System.out.println("done ");
	}

	private static void tryToDeleteRecord(String itemType, String itemId) {
		try {
			String response = recordDeleter.deleteRecordByTypeAndId(itemType, itemId);
			System.out.println("recordId :" + itemId + " response" + response);
		} catch (CoraClientException e) {
			errors.add(e.getMessage());
		}
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
		finder = (RecordFinder) constructor.invoke(null, coraClientFactory, coraClientConfig);
	}

	private static void createRecordDeleter(String deleterClassName) throws NoSuchMethodException,
			ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		Class<?>[] cArg = new Class[2];
		cArg[0] = CoraClientFactory.class;
		cArg[1] = CoraClientConfig.class;
		Method constructor = Class.forName(deleterClassName)
				.getMethod("usingCoraClientFactoryAndClientConfig", cArg);
		recordDeleter = (RecordDeleter) constructor.invoke(null, coraClientFactory,
				coraClientConfig);
	}

}
