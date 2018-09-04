package se.uu.ub.cora.batchrunner.change;

public interface DataJsonCopier {
    String copyDataGroupAsJsonUsingJsonAndNewId(String jsonRecord, String newId);
}
