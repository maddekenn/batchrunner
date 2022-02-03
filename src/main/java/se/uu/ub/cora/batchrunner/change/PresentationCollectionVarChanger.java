package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataGroup;

public class PresentationCollectionVarChanger implements DataChanger {

	private ClientDataGroup dataGroupToChange;

	public PresentationCollectionVarChanger(ClientDataGroup dataGroupToChange) {
		this.dataGroupToChange = dataGroupToChange;
	}

	@Override
	public ClientDataGroup changeReference(String recordType, String recordId, String newId) {
		// TODO Auto-generated method stub
		return null;
	}

	public ClientDataGroup getDataGroup() {
		return dataGroupToChange;
	}

}
