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

import se.uu.ub.cora.clientdata.ClientData;
import se.uu.ub.cora.clientdata.ClientDataList;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverter;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactory;
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonString;
import se.uu.ub.cora.json.parser.JsonValue;

public class JsonToClientDataImp implements JsonToClientData {

	private JsonParser jsonParser;
	private JsonToDataConverterFactory converterFactory;

	public JsonToClientDataImp(JsonParser jsonParser, JsonToDataConverterFactory converterFactory) {
		this.jsonParser = jsonParser;
		this.converterFactory = converterFactory;
	}

	@Override
	public ClientDataList getJsonStringAsClientDataRecordList(String jsonListToConvert) {
		JsonObject listObject = getListAsObject(jsonParser, jsonListToConvert);
		ClientDataList dataList = createClientDataList(listObject);

		convertAndAddRecordsToDataList(converterFactory, listObject, dataList);
		return dataList;
	}

	private void convertAndAddRecordsToDataList(JsonToDataConverterFactory converterFactory,
			JsonObject listObject, ClientDataList dataList) {
		JsonArray valueAsJsonArray = listObject.getValueAsJsonArray("data");
		for (JsonValue value : valueAsJsonArray) {
			convertAndAddRecordToDataList(converterFactory, dataList, (JsonObject) value);
		}
	}

	private void convertAndAddRecordToDataList(JsonToDataConverterFactory converterFactory,
			ClientDataList dataList, JsonObject record) {
		JsonToDataConverter converter = converterFactory.createForJsonObject(record);
		ClientData dataElement = (ClientData) converter.toInstance();
		dataList.addData(dataElement);
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

	public JsonToDataConverterFactory getJsonToDataConverterFactory() {
		return converterFactory;
	}

}
