package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.client.CoraClient;

public class CoraClientSpy implements CoraClient {

	public String recordType;
	public String recordId;

	@Override
	public String create(String recordType, String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String read(String recordType, String recordId) {
		this.recordType = recordType;
		this.recordId = recordId;
		return "{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"languageCollection\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"metadataItemCollection\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"name\":\"dataDivider\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"updatedBy\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 18:00:00.0\"}],\"name\":\"updated\"}],\"name\":\"recordInfo\"},{\"name\":\"nameInData\",\"value\":\"language\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"coraText\"},{\"name\":\"linkedRecordId\",\"value\":\"languageCollectionText\"}],\"name\":\"textId\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"coraText\"},{\"name\":\"linkedRecordId\",\"value\":\"languageCollectionDefText\"}],\"name\":\"defTextId\"},{\"children\":[{\"repeatId\":\"0\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"genericCollectionItem\"},{\"name\":\"linkedRecordId\",\"value\":\"svItem\"}],\"name\":\"ref\"},{\"repeatId\":\"1\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"genericCollectionItem\"},{\"name\":\"linkedRecordId\",\"value\":\"enItem\"}],\"name\":\"ref\"}],\"name\":\"collectionItemReferences\"}],\"name\":\"metadata\",\"attributes\":{\"type\":\"itemCollection\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/alvin/rest/record/metadataItemCollection/languageCollection\",\"accept\":\"application/vnd.uub.record+json\"},\"read_incoming_links\":{\"requestMethod\":\"GET\",\"rel\":\"read_incoming_links\",\"url\":\"https://cora.epc.ub.uu.se/alvin/rest/record/metadataItemCollection/languageCollection/incomingLinks\",\"accept\":\"application/vnd.uub.recordList+json\"}}}}";
	}

	@Override
	public String update(String recordType, String recordId, String json) {
		this.recordType = recordType;
		this.recordId = recordId;
		return json;
	}

}
