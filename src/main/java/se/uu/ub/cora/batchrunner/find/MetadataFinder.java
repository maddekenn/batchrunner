package se.uu.ub.cora.batchrunner.find;

import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonValue;

public class MetadataFinder {
    protected static final String CHILDREN = "children";

    protected JsonArray extractChildrenFromRecordData(JsonValue value) {
        JsonObject recordData = getDataPartOfRecord(value);

        return recordData.getValueAsJsonArray(MetadataItemCollectionFinder.CHILDREN);
    }

    private JsonObject getDataPartOfRecord(JsonValue value) {
        JsonObject record = ((JsonObject) value).getValueAsJsonObject("record");
        return record.getValueAsJsonObject("data");
    }
}
