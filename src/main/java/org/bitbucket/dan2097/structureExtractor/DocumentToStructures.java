package org.bitbucket.dan2097.structureExtractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.cam.ch.wwmm.opsin.NameToStructure;
import uk.ac.cam.ch.wwmm.opsin.NameToStructureException;
import uk.ac.cam.ch.wwmm.opsin.OpsinPreProcessorWrapper;
import uk.ac.cam.ch.wwmm.opsin.ParseRules;
import uk.ac.cam.ch.wwmm.opsin.ParseRulesResults;
import uk.ac.cam.ch.wwmm.opsin.ParseTokens;
import uk.ac.cam.ch.wwmm.opsin.StringTools;

public class DocumentToStructures {
	
	private static final Pattern matchWhiteSpace = Pattern.compile("\\s+");
	private static final Pattern tokenPattern = Pattern.compile("[\\S]+");
	private static final char END_OF_MAINGROUP = '\u00e2';
	private static final char END_OF_FUNCTIONALTERM = '\u00FB';
	private static final char END_OF_SUBSTITUENT = '\u00e9';
	private static ParseRules pr;
	/**These are words that are either interpreted erroneously as chemicals or have a nasty tendency to be interpreted as chemical when space removal is invoked*/
	private static final Set<String> stopWords = new HashSet<String>(Arrays.asList("period", "periodic", "on", "one", "it", "at", "an", "in", "brine", "n2", "n2,", "o2", "o2,", "f2", "f2,", "cl2", "cl2,", "br2", "br2,", "i2", "i2,"));
	
	private final String[] words;
	private final int wordsLength;
	private final String[] normalisedWords;
	private final List<Integer> wordStartIndices = new ArrayList<Integer>();
	
	private NameType currentNameType = null;

