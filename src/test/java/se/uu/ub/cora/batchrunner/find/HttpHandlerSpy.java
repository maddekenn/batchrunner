package se.uu.ub.cora.batchrunner.find;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.uu.ub.cora.httphandler.HttpHandler;

public class HttpHandlerSpy implements HttpHandler {

	public HttpURLConnection httpUrlConnection;
	public String requestMethod;
	public String outputString;
	public Map<String, String> requestProperties = new HashMap<>();
	public int responseCode = 200;
	public String urlString = "";
	public List<String> outputStrings = new ArrayList<>();

	private HttpHandlerSpy(HttpURLConnection httpUrlConnection) {
		this.httpUrlConnection = httpUrlConnection;
		URL url = httpUrlConnection.getURL();
		urlString = url.toString();
	}

	public static HttpHandlerSpy usingURLConnection(HttpURLConnection httpUrlConnection) {
		return new HttpHandlerSpy(httpUrlConnection);
	}

	@Override
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	@Override
	public String getResponseText() {
		if (urlString.endsWith("metadataItemCollection")) {
			return "{\"dataList\":{\"fromNo\":\"1\",\"data\":[{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"searchTermTypeCollection\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/system/cora\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"metadataItemCollection\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/recordType/metadataItemCollection\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 18:00:00.0\"}],\"name\":\"recordInfo\"},{\"name\":\"nameInData\",\"value\":\"searchTermTypeCollection\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"text\"},{\"name\":\"linkedRecordId\",\"value\":\"searchTermTypeCollectionText\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/text/searchTermTypeCollectionText\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"textId\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"text\"},{\"name\":\"linkedRecordId\",\"value\":\"searchTermTypeCollectionDefText\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/text/searchTermTypeCollectionDefText\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"defTextId\"},{\"children\":[{\"repeatId\":\"0\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataCollectionItem\"},{\"name\":\"linkedRecordId\",\"value\":\"textInCollectionItem\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataCollectionItem/textInCollectionItem\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"ref\"}],\"name\":\"collectionItemReferences\"}],\"name\":\"metadata\",\"attributes\":{\"type\":\"itemCollection\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataItemCollection/searchTermTypeCollection\",\"accept\":\"application/vnd.uub.record+json\"},\"read_incoming_links\":{\"requestMethod\":\"GET\",\"rel\":\"read_incoming_links\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataItemCollection/searchTermTypeCollection/incomingLinks\",\"accept\":\"application/vnd.uub.recordList+json\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"nameTypeCollection\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"metadataItemCollection\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/recordType/metadataItemCollection\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"bibsys\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/system/bibsys\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"name\":\"tsCreated\",\"value\":\"2017-11-22 09:11:29.980\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"updatedBy\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-22 09:21:40.826\"}],\"name\":\"recordInfo\"},{\"name\":\"nameInData\",\"value\":\"nameType\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"text\"},{\"name\":\"linkedRecordId\",\"value\":\"nameTypeItemText\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/text/nameTypeItemText\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"textId\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"text\"},{\"name\":\"linkedRecordId\",\"value\":\"nameTypeCollectionDefText\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/text/nameTypeCollectionDefText\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"defTextId\"},{\"children\":[{\"repeatId\":\"0\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataCollectionItem\"},{\"name\":\"linkedRecordId\",\"value\":\"acronymItem\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataCollectionItem/acronymItem\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"ref\"},{\"repeatId\":\"1\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataCollectionItem\"},{\"name\":\"linkedRecordId\",\"value\":\"abbreviationItem\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataCollectionItem/abbreviationItem\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"ref\"},{\"repeatId\":\"2\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataCollectionItem\"},{\"name\":\"linkedRecordId\",\"value\":\"translationItem\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataCollectionItem/translationItem\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"ref\"},{\"repeatId\":\"2\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataCollectionItem\"},{\"name\":\"linkedRecordId\",\"value\":\"translationItem\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataCollectionItem/translationItem\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"NOTref\"}],\"name\":\"collectionItemReferences\"}],\"name\":\"metadata\",\"attributes\":{\"type\":\"itemCollection\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataItemCollection/nameTypeCollection\",\"accept\":\"application/vnd.uub.record+json\"},\"read_incoming_links\":{\"requestMethod\":\"GET\",\"rel\":\"read_incoming_links\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataItemCollection/nameTypeCollection/incomingLinks\",\"accept\":\"application/vnd.uub.recordList+json\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"noItemsTypeCollection\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/system/cora\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"metadataItemCollection\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/recordType/metadataItemCollection\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 18:00:00.0\"}],\"name\":\"recordInfo\"},{\"name\":\"nameInData\",\"value\":\"noItemsTypeCollection\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"text\"},{\"name\":\"linkedRecordId\",\"value\":\"searchTermTypeCollectionText\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/text/searchTermTypeCollectionText\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"textId\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"text\"},{\"name\":\"linkedRecordId\",\"value\":\"searchTermTypeCollectionDefText\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/text/searchTermTypeCollectionDefText\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"defTextId\"}],\"name\":\"metadata\",\"attributes\":{\"type\":\"itemCollection\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataItemCollection/searchTermTypeCollection\",\"accept\":\"application/vnd.uub.record+json\"},\"read_incoming_links\":{\"requestMethod\":\"GET\",\"rel\":\"read_incoming_links\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataItemCollection/searchTermTypeCollection/incomingLinks\",\"accept\":\"application/vnd.uub.recordList+json\"}}}}],\"totalNo\":\"2\",\"containDataOfType\":\"metadataItemCollection\",\"toNo\":\"2\"}}";

		} else if (urlString.endsWith("text")) {
			return "{\"dataList\":{\"fromNo\":\"1\",\"data\":[{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"nameTypeCollectionText\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"bibsys\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://epc.ub.uu.se/therest/rest/record/system/bibsys\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"coraText\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://epc.ub.uu.se/therest/rest/record/recordType/coraText\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:37:36.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"text\",\"value\":\"Namnform\"}],\"name\":\"textPart\",\"attributes\":{\"type\":\"default\",\"lang\":\"sv\"}},{\"children\":[{\"name\":\"text\",\"value\":\"Name \"}],\"name\":\"textPart\",\"attributes\":{\"type\":\"alternative\",\"lang\":\"en\"}}],\"name\":\"text\"},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://epc.ub.uu.se/therest/rest/record/coraText/nameTypeCollectionText\",\"accept\":\"application/vnd.uub.record+json\"},\"update\":{\"requestMethod\":\"POST\",\"rel\":\"update\",\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"https://epc.ub.uu.se/therest/rest/record/coraText/nameTypeCollectionText\",\"accept\":\"application/vnd.uub.record+json\"},\"index\":{\"requestMethod\":\"POST\",\"rel\":\"index\",\"body\":{\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"coraText\"}],\"name\":\"recordType\"},{\"name\":\"recordId\",\"value\":\"nameTypeCollectionText\"},{\"name\":\"type\",\"value\":\"index\"}],\"name\":\"workOrder\"},\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"https://epc.ub.uu.se/therest/rest/record/workOrder/\",\"accept\":\"application/vnd.uub.record+json\"},\"delete\":{\"requestMethod\":\"DELETE\",\"rel\":\"delete\",\"url\":\"https://epc.ub.uu.se/therest/rest/record/coraText/nameTypeCollectionText\"}}}}],\"totalNo\":\"1\",\"containDataOfType\":\"text\",\"toNo\":\"1\"}}";

		} else if (urlString.endsWith("presentationGroup")) {
			return "{\"dataList\":{\"fromNo\":\"1\",\"data\":[{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"collectTermListPGroup\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/system/cora\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationGroup\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/recordType/presentationGroup\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/systemOneUser/141414\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/systemOneUser/141414\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:56:07.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"repeatId\":\"0\",\"children\":[{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"presentationGroup\"},{\"name\":\"linkedRecordId\",\"value\":\"recordInfoOutputPGroup\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/presentationGroup/recordInfoOutputPGroup\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"ref\",\"attributes\":{\"type\":\"presentation\"}}],\"name\":\"refGroup\"}],\"name\":\"childReference\"}],\"name\":\"childReferences\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataGroup\"},{\"name\":\"linkedRecordId\",\"value\":\"collectTermGroup\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/metadataGroup/collectTermGroup\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"presentationOf\"},{\"name\":\"mode\",\"value\":\"output\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pGroup\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/presentationGroup/collectTermListPGroup\",\"accept\":\"application/vnd.uub.record+json\"},\"read_incoming_links\":{\"requestMethod\":\"GET\",\"rel\":\"read_incoming_links\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/presentationGroup/collectTermListPGroup/incomingLinks\",\"accept\":\"application/vnd.uub.recordList+json\"},\"update\":{\"requestMethod\":\"POST\",\"rel\":\"update\",\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/presentationGroup/collectTermListPGroup\",\"accept\":\"application/vnd.uub.record+json\"},\"index\":{\"requestMethod\":\"POST\",\"rel\":\"index\",\"body\":{\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationGroup\"}],\"name\":\"recordType\"},{\"name\":\"recordId\",\"value\":\"collectTermListPGroup\"},{\"name\":\"type\",\"value\":\"index\"}],\"name\":\"workOrder\"},\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/workOrder/\",\"accept\":\"application/vnd.uub.record+json\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"presentationGroupWithoutLinksPGroup\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/system/cora\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationGroup\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/recordType/presentationGroup\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/systemOneUser/141414\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/systemOneUser/141414\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:56:07.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"repeatId\":\"0\",\"children\":[{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"presentationGroup\"},{\"name\":\"linkedRecordId\",\"value\":\"recordInfoOutputPGroup\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/presentationGroup/recordInfoOutputPGroup\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"ref\",\"attributes\":{\"type\":\"presentation\"}}],\"name\":\"refGroup\"}],\"name\":\"childReference\"}],\"name\":\"childReferences\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataGroup\"},{\"name\":\"linkedRecordId\",\"value\":\"collectTermGroup\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/metadataGroup/collectTermGroup\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"presentationOf\"},{\"name\":\"mode\",\"value\":\"output\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pGroup\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/presentationGroup/presentationGroupWithoutLinksPGroup\",\"accept\":\"application/vnd.uub.record+json\"},\"update\":{\"requestMethod\":\"POST\",\"rel\":\"update\",\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/presentationGroup/presentationGroupWithoutLinksPGroup\",\"accept\":\"application/vnd.uub.record+json\"},\"index\":{\"requestMethod\":\"POST\",\"rel\":\"index\",\"body\":{\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationGroup\"}],\"name\":\"recordType\"},{\"name\":\"recordId\",\"value\":\"presentationGroupWithoutLinksPGroup\"},{\"name\":\"type\",\"value\":\"index\"}],\"name\":\"workOrder\"},\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/workOrder/\",\"accept\":\"application/vnd.uub.record+json\"}}}}],\"totalNo\":\"2\",\"containDataOfType\":\"presentationGroup\",\"toNo\":\"2\"}}";

		} else if (urlString.endsWith("presentationVar")) {
			return "{\"dataList\":{\"fromNo\":\"1\",\"data\":[{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"idTextTextVarPVar\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationVar\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:36:06.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataTextVariable\"},{\"name\":\"linkedRecordId\",\"value\":\"idTextTextVar\"}],\"name\":\"presentationOf\"},{\"name\":\"mode\",\"value\":\"input\"},{\"name\":\"inputType\",\"value\":\"input\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"textSystemOne\"},{\"name\":\"linkedRecordId\",\"value\":\"idTextTextVarPlaceholderText\"}],\"name\":\"emptyTextId\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pVar\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://localhost:8080/therest/rest/record/presentationVar/idTextTextVarPVar\",\"accept\":\"application/vnd.uub.record+json\"},\"update\":{\"requestMethod\":\"POST\",\"rel\":\"update\",\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"http://localhost:8080/therest/rest/record/presentationVar/idTextTextVarPVar\",\"accept\":\"application/vnd.uub.record+json\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"cityPVar\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationVar\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:36:06.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataTextVariable\"},{\"name\":\"linkedRecordId\",\"value\":\"cityTextVar\"}],\"name\":\"presentationOf\"},{\"name\":\"mode\",\"value\":\"input\"},{\"name\":\"inputType\",\"value\":\"input\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pVar\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://localhost:8080/therest/rest/record/presentationVar/cityPVar\",\"accept\":\"application/vnd.uub.record+json\"},\"read_incoming_links\":{\"requestMethod\":\"GET\",\"rel\":\"read_incoming_links\",\"url\":\"http://localhost:8080/therest/rest/record/presentationVar/cityPVar/incomingLinks\",\"accept\":\"application/vnd.uub.recordList+json\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"linkedRepeatIdOutputPVar\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationVar\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:36:06.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataTextVariable\"},{\"name\":\"linkedRecordId\",\"value\":\"linkedRepeatIdTextVar\"}],\"name\":\"presentationOf\"},{\"name\":\"mode\",\"value\":\"output\"},{\"name\":\"inputType\",\"value\":\"input\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pVar\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://localhost:8080/therest/rest/record/presentationVar/linkedRepeatIdOutputPVar\",\"accept\":\"application/vnd.uub.record+json\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"linkedRepeatIdPVar\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationVar\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:36:06.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataTextVariable\"},{\"name\":\"linkedRecordId\",\"value\":\"linkedRepeatIdTextVar\"}],\"name\":\"presentationOf\"},{\"name\":\"mode\",\"value\":\"output\"},{\"name\":\"inputType\",\"value\":\"input\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pVar\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://localhost:8080/therest/rest/record/presentationVar/linkedRepeatIdPVar\",\"accept\":\"application/vnd.uub.record+json\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"linkedRecordIdOutputPVar\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationVar\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:36:06.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataTextVariable\"},{\"name\":\"linkedRecordId\",\"value\":\"linkedRecordIdTextVar\"}],\"name\":\"presentationOf\"},{\"name\":\"mode\",\"value\":\"output\"},{\"name\":\"inputType\",\"value\":\"input\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pVar\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://localhost:8080/therest/rest/record/presentationVar/linkedRecordIdOutputPVar\",\"accept\":\"application/vnd.uub.record+json\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"linkedRecordIdPVar\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationVar\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:36:06.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataTextVariable\"},{\"name\":\"linkedRecordId\",\"value\":\"linkedRecordIdTextVar\"}],\"name\":\"presentationOf\"},{\"name\":\"mode\",\"value\":\"output\"},{\"name\":\"inputType\",\"value\":\"input\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pVar\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://localhost:8080/therest/rest/record/presentationVar/linkedRecordIdPVar\",\"accept\":\"application/vnd.uub.record+json\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"linkedRecordTypeOutputPVar\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationVar\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:36:06.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataTextVariable\"},{\"name\":\"linkedRecordId\",\"value\":\"linkedRecordTypeTextVar\"}],\"name\":\"presentationOf\"},{\"name\":\"mode\",\"value\":\"output\"},{\"name\":\"inputType\",\"value\":\"input\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pVar\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://localhost:8080/therest/rest/record/presentationVar/linkedRecordTypeOutputPVar\",\"accept\":\"application/vnd.uub.record+json\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"linkedRecordTypePVar\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationVar\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:36:06.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataTextVariable\"},{\"name\":\"linkedRecordId\",\"value\":\"linkedRecordTypeTextVar\"}],\"name\":\"presentationOf\"},{\"name\":\"mode\",\"value\":\"output\"},{\"name\":\"inputType\",\"value\":\"input\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pVar\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://localhost:8080/therest/rest/record/presentationVar/linkedRecordTypePVar\",\"accept\":\"application/vnd.uub.record+json\"}}}}],\"totalNo\":\"8\",\"containDataOfType\":\"presentationVar\",\"toNo\":\"8\"}}";
		} else if (urlString.endsWith("presentationCollectionVar")) {
			return "{\"dataList\":{\"fromNo\":\"1\",\"data\":[{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"inputTypeCollectionVarPCollVar\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationCollectionVar\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"12345\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:50:41.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataCollectionVariable\"},{\"name\":\"linkedRecordId\",\"value\":\"inputTypeCollectionVar\"}],\"name\":\"presentationOf\"},{\"name\":\"mode\",\"value\":\"input\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"coraText\"},{\"name\":\"linkedRecordId\",\"value\":\"initialEmptyValueText\"}],\"name\":\"emptyTextId\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pCollVar\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/presentationCollectionVar/inputTypeCollectionVarPCollVar\",\"accept\":\"application/vnd.uub.record+json\"},\"update\":{\"requestMethod\":\"POST\",\"rel\":\"update\",\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/presentationCollectionVar/inputTypeCollectionVarPCollVar\",\"accept\":\"application/vnd.uub.record+json\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"uncertainPCollVar\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"bibsys\"}],\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationCollectionVar\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:50:41.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataCollectionVariable\"},{\"name\":\"linkedRecordId\",\"value\":\"uncertainCollectionVar\"}],\"name\":\"presentationOf\"},{\"name\":\"mode\",\"value\":\"input\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"coraText\"},{\"name\":\"linkedRecordId\",\"value\":\"initialEmptyValueText\"}],\"name\":\"emptyTextId\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pCollVar\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/presentationCollectionVar/uncertainPCollVar\",\"accept\":\"application/vnd.uub.record+json\"},\"update\":{\"requestMethod\":\"POST\",\"rel\":\"update\",\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/presentationCollectionVar/uncertainPCollVar\",\"accept\":\"application/vnd.uub.record+json\"}}}}],\"totalNo\":\"2\",\"containDataOfType\":\"presentationCollectionVar\",\"toNo\":\"2\"}}";
		} else if (urlString.endsWith("presentationRecordLink")) {
			return "{\"dataList\":{\"fromNo\":\"1\",\"data\":[{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"refPresentationRecordLinkPLink\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"presentationRecordLink\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"131313\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"systemOneUser\"},{\"name\":\"linkedRecordId\",\"value\":\"131313\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:48:31.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataRecordLink\"},{\"name\":\"linkedRecordId\",\"value\":\"refPresentationRecordLinkLink\"}],\"name\":\"presentationOf\"},{\"name\":\"mode\",\"value\":\"input\"}],\"name\":\"presentation\",\"attributes\":{\"type\":\"pRecordLink\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://cora.epc.ub.uu.se/systemone/rest/record/presentationRecordLink/refPresentationRecordLinkPLink\",\"accept\":\"application/vnd.uub.record+json\"}}}}],\"totalNo\":\"1\",\"containDataOfType\":\"presentationRecordLink\",\"toNo\":\"1\"}}";
		}
		return null;
	}

	@Override
	public int getResponseCode() {
		return responseCode;
	}

	@Override
	public void setOutput(String outputString) {
		this.outputString = outputString;
		outputStrings.add(outputString);
	}

	@Override
	public void setRequestProperty(String key, String value) {
		requestProperties.put(key, value);
	}

	@Override
	public String getErrorText() {
		return null;
	}

	@Override
	public void setStreamOutput(InputStream inputStream) {

	}

	@Override
	public String getHeaderField(String s) {
		return null;
	}
}
