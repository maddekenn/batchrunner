package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;

public class RecordTypePGroupIdsModifier implements HTTPCaller{
    private final String url;
    private final HttpHandlerFactory httpHandlerFactory;

    public RecordTypePGroupIdsModifier(String url, HttpHandlerFactory httpHandlerFactory) {

        this.url = url;
        this.httpHandlerFactory = httpHandlerFactory;
    }

    public static RecordTypePGroupIdsModifier usingURLAndHttpHandlerFactory(String url, HttpHandlerFactory httpHandlerFactory) {
        return new RecordTypePGroupIdsModifier(url, httpHandlerFactory);
    }

    @Override
    public void modifyData(String recordTypeId) {
        HttpHandler httpHandler = httpHandlerFactory.factor(url+"recordType/"+recordTypeId);
        httpHandler.setRequestMethod("GET");

        HttpHandler pGroupHttpHandler = httpHandlerFactory.factor(url+"presentationGroup/"+recordTypeId+"PGroup");
        pGroupHttpHandler.setRequestMethod("GET");

        HttpHandler formPGroupHttpHandler = httpHandlerFactory.factor(url+"presentationGroup/"+recordTypeId+"FormPGroup");
        formPGroupHttpHandler.setRequestMethod("GET");

        HttpHandler pGroupCreateHttpHandler = httpHandlerFactory.factor(url+"presentationGroup/");
        pGroupCreateHttpHandler.setRequestMethod("POST");
        pGroupCreateHttpHandler.setRequestProperty("Accept", "application/vnd.uub.record+json");
        pGroupCreateHttpHandler.setRequestProperty("Content-Type", "application/vnd.uub.record+json");
        pGroupCreateHttpHandler.setOutput("");

    }

    public String getUrl() {
      return url;
    }

    public HttpHandlerFactory getHttpHandlerFactory() {
        return httpHandlerFactory;
    }

}
