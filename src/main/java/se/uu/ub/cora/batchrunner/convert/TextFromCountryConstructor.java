package se.uu.ub.cora.batchrunner.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;

public class TextFromCountryConstructor {

	private Map<String, String> rowFromDb;

	List<DataGroup> constructFromDbRow(Map<String, String> rowFromDb) {
		List<DataGroup> texts = new ArrayList<>();
		this.rowFromDb = rowFromDb;
		createText(texts);
		createDefText(texts);
		return texts;
	}

	private void createText(List<DataGroup> texts) {
		createAndAddTextWithIdEnding(texts, "Text");
	}

	private void createAndAddTextWithIdEnding(List<DataGroup> texts, String textIdEnding) {
		DataGroup text = DataGroup.withNameInData("text");
		addRecordInfo(textIdEnding, text);
		addTextParts(text);
		texts.add(text);
	}

	private void addRecordInfo(String textIdEnding, DataGroup text) {
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		String id = constructIdFromCodeWithEnding(textIdEnding);
		addId(id, recordInfo);
		addDataDivider(recordInfo);
		text.addChild(recordInfo);
	}

	private String constructIdFromCodeWithEnding(String ending) {
		String code = rowFromDb.get("alpha2code");
		return code.toLowerCase() + "CountryItem" + ending;
	}

	private void addId(String id, DataGroup recordInfo) {
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", id));
	}

	private void addDataDivider(DataGroup recordInfo) {
		DataGroup dataDivider = DataGroup.withNameInData("dataDivider");
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "bibsys"));
		recordInfo.addChild(dataDivider);
	}

	private void addTextParts(DataGroup text) {
		String textValue = rowFromDb.get("svText");
		addTextPartUsingValueLangAttributeAndTypeAttribute(textValue, "sv", "default", text);
		possiblyAddEnglishTextPart(text);
	}

	private void addTextPartUsingValueLangAttributeAndTypeAttribute(String textValue,
			String langAttribute, String typeAttribute, DataGroup text) {
		DataGroup textPart = DataGroup.withNameInData("textPart");
		textPart.addAttributeByIdWithValue("type", typeAttribute);
		textPart.addAttributeByIdWithValue("lang", langAttribute);
		textPart.addChild(DataAtomic.withNameInDataAndValue("text", textValue));
		text.addChild(textPart);
	}

	private void possiblyAddEnglishTextPart(DataGroup text) {
		if (rowFromDb.containsKey("enText")) {
			String enTextValue = rowFromDb.get("enText");
			addTextPartUsingValueLangAttributeAndTypeAttribute(enTextValue, "en", "alternative",
					text);
		}
	}

	private void createDefText(List<DataGroup> texts) {
		createAndAddTextWithIdEnding(texts, "DefText");
	}
}
