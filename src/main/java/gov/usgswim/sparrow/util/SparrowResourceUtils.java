package gov.usgswim.sparrow.util;

import gov.usgs.webservices.framework.utils.ResourceLoaderUtils;
import gov.usgs.webservices.framework.utils.SmartXMLProperties;

import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.NotImplementedException;

public abstract class SparrowResourceUtils {
	public static final String HELP_FILE = "model.xml";
	public static final String SESSIONS_FILE = "sessions.properties";
	private static final Properties modelIndex = ResourceLoaderUtils.loadResourceAsProperties("models/modelIndex.txt");

	public static String getModelResourceFilePath(Long modelId, String fileName) {
		if (modelId == null || fileName == null) return null;
		String modelFolder = "models/" + modelId + "/";
		return modelFolder + fileName;
	}

	public static String getResourceFilePath(String fileName) {
		String modelFolder = "models/";
		return modelFolder + fileName;
	}

	public static String retrieveSavedSession(String model, String sessionName) {
		Long modelID = lookupModelID(model);
		Properties props = ResourceLoaderUtils.loadResourceAsProperties(getModelResourceFilePath(modelID, SESSIONS_FILE));
		String defaultValue = null; // TODO decide on default value for session not found
		return props.getProperty(sessionName, defaultValue);
	}

	public static Set<Entry<Object, Object>> retrieveAllSavedSessions(String model) {
		Long modelID = lookupModelID(model);
		Properties props = ResourceLoaderUtils.loadResourceAsProperties(getModelResourceFilePath(modelID, SESSIONS_FILE));
		return props.entrySet();
	}

	public static String lookupHelp(String model, String helpItem) {
		SmartXMLProperties help = retrieveHelp(model);
		return help.getAsFullXMLNode(helpItem);
	}

	public static SmartXMLProperties retrieveHelp(String model) {
		Long modelID = lookupModelID(model);
		String resourceFilePath = getModelResourceFilePath(modelID, HELP_FILE);
		return ResourceLoaderUtils.loadResourceAsSmartXML(resourceFilePath);
	}

	/**
	 * Returns the id of the model, as model input string may be either an id or a name.
	 * @param model
	 * @return
	 */
	public static Long lookupModelID(String model) {
		if (model == null) return null;
		try {
			return Long.parseLong(model);
		} catch (Exception e) {
			try {
				return Long.parseLong(modelIndex.get(model).toString());
			} catch (Exception ex) { /* do nothing. Let someone else deal with bad lookup value */ }
		}
		return null;
	}

	public static String lookupModelName(String model) {
		if (model == null) return null;
		try {
			Long id = Long.parseLong(model);
			return ""; // TODO use bimap to lookup by value
		} catch (Exception e) {
			return model;
		}

	}

	public static String lookupModelName(Long modelID) {
		// TODO implement this
		throw new NotImplementedException();
	}

	private SparrowResourceUtils() {/* private constructor to prevent instantiation */ }

}
