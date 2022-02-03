package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientSpy;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.RecordIdentifier;

public class DataChildModifierTest {

	private DataChangerFactorySpy changerFactory;
	private CoraClientSpy coraClient;
	private RecordIdentifier recordIdentifierParent;
	private RecordIdentifier childIdentifier;
	private DataChildModifierImp dataUpdater;

	@BeforeMethod
	public void setUp() {
		changerFactory = new DataChangerFactorySpy();
		coraClient = new CoraClientSpy();
		dataUpdater = new DataChildModifierImp(changerFactory, coraClient);
		recordIdentifierParent = RecordIdentifier.usingTypeAndId("metadataGroup",
				"someMetadataGroupId");
		childIdentifier = RecordIdentifier.usingTypeAndId("metadataCollectionVar",
				"someCollectionChild");
	}

	@Test
	public void testUpdateCallToReadIsCorrect() {
		dataUpdater.update(recordIdentifierParent, childIdentifier, "someNewId");

		assertEquals(coraClient.calledMethods.get(0), "readAsDataRecord");
		assertEquals(coraClient.recordTypes.get(0), "metadataGroup");
		assertEquals(coraClient.recordIds.get(0), "someMetadataGroupId");
	}

	@Test
	public void testUpdateCallToChangerIsCorrect() {
		dataUpdater.update(recordIdentifierParent, childIdentifier, "someNewId");

		assertEquals(changerFactory.types.get(0), "metadataGroup");
		ClientDataGroup readDataGroup = coraClient.returnedListOfRecords.get(0)
				.getClientDataGroup();
		assertSame(changerFactory.dataGroups.get(0), readDataGroup);

		DataChangerSpy factoredDataChanger = changerFactory.factoredDataChangers.get(0);
		assertEquals(factoredDataChanger.recordType, "metadataCollectionVar");
		assertEquals(factoredDataChanger.recordId, "someCollectionChild");
		assertEquals(factoredDataChanger.newId, "someNewId");
	}

	@Test
	public void testUpdateCallToUpdateIsCorrect() {
		dataUpdater.update(recordIdentifierParent, childIdentifier, "someNewId");

		DataChangerSpy factoredDataChanger = changerFactory.factoredDataChangers.get(0);
		assertEquals(coraClient.calledMethods.get(1), "update");
		assertEquals(coraClient.recordTypes.get(1), recordIdentifierParent.type);
		assertEquals(coraClient.recordIds.get(1), recordIdentifierParent.id);
		assertSame(coraClient.updatedGroups.get(0), factoredDataChanger.returnedDataGroup);

	}

}
