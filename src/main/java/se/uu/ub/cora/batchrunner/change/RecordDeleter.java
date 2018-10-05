package se.uu.ub.cora.batchrunner.change;

public interface RecordDeleter {

	String deleteRecordByTypeAndId(String recordType, String recordId);

}
