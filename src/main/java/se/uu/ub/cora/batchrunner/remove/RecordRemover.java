package se.uu.ub.cora.batchrunner.remove;

import java.util.Collection;

import se.uu.ub.cora.batchrunner.find.Finder;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public interface RecordRemover {
	void setUrlString(String url);

	Collection<String> removeRecordsFoundByFinder();

	void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactorySpy);

	void setFinder(Finder finder);
}
