package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactory;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactoryImp;

public class DataGroupJsonCopier implements DataJsonCopier {
	private static final String RECORD_INFO = "recordInfo";

	@Override
	public String copyDataGroupAsJsonUsingJsonAndNewId(String jsonRecord, String newId) {
		ClientDataGroup pGroupClientDataGroup = getJsonAsDataGroup(jsonRecord, newId);
		DataToJsonConverterFactoryImp jsonConverterFactory = new DataToJsonConverterFactoryImp();
		return ConverterHelper.getDataGroupAsJsonUsingConverterFactory(pGroupClientDataGroup,
				jsonConverterFactory);

	}

	private ClientDataGroup getJsonAsDataGroup(String jsonRecord, String newId) {
		ClientDataRecord pGroupClientDataRecord = ConverterHelper
				.getJsonStringAsClientDataRecord(jsonRecord);
		ClientDataGroup pGroupClientDataGroup = pGroupClientDataRecord.getClientDataGroup();

		ClientDataGroup newRecordInfo = createNewRecordInfoUsingDataGroupAndId(
				pGroupClientDataGroup, newId);
		pGroupClientDataGroup.removeFirstChildWithNameInData(RECORD_INFO);
		pGroupClientDataGroup.addChild(newRecordInfo);
		return pGroupClientDataGroup;
	}

	private ClientDataGroup createNewRecordInfoUsingDataGroupAndId(
			ClientDataGroup pGroupClientDataGroup, String newId) {
		ClientDataGroup recordInfoToCopy = pGroupClientDataGroup
				.getFirstGroupWithNameInData(RECORD_INFO);
		ClientDataGroup dataDivider = recordInfoToCopy.getFirstGroupWithNameInData("dataDivider");

		ClientDataGroup newRecordInfo = ClientDataGroup.withNameInData(RECORD_INFO);
		newRecordInfo.addChild(ClientDataAtomic.withNameInDataAndValue("id", newId));
		newRecordInfo.addChild(dataDivider);
		return newRecordInfo;
	}

	public String copyDataGroupAsJsonExcludeLinksUsingJsonAndNewId(String jsonRecord,
			String newId) {
		ClientDataGroup pGroupClientDataGroup = getJsonAsDataGroup(jsonRecord, newId);
		DataToJsonConverterFactory jsonConverterFactory = new DataToJsonConverterFactoryImp();
		return ConverterHelper.getDataGroupAsJsonUsingConverterFactory(pGroupClientDataGroup,
				jsonConverterFactory);

	}
}
