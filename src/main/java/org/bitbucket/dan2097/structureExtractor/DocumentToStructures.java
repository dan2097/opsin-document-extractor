package org.bitbucket.dan2097.structureExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import uk.ac.cam.ch.wwmm.opsin.NameToStructure;
import uk.ac.cam.ch.wwmm.opsin.NameToStructureException;
import uk.ac.cam.ch.wwmm.opsin.ParseRules;
import uk.ac.cam.ch.wwmm.opsin.ParseRulesResults;
import uk.ac.cam.ch.wwmm.opsin.ParsingException;
import uk.ac.cam.ch.wwmm.opsin.StringTools;

public class DocumentToStructures {
	
	private static final Pattern matchWhiteSpace = Pattern.compile("\\s+");
	private final static char endOfMainGroup = '\u00e2';
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
	 * Runs the preProcessor over the string, splits on white space then extracts names
	 * @param sentence
	 * @return
	 * @throws Exception
	 */
	public static List<IdentifiedChemicalName> extractNames(String sentence) throws Exception{
		String[] words = matchWhiteSpace.split(PreProcesssor.preProcess(sentence));
		return extractNames(words);
	}

	/**
	 * Given an array of words not containing white space extracts all the chemical names that are interpretable by OPSIN
	 * @param words
	 * @return
	 * @throws Exception
	 */
	public static List<IdentifiedChemicalName> extractNames(String[] words) throws Exception{
		List<IdentifiedChemicalName> identifiedChemicalNames = new ArrayList<IdentifiedChemicalName>();
		StringBuilder chemicalNameBuffer = new StringBuilder();//holds the current chemical name under consideration
		//StringBuilder textBuffer = new StringBuilder();//holds the raw text from which the chemical name was formed (i.e. pre normalisation)
		int wordsLength =words.length;
		int totalSpacesRemoved =0;//running total of spaces removed for the current name
		for (int i = 0; i < wordsLength; i++) {
			String stringToTest= (i+1) < wordsLength ? words[i] + " "+ words[i+1] : words[i];//attempt to parse the word plus the next word
			ParseRulesResults prr = pr.getParses(stringToTest);
			int spacesRemoved = 0;
			if (prr.getParseTokensList().size()==0 && (i+1 ) < wordsLength){//completely uninterpretable
				stringToTest=words[i] + words[i+1];//attempt space removal
				prr = pr.getParses(stringToTest);
				if (prr.getParseTokensList().size()!=0){//space removal made some of the input interpretable!
					spacesRemoved++;
				}
			}
			if (prr.getParseTokensList().size()==0){//input could not be used to form a chemical name
				if (!chemicalNameBuffer.toString().equals("")){//a chemical name is already in the buffer, add it to the identifiedChemicalNames
					String name =chemicalNameBuffer.toString();
					name= stripUnbalancedTrailingOrLeadingBracket(name);
					identifiedChemicalNames.add(new IdentifiedChemicalName(i-(matchWhiteSpace.split(name).length + spacesRemoved + totalSpacesRemoved), name));
					chemicalNameBuffer = new StringBuilder();
				}
				totalSpacesRemoved =0;
			}
			else{
				String uninterpretableName = prr.getUninterpretableName();
				String uninterpretedWordSection = matchWhiteSpace.split(uninterpretableName)[0];
				if (uninterpretedWordSection.length() >=2 || (uninterpretedWordSection.length() ==1 && (Character.isLetter(uninterpretedWordSection.charAt(0)) || Character.isDigit(uninterpretedWordSection.charAt(0))))){
					//uninterpretable as is
					SpaceRemovalResult srr =attemptSpaceRemoval(words, i, uninterpretableName, stringToTest, wordsLength);
					if (srr.isSuccess()){
						prr = srr.getParseRulesResults();
						uninterpretableName = prr.getUninterpretableName();
						uninterpretedWordSection = matchWhiteSpace.split(uninterpretableName)[0];
						spacesRemoved +=srr.getSpacesRemoved();
					}
					else if(!fullWordFollowedByBracket(prr, uninterpretedWordSection)){
						chemicalNameBuffer = new StringBuilder();
						continue;
					}
				}
				else if (isFullWord(prr)){
					//e.g. benzene sulfonamide
					SpaceRemovalResult srr =attemptSpaceRemoval(words, i, uninterpretableName, stringToTest, wordsLength);
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
				if (uninterpretedWordSection.length()==1){//encountered punctuation
					String name =chemicalNameBuffer.toString();
					name= stripUnbalancedTrailingOrLeadingBracket(name);
					identifiedChemicalNames.add(new IdentifiedChemicalName(i + 1 -(matchWhiteSpace.split(name).length + spacesRemoved + totalSpacesRemoved), name));
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
			name= stripUnbalancedTrailingOrLeadingBracket(name);
			identifiedChemicalNames.add(new IdentifiedChemicalName(wordsLength - (matchWhiteSpace.split(name).length + totalSpacesRemoved), name));
		}
		return identifiedChemicalNames;
	}
	
	private static SpaceRemovalResult attemptSpaceRemoval(String[] words, int i, String uninterpretableName, String stringToTest, int wordsLength) throws ParsingException{
		int spacesRemoved = 0;
		if (matchWhiteSpace.split(uninterpretableName).length==2 && (i+1)<wordsLength){//first space erroneous
			stringToTest = words[i] + words[i+1];
			ParseRulesResults prr = pr.getParses(stringToTest);
			uninterpretableName = prr.getUninterpretableName();
			String uninterpretedWordSection = matchWhiteSpace.split(uninterpretableName)[0];
			spacesRemoved++;
			if (uninterpretedWordSection.length() ==0 || (uninterpretedWordSection.length() ==1 && !Character.isLetter(uninterpretedWordSection.charAt(0)) && !Character.isDigit(uninterpretedWordSection.charAt(0)))){
				return new SpaceRemovalResult(true, spacesRemoved, prr);
			}
		}
		String parsedOpsinNormalisedText =StringTools.stringListToString(pr.getParses(stringToTest).getParseTokensList().get(0).getTokens(), "");
		String newParsedOpsinNormalisedText = parsedOpsinNormalisedText;
		int indiceTojoin =i+2;
		do {
			if (indiceTojoin >=wordsLength){
				break;
			}
			parsedOpsinNormalisedText = newParsedOpsinNormalisedText;
			stringToTest+=words[indiceTojoin];
			ParseRulesResults prr = pr.getParses(stringToTest);
			newParsedOpsinNormalisedText = StringTools.stringListToString(prr.getParseTokensList().get(0).getTokens(), "");
			uninterpretableName = prr.getUninterpretableName();
			String uninterpretedWordSection = matchWhiteSpace.split(uninterpretableName)[0];
			spacesRemoved++;
			if (uninterpretedWordSection.length() ==0 || (uninterpretedWordSection.length() ==1 && !Character.isLetter(uninterpretedWordSection.charAt(0)) && !Character.isDigit(uninterpretedWordSection.charAt(0)))){
				return new SpaceRemovalResult(true, spacesRemoved, prr);
			}
			indiceTojoin++;
		}
		while (newParsedOpsinNormalisedText.length()> parsedOpsinNormalisedText.length());
		return new SpaceRemovalResult(false, spacesRemoved, null);
	}
	
	private static boolean isFullWord(ParseRulesResults prr){
		List<Character> annotations = prr.getParseTokensList().get(0).getAnnotations();
		Character finalAnnotation = annotations.get(annotations.size() -1);
		return finalAnnotation.equals(endOfMainGroup);
	}
	
	private static boolean fullWordFollowedByBracket(ParseRulesResults prr, String uninterpretedWordSection) {
		//exception made for full chemical followed by bracketed section
		Character firstLetter= uninterpretedWordSection.charAt(0);
		if (isFullWord(prr) && (firstLetter =='(' || firstLetter =='[' || firstLetter =='{')){
			return true;
		}
		return false;
	}

	private static String stripUnbalancedTrailingOrLeadingBracket(String name) {
		int openBrackets = numberOfOpenbrackets(name);
		if (openBrackets==0){
			return name;
		}
		else if (openBrackets==1){
			Character firstLetter= name.charAt(0);
			if (firstLetter =='(' || firstLetter =='[' || firstLetter =='{'){
				name = name.substring(1);
			}
		}
		else if (openBrackets==-1){
			Character lastLetter= name.charAt(name.length()-1);
			if (lastLetter ==')' || lastLetter ==']' || lastLetter =='}'){
				name = name.substring(0, name.length()-1);
			}
		}
		return name;
	}

	private static int numberOfOpenbrackets(String name) {
		int bracketLevel = 0;
		int stringLength  = name.length();
		for(int i = 0 ; i < stringLength; i++) {
			char c = name.charAt(i);
			if(c == '(' || c == '[' || c == '{') {
				bracketLevel++;
			}
			else if(c == ')' || c == ']' || c == '}') {
				bracketLevel--;
			}
		}
		return bracketLevel;
	}
	
	public static void main(String[] args) throws Exception {
		String input ="ethylene glycol, propylene glycol, 1,3- propanediol, 1,2-butanediol, 1,3-butanediol, 1,4-butanediol, 2,3-butanediol, 1,2-pentanediol, 1,5-pentanediol, 1,2-hexanediol, 1,6-hexanediol, 1,2-heptanediol, 1,7-heptanediol, 1,2-octanediol, 1,8-octanediol, 1,2-decanediol, 1,10-decanediol, 3-methyl-1,2-butanediol, 3,3-dimethyl-1,2-butanediol, 4-methyl-1,2-pentanediol, 5-methyl-1,2-hexanediol, 3-chloro- 1,2-propanediol, 3-butene-1,2-diol, 4-pentene-1,2-diol, 1-phenylethane-1,2-diol, 1-(4-methylphenyl)ethane-1,2-diol, 1-(4-methoxyphenyl)ethane-1,2-diol, 1-(4-chlorophenyl)ethane-1,2-diol, 1-(4-nitrophenyl)ethane-1,2-diol, 1-cyclohexylethane- 1,2-diol, 1,2-cyclohexanediol,";
		List<IdentifiedChemicalName> identifiedNames = extractNames(matchWhiteSpace.split(input));
		for (IdentifiedChemicalName identifiedChemicalName : identifiedNames) {
			System.out.println(identifiedChemicalName.getValue());
		}
	}
}
