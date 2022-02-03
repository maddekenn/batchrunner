package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataGroup;

public class DataChangerSpy implements DataChanger {

	public String recordType;
	public String recordId;
	public String newId;
	public ClientDataGroup returnedDataGroup;

	@Override
	public ClientDataGroup changeReference(String recordType, String recordId, String newId) {
		this.recordType = recordType;
		this.recordId = recordId;
		this.newId = newId;
		returnedDataGroup = ClientDataGroup.withNameInData("dataGroup");
		return returnedDataGroup;
	}

}
