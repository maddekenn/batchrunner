package se.uu.ub.cora.batchrunner.change;

import se.uu.ub.cora.httphandler.HttpHandlerFactory;

import java.util.ArrayList;
import java.util.List;

public class ModifierSpy implements Modifier {
    public List<String> recordTypes = new ArrayList<>();

    @Override
    public void modifyData(String recordTypeId) {
        recordTypes.add(recordTypeId);

    }

    public static ModifierSpy usingURLAndHttpHandlerFactory(String url,
                                                                            HttpHandlerFactory httpHandlerFactory) {

        return new ModifierSpy();
    }
    }
