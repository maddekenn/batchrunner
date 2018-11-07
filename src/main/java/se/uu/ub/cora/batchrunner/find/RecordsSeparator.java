package se.uu.ub.cora.batchrunner.find;

import java.util.List;

import se.uu.ub.cora.batchrunner.ResultHolder;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public interface RecordsSeparator {

	ResultHolder sortOutRecordIdentifiers(List<RecordIdentifier> recordIdentifiers);

}
