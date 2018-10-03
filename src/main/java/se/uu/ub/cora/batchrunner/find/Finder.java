package se.uu.ub.cora.batchrunner.find;

import java.util.Collection;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public interface Finder {

	void setUrlString(String url);

	Collection<String> findRecords();

	void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactorySpy);

}
