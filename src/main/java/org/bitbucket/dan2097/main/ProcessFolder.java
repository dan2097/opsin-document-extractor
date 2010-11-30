package org.bitbucket.dan2097.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nu.xom.Document;

import org.bitbucket.dan2097.structureExtractor.DocumentToStructures;
import org.bitbucket.dan2097.structureExtractor.IdentifiedChemicalName;
import org.bitbucket.dan2097.structureExtractor.XMLDocumentToString;
import org.bitbucket.dan2097.structureExtractor.XMLFileToXMLDocument;

import uk.ac.cam.ch.wwmm.opsin.InchiPruner;
import uk.ac.cam.ch.wwmm.opsin.NameToInchi;
import uk.ac.cam.ch.wwmm.opsin.NameToStructure;
import uk.ac.cam.ch.wwmm.opsin.OpsinResult;
import uk.ac.cam.ch.wwmm.opsin.OpsinResult.OPSIN_RESULT_STATUS;

public class ProcessFolder {
	public static void main(String[] args) throws Exception {
		String inputDirectoryLocation = "C:/My Documents/Patents/USPTO-50xml/";
		String outputDirectoryLocation = "C:/My Documents/Patents/USPTO-50xml/OPSIN/";
		
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
		Set<String> normalisedInChIs = new HashSet<String>();
		for (int i = 0; i < files.length; i++) {
			File inputFile =files[i];
			if (!inputFile.isFile()){
				continue;
			}
			File outputFile = new File(outputDirectory, inputFile.getName().substring(0,9)+".name");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),"UTF-8"));
			//File outputFile2 = new File(outputDirectory, inputFile.getName().substring(0,9)+".in");
			//BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile2),"UTF-8"));
			Document doc = XMLFileToXMLDocument.readXMLFileUsingHtmlTagSoup(inputFile);
			List<String> chunks = xmlDocumentToString.convertDocumentToNewLineDelimitedList(doc);
			for (String chunk : chunks) {
				List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames(chunk);
				//writer2.append(chunk +"\n");
				for (IdentifiedChemicalName identifiedChemicalName : identifiedNames) {
					OpsinResult or = n2s.parseChemicalName(identifiedChemicalName.getValue(), false);
					if (or.getStatus() != OPSIN_RESULT_STATUS.FAILURE){
						writer.write(identifiedChemicalName.getValue() +"\n");
						String inchi =NameToInchi.convertResultToInChI(or, false);
						if (inchi!=null){
							normalisedInChIs.add(InchiPruner.mainChargeAndStereochemistryLayers(inchi));
						}
					}
				}
			}
			writer.close();
			//writer2.close();
		}
		BufferedReader input = new BufferedReader(new FileReader("C:/My Documents/Patents/expected.inchi"));
		String line = null;
		int i=0;
		int j=0;
		System.out.println(normalisedInChIs.size());
		while ((line = input.readLine()) != null) {
			String inchi = InchiPruner.mainChargeAndStereochemistryLayers(line);
			j++;
			if (normalisedInChIs.contains(inchi)){
				System.out.println(j);
				i++;
			}
		}
		System.out.println(i);
		input.close();
	}
}
