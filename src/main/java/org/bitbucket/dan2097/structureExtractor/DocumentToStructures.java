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
		String input ="The piperidine derivative compound pyridines according to claim 4, which is (1) 2-(4-(3-cyclopentyloxy-4-methoxyphenyl)-4-cyanopiperidin-1-yl)acetic acid, (2) 2-(4-(3,4-dimethoxyphenyl)-4-cyanopiperidin-1-yl)acetic acid, (3) 2-(4-(3-ethoxy-4-methoxyphenyl-4-cyanopiperidin-1-yl)acetic acid, (4) 2-(4-(3-cyclopropylmethoxy-4-methoxyphenyl)-4-cyanopiperidin-1-yl)acetic acid, (5) 2-(4-(3-isopropyloxy-4-methoxyphenyl)-4-cyanopiperidin-1-yl)acetic acid, (6) 2-(4-(3-cyclobutyloxy-4-methoxyphenyl)-4-cyanopiperidin-1-yl)acetic acid, (7) 2-(4-(3-cyclopentyloxy-4-methoxyphenyl)-4-cyanopiperidin-1-yl)propanoic acid; (8) 4-(4-(3-cyclopentyloxy-4-methoxyphenyl)4-cyanopiperidin-1-yl)butanoic acid, (9) 2-(4-(3-cyclopentyloxy-4-methoxyphenyl)-4-cyanopiperidin-1-yl)butanoic acid, (10) 2-(4-(3-cyclopentyloxy-4-difluoromethoxyphenyl)-4-cyanopiperidin-1-yl)acetic acid";
		extractNames(matchWhiteSpace.split(input));
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
					identifiedChemicalNames.add(new IdentifiedChemicalName(i-1-matchWhiteSpace.split(name).length, name));
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
					nameBuffer += StringTools.stringListToString(prr.getParseTokensList().get(0).getTokens(), "");
					if (uninterpretableName.indexOf(' ')==-1){
						i++;
					}
					if (followingChar!=null && followingChar !=' '){//encountered punctuation
						String name =nameBuffer.toString();
						name= stripUnbalancedTrailingOrLeadingBracket(name);
						identifiedChemicalNames.add(new IdentifiedChemicalName(i-matchWhiteSpace.split(name).length, name));
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
			identifiedChemicalNames.add(new IdentifiedChemicalName(wordsLength-1-matchWhiteSpace.split(name).length, name));
		}
//		for (IdentifiedChemicalName identifiedChemicalName : identifiedChemicalNames) {
//			System.out.println(identifiedChemicalName.getValue());
//		}
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
