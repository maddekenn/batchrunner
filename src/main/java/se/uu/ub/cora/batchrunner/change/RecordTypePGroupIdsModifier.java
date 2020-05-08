package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactory;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactoryImp;
import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class RecordTypePGroupIdsModifier implements Modifier {
	private static final String LINKED_RECORD_ID = "linkedRecordId";
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
		errorMessages = new ArrayList<>();
		String recordTypeJson = readRecordType(recordTypeId);

		setIdsOfOldAndNewPresentations(recordTypeId);

		String presentationFormId = getPresentationFormId(recordTypeJson);
		if ((recordTypeId + "FormPGroup").equals(presentationFormId)) {
			String updatedRecordTypeJson = getUpdatedRecordTypeAsJson(recordTypeJson);
			createHttpHandlerForPostWithUrlAndJson(url + "recordType/" + recordTypeId,
					updatedRecordTypeJson);
		} else {
			errorMessages.add(recordTypeId + " does not have default id for presentationFormId");
		}
		return errorMessages;

	}

	private String getPresentationFormId(String recordTypeJson) {
		ClientDataRecord recordTypeDataRecord = ConverterHelper
				.getJsonStringAsClientDataRecord(recordTypeJson);
		ClientDataGroup recordTypeDataGroup = recordTypeDataRecord.getClientDataGroup();
		ClientDataGroup presentationForm = recordTypeDataGroup
				.getFirstGroupWithNameInData("presentationFormId");
		return presentationForm.getFirstAtomicValueWithNameInData(LINKED_RECORD_ID);
	}

	private String readRecordType(String recordTypeId) {
		HttpHandler httpHandler = httpHandlerFactory.factor(url + "recordType/" + recordTypeId);
		httpHandler.setRequestMethod("GET");
		return httpHandler.getResponseText();
	}

	private void setIdsOfOldAndNewPresentations(String recordTypeId) {
		formPresentation = new PresentationObject(recordTypeId + "FormPGroup",
				recordTypeId + "PGroup", "presentationFormId");
		formNewPresentation = new PresentationObject(recordTypeId + "FormNewPGroup",
				recordTypeId + "NewPGroup", "newPresentationFormId");
		viewPresentation = new PresentationObject(recordTypeId + "ViewPGroup",
				recordTypeId + "OutputPGroup", "presentationViewId");
	}

	private String getUpdatedRecordTypeAsJson(String recordTypeJson) {
		ClientDataRecord recordTypeDataRecord = ConverterHelper
				.getJsonStringAsClientDataRecord(recordTypeJson);
		ClientDataGroup recordTypeDataGroup = recordTypeDataRecord.getClientDataGroup();
		updateLinkUsingDataGroupAndPresentation(formPresentation, recordTypeDataGroup);
		updateLinkUsingDataGroupAndPresentation(formNewPresentation, recordTypeDataGroup);
		updateLinkUsingDataGroupAndPresentation(viewPresentation, recordTypeDataGroup);
		DataToJsonConverterFactory jsonConverterFactory = new DataToJsonConverterFactoryImp();

		return ConverterHelper.getDataGroupAsJsonUsingConverterFactory(recordTypeDataGroup,
				jsonConverterFactory);
	}

	private void updateLinkUsingDataGroupAndPresentation(PresentationObject presentationObject,
			ClientDataGroup recordTypeDataGroup) {
		ClientDataGroup presentationFormId = recordTypeDataGroup
				.getFirstGroupWithNameInData(presentationObject.presentationId);
		presentationFormId.removeFirstChildWithNameInData(LINKED_RECORD_ID);
		presentationFormId.addChild(ClientDataAtomic.withNameInDataAndValue(LINKED_RECORD_ID,
				presentationObject.newPGroupId));
	}

	private int createHttpHandlerForPostWithUrlAndJson(String urlString, String jsonToSend) {
		HttpHandler httpHandler = httpHandlerFactory.factor(urlString);
		httpHandler.setRequestMethod("POST");
		httpHandler.setRequestProperty("Accept", "application/vnd.uub.record+json");
		httpHandler.setRequestProperty("Content-Type", "application/vnd.uub.record+json");
		httpHandler.setOutput(jsonToSend);
		int responseCode = httpHandler.getResponseCode();
		if (responseCode != 200) {
			errorMessages.add(String.valueOf(responseCode) + " " + httpHandler.getErrorText()
					+ " Error creating: " + jsonToSend);
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
