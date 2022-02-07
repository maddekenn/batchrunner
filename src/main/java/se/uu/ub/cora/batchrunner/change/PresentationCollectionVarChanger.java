package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;

public class PresentationCollectionVarChanger implements DataChanger {

	private static final String LINKED_RECORD_ID = "linkedRecordId";
	private ClientDataGroup dataGroupToChange;

	public PresentationCollectionVarChanger(ClientDataGroup dataGroupToChange) {
		this.dataGroupToChange = dataGroupToChange;
	}

	@Override
	public ClientDataGroup changeReference(String recordType, String recordId, String newId) {
		ClientDataGroup presentationOf = dataGroupToChange
				.getFirstGroupWithNameInData("presentationOf");
		String linkedRecordType = extractLinkedRecordType(presentationOf);
		String linkedRecordId = extractLinkedRecordId(presentationOf);
		if (typeAndIdMatches(recordType, recordId, linkedRecordType, linkedRecordId)) {
			replaceValue(newId, presentationOf);
		}
		return dataGroupToChange;
	}

	private void replaceValue(String newId, ClientDataGroup presentationOf) {
		presentationOf.removeFirstChildWithNameInData(LINKED_RECORD_ID);
		presentationOf
				.addChild(ClientDataAtomic.withNameInDataAndValue(LINKED_RECORD_ID, newId));
	}

	private String extractLinkedRecordId(ClientDataGroup presentationOf) {
		return presentationOf.getFirstAtomicValueWithNameInData(LINKED_RECORD_ID);
	}

	private String extractLinkedRecordType(ClientDataGroup presentationOf) {
		return presentationOf.getFirstAtomicValueWithNameInData("linkedRecordType");
	}

	private boolean typeAndIdMatches(String recordType, String recordId, String linkedRecordType,
			String linkedRecordId) {
		return linkedRecordType.equals(recordType) && recordId.equals(linkedRecordId);
	}

	public ClientDataGroup getDataGroup() {
		return dataGroupToChange;
	}

}
