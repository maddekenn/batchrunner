package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class RecordTypePGroupIdsModifier implements HTTPCaller {
	private static final String PRESENTATION_GROUP_AS_URL_PART = "presentationGroup/";
	private final String url;
	private final HttpHandlerFactory httpHandlerFactory;
	private PresentationObject formPresentation;
	private PresentationObject formNewPresentation;
	private PresentationObject viewPresentation;

	List<String> pGroupsNotAllowedToRemove = new ArrayList<>();

	public RecordTypePGroupIdsModifier(String url, HttpHandlerFactory httpHandlerFactory) {

		this.url = url;
		this.httpHandlerFactory = httpHandlerFactory;
	}

	public static RecordTypePGroupIdsModifier usingURLAndHttpHandlerFactory(String url,
			HttpHandlerFactory httpHandlerFactory) {
		return new RecordTypePGroupIdsModifier(url, httpHandlerFactory);
	}

	// TODO: grupper ska bara kopieras om recordTypen har de autogenerereade
	// presentationgruppsnamnen
	@Override
	public void modifyData(String recordTypeId) {
		String recordTypeJson = readRecordType(recordTypeId);

		formPresentation = new PresentationObject(recordTypeId + "FormPGroup",
				recordTypeId + "PGroup", "presentationFormId");
		formNewPresentation = new PresentationObject(recordTypeId + "FormNewPGroup",
				recordTypeId + "NewPGroup", "newPresentationFormId");
		viewPresentation = new PresentationObject(recordTypeId + "ViewPGroup",
				recordTypeId + "OutputPGroup", "presentationViewId");

		String[] newPGroupEndings = { "PGroup", "NewPGroup", "OutputPGroup" };
		String[] oldPGroupEndings = { "FormPGroup", "FormNewPGroup", "ViewPGroup" };

		for (

		String newPGroupEnding : newPGroupEndings) {
			int responseCode = deletePresentationGroup(recordTypeId, newPGroupEnding);
			if (responseCode == 405) {
				pGroupsNotAllowedToRemove.add(recordTypeId + newPGroupEnding);
			}
		}
		// kopiera alla tre
		List<String> jsonToCreateList = new ArrayList<>();
		for (int i = 0; i < newPGroupEndings.length; i++) {
			if (!pGroupsNotAllowedToRemove.contains(recordTypeId + newPGroupEndings[i])) {
				jsonToCreateList.add(
						copyOldPGroupToNew(recordTypeId, newPGroupEndings[i], oldPGroupEndings[i]));
			}
		}

		// skapa nya för alla tre
		for (String jsonToSendToCreate : jsonToCreateList) {
			createPGroup(jsonToSendToCreate);
		}

		String updatedRecordTypeJson = getUpdatedRecordTypeAsJson(recordTypeJson);

		createHttpHandlerForPostWithUrlAndJson(url + "recordType/" + recordTypeId,
				updatedRecordTypeJson);

	}

	private String copyOldPGroupToNew(String recordTypeId, String newPGroupEnding,
			String oldPGroupEnding) {
		HttpHandler formPGroupHttpHandler = httpHandlerFactory
				.factor(url + PRESENTATION_GROUP_AS_URL_PART + recordTypeId + oldPGroupEnding);
		formPGroupHttpHandler.setRequestMethod("GET");
		String pGroupToCopyJson = formPGroupHttpHandler.getResponseText();
		DataGroupCopier copier = DataGroupCopier.usingNewId(recordTypeId + newPGroupEnding);
		return copier.copyDataGroupAsJson(pGroupToCopyJson);

	}

	private int deletePresentationGroup(String recordTypeId, String pGroupEnding) {
		HttpHandler pGroupDeleteHttpHandler = httpHandlerFactory
				.factor(url + PRESENTATION_GROUP_AS_URL_PART + recordTypeId + pGroupEnding);
		pGroupDeleteHttpHandler.setRequestMethod("DELETE");
		return pGroupDeleteHttpHandler.getResponseCode();
	}

	private String readRecordType(String recordTypeId) {
		HttpHandler httpHandler = httpHandlerFactory.factor(url + "recordType/" + recordTypeId);
		httpHandler.setRequestMethod("GET");
		return httpHandler.getResponseText();
	}

	private String getUpdatedRecordTypeAsJson(String recordTypeJson) {
		ClientDataRecord recordTypeDataRecord = ConverterHelper
				.getJsonAsClientDataRecord(recordTypeJson);
		ClientDataGroup recordTypeDataGroup = recordTypeDataRecord.getClientDataGroup();
		updateLinkUsingDataGroupAndPresentation(formPresentation, recordTypeDataGroup);
		updateLinkUsingDataGroupAndPresentation(formNewPresentation, recordTypeDataGroup);
		updateLinkUsingDataGroupAndPresentation(viewPresentation, recordTypeDataGroup);

		return ConverterHelper.getDataGroupAsJson(recordTypeDataGroup);
	}

	private void updateLinkUsingDataGroupAndPresentation(PresentationObject presentationObject,
			ClientDataGroup recordTypeDataGroup) {
		if (!pGroupsNotAllowedToRemove.contains(presentationObject.newPGroupId)) {
			ClientDataGroup presentationFormId = recordTypeDataGroup
					.getFirstGroupWithNameInData(presentationObject.presentationId);
			presentationFormId.removeFirstChildWithNameInData("linkedRecordId");
			presentationFormId.addChild(ClientDataAtomic.withNameInDataAndValue("linkedRecordId",
					presentationObject.newPGroupId));
		}
	}

	private void createPGroup(String jsonToSendToCreate) {
		String urlString = url + PRESENTATION_GROUP_AS_URL_PART;
		createHttpHandlerForPostWithUrlAndJson(urlString, jsonToSendToCreate);
	}

	private void createHttpHandlerForPostWithUrlAndJson(String urlString,
			String jsonToSendToCreate) {
		HttpHandler pGroupCreateHttpHandler = httpHandlerFactory.factor(urlString);
		pGroupCreateHttpHandler.setRequestMethod("POST");
		pGroupCreateHttpHandler.setRequestProperty("Accept", "application/vnd.uub.record+json");
		pGroupCreateHttpHandler.setRequestProperty("Content-Type",
				"application/vnd.uub.record+json");
		pGroupCreateHttpHandler.setOutput(jsonToSendToCreate);
	}

	public String getUrl() {
		return url;
	}

	public HttpHandlerFactory getHttpHandlerFactory() {
		return httpHandlerFactory;
	}

}
