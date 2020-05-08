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
import java.util.List;

import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.find.RecordFinderSpy;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.javaclient.CoraClientConfig;

public class DataDividerChangerBatchRunnerTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<DataDividerChangerBatchRunner> constructor = DataDividerChangerBatchRunner.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String args[] = new String[] { "someUserId", "someAppToken", "appTokenVerifierUrl",
				"http://localhost:8080/therest/rest/record/",
				"se.uu.ub.cora.batchrunner.change.DataUpdaterSpy",
				"se.uu.ub.cora.batchrunner.CoraClientFactorySpy",
				"se.uu.ub.cora.batchrunner.find.RecordFinderSpy", "someNewDataDivider",
				"metadataItemCollection", "completeLanguageCollection" };

		DataDividerChangerBatchRunner.main(args);
		CoraClientFactorySpy coraClientFactory = (CoraClientFactorySpy) DataDividerChangerBatchRunner.coraClientFactory;

		assertTrue(coraClientFactory instanceof CoraClientFactorySpy);
		assertEquals(coraClientFactory.appTokenVerifierUrl, "appTokenVerifierUrl");
		assertEquals(coraClientFactory.baseUrl, "http://localhost:8080/therest/rest/record/");

		RecordFinderSpy finder = (RecordFinderSpy) DataDividerChangerBatchRunner.finder;
		CoraClientConfig coraClientConfig = finder.coraClientConfig;
		assertEquals(coraClientConfig.userId, args[0]);
		assertEquals(coraClientConfig.appToken, args[1]);
		assertEquals(coraClientConfig.appTokenVerifierUrl, args[2]);
		assertEquals(coraClientConfig.coraUrl, args[3]);

		DataUpdaterSpy dataUpdater = (DataUpdaterSpy) DataDividerChangerBatchRunner.dataUpdater;

		List<RecordIdentifier> identifiersFromFinder = finder.recordIdentifiers;
		List<RecordIdentifier> identifiersFromUpdater = dataUpdater.recordIdentifiers;
		for (int i = 0; i < identifiersFromFinder.size(); i++) {
			assertEquals(identifiersFromFinder.get(i), identifiersFromUpdater.get(i));
		}

		assertEquals(dataUpdater.dataDividers.size(), 1);
		assertEquals(dataUpdater.dataDividers.get(0), "someNewDataDivider");

	}

}
