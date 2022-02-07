package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;

public class PresentationCollectionVarChangerTest {

	private ClientDataGroup presentation;

	@BeforeMethod
	public void setUp() {
		presentation = ClientDataGroup.withNameInData("presentation");
		ClientDataGroup presentationOf = ClientDataGroup.withNameInData("presentationOf");
		presentationOf.addChild(ClientDataAtomic.withNameInDataAndValue("linkedRecordType",
				"metadataCollectionVariable"));
		presentationOf.addChild(ClientDataAtomic.withNameInDataAndValue("linkedRecordId",
				"divaLanguageCollectionVar"));
		presentation.addChild(presentationOf);
	}

	@Test
	public void testChangeReference() {
		DataChanger dataChanger = new PresentationCollectionVarChanger(presentation);
		ClientDataGroup changedDataGroup = dataChanger.changeReference("metadataCollectionVariable",
				"divaLanguageCollectionVar", "someIdToChangeTo");
		ClientDataGroup presentationOf = changedDataGroup
				.getFirstGroupWithNameInData("presentationOf");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someIdToChangeTo");
	}

	@Test
	public void testChangeReferenceWrongRecordType() {
		DataChanger dataChanger = new PresentationCollectionVarChanger(presentation);
		ClientDataGroup changedDataGroup = dataChanger.changeReference("metadataGroup",
				"divaLanguageCollectionVar", "someIdToChangeTo");
		ClientDataGroup presentationOf = changedDataGroup
				.getFirstGroupWithNameInData("presentationOf");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"divaLanguageCollectionVar");
	}

	@Test
	public void testChangeReferenceWrongRecordId() {
		DataChanger dataChanger = new PresentationCollectionVarChanger(presentation);
		ClientDataGroup changedDataGroup = dataChanger.changeReference("metadataCollectionVariable",
				"divaNOTLanguageCollectionVar", "someIdToChangeTo");
		ClientDataGroup presentationOf = changedDataGroup
				.getFirstGroupWithNameInData("presentationOf");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"divaLanguageCollectionVar");
	}

}
