package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.batchrunner.HttpHandlerHelper;
import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactory;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonWithoutActionLinksForLinksConverterFactory;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class DataDividerUpdater {

	private String url;
	private HttpHandlerFactory httpHandlerFactory;

	private DataDividerUpdater(String url, HttpHandlerFactory httpHandlerFactory) {
		this.url = url;
		this.httpHandlerFactory = httpHandlerFactory;
	}

	public static DataDividerUpdater usingURLAndHttpHandlerFactory(String url,
			HttpHandlerFactory httpHandlerFactory) {
		return new DataDividerUpdater(url, httpHandlerFactory);
	}

	HttpHandlerFactory getHttpHandler() {
		return httpHandlerFactory;
	}

	public String getUrl() {
		return url;
	}

	public void updateDataDividerInRecordUsingTypeIdAndNewDivider(String type, String recordId,
			String newDataDivider) {
		HttpHandlerHelper httpHandlerHelper = HttpHandlerHelper.usingURLAndHttpHandlerFactory(url,
				httpHandlerFactory);
		String readRecord = httpHandlerHelper.readRecord(type, recordId);
		ClientDataGroup dataGroup = changeDataDividerInRecord(newDataDivider, readRecord);
		String newJson = getDataGroupAsJson(dataGroup);

		httpHandlerHelper.updateRecord(type, recordId, newJson);
	}

	private String getDataGroupAsJson(ClientDataGroup dataGroup) {
		DataToJsonConverterFactory jsonConverterFactory = new DataToJsonWithoutActionLinksForLinksConverterFactory();
		return ConverterHelper.getDataGroupAsJsonUsingConverterFactory(dataGroup,
				jsonConverterFactory);
	}

	private ClientDataGroup changeDataDividerInRecord(String newDataDivider, String readRecord) {
		ClientDataGroup dataGroup = getJsonAsClientDataGroup(readRecord);
		ClientDataGroup dataDividerGroup = extractDataDividerGroup(dataGroup);
		changeDataDividerInGroup(newDataDivider, dataDividerGroup);
		return dataGroup;
	}

	private ClientDataGroup getJsonAsClientDataGroup(String json) {
		ClientDataRecord pGroupClientDataRecord = ConverterHelper.getJsonAsClientDataRecord(json);
		return pGroupClientDataRecord.getClientDataGroup();
	}

	private ClientDataGroup extractDataDividerGroup(ClientDataGroup dataGroup) {
		ClientDataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		return recordInfo.getFirstGroupWithNameInData("dataDivider");
	}

	private void changeDataDividerInGroup(String newDataDivider, ClientDataGroup dataDividerGroup) {
		dataDividerGroup.removeFirstChildWithNameInData("linkedRecordId");
		dataDividerGroup.addChild(
				ClientDataAtomic.withNameInDataAndValue("linkedRecordId", newDataDivider));
	}

}
