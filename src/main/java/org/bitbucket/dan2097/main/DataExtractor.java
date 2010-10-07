package org.bitbucket.dan2097.main;

import java.util.List;

import nu.xom.Document;

import org.bitbucket.dan2097.activityExtractor.ActivityExtractor;
import org.bitbucket.dan2097.activityExtractor.NameActivityPair;
import org.bitbucket.dan2097.structureExtractor.ArticleHtmlProcessor;
import org.bitbucket.dan2097.structureExtractor.NameIdentifierPair;
import org.bitbucket.dan2097.structureExtractor.NameIdentifierPairFinder;

public class DataExtractor {


	public static void main(String[] args) throws Exception {
		String fileName = "src/main/resources/org/bitbucket/dan2097/structureExtractor/jm100068m.htm";
		Document doc = ArticleHtmlProcessor.buildXomDoucmentFromArticle(fileName);
		String contents = ArticleHtmlProcessor.convertArticleDocumentToString(doc);
		List<NameIdentifierPair> nips = NameIdentifierPairFinder.extractNameIdentifierPairs(contents);
		for (NameIdentifierPair nameIdentifierPair : nips) {
			System.out.println(nameIdentifierPair.name + " :: " + nameIdentifierPair.identifier  + " :: " + nameIdentifierPair.identifierType);
		}
		List<NameActivityPair> naps = ActivityExtractor.extractNameActivityTypeActivityValueTuples(doc);
		for (NameActivityPair nameActivityPair : naps) {
			System.out.println(nameActivityPair.name + " :: " + nameActivityPair.activity+ " :: " + nameActivityPair.activityType);
			for (NameIdentifierPair nameIdentifierPair : nips) {
				if (nameIdentifierPair.identifier.equalsIgnoreCase(nameActivityPair.name)){
					System.out.println(nameIdentifierPair.name +" is recognized");
				}
			}
		}
	}
}
