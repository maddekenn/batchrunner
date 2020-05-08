package se.uu.ub.cora.batchrunner.change;

import java.util.List;

import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public interface DataUpdater {

	String updateDataDividerInRecordUsingTypeIdAndNewDivider(String type, String recordId,
			String newDataDivider);

	CoraClientFactory getCoraClientFactory();

	List<String> updateDataDividerUsingRecordIdentifiersAndNewDivider(
			List<RecordIdentifier> recordIdentifiers, String newDataDivider);

}
