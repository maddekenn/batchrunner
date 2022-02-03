package se.uu.ub.cora.batchrunner.change;

import org.testng.annotations.Test;

public class PresentationCollectionVarChangerTest {

	@Test
	public void testChangeReference() {
		DataChanger dataChanger = new PresentationCollectionVarChanger(null);
	}

	// {
	// "name": "presentation",
	// "children": [
	// {
	// "name": "recordInfo",
	// "children": [
	// {
	// "name": "id",
	// "value": "divaLanguageOutputPCollVar"
	// },
	// {
	// "name": "type",
	// "children": [
	// {
	// "name": "linkedRecordType",
	// "value": "recordType"
	// },
	// {
	// "name": "linkedRecordId",
	// "value": "presentationCollectionVar"
	// }
	// ]
	// }
	// ]
	// },
	// {
	// "name": "presentationOf",
	// "children": [
	// {
	// "name": "linkedRecordType",
	// "value": "metadataCollectionVariable"
	// },
	// {
	// "name": "linkedRecordId",
	// "value": "divaLanguageCollectionVar"
	// }
	// ]
	// },
	// {
	// "name": "mode",
	// "value": "output"
	// }
	// ],
	// "attributes": {
	// "type": "pCollVar"
	// }
	// }

}
