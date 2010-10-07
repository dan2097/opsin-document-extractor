package org.bitbucket.dan2097.activityExtractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.cam.ch.wwmm.opsin.XOMTools;
import nu.xom.Document;
import nu.xom.Element;


public class ActivityExtractor {
	static List<String> activityTypes;
	static {
		activityTypes = new ArrayList<String>();
		activityTypes.add("Ki");
	}
	public static List<NameActivityPair> extractNameActivityTypeActivityValueTuples(Document doc){
		List<NameActivityPair> naps = new ArrayList<NameActivityPair>();
		List<Element> tables = XOMTools.getDescendantElementsWithTagName(doc.getRootElement(), "table");
		for (Element table : tables) {
			List<Element> theads = XOMTools.getChildElementsWithTagName(table, "thead");
			if (theads.size()==1){
				List<String> compoundNames = extractNamesInColumn(table, 0);
				Map<Integer, String> columnToActivityType = determineColumnToActivityTypeMapping(theads.get(0));
				naps.addAll(extactsNameActivityPairs(columnToActivityType, compoundNames, table));
			}
		}
		return naps;
	}

	private static List<String> extractNamesInColumn(Element table, int column) {
		List<String> compoundNames = new ArrayList<String>();
		List<Element> tbodys = XOMTools.getChildElementsWithTagName(table, "tbody");
		if (tbodys.size()==1){
			List<Element> rows = XOMTools.getChildElementsWithTagName(tbodys.get(0), "tr");
			for (Element row : rows) {
				List<Element> cells = XOMTools.getChildElementsWithTagName(row, "td");
				if (cells.size()>0){
					compoundNames.add(cells.get(column).getValue());
				}
			}
		}
		return compoundNames;
	}
	
	private static Map<Integer, String> determineColumnToActivityTypeMapping(Element thead) {
		Map<Integer, String> columnToActivityType = new HashMap<Integer, String>();
		List<Element> rows = XOMTools.getChildElementsWithTagName(thead, "tr");
		for (Element row : rows) {
			List<Element> th = XOMTools.getChildElementsWithTagName(row, "th");
			for (int i = 0; i < th.size(); i++) {
				String columnValue = th.get(i).getValue();
				for (String activityType : activityTypes) {
					if (columnValue.regionMatches(true, 0, activityType, 0, activityType.length())){
						columnToActivityType.put(i, activityType);
					}
				}
			}
		}
		return columnToActivityType;
	}
	
	private static List<NameActivityPair> extactsNameActivityPairs(Map<Integer, String> columnToActivityType, List<String> compoundNames, Element table) {
		List<NameActivityPair> naps = new ArrayList<NameActivityPair>();
		Set<Integer> columnsOfInterest = columnToActivityType.keySet();
		List<Element> tbodys = XOMTools.getChildElementsWithTagName(table, "tbody");
		List<Element> rows = XOMTools.getChildElementsWithTagName(tbodys.get(0), "tr");
		if (tbodys.size()==1){
			for (int i = 0; i < compoundNames.size(); i++) {
				Element currentRow = rows.get(i);
				List<Element> cells = XOMTools.getChildElementsWithTagName(currentRow, "td");
				for (Integer column : columnsOfInterest) {
					List<Element> linkElements = XOMTools.getChildElementsWithTagName(cells.get(column), "a");//don't want any links for additonal information on activity values
					for (Element linkElement : linkElements) {
						linkElement.detach();
					}
					String activityValue =cells.get(column).getValue();
					NameActivityPair nap = new NameActivityPair(compoundNames.get(i), activityValue, columnToActivityType.get(column));
					naps.add(nap);
				}
			}
		}
		return naps;
	}
}
