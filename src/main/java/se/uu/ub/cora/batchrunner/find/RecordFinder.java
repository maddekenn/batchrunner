package se.uu.ub.cora.batchrunner.find;

import se.uu.ub.cora.bookkeeper.metadata.converter.DataGroupToMetadataConverterFactory;
import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecordFinder extends MetadataFinder implements Finder{

    private String urlString;
    private HttpHandlerFactory httpHandlerFactory;

    public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactory) {
        this.httpHandlerFactory = httpHandlerFactory;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    @Override
    public Collection<String> findRecords() {
        HttpHandler httpHandler = httpHandlerFactory.factor(urlString);
        httpHandler.setRequestMethod("GET");
        String responseText = httpHandler.getResponseText();

        List<String> ids = new ArrayList<>();
        JsonArray data = getDataFromListOfRecords(responseText);
        for (JsonValue value : data) {
            JsonArray children = extractChildrenFromRecordData(value);
            String recordId = getIdFromRecordInfo(children);
            ids.add(recordId);
        }

        return ids;
    }
}
