package update;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.data.converter.JsonToDataConverter;
import se.uu.ub.cora.bookkeeper.data.converter.JsonToDataConverterFactory;
import se.uu.ub.cora.bookkeeper.data.converter.JsonToDataConverterFactoryImp;
import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerImp;
import se.uu.ub.cora.json.parser.*;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;


public class DataGroupModifierRunner {


	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException, IOException {

		String urlString = "http://localhost:8080/therest/rest/record/recordType";
		URL url = new URL(urlString);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		HttpHandler httpHandler = HttpHandlerImp.usingURLConnection(urlConnection);
		httpHandler.setRequestMethod("GET");
		int responseCode = httpHandler.getResponseCode();
		String responseText = httpHandler.getResponseText();
		System.out.println(responseText);

		JsonParser jsonParser = new OrgJsonParser();
		JsonObject jsonValue = jsonParser.parseStringAsObject(responseText);
		JsonObject dataList = (JsonObject) jsonValue.getValue("dataList");
		JsonArray data =  dataList.getValueAsJsonArray("data");
		Iterator<JsonValue> iterator = data.iterator();
		while(iterator.hasNext()) {
		JsonToDataConverterFactory jsonToDataConverterFactory = new JsonToDataConverterFactoryImp();
			JsonObject next = (JsonObject) iterator.next();
			JsonObject record = next.getValueAsJsonObject("record");
			JsonObject recordData= record.getValueAsJsonObject("data");
			JsonArray children = recordData.getValueAsJsonArray("children");
			Iterator<JsonValue> iterator1 = children.iterator();
			for (JsonValue child : children) {
				JsonObject objectChild = (JsonObject)child;
				JsonString name = objectChild.getValueAsJsonString("name");
				if(name.getStringValue().equals("recordInfo")){
					for(JsonValue recordInfoChild : objectChild.getValueAsJsonArray("children")){
						JsonObject recordInfoObjectChild = (JsonObject)recordInfoChild;
						JsonString name2 = recordInfoObjectChild.getValueAsJsonString("name");
						if(name2.getStringValue().equals("id")){
							JsonString nameValue = recordInfoObjectChild.getValueAsJsonString("value");
							System.out.print(nameValue.getStringValue()+"\n");

						}
					}
				}
			}

//			System.out.println("recordData");


		}

//		System.out.println(dataPart.getNameInData());
//		return (DataGroup) dataPart;


//		String basePath = args[0];
//		String modifierClassName = args[1];
//		String recordType = args[2];
//		RecordStorage recordStorage = RecordStorageOnDisk
//				.createRecordStorageOnDiskWithBasePath(basePath);
//		DataRecordLinkCollector linkCollector = new DataRecordLinkCollectorImp(
//				(MetadataStorage) recordStorage);
//
//		Constructor<?> constructor = Class.forName(modifierClassName).getConstructor();
//		dataModifier = (DataModifier) constructor.newInstance();
//		dataModifier.setLinkCollector(linkCollector);
//		dataModifier.setRecordStorage(recordStorage);
//
//		dataModifier.modifyByRecordType(recordType);
		System.out.println("done");
	}
}
