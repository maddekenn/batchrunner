package se.uu.ub.cora.batchrunner.change;

import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public interface Deleter {

	List<String> deletePGroups(List<String> pGroupNames);

	void setUrlString(String url);

	void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactorySpy);

}
