package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.client.CoraClient;
import se.uu.ub.cora.client.CoraClientFactory;

public class CoraClientFactorySpy implements CoraClientFactory {

	public List<CoraClientSpy> factoredClientSpies = new ArrayList<>();

	@Override
	public CoraClient factor(String userId, String appToken) {
		// TODO Auto-generated method stub
		CoraClientSpy coraClientSpy = new CoraClientSpy();
		factoredClientSpies.add(coraClientSpy);
		return coraClientSpy;
	}

}
