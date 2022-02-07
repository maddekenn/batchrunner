package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.batchrunner.NotImplementedException;
import se.uu.ub.cora.clientdata.ClientDataGroup;

public class DataChangerFactoryImp implements DataChangerFactory {

	@Override
	public DataChanger factor(String referenceType, ClientDataGroup dataGroupToChange) {
		if ("metadataGroup".equals(referenceType)) {
			return new MetadataGroupChildChanger(dataGroupToChange);
		}
		if ("presentationCollectionVar".equals(referenceType)) {
			return new PresentationCollectionVarChanger(dataGroupToChange);
		}
		if ("presentationSurroundingContainer".equals(referenceType)) {
			return new PresentationSurroundingContainerChanger(dataGroupToChange);
		}
		throw NotImplementedException
				.withMessage("DataChanger not implemented for " + referenceType);
	}

}
