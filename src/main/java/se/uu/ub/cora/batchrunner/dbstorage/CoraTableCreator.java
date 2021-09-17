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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import se.uu.ub.cora.connection.SqlConnectionProvider;
import se.uu.ub.cora.sqldatabase.SqlStorageException;

public class CoraTableCreator implements TableCreator {

	private SqlConnectionProvider sqlConnectionProvider;

	public CoraTableCreator(SqlConnectionProvider sqlConnectionProvider) {
		this.sqlConnectionProvider = sqlConnectionProvider;
	}

	@Override
	public void createTables(List<String> tableNames) {
		for (String tableName : tableNames) {

			String sql = "create table " + tableName
					+ " (id varchar, record jsonb, PRIMARY KEY(id));";
			try (Connection connection = sqlConnectionProvider.getConnection();
					PreparedStatement prepareStatement = connection.prepareStatement(sql);) {
				prepareStatement.executeUpdate();
			} catch (SQLException e) {
				throw SqlStorageException.withMessageAndException(
						"Error executing prepared statement: " + e.getMessage(), e);
			}
		}
	}

}
