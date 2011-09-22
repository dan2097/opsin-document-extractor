package org.bitbucket.dan2097.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

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
		BufferedReader input = new BufferedReader(new FileReader("C:/My Documents/Patents/Extracting names for roger/expected.inchi"));
		String line = null;
		List<String> normalisedStereoReferenceInChIs = new ArrayList<String>();
		List<String> normalisedNoStereoReferenceInChIs = new ArrayList<String>();
		while ((line = input.readLine()) != null) {
			normalisedStereoReferenceInChIs.add(InchiPruner.mainChargeAndStereochemistryLayers(line));
			normalisedNoStereoReferenceInChIs.add(InchiPruner.mainAndChargeLayers(line));
		}
		input.close();
		
		String inputDirectoryLocation = "C:/My Documents/Patents/Extracting names for roger/USPTO-50xml/";
		String outputDirectoryLocation = "C:/My Documents/Patents/Extracting names for roger/USPTO-50xml/OPSIN2/";
		
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
		int patentCounter =0;
		for (int i = 0; i < files.length; i++) {
			File inputFile =files[i];
			if (!inputFile.isFile()){
				continue;
			}
			File outputFile = new File(outputDirectory, inputFile.getName().substring(0,9)+".name");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),"UTF-8"));
			List<IdentifiedChemicalName> identifiedNames = new ArrayList<IdentifiedChemicalName>();
			Document doc = XMLFileToXMLDocument.readXMLFileUsingHtmlTagSoup(inputFile);
			List<String> chunks = xmlDocumentToString.convertDocumentToNewLineDelimitedList(doc);
			for (String chunk : chunks) {
				identifiedNames.addAll(new DocumentToStructures(chunk).extractNames());
			}
			IdentifiedChemicalName matchForDrug  = null;
			IdentifiedChemicalName matchForDrugNoStereo  = null;
			for (IdentifiedChemicalName identifiedChemicalName : identifiedNames) {
				OpsinResult or = n2s.parseChemicalName(identifiedChemicalName.getChemicalName());
				if (or.getStatus() != OPSIN_RESULT_STATUS.FAILURE){
					writer.write(identifiedChemicalName.getChemicalName() +"\n");
					String inchi =NameToInchi.convertResultToInChI(or);
					if (inchi!=null && matchForDrug==null){
						String normalisedStereoInChI = InchiPruner.mainChargeAndStereochemistryLayers(inchi);
						if (normalisedStereoInChI.equals(normalisedStereoReferenceInChIs.get(patentCounter))){
							matchForDrug = identifiedChemicalName;
						}
						else if (matchForDrugNoStereo==null &&
								InchiPruner.mainAndChargeLayers(inchi).equals(normalisedNoStereoReferenceInChIs.get(patentCounter))){
							matchForDrugNoStereo = identifiedChemicalName;
						}
					}
				}
			}
			System.out.print(inputFile.getName().substring(0, 9));
			if (matchForDrug !=null){
				System.out.print(" stereomatched:\t" +  matchForDrug.getTextValue() + "\t" + matchForDrug.getChemicalName());
			}
			else if (matchForDrugNoStereo !=null){
				System.out.print(" stereodidnotmatch:\t" +  matchForDrugNoStereo.getTextValue() + "\t" + matchForDrugNoStereo.getChemicalName());
			}
			System.out.println("");
			patentCounter++;
			writer.close();
		}
	}
}
