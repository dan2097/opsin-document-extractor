package org.bitbucket.dan2097.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.List;

import nu.xom.Document;

import org.bitbucket.dan2097.structureExtractor.DocumentToStructures;
import org.bitbucket.dan2097.structureExtractor.IdentifiedChemicalName;
import org.bitbucket.dan2097.structureExtractor.XMLDocumentToString;
import org.bitbucket.dan2097.structureExtractor.XMLFileToXMLDocument;

import uk.ac.cam.ch.wwmm.opsin.NameToStructure;
import uk.ac.cam.ch.wwmm.opsin.OpsinResult;
import uk.ac.cam.ch.wwmm.opsin.OpsinResult.OPSIN_RESULT_STATUS;

public class ProcessFolder {
	public static void main(String[] args) throws Exception {
		String inputDirectoryLocation = "C:/My Documents/Patents/USPTO-50/";
		String outputDirectoryLocation = "C:/My Documents/Patents/USPTO-50/OPSIN/";
		
		File inputDirectory = new File(inputDirectoryLocation);
		if (!inputDirectory.isDirectory()){
			throw new Exception(inputDirectory.getAbsolutePath() +" is not a directory!");
		}
		File outputDirectory = new File(outputDirectoryLocation);
		if (!outputDirectory.exists()){
			outputDirectory.mkdir();
		}
		File[] files = inputDirectory.listFiles();
		XMLDocumentToString xmlDocumentToString = new XMLDocumentToString();
		NameToStructure n2s = NameToStructure.getInstance();
		for (int i = 0; i < files.length; i++) {
			File inputFile =files[i];
			if (!inputFile.isFile()){
				continue;
			}
			File outputFile = new File(outputDirectory, inputFile.getName().substring(0,9)+".name");
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
			Document doc = XMLFileToXMLDocument.readXMLFileUsingXomBuilder(inputFile);
			List<String> chunks = xmlDocumentToString.convertDocumentToNewLineDelimitedList(doc);
			for (String chunk : chunks) {
				List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames(chunk);
				for (IdentifiedChemicalName identifiedChemicalName : identifiedNames) {
					OpsinResult or = n2s.parseChemicalName(identifiedChemicalName.getValue(), false);
					if (or.getStatus() != OPSIN_RESULT_STATUS.FAILURE){
						writer.write(identifiedChemicalName.getValue() +"\n");
					}
				}
			}
			writer.close();
		}
	}
}
