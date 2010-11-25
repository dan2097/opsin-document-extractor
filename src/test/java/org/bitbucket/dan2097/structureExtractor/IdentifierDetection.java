package org.bitbucket.dan2097.structureExtractor;

import java.util.List;

import org.bitbucket.dan2097.structureExtractor.NameIdentifierPair.IdentifierType;
import org.junit.Test;
import static junit.framework.Assert.*;

public class IdentifierDetection {
	
	@Test
	public void detectIdentifier1(){
		List<NameIdentifierPair> nip = NameIdentifierPairFinder.findIdentifiers("foo", "(8).(22)");
		assertEquals(1, nip.size());
		assertEquals("8", nip.get(0).identifier);
		assertEquals(IdentifierType.identifier, nip.get(0).identifierType);
	}
	
	
	@Test
	public void detectIdentifier2(){
		List<NameIdentifierPair> nip = NameIdentifierPairFinder.findIdentifiers("foo", "(5m)");
		assertEquals(1, nip.size());
		assertEquals("5m", nip.get(0).identifier);
		assertEquals(IdentifierType.identifier, nip.get(0).identifierType);
	}
	
	@Test
	public void detectIdentifier3(){
		List<NameIdentifierPair> nip = NameIdentifierPairFinder.findIdentifiers("foo", "(10)");
		assertEquals(1, nip.size());
		assertEquals("10",  nip.get(0).identifier);
		assertEquals(IdentifierType.identifier, nip.get(0).identifierType);
	}
	

	@Test
	public void detectNameAndIdentifier1() throws Exception{
		String text = "(Z)-2-Azido-3-imidazo[1,2-a]pyridin-3-yl-acrylic Acid Ethyl Ester (8).(22)";
		List<NameIdentifierPair> results = NameIdentifierPairFinder.extractNameIdentifierPairs(text);
		assertEquals(1, results.size());
		NameIdentifierPair pair = results.get(0);
		assertEquals("(Z)-2-Azido-3-imidazo[1,2-a]pyridin-3-yl-acrylic Acid Ethyl Ester", pair.name);
		assertEquals("8", pair.identifier);
		assertEquals(IdentifierType.identifier, pair.identifierType);
	}
	
	@Test
	public void detectNameAndIdentifier2() throws Exception{
		String text = "5-(2-Methoxyphenyl)-7-phenyl-2H-thiazolo[3,2-a]pyrimidin-3(5H)-one (5m)";
		List<NameIdentifierPair> results =  NameIdentifierPairFinder.extractNameIdentifierPairs(text);
		assertEquals(1, results.size());
		NameIdentifierPair pair = results.get(0);
		assertEquals("5-(2-Methoxyphenyl)-7-phenyl-2H-thiazolo[3,2-a]pyrimidin-3(5H)-one", pair.name);
		assertEquals("5m", pair.identifier);
		assertEquals(IdentifierType.identifier, pair.identifierType);
	}
	
	@Test
	public void detectNameAndIdentifier3() throws Exception{
		String text = "4-(2-Methoxyphenyl)-3,4,5,6-tetrahydrobenzo[h]quinazoline-2(1H)-thione (4a) and 7-(2-Methoxyphenyl)-7,10-dihydro-5H-benzo[h]thiazolo[2,3-b]quinazolin-9(6H)-one (5a).)";
		List<NameIdentifierPair> results =  NameIdentifierPairFinder.extractNameIdentifierPairs(text);
		assertEquals(2, results.size());
		NameIdentifierPair pair1 = results.get(0);
		assertEquals("4-(2-Methoxyphenyl)-3,4,5,6-tetrahydrobenzo[h]quinazoline-2(1H)-thione", pair1.name);
		assertEquals("4a", pair1.identifier);
		assertEquals(IdentifierType.identifier, pair1.identifierType);
		
		NameIdentifierPair pair2 = results.get(1);
		assertEquals("7-(2-Methoxyphenyl)-7,10-dihydro-5H-benzo[h]thiazolo[2,3-b]quinazolin-9(6H)-one", pair2.name);
		assertEquals("5a", pair2.identifier);
		assertEquals(IdentifierType.identifier, pair2.identifierType);
	}
	
	@Test
	public void detectNameAndIdentifier4() throws Exception{
		String text = "Indolo[2,1-b]quinazolin-6,12-dione (tryptanthrin, Figure 1)";
		List<NameIdentifierPair> results =  NameIdentifierPairFinder.extractNameIdentifierPairs(text);
		assertEquals(2, results.size());
		NameIdentifierPair pair1 = results.get(0);
		assertEquals("Indolo[2,1-b]quinazolin-6,12-dione", pair1.name);
		assertEquals("tryptanthrin", pair1.identifier);
		assertEquals(IdentifierType.alias, pair1.identifierType);
		
		NameIdentifierPair pair2 = results.get(1);
		assertEquals("Indolo[2,1-b]quinazolin-6,12-dione", pair2.name);
		assertEquals("Figure 1", pair2.identifier);
		assertEquals(IdentifierType.identifier, pair2.identifierType);
	}
	
	@Test
	public void detectNameAndIdentifier5() throws Exception{
		String text = "(6Z,8E,10E,14Z)-(5S,12R)-5,12-dihydroxyeicosa-6,8,10,14-tetraenoic acid, 1,";
		List<NameIdentifierPair> results =  NameIdentifierPairFinder.extractNameIdentifierPairs(text);
		assertEquals(1, results.size());
		NameIdentifierPair pair1 = results.get(0);
		assertEquals("(6Z,8E,10E,14Z)-(5S,12R)-5,12-dihydroxyeicosa-6,8,10,14-tetraenoic acid", pair1.name);
		assertEquals("1", pair1.identifier);
		assertEquals(IdentifierType.identifier, pair1.identifierType);
	}
	
	@Test
	public void detectNameAndIdentifier6() throws Exception{
		String text = "	Reference Example 6\n1-(3-cyclopentyloxy-4-methoxyphenyl)cyclopent-3-encarbonitrile";
		List<NameIdentifierPair> results =  NameIdentifierPairFinder.extractNameIdentifierPairs(text);
		assertEquals(1, results.size());
		NameIdentifierPair pair1 = results.get(0);
		assertEquals("1-(3-cyclopentyloxy-4-methoxyphenyl)cyclopent-3-encarbonitrile", pair1.name);
		assertEquals("6", pair1.identifier);
		assertEquals(IdentifierType.identifier, pair1.identifierType);
	}
	
	

	@Test
	public void detectNameAndIdentifierFalsePositive1() throws Exception{
		String text = "2-(pyridine-2-yl)benzo[b]thiophene (Yield: 35.4 %)";
		List<NameIdentifierPair> results =  NameIdentifierPairFinder.extractNameIdentifierPairs(text);
		assertEquals(0, results.size());
	}
	
	
}
