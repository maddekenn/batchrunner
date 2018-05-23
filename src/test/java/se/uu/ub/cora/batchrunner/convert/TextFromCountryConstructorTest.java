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

public class TextFromCountryConstructorTest {
	private Map<String, String> rowFromDb;

	@BeforeMethod
	public void beforeMethod() {
		rowFromDb = new HashMap<>();
		rowFromDb.put("alpha2code", "SE");
		rowFromDb.put("svText", "Sverige");

	}

	@Test
	public void testConstructTexts() {
		TextFromCountryConstructor textConstructor = new TextFromCountryConstructor();
		List<DataGroup> texts = textConstructor.constructFromDbRow(rowFromDb);
		assertEquals(texts.size(), 2);

		DataGroup text = texts.get(0);
		assertEquals(text.getNameInData(), "text");

		assertCorrectRecordInfo(text, "seCountryItemText");
		assertCorrectSwedishTextPart(text, "Sverige");
		assertNoEnglishTextPart(text);

		DataGroup defText = texts.get(1);
		assertEquals(defText.getNameInData(), "text");

		assertCorrectRecordInfo(defText, "seCountryItemDefText");
		assertCorrectSwedishTextPart(defText, "Sverige");
		assertNoEnglishTextPart(defText);
	}

	private void assertNoEnglishTextPart(DataGroup text) {
		DataAttribute type = DataAttribute.withNameInDataAndValue("type", "alternative");
		DataAttribute lang = DataAttribute.withNameInDataAndValue("lang", "en");
		List<DataGroup> textParts = (List<DataGroup>) text
				.getAllGroupsWithNameInDataAndAttributes("textPart", type, lang);
		assertEquals(textParts.size(), 0);
	}

	private void assertCorrectSwedishTextPart(DataGroup text, String textValue) {
		DataAttribute type = DataAttribute.withNameInDataAndValue("type", "default");
		DataAttribute lang = DataAttribute.withNameInDataAndValue("lang", "sv");
		List<DataGroup> textParts = (List<DataGroup>) text
				.getAllGroupsWithNameInDataAndAttributes("textPart", type, lang);
		DataGroup svTextPart = textParts.get(0);
		assertEquals(svTextPart.getFirstAtomicValueWithNameInData("text"), textValue);
	}

	private void assertCorrectRecordInfo(DataGroup text, String textId) {
		DataGroup recordInfo = text.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), textId);
		assertCorrectDataDivider(recordInfo);
	}

	private void assertCorrectDataDivider(DataGroup recordInfo) {
		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		String linkedRecordId = dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId");
		assertEquals(linkedRecordId, "bibsys");
		String linkedRecordType = dataDivider.getFirstAtomicValueWithNameInData("linkedRecordType");
		assertEquals(linkedRecordType, "system");
	}

	@Test
	public void testConstructTextsWithEnglishParts() {
		rowFromDb.put("enText", "Sweden");
		TextFromCountryConstructor textConstructor = new TextFromCountryConstructor();
		List<DataGroup> texts = textConstructor.constructFromDbRow(rowFromDb);
		assertEquals(texts.size(), 2);
		DataGroup text = texts.get(0);
		assertCorrectSwedishTextPart(text, "Sverige");
		assertCorrectEnglishTextPart(text, "Sweden");
	}

	private void assertCorrectEnglishTextPart(DataGroup text, String textValue) {
		DataAttribute type = DataAttribute.withNameInDataAndValue("type", "alternative");
		DataAttribute lang = DataAttribute.withNameInDataAndValue("lang", "en");
		List<DataGroup> textParts = (List<DataGroup>) text
				.getAllGroupsWithNameInDataAndAttributes("textPart", type, lang);
		DataGroup svTextPart = textParts.get(0);
		assertEquals(svTextPart.getFirstAtomicValueWithNameInData("text"), textValue);
	}

}
