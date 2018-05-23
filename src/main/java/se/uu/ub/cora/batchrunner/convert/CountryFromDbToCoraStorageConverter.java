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
package se.uu.ub.cora.batchrunner.convert;

import java.util.Map;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;

public class CountryFromDbToCoraStorageConverter {

	private Map<String, String> rowFromDb;

	public DataGroup convert(Map<String, String> rowFromDb) {
		this.rowFromDb = rowFromDb;
		DataGroup item = createDataGroupWithAttribute();
		addChildrenToDataGroup(item);
		return item;
	}

	private DataGroup createDataGroupWithAttribute() {
		DataGroup item = DataGroup.withNameInData("metadata");
		item.addAttributeByIdWithValue("type", "collectionItem");
		return item;
	}

	private void addChildrenToDataGroup(DataGroup item) {
		String alpha2 = rowFromDb.get("alpha2code");
		addRecordInfo(alpha2, item);
		addNameInData(alpha2, item);
		addExtraData(alpha2, item);
	}

	private void addRecordInfo(String alpha2code, DataGroup item) {
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		addId(alpha2code, recordInfo);
		addDataDivider(recordInfo);
		item.addChild(recordInfo);
	}

	private void addId(String alpha2code, DataGroup recordInfo) {
		String id = alpha2code.toLowerCase() + "CountryItem";
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", id));
	}

	private void addDataDivider(DataGroup recordInfo) {
		DataGroup dataDivider = DataGroup.withNameInData("dataDivider");
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "bibsys"));
		recordInfo.addChild(dataDivider);
	}

	private void addNameInData(String alpha2, DataGroup item) {
		item.addChild(DataAtomic.withNameInDataAndValue("nameInData", alpha2));
	}

	private void addExtraData(String value, DataGroup item) {
		DataGroup extraData = DataGroup.withNameInData("extraData");
		DataGroup iso2ExtraDataPart = createExtraDataPartWithAttributeAndValue("iso31661Alpha2",
				value);
		extraData.addChild(iso2ExtraDataPart);
		possiblyAddExtraDataPartWithKeyAndAttribute("alpha3code", "iso31661Alpha3", extraData);
		possiblyAddExtraDataPartWithKeyAndAttribute("numericalcode", "numericalcode", extraData);
		possiblyAddExtraDataPartWithKeyAndAttribute("marccode", "marccode", extraData);
		item.addChild(extraData);
	}

	private void possiblyAddExtraDataPartWithKeyAndAttribute(String key, String attribute,
			DataGroup extraData) {
		if (valueExistsForKey(key)) {
			addExtraDataPartWithAttributeAndValue(key, attribute, extraData);
		}
	}

	private void addExtraDataPartWithAttributeAndValue(String key, String attribute,
			DataGroup extraData) {
		String alpha3 = rowFromDb.get(key);
		DataGroup iso3ExtraDataPart = createExtraDataPartWithAttributeAndValue(attribute, alpha3);
		extraData.addChild(iso3ExtraDataPart);
	}

	private boolean valueExistsForKey(String key) {
		return rowFromDb.containsKey(key);
	}

	private DataGroup createExtraDataPartWithAttributeAndValue(String attribute, String value) {
		DataGroup extraDataPart = DataGroup.withNameInData("extraDataPart");
		extraDataPart.addAttributeByIdWithValue("type", attribute);
		extraDataPart.addChild(DataAtomic.withNameInDataAndValue("value", value));
		return extraDataPart;
	}

}
