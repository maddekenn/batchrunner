package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class RecordTypePGroupIdsModifier implements Modifier {
	private static final String PRESENTATION_GROUP_AS_URL_PART = "presentationGroup/";
	private final String url;
	private final HttpHandlerFactory httpHandlerFactory;
	private PresentationObject formPresentation;
	private PresentationObject formNewPresentation;
	private PresentationObject viewPresentation;

	List<String> pGroupsNotAllowedToRemove = new ArrayList<>();
	List<PresentationObject> presentations = new ArrayList<>();
	List<String> errorMessages = new ArrayList<>();

	public RecordTypePGroupIdsModifier(String url, HttpHandlerFactory httpHandlerFactory) {
		this.url = url;
		this.httpHandlerFactory = httpHandlerFactory;
	}

	public static RecordTypePGroupIdsModifier usingURLAndHttpHandlerFactory(String url,
			HttpHandlerFactory httpHandlerFactory) {
		return new RecordTypePGroupIdsModifier(url, httpHandlerFactory);
	}

	@Override
	public List<String> modifyData(String recordTypeId) {
		String recordTypeJson = readRecordType(recordTypeId);

		formPresentation = new PresentationObject(recordTypeId + "FormPGroup",
				recordTypeId + "PGroup", "presentationFormId");
		formNewPresentation = new PresentationObject(recordTypeId + "FormNewPGroup",
				recordTypeId + "NewPGroup", "newPresentationFormId");
		viewPresentation = new PresentationObject(recordTypeId + "ViewPGroup",
				recordTypeId + "OutputPGroup", "presentationViewId");

		presentations.add(formPresentation);
		presentations.add(formNewPresentation);
		presentations.add(viewPresentation);

		possiblyDeletePresentations();

		possiblyCreateNewPresentations();

		String updatedRecordTypeJson = getUpdatedRecordTypeAsJson(recordTypeJson);

		createHttpHandlerForPostWithUrlAndJson(url + "recordType/" + recordTypeId,
				updatedRecordTypeJson);

		return errorMessages;

	}

	private void possiblyDeletePresentations() {
		for (PresentationObject presentation : presentations) {
			int responseCode = deletePresentationGroup(presentation.newPGroupId);
			if (responseCode == 405) {
				pGroupsNotAllowedToRemove.add(presentation.newPGroupId);
			}
		}
	}

	private void possiblyCreateNewPresentations() {
		List<String> jsonToCreateList = new ArrayList<>();
		for (PresentationObject presentation : presentations) {
			if (!pGroupsNotAllowedToRemove.contains(presentation.newPGroupId)) {
				jsonToCreateList.add(
						copyOldPGroupToNew(presentation.newPGroupId, presentation.oldPGroupId));
			}
		}

		for (String jsonToSendToCreate : jsonToCreateList) {
			int responseCode = createPGroup(jsonToSendToCreate);

		}
	}

	private String copyOldPGroupToNew(String newPGroupEnding, String oldPGroupEnding) {
		HttpHandler formPGroupHttpHandler = httpHandlerFactory
				.factor(url + PRESENTATION_GROUP_AS_URL_PART + oldPGroupEnding);
		formPGroupHttpHandler.setRequestMethod("GET");
		String pGroupToCopyJson = formPGroupHttpHandler.getResponseText();
		DataGroupCopier copier = DataGroupCopier.usingNewId(newPGroupEnding);
		return copier.copyDataGroupAsJson(pGroupToCopyJson);

	}

	private int deletePresentationGroup(String pGroupEnding) {
		HttpHandler pGroupDeleteHttpHandler = httpHandlerFactory
				.factor(url + PRESENTATION_GROUP_AS_URL_PART + pGroupEnding);
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

	private int createPGroup(String jsonToSendToCreate) {
		String urlString = url + PRESENTATION_GROUP_AS_URL_PART;
		return createHttpHandlerForPostWithUrlAndJson(urlString, jsonToSendToCreate);
	}

	private int createHttpHandlerForPostWithUrlAndJson(String urlString,
			String jsonToSendToCreate) {
		HttpHandler pGroupCreateHttpHandler = httpHandlerFactory.factor(urlString);
		pGroupCreateHttpHandler.setRequestMethod("POST");
		pGroupCreateHttpHandler.setRequestProperty("Accept", "application/vnd.uub.record+json");
		pGroupCreateHttpHandler.setRequestProperty("Content-Type",
				"application/vnd.uub.record+json");
		pGroupCreateHttpHandler.setOutput(jsonToSendToCreate);
		int responseCode = pGroupCreateHttpHandler.getResponseCode();
		if (responseCode != 200) {
			errorMessages
					.add(String.valueOf(responseCode) + " " + pGroupCreateHttpHandler.getErrorText()
							+ " Error creating: " + jsonToSendToCreate);
		}
		return responseCode;
	}

	public String getUrl() {
		return url;
	}

	public HttpHandlerFactory getHttpHandlerFactory() {
		return httpHandlerFactory;
	}

}
