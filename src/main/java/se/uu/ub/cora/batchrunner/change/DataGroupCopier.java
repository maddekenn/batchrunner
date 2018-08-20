package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;

public class DataGroupCopier implements DataCopier{
    private static final String RECORD_INFO = "recordInfo";
    String newId;

    private DataGroupCopier(String newId) {
        this.newId = newId;
    }

    @Override
    public String copyDataGroupAsJson(String jsonRecord) {
        ClientDataRecord pGroupClientDataRecord = ConverterHelper
                .getJsonAsClientDataRecord(jsonRecord);
        ClientDataGroup pGroupClientDataGroup = pGroupClientDataRecord.getClientDataGroup();

        ClientDataGroup newRecordInfo = createNewRecordInfoUsingDataGroupAndId(
                pGroupClientDataGroup);
        pGroupClientDataGroup.removeFirstChildWithNameInData(RECORD_INFO);
        pGroupClientDataGroup.addChild(newRecordInfo);

        return ConverterHelper.getDataGroupAsJson(pGroupClientDataGroup);

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

    public static DataGroupCopier usingNewId(String newId) {
        return new DataGroupCopier(newId);
    }
}
