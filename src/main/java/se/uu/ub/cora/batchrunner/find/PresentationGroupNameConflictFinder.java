package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class PresentationGroupNameConflictFinder implements Finder {

	private List<String> recordTypeNames;

	private HttpHandlerFactory httpHandlerFactory;
	private String urlString;
	private List<String> pGroupEndings = Arrays.asList("PGroup", "NewPGroup", "OutputPGroup");

	public PresentationGroupNameConflictFinder(List<String> recordTypeNames) {
		this.recordTypeNames = recordTypeNames;
	}

	public static PresentationGroupNameConflictFinder usingListOfRecordTypes(
			List<String> recordTypeNames) {
		return new PresentationGroupNameConflictFinder(recordTypeNames);
	}

	@Override
	public Collection<String> findRecords() {
		if (recordTypeNames != null) {
			return findPresentationGroupsForAllRecordTypes();
		}
		return Collections.emptyList();
	}

	private List<String> findPresentationGroupsForAllRecordTypes() {
		List<String> existingPGroups = new ArrayList<>();
		for (String recordTypeName : recordTypeNames) {
			findPresentationGroupsForRecordType(existingPGroups, recordTypeName);
		}
		return existingPGroups;
	}

	private void findPresentationGroupsForRecordType(List<String> existingPGroups,
			String recordTypeName) {
		for (String pGroupEnding : pGroupEndings) {
			addToFoundIdPGroupExists(existingPGroups, recordTypeName, pGroupEnding);
		}
	}

	private void addToFoundIdPGroupExists(List<String> existingPGroups, String recordTypeName,
			String pGroupEnding) {
		String currentPGroupId = recordTypeName + pGroupEnding;
		int responseCode = getResponseCodeForPresentationGroupWithId(currentPGroupId);
		if (responseCode == 200) {
			existingPGroups.add(currentPGroupId);
		}
	}

	private int getResponseCodeForPresentationGroupWithId(String recordTypeName) {
		HttpHandler httpHandler = httpHandlerFactory.factor(urlString + "/" + recordTypeName);
		httpHandler.setRequestMethod("GET");
		return httpHandler.getResponseCode();
	}

	@Override
	public void setUrlString(String url) {
		urlString = url;
	}

	@Override
	public void setHttpHandlerFactory(HttpHandlerFactory httpHandlerFactory) {
		this.httpHandlerFactory = httpHandlerFactory;

	}

}
