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

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.batchrunner.JsonToClientDataConverter;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactory;
import se.uu.ub.cora.json.parser.JsonObject;

public class JsonToClientDataConverterSpy implements JsonToClientDataConverter {

	public List<String> jsonStringsToConvert = new ArrayList<>();
	public ClientDataRecord returnedRecord;

	@Override
	public ClientDataRecord getJsonObjectAsClientDataRecord(JsonObject jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientDataRecord getJsonStringAsClientDataRecord(String jsonToConvert) {
		jsonStringsToConvert.add(jsonToConvert);
		ClientDataGroup clientDataGroup = ClientDataGroup.withNameInData("someNameInData");

		returnedRecord = ClientDataRecord.withClientDataGroup(clientDataGroup);
		return returnedRecord;
	}

	@Override
	public String getDataGroupAsJsonUsingConverterFactory(ClientDataGroup dataGroup,
			DataToJsonConverterFactory jsonConverterFactory) {
		// TODO Auto-generated method stub
		return null;
	}

}
