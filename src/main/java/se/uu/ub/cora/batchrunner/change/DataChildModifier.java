package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.RecordIdentifier;

public interface DataChildModifier {

	void update(RecordIdentifier parentIdentifier, RecordIdentifier childIdentifier, String newId);

}
