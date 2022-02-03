package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.batchrunner.NotImplementedException;
import se.uu.ub.cora.clientdata.ClientDataGroup;

public class DataChangerFactoryImp implements DataChangerFactory {

	@Override
	public DataChanger factor(String referenceType, ClientDataGroup dataGroupToChange) {
		if ("metadataGroup".equals(referenceType)) {
			return new MetadataGroupChildChanger(dataGroupToChange);
		}
		// fel, ska inte vara eller, fixa
		if ("presentationCollectionVar".equals(referenceType)
				|| "presentationSurroundingContainer".equals(referenceType)) {
			return new PresentationCollectionVarChanger(dataGroupToChange);
		}
		throw NotImplementedException
				.withMessage("DataChanger not implemented for " + referenceType);
	}

}
