package se.uu.ub.cora.batchrunner;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import se.uu.ub.cora.clientdata.RecordIdentifier;

public class ResultHolderTest {
	@Test
	public void testInit() throws Exception {
		List<RecordIdentifier> recordIdentifiers = new ArrayList<>();
		List<String> errorMessages = new ArrayList<>();

		ResultHolder resultHolder = new ResultHolder();
		assertNotNull(resultHolder.recordIdentifiers);
		assertNotNull(resultHolder.messages);
		RecordIdentifier recordIdentifier = RecordIdentifier.usingTypeAndId("someRecordType",
				"someRecordId");

		resultHolder.addRecordIdentifier(recordIdentifier);
		assertEquals(resultHolder.recordIdentifiers.size(), 1);
		assertEquals(resultHolder.recordIdentifiers.get(0), recordIdentifier);

		resultHolder.addMessage("some message");
		assertEquals(resultHolder.messages.size(), 1);
		assertEquals(resultHolder.messages.get(0), "some message");

	}
}
