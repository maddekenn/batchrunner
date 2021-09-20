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

public class RecordTypeTableCreatorBatchRunnerTest {

	// "jdbc:postgresql://diva-cora-docker-postgresql:5432/diva", "diva", "diva")
	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<RecordTypeTableCreatorBatchRunner> constructor = RecordTypeTableCreatorBatchRunner.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testConncectionProviderCreatedCorrectly()
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		String args[] = new String[] {
				"se.uu.ub.cora.batchrunner.dbstorage.SqlConnectionProviderSpy",
				"jdbc:postgresql://diva-cora:544545/diva", "someUser", "somePassword",
				"se.uu.ub.cora.batchrunner.dbstorage.TableCreatorSpy" };

		RecordTypeTableCreatorBatchRunner.main(args);

		SqlConnectionProviderSpy connectionProvider = (SqlConnectionProviderSpy) RecordTypeTableCreatorBatchRunner.connectionProvider;
		assertEquals(connectionProvider.url, args[1]);
		assertEquals(connectionProvider.user, args[2]);
		assertEquals(connectionProvider.password, args[3]);

	}

	@Test
	public void testTableCreatorCreatedCorrectly()
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		String args[] = new String[] {
				"se.uu.ub.cora.batchrunner.dbstorage.SqlConnectionProviderSpy",
				"jdbc:postgresql://diva-cora:544545/diva", "someUser", "somePassword",
				"se.uu.ub.cora.batchrunner.dbstorage.TableCreatorSpy" };

		RecordTypeTableCreatorBatchRunner.main(args);

		TableCreatorSpy tableCreator = (TableCreatorSpy) RecordTypeTableCreatorBatchRunner.tableCreator;
		assertEquals(tableCreator.sqlConnectionProvider,
				RecordTypeTableCreatorBatchRunner.connectionProvider);
	}

	@Test
	public void testCreateTables() {

	}

}
