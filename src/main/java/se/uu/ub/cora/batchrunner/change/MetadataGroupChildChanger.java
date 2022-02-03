package se.uu.ub.cora.batchrunner.change;

import java.util.List;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;

public class MetadataGroupChildChanger implements DataChanger {

	private static final String LINKED_RECORD_ID = "linkedRecordId";
	private ClientDataGroup metadata;

	public MetadataGroupChildChanger(ClientDataGroup dataGroupToChange) {
		this.metadata = dataGroupToChange;
	}

	@Override
	public ClientDataGroup changeReference(String recordType, String recordId, String newId) {
		List<ClientDataGroup> childReferenceList = getListOfChildReferences();
		for (ClientDataGroup childReference : childReferenceList) {
			ClientDataGroup ref = childReference.getFirstGroupWithNameInData("ref");
			if (childMatchedChildToBeReplaced(ref, recordType, recordId)) {
				replaceWithNewId(ref, newId);
			}
		}
		return metadata;
	}

	private List<ClientDataGroup> getListOfChildReferences() {
		ClientDataGroup childReferences = metadata.getFirstGroupWithNameInData("childReferences");
		return childReferences.getAllGroupsWithNameInData("childReference");
	}

	private boolean childMatchedChildToBeReplaced(ClientDataGroup ref, String recordType,
			String recordId) {
		return linkedRecordIdMatches(ref, recordId) && linkedRecordTypeMatches(ref, recordType);
	}

	private void replaceWithNewId(ClientDataGroup ref, String newId) {
		ref.removeFirstChildWithNameInData(LINKED_RECORD_ID);
		ClientDataAtomic newDataAtomic = ClientDataAtomic.withNameInDataAndValue(LINKED_RECORD_ID,
				newId);
		ref.addChild(newDataAtomic);
	}

	private boolean linkedRecordTypeMatches(ClientDataGroup ref, String recordType) {
		return extractAtomicValue(ref, "linkedRecordType").equals(recordType);
	}

	private String extractAtomicValue(ClientDataGroup ref, String nameInData) {
		return ref.getFirstAtomicValueWithNameInData(nameInData);
	}

	private boolean linkedRecordIdMatches(ClientDataGroup ref, String recordId) {
		return extractAtomicValue(ref, LINKED_RECORD_ID).equals(recordId);
	}

	public ClientDataGroup getDataGroup() {
		return metadata;
	}

}
