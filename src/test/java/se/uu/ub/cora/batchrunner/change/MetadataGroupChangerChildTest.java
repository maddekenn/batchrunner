package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;

public class MetadataGroupChangerChildTest {

	private ClientDataGroup metadata;

	@BeforeMethod
	public void setUp() {
		createMetadataGroup();
	}

	private void createMetadataGroup() {
		metadata = ClientDataGroup.withNameInData("metadata");
		ClientDataGroup childReferences = ClientDataGroup.withNameInData("childReferences");
		createChildReference("metadataGroup", "someChildDataGroup", "1", childReferences);
		createChildReference("metadataCollectionVariable", "divaLanguageCollectionVar", "2",
				childReferences);
		metadata.addChild(childReferences);
	}

	private void createChildReference(String linkedRecordType, String linkedRecordId,
			String repeatId, ClientDataGroup childReferences) {
		ClientDataGroup childReference = ClientDataGroup.withNameInData("childReference");
		ClientDataGroup ref = ClientDataGroup.withNameInData("ref");
		ref.addChild(ClientDataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		ref.addChild(ClientDataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
		childReference.addChild(ref);
		childReference.setRepeatId(repeatId);
		childReferences.addChild(childReference);
	}

	@Test
	public void testNoReferenceFound() {
		DataChanger dataChanger = new MetadataGroupChildChanger(metadata);
		ClientDataGroup updatedDataGroup = dataChanger.changeReference("nonExistingReferenceType",
				"nonExistingReferenceId", "idToChangeTo");

		List<ClientDataGroup> childReferencesList = getListOfChildReferences(updatedDataGroup);
		ClientDataGroup ref = childReferencesList.get(1).getFirstGroupWithNameInData("ref");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"divaLanguageCollectionVar");
	}

	private List<ClientDataGroup> getListOfChildReferences(ClientDataGroup updatedDataGroup) {
		ClientDataGroup childReferences = updatedDataGroup
				.getFirstGroupWithNameInData("childReferences");
		List<ClientDataGroup> childReferencesList = childReferences
				.getAllGroupsWithNameInData("childReference");
		return childReferencesList;
	}

	@Test
	public void testMatchingReferenceFound() {
		DataChanger dataChanger = new MetadataGroupChildChanger(metadata);

		ClientDataGroup updatedDataGroup = dataChanger.changeReference("metadataCollectionVariable",
				"divaLanguageCollectionVar", "idToChangeTo");

		List<ClientDataGroup> childReferencesList = getListOfChildReferences(updatedDataGroup);
		ClientDataGroup ref = childReferencesList.get(1).getFirstGroupWithNameInData("ref");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadataCollectionVariable");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordId"), "idToChangeTo");
	}

	@Test
	public void testMatchingReferenceNotFoundWrongRecordType() {
		DataChanger dataChanger = new MetadataGroupChildChanger(metadata);
		ClientDataGroup updatedDataGroup = dataChanger.changeReference("metadataGroup",
				"divaLanguageCollectionVar", "idToChangeTo");

		List<ClientDataGroup> childReferencesList = getListOfChildReferences(updatedDataGroup);
		ClientDataGroup ref = childReferencesList.get(1).getFirstGroupWithNameInData("ref");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"divaLanguageCollectionVar");
	}

}
