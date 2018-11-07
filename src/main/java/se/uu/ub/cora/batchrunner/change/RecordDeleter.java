package se.uu.ub.cora.batchrunner.change;

import java.util.List;

import se.uu.ub.cora.clientdata.RecordIdentifier;

public interface RecordDeleter {

	String deleteRecordByTypeAndId(String recordType, String recordId);

	List<String> deleteByRecordIdentifiers(List<RecordIdentifier> identifiers);

}
