package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverter;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactory;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactory;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataConverterFactoryImp;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataGroupConverter;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataRecordConverter;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataRecordConverterImp;
import se.uu.ub.cora.json.builder.JsonBuilderFactory;
import se.uu.ub.cora.json.builder.org.OrgJsonBuilderFactoryAdapter;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

public class ConverterHelper {

	private ConverterHelper() {
	}

	public static ClientDataRecord getJsonStringAsClientDataRecord(String json) {
		JsonObject recordJsonObject = createJsonObjectFromResponseText(json);
		return getJsonObjectAsClientDataRecord(recordJsonObject);
	}

	public static ClientDataRecord getJsonObjectAsClientDataRecord(JsonObject recordJsonObject) {
		JsonToDataConverterFactory recordConverterFactory = new JsonToDataConverterFactoryImp();
		JsonToDataRecordConverter converter = JsonToDataRecordConverterImp
				.usingConverterFactory(recordConverterFactory);
		return (ClientDataRecord) converter.toInstance(recordJsonObject);
	}

	public static ClientDataGroup getJsonObjectAsClientDataGroup(JsonObject jsonObject) {
		JsonToDataConverterFactory converterFactory = new JsonToDataConverterFactoryImp();
		JsonToDataGroupConverter converter = JsonToDataGroupConverter
				.forJsonObjectUsingConverterFactory(jsonObject, converterFactory);
		return (ClientDataGroup) converter.toInstance();
	}

	private static JsonObject createJsonObjectFromResponseText(String responseText) {
		JsonParser jsonParser = new OrgJsonParser();
		JsonValue jsonValue = jsonParser.parseString(responseText);
		return (JsonObject) jsonValue;
	}

	public static String getDataGroupAsJsonUsingConverterFactory(ClientDataGroup dataGroup,
			DataToJsonConverterFactory jsonConverterFactory) {
		JsonBuilderFactory factory = new OrgJsonBuilderFactoryAdapter();
		DataToJsonConverter converter = jsonConverterFactory
				.createForClientDataElementIncludingActionLinks(factory, dataGroup, false);
		return converter.toJson();
	}

}
