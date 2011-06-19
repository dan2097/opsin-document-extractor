package org.bitbucket.dan2097.structureExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import uk.ac.cam.ch.wwmm.opsin.NameToStructure;
import uk.ac.cam.ch.wwmm.opsin.NameToStructureException;
import uk.ac.cam.ch.wwmm.opsin.ParseRules;
import uk.ac.cam.ch.wwmm.opsin.ParseRulesResults;
import uk.ac.cam.ch.wwmm.opsin.ParseTokens;
import uk.ac.cam.ch.wwmm.opsin.StringTools;

public class DocumentToStructures {
	
	private static final Pattern matchWhiteSpace = Pattern.compile("\\s+");
	private static final char END_OF_MAINGROUP = '\u00e2';
	private static final char END_OF_FUNCTIONALTERM = '\u00FB';
	private static ParseRules pr;

	static{
		try {
			pr = NameToStructure.getOpsinParser();
		} catch (NameToStructureException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Convenience method:
	 * Splits on white space then extracts names
	 * @param sentence
	 * @return
	 * @throws Exception
	 */
	public static List<IdentifiedChemicalName> extractNames(String sentence) throws Exception{
		String[] words = matchWhiteSpace.split(sentence);
		return extractNames(words);
	}

	/**
	 * Given an array of words not containing white space extracts all the chemical names that are interpretable by OPSIN
	 * @param words
	 * @return
	 */
	public static List<IdentifiedChemicalName> extractNames(String[] words){
		List<IdentifiedChemicalName> identifiedChemicalNames = new ArrayList<IdentifiedChemicalName>();
		int wordsLength =words.length;
		String[] normalisedWords = new String[wordsLength];
		for (int i = wordsLength -1; i >=0; i--) {
			normalisedWords[i] = PreProcesssor.preProcess(words[i]);
		}
		StringBuilder chemicalNameBuffer = new StringBuilder();//holds the current chemical name under consideration
		int totalSpacesRemoved =0;//running total of spaces removed for the current name
		for (int i = 0; i < wordsLength; i++) {
			String stringToTest= (i+1) < wordsLength ? normalisedWords[i] + " "+ normalisedWords[i+1] : normalisedWords[i];//attempt to parse the word plus the next word
			ParseRulesResults prr = getParses(stringToTest);
			int spacesRemoved = 0;
			if (prr.getParseTokensList().size()==0 && (i+1 ) < wordsLength){//completely uninterpretable as is
				stringToTest=normalisedWords[i] + normalisedWords[i+1];//attempt space removal
				prr = getParses(stringToTest);
				if (prr.getParseTokensList().size()!=0){//space removal made some of the input interpretable!
					spacesRemoved++;
				}
			}
			if (prr.getParseTokensList().size()==0){//input could not be used to form a chemical name
				if (!chemicalNameBuffer.toString().equals("")){//a chemical name is already in the buffer, add it to the identifiedChemicalNames
					String name =chemicalNameBuffer.toString();
					int startingIndice = i-(matchWhiteSpace.split(name).length + spacesRemoved + totalSpacesRemoved);
					int finalIndice = i -1;
					identifiedChemicalNames.add(createIdentifiedName(name, startingIndice, finalIndice, words));
					chemicalNameBuffer = new StringBuilder();
				}
				totalSpacesRemoved =0;
			}
			else{
				String uninterpretableName = prr.getUninterpretableName();
				String uninterpretedWordSection = matchWhiteSpace.split(uninterpretableName)[0];
				if (uninterpretedWordSection.length() >=2 || (uninterpretedWordSection.length() ==1 && (Character.isLetter(uninterpretedWordSection.charAt(0)) || Character.isDigit(uninterpretedWordSection.charAt(0))))){
					//uninterpretable as is
					SpaceRemovalResult srr =attemptSpaceRemoval(normalisedWords, i, uninterpretableName, stringToTest, wordsLength);
					if (srr.isSuccess()){
						prr = srr.getParseRulesResults();
						uninterpretableName = prr.getUninterpretableName();
						uninterpretedWordSection = matchWhiteSpace.split(uninterpretableName)[0];
						spacesRemoved +=srr.getSpacesRemoved();
					}
					else if(!fullWordImmediatelyFollowedByBracket(prr, uninterpretedWordSection)){//generally name is uninterpretable but exception made for a full name followed by a bracket e.g. pyridine(5ml)
						chemicalNameBuffer = new StringBuilder();
						continue;
					}
				}
				else if (isFullWord(prr) && !nextWordIsFullWord(normalisedWords, i, uninterpretableName, wordsLength)){
					//e.g. benzene sulfonamide
					SpaceRemovalResult srr =attemptSpaceRemoval(normalisedWords, i, uninterpretableName, stringToTest, wordsLength);
					if (srr.isSuccess()){
						prr = srr.getParseRulesResults();
						uninterpretableName = prr.getUninterpretableName();
						uninterpretedWordSection = matchWhiteSpace.split(uninterpretableName)[0];
						spacesRemoved +=srr.getSpacesRemoved();
					}
				}
				if (!chemicalNameBuffer.toString().equals("")){
					chemicalNameBuffer.append(" ");
				}
				String parsedOpsinNormalisedText =StringTools.stringListToString(prr.getParseTokensList().get(0).getTokens(), "");
				chemicalNameBuffer.append(parsedOpsinNormalisedText);
				if (parsedOpsinNormalisedText.indexOf(' ')!=-1){//both words were partially or fully interpreted
					i++;
				}
				i = i +spacesRemoved;
				if (uninterpretedWordSection.length()==1 || fullOrFunctionalWordFollowedByBracket(prr, words, i)){//encountered punctuation or next word is likely to be irrelevant/a synonymn
					String name =chemicalNameBuffer.toString();
					int startingIndice = i + 1 -(matchWhiteSpace.split(name).length + spacesRemoved + totalSpacesRemoved);
					int finalIndice = i;
					identifiedChemicalNames.add(createIdentifiedName(name, startingIndice, finalIndice, words));
					chemicalNameBuffer = new StringBuilder();
					totalSpacesRemoved =0;
				}
				else{
					totalSpacesRemoved+=spacesRemoved;
				}
			}
		}
		if (!chemicalNameBuffer.toString().equals("")){
			String name =chemicalNameBuffer.toString();
			int startingIndice = wordsLength - (matchWhiteSpace.split(name).length + totalSpacesRemoved);
			int finalIndice = wordsLength -1;
			identifiedChemicalNames.add(createIdentifiedName(name, startingIndice, finalIndice, words));
		}
		return identifiedChemicalNames;
	}

	private static boolean nextWordIsFullWord(String[] normalisedWords, int i,String uninterpretableName, int wordsLength) {
		String secondWord =null;
		if (matchWhiteSpace.split(uninterpretableName).length==2 && (i+1)<wordsLength){
			secondWord = normalisedWords[i+1];
		}
		else if ((i+2)<wordsLength){
			secondWord = normalisedWords[i+2];
		}
		if (secondWord!=null){
			return isFullWord(getParses(secondWord));
		}
		return false;
	}

	private static ParseRulesResults getParses(String stringToTest) {
		try {
			return pr.getParses(stringToTest);
		} catch (Exception e) {//if OPSIN cannot unambiguously parse something, or something that really shouldn't happen happens
			return new ParseRulesResults(new ArrayList<ParseTokens>(), stringToTest, stringToTest);
		}
	}

	private static SpaceRemovalResult attemptSpaceRemoval(String[] words, int i, String uninterpretableName, String stringToTest, int wordsLength) {
		int spacesRemoved = 0;
		if (matchWhiteSpace.split(uninterpretableName).length==2 && (i+1)<wordsLength){//is the space between the words erroneous
			stringToTest = words[i] + words[i+1];
			ParseRulesResults prr = getParses(stringToTest);
			uninterpretableName = prr.getUninterpretableName();
			String uninterpretedWordSection = matchWhiteSpace.split(uninterpretableName)[0];
			spacesRemoved++;
			if (uninterpretedWordSection.length() ==0 || (uninterpretedWordSection.length() ==1  && words[i+1].length() > 1 && !Character.isLetter(uninterpretedWordSection.charAt(0)) && !Character.isDigit(uninterpretedWordSection.charAt(0)))){
				return new SpaceRemovalResult(true, spacesRemoved, prr);
			}
		}
		String parsedOpsinNormalisedText =StringTools.stringListToString(getParses(stringToTest).getParseTokensList().get(0).getTokens(), "");
		String newParsedOpsinNormalisedText = parsedOpsinNormalisedText;
		int indiceTojoin =i+2;
		do {//join with subsequent words until either the chemical name is fully interpretable or the join does not increase the amount of interpretable name
			if (indiceTojoin >=wordsLength){
				break;
			}
			parsedOpsinNormalisedText = newParsedOpsinNormalisedText;
			stringToTest+=words[indiceTojoin];
			ParseRulesResults prr = getParses(stringToTest);
			newParsedOpsinNormalisedText = StringTools.stringListToString(prr.getParseTokensList().get(0).getTokens(), "");
			uninterpretableName = prr.getUninterpretableName();
			String uninterpretedWordSection = matchWhiteSpace.split(uninterpretableName)[0];
			spacesRemoved++;
			if (uninterpretedWordSection.length() ==0 || (uninterpretedWordSection.length() ==1 && words[indiceTojoin].length() > 1 && !Character.isLetter(uninterpretedWordSection.charAt(0)) && !Character.isDigit(uninterpretedWordSection.charAt(0)))){
				return new SpaceRemovalResult(true, spacesRemoved, prr);
			}
			indiceTojoin++;
		}
		while (newParsedOpsinNormalisedText.length()> parsedOpsinNormalisedText.length());
		return new SpaceRemovalResult(false, spacesRemoved, null);
	}

	private static boolean fullWordImmediatelyFollowedByBracket(ParseRulesResults prr, String uninterpretedWordSection) {
		//exception made for full chemical followed by bracketed section
		Character firstLetter= uninterpretedWordSection.charAt(0);
		if (isFullWord(prr) && isOpenBracket(firstLetter)){
			return true;
		}
		return false;
	}
	
	private static boolean fullOrFunctionalWordFollowedByBracket(ParseRulesResults prr,String[] words, int i) {
		if ((isFullWord(prr) || isFunctionalWord(prr)) && i+1 <words.length){
			Character firstLetter = words[i+1].charAt(0);
			if (isOpenBracket(firstLetter)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Determines whether a parse rules results describes a "full" word by checking whether the final annotation is an END_OF_MAINGROUP
	 * @param prr
	 * @return
	 */
	private static boolean isFullWord(ParseRulesResults prr){
		if (prr.getParseTokensList().size()==0){
			return false;
		}
		List<Character> annotations = prr.getParseTokensList().get(0).getAnnotations();
		if (annotations.size()==0){
			return false;
		}
		Character finalAnnotation = annotations.get(annotations.size() -1);
		return finalAnnotation.equals(END_OF_MAINGROUP);
	}
	
	/**
	 * Determines whether a parse rules results describes a "functional" word by checking whether the final annotation is an END_OF_FUNCTIONALTERM
	 * @param prr
	 * @return
	 */
	private static boolean isFunctionalWord(ParseRulesResults prr){
		if (prr.getParseTokensList().size()==0){
			return false;
		}
		List<Character> annotations = prr.getParseTokensList().get(0).getAnnotations();
		if (annotations.size()==0){
			return false;
		}
		Character finalAnnotation = annotations.get(annotations.size() -1);
		return finalAnnotation.equals(END_OF_FUNCTIONALTERM);
	}

	private static int numberOfOpenbrackets(String name) {
		int bracketLevel = 0;
		int stringLength  = name.length();
		for(int i = 0 ; i < stringLength; i++) {
			char c = name.charAt(i);
			if(isOpenBracket(c)) {
				bracketLevel++;
			}
			else if(isCloseBracket(c)) {
				bracketLevel--;
			}
		}
		return bracketLevel;
	}

	private static boolean isOpenBracket(char c){
		return c == '(' || c == '[' || c == '{';
	}

	private static boolean isCloseBracket(char c){
		return c == ')' || c == ']' || c == '}';
	}
	
	/**
	 * Removes non chemical brackets, updating the starting and final indices appropriately before returning a new IdentifiedChemicalName
	 * @param name
	 * @param startingIndice
	 * @param finalIndice
	 * @param words
	 * @return
	 */
	private static IdentifiedChemicalName createIdentifiedName(String name,int startingIndice, int finalIndice, String[] words) {
		boolean frontBracketRemoved =false;
		boolean endBracketRemoved =true;
		Character firstLetter= name.charAt(0);
		Character lastLetter= name.charAt(name.length()-1);
		int openBrackets = numberOfOpenbrackets(name);
		if (openBrackets==1){
			if (isOpenBracket(firstLetter)){
				name = name.substring(1);
				frontBracketRemoved =true;
			}
		}
		else if (openBrackets==-1){
			if (isCloseBracket(lastLetter)){
				name = name.substring(0, name.length()-1);
				endBracketRemoved =true;
			}
		}
		else if (openBrackets==0){;
			if (isOpenBracket(firstLetter) && isCloseBracket(lastLetter)){
				name = name.substring(1, name.length()-1);
				frontBracketRemoved =true;
				endBracketRemoved =true;
			}
		}
		if (frontBracketRemoved && words[startingIndice].length()==1 && isOpenBracket(words[startingIndice].charAt(0))){
			startingIndice++;
		}
		if (endBracketRemoved && words[finalIndice].length()==1 && isCloseBracket(words[finalIndice].charAt(0))){
			finalIndice--;
		}
		return new IdentifiedChemicalName(startingIndice, finalIndice, name, extractRawText(words, startingIndice, finalIndice));
	}

	private static String extractRawText(String[] words, int startingIndice, int finalIndice) {
		StringBuilder rawTextBuilder =new StringBuilder();
		for (int i = startingIndice; i <= finalIndice; i++) {
			rawTextBuilder.append(' ');
			rawTextBuilder.append(words[i]);
		}
		return rawTextBuilder.toString().substring(1);
	}
	
	public static void main(String[] args) throws Exception {
		String input ="ethylene glycol, Propylene glycol, 1,3- propanediol, 1,2-butanediol, 1,3-butanediol, 1,4-butanediol, 2,3-butanediol, 1,2-pentanediol, 1,5-pentanediol, 1,2-hexanediol, 1,6-hexanediol, 1,2-heptanediol, 1,7-heptanediol, 1,2-octanediol, 1,8-octanediol, 1,2-decanediol, 1,10-decanediol, 3-methyl-1,2-butanediol, 3,3-dimethyl-1,2-butanediol, 4-methyl-1,2-pentanediol, 5-methyl-1,2-hexanediol, 3-chloro- 1,2-propanediol, 3-butene-1,2-diol, 4-pentene-1,2-diol, 1-phenylethane-1,2-diol, 1-(4-methylphenyl)ethane-1,2-diol, 1-(4-methoxyphenyl)ethane-1,2-diol, 1-(4-chlorophenyl)ethane-1,2-diol, 1-(4-nitrophenyl)ethane-1,2-diol, 1-cyclohexylethane- 1,2-diol, 1,2-cyclohexanediol,";
		List<IdentifiedChemicalName> identifiedNames = extractNames(matchWhiteSpace.split(input));
		for (IdentifiedChemicalName identifiedChemicalName : identifiedNames) {
			System.out.println(identifiedChemicalName.getTextValue());
		}
	}
}
