package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.client.CoraClient;
import se.uu.ub.cora.client.CoraClientFactory;

public class CoraClientFactorySpy implements CoraClientFactory {

	public List<CoraClientSpy> factoredClientSpies = new ArrayList<>();
	public String appTokenVerifierUrl;
	public String baseUrl;

	public CoraClientFactorySpy(String appTokenVerifierUrl, String baseUrl) {
		this.appTokenVerifierUrl = appTokenVerifierUrl;
		this.baseUrl = baseUrl;
	}

	@Override
	public CoraClient factor(String userId, String appToken) {
		CoraClientSpy coraClientSpy = new CoraClientSpy();
		factoredClientSpies.add(coraClientSpy);
		return coraClientSpy;
	}

	public static CoraClientFactorySpy usingAppTokenVerifierUrlAndBaseUrl(
			String appTokenVerifierUrl, String baseUrl) {
		return new CoraClientFactorySpy(appTokenVerifierUrl, baseUrl);
	}

}
