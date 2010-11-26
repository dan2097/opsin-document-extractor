package org.bitbucket.dan2097.structureExtractor;

import java.io.File;

import nu.xom.Builder;
import nu.xom.Document;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XMLFileToXMLDocument {

	public static Document readXMLFileUsingXomBuilder(File xmlFile) throws Exception{
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
		return xomBuilder.build(xmlFile);
	}
	
	public static Document readXMLFileUsingHtmlTagSoup(File xmlFile) throws Exception{
	    XMLReader tagsoup = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
	    Builder xomBuilder = new Builder(tagsoup);
		return xomBuilder.build(xmlFile);
	}
}
