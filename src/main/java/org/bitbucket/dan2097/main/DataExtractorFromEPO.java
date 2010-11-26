package org.bitbucket.dan2097.main;

import java.io.File;
import java.util.List;

import nu.xom.Document;

import org.bitbucket.dan2097.structureExtractor.XMLDocumentToString;
import org.bitbucket.dan2097.structureExtractor.NameIdentifierPair;
import org.bitbucket.dan2097.structureExtractor.NameIdentifierPairFinder;
import org.bitbucket.dan2097.structureExtractor.XMLFileToXMLDocument;

public class DataExtractorFromEPO {

	public static void main(String[] args) throws Exception {
		//String fileName = "src/main/resources/org/bitbucket/dan2097/structureExtractor/EPO-2009-05-06/EP 1326865B1/DOC00001.xml";
		String fileName ="C:/My Documents/Patents/USPTO-50/foo.xml"; 
		Document doc = XMLFileToXMLDocument.readXMLFileUsingXomBuilder(new File(fileName));
		XMLDocumentToString xmlDocToString = new XMLDocumentToString();
		List<String> contents = xmlDocToString.convertDocumentToNewLineDelimitedList(doc);
		for (String string : contents) {
			List<NameIdentifierPair> nips = NameIdentifierPairFinder.extractNameIdentifierPairs(string);
		}
//		for (NameIdentifierPair nameIdentifierPair : nips) {
//			System.out.println(nameIdentifierPair.name + " :: " + nameIdentifierPair.identifier  + " :: " + nameIdentifierPair.identifierType);
//		}
//		System.out.println("!!!!!!!!!!!!!!!!!!");
//		List<NameActivityPair> naps = ActivityExtractor.extractNameActivityTypeActivityValueTuples(doc);
//		for (NameActivityPair nameActivityPair : naps) {
//			boolean structureAvailable =false;
//			for (NameIdentifierPair nameIdentifierPair : nips) {
//				if (nameIdentifierPair.identifier.equalsIgnoreCase(nameActivityPair.name)){
//					//System.out.println(nameIdentifierPair.name +" is recognized");
//					structureAvailable =true;
//				}
//			}
//			System.out.println(nameActivityPair.name + " :: " + nameActivityPair.activity+ " :: " + nameActivityPair.activityType + ":: Structure available:" +structureAvailable);
//		}
	}
}
