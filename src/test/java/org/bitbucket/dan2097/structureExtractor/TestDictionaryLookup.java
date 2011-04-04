package org.bitbucket.dan2097.structureExtractor;

import static junit.framework.Assert.*;

import java.util.List;

import org.junit.Test;
public class TestDictionaryLookup {

	@Test
	public void testTrivalCase1() throws Exception{
		String[] words =new String[]{"HNO3"};
		List<IdentifiedChemicalName> identifiedNames = DictionaryLookup.performDictionaryLookup( words);
		assertEquals(1, identifiedNames.size());
		assertEquals("hno3", identifiedNames.get(0).getChemicalName());
		assertEquals("HNO3", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testTrivalCase2() throws Exception{
		String[] words =new String[]{"cl(-)"};
		List<IdentifiedChemicalName> identifiedNames = DictionaryLookup.performDictionaryLookup(words);
		assertEquals(1, identifiedNames.size());
		assertEquals("cl(-)", identifiedNames.get(0).getChemicalName());
		assertEquals("cl(-)", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testMultiWordCase1() throws Exception{
		String[] words =new String[]{"monosodium", "cloxacillin"};
		List<IdentifiedChemicalName> identifiedNames = DictionaryLookup.performDictionaryLookup(words);
		assertEquals(2, identifiedNames.size());
		assertEquals("monosodium cloxacillin", identifiedNames.get(0).getChemicalName());
		assertEquals("monosodium cloxacillin", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals("cloxacillin", identifiedNames.get(1).getChemicalName());
		assertEquals("cloxacillin", identifiedNames.get(1).getTextValue());
		assertEquals(1, identifiedNames.get(1).getWordPositionStartIndice());
	}
	
	@Test
	public void testMultiWordCase2() throws Exception{
		String[] words =new String[]{"Betamethasone", "sodium", "phosphate"};
		List<IdentifiedChemicalName> identifiedNames = DictionaryLookup.performDictionaryLookup(words);
		assertEquals(2, identifiedNames.size());
		
		assertEquals("betamethasone", identifiedNames.get(0).getChemicalName());
		assertEquals("Betamethasone", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		
		assertEquals("betamethasone sodium phosphate", identifiedNames.get(1).getChemicalName());
		assertEquals("Betamethasone sodium phosphate", identifiedNames.get(1).getTextValue());
		assertEquals(0, identifiedNames.get(1).getWordPositionStartIndice());
		assertEquals(2, identifiedNames.get(1).getWordPositionEndIndice());
	}
}
