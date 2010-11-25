package org.bitbucket.dan2097.structureExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import uk.ac.cam.ch.wwmm.opsin.NameToStructure;
import uk.ac.cam.ch.wwmm.opsin.ParseRules;
import uk.ac.cam.ch.wwmm.opsin.ParseRulesResults;
import uk.ac.cam.ch.wwmm.opsin.StringTools;

public class DocumentToStructures {
	private static final Pattern matchWhiteSpace = Pattern.compile("\\s+");
	
	public static void main(String[] args) throws Exception {
		String input ="ethylene glycol, propylene glycol, 1,3- propanediol, 1,2-butanediol, 1,3-butanediol, 1,4-butanediol, 2,3-butanediol, 1,2-pentanediol, 1,5-pentanediol, 1,2-hexanediol, 1,6-hexanediol, 1,2-heptanediol, 1,7-heptanediol, 1,2-octanediol, 1,8-octanediol, 1,2-decanediol, 1,10-decanediol, 3-methyl-1,2-butanediol, 3,3-dimethyl-1,2-butanediol, 4-methyl-1,2-pentanediol, 5-methyl-1,2-hexanediol, 3-chloro- 1,2-propanediol, 3-butene-1,2-diol, 4-pentene-1,2-diol, 1-phenylethane-1,2-diol, 1-(4-methylphenyl)ethane-1,2-diol, 1-(4-methoxyphenyl)ethane-1,2-diol, 1-(4-chlorophenyl)ethane-1,2-diol, 1-(4-nitrophenyl)ethane-1,2-diol, 1-cyclohexylethane- 1,2-diol, 1,2-cyclohexanediol,";
		List<IdentifiedChemicalName> identifiedNames = extractNames(matchWhiteSpace.split(input));
		for (IdentifiedChemicalName identifiedChemicalName : identifiedNames) {
			System.out.println(identifiedChemicalName.getValue());
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

	public static List<IdentifiedChemicalName> extractNames(String[] words) throws Exception{
		List<IdentifiedChemicalName> identifiedChemicalNames = new ArrayList<IdentifiedChemicalName>();
		ParseRules pr = NameToStructure.getOpsinParser();
		String nameBuffer= "";
		int wordsLength =words.length;
		for (int i = 0; i < wordsLength; i++) {
			String stringToTest= (i+1)<wordsLength ? words[i] + " "+ words[i+1] : words[i];
			ParseRulesResults prr = pr.getParses(stringToTest);
			if (prr.getParseTokensList().size()==0){
				if (!nameBuffer.toString().equals("")){
					String name =nameBuffer.toString();
					name= stripUnbalancedTrailingOrLeadingBracket(name);
					identifiedChemicalNames.add(new IdentifiedChemicalName(i-matchWhiteSpace.split(name).length, name));
					nameBuffer = "";
				}
			}
			else{
				String uninterpretableName = prr.getUninterpretableName();
				Character followingChar = !uninterpretableName.equals("") ? uninterpretableName.charAt(0) : null;
				if (followingChar==null || !Character.isLetter(followingChar)){
					if (!nameBuffer.equals("")){
						nameBuffer+=" ";
					}
					String parsedOpsinNormalisedText =StringTools.stringListToString(prr.getParseTokensList().get(0).getTokens(), "");
					nameBuffer += parsedOpsinNormalisedText;
					if (parsedOpsinNormalisedText.indexOf(' ')!=-1){//both words were partially or fully interpreted
						i++;
					}
					if (followingChar!=null && followingChar !=' '){//encountered punctuation
						String name =nameBuffer.toString();
						name= stripUnbalancedTrailingOrLeadingBracket(name);
						identifiedChemicalNames.add(new IdentifiedChemicalName(i + 1 -matchWhiteSpace.split(name).length, name));
						nameBuffer = "";
					}
				}
				else{
					nameBuffer = "";
				}
			}
		}
		if (!nameBuffer.toString().equals("")){
			String name =nameBuffer.toString();
			name= stripUnbalancedTrailingOrLeadingBracket(name);
			identifiedChemicalNames.add(new IdentifiedChemicalName(wordsLength - matchWhiteSpace.split(name).length, name));
		}
		return identifiedChemicalNames;
	}
	
	private static String stripUnbalancedTrailingOrLeadingBracket(String name) {
		int openBrackets = numberOfOpenbrackets(name);
		if (openBrackets==0){
			return name;
		}
		else if (openBrackets==1){
			Character firstLetter= name.charAt(01);
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
}
