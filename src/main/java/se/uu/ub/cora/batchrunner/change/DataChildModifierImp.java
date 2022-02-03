package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.RecordIdentifier;
import se.uu.ub.cora.javaclient.cora.CoraClient;

public class DataChildModifierImp implements DataChildModifier {

	private CoraClient coraClient;
	private DataChangerFactory changerFactory;

	public DataChildModifierImp(DataChangerFactory changerFactory, CoraClient coraClient) {
		this.changerFactory = changerFactory;
		this.coraClient = coraClient;
	}

	@Override
	public void update(RecordIdentifier parentIdentifier, RecordIdentifier childIdentifier,
			String newId) {
		ClientDataRecord parentDataRecord = coraClient.readAsDataRecord(parentIdentifier.type,
				parentIdentifier.id);
		ClientDataGroup parentDataGroup = parentDataRecord.getClientDataGroup();

		DataChanger dataChanger = changerFactory.factor(parentIdentifier.type, parentDataGroup);
		ClientDataGroup changedDataGroup = dataChanger.changeReference(childIdentifier.type,
				childIdentifier.id, newId);

		coraClient.update(parentIdentifier.type, parentIdentifier.id, changedDataGroup);

	}

}
