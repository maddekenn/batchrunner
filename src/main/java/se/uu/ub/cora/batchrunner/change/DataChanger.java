package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataGroup;

public interface DataChanger {

	ClientDataGroup changeReference(String recordType, String recordId, String newId);

}
