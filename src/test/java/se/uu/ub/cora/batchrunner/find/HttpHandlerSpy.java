package se.uu.ub.cora.batchrunner.find;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import se.uu.ub.cora.httphandler.HttpHandler;

public class HttpHandlerSpy implements HttpHandler {

	public HttpURLConnection httpUrlConnection;
	public String requestMethod;
	public String outputString;
	public Map<String, String> requestProperties = new HashMap<>();
	public int responseCode = 200;
	public static String urlString = "";

	private HttpHandlerSpy(HttpURLConnection httpUrlConnection) {
		this.httpUrlConnection = httpUrlConnection;
	}

	public static HttpHandlerSpy usingURLConnection(HttpURLConnection httpUrlConnection) {
		URL url = httpUrlConnection.getURL();
		urlString = url.toString();
		return new HttpHandlerSpy(httpUrlConnection);
	}

	@Override
	public void setRequestMethod(String s) {

	}

	@Override
	public String getResponseText() {
		if(urlString.endsWith("metadataItemCollection")){
			return "{\"dataList\":{\"fromNo\":\"1\",\"data\":[{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"searchTermTypeCollection\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/system/cora\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"metadataItemCollection\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/recordType/metadataItemCollection\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 18:00:00.0\"}],\"name\":\"recordInfo\"},{\"name\":\"nameInData\",\"value\":\"searchTermTypeCollection\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"text\"},{\"name\":\"linkedRecordId\",\"value\":\"searchTermTypeCollectionText\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/text/searchTermTypeCollectionText\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"textId\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"text\"},{\"name\":\"linkedRecordId\",\"value\":\"searchTermTypeCollectionDefText\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/text/searchTermTypeCollectionDefText\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"defTextId\"},{\"children\":[{\"repeatId\":\"0\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataCollectionItem\"},{\"name\":\"linkedRecordId\",\"value\":\"textInCollectionItem\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataCollectionItem/textInCollectionItem\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"ref\"}],\"name\":\"collectionItemReferences\"}],\"name\":\"metadata\",\"attributes\":{\"type\":\"itemCollection\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataItemCollection/searchTermTypeCollection\",\"accept\":\"application/vnd.uub.record+json\"},\"read_incoming_links\":{\"requestMethod\":\"GET\",\"rel\":\"read_incoming_links\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataItemCollection/searchTermTypeCollection/incomingLinks\",\"accept\":\"application/vnd.uub.recordList+json\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"nameTypeCollection\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"metadataItemCollection\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/recordType/metadataItemCollection\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"bibsys\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/system/bibsys\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"name\":\"tsCreated\",\"value\":\"2017-11-22 09:11:29.980\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"updatedBy\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-22 09:21:40.826\"}],\"name\":\"recordInfo\"},{\"name\":\"nameInData\",\"value\":\"nameType\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"text\"},{\"name\":\"linkedRecordId\",\"value\":\"nameTypeItemText\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/text/nameTypeItemText\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"textId\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"text\"},{\"name\":\"linkedRecordId\",\"value\":\"nameTypeCollectionDefText\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/text/nameTypeCollectionDefText\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"defTextId\"},{\"children\":[{\"repeatId\":\"0\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataCollectionItem\"},{\"name\":\"linkedRecordId\",\"value\":\"acronymItem\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataCollectionItem/acronymItem\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"ref\"},{\"repeatId\":\"1\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataCollectionItem\"},{\"name\":\"linkedRecordId\",\"value\":\"abbreviationItem\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataCollectionItem/abbreviationItem\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"ref\"},{\"repeatId\":\"2\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadataCollectionItem\"},{\"name\":\"linkedRecordId\",\"value\":\"translationItem\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataCollectionItem/translationItem\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"ref\"}],\"name\":\"collectionItemReferences\"}],\"name\":\"metadata\",\"attributes\":{\"type\":\"itemCollection\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataItemCollection/nameTypeCollection\",\"accept\":\"application/vnd.uub.record+json\"},\"read_incoming_links\":{\"requestMethod\":\"GET\",\"rel\":\"read_incoming_links\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataItemCollection/nameTypeCollection/incomingLinks\",\"accept\":\"application/vnd.uub.recordList+json\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"noItemsTypeCollection\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/system/cora\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"metadataItemCollection\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/recordType/metadataItemCollection\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 18:00:00.0\"}],\"name\":\"recordInfo\"},{\"name\":\"nameInData\",\"value\":\"noItemsTypeCollection\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"text\"},{\"name\":\"linkedRecordId\",\"value\":\"searchTermTypeCollectionText\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/text/searchTermTypeCollectionText\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"textId\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"text\"},{\"name\":\"linkedRecordId\",\"value\":\"searchTermTypeCollectionDefText\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/text/searchTermTypeCollectionDefText\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"defTextId\"}],\"name\":\"metadata\",\"attributes\":{\"type\":\"itemCollection\"}},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataItemCollection/searchTermTypeCollection\",\"accept\":\"application/vnd.uub.record+json\"},\"read_incoming_links\":{\"requestMethod\":\"GET\",\"rel\":\"read_incoming_links\",\"url\":\"http://epc.ub.uu.se/therest/rest/record/metadataItemCollection/searchTermTypeCollection/incomingLinks\",\"accept\":\"application/vnd.uub.recordList+json\"}}}}],\"totalNo\":\"2\",\"containDataOfType\":\"metadataItemCollection\",\"toNo\":\"2\"}}";

		}else if(urlString.endsWith("text")){
			return "{\"dataList\":{\"fromNo\":\"1\",\"data\":[{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"nameTypeCollectionText\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"bibsys\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://epc.ub.uu.se/therest/rest/record/system/bibsys\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"coraText\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://epc.ub.uu.se/therest/rest/record/recordType/coraText\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"createdBy\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}],\"name\":\"updatedBy\"},{\"name\":\"tsCreated\",\"value\":\"2017-10-01 00:00:00.0\"},{\"name\":\"tsUpdated\",\"value\":\"2017-11-01 17:37:36.0\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"text\",\"value\":\"Namnform\"}],\"name\":\"textPart\",\"attributes\":{\"type\":\"default\",\"lang\":\"sv\"}},{\"children\":[{\"name\":\"text\",\"value\":\"Name \"}],\"name\":\"textPart\",\"attributes\":{\"type\":\"alternative\",\"lang\":\"en\"}}],\"name\":\"text\"},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"https://epc.ub.uu.se/therest/rest/record/coraText/nameTypeCollectionText\",\"accept\":\"application/vnd.uub.record+json\"},\"update\":{\"requestMethod\":\"POST\",\"rel\":\"update\",\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"https://epc.ub.uu.se/therest/rest/record/coraText/nameTypeCollectionText\",\"accept\":\"application/vnd.uub.record+json\"},\"index\":{\"requestMethod\":\"POST\",\"rel\":\"index\",\"body\":{\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"coraText\"}],\"name\":\"recordType\"},{\"name\":\"recordId\",\"value\":\"nameTypeCollectionText\"},{\"name\":\"type\",\"value\":\"index\"}],\"name\":\"workOrder\"},\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"https://epc.ub.uu.se/therest/rest/record/workOrder/\",\"accept\":\"application/vnd.uub.record+json\"},\"delete\":{\"requestMethod\":\"DELETE\",\"rel\":\"delete\",\"url\":\"https://epc.ub.uu.se/therest/rest/record/coraText/nameTypeCollectionText\"}}}}],\"totalNo\":\"1\",\"containDataOfType\":\"text\",\"toNo\":\"1\"}}";

		}
		return null;
	}

	@Override
	public int getResponseCode() {
		return 0;
	}

	@Override
	public void setOutput(String s) {

	}

	@Override
	public void setRequestProperty(String s, String s1) {

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
