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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.CoraClientSpy;

public class CoraImporterBatchRunnerTest {

	private String args[] = new String[] { "someUserId", "someAppToken", "appTokenVerifierUrl",
			"http://localhost:8080/therest/rest/record/",
			"se.uu.ub.cora.batchrunner.dbstorage.SqlConnectionProviderSpy",
			"jdbc:postgresql://diva-cora:544545/diva", "someUser", "somePassword",
			"se.uu.ub.cora.batchrunner.dbstorage.TableInserterSpy",
			"se.uu.ub.cora.batchrunner.CoraClientFactorySpy" };

	// "jdbc:postgresql://diva-cora-docker-postgresql:5432/diva", "diva", "diva")
	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<CoraImporterBatchRunner> constructor = CoraImporterBatchRunner.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testConncectionProviderCreatedCorrectly()
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {

		CoraImporterBatchRunner.main(args);

		SqlConnectionProviderSpy connectionProvider = (SqlConnectionProviderSpy) CoraImporterBatchRunner.connectionProvider;
		assertEquals(connectionProvider.url, args[5]);
		assertEquals(connectionProvider.user, args[6]);
		assertEquals(connectionProvider.password, args[7]);

	}

	@Test
	public void testCoraDataInserterCreatedCorrectly()
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {

		CoraImporterBatchRunner.main(args);

		TableInserterSpy tableInserter = (TableInserterSpy) CoraImporterBatchRunner.tableInserter;
		assertEquals(tableInserter.sqlConnectionProvider,
				CoraImporterBatchRunner.connectionProvider);
	}

	@Test
	public void testCoraClientCreatedCorrectly()
			throws NoSuchMethodException, SecurityException, ClassNotFoundException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		CoraImporterBatchRunner.main(args);
		CoraClientFactorySpy coraClientFactory = (CoraClientFactorySpy) CoraImporterBatchRunner.coraClientFactory;

		assertTrue(coraClientFactory instanceof CoraClientFactorySpy);
		assertEquals(coraClientFactory.appTokenVerifierUrl, "appTokenVerifierUrl");
		assertEquals(coraClientFactory.baseUrl, "http://localhost:8080/therest/rest/record/");

	}

	@Test
	public void testCreateCalledCorrectlyOnlyImplementingSentToCreator()
			throws NoSuchMethodException, SecurityException, ClassNotFoundException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		CoraImporterBatchRunner.main(args);
		CoraClientFactorySpy coraClientFactory = (CoraClientFactorySpy) CoraImporterBatchRunner.coraClientFactory;

		assertEquals(coraClientFactory.userId, "someUserId");
		assertEquals(coraClientFactory.appToken, "someAppToken");

		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);
		assertEquals(coraClientSpy.recordTypes.get(0), "recordType");

		TableInserterSpy tableCreator = (TableInserterSpy) CoraImporterBatchRunner.tableInserter;
		assertEquals(tableCreator.sentInRecordTypeIds.size(), 3);
		assertEquals(tableCreator.sentInRecordTypeIds.get(0), "spyDataGroup0Id");
		assertEquals(tableCreator.sentInRecordTypeIds.get(1), "spyDataGroup1Id");
		assertEquals(tableCreator.sentInRecordTypeIds.get(2), "spyDataGroup2Id");

	}
}
