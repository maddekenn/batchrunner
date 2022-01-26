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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.uu.ub.cora.connection.SqlConnectionProvider;

public class TableCreatorSpy implements TableCreator {

	public SqlConnectionProvider sqlConnectionProvider;
	public List<String> sentInTableNames = new ArrayList<>();

	public TableCreatorSpy(SqlConnectionProvider sqlConnectionProvider) {
		this.sqlConnectionProvider = sqlConnectionProvider;
	}

	public static TableCreatorSpy usingConnectionProvider(
			SqlConnectionProvider sqlConnectionProvider) {
		return new TableCreatorSpy(sqlConnectionProvider);
	}

	@Override
	public List<String> createTables(List<String> tableNames) {
		sentInTableNames.addAll(tableNames);
		return Collections.emptyList();
	}

}
