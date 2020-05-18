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
package se.uu.ub.cora.batchrunner;

import se.uu.ub.cora.clientdata.ClientDataList;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataRecordConverter;
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonString;
import se.uu.ub.cora.json.parser.JsonValue;

public class JsonToClientDataImp implements JsonToClientData {

	private JsonParser jsonParser;
	private JsonToDataRecordConverter jsonToDataRecordConverter;

	public static JsonToClientDataImp usingJsonParser(JsonParser jsonParser) {
		return new JsonToClientDataImp(jsonParser);
	}

	private JsonToClientDataImp(JsonParser jsonParser) {
		this.jsonParser = jsonParser;
	}

	@Override
	public ClientDataList getJsonStringAsClientDataRecordList(
			JsonToDataRecordConverter jsonToDataRecordConverter, String jsonListToConvert) {
		this.jsonToDataRecordConverter = jsonToDataRecordConverter;
		JsonObject listObject = getListAsObject(jsonParser, jsonListToConvert);
		ClientDataList dataList = createClientDataList(listObject);

		convertAndAddRecordsToDataList(listObject, dataList);
		return dataList;
	}

	private void convertAndAddRecordsToDataList(JsonObject listObject, ClientDataList dataList) {
		JsonArray valueAsJsonArray = listObject.getValueAsJsonArray("data");
		for (JsonValue value : valueAsJsonArray) {
			convertAndAddRecordToDataList(dataList, (JsonObject) value);
		}
	}

	private void convertAndAddRecordToDataList(ClientDataList dataList, JsonObject record) {
		ClientDataRecord dataRecord = (ClientDataRecord) jsonToDataRecordConverter
				.toInstance(record);
		dataList.addData(dataRecord);
	}

	private JsonObject getListAsObject(JsonParser jsonParser, String jsonListToConvert) {
		JsonObject jsonObject = jsonParser.parseStringAsObject(jsonListToConvert);
		return (JsonObject) jsonObject.getValue("dataList");
	}

	private ClientDataList createClientDataList(JsonObject listObject) {
		String containDataOfType = getStringValueFromList("containDataOfType", listObject);

		ClientDataList dataList = ClientDataList.withContainDataOfType(containDataOfType);
		dataList.setFromNo(getStringValueFromList("fromNo", listObject));
		dataList.setToNo(((JsonString) listObject.getValue("toNo")).getStringValue());
		dataList.setTotalNo(((JsonString) listObject.getValue("totalNo")).getStringValue());
		return dataList;
	}

	private String getStringValueFromList(String valueToGet, JsonObject listObject) {
		return ((JsonString) listObject.getValue(valueToGet)).getStringValue();
	}

	public JsonParser getJsonParser() {
		return jsonParser;
	}

	public JsonToDataRecordConverter getjsonToDataRecordConverter() {
		return jsonToDataRecordConverter;
	}

}