	static{
		try {
			pr = NameToStructure.getOpsinParser();
		} catch (NameToStructureException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * A string which should be searched for chemicals.
	 * This could be a single word or even a whole document
	 * @param stringToFindChemicalsIn
	 */
	public DocumentToStructures(String stringToFindChemicalsIn) {
		Matcher m = tokenPattern.matcher(stringToFindChemicalsIn);
		List<String> words = new ArrayList<String>();
		while (m.find()) {
			wordStartIndices.add(m.start());
			words.add(m.group());
		}
		this.words = words.toArray(new String[words.size()]);
		wordsLength = this.words.length;
		normalisedWords = generateNormalisedWords();
	}

	/**
	 * A list of tokens not containing whitespace and not of zero length
	 * The start/end token indices are calculated on the assumption of one space between each token
	 * The word position indices are hence more likely to be applicable
	 * @param words
	 */
	public DocumentToStructures(List<String> tokens) {
		int indice =-1;//assume no starting space
		for (String token : tokens) {
			indice++;
			if (token.length()==0){
				throw new IllegalArgumentException("Token list contained a zero length token");
			}
			if (matchWhiteSpace.matcher(token).find()){
				throw new IllegalArgumentException("Token contained white space:" +token);
			}
			wordStartIndices.add(indice);
			indice += token.length();
		}
		words = tokens.toArray(new String[tokens.size()]);
		wordsLength = words.length;
		normalisedWords = generateNormalisedWords();
	}
	
	/**
	 * These are the words given as input to the program
	 * @return
	 */
	public String[] getWords() {
		return words;
	}

	/**
	 * These are the normalised words the program actually operated on
	 * @return
	 */
	String[] getNormalisedWords() {
		return normalisedWords;
	}

	/**
	 * Extracts chemical names that are parsable by OPSIN
	 * The chemical names know their position within the array of words used for input
	 * Both the normalised chemical name and the raw text from which it was extracted is returned
	 * @return
	 */
	public List<IdentifiedChemicalName> extractNames() {
		List<IdentifiedChemicalName> identifiedChemicalNames = new ArrayList<IdentifiedChemicalName>();
		StringBuilder chemicalNameBuffer = new StringBuilder();//holds the current chemical name under consideration
		int totalSpacesRemoved =0;//running total of spaces removed for the current name
		for (int i = 0; i < wordsLength; i++) {
			if (chemicalNameBuffer.length()==0 && tooAmbiguousToClassifyAsChemical(normalisedWords[i])){//stray digits and the like cannot be assumed to be part of a chemical name
				continue;
			}
			String stringToTest= (i+1) < wordsLength ? normalisedWords[i] + " "+ normalisedWords[i+1] : normalisedWords[i];//attempt to parse the word plus the next word
			ParseRulesResults prr = getParses(stringToTest);
			int spacesRemoved = 0;
			if (prr.getParseTokensList().size()==0 && (i+1 ) < wordsLength){//completely uninterpretable as is
				stringToTest = normalisedWords[i] + normalisedWords[i+1];//attempt space removal
				prr = getParses(stringToTest);
				if (prr.getParseTokensList().size()!=0){//space removal made some of the input interpretable!
					spacesRemoved++;
				}
			}
			if (prr.getParseTokensList().size()==0){//input could not be used to form a chemical name
				if (!chemicalNameBuffer.toString().equals("")){//a chemical name is already in the buffer, add it to the identifiedChemicalNames
					String name =chemicalNameBuffer.toString();
					int startWordIndice = i-(matchWhiteSpace.split(name).length + spacesRemoved + totalSpacesRemoved);
					int endWordIndice = i -1;
					identifiedChemicalNames.add(createIdentifiedName(name, startWordIndice, endWordIndice, 0));
					chemicalNameBuffer = new StringBuilder();
					currentNameType = null;
				}
				totalSpacesRemoved =0;
			}
			else{
				String[] uninterpretableNameArray = matchWhiteSpace.split(prr.getUninterpretableName());
				String uninterpretedWordSection = uninterpretableNameArray[0];
				int indiceOfNextUnusedWord = uninterpretableNameArray.length==2 ? i+1 : i+2;
				String nextWord = indiceOfNextUnusedWord < wordsLength ? normalisedWords[indiceOfNextUnusedWord] : null;
				if (!isEmptyStringOrSinglePunctuationCharacter(uninterpretedWordSection)){
					//uninterpretable as is
					SpaceRemovalResult srr =attemptSpaceRemoval(indiceOfNextUnusedWord, stringToTest, prr.getUninterpretableName());
					if (srr.isSuccess()){
						prr = srr.getParseRulesResults();
						uninterpretedWordSection = matchWhiteSpace.split(prr.getUninterpretableName())[0];
						spacesRemoved +=srr.getSpacesRemoved();
						indiceOfNextUnusedWord += srr.getSpacesRemoved();
						stringToTest = srr.getInputString();
						nextWord = indiceOfNextUnusedWord < wordsLength ? normalisedWords[indiceOfNextUnusedWord] : null;
					}
					else if (!fullWordImmediatelyFollowedByBracket(prr, uninterpretedWordSection) && !isAnUninterpretableS(uninterpretedWordSection)){
						//exception made for a full name followed by a bracket e.g. pyridine(5ml
						//exception made for a class names e.g. pyridines
						//otherwise the name was still uninterpretable even after space removal and hence is rejected
						chemicalNameBuffer = new StringBuilder();
						continue;
					}
				}
				while (true){//allows this to be called recursively, probably should replace with a more elegant way of doing this
					boolean nextWordAppearsUninterpretable = false;
					if (nextWord !=null){
						nextWordAppearsUninterpretable = wordAppearsUninterpretable(nextWord);
					}
					if (nextWordAppearsUninterpretable && isASpecialCaseWhereSpaceRemovalShouldBeAttempted(prr, nextWord)){
						//e.g. benzene sulfonamide
						SpaceRemovalResult srr =attemptSpaceRemoval(indiceOfNextUnusedWord, stringToTest, prr.getUninterpretableName());
						if (srr.isSuccess()){
							prr = srr.getParseRulesResults();
							uninterpretedWordSection = matchWhiteSpace.split(prr.getUninterpretableName())[0];
							spacesRemoved +=srr.getSpacesRemoved();
							indiceOfNextUnusedWord += srr.getSpacesRemoved();
							stringToTest = srr.getInputString();
							nextWord = indiceOfNextUnusedWord < wordsLength ? normalisedWords[indiceOfNextUnusedWord] : null;
						}
						else{
							break;
						}
					}
					else{
						break;
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
				i += spacesRemoved;

				if (isAnUninterpretableS(uninterpretedWordSection)){//terminal uninterpretable 's' implies a family of compounds
					uninterpretedWordSection = uninterpretedWordSection.substring(1);
					currentNameType = NameType.family;
					chemicalNameBuffer.append('s');
				}
				else{
					adjustCurrentNameType(prr);
				}
				if (currentNameType == NameType.family || 
						i +1==wordsLength ||
						(uninterpretedWordSection.length()==1 && !Character.isLetterOrDigit(uninterpretedWordSection.charAt(0)) ) ||
						fullWordImmediatelyFollowedByBracket(prr, uninterpretedWordSection) ||
						fullOrFunctionalWordFollowedByBracket(prr, i)){//encountered punctuation or next word is likely to be irrelevant/a synonymn
					String name =chemicalNameBuffer.toString();
					int startWordIndice = i + 1 -(matchWhiteSpace.split(name).length + spacesRemoved + totalSpacesRemoved);
					int endWordIndice = i;
					identifiedChemicalNames.add(createIdentifiedName(name, startWordIndice, endWordIndice, uninterpretedWordSection.length()));
					chemicalNameBuffer = new StringBuilder();
					currentNameType = null;
					totalSpacesRemoved =0;
				}
				else{
					totalSpacesRemoved+=spacesRemoved;
				}
			}
		}
		return identifiedChemicalNames;
	}

	/**
	 * Is the uninterpretedWordSection an s e.g. the s of pyridines
	 * s followed by punctuation is also allowed
	 * @param uninterpretedWordSection
	 * @return
	 */
	private boolean isAnUninterpretableS(String uninterpretedWordSection) {
		if (uninterpretedWordSection.length()==1){
			return uninterpretedWordSection.charAt(0)=='s';
		}
		if (uninterpretedWordSection.length()==2 && !Character.isLetterOrDigit(uninterpretedWordSection.charAt(1))){
			return uninterpretedWordSection.charAt(0)=='s';
		}
		return false;
	}

	/**
	 * Checks what word type/s are included in the current prr and adjusts the currentNameType accordingly
	 * @param prr
	 */
	private void adjustCurrentNameType(ParseRulesResults prr) {
		if (prr.getParseTokensList().size()>0){
			List<Character> annotations = prr.getParseTokensList().get(0).getAnnotations();
			String firsToken = (prr.getParseTokensList().get(0).getTokens().size() > 0) ? prr.getParseTokensList().get(0).getTokens().get(0) :"";
			for (Character annotation : annotations) {
				if (annotation.equals(END_OF_MAINGROUP) || annotation.equals(END_OF_SUBSTITUENT) || annotation.equals(END_OF_FUNCTIONALTERM)){
					if (currentNameType == null){
						if (annotation.equals(END_OF_MAINGROUP)){
							currentNameType = NameType.complete;
						}
						else if (annotation.equals(END_OF_SUBSTITUENT)){
							currentNameType = NameType.part;
						}
						else if (firsToken.equals("poly") || firsToken.equals("oligo")){
							currentNameType = NameType.polymer;
						}
						else{
							currentNameType = NameType.family;
						}
					}
					else if (currentNameType == NameType.part){
						if (annotation.equals(END_OF_MAINGROUP)){
							currentNameType = NameType.complete;
						}
						else if (annotation.equals(END_OF_SUBSTITUENT)){
							currentNameType = NameType.part;
						}
						else{
							currentNameType = NameType.complete;
						}
					}
					else if (currentNameType == NameType.family &&
							(annotation.equals(END_OF_MAINGROUP) || annotation.equals(END_OF_SUBSTITUENT))){
						//might in the future occur for CAS names
						currentNameType = NameType.complete;
					}
					//NameType.complete and NameType.polymer just stays as is
				}
			}
		}
	}

	private boolean isASpecialCaseWhereSpaceRemovalShouldBeAttempted(ParseRulesResults prr, String nextWord) {
		if (isFullWord(prr) && !isFullWord(getParses(nextWord))){
			//e.g. benzene sulfonamide
			return true;
		}
		else if (isSubstituentWord(prr) && nextWord.startsWith("-")){
			return true;
		}
		return false;
	}

	private boolean isEmptyStringOrSinglePunctuationCharacter(String uninterpretedWordSection) {
		return uninterpretedWordSection.length() ==0 || (uninterpretedWordSection.length() ==1 && !Character.isLetterOrDigit(uninterpretedWordSection.charAt(0)));
	}

	
	private boolean tooAmbiguousToClassifyAsChemical(String word) {
		if (isMadeOfDigits(word)){
			return true;
		}
		if (word.endsWith("M") && (word.length()==1 || isMadeOfDigits(word.substring(0, word.length()-1)))){
			return true;
		}
		return false;
	}

	private static boolean isMadeOfDigits(String word) {
		for (char charac : word.toCharArray()) {
			if(!Character.isDigit(charac)){
				return false;
			}
		}
		if (word.length()==0){
			return false;
		}
		return true;
	}

	/**
	 * Runs the OPSIN preprocessor over the list of words and marks up problematic words with exclamation marks to prevent their recognition
	 * @return
	 */
	private String[] generateNormalisedWords() {
		String[] normalisedWords = new String[wordsLength];
		for (int i = 0; i < wordsLength; i++) {
			String word = OpsinPreProcessorWrapper.normalise(words[i]);
			if (stopWords.contains(word.toLowerCase()) && !periodicSpecialCase(word, i)){
				word = '_' + word + '_';
			}
			normalisedWords[i] = word;
		}
		return normalisedWords;
	}

	/**
	 * periodic can be chemical if followed by a suitable word
	 * @param word
	 * @param i
	 * @return
	 */
	private boolean periodicSpecialCase(String word, int i) {
		if (word.equals("periodic") && i+1 < wordsLength){
			String nextWord  = words[i+1];
			if (nextWord.equalsIgnoreCase("acid") || isFunctionalWord(getParses(nextWord))){
				return true;
			}
		}
		return false;
	}

	private boolean wordAppearsUninterpretable(String word) {
		String uninterpretedWordSection = getParses(word).getUninterpretableName();
		return !isEmptyStringOrSinglePunctuationCharacter(uninterpretedWordSection);
	}

	private ParseRulesResults getParses(String stringToTest) {
		try {
			return pr.getParses(stringToTest);
		} catch (Exception e) {//if OPSIN cannot unambiguously parse something, or something that really shouldn't happen happens
			return new ParseRulesResults(new ArrayList<ParseTokens>(), stringToTest, stringToTest);
		}
	}

	/**
	 * Attempts to remove spaces to produce something that is interpretable.
	 * If uninterpretableName contains a space then the space within stringToTest may be removed
	 * Otherwise starts joining words starting at indiceOfNextUnusedWord
	 * @param indiceOfNextUnusedWord
	 * @param stringToTest
	 * @param uninterpretableName
	 * @return
	 */
	private SpaceRemovalResult attemptSpaceRemoval(int indiceOfNextUnusedWord, String stringToTest, String uninterpretableName) {
		int spacesRemoved = 0;
		int indiceTojoin = indiceOfNextUnusedWord;
		if (matchWhiteSpace.split(uninterpretableName).length==2){//is the space between the words erroneous
			String[] stringToTestArray = matchWhiteSpace.split(stringToTest);
			stringToTest = stringToTestArray[0] + stringToTestArray[1];
			ParseRulesResults prr = getParses(stringToTest);
			String uninterpretedWordSection = matchWhiteSpace.split(prr.getUninterpretableName())[0];
			spacesRemoved++;
			if (isEmptyStringOrSinglePunctuationCharacter(uninterpretedWordSection)){
				return new SpaceRemovalResult(true, prr, stringToTest, spacesRemoved);
			}
			indiceTojoin++;
		}
		String parsedOpsinNormalisedText =StringTools.stringListToString(getParses(stringToTest).getParseTokensList().get(0).getTokens(), "");
		String newParsedOpsinNormalisedText = parsedOpsinNormalisedText;
		do {//join with subsequent words until either the chemical name is fully interpretable or the join does not increase the amount of interpretable name
			if (indiceTojoin >=wordsLength){
				break;
			}
			parsedOpsinNormalisedText = newParsedOpsinNormalisedText;
			stringToTest+=normalisedWords[indiceTojoin];
			ParseRulesResults prr = getParses(stringToTest);
			newParsedOpsinNormalisedText = StringTools.stringListToString(prr.getParseTokensList().get(0).getTokens(), "");
			String uninterpretedWordSection = matchWhiteSpace.split(prr.getUninterpretableName())[0];
			spacesRemoved++;
			if (isEmptyStringOrSinglePunctuationCharacter(uninterpretedWordSection)){
				return new SpaceRemovalResult(true, prr, stringToTest, spacesRemoved);
			}
			indiceTojoin++;
		}
		while (newParsedOpsinNormalisedText.length()> parsedOpsinNormalisedText.length());
		return new SpaceRemovalResult(false, null, null, null);
	}

	private boolean fullWordImmediatelyFollowedByBracket(ParseRulesResults prr, String uninterpretedWordSection) {
		if (uninterpretedWordSection.length() <1){
			return false;
		}
		//exception made for full chemical followed by bracketed section
		Character firstLetter= uninterpretedWordSection.charAt(0);
		if (isFullWord(prr) && isOpenBracket(firstLetter)){
			return true;
		}
		return false;
	}
	
	private boolean fullOrFunctionalWordFollowedByBracket(ParseRulesResults prr, int i) {
		if ((isFullWord(prr) || isFunctionalWord(prr)) && i+1 < wordsLength){
			Character firstLetter = normalisedWords[i+1].charAt(0);
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
	private boolean isFullWord(ParseRulesResults prr){
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
	 * Determines whether a parse rules results describes a "substituent" word by checking whether the final annotation is an END_OF_SUBSTITUENT
	 * @param prr
	 * @return
	 */
	private boolean isSubstituentWord(ParseRulesResults prr) {
		if (prr.getParseTokensList().size()==0){
			return false;
		}
		List<Character> annotations = prr.getParseTokensList().get(0).getAnnotations();
		if (annotations.size()==0){
			return false;
		}
		Character finalAnnotation = annotations.get(annotations.size() -1);
		return finalAnnotation.equals(END_OF_SUBSTITUENT);
	}

	/**
	 * Determines whether a parse rules results describes a "functional" word by checking whether the final annotation is an END_OF_FUNCTIONALTERM
	 * @param prr
	 * @return
	 */
	private boolean isFunctionalWord(ParseRulesResults prr){
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

	private int numberOfOpenbrackets(String name) {
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

	private boolean isOpenBracket(char c){
		return c == '(' || c == '[' || c == '{';
	}

	private boolean isCloseBracket(char c){
		return c == ')' || c == ']' || c == '}';
	}
	
	/**
	 * Removes non chemical brackets, updating the starting and final indices appropriately before returning a new IdentifiedChemicalName
	 * @param opsinChemicalName
	 * @param startWordIndice
	 * @param endWordIndice
	 * @param lengthOfEndOfLastWordThatIsUnused 
	 * @return
	 */
	private IdentifiedChemicalName createIdentifiedName(String opsinChemicalName, int startWordIndice, int endWordIndice, int lengthOfEndOfLastWordThatIsUnused) {
		String rawChemicalName = extractRawText(startWordIndice, endWordIndice);
		rawChemicalName =rawChemicalName.substring(0, rawChemicalName.length()-lengthOfEndOfLastWordThatIsUnused);
		int startingIndice = wordStartIndices.get(startWordIndice);
		int frontBracketsRemoved = 0;
		int endBracketsRemoved = 0;
		int openBrackets = numberOfOpenbrackets(opsinChemicalName);
		if (openBrackets >= 1){
			while (openBrackets >= 1 && isOpenBracket(opsinChemicalName.charAt(0))){
				opsinChemicalName = opsinChemicalName.substring(1);
				frontBracketsRemoved++;
				openBrackets--;
			}
		}
		else if (openBrackets <= -1){
			while (openBrackets <= -1 && isCloseBracket(opsinChemicalName.charAt(opsinChemicalName.length()-1))){
				opsinChemicalName = opsinChemicalName.substring(0, opsinChemicalName.length()-1);
				endBracketsRemoved++;
				openBrackets++;
			}
		}
		else if (openBrackets==0){
			while (isOpenBracket(opsinChemicalName.charAt(0)) && isCloseBracket(opsinChemicalName.charAt(opsinChemicalName.length()-1))){
				opsinChemicalName = opsinChemicalName.substring(1, opsinChemicalName.length()-1);
				frontBracketsRemoved++;
				endBracketsRemoved++;
			}
		}
		if (frontBracketsRemoved > 0){
			int interWordSpacesToRemove = 0;//e.g. "( methanol" --> "methanol" involves removing one space
			int bracketsEncountered = 0;
			mainLoop: for (int i = startWordIndice; i < words.length; i++) {
				String rawWord = words[i];
				for (int j = 0; j < rawWord.length(); j++) {
					if (bracketsEncountered == frontBracketsRemoved && !Character.isWhitespace(rawWord.charAt(j))){
						interWordSpacesToRemove = i - startWordIndice;
						break mainLoop;
					}
					if (isOpenBracket(rawWord.charAt(j))){
						bracketsEncountered++;
					}
				}
			}
			rawChemicalName = rawChemicalName.substring(frontBracketsRemoved + interWordSpacesToRemove);
			startingIndice += (frontBracketsRemoved + interWordSpacesToRemove);
			startWordIndice += interWordSpacesToRemove;
		}
		if (endBracketsRemoved > 0){
			int interWordSpacesToRemove = 0;//e.g. "methanol )" --> "methanol" involves removing one space
			int bracketsEncountered = 0;
			mainLoop: for (int i = endWordIndice; i >= 0; i--) {
				String rawWord = words[i];
				for (int j = rawWord.length() -1; j >= 0; j--) {
					if (bracketsEncountered == endBracketsRemoved && !Character.isWhitespace(rawWord.charAt(j))){
						interWordSpacesToRemove = endWordIndice -i;
						break mainLoop;
					}
					if (isCloseBracket(rawWord.charAt(j))){
						bracketsEncountered++;
					}
				}
			}
			rawChemicalName = rawChemicalName.substring(0, rawChemicalName.length() - (endBracketsRemoved + interWordSpacesToRemove));
			endWordIndice -= interWordSpacesToRemove;
		}
		if (opsinChemicalName.endsWith("-") || opsinChemicalName.endsWith(",")){
			opsinChemicalName = opsinChemicalName.substring(0, opsinChemicalName.length()-1);
			rawChemicalName = rawChemicalName.substring(0, rawChemicalName.length()-1);
		}
		int endingIndice = startingIndice + rawChemicalName.length();
		return new IdentifiedChemicalName(startWordIndice, endWordIndice, startingIndice, endingIndice, opsinChemicalName, rawChemicalName, currentNameType);
	}

	private String extractRawText(int startWordIndice, int endWordIndice) {
		StringBuilder rawTextBuilder =new StringBuilder();
		for (int i = startWordIndice; i <= endWordIndice; i++) {
			rawTextBuilder.append(' ');
			rawTextBuilder.append(words[i]);
		}
		return rawTextBuilder.toString().substring(1);
	}
	
	public static void main(String[] args) throws Exception {
		String input ="Pyridine and benzene are chemicals";
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures(input).extractNames();
		for (IdentifiedChemicalName identifiedChemicalName : identifiedNames) {
			System.out.println(identifiedChemicalName.getChemicalName());
			System.out.println(identifiedChemicalName.getTextValue());
			System.out.println(identifiedChemicalName.getNameType());
		}
	}
}
