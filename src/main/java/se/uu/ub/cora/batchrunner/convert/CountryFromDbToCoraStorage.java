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

			TextFromCountryConstructor textConstructor = new TextFromCountryConstructor();
			List<ClientDataGroup> texts = textConstructor.constructFromDbRow(rowFromDb);
			for (ClientDataGroup text : texts) {
				DataGroupToJsonConverter converter = DataGroupToJsonConverter
						.usingJsonFactoryForClientDataGroup(jsonFactory, text);
				String json = converter.toJson();
				coraClient.create("coraText", json);
			}

			CountryCollectionItemConstructor itemConstructor = new CountryCollectionItemConstructor();
			ClientDataGroup itemDataGroup = itemConstructor.convert(rowFromDb);
			DataGroupToJsonConverter converter = DataGroupToJsonConverter
					.usingJsonFactoryForClientDataGroup(jsonFactory, itemDataGroup);
			String json = converter.toJson();
			coraClient.create("countryCollectionItem", json);
		}
	}

}
