package se.uu.ub.cora.batchrunner.change;

public interface DataUpdater {

	String updateDataDividerInRecordUsingTypeIdAndNewDivider(String type, String recordId,
			String newDataDivider);

}
