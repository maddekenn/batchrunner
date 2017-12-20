package se.uu.ub.cora.batchrunner.find;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

import java.util.Collection;

public interface Finder {
    void setRecordStorage(RecordStorage recordStorage);
    void setUrlString(String url);

    Collection<DataGroup> findRecords();

    void setHttpHandler(HttpHandler httpHandler);
}
