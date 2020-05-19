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
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataRecordConverter;
import se.uu.ub.cora.json.parser.JsonParser;

/**
 * JsonToClientData is a helper class for parsing json and converting it to ClientData. A
 * {@link JsonParser} MUST be provided on instantiation
 */
public interface JsonToClientData {

	/**
	 * Converts a json string to a {@link ClientDataList}, containing a list of {@ClientDataRecord}
	 * 
	 * @param jsonToDataRecordConverter,
	 *            a {@link JsonToDataRecordConverter} to use for converting each record in the list
	 * 
	 * @param jsonListToConvert,
	 *            a String containing the json to convert
	 */
	ClientDataList getJsonStringAsClientDataRecordList(
			JsonToDataRecordConverter jsonToDataRecordConverter, String jsonListToConvert);

}
