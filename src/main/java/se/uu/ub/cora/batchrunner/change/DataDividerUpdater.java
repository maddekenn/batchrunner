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

import se.uu.ub.cora.client.CoraClient;
import se.uu.ub.cora.client.CoraClientConfig;
import se.uu.ub.cora.client.CoraClientFactory;
import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactory;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonWithoutActionLinksForLinksConverterFactory;

public class DataDividerUpdater implements DataUpdater {

	private String url;
	private CoraClientFactory coraClientFactory;
	private CoraClientConfig coraClientConfig;

	public DataDividerUpdater(CoraClientFactory coraClientFactory,
			CoraClientConfig coraClientConfig) {
		this.coraClientFactory = coraClientFactory;
		this.coraClientConfig = coraClientConfig;
	}

	public static DataDividerUpdater usingCoraClientFactoryAndClientConfig(
			CoraClientFactory coraClientFactory, CoraClientConfig coraClientConfig) {
		return new DataDividerUpdater(coraClientFactory, coraClientConfig);
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String updateDataDividerInRecordUsingTypeIdAndNewDivider(String type, String recordId,
			String newDataDivider) {
		CoraClient coraClient = coraClientFactory.factor(coraClientConfig.userId,
				coraClientConfig.appToken);
		String readRecord = coraClient.read(type, recordId);
		ClientDataGroup dataGroup = changeDataDividerInRecord(newDataDivider, readRecord);
		String newJson = getDataGroupAsJson(dataGroup);
		return coraClient.update(type, recordId, newJson);
	}

	private String getDataGroupAsJson(ClientDataGroup dataGroup) {
		DataToJsonConverterFactory jsonConverterFactory = new DataToJsonWithoutActionLinksForLinksConverterFactory();
		return ConverterHelper.getDataGroupAsJsonUsingConverterFactory(dataGroup,
				jsonConverterFactory);
	}

	private ClientDataGroup changeDataDividerInRecord(String newDataDivider, String readRecord) {
		ClientDataGroup dataGroup = getJsonAsClientDataGroup(readRecord);
		ClientDataGroup dataDividerGroup = extractDataDividerGroup(dataGroup);
		changeDataDividerInGroup(newDataDivider, dataDividerGroup);
		return dataGroup;
	}

	private ClientDataGroup getJsonAsClientDataGroup(String json) {
		ClientDataRecord pGroupClientDataRecord = ConverterHelper.getJsonAsClientDataRecord(json);
		return pGroupClientDataRecord.getClientDataGroup();
	}

	private ClientDataGroup extractDataDividerGroup(ClientDataGroup dataGroup) {
		ClientDataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		return recordInfo.getFirstGroupWithNameInData("dataDivider");
	}

	private void changeDataDividerInGroup(String newDataDivider, ClientDataGroup dataDividerGroup) {
		dataDividerGroup.removeFirstChildWithNameInData("linkedRecordId");
		dataDividerGroup.addChild(
				ClientDataAtomic.withNameInDataAndValue("linkedRecordId", newDataDivider));
	}

	@Override
	public CoraClientFactory getCoraClientFactory() {
		return coraClientFactory;
	}
}
