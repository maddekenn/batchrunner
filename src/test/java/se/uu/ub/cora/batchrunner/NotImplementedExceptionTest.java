package se.uu.ub.cora.batchrunner;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class NotImplementedExceptionTest {
	@Test
	public void testInit() {
		String message = "message";
		NotImplementedException exception = NotImplementedException.withMessage(message);
		assertEquals(exception.getMessage(), "message");
	}
}