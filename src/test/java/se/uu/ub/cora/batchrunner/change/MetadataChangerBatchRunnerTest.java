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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientFactorySpy;
import se.uu.ub.cora.batchrunner.CoraClientSpy;
import se.uu.ub.cora.batchrunner.find.RecordFinderSpy;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.javaclient.cora.CoraClientConfig;

public class MetadataChangerBatchRunnerTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<MetadataChangerBatchRunner> constructor = MetadataChangerBatchRunner.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	// "coraUser:4412982402853626" "b27039e1-593d-4d95-a69a-b33aa8c0924a"
	// "http://192.168.1.157:38082/apptokenverifier/" "http://192.168.1.157:38082/diva/rest/"
	// "se.uu.ub.cora.batchrunner.change.DataDividerUpdater"
	// "se.uu.ub.cora.javaclient.cora.CoraClientFactoryImp"
	// "se.uu.ub.cora.batchrunner.find.RecordRelatedElementsFinder" "diva"
	// "metadataCollectionVariable" "alternativeNameLanguageEnCollectionVar"
	// "engLanguageCollectionVar" "17cdc993-19dd-40b3-9de4-452ea449d1c3"

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String args[] = new String[] { "someUserId", "someAppToken", "appTokenVerifierUrl",
				"http://localhost:8080/therest/rest/record/",
				"se.uu.ub.cora.batchrunner.change.DataChangerFactorySpy",
				"se.uu.ub.cora.batchrunner.CoraClientFactorySpy",
				"se.uu.ub.cora.batchrunner.find.RecordFinderSpy", "metadataCollectionVariable",
				"completeLanguageCollectionVar", "newNameLanguageCollectionVar",
				"sdfstt-sfsd-40b3-9de4-452etrurtuac3" };

		MetadataChangerBatchRunner.main(args);

		CoraClientFactorySpy coraClientFactory = (CoraClientFactorySpy) MetadataChangerBatchRunner.coraClientFactory;

		assertCorrectParametersToCoraClientFactory(coraClientFactory);
		CoraClientSpy coraClientSpy = coraClientFactory.factoredClientSpies.get(0);

		RecordFinderSpy finder = (RecordFinderSpy) MetadataChangerBatchRunner.finder;
		assertCorrectConfigInFinder(args, finder);

		DataChangerFactorySpy changerFactory = (DataChangerFactorySpy) MetadataChangerBatchRunner.dataChangerFactory;
		List<DataChangerSpy> factoredDataChangers = changerFactory.factoredDataChangers;
		assertEquals(factoredDataChangers.size(), 2);

		List<ClientDataRecord> returnedResult = coraClientSpy.returnedListOfRecords;

		assertCorrectCallsToChangerFactory(finder, changerFactory, returnedResult);

		assertCorrectCallToChanger(factoredDataChangers, 0);
		assertCorrectCallToChanger(factoredDataChangers, 1);

		assertSame(coraClientSpy.updatedGroups.get(0),
				factoredDataChangers.get(0).returnedDataGroup);
		assertSame(coraClientSpy.updatedGroups.get(1),
				factoredDataChangers.get(1).returnedDataGroup);

	}

	private void assertCorrectCallToChanger(List<DataChangerSpy> factoredDataChangers, int index) {
		assertEquals(factoredDataChangers.get(index).recordType, "metadataCollectionVariable");
		assertEquals(factoredDataChangers.get(0).recordId, "completeLanguageCollectionVar");
		assertEquals(factoredDataChangers.get(0).newId, "newNameLanguageCollectionVar");
	}

	private void assertCorrectCallsToChangerFactory(RecordFinderSpy finder,
			DataChangerFactorySpy changerFactory, List<ClientDataRecord> returnedResult) {
		assertEquals(changerFactory.types.get(0), finder.recordIdentifiers.get(0).type);
		assertEquals(changerFactory.dataGroups.get(0), returnedResult.get(0).getClientDataGroup());
		assertEquals(changerFactory.types.get(1), finder.recordIdentifiers.get(1).type);
		assertEquals(changerFactory.dataGroups.get(1), returnedResult.get(1).getClientDataGroup());
	}

	private void assertCorrectParametersToCoraClientFactory(
			CoraClientFactorySpy coraClientFactory) {
		assertTrue(coraClientFactory instanceof CoraClientFactorySpy);
		assertEquals(coraClientFactory.appTokenVerifierUrl, "appTokenVerifierUrl");
		assertEquals(coraClientFactory.baseUrl, "http://localhost:8080/therest/rest/record/");
	}

	private void assertCorrectConfigInFinder(String[] args, RecordFinderSpy finder) {
		CoraClientConfig coraClientConfig = finder.coraClientConfig;
		assertEquals(coraClientConfig.userId, args[0]);
		assertEquals(coraClientConfig.appToken, args[1]);
		assertEquals(coraClientConfig.appTokenVerifierUrl, args[2]);
		assertEquals(coraClientConfig.coraUrl, args[3]);
	}

}
