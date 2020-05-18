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

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.DataRecord;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataRecordConverter;
import se.uu.ub.cora.json.parser.JsonObject;

public class JsonToDataRecordConverterSpy implements JsonToDataRecordConverter {

	public List<JsonObject> jsonObjects = new ArrayList<>();
	public List<ClientDataRecord> returnedRecords = new ArrayList<>();

	@Override
	public DataRecord toInstance(JsonObject jsonObject) {
		jsonObjects.add(jsonObject);
		ClientDataGroup dataGroup = ClientDataGroup.withNameInData("fromSpy");
		ClientDataRecord returnedRecord = ClientDataRecord.withClientDataGroup(dataGroup);
		returnedRecords.add(returnedRecord);
		return returnedRecord;
	}

}
