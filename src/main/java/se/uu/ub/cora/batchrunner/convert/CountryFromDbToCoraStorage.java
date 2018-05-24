package se.uu.ub.cora.batchrunner.convert;

import java.util.List;
import java.util.Map;

import se.uu.ub.cora.client.CoraClient;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.converter.javatojson.DataGroupToJsonConverter;
import se.uu.ub.cora.json.builder.JsonBuilderFactory;

public class CountryFromDbToCoraStorage {

	private CoraClient coraClient;
	private JsonBuilderFactory jsonFactory;

	private CountryFromDbToCoraStorage(CoraClient coraClient, JsonBuilderFactory jsonFactory) {
		this.coraClient = coraClient;
		this.jsonFactory = jsonFactory;
	}

	public static CountryFromDbToCoraStorage usingCoraClientAndJsonFactory(CoraClient coraClient,
			JsonBuilderFactory jsonFactory) {
		return new CountryFromDbToCoraStorage(coraClient, jsonFactory);
	}

	public void importFromRowsFromDb(List<Map<String, String>> rowsFromDb) {
		for (Map<String, String> rowFromDb : rowsFromDb) {
			createTexts(rowFromDb);
			createCountryItem(rowFromDb);
		}
	}

	private void createCountryItem(Map<String, String> rowFromDb) {
		ClientDataGroup itemDataGroup = getConstructedCountryItemToCreate(rowFromDb);
		DataGroupToJsonConverter converter = DataGroupToJsonConverter
				.usingJsonFactoryForClientDataGroup(jsonFactory, itemDataGroup);
		String json = converter.toJson();
		coraClient.create("countryCollectionItem", json);
	}

	private ClientDataGroup getConstructedCountryItemToCreate(Map<String, String> rowFromDb) {
		CountryCollectionItemConstructor itemConstructor = new CountryCollectionItemConstructor();
		return itemConstructor.convert(rowFromDb);
	}

	private void createTexts(Map<String, String> rowFromDb) {
		List<ClientDataGroup> texts = getConstructedTextDataGroupsToCreate(rowFromDb);
		for (ClientDataGroup text : texts) {
			createText(text);
		}
	}

	private void createText(ClientDataGroup text) {
		DataGroupToJsonConverter converter = DataGroupToJsonConverter
				.usingJsonFactoryForClientDataGroup(jsonFactory, text);
		String json = converter.toJson();
		coraClient.create("coraText", json);
	}

	private List<ClientDataGroup> getConstructedTextDataGroupsToCreate(
			Map<String, String> rowFromDb) {
		TextFromCountryConstructor textConstructor = new TextFromCountryConstructor();
		return textConstructor.constructFromDbRow(rowFromDb);
	}

}
