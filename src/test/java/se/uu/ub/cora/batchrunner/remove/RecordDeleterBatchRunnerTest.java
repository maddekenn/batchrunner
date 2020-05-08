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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.find.RecordFinderSpy;
import se.uu.ub.cora.javaclient.CoraClientConfig;

public class RecordDeleterBatchRunnerTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<RecordDeleterBatchRunner> constructor = RecordDeleterBatchRunner.class
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
				"se.uu.ub.cora.batchrunner.remove.RecordDeleterSpy",
				"se.uu.ub.cora.batchrunner.CoraClientFactorySpy",
				"se.uu.ub.cora.batchrunner.find.RecordFinderSpy", "metadataItemCollection",
				"completeLanguageCollection" };

		RecordDeleterBatchRunner.main(args);
		CoraClientFactorySpy coraClientFactory = (CoraClientFactorySpy) RecordDeleterBatchRunner.coraClientFactory;

		assertTrue(coraClientFactory instanceof CoraClientFactorySpy);
		assertEquals(coraClientFactory.appTokenVerifierUrl, "appTokenVerifierUrl");
		assertEquals(coraClientFactory.baseUrl, "http://localhost:8080/therest/rest/record/");

		RecordFinderSpy finder = (RecordFinderSpy) RecordDeleterBatchRunner.finder;
		CoraClientConfig coraClientConfig = finder.coraClientConfig;
		assertEquals(coraClientConfig.userId, args[0]);
		assertEquals(coraClientConfig.appToken, args[1]);
		assertEquals(coraClientConfig.appTokenVerifierUrl, args[2]);
		assertEquals(coraClientConfig.coraUrl, args[3]);

		RecordDeleterSpy recordDeleter = (RecordDeleterSpy) RecordDeleterBatchRunner.recordDeleter;

		assertEquals(recordDeleter.types.size(), 2);
		assertEquals(recordDeleter.types.get(1), "genericCollectionItem");
		assertEquals(recordDeleter.types.get(0), "genericCollectionItem");

		assertEquals(recordDeleter.recordIds.size(), 2);
		assertEquals(recordDeleter.recordIds.get(0), "svItem");
		assertEquals(recordDeleter.recordIds.get(1), "enItem");

	}

	@Test
	public void testMainMethodErrorInUpdate() throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String args[] = new String[] { "someUserId", "someAppToken", "appTokenVerifierUrl",
				"http://localhost:8080/therest/rest/record/",
				"se.uu.ub.cora.batchrunner.remove.RecordDeleterSpy",
				"se.uu.ub.cora.batchrunner.CoraClientFactorySpy",
				"se.uu.ub.cora.batchrunner.find.RecordFinderSpy", "metadataItemCollection",
				"errorInItemCollection" };

		RecordDeleterBatchRunner.main(args);
		assertEquals(RecordDeleterBatchRunner.errors.get(0), "Error from DataUpdaterSpy");
	}

}
