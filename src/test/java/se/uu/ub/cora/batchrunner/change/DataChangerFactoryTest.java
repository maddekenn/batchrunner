package se.uu.ub.cora.batchrunner.change;

import static org.testng.Assert.assertSame;

import org.testng.annotations.Test;

import se.uu.ub.cora.batchrunner.NotImplementedException;
import se.uu.ub.cora.clientdata.ClientDataGroup;

public class DataChangerFactoryTest {

	@Test
	public void testFactorForMetadataGroup() {
		DataChangerFactory factory = new DataChangerFactoryImp();
		ClientDataGroup dataGroupToChange = ClientDataGroup.withNameInData("metadata");
		MetadataGroupChildChanger dataChanger = (MetadataGroupChildChanger) factory
				.factor("metadataGroup", dataGroupToChange);
		assertSame(dataChanger.getDataGroup(), dataGroupToChange);

	}

	@Test
	public void testFactorForPresentationCollectionVar() {
		DataChangerFactory factory = new DataChangerFactoryImp();
		ClientDataGroup dataGroupToChange = ClientDataGroup.withNameInData("presentation");
		PresentationCollectionVarChanger dataChanger = (PresentationCollectionVarChanger) factory
				.factor("presentationCollectionVar", dataGroupToChange);
		assertSame(dataChanger.getDataGroup(), dataGroupToChange);

	}

	@Test
	public void testFactorForPresentationSurroundingContainer() {
		DataChangerFactory factory = new DataChangerFactoryImp();
		ClientDataGroup dataGroupToChange = ClientDataGroup.withNameInData("presentation");
		PresentationSurroundingContainerChanger dataChanger = (PresentationSurroundingContainerChanger) factory
				.factor("presentationSurroundingContainer", dataGroupToChange);
		assertSame(dataChanger.getDataGroup(), dataGroupToChange);

	}

	@Test(expectedExceptions = NotImplementedException.class, expectedExceptionsMessageRegExp = ""
			+ "DataChanger not implemented for notImplementedType")
	public void testFactorTypeNotImplemented() {
		DataChangerFactory factory = new DataChangerFactoryImp();
		factory.factor("notImplementedType", null);
	}
}
