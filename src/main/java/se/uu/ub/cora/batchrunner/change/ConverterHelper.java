package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverter;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactoryImp;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactory;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactoryImp;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataRecordConverter;
import se.uu.ub.cora.json.builder.JsonBuilderFactory;
import se.uu.ub.cora.json.builder.org.OrgJsonBuilderFactoryAdapter;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

public class ConverterHelper {

	private ConverterHelper() {
	}

	public static ClientDataRecord getJsonAsClientDataRecord(String json) {
		JsonObject recordJsonObject = createJsonObjectFromResponseText(json);

		JsonToDataConverterFactory recordConverterFactory = new JsonToDataConverterFactoryImp();
		JsonToDataRecordConverter converter = JsonToDataRecordConverter
				.forJsonObjectUsingConverterFactory(recordJsonObject, recordConverterFactory);
		return converter.toInstance();
	}

	private static JsonObject createJsonObjectFromResponseText(String responseText) {
		JsonParser jsonParser = new OrgJsonParser();
		JsonValue jsonValue = jsonParser.parseString(responseText);
		return (JsonObject) jsonValue;
	}

	public static String getDataGroupAsJson(ClientDataGroup dataGroup) {
		DataToJsonConverterFactoryImp jsonConverterFactory = new DataToJsonConverterFactoryImp();
		JsonBuilderFactory factory = new OrgJsonBuilderFactoryAdapter();
		DataToJsonConverter forClientDataElement = jsonConverterFactory
				.createForClientDataElement(factory, dataGroup);
		return forClientDataElement.toJson();
	}
}
