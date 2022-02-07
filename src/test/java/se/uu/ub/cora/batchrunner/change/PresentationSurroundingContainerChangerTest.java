package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataElement;
import se.uu.ub.cora.clientdata.ClientDataGroup;

public class PresentationSurroundingContainerChangerTest {

	private ClientDataGroup container;
	private DataChanger dataChanger;

	@BeforeMethod
	public void setUp() {
		createDataGroup();
		dataChanger = new PresentationSurroundingContainerChanger(container);

	}

	private void createDataGroup() {
		container = ClientDataGroup.withNameInData("presentation");
		ClientDataGroup presentationsOf = ClientDataGroup.withNameInData("presentationsOf");
		ClientDataGroup presentationOf = createPresentationOf("someLanguageCollectionVar", "0");
		presentationsOf.addChild(presentationOf);
		ClientDataGroup presentationOf2 = createPresentationOf("someOtherCollectionVar", "0");
		presentationsOf.addChild(presentationOf2);

		container.addChild(presentationsOf);
	}

	private ClientDataGroup createPresentationOf(String recordId, String repeatId) {
		ClientDataGroup presentationOf = ClientDataGroup.withNameInData("presentationOf");
		presentationOf.addChild(ClientDataAtomic.withNameInDataAndValue("linkedRecordType",
				"metadataCollectionVariable"));
		presentationOf
				.addChild(ClientDataAtomic.withNameInDataAndValue("linkedRecordId", recordId));
		presentationOf.setRepeatId(repeatId);
		return presentationOf;
	}

	@Test
	public void testChangeOneReference() {
		ClientDataGroup changedContainer = dataChanger.changeReference("metadataCollectionVariable",
				"someLanguageCollectionVar", "someChangedId");
		List<ClientDataElement> allPresentationOf = extractPresentationsOf(changedContainer);
		ClientDataGroup changedRef = (ClientDataGroup) allPresentationOf.get(0);
		assertEquals(changedRef.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someChangedId");

		ClientDataGroup notChangedRef = (ClientDataGroup) allPresentationOf.get(1);
		assertEquals(notChangedRef.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someOtherCollectionVar");
	}

	private List<ClientDataElement> extractPresentationsOf(ClientDataGroup changedContainer) {
		ClientDataGroup presentationsOf = changedContainer
				.getFirstGroupWithNameInData("presentationsOf");
		List<ClientDataElement> allPresentationOf = presentationsOf
				.getAllChildrenWithNameInData("presentationOf");
		return allPresentationOf;
	}

	@Test
	public void testNotChangeReferencesWrongRecordType() {
		ClientDataGroup changedContainer = dataChanger.changeReference("metadataGroup",
				"someLanguageCollectionVar", "someChangedId");
		List<ClientDataElement> allPresentationOf = extractPresentationsOf(changedContainer);
		ClientDataGroup changedRef = (ClientDataGroup) allPresentationOf.get(0);
		assertEquals(changedRef.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someLanguageCollectionVar");

		ClientDataGroup notChangedRef = (ClientDataGroup) allPresentationOf.get(1);
		assertEquals(notChangedRef.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someOtherCollectionVar");
	}
}
