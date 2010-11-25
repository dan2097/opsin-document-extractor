package org.bitbucket.dan2097.main;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;

import org.apache.commons.io.IOUtils;
import org.bitbucket.dan2097.activityExtractor.ActivityExtractor;
import org.bitbucket.dan2097.activityExtractor.NameActivityPair;
import org.bitbucket.dan2097.structureExtractor.XMLDocumentToString;
import org.bitbucket.dan2097.structureExtractor.NameIdentifierPair;
import org.bitbucket.dan2097.structureExtractor.NameIdentifierPairFinder;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class DataExtractorFromEPO {

	public static void main(String[] args) throws Exception {
		//String fileName = "src/main/resources/org/bitbucket/dan2097/structureExtractor/EPO-2009-05-06/EP 1326865B1/DOC00001.xml";
		String fileName ="C:/My Documents/Patents/USPTO-50/US6566360_SIMPLE_XML.xml";
		XMLReader xmlReader;
		try{
			xmlReader = XMLReaderFactory.createXMLReader();
		}
		catch (Exception e) {
			throw new Exception("No XML Reader could be initialised!");
		}
		try{
			xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		}
		catch (Exception e) {
			throw new Exception("Your system's default XML Reader does not support disabling DTD loading! Maybe try updating your version of java?");
		}
		Builder xomBuilder = new Builder(xmlReader);
		File f =new File(fileName);
		Document doc =  xomBuilder.build(f);
		XMLDocumentToString xmlDocToString = new XMLDocumentToString();
		String contents = xmlDocToString.convertDocumentToString(doc);
		FileOutputStream out = new FileOutputStream(new File("out.txt"));
		IOUtils.write(contents, out);
		out.close();
		List<NameIdentifierPair> nips = NameIdentifierPairFinder.extractNameIdentifierPairs(contents);
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
