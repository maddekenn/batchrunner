package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class RecordTypePGroupIdsModifier implements HTTPCaller {
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
		HttpHandler httpHandler = httpHandlerFactory.factor(url + "recordType/" + recordTypeId);
		httpHandler.setRequestMethod("GET");

		HttpHandler pGroupHttpHandler = httpHandlerFactory
				.factor(url + PRESENTATION_GROUP_AS_URL_PART + recordTypeId + "PGroup");
		pGroupHttpHandler.setRequestMethod("GET");
		// TODO: om record not found exception = allt ok, annars ta bort
		// om det inte går att ta bort, logga ut namn på pGroup

		HttpHandler formPGroupHttpHandler = httpHandlerFactory
				.factor(url + PRESENTATION_GROUP_AS_URL_PART + recordTypeId + "FormPGroup");
		formPGroupHttpHandler.setRequestMethod("GET");
		String pGroupToCopyJson = formPGroupHttpHandler.getResponseText();
		ClientDataRecord pGroupClientDataRecord = ConverterHelper
				.getJsonAsClientDataRecord(pGroupToCopyJson);
		ClientDataGroup pGroupClientDataGroup = pGroupClientDataRecord.getClientDataGroup();

		ClientDataGroup recordInfoToCopy = pGroupClientDataGroup
				.getFirstGroupWithNameInData("recordInfo");
		ClientDataGroup dataDivider = recordInfoToCopy.getFirstGroupWithNameInData("dataDivider");
		pGroupClientDataGroup.removeFirstChildWithNameInData("recordInfo");

		ClientDataGroup newRecordInfo = ClientDataGroup.withNameInData("recordInfo");
		newRecordInfo
				.addChild(ClientDataAtomic.withNameInDataAndValue("id", recordTypeId + "PGroup"));
		newRecordInfo.addChild(dataDivider);
		pGroupClientDataGroup.addChild(newRecordInfo);

		String jsonToSendToCreate = ConverterHelper.getDataGroupAsJson(pGroupClientDataGroup);

		HttpHandler pGroupCreateHttpHandler = httpHandlerFactory
				.factor(url + PRESENTATION_GROUP_AS_URL_PART);
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
