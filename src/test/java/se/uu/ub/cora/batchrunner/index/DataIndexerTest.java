package se.uu.ub.cora.batchrunner.index;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.CoraClientSpy;
import se.uu.ub.cora.clientdata.ClientDataRecord;

public class DataIndexerTest {

	private DataIndexerImp indexer;
	private CoraClientSpy coraClientSpy;

	@BeforeMethod
	public void setUp() {
		coraClientSpy = new CoraClientSpy();
		indexer = DataIndexerImp.usingCoraClient(coraClientSpy);

	}

	@Test
	public void testInit() {
		assertSame(indexer.getCoraClient(), coraClientSpy);
	}

	@Test
	public void testCorrectIndexedData() {
		indexer.indexDataWithRecordType("someRecordType");
		assertEquals(coraClientSpy.recordTypes.get(0), "someRecordType");

		List<ClientDataRecord> returnedListOfRecords = coraClientSpy.returnedListOfRecords;
		assertSame(coraClientSpy.indexedRecords.get(0), returnedListOfRecords.get(0));
		assertSame(coraClientSpy.indexedRecords.get(1), returnedListOfRecords.get(1));
		assertSame(coraClientSpy.indexedRecords.get(2), returnedListOfRecords.get(2));

	}

	@Test
	public void testCatchErrorWhenIndexing() {
		coraClientSpy.throwErrorWhenIndexing = true;
		indexer.indexDataWithRecordType("someRecordType");
		assertEquals(coraClientSpy.recordTypes.get(0), "someRecordType");
	}
}
