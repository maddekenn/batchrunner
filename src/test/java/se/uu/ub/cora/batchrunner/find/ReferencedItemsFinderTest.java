package se.uu.ub.cora.batchrunner.find;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class ReferencedItemsFinderTest {
	private Finder finder;
	private HttpHandlerFactory httpHandlerFactorySpy;
	private String url;

	@BeforeMethod
	public void setUp() {
		httpHandlerFactorySpy = new HttpHandlerFactorySpy();
	}

	@Test
	public void testFindItemsForACollection() {
		finder = ReferencedItemsFinder.usingURLAndHttpHandlerFactory(url, httpHandlerFactorySpy);
		setHttpHandlerAndUrlString();
		List<String> presentationGroupsWithNameConflict = (List<String>) finder.findRecords();
		// assertEquals(presentationGroupsWithNameConflict.size(), 0);
		// assertEquals(httpHandlerFactorySpy.httpHandlerSpies.size(), 0);
	}

	private void setHttpHandlerAndUrlString() {
		// httpHandlerFactorySpy.setResponseCode(404);
		// finder.setHttpHandlerFactory(httpHandlerFactorySpy);
		// url = "http://localhost:8080/";
		// finder.setUrlString(url);
	}

	// @Test
	// public void testFindCollectionsWithRecordTypesNoConflict() {
	// List<String> recordTypes = new ArrayList<>();
	// recordTypes.add("noConflictRecordType");
	// finder =
	// PresentationGroupNameConflictFinder.usingListOfRecordTypes(recordTypes);
	// setHttpHandlerAndUrlString();
	// List<String> metadataGroupsWithNameConflict = (List<String>)
	// finder.findRecords();
	//
	// assertEquals(httpHandlerFactorySpy.httpHandlerSpies.size(), 3);
	//
	// assertCorrectHttpHandlerByIndexAndId(0, "noConflictRecordTypePGroup");
	// assertCorrectHttpHandlerByIndexAndId(1, "noConflictRecordTypeNewPGroup");
	// assertCorrectHttpHandlerByIndexAndId(2, "noConflictRecordTypeOutputPGroup");
	//
	// assertEquals(metadataGroupsWithNameConflict.size(), 0);
	// }
	//
	// private void assertCorrectHttpHandlerByIndexAndId(int index, String id) {
	// HttpHandlerSpy httpHandlerSpy =
	// httpHandlerFactorySpy.httpHandlerSpies.get(index);
	// assertEquals(httpHandlerSpy.urlString,
	// "http://localhost:8080/presentationGroup/" + id);
	// }
	//
	// @Test
	// public void testFindCollectionsWithRecordTypesOneConflict() {
	// List<String> recordTypes = new ArrayList<>();
	// recordTypes.add("noConflictRecordType");
	// recordTypes.add("person");
	// finder =
	// PresentationGroupNameConflictFinder.usingListOfRecordTypes(recordTypes);
	// setHttpHandlerAndUrlString();
	// List<String> presentationGroupsWithNameConflict = (List<String>)
	// finder.findRecords();
	//
	// assertEquals(httpHandlerFactorySpy.httpHandlerSpies.size(), 6);
	// assertEquals(presentationGroupsWithNameConflict.size(), 1);
	// assertEquals(presentationGroupsWithNameConflict.get(0), "personNewPGroup");
	// }
	//
	// @Test
	// public void testFindCollectionsWithRecordTypesMultipleConflicts() {
	// List<String> recordTypes = new ArrayList<>();
	// recordTypes.add("noConflictRecordType");
	// recordTypes.add("person");
	// recordTypes.add("project");
	// finder =
	// PresentationGroupNameConflictFinder.usingListOfRecordTypes(recordTypes);
	// setHttpHandlerAndUrlString();
	// List<String> presentationGroupsWithNameConflict = (List<String>)
	// finder.findRecords();
	//
	// assertEquals(httpHandlerFactorySpy.httpHandlerSpies.size(), 9);
	// assertEquals(presentationGroupsWithNameConflict.size(), 4);
	// assertEquals(presentationGroupsWithNameConflict.get(0), "personNewPGroup");
	// assertEquals(presentationGroupsWithNameConflict.get(1), "projectPGroup");
	// assertEquals(presentationGroupsWithNameConflict.get(2), "projectNewPGroup");
	// assertEquals(presentationGroupsWithNameConflict.get(3),
	// "projectOutputPGroup");
	// }
}
