/*
 * Copyright 2020 Uppsala University Library
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
package se.uu.ub.cora.batchrunner.index;

import java.util.List;

import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.javaclient.CoraClientConfig;
import se.uu.ub.cora.javaclient.cora.CoraClient;
import se.uu.ub.cora.javaclient.cora.CoraClientException;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public class DataIndexerImp implements DataIndexer {
	private CoraClientFactory coraClientFactory;
	private CoraClientConfig coraClientConfig;
	private CoraClient coraClient;

	public DataIndexerImp(CoraClient coraClient) {
		this.coraClient = coraClient;
	}

	@Override
	public void indexDataWithRecordType(String recordType) {

		List<ClientDataRecord> list = coraClient.readListAsDataRecords(recordType);
		for (ClientDataRecord clientDataRecord : list) {
			try {
				String indexData = coraClient.indexData(clientDataRecord);
				System.out.println(indexData);
			} catch (CoraClientException exception) {
				System.out.println(exception.getMessage());
			}
		}
	}

	public CoraClientFactory getCoraClientFactory() {
		return coraClientFactory;
	}

	public CoraClientConfig getCoraClientConfig() {
		return coraClientConfig;
	}

	public static DataIndexerImp usingCoraClient(CoraClient coraClient) {
		return new DataIndexerImp(coraClient);
	}

	public CoraClient getCoraClient() {
		return coraClient;
	}
}
