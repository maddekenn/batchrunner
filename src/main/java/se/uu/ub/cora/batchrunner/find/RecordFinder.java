package se.uu.ub.cora.batchrunner.find;

import java.util.List;

import se.uu.ub.cora.clientdata.RecordIdentifier;

public interface RecordFinder {

	List<RecordIdentifier> findRecordsRelatedToRecordIdentifier(RecordIdentifier recordIdentifier);

}
