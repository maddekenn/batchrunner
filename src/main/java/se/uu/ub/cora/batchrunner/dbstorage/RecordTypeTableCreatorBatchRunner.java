package se.uu.ub.cora.batchrunner.dbstorage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import se.uu.ub.cora.connection.SqlConnectionProvider;

public class RecordTypeTableCreatorBatchRunner {

	protected static SqlConnectionProvider connectionProvider;
	protected static TableCreator tableCreator;

	private RecordTypeTableCreatorBatchRunner() {
	}

	public static void main(String[] args)
			throws NoSuchMethodException, SecurityException, ClassNotFoundException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		createConnectionProvider(args);

		// ParameterConnectionProviderImp connectionProvider = ParameterConnectionProviderImp
		// .usingUriAndUserAndPassword(
		// "jdbc:postgresql://diva-cora-docker-postgresql:5432/diva", "diva", "diva");

		createTableCreator(args);

		// TableCreator coraTableCreator = CoraTableCreator
		// .usingConnectionProvider(connectionProvider);

		// coraTableCreator.createTables(List.of("project", "series"));

		// XsltTransformationFactory xsltTransformationFactory = new XsltTransformationFactory();
		// xsltTransformationFactory.factor("person/coraPerson.xsl");
		// new CoraDbStorer();
		// SqlConnectionProvider connectionProvider = n
		// TODO Auto-generated method stub

		// läs alla recordTypes med coraClient

		// för varje recordType, gör en create av en tabell i db

	}

	private static void createConnectionProvider(String[] args) throws NoSuchMethodException,
			ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		Class<?>[] cArg = new Class[3];
		cArg[0] = String.class;
		cArg[1] = String.class;
		cArg[2] = String.class;

		String className = args[0];
		String uri = args[1];
		String user = args[2];
		String password = args[3];
		Method constructor = Class.forName(className).getMethod("usingUriAndUserAndPassword", cArg);

		connectionProvider = (SqlConnectionProvider) constructor.invoke(null, uri, user, password);
	}

	private static void createTableCreator(String[] args) throws NoSuchMethodException,
			ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		Class<?>[] cArg = new Class[1];
		cArg[0] = SqlConnectionProvider.class;

		Method constructor = Class.forName(args[4]).getMethod("usingConnectionProvider", cArg);

		tableCreator = (TableCreator) constructor.invoke(null, connectionProvider);
	}

}
