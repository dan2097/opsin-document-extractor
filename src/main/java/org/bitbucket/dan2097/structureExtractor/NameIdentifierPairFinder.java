package org.bitbucket.dan2097.structureExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bitbucket.dan2097.structureExtractor.NameIdentifierPair.IdentifierType;

import uk.ac.cam.ch.wwmm.opsin.NameToStructure;
import uk.ac.cam.ch.wwmm.opsin.NameToStructureException;
import uk.ac.cam.ch.wwmm.opsin.ParseRules;
import uk.ac.cam.ch.wwmm.opsin.ParseRulesResults;
import uk.ac.cam.ch.wwmm.opsin.OpsinResult.OPSIN_RESULT_STATUS;
import uk.ac.cam.ch.wwmm.oscar3.chemnamedict.ChemNameDictSingleton;

public class NameIdentifierPairFinder {

	private static ParseRules pr;
	private final static Pattern matchWhitespace = Pattern.compile("\\s+");
	private final static Pattern matchComma = Pattern.compile(",[ ]?");
	private final static Pattern matchDelimitedIdentifier = Pattern.compile("[\\[{\\(,;](\\S+)[,;]");
	private final static Pattern matchbracketedIdentifier = Pattern.compile("[\\[{\\(](.+?)[\\]}\\)]");
	private final static Pattern hasDigits = Pattern.compile("[0-9]");
	
	static{
		try {
			pr = NameToStructure.getOpsinParser();
		} catch (NameToStructureException e) {
			throw new RuntimeException("Unable to intialise OPSIN parser", e);
		}
	}
	
	public static List<NameIdentifierPair> extractNameIdentifierPairs(String fullText) throws Exception{
	    String[] words = matchWhitespace.split(fullText);
	    List<NameIdentifierPair> nameIdentifierPairs = new ArrayList<NameIdentifierPair>();
	    StringBuilder nameBuffer = new StringBuilder();
	    int wordCount = words.length;
	    for (int i = 0; i < wordCount; i++) {
	    	String word = words[i]; 
	    	String terminalNonLetter = "";
	    	if (word.length()>0){
	    		char lastLetter =word.charAt(word.length()-1);
		    	if (!Character.isLetter(lastLetter)&& lastLetter!=')' && lastLetter!=']' && lastLetter!='}'){
		    		word = word.substring(0, word.length()-1);
		    		terminalNonLetter =String.valueOf(lastLetter);
		    	}
	    	}
	    	if (nameBuffer.length()!=0 && word.equalsIgnoreCase("acid")){//special case for acid
	    		nameBuffer.append(" ");
	    		nameBuffer.append(word);
	    		if (i +1 < words.length){
	    			words[i+1] = terminalNonLetter + words[i+1];
	    		}
	    		continue;
	    	}
	    	ParseRulesResults prr = pr.getParses(word);
	    	if (prr.getUninterpretableName().length()==0){
	    		if (nameBuffer.length()!=0){
	    			nameBuffer.append(" ");
	    		}
	    		if (i +1 < words.length){
	    			words[i+1] = terminalNonLetter + words[i+1];
	    		}
	    		nameBuffer.append(word);
	    	}
	    	else{
	    		List<NameIdentifierPair>  identifiersForThisName = new ArrayList<NameIdentifierPair>();
	    		if (!nameBuffer.toString().equals("")){
	    			String name = nameBuffer.toString();
	    			identifiersForThisName = findIdentifiers(name, words, i);
	    			if (identifiersForThisName.size()>0 && OPSIN_RESULT_STATUS.FAILURE.equals(NameToStructure.getInstance().parseChemicalName(name, false).getStatus())){
	    				identifiersForThisName.clear();
	    			}
	    		}
		    	String inchi = ChemNameDictSingleton.getInChIForShortestSmiles(word);
		    	if (inchi !=null){
		    		if (i +1 < words.length){
		    			words[i+1] = terminalNonLetter + words[i+1];
		    		}
		    		identifiersForThisName = findIdentifiers(word, words, i +1);
		    	}
    			nameIdentifierPairs.addAll(identifiersForThisName);
	    		nameBuffer = new StringBuilder();
	    	}
		}
		return nameIdentifierPairs;
	}

	private static List<NameIdentifierPair> findIdentifiers(String name, String[] words, int i) {
		if (i < words.length){
			String nextWord = joinWordsIfBracketsAreUnbalanced(words[i], i, words);
			return findIdentifiers(name, nextWord);
		}
		return new ArrayList<NameIdentifierPair>();
	}

	static List<NameIdentifierPair> findIdentifiers(String name, String nextWord) {
		List<NameIdentifierPair> nameIdentifierPairs = new ArrayList<NameIdentifierPair>();
		Matcher m = matchbracketedIdentifier.matcher(nextWord);
		if (m.lookingAt()){
			String[] identifiers = matchComma.split(m.group(1));
			for (String identifier : identifiers) {
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
