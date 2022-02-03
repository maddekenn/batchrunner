package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.uu.ub.cora.batchrunner.find.DataAtomicSpy;
import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataAtomicFactory;

public class DataAtomicFactorySpy implements DataAtomicFactory {

	public DataAtomicSpy factoredDataAtomic;
	public List<DataAtomicSpy> factoredDataAtomics = new ArrayList<>();
	public Map<String, DataAtomicSpy> factoredDataAtomicsMap = new HashMap<>();
	public List<String> usedNameInDatas = new ArrayList<>();
	public List<String> usedValues = new ArrayList<>();
	public List<String> usedRepeatIds = new ArrayList<>();

	@Override
	public DataAtomic factorUsingNameInDataAndValue(String nameInData, String value) {
		usedNameInDatas.add(nameInData);
		usedValues.add(value);
		factoredDataAtomic = new DataAtomicSpy(nameInData, value);
		factoredDataAtomics.add(factoredDataAtomic);
		factoredDataAtomicsMap.put(nameInData, factoredDataAtomic);
		return factoredDataAtomic;
	}

	@Override
	public DataAtomic factorUsingNameInDataAndValueAndRepeatId(String nameInData, String value,
			String repeatId) {
		usedNameInDatas.add(nameInData);
		usedValues.add(value);
		usedRepeatIds.add(repeatId);
		factoredDataAtomic = new DataAtomicSpy(nameInData, value);
		factoredDataAtomics.add(factoredDataAtomic);
		return factoredDataAtomic;
	}

}
