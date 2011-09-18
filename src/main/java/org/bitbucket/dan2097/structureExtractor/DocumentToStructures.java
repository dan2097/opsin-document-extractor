package org.bitbucket.dan2097.structureExtractor;

import java.util.ArrayList;
import java.util.Arrays;
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
	private static final char END_OF_SUBSTITUENT = '\u00e9';
	private static ParseRules pr;
	/**These are words that are either interpreted erroneously as chemicals or have a nasty tendency to be interpreted as chemical when space removal is invoked*/
	private static final List<String> stopWords = Arrays.asList("period", "periodic", "on", "one", "it", "at", "an", "in");
	
	private final String[] words;
	private final int wordsLength;
	private final String[] normalisedWords;

	static{
		try {
			pr = NameToStructure.getOpsinParser();
		} catch (NameToStructureException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Convenience constructor: splits on whitespace to generate words array
	 * @param sentence
	 */
	public DocumentToStructures(String sentence) {
		this(matchWhiteSpace.split(sentence));
	}

	/**
	 * A list of words to find chemicals within
	 * THESE WORDS MUST NOT CONTAIN WHITESPACE
	 * @param words
	 */
	public DocumentToStructures(String[] words) {
		this.words = words;
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
			if (chemicalNameBuffer.length()==0 && isMadeOfDigits(normalisedWords[i])){//stray digits cannot be assumed to be part of a chemical name
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
					int startingIndice = i-(matchWhiteSpace.split(name).length + spacesRemoved + totalSpacesRemoved);
					int finalIndice = i -1;
					identifiedChemicalNames.add(createIdentifiedName(name, startingIndice, finalIndice));
					chemicalNameBuffer = new StringBuilder();
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
					SpaceRemovalResult srr =attemptSpaceRemoval(indiceOfNextUnusedWord, stringToTest);
					if (srr.isSuccess()){
						prr = srr.getParseRulesResults();
						uninterpretedWordSection = matchWhiteSpace.split(prr.getUninterpretableName())[0];
						spacesRemoved +=srr.getSpacesRemoved();
						indiceOfNextUnusedWord += srr.getSpacesRemoved();
						stringToTest = srr.getInputString();
						nextWord = indiceOfNextUnusedWord < wordsLength ? normalisedWords[indiceOfNextUnusedWord] : null;
					}
					else if (!fullWordImmediatelyFollowedByBracket(prr, uninterpretedWordSection)){
						//exception made for a full name followed by a bracket e.g. pyridine(5ml
						//otherwise the name was still uninterpretable even after space removal and hence is rejected
						chemicalNameBuffer = new StringBuilder();
						continue;
					}
				}
				boolean nextWordAppearsUninterpretable = false;
				if (nextWord !=null){
					nextWordAppearsUninterpretable = wordAppearsUninterpretable(nextWord);
				}
				if (nextWordAppearsUninterpretable && isASpecialCaseWhereSpaceRemovalShouldBeAttempted(prr, nextWord)){
					//e.g. benzene sulfonamide
					SpaceRemovalResult srr =attemptSpaceRemoval(indiceOfNextUnusedWord, stringToTest);
					if (srr.isSuccess()){
						prr = srr.getParseRulesResults();
						uninterpretedWordSection = matchWhiteSpace.split(prr.getUninterpretableName())[0];
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
				i += spacesRemoved;
				if ((uninterpretedWordSection.length()==1 && !Character.isLetterOrDigit(uninterpretedWordSection.charAt(0)) )|| fullOrFunctionalWordFollowedByBracket(prr, i)){//encountered punctuation or next word is likely to be irrelevant/a synonymn
					String name =chemicalNameBuffer.toString();
					int startingIndice = i + 1 -(matchWhiteSpace.split(name).length + spacesRemoved + totalSpacesRemoved);
					int finalIndice = i;
					identifiedChemicalNames.add(createIdentifiedName(name, startingIndice, finalIndice));
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
			identifiedChemicalNames.add(createIdentifiedName(name, startingIndice, finalIndice));
		}
		return identifiedChemicalNames;
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

	private static boolean isMadeOfDigits(String word) {
		for (char charac : word.toCharArray()) {
			if(!Character.isDigit(charac)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Runs the preprocessor over the list of words and markups up stop words with exclamation marks to prevent their recognition
	 * @return
	 */
	private String[] generateNormalisedWords() {
		String[] normalisedWords = new String[wordsLength];
		for (int i = 0; i < wordsLength; i++) {
			String word = PreProcesssor.preProcess(words[i]);
			if (stopWords.contains(word.toLowerCase()) && !periodicSpecialCase(word, i)){
				word = '!' + word + '!';
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

	private SpaceRemovalResult attemptSpaceRemoval(int indiceOfNextUnusedWord, String stringToTest) {
		int spacesRemoved = 0;
		int indiceTojoin = indiceOfNextUnusedWord;
		String[] stringToTestArray = matchWhiteSpace.split(stringToTest);
		if (stringToTestArray.length==2){//is the space between the words erroneous
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
	 * @param name
	 * @param startingIndice
	 * @param finalIndice
	 * @return
	 */
	private IdentifiedChemicalName createIdentifiedName(String name, int startingIndice, int finalIndice) {
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
		return new IdentifiedChemicalName(startingIndice, finalIndice, name, extractRawText(startingIndice, finalIndice));
	}

	private String extractRawText(int startingIndice, int finalIndice) {
		StringBuilder rawTextBuilder =new StringBuilder();
		for (int i = startingIndice; i <= finalIndice; i++) {
			rawTextBuilder.append(' ');
			rawTextBuilder.append(words[i]);
		}
		return rawTextBuilder.toString().substring(1);
	}
	
	public static void main(String[] args) throws Exception {
		String input ="5-amino-2- [2- (4-methoxyphenyl) vinyl] -4- (4-methylpiperazin-1-yl) -6-morpholino pyrimidine";
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures(input).extractNames();;
		for (IdentifiedChemicalName identifiedChemicalName : identifiedNames) {
			System.out.println(identifiedChemicalName.getChemicalName());
		}
	}
}
