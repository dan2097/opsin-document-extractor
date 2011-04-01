package org.bitbucket.dan2097.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;

import org.bitbucket.dan2097.structureExtractor.PreProcesssor;

import uk.ac.cam.ch.wwmm.opsin.NameToStructure;
import uk.ac.cam.ch.wwmm.opsin.NameToStructureConfig;

public class ExtraDictionaryFromChEBIOBO {
	public static void main(String[] args) throws Exception {
		File input =new File("C:/My Documents/OPSIN/resources/chebimar11.obo");
		FileWriter output =new FileWriter(new File("C:/My Documents/OPSIN/resources/dictionary.txt"));
		BufferedReader compoundsFile = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
		String cLine=compoundsFile.readLine();
		NameToStructure n2s = NameToStructure.getInstance();
		NameToStructureConfig config = NameToStructureConfig.getDefaultConfigInstance();
		config.setAllowRadicals(true);
		while(cLine != null) {
			if("[Term]".equals(cLine)) {
				String id = null;
				String inchi =null;
				String smiles =null;
				Set<String> names = new LinkedHashSet<String>();
				while (!"".equals(cLine)){
					cLine = compoundsFile.readLine();
					if(cLine.startsWith("id: ")) {
						id = cLine.substring(10);
					}
					else if(cLine.endsWith("RELATED InChI [ChEBI:]")){
						String[] tempArray = cLine.split("\"");
						inchi = tempArray[1];
					}
					else if(cLine.startsWith("synonym: ") && cLine.endsWith("RELATED SMILES [ChEBI:]")) {
						String[] tempArray = cLine.split("\"");
						smiles = tempArray[1];
					}
					else if(cLine.startsWith("synonym: ") && !(cLine.contains("InChI") || cLine.contains("FORMULA"))) {
						String[] tempArray = cLine.split("\"");
						if (n2s.parseChemicalName(tempArray[1]).getSmiles()==null){
							names.add(tempArray[1].toLowerCase());
						}
					}
					
				}
				if (id!=null && inchi!=null  && smiles!=null && names.size()>0){
					for (String name : names) {
						output.write(PreProcesssor.preProcess(name) +"\t" +inchi +"\t" +smiles+ "\t" +id +"\n");
					}
				}	
			}
			cLine = compoundsFile.readLine();
		}
		compoundsFile.close();
		output.close();
	}

}
