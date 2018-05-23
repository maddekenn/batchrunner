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

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataAttribute;
import se.uu.ub.cora.bookkeeper.data.DataGroup;

public class CountryFromDbToCoraStorageConverterTest {
	private Map<String, String> rowFromDb;

	@BeforeMethod
	public void beforeMethod() {
		rowFromDb = new HashMap<>();
		rowFromDb.put("alpha2code", "SE");
		rowFromDb.put("lastupdated", "2014-04-17 10:12:48.8");
	}

	@Test
	public void testConvertCountry() {
		CountryFromDbToCoraStorageConverter countryFromDbToCoraStorageConverter = new CountryFromDbToCoraStorageConverter();
		DataGroup countryItem = countryFromDbToCoraStorageConverter.convert(rowFromDb);
		assertEquals(countryItem.getNameInData(), "metadata");
		assertEquals(countryItem.getAttribute("type"), "collectionItem");

		DataGroup recordInfo = countryItem.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "seCountryItem");
		assertCorrectDataDivider(recordInfo);

		assertEquals(countryItem.getFirstAtomicValueWithNameInData("nameInData"), "SE");
	}

	private void assertCorrectDataDivider(DataGroup recordInfo) {
		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		String linkedRecordId = dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId");
		assertEquals(linkedRecordId, "bibsys");
		String linkedRecordType = dataDivider.getFirstAtomicValueWithNameInData("linkedRecordType");
		assertEquals(linkedRecordType, "system");
	}

	@Test
	public void testConvertCountryExtraDataOnlyIso2() {
		CountryFromDbToCoraStorageConverter countryFromDbToCoraStorageConverter = new CountryFromDbToCoraStorageConverter();
		DataGroup countryItem = countryFromDbToCoraStorageConverter.convert(rowFromDb);

		DataGroup extraData = countryItem.getFirstGroupWithNameInData("extraData");
		assertCorrectExtraDataPartGroup(extraData, "iso31661Alpha2", "SE");
		assertEquals(extraData.getAllGroupsWithNameInData("extraDataPart").size(), 1);
	}

	private void assertCorrectExtraDataPartGroup(DataGroup extraData, String attribute,
			String value) {
		DataAttribute dataAttribute = DataAttribute.withNameInDataAndValue("type", attribute);
		List<DataGroup> extraParts = (List<DataGroup>) extraData
				.getAllGroupsWithNameInDataAndAttributes("extraDataPart", dataAttribute);
		DataGroup extraPart = extraParts.get(0);
		assertEquals(extraPart.getFirstAtomicValueWithNameInData("value"), value);
	}

	@Test
	public void testConvertCountryExtraDataAllValues() {
		rowFromDb.put("alpha3code", "SWE");
		rowFromDb.put("numericalcode", "752");
		rowFromDb.put("marccode", "sw");
		CountryFromDbToCoraStorageConverter countryFromDbToCoraStorageConverter = new CountryFromDbToCoraStorageConverter();
		DataGroup countryItem = countryFromDbToCoraStorageConverter.convert(rowFromDb);

		DataGroup extraData = countryItem.getFirstGroupWithNameInData("extraData");
		assertCorrectExtraDataPartGroup(extraData, "iso31661Alpha2", "SE");
		assertCorrectExtraDataPartGroup(extraData, "iso31661Alpha3", "SWE");
		assertCorrectExtraDataPartGroup(extraData, "numericalcode", "752");
		assertCorrectExtraDataPartGroup(extraData, "marccode", "sw");
		assertEquals(extraData.getAllGroupsWithNameInData("extraDataPart").size(), 4);
	}
}
