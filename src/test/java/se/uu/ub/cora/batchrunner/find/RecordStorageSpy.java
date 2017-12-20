package se.uu.ub.cora.batchrunner.find;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecordStorageSpy implements RecordStorage {
    @Override
    public DataGroup read(String s, String s1) {
        return null;
    }

    @Override
    public void create(String s, String s1, DataGroup dataGroup, DataGroup dataGroup1, DataGroup dataGroup2, String s2) {

    }

    @Override
    public void deleteByTypeAndId(String s, String s1) {

    }

    @Override
    public boolean linksExistForRecord(String s, String s1) {
        return false;
    }

    @Override
    public void update(String s, String s1, DataGroup dataGroup, DataGroup dataGroup1, DataGroup dataGroup2, String s2) {

    }

    @Override
    public Collection<DataGroup> readList(String recordType, DataGroup dataGroup) {
        List<DataGroup> recordList = new ArrayList<>();
        DataGroup metadataItemCollection = createItemCollectionWithOneItemRef();

        recordList.add(metadataItemCollection);

        DataGroup metadataItemCollection2 = createItemCollectionWithTwoItemRefs();
        recordList.add(metadataItemCollection2);
        return recordList;
    }

    private DataGroup createItemCollectionWithTwoItemRefs() {
        DataGroup metadataItemCollection2 = createItemCollectionWithIdAndNameInData("secondItemCollection", "second");
        DataGroup itemReferences2 = DataGroup.withNameInData("collectionItemReferences");
        DataGroup firstItem2 = createRefWithItemIdAndRepeatId("firstItem", "1");
        itemReferences2.addChild(firstItem2);
        DataGroup secondItem2 = createRefWithItemIdAndRepeatId("secondItem", "2");
        itemReferences2.addChild(secondItem2);

        metadataItemCollection2.addChild(itemReferences2);
        return metadataItemCollection2;
    }

    private DataGroup createItemCollectionWithOneItemRef() {
        DataGroup metadataItemCollection = createItemCollectionWithIdAndNameInData("firstItemCollection", "first");

        DataGroup itemReferences = DataGroup.withNameInData("collectionItemReferences");
        DataGroup firstItem = createRefWithItemIdAndRepeatId("firstItem", "1");
        itemReferences.addChild(firstItem);
        metadataItemCollection.addChild(itemReferences);
        return metadataItemCollection;
    }

    private DataGroup createItemCollectionWithIdAndNameInData(String id, String nameInData) {
        DataGroup metadataItemCollection  = DataGroup.withNameInData("metadata");
        metadataItemCollection.addChild(createRecordInfo(id));
        metadataItemCollection.addChild(DataAtomic.withNameInDataAndValue("nameInData", nameInData));
        metadataItemCollection.addAttributeByIdWithValue("type", "itemCollection");
        return metadataItemCollection;
    }

    private DataGroup createRefWithItemIdAndRepeatId(String itemId, String repeatId) {
        DataGroup ref = DataGroup.withNameInData("ref");
        ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "metadataCollectionItem"));
        ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", itemId));
        ref.setRepeatId(repeatId);
        return ref;
    }

    @Override
    public Collection<DataGroup> readAbstractList(String s, DataGroup dataGroup) {
        return null;
    }

    @Override
    public DataGroup readLinkList(String s, String s1) {
        return null;
    }

    @Override
    public Collection<DataGroup> generateLinkCollectionPointingToRecord(String s, String s1) {
        return null;
    }

    @Override
    public boolean recordsExistForRecordType(String s) {
        return false;
    }

    @Override
    public boolean recordExistsForAbstractOrImplementingRecordTypeAndRecordId(String s, String s1) {
        return false;
    }

    public DataGroup createRecordInfo(String id){
        DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
        recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", id));
        DataGroup typeGroup = DataGroup.withNameInData("type");
        typeGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
        typeGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "metadataItemCollection"));
        recordInfo.addChild(typeGroup);

        recordInfo.addChild(createDataDivider("testSystem"));
        return recordInfo;
    }

    private static DataGroup createDataDivider(String dataDividerId) {
        DataGroup dataDivider = DataGroup.withNameInData("dataDivider");
        dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
        dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", dataDividerId));
        return dataDivider;
    }
}
