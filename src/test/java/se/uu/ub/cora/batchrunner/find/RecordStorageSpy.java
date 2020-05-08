/*
 * Copyright 2018 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.uu.ub.cora.batchrunner.find;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.storage.RecordStorage;
import se.uu.ub.cora.storage.StorageReadResult;

public class RecordStorageSpy implements RecordStorage {
	@Override
	public DataGroup read(String s, String s1) {
		return null;
	}

	@Override
	public void create(String s, String s1, DataGroup dataGroup, DataGroup dataGroup1,
			DataGroup dataGroup2, String s2) {

	}

	@Override
	public void deleteByTypeAndId(String s, String s1) {

	}

	@Override
	public boolean linksExistForRecord(String s, String s1) {
		return false;
	}

	@Override
	public void update(String s, String s1, DataGroup dataGroup, DataGroup dataGroup1,
			DataGroup dataGroup2, String s2) {

	}

	@Override
	public StorageReadResult readList(String recordType, DataGroup dataGroup) {
		List<DataGroup> recordList = new ArrayList<>();
		DataGroup metadataItemCollection = createItemCollectionWithOneItemRef();

		recordList.add(metadataItemCollection);

		DataGroup metadataItemCollection2 = createItemCollectionWithTwoItemRefs();
		recordList.add(metadataItemCollection2);
		StorageReadResult storageReadResult = new StorageReadResult();
		storageReadResult.listOfDataGroups = recordList;
		return storageReadResult;
		// return recordList;
	}

	private DataGroup createItemCollectionWithTwoItemRefs() {
		DataGroup metadataItemCollection2 = createItemCollectionWithIdAndNameInData(
				"secondItemCollection", "second");
		DataGroup itemReferences2 = new DataGroupSpy("collectionItemReferences");
		DataGroup firstItem2 = createRefWithItemIdAndRepeatId("firstItem", "1");
		itemReferences2.addChild(firstItem2);
		DataGroup secondItem2 = createRefWithItemIdAndRepeatId("secondItem", "2");
		itemReferences2.addChild(secondItem2);

		metadataItemCollection2.addChild(itemReferences2);
		return metadataItemCollection2;
	}

	private DataGroup createItemCollectionWithOneItemRef() {
		DataGroup metadataItemCollection = createItemCollectionWithIdAndNameInData(
				"firstItemCollection", "first");

		DataGroup itemReferences = new DataGroupSpy("collectionItemReferences");
		DataGroup firstItem = createRefWithItemIdAndRepeatId("firstItem", "1");
		itemReferences.addChild(firstItem);
		metadataItemCollection.addChild(itemReferences);
		return metadataItemCollection;
	}

	private DataGroup createItemCollectionWithIdAndNameInData(String id, String nameInData) {
		DataGroup metadataItemCollection = new DataGroupSpy("metadata");
		metadataItemCollection.addChild(createRecordInfo(id));
		metadataItemCollection.addChild(new DataAtomicSpy("nameInData", nameInData));
		metadataItemCollection.addAttributeByIdWithValue("type", "itemCollection");
		return metadataItemCollection;
	}

	private DataGroup createRefWithItemIdAndRepeatId(String itemId, String repeatId) {
		DataGroup ref = new DataGroupSpy("ref");
		ref.addChild(new DataAtomicSpy("linkedRecordType", "metadataCollectionItem"));
		ref.addChild(new DataAtomicSpy("linkedRecordId", itemId));
		ref.setRepeatId(repeatId);
		return ref;
	}

	@Override
	public StorageReadResult readAbstractList(String s, DataGroup dataGroup) {
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

	public DataGroup createRecordInfo(String id) {
		DataGroup recordInfo = new DataGroupSpy("recordInfo");
		recordInfo.addChild(new DataAtomicSpy("id", id));
		DataGroup typeGroup = new DataGroupSpy("type");
		typeGroup.addChild(new DataAtomicSpy("linkedRecordType", "recordType"));
		typeGroup.addChild(new DataAtomicSpy("linkedRecordId", "metadataItemCollection"));
		recordInfo.addChild(typeGroup);

		recordInfo.addChild(createDataDivider("testSystem"));
		return recordInfo;
	}

	private static DataGroup createDataDivider(String dataDividerId) {
		DataGroup dataDivider = new DataGroupSpy("dataDivider");
		dataDivider.addChild(new DataAtomicSpy("linkedRecordType", "system"));
		dataDivider.addChild(new DataAtomicSpy("linkedRecordId", dataDividerId));
		return dataDivider;
	}
}
