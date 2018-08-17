package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.json.parser.JsonObject;

public class RecordTypePGroupIdsModifier implements HTTPCaller {
	private static final String RECORD_INFO = "recordInfo";
	private static final String PRESENTATION_GROUP_AS_URL_PART = "presentationGroup/";
	private final String url;
	private final HttpHandlerFactory httpHandlerFactory;

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

        HttpHandler pGroupHttpHandler = httpHandlerFactory
				.factor(url + PRESENTATION_GROUP_AS_URL_PART + recordTypeId + "PGroup");
		pGroupHttpHandler.setRequestMethod("GET");

        // TODO: om record not found exception = allt ok, annars ta bort
		// om det inte går att ta bort, logga ut namn på pGroup
        HttpHandler pGroupDeleteHttpHandler = httpHandlerFactory.factor(url + PRESENTATION_GROUP_AS_URL_PART + recordTypeId + "PGroup");
        pGroupDeleteHttpHandler.setRequestMethod("DELETE");
        pGroupDeleteHttpHandler.getResponseCode();

		HttpHandler formPGroupHttpHandler = httpHandlerFactory
				.factor(url + PRESENTATION_GROUP_AS_URL_PART + recordTypeId + "FormPGroup");
		formPGroupHttpHandler.setRequestMethod("GET");
		String pGroupToCopyJson = formPGroupHttpHandler.getResponseText();
		String jsonToSendToCreate = createJsonForNewPGroupUsingIdAndJsonToCopy(recordTypeId,
				pGroupToCopyJson);

		createPGroup(jsonToSendToCreate);

        String updatedRecordTypeJson = getUpdatedRecordTypeAsJson(recordTypeId, recordTypeJson);

        createHttpHandlerForPostWithUrlAndJson(url + "recordType/" + recordTypeId,
                updatedRecordTypeJson);

	}

    private String readRecordType(String recordTypeId) {
        HttpHandler httpHandler = httpHandlerFactory.factor(url + "recordType/" + recordTypeId);
        httpHandler.setRequestMethod("GET");
        return httpHandler.getResponseText();
    }

    private String getUpdatedRecordTypeAsJson(String recordTypeId, String recordTypeJson) {
        ClientDataRecord recordTypeDataRecord = ConverterHelper.getJsonAsClientDataRecord(recordTypeJson);
        ClientDataGroup recordTypeDataGroup = recordTypeDataRecord.getClientDataGroup();
        ClientDataGroup presentationFormId = recordTypeDataGroup.getFirstGroupWithNameInData("presentationFormId");
        presentationFormId.removeFirstChildWithNameInData("linkedRecordId");
        presentationFormId.addChild(ClientDataAtomic.withNameInDataAndValue("linkedRecordId", recordTypeId+"PGroup"));

        return ConverterHelper.getDataGroupAsJson(recordTypeDataGroup);
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

	private String createJsonForNewPGroupUsingIdAndJsonToCopy(String recordTypeId,
			String pGroupToCopyJson) {
		ClientDataRecord pGroupClientDataRecord = ConverterHelper
				.getJsonAsClientDataRecord(pGroupToCopyJson);
		ClientDataGroup pGroupClientDataGroup = pGroupClientDataRecord.getClientDataGroup();

		ClientDataGroup newRecordInfo = createNewRecordInfoUsingDataGroupAndId(
				pGroupClientDataGroup, recordTypeId + "PGroup");
		pGroupClientDataGroup.removeFirstChildWithNameInData(RECORD_INFO);
		pGroupClientDataGroup.addChild(newRecordInfo);

		String jsonToSendToCreate = ConverterHelper.getDataGroupAsJson(pGroupClientDataGroup);
		return jsonToSendToCreate;
	}

	private ClientDataGroup createNewRecordInfoUsingDataGroupAndId(
			ClientDataGroup pGroupClientDataGroup, String id) {
		ClientDataGroup recordInfoToCopy = pGroupClientDataGroup
				.getFirstGroupWithNameInData(RECORD_INFO);
		ClientDataGroup dataDivider = recordInfoToCopy.getFirstGroupWithNameInData("dataDivider");

		ClientDataGroup newRecordInfo = ClientDataGroup.withNameInData(RECORD_INFO);
		newRecordInfo.addChild(ClientDataAtomic.withNameInDataAndValue("id", id));
		newRecordInfo.addChild(dataDivider);
		return newRecordInfo;
	}

	public String getUrl() {
		return url;
	}

	public HttpHandlerFactory getHttpHandlerFactory() {
		return httpHandlerFactory;
	}

}
