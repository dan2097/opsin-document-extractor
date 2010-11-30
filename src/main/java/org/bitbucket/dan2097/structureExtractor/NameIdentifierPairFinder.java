package org.bitbucket.dan2097.structureExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bitbucket.dan2097.structureExtractor.NameIdentifierPair.IdentifierType;

import uk.ac.cam.ch.wwmm.opsin.NameToStructure;
import uk.ac.cam.ch.wwmm.opsin.NameToStructureException;
import uk.ac.cam.ch.wwmm.opsin.OpsinResult;
import uk.ac.cam.ch.wwmm.opsin.OpsinResult.OPSIN_RESULT_STATUS;


public class NameIdentifierPairFinder {

	private static final Pattern matchWhiteSpace = Pattern.compile("\\s+");
	private final static Pattern matchComma = Pattern.compile(",[ ]?");
	private final static Pattern matchDelimitedIdentifier = Pattern.compile("[\\[{\\(,;](\\S+)[,;]");
	private final static Pattern matchbracketedIdentifier = Pattern.compile("[\\[{\\(](.+?)[\\]}\\)]");
	private final static Pattern hasDigits = Pattern.compile("[0-9]");
	private final static NameToStructure n2s;
	
	static{
		 try {
			n2s = NameToStructure.getInstance();
		} catch (NameToStructureException e) {
			throw new RuntimeException("Unable to initialise OPSIN", e);
		}
	}
	
	public static List<NameIdentifierPair> extractNameIdentifierPairs(String fullText) throws Exception{
		String[] words = matchWhiteSpace.split(PreProcesssor.preProcess(fullText));
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames(words);
		List<IdentifiedChemicalName> resolvedChemicalNames = new ArrayList<IdentifiedChemicalName>();
		for (IdentifiedChemicalName identifiedChemicalName : identifiedNames) {
			//System.out.println(identifiedChemicalName.getValue());
			OpsinResult or = n2s.parseChemicalName(identifiedChemicalName.getValue(), false);
			if (or.getStatus() != OPSIN_RESULT_STATUS.FAILURE){
				identifiedChemicalName.setOpsinResult(or);
				resolvedChemicalNames.add(identifiedChemicalName);
			}
		}
		for (IdentifiedChemicalName resolvedChemicalName : resolvedChemicalNames) {
			System.out.println(resolvedChemicalName.getValue());
		}
		return null;
	}

	private static List<NameIdentifierPair> findIdentifiers(String name, String[] words, int i) {
		List<NameIdentifierPair> nameIdentifierPairs = new ArrayList<NameIdentifierPair>();
		if (i < words.length){
			String nextWord = joinWordsIfBracketsAreUnbalanced(words[i], i, words);
			nameIdentifierPairs = findIdentifiers(name, nextWord);
		}
		if (nameIdentifierPairs.size()==0){
			if (i -1 >=0){
				nameIdentifierPairs = findIdentifiers(name, words[i-1]);
			}
		}
		return nameIdentifierPairs;
	}

	static List<NameIdentifierPair> findIdentifiers(String name, String nextWord) {
		List<NameIdentifierPair> nameIdentifierPairs = new ArrayList<NameIdentifierPair>();
		Matcher m = matchbracketedIdentifier.matcher(nextWord);
		if (m.lookingAt()){
			String[] identifiers = matchComma.split(m.group(1));
			for (String identifier : identifiers) {
				if (identifier.contains("%")){
					continue;
				}
				m = hasDigits.matcher(identifier);
				if (m.find()){
					nameIdentifierPairs.add(new NameIdentifierPair(name, identifier, IdentifierType.identifier));
				}
				else{
					nameIdentifierPairs.add(new NameIdentifierPair(name, identifier, IdentifierType.alias));
				}
			}
		}
		else{
			m = matchDelimitedIdentifier.matcher(nextWord);
			if (m.matches()){
				String identifier = m.group(1);
				m = hasDigits.matcher(identifier);
				if (m.find() && identifier.length()<15){
					nameIdentifierPairs.add(new NameIdentifierPair(name, identifier, IdentifierType.identifier));
				}
				else{
					nameIdentifierPairs.add(new NameIdentifierPair(name, identifier, IdentifierType.alias));
				}
			}
		}

		return nameIdentifierPairs;
	}
	
	
	private static String joinWordsIfBracketsAreUnbalanced(String word, int wordIndice, String[] words) {
		int firstChar = word.charAt(0);
		if(firstChar == '(' || firstChar == '[' || firstChar == '{') {
			int stringLength  = word.length();
			for(int i = 1 ; i < stringLength; i++) {
				char c = word.charAt(i);
				if(c == '(' || c == '[' || c == '{') {
					return word;
				}
				else if(c == ')' || c == ']' || c == '}') {
					return word;
				}
			}
			if (wordIndice +1 <words.length){
				return joinWordsIfBracketsAreUnbalanced(word + " " + words[wordIndice+1] ,++wordIndice, words);
			}
		}
		return word;
	}
}
