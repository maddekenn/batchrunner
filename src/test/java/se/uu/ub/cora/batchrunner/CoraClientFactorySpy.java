package se.uu.ub.cora.batchrunner;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.javaclient.cora.CoraClient;
import se.uu.ub.cora.javaclient.cora.CoraClientFactory;

public class CoraClientFactorySpy implements CoraClientFactory {

	public List<CoraClientSpy> factoredClientSpies = new ArrayList<>();
	public String appTokenVerifierUrl;
	public String baseUrl;
	public String userId;
	public String appToken;
	public String authToken;

	public CoraClientFactorySpy(String appTokenVerifierUrl, String baseUrl) {
		this.appTokenVerifierUrl = appTokenVerifierUrl;
		this.baseUrl = baseUrl;
	}

	@Override
	public CoraClient factor(String userId, String appToken) {
		this.userId = userId;
		this.appToken = appToken;
		CoraClientSpy coraClientSpy = new CoraClientSpy();
		factoredClientSpies.add(coraClientSpy);
		return coraClientSpy;
	}

	public static CoraClientFactorySpy usingAppTokenVerifierUrlAndBaseUrl(
			String appTokenVerifierUrl, String baseUrl) {
		return new CoraClientFactorySpy(appTokenVerifierUrl, baseUrl);
	}

	@Override
	public CoraClient factorUsingAuthToken(String authToken) {
		this.authToken = authToken;
		CoraClientSpy coraClientSpy = new CoraClientSpy();
		factoredClientSpies.add(coraClientSpy);
		return coraClientSpy;
	}

}
