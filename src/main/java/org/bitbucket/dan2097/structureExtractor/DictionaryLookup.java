package org.bitbucket.dan2097.structureExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DictionaryLookup {

	private static Map<String, HashHolder> wordToHashHolderMap = new HashMap<String, HashHolder>();
	
	private static class HashHolder {
		private Map<String, HashHolder> wordToHashHolderMap;

		private boolean isTerminal;
		public HashHolder(boolean isTerminal) {
			this.isTerminal = isTerminal;
		}
		
		boolean isTerminal() {
			return isTerminal;
		}
		void setIsTerminal(boolean isTerminal) {
			this.isTerminal = isTerminal;
		}
		
		Map<String, HashHolder> getWordToHashHolderMap() {
			return wordToHashHolderMap;
		}

		void setWordToHashHolderMap(Map<String, HashHolder> wordToHashHolderMap) {
			this.wordToHashHolderMap = wordToHashHolderMap;
		}
	}
	
	private static final Pattern matchWhiteSpace = Pattern.compile("\\s+");
	
	static{
		Pattern matchTab = Pattern.compile("\t");
		InputStream is = DictionaryLookup.class.getResourceAsStream("dictionary.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				String[] lineArray = matchTab.split(line);
				addToHashMaps(lineArray[0]);//TODO maybe associate with a structure
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to initalise dictionary",e);
		}
	}

	private static void addToHashMaps(String name) {
		String[] words = matchWhiteSpace.split(name);
		if (words.length>0){
			HashHolder currentHashHolder;
			boolean isTerminal = words.length==1;
			if (wordToHashHolderMap.containsKey(words[0])){
				currentHashHolder = wordToHashHolderMap.get(words[0]);
				if (isTerminal){
					currentHashHolder.setIsTerminal(true);
				}
			}
			else{
				currentHashHolder = new HashHolder(isTerminal);
				wordToHashHolderMap.put(words[0], currentHashHolder);
			}
			for (int i = 1; i < words.length; i++) {
				Map<String, HashHolder> wordToHashHolderMap = currentHashHolder.getWordToHashHolderMap();
				isTerminal = (words.length-1==i);
				if (wordToHashHolderMap !=null){
					if (wordToHashHolderMap.containsKey(words[i])){
						currentHashHolder = wordToHashHolderMap.get(words[i]);
						if (isTerminal){
							currentHashHolder.setIsTerminal(true);
						}
					}
					else{
						currentHashHolder = new HashHolder(isTerminal);
						wordToHashHolderMap.put(words[i], currentHashHolder);
					}
				}
				else{
					wordToHashHolderMap = new HashMap<String, HashHolder>();
					currentHashHolder.setWordToHashHolderMap(wordToHashHolderMap);
					currentHashHolder = new HashHolder(isTerminal);
					wordToHashHolderMap.put(words[i], currentHashHolder);
					if (name.equals("monosodium cloxacillin")){
						System.out.println(words[i]);
					}
				}
			}
		}
	}

	public static List<IdentifiedChemicalName> performDictionaryLookup(String[] normalisedWords, String[] words) {
		List<IdentifiedChemicalName> identifiedNames = new ArrayList<IdentifiedChemicalName>();
		int wordsLength = words.length;
		for (int i = 0; i < wordsLength; i++) {
			List<IdentifiedChemicalName> names =findMatches(normalisedWords, words, i);
			if (names!=null){
				identifiedNames.addAll(names);
			}
		}
		return identifiedNames;
	}

	private static List<IdentifiedChemicalName> findMatches(String[] normalisedWords, String[] words, int firstWordToConsider) {
		List<IdentifiedChemicalName> identifiedNames = null;
		HashHolder currentHashHolder = wordToHashHolderMap.get(normalisedWords[firstWordToConsider]);
		if (currentHashHolder!=null){
			StringBuilder normalisedName = new StringBuilder(normalisedWords[firstWordToConsider]);
			StringBuilder rawTextName = new StringBuilder(words[firstWordToConsider]);
			identifiedNames = new ArrayList<IdentifiedChemicalName>();
			if (currentHashHolder.isTerminal()){
				identifiedNames.add(new IdentifiedChemicalName(firstWordToConsider, firstWordToConsider, normalisedName.toString(), rawTextName.toString()));
			}
			for (int i = firstWordToConsider +1; i < normalisedWords.length; i++) {
				Map<String, HashHolder> wordToHashHolderMap = currentHashHolder.getWordToHashHolderMap();
				if (wordToHashHolderMap !=null){
					currentHashHolder = wordToHashHolderMap.get(normalisedWords[i]);
					if (currentHashHolder!=null){
						normalisedName.append(' ');
						normalisedName.append(normalisedWords[i]);
						rawTextName.append(' ');
						rawTextName.append(words[i]);
						if (currentHashHolder.isTerminal()){
							identifiedNames.add(new IdentifiedChemicalName(firstWordToConsider, i, normalisedName.toString(), rawTextName.toString()));
						}
					}
					else{
						break;
					}
				}
				else{
					break;
				}
			}
		}
		return identifiedNames;
	}
}
