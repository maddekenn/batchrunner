package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;

public class PresentationSurroundingContainerChanger implements DataChanger {

	private static final String LINKED_RECORD_ID = "linkedRecordId";
	private ClientDataGroup container;

	public PresentationSurroundingContainerChanger(ClientDataGroup container) {
		this.container = container;
	}

	@Override
	public ClientDataGroup changeReference(String recordType, String recordId, String newId) {
		ClientDataGroup presentationsOf = container.getFirstGroupWithNameInData("presentationsOf");
		for (ClientDataGroup presentationOf : presentationsOf
				.getAllGroupsWithNameInData("presentationOf")) {
			possiblyChangeReference(presentationOf, recordType, recordId, newId);
		}
		return container;
	}

	private void possiblyChangeReference(ClientDataGroup presentationOf, String recordType,
			String recordId, String newId) {
		if (recordIdMatches(recordId, presentationOf)
				&& recordTypeMatches(recordType, presentationOf)) {
			presentationOf.removeFirstChildWithNameInData(LINKED_RECORD_ID);
			presentationOf
					.addChild(ClientDataAtomic.withNameInDataAndValue(LINKED_RECORD_ID, newId));
		}
	}

	private boolean recordIdMatches(String recordId, ClientDataGroup presentationOf) {
		return presentationOf.getFirstAtomicValueWithNameInData(LINKED_RECORD_ID).equals(recordId);
	}

	private boolean recordTypeMatches(String recordType, ClientDataGroup presentationOf) {
		return presentationOf.getFirstAtomicValueWithNameInData("linkedRecordType")
				.equals(recordType);
	}

	public ClientDataGroup getDataGroup() {
		return container;
	}

}
