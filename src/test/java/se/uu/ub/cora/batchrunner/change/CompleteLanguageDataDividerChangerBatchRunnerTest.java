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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.FinderSpy;
import se.uu.ub.cora.client.CoraClientConfig;

public class CompleteLanguageDataDividerChangerBatchRunnerTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<CompleteLanguageDataDividerChangerBatchRunner> constructor = CompleteLanguageDataDividerChangerBatchRunner.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testInit() throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String args[] = new String[] { "someUserId", "someAppToken", "appTokenVerifierUrl",
				"http://localhost:8080/therest/rest/record/",
				"se.uu.ub.cora.batchrunner.change.DataUpdaterSpy",
				"se.uu.ub.cora.batchrunner.change.CoraClientFactorySpy",
				"se.uu.ub.cora.batchrunner.find.FinderSpy", "someNewDataDivider",
				"metadataItemCollection", "completeLanguageCollection" };

		CompleteLanguageDataDividerChangerBatchRunner.main(args);
		CoraClientFactorySpy coraClientFactory = (CoraClientFactorySpy) CompleteLanguageDataDividerChangerBatchRunner.coraClientFactory;

		assertTrue(coraClientFactory instanceof CoraClientFactorySpy);
		assertEquals(coraClientFactory.appTokenVerifierUrl, "appTokenVerifierUrl");
		assertEquals(coraClientFactory.baseUrl, "http://localhost:8080/therest/rest/record/");

		CoraClientConfig coraClientConfig = CompleteLanguageDataDividerChangerBatchRunner.coraClientConfig;
		assertEquals(coraClientConfig.userId, args[0]);
		assertEquals(coraClientConfig.appToken, args[1]);
		assertEquals(coraClientConfig.appTokenVerifierUrl, args[2]);
		assertEquals(coraClientConfig.coraUrl, args[3]);

	}

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "someUserId", "someAppToken", "appTokenVerifierUrl",
				"http://localhost:8080/therest/rest/record/",
				"se.uu.ub.cora.batchrunner.change.DataUpdaterSpy",
				"se.uu.ub.cora.batchrunner.change.CoraClientFactorySpy",
				"se.uu.ub.cora.batchrunner.find.FinderSpy", "someNewDataDivider",
				"metadataItemCollection", "completeLanguageCollection" };

		CompleteLanguageDataDividerChangerBatchRunner.main(args);

		FinderSpy finder = (FinderSpy) CompleteLanguageDataDividerChangerBatchRunner.finder;
		CoraClientConfig coraClientConfig = finder.coraClientConfig;
		assertEquals(coraClientConfig.userId, args[0]);
		assertEquals(coraClientConfig.appToken, args[1]);
		assertEquals(coraClientConfig.appTokenVerifierUrl, args[2]);
		assertEquals(coraClientConfig.coraUrl, args[3]);
		DataUpdaterSpy dataUpdater = (DataUpdaterSpy) CompleteLanguageDataDividerChangerBatchRunner.dataUpdater;
		CoraClientFactorySpy coraClientFactory = (CoraClientFactorySpy) CompleteLanguageDataDividerChangerBatchRunner.coraClientFactory;

		CoraClientSpy coraClientReadCollectionSpy = coraClientFactory.factoredClientSpies.get(0);

		assertEquals(coraClientReadCollectionSpy.recordType, "metadataItemCollection");
		assertEquals(coraClientReadCollectionSpy.recordId, "completeLanguageCollection");

		assertEquals(dataUpdater.types.size(), 2);
		assertEquals(dataUpdater.types.get(1), "genericCollectionItem");
		assertEquals(dataUpdater.types.get(0), "genericCollectionItem");

		assertEquals(dataUpdater.recordIds.size(), 2);
		assertEquals(dataUpdater.recordIds.get(0), "svItem");
		assertEquals(dataUpdater.recordIds.get(1), "enItem");

		assertEquals(dataUpdater.dataDividers.size(), 2);
		assertEquals(dataUpdater.dataDividers.get(0), "someNewDataDivider");

	}

}
