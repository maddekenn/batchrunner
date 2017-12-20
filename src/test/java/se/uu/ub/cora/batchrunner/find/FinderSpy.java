package se.uu.ub.cora.batchrunner.find;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

import java.util.Collection;
import java.util.Collections;

public class FinderSpy implements Finder{
    boolean findRecordCalled = false;
    public String url;

    @Override
    public void setRecordStorage(RecordStorage recordStorage) {

    }

    public void setUrlString(String url) {
        this.url = url;
    }

    @Override
    public Collection<DataGroup> findRecords() {
        findRecordCalled = true;
        return Collections.emptyList();
    }

    @Override
    public void setHttpHandler(HttpHandler httpHandler) {

    }
}
