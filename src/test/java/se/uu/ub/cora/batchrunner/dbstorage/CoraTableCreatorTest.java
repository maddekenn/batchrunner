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

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.sqldatabase.SqlStorageException;

public class CoraTableCreatorTest {

	private SqlConnectionProviderSpy sqlConnectionProvider;

	@BeforeMethod
	public void setUp() {
		sqlConnectionProvider = new SqlConnectionProviderSpy();
	}

	@Test
	public void testCreateTables() {
		TableCreator tableCreator = CoraTableCreator.usingConnectionProvider(sqlConnectionProvider);

		List<String> tableNames = List.of("person", "organisation", "project");
		tableCreator.createTables(tableNames);

		assertTrue(sqlConnectionProvider.connectionWasCalled);
		List<ConnectionSpy> factoredConnections = sqlConnectionProvider.factoredConnections;
		assertEquals(factoredConnections.size(), 3);

		assertCorrectFactoredAndCalledConnection(factoredConnections, 0, "person");
		assertCorrectFactoredAndCalledConnection(factoredConnections, 1, "organisation");
		assertCorrectFactoredAndCalledConnection(factoredConnections, 2, "project");

	}

	private void assertCorrectFactoredAndCalledConnection(List<ConnectionSpy> factoredConnections,
			int index, String tableName) {
		ConnectionSpy connectionSpy = factoredConnections.get(index);
		assertEquals(connectionSpy.sql, "CREATE TABLE IF NOT EXISTS " + tableName
				+ " (id varchar, record jsonb, PRIMARY KEY(id));");

		PreparedStatementSpy preparedStatementSpy = (PreparedStatementSpy) connectionSpy.preparedStatementSpy;
		assertTrue(preparedStatementSpy.executeUpdateWasCalled);
	}

	@Test(expectedExceptions = SqlStorageException.class)
	public void testWhenError() {
		sqlConnectionProvider.throwException = true;
		TableCreator tableCreator = CoraTableCreator.usingConnectionProvider(sqlConnectionProvider);
		tableCreator.createTables(List.of("person"));
	}

}
