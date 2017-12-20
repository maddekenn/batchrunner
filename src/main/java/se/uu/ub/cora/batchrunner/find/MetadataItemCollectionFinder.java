package se.uu.ub.cora.batchrunner.find;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerImp;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MetadataItemCollectionFinder implements Finder {

    private RecordStorage recordStorage;
    private String urlString;

    @Override
    public void setRecordStorage(RecordStorage recordStorage) {
        this.recordStorage = recordStorage;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    @Override
    public Collection<DataGroup> findRecords() {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
        }
        HttpHandler httpHandler = HttpHandlerImp.usingURLConnection(urlConnection);
        httpHandler.setRequestMethod("GET");
        int responseCode = httpHandler.getResponseCode();
        String responseText = httpHandler.getResponseText();



        DataGroup emptyFilter = DataGroup.withNameInData("filter");
        Collection<DataGroup> recordList = recordStorage.readList("metadataItemCollection", emptyFilter);
        List<DataGroup> recordsWithOneReference = new ArrayList<>();
        for (DataGroup itemCollection : recordList) {
            DataGroup itemReferences = itemCollection.getFirstGroupWithNameInData("collectionItemReferences");
            List<DataGroup> refs = itemReferences.getAllGroupsWithNameInData("ref");
            if(refs.size() == 1){
                recordsWithOneReference.add(itemCollection);
            }
        }


        return recordsWithOneReference;
    }

    @Override
    public void setHttpHandler(HttpHandler httpHandler) {

    }
}
