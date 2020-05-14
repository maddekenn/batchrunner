package se.uu.ub.cora.batchrunner.change;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonValue;

public class JsonParserSpy implements JsonParser {
	public String jsonStringSentToParser;
	// public JsonValueSpy jsonValueSpy;
	public JsonObjectSpy jsonObjectSpy;
	public List<JsonObjectSpy> jsonObjectSpies = new ArrayList<>();
	// public JsonArraySpy jsonArraySpy;
	public List<String> jsonStringsSentToParser = new ArrayList<>();

	@Override
	public JsonValue parseString(String jsonString) {
		// this.jsonStringSentToParser = jsonString;
		// jsonValueSpy = new JsonValueSpy();
		// return jsonValueSpy;
		return null;
	}

	@Override
	public JsonObject parseStringAsObject(String jsonString) {
		jsonStringsSentToParser.add(jsonString);
		jsonObjectSpy = new JsonObjectSpy();
		jsonObjectSpies.add(jsonObjectSpy);
		return jsonObjectSpy;
		// return null;
	}

	@Override
	public JsonArray parseStringAsArray(String jsonString) {
		// jsonStringSentToParser = jsonString;
		// jsonArraySpy = new JsonArraySpy();
		// return jsonArraySpy;
		return null;
	}
}
