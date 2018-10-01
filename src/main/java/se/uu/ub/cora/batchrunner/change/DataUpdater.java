package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.client.CoraClientFactory;

public interface DataUpdater {

	String updateDataDividerInRecordUsingTypeIdAndNewDivider(String type, String recordId,
			String newDataDivider);

	// HttpHandlerFactory getHttpHandlerFactory();

	CoraClientFactory getCoraClientFactory();

}
