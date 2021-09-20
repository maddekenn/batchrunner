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
import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.connection.SqlConnectionProvider;

public class SqlConnectionProviderSpy implements SqlConnectionProvider {

	public boolean connectionWasCalled = false;
	public boolean throwException = false;
	public ConnectionSpy factoredConnection;

	public List<ConnectionSpy> factoredConnections = new ArrayList<>();
	public String url;
	public String user;
	public String password;

	public SqlConnectionProviderSpy(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public SqlConnectionProviderSpy() {
		// TODO Auto-generated constructor stub
	}

	public static SqlConnectionProviderSpy usingUriAndUserAndPassword(String url, String user,
			String password) {
		return new SqlConnectionProviderSpy(url, user, password);
	}

	@Override
	public Connection getConnection() {
		factoredConnection = new ConnectionSpy();
		factoredConnections.add(factoredConnection);
		connectionWasCalled = true;
		if (throwException) {
			factoredConnection.throwException = true;
		}
		return factoredConnection;
	}

}