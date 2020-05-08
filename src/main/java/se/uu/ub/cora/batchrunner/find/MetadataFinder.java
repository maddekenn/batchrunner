package se.uu.ub.cora.batchrunner.find;

import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonString;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

public class MetadataFinder {
    protected static final String CHILDREN = "children";

    protected JsonArray extractChildrenFromRecordData(JsonValue value) {
        JsonObject recordData = getDataPartOfRecord(value);

        return recordData.getValueAsJsonArray(MetadataItemCollectionOneReferenceFinder.CHILDREN);
    }

    private JsonObject getDataPartOfRecord(JsonValue value) {
        JsonObject record = ((JsonObject) value).getValueAsJsonObject("record");
        return record.getValueAsJsonObject("data");
    }

    protected JsonArray getDataFromListOfRecords(String responseText) {
        JsonParser jsonParser = new OrgJsonParser();
        JsonObject jsonValue = jsonParser.parseStringAsObject(responseText);
        JsonObject dataList = (JsonObject) jsonValue.getValue("dataList");
        return dataList.getValueAsJsonArray("data");
    }

    protected String getRecordIdentifierFromRecordInfo(JsonArray children) {
        JsonObject recordInfo = (JsonObject) findChildWithName("recordInfo", children);
        return getIdFromRecordInfo(recordInfo);
    }

    protected JsonValue findChildWithName(String nameToFind, JsonArray children){
        for (JsonValue child : children) {
            JsonObject objectChild = (JsonObject) child;
            String name = extractNameFromObject(objectChild);
            if(name.equals(nameToFind)){
                return objectChild;
            }

        }
        return null;
    }

    protected String extractNameFromObject(JsonObject objectChild) {
        return objectChild.getValueAsJsonString("name").getStringValue();
    }

    private String getIdFromRecordInfo(JsonObject recordInfo) {
        JsonArray children = recordInfo.getValueAsJsonArray(CHILDREN);
        JsonObject idChild = (JsonObject) findChildWithName("id", children);
        return extractValueFromObject(idChild);
    }

    private String extractValueFromObject(JsonObject recordInfoObjectChild) {
        JsonString nameValue = recordInfoObjectChild
.getValueAsJsonString("value");
        return nameValue.getStringValue();
    }
}
