package se.uu.ub.cora.batchrunner.find;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.spider.record.storage.RecordStorage;
import se.uu.ub.cora.storage.RecordStorageOnDisk;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class FinderBatchRunner {

    protected static Finder finder;

    private FinderBatchRunner(){

    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        String basePath = args[0];
        String finderClassName = args[1];
        String url = args[2];
        RecordStorage recordStorage = RecordStorageOnDisk
                .createRecordStorageOnDiskWithBasePath(basePath);

        Constructor<?> constructor = Class.forName(finderClassName).getConstructor();
        finder = (Finder) constructor.newInstance();
        finder.setRecordStorage(recordStorage);
        finder.setUrlString(url);
        Collection<DataGroup> records = finder.findRecords();

        for(DataGroup record : records){
            DataGroup recordInfo = record.getFirstGroupWithNameInData("recordInfo");
            System.out.println(recordInfo.getFirstAtomicValueWithNameInData("id"));
        }

        System.out.println("done");

    }
}
