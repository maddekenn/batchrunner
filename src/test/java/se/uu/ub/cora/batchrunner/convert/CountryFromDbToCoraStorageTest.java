package se.uu.ub.cora.batchrunner.convert;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactory;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactoryImp;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataGroupConverter;
import se.uu.ub.cora.json.builder.JsonBuilderFactory;
import se.uu.ub.cora.json.builder.org.OrgJsonBuilderFactoryAdapter;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

public class CountryFromDbToCoraStorageTest {
	List<Map<String, String>> rowsFromDb = new ArrayList<Map<String, String>>();
	CoraClientSpy coraClient;
	private JsonBuilderFactory jsonFactory;
	private CountryFromDbToCoraStorage toCoraStorage;

	@BeforeMethod
	public void beforeMethod() {
		rowsFromDb = new ArrayList<Map<String, String>>();
		Map<String, String> rowFromDb = new HashMap<>();
		rowFromDb.put("alpha2code", "SE");
		rowFromDb.put("svText", "Sverige");

		rowsFromDb.add(rowFromDb);

		coraClient = new CoraClientSpy();
		jsonFactory = new OrgJsonBuilderFactoryAdapter();
		toCoraStorage = CountryFromDbToCoraStorage.usingCoraClientAndJsonFactory(coraClient,
				jsonFactory);

	}

	@Test
	public void testConvertCountryOneRow() {
		toCoraStorage.importFromRowsFromDb(rowsFromDb);
		String firstText = coraClient.createdRecordTypes.get(0);
		assertEquals(firstText, ("coraText"));
		String secondText = coraClient.createdRecordTypes.get(1);
		assertEquals(secondText, ("coraText"));

		String firstJsonSentToCreate = coraClient.jsonStrings.get(0);
		assertCorrectDataGroupSentToCreate(firstJsonSentToCreate, "seCountryItemText", "text");

		String secondJsonSentToCreate = coraClient.jsonStrings.get(1);
		assertCorrectDataGroupSentToCreate(secondJsonSentToCreate, "seCountryItemDefText", "text");

		String item = coraClient.createdRecordTypes.get(2);
		assertEquals(item, ("countryCollectionItem"));
		String thirdJsonSentToCreate = coraClient.jsonStrings.get(2);
		assertCorrectDataGroupSentToCreate(thirdJsonSentToCreate, "seCountryItem", "metadata");

	}

	private void assertCorrectDataGroupSentToCreate(String firstJsonSentToText, String textId,
			String nameInData) {
		ClientDataGroup textDataGroup = getJsonAsDataGroupToUseInAsserts(firstJsonSentToText);

		assertEquals(textDataGroup.getNameInData(), nameInData);
		ClientDataGroup recordInfo = textDataGroup.getFirstGroupWithNameInData("recordInfo");

		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), textId);
	}

	private ClientDataGroup getJsonAsDataGroupToUseInAsserts(String firstJsonSentToText) {
		JsonObject recordJsonObject = createJsonObjectFromResponseText(firstJsonSentToText);

		JsonToDataConverterFactory converterFactory = new JsonToDataConverterFactoryImp();
		JsonToDataGroupConverter converter = JsonToDataGroupConverter
				.forJsonObjectUsingConverterFactory(recordJsonObject, converterFactory);

		ClientDataGroup textDataGroup = (ClientDataGroup) converter.toInstance();
		return textDataGroup;
	}

	private JsonObject createJsonObjectFromResponseText(String responseText) {
		JsonParser jsonParser = new OrgJsonParser();
		JsonValue jsonValue = jsonParser.parseString(responseText);
		return (JsonObject) jsonValue;
	}

	@Test
	public void testConvertCountryTwoRow() {
		Map<String, String> rowFromDb = new HashMap<>();
		rowFromDb.put("alpha2code", "NO");
		rowFromDb.put("svText", "Norge");
		rowsFromDb.add(rowFromDb);

		toCoraStorage.importFromRowsFromDb(rowsFromDb);
		assertEquals(getRecordTypeSentToCreateByIndex(0), ("coraText"));
		assertEquals(getRecordTypeSentToCreateByIndex(1), ("coraText"));
		assertEquals(getRecordTypeSentToCreateByIndex(2), ("countryCollectionItem"));
		assertEquals(getRecordTypeSentToCreateByIndex(3), ("coraText"));
		assertEquals(getRecordTypeSentToCreateByIndex(4), ("coraText"));
		assertEquals(getRecordTypeSentToCreateByIndex(5), ("countryCollectionItem"));

		assertCorrectDataGroupSentToCreate(getJsonSentToCreateByIndex(0), "seCountryItemText",
				"text");
		assertCorrectDataGroupSentToCreate(getJsonSentToCreateByIndex(1), "seCountryItemDefText",
				"text");
		assertCorrectDataGroupSentToCreate(getJsonSentToCreateByIndex(2), "seCountryItem",
				"metadata");
		assertCorrectDataGroupSentToCreate(getJsonSentToCreateByIndex(3), "noCountryItemText",
				"text");
		assertCorrectDataGroupSentToCreate(getJsonSentToCreateByIndex(4), "noCountryItemDefText",
				"text");

	}

	private String getJsonSentToCreateByIndex(int index) {
		return coraClient.jsonStrings.get(index);
	}

	private String getRecordTypeSentToCreateByIndex(int index) {
		return coraClient.createdRecordTypes.get(index);
	}
}
