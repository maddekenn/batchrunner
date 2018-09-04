package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.find.HttpHandlerFactorySpy;
import se.uu.ub.cora.batchrunner.find.HttpHandlerSpy;

public class PGroupDeleterTest {

	private String url;
	private HttpHandlerFactorySpy httpHandlerFactory;
	private PGroupDeleter deleter;

	@BeforeMethod
	public void setUp() {
		url = "http://someTestUrl/";
		httpHandlerFactory = new HttpHandlerFactorySpy();
		deleter = new PGroupDeleter();
		deleter.setHttpHandlerFactory(httpHandlerFactory);
		deleter.setUrlString(url);
	}

	@Test
	public void testInit() {
		assertEquals(deleter.getHttpHandlerFactory(), httpHandlerFactory);
		assertEquals(deleter.getUrl(), url);
	}

	@Test
	public void testDeleteNoPGroupsExist() {
		httpHandlerFactory.setResponseCode(404);
		List<String> pGroupNames = new ArrayList<>();
		pGroupNames.add("myRecordTypePGroup");
		pGroupNames.add("myRecordTypeNewPGroup");
		pGroupNames.add("myRecordTypeOutputPGroup");
		List<String> unableToDelete = deleter.deletePGroups(pGroupNames);
		assertTrue(unableToDelete.isEmpty());

		assertHttpHandlerIsCorrectForDeleteByIndexAndId(0, "myRecordTypePGroup");
		assertHttpHandlerIsCorrectForDeleteByIndexAndId(1, "myRecordTypeNewPGroup");
		assertHttpHandlerIsCorrectForDeleteByIndexAndId(2, "myRecordTypeOutputPGroup");

	}

	private void assertHttpHandlerIsCorrectForDeleteByIndexAndId(int index, String id) {
		HttpHandlerSpy httpHandlerForPGroupDelete = httpHandlerFactory.httpHandlerSpies.get(index);
		String url = "http://someTestUrl/presentationGroup/" + id;
		assertEquals(httpHandlerForPGroupDelete.urlString, url);
		assertEquals(httpHandlerForPGroupDelete.requestMethod, "DELETE");
		assertTrue(httpHandlerForPGroupDelete.deleteWasCalled);
	}

	@Test
	public void testDeleteAllPGroupsExist() {
		httpHandlerFactory.setResponseCode(405);
		List<String> pGroupNames = new ArrayList<>();
		pGroupNames.add("myRecordTypePGroup");
		pGroupNames.add("myRecordTypeNewPGroup");
		pGroupNames.add("myRecordTypeOutputPGroup");
		List<String> unableToDelete = deleter.deletePGroups(pGroupNames);
		assertEquals(unableToDelete.size(), 3);

		assertEquals(unableToDelete.get(0), "Unable to delete pGroup: myRecordTypePGroup");
		assertEquals(unableToDelete.get(1), "Unable to delete pGroup: myRecordTypeNewPGroup");
		assertEquals(unableToDelete.get(2), "Unable to delete pGroup: myRecordTypeOutputPGroup");
		assertHttpHandlerIsCorrectForDeleteByIndexAndId(0, "myRecordTypePGroup");
		assertHttpHandlerIsCorrectForDeleteByIndexAndId(1, "myRecordTypeNewPGroup");
		assertHttpHandlerIsCorrectForDeleteByIndexAndId(2, "myRecordTypeOutputPGroup");

	}

}
