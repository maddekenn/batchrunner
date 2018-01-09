package se.uu.ub.cora.batchrunner.index;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

import java.util.List;

public class DataIndexer {
    private HttpHandlerFactory httpHandlerFactory;

    public void indexData(List<String> idsToIndex) {

    }

    public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactory) {
        this.httpHandlerFactory = httpHandlerFactory;
    }
}
