package se.uu.ub.cora.batchrunner.change;

import java.util.List;

import se.uu.ub.cora.client.CoraClientFactory;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public interface DataUpdater {

	String updateDataDividerInRecordUsingTypeIdAndNewDivider(String type, String recordId,
			String newDataDivider);

	CoraClientFactory getCoraClientFactory();

	List<String> updateDataDividerUsingRecordIdentifiersAndNewDivider(
			List<RecordIdentifier> recordIdentifiers, String newDataDivider);

}
