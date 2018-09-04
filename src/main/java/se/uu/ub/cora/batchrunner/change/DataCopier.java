package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public interface DataCopier {

	String copyTypeFromIdToNewId(String type, String id, String newId);

	HttpHandlerFactory getHttpHandler();

	String getUrl();

}
