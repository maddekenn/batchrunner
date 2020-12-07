/*
 * Copyright 2020 Uppsala University Library
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
package se.uu.ub.cora.batchrunner.index;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.javaclient.cora.CoraClient;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public class IndexerBatchRunner {
	protected static DataIndexer dataIndexer;
	protected static CoraClientFactory coraClientFactory;
	static List<String> errors = new ArrayList<>();

	private IndexerBatchRunner() {
	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		String clientFactoryClassName = args[3];
		String indexerClassName = args[4];
		String recordType = args[5];

		createCoraClientFactory(args, clientFactoryClassName);

		String authToken = args[0];
		CoraClient coraClient = coraClientFactory.factorUsingAuthToken(authToken);
		createDataIndexer(indexerClassName, coraClient);

		System.out.println("starting ");
		dataIndexer.indexDataWithRecordType(recordType);
		System.out.println("done ");
	}

	private static void createCoraClientFactory(String[] args, String clientFactoryClassName)
			throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException,
			InvocationTargetException {
		Class<?>[] cArg = new Class[2];
		cArg[0] = String.class;
		cArg[1] = String.class;
		String appTokenVerifierUrl = args[1];
		String coraUrl = args[2];
		Method constructor = Class.forName(clientFactoryClassName)
				.getMethod("usingAppTokenVerifierUrlAndBaseUrl", cArg);
		coraClientFactory = (CoraClientFactory) constructor.invoke(null, appTokenVerifierUrl,
				coraUrl);

	}

	private static void createDataIndexer(String indexerClassName, CoraClient coraClient)
			throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException,
			InvocationTargetException {
		Class<?>[] cArg = new Class[1];
		cArg[0] = CoraClient.class;

		Method constructor = Class.forName(indexerClassName).getMethod("usingCoraClient", cArg);
		dataIndexer = (DataIndexer) constructor.invoke(null, coraClient);
	}

}
