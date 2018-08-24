package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactory;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactoryImp;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonWithoutActionLinksForLinksConverterFactory;

public class DataGroupCopier implements DataCopier {
	private static final String RECORD_INFO = "recordInfo";
	String newId;

	private DataGroupCopier(String newId) {
		this.newId = newId;
	}

	public static DataGroupCopier usingNewId(String newId) {
		return new DataGroupCopier(newId);
	}

	@Override
	public String copyDataGroupAsJson(String jsonRecord) {
		ClientDataGroup pGroupClientDataGroup = getJsonAsDataGroup(jsonRecord);
		DataToJsonConverterFactoryImp jsonConverterFactory = new DataToJsonConverterFactoryImp();
		return ConverterHelper.getDataGroupAsJsonUsingConverterFactory(pGroupClientDataGroup,
				jsonConverterFactory);

	}

	private ClientDataGroup getJsonAsDataGroup(String jsonRecord) {
		ClientDataRecord pGroupClientDataRecord = ConverterHelper
				.getJsonAsClientDataRecord(jsonRecord);
		ClientDataGroup pGroupClientDataGroup = pGroupClientDataRecord.getClientDataGroup();

		ClientDataGroup newRecordInfo = createNewRecordInfoUsingDataGroupAndId(
				pGroupClientDataGroup);
		pGroupClientDataGroup.removeFirstChildWithNameInData(RECORD_INFO);
		pGroupClientDataGroup.addChild(newRecordInfo);
		return pGroupClientDataGroup;
	}

	private ClientDataGroup createNewRecordInfoUsingDataGroupAndId(
			ClientDataGroup pGroupClientDataGroup) {
		ClientDataGroup recordInfoToCopy = pGroupClientDataGroup
				.getFirstGroupWithNameInData(RECORD_INFO);
		ClientDataGroup dataDivider = recordInfoToCopy.getFirstGroupWithNameInData("dataDivider");

		ClientDataGroup newRecordInfo = ClientDataGroup.withNameInData(RECORD_INFO);
		newRecordInfo.addChild(ClientDataAtomic.withNameInDataAndValue("id", newId));
		newRecordInfo.addChild(dataDivider);
		return newRecordInfo;
	}

	public String copyDataGroupAsJsonExcludeLinks(String jsonRecord) {
		ClientDataGroup pGroupClientDataGroup = getJsonAsDataGroup(jsonRecord);
		DataToJsonConverterFactory jsonConverterFactory = new DataToJsonWithoutActionLinksForLinksConverterFactory();
		return ConverterHelper.getDataGroupAsJsonUsingConverterFactory(pGroupClientDataGroup,
				jsonConverterFactory);

	}
}
