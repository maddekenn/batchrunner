package se.uu.ub.cora.batchrunner.dbstorage;

import java.util.List;

import se.uu.ub.cora.connection.ParameterConnectionProviderImp;

public class RecordTypeTableCreatorBatchRunner {

	public static void main(String[] args) {

		ParameterConnectionProviderImp connectionProvider = ParameterConnectionProviderImp
				.usingUriAndUserAndPassword(
						"jdbc:postgresql://diva-cora-docker-postgresql:5432/diva", "diva", "diva");
		CoraTableCreator coraTableCreator = new CoraTableCreator(connectionProvider);

		coraTableCreator.createTables(List.of("project", "series"));

		// XsltTransformationFactory xsltTransformationFactory = new XsltTransformationFactory();
		// xsltTransformationFactory.factor("person/coraPerson.xsl");
		// new CoraDbStorer();
		// SqlConnectionProvider connectionProvider = n
		// TODO Auto-generated method stub

		// läs alla recordTypes med coraClient

		// för varje recordType, gör en create av en tabell i db

	}

}
