package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public interface DataUpdater {

	String updateDataDividerInRecordUsingTypeIdAndNewDivider(String type, String recordId,
			String newDataDivider);

	HttpHandlerFactory getHttpHandlerFactory();

}
