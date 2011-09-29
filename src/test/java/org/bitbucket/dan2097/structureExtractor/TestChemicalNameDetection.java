package org.bitbucket.dan2097.structureExtractor;

import java.util.List;

import org.junit.Test;
import static junit.framework.Assert.*;

public class TestChemicalNameDetection {

	@Test
	public void testTrivalCase1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("propane").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("propane", identifiedNames.get(0).getChemicalName());
		assertEquals("propane", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(7, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testTrivalCase2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("ethyl chloride").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl chloride", identifiedNames.get(0).getChemicalName());
		assertEquals("ethyl chloride", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(14, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testTrivalCase3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("ethylchloride").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("ethylchloride", identifiedNames.get(0).getChemicalName());
		assertEquals("ethylchloride", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(13, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testTrivalCase4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("ethanoic acid ethyl ester").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getChemicalName());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(3, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(25, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testTrivalCase5() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("acetic acid").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("acetic acid", identifiedNames.get(0).getChemicalName());
		assertEquals("acetic acid", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(11, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testTrivalCase6() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("vitamin C").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("vitamin c", identifiedNames.get(0).getChemicalName());
		assertEquals("vitamin C", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(9, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testTrivalCase7() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("Carbonyl cyanide m-chlorophenyl hydrazone").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("carbonyl cyanide m-chlorophenyl hydrazone", identifiedNames.get(0).getChemicalName());
		assertEquals("Carbonyl cyanide m-chlorophenyl hydrazone", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(3, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(41, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testFamily1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("alcohol").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("alcohol", identifiedNames.get(0).getChemicalName());
		assertEquals("alcohol", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.family, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(7, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testFamily2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("alcohols").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("alcohols", identifiedNames.get(0).getChemicalName());
		assertEquals("alcohols", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.family, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(8, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testFamily3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("pyridines").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("pyridines", identifiedNames.get(0).getChemicalName());
		assertEquals("pyridines", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.family, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(9, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testFamily4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("ethyloxazolones").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyloxazolones", identifiedNames.get(0).getChemicalName());
		assertEquals("ethyloxazolones", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.family, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(15, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testFamily5() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("(pyridines)").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("pyridines", identifiedNames.get(0).getChemicalName());
		assertEquals("pyridines", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.family, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(1, identifiedNames.get(0).getStart());
		assertEquals(10, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testPart1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("ethyl").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl", identifiedNames.get(0).getChemicalName());
		assertEquals("ethyl", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.part, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(5, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testPart2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("3-methyl butyl").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("3-methyl butyl", identifiedNames.get(0).getChemicalName());
		assertEquals("3-methyl butyl", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.part, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(14, identifiedNames.get(0).getEnd());
	}
	
	@Test
	public void testPart3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("pentyl,").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("pentyl", identifiedNames.get(0).getChemicalName());
		assertEquals("pentyl", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.part, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(6, identifiedNames.get(0).getEnd());
	}
	
	@Test
	public void testPart4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("methyl-").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("methyl", identifiedNames.get(0).getChemicalName());
		assertEquals("methyl", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.part, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(6, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testPolymer1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("polyethylene").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("polyethylene", identifiedNames.get(0).getChemicalName());
		assertEquals("polyethylene", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.polymer, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(12, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testPolymer2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("poly ethylene").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("poly ethylene", identifiedNames.get(0).getChemicalName());
		assertEquals("poly ethylene", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.polymer, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(13, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testPolymer3() throws Exception{
		//OPSIN doesn't actually understand polystyrene, but it understands poly and styrene!
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("polystyrene").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("polystyrene", identifiedNames.get(0).getChemicalName());
		assertEquals("polystyrene", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.polymer, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(11, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testStartsWithChemical() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("ethanoic acid ethyl ester is mixed with...").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getChemicalName());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(3, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(25, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testEndsWithChemical() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("foo was added to ethanoic acid ethyl ester").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getChemicalName());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(4, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(7, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(17, identifiedNames.get(0).getStart());
		assertEquals(42, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testHyphensInsteadOfSpaces() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("ethyl-isopropyl-ether").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl-isopropyl-ether", identifiedNames.get(0).getChemicalName());
		assertEquals("ethyl-isopropyl-ether", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(21, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testEndingPunctuation1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("2,2-Bis[4-(methacryloxyethoxy)phenyl]propane: 48 parts by mass").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("2,2-bis[4-(methacryloxyethoxy)phenyl]propane", identifiedNames.get(0).getChemicalName());
		assertEquals("2,2-Bis[4-(methacryloxyethoxy)phenyl]propane", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(44, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testEndingPunctuation2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("ethyl chloride: 1 mol/dm3").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl chloride", identifiedNames.get(0).getChemicalName());
		assertEquals("ethyl chloride", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(14, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testEndingPunctuation3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("and butane.").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("butane", identifiedNames.get(0).getChemicalName());
		assertEquals("butane", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(1, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(4, identifiedNames.get(0).getStart());
		assertEquals(10, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testEndingPunctuation4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("and ethyl chloride.").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl chloride", identifiedNames.get(0).getChemicalName());
		assertEquals("ethyl chloride", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(1, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(2, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(4, identifiedNames.get(0).getStart());
		assertEquals(18, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testSynonym() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione (caffeine)").extractNames();
		assertEquals(2, identifiedNames.size());
		assertEquals("1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione", identifiedNames.get(0).getChemicalName());
		assertEquals("1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(42, identifiedNames.get(0).getEnd());

		assertEquals("caffeine", identifiedNames.get(1).getChemicalName());
		assertEquals("caffeine", identifiedNames.get(1).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(1).getNameType());
		assertEquals(1, identifiedNames.get(1).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(1).getWordPositionEndIndice());
		assertEquals(44, identifiedNames.get(1).getStart());
		assertEquals(52, identifiedNames.get(1).getEnd());
	}

	@Test
	public void testSynonym2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione ( caffeine )").extractNames();
		assertEquals(2, identifiedNames.size());
		assertEquals("1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione", identifiedNames.get(0).getChemicalName());
		assertEquals("1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(42, identifiedNames.get(0).getEnd());

		assertEquals("caffeine", identifiedNames.get(1).getChemicalName());
		assertEquals("caffeine", identifiedNames.get(1).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(1).getNameType());
		assertEquals(2, identifiedNames.get(1).getWordPositionStartIndice());
		assertEquals(2, identifiedNames.get(1).getWordPositionEndIndice());
		assertEquals(45, identifiedNames.get(1).getStart());
		assertEquals(53, identifiedNames.get(1).getEnd());
	}

	@Test
	public void testNonchemicalBrackets() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("energy drink (1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione: 250mg and water: 250ml").extractNames();
		assertEquals(2, identifiedNames.size());
		assertEquals("1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione", identifiedNames.get(0).getChemicalName());
		assertEquals("1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(2, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(14, identifiedNames.get(0).getStart());
		assertEquals(56, identifiedNames.get(0).getEnd());

		assertEquals("water", identifiedNames.get(1).getChemicalName());
		assertEquals("water", identifiedNames.get(1).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(1).getNameType());
		assertEquals(5, identifiedNames.get(1).getWordPositionStartIndice());
		assertEquals(5, identifiedNames.get(1).getWordPositionEndIndice());
		assertEquals(68, identifiedNames.get(1).getStart());
		assertEquals(73, identifiedNames.get(1).getEnd());
	}

	@Test
	public void testNonchemicalBrackets2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("(tetrahydrofuran)").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("tetrahydrofuran", identifiedNames.get(0).getChemicalName());
		assertEquals("tetrahydrofuran", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(1, identifiedNames.get(0).getStart());
		assertEquals(16, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testNonchemicalBrackets3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("( tetrahydrofuran )").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("tetrahydrofuran", identifiedNames.get(0).getChemicalName());
		assertEquals("tetrahydrofuran", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(1, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(2, identifiedNames.get(0).getStart());
		assertEquals(17, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testNonchemicalBrackets4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("dimethyl ether (methoxymethane)").extractNames();
		assertEquals(2, identifiedNames.size());
		assertEquals("dimethyl ether", identifiedNames.get(0).getChemicalName());
		assertEquals("dimethyl ether", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(14, identifiedNames.get(0).getEnd());

		assertEquals("methoxymethane", identifiedNames.get(1).getChemicalName());
		assertEquals("methoxymethane", identifiedNames.get(1).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(1).getNameType());
		assertEquals(2, identifiedNames.get(1).getWordPositionStartIndice());
		assertEquals(2, identifiedNames.get(1).getWordPositionEndIndice());
		assertEquals(16, identifiedNames.get(1).getStart());
		assertEquals(30, identifiedNames.get(1).getEnd());
	}

	@Test
	public void testAdjacentNonNameInformation1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("benzene( 50 ml)").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("benzene", identifiedNames.get(0).getChemicalName());
		assertEquals("benzene", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(7, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testAdjacentNonNameInformation2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("benzene(50 ml)").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("benzene", identifiedNames.get(0).getChemicalName());
		assertEquals("benzene", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(0, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(7, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testList() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("An alkane or alkyl halide such as: ethane, propane, butane, ethyl chloride, propyl bromide or butyl chloride.").extractNames();
		assertEquals(6, identifiedNames.size());
		assertEquals("ethane", identifiedNames.get(0).getChemicalName());
		assertEquals("ethane", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(7, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(7, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(35, identifiedNames.get(0).getStart());
		assertEquals(41, identifiedNames.get(0).getEnd());

		assertEquals("propane", identifiedNames.get(1).getChemicalName());
		assertEquals("propane", identifiedNames.get(1).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(1).getNameType());
		assertEquals(8, identifiedNames.get(1).getWordPositionStartIndice());
		assertEquals(8, identifiedNames.get(1).getWordPositionEndIndice());
		assertEquals(43, identifiedNames.get(1).getStart());
		assertEquals(50, identifiedNames.get(1).getEnd());

		assertEquals("butane", identifiedNames.get(2).getChemicalName());
		assertEquals("butane", identifiedNames.get(2).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(2).getNameType());
		assertEquals(9, identifiedNames.get(2).getWordPositionStartIndice());
		assertEquals(9, identifiedNames.get(2).getWordPositionEndIndice());
		assertEquals(52, identifiedNames.get(2).getStart());
		assertEquals(58, identifiedNames.get(2).getEnd());

		assertEquals("ethyl chloride", identifiedNames.get(3).getChemicalName());
		assertEquals("ethyl chloride", identifiedNames.get(3).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(3).getNameType());
		assertEquals(10, identifiedNames.get(3).getWordPositionStartIndice());
		assertEquals(11, identifiedNames.get(3).getWordPositionEndIndice());
		assertEquals(60, identifiedNames.get(3).getStart());
		assertEquals(74, identifiedNames.get(3).getEnd());

		assertEquals("propyl bromide", identifiedNames.get(4).getChemicalName());
		assertEquals("propyl bromide", identifiedNames.get(4).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(4).getNameType());
		assertEquals(12, identifiedNames.get(4).getWordPositionStartIndice());
		assertEquals(13, identifiedNames.get(4).getWordPositionEndIndice());
		assertEquals(76, identifiedNames.get(4).getStart());
		assertEquals(90, identifiedNames.get(4).getEnd());

		assertEquals("butyl chloride", identifiedNames.get(5).getChemicalName());
		assertEquals("butyl chloride", identifiedNames.get(5).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(5).getNameType());
		assertEquals(15, identifiedNames.get(5).getWordPositionStartIndice());
		assertEquals(16, identifiedNames.get(5).getWordPositionEndIndice());
		assertEquals(94, identifiedNames.get(5).getStart());
		assertEquals(108, identifiedNames.get(5).getEnd());
	}

	@Test
	public void testPatentText1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("As the solvent, although not particularly limited, for example, tetrahydrofuran, toluene, dioxane , N,N-dimethylformamide, N-methylpyrrolidone, water and the like may be used alone or in combination.").extractNames();
		assertEquals(6, identifiedNames.size());
		assertEquals("tetrahydrofuran", identifiedNames.get(0).getChemicalName());
		assertEquals("tetrahydrofuran", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(9, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(9, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(64, identifiedNames.get(0).getStart());
		assertEquals(79, identifiedNames.get(0).getEnd());

		assertEquals("toluene", identifiedNames.get(1).getChemicalName());
		assertEquals("toluene", identifiedNames.get(1).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(1).getNameType());
		assertEquals(10, identifiedNames.get(1).getWordPositionStartIndice());
		assertEquals(10, identifiedNames.get(1).getWordPositionEndIndice());
		assertEquals(81, identifiedNames.get(1).getStart());
		assertEquals(88, identifiedNames.get(1).getEnd());

		assertEquals("dioxane", identifiedNames.get(2).getChemicalName());
		assertEquals("dioxane", identifiedNames.get(2).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(2).getNameType());
		assertEquals(11, identifiedNames.get(2).getWordPositionStartIndice());
		assertEquals(11, identifiedNames.get(2).getWordPositionEndIndice());
		assertEquals(90, identifiedNames.get(2).getStart());
		assertEquals(97, identifiedNames.get(2).getEnd());

		assertEquals("N,N-dimethylformamide", identifiedNames.get(3).getChemicalName());
		assertEquals("N,N-dimethylformamide", identifiedNames.get(3).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(3).getNameType());
		assertEquals(13, identifiedNames.get(3).getWordPositionStartIndice());
		assertEquals(13, identifiedNames.get(3).getWordPositionEndIndice());
		assertEquals(100, identifiedNames.get(3).getStart());
		assertEquals(121, identifiedNames.get(3).getEnd());


		assertEquals("N-methylpyrrolidone", identifiedNames.get(4).getChemicalName());
		assertEquals("N-methylpyrrolidone", identifiedNames.get(4).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(4).getNameType());
		assertEquals(14, identifiedNames.get(4).getWordPositionStartIndice());
		assertEquals(14, identifiedNames.get(4).getWordPositionEndIndice());
		assertEquals(123, identifiedNames.get(4).getStart());
		assertEquals(142, identifiedNames.get(4).getEnd());


		assertEquals("water", identifiedNames.get(5).getChemicalName());
		assertEquals("water", identifiedNames.get(5).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(5).getNameType());
		assertEquals(15, identifiedNames.get(5).getWordPositionStartIndice());
		assertEquals(15, identifiedNames.get(5).getWordPositionEndIndice());
		assertEquals(144, identifiedNames.get(5).getStart());
		assertEquals(149, identifiedNames.get(5).getEnd());

	}

	@Test
	public void testPatentText2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("Preparation of 2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl] methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfonylethoxy)pyrimidine").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl] methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfonylethoxy)pyrimidine", identifiedNames.get(0).getChemicalName());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl] methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfonylethoxy)pyrimidine", identifiedNames.get(0).getTextValue());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(3, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(15, identifiedNames.get(0).getStart());
		assertEquals(159, identifiedNames.get(0).getEnd());
	}

	@Test
	public void testPatentText3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("By using 2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine instead of 2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethylbenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine, reactions and treatments were performed in the same manner as those of Example 3 to obtain the title compound as reddish brown oil.").extractNames();
		assertEquals(2, identifiedNames.size());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine", identifiedNames.get(0).getChemicalName());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(2, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(9, identifiedNames.get(0).getStart());
		assertEquals(152, identifiedNames.get(0).getEnd());

		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethylbenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine", identifiedNames.get(1).getChemicalName());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethylbenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine", identifiedNames.get(1).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(1).getNameType());
		assertEquals(5, identifiedNames.get(1).getWordPositionStartIndice());
		assertEquals(5, identifiedNames.get(1).getWordPositionEndIndice());
		assertEquals(164, identifiedNames.get(1).getStart());
		assertEquals(306, identifiedNames.get(1).getEnd());
	}

	@Test
	public void testPatentText4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("By using 2,3-difluorobenzyl bromide instead of 3-trifluoromethylbenzyl bromide,").extractNames();
		assertEquals(2, identifiedNames.size());
		assertEquals("2,3-difluorobenzyl bromide", identifiedNames.get(0).getChemicalName());
		assertEquals("2,3-difluorobenzyl bromide", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(3, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(9, identifiedNames.get(0).getStart());
		assertEquals(35, identifiedNames.get(0).getEnd());

		assertEquals("3-trifluoromethylbenzyl bromide", identifiedNames.get(1).getChemicalName());
		assertEquals("3-trifluoromethylbenzyl bromide", identifiedNames.get(1).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(1).getNameType());
		assertEquals(6, identifiedNames.get(1).getWordPositionStartIndice());
		assertEquals(7, identifiedNames.get(1).getWordPositionEndIndice());
		assertEquals(47, identifiedNames.get(1).getStart());
		assertEquals(78, identifiedNames.get(1).getEnd());
	}

	@Test
	public void testPatentText5() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("Synthesis of Compounds 6a-n, 1-[4-(5-Bromopyrimidin-2-ylsulfanyl)phenyl]-3-(2-nitrobenzoyl)-urea (6n)").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("1-[4-(5-bromopyrimidin-2-ylsulfanyl)phenyl]-3-(2-nitrobenzoyl)-urea", identifiedNames.get(0).getChemicalName());
		assertEquals("1-[4-(5-Bromopyrimidin-2-ylsulfanyl)phenyl]-3-(2-nitrobenzoyl)-urea", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(4, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(4, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(29, identifiedNames.get(0).getStart());
		assertEquals(96, identifiedNames.get(0).getEnd());
	}

	@Test
	public void erroneousSpaces1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("added to ethyl benzene and").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl benzene", identifiedNames.get(0).getChemicalName());//erroneous space will be fixed by OPSIN later
		assertEquals("ethyl benzene", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(3, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(9, identifiedNames.get(0).getStart());
		assertEquals(22, identifiedNames.get(0).getEnd());
	}

	@Test
	public void erroneousSpaces2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("added to eth ane and").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("ethane", identifiedNames.get(0).getChemicalName());
		assertEquals("eth ane", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(3, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(9, identifiedNames.get(0).getStart());
		assertEquals(16, identifiedNames.get(0).getEnd());
	}

	@Test
	public void erroneousSpaces3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("added to et hane and").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("ethane", identifiedNames.get(0).getChemicalName());
		assertEquals("et hane", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(3, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(9, identifiedNames.get(0).getStart());
		assertEquals(16, identifiedNames.get(0).getEnd());
	}

	@Test
	public void erroneousSpaces4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("added to 2-[N-[ 3-(N-cyclopentylmethyl-N-ethyl)amino-6- methoxypyrid in-2-yl]methyl-N-(3-t rifluoromethylbe nzyl)]amino-5-(2-methylsulfinylethoxy)py rimidine and").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethylbenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine", identifiedNames.get(0).getChemicalName());
		assertEquals("2-[N-[ 3-(N-cyclopentylmethyl-N-ethyl)amino-6- methoxypyrid in-2-yl]methyl-N-(3-t rifluoromethylbe nzyl)]amino-5-(2-methylsulfinylethoxy)py rimidine", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(8, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(9, identifiedNames.get(0).getStart());
		assertEquals(157, identifiedNames.get(0).getEnd());
	}

	@Test
	public void erroneousSpaces5() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("adde to ethylbenz ene and").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("ethylbenzene", identifiedNames.get(0).getChemicalName());
		assertEquals("ethylbenz ene", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(3, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(8, identifiedNames.get(0).getStart());
		assertEquals(21, identifiedNames.get(0).getEnd());
	}

	@Test
	public void erroneousSpaces6() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("added to the 2-nitrobenzene sulfonamide 50").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("2-nitrobenzenesulfonamide", identifiedNames.get(0).getChemicalName());
		assertEquals("2-nitrobenzene sulfonamide", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(3, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(4, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(13, identifiedNames.get(0).getStart());
		assertEquals(39, identifiedNames.get(0).getEnd());
	}

	@Test
	public void erroneousSpaces7() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("benzene -ethanol").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("benzene-ethanol", identifiedNames.get(0).getChemicalName());
		assertEquals("benzene -ethanol", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(16, identifiedNames.get(0).getEnd());
	}

	@Test
	public void erroneousSpaces8() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("1-ethyl -2-methyl-benzene").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("1-ethyl-2-methyl-benzene", identifiedNames.get(0).getChemicalName());
		assertEquals("1-ethyl -2-methyl-benzene", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(25, identifiedNames.get(0).getEnd());
	}

	@Test
	public void erroneousSpaces9() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("2-ethyl-1-ben ze ne -ethanol").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("2-ethyl-1-benzene-ethanol", identifiedNames.get(0).getChemicalName());
		assertEquals("2-ethyl-1-ben ze ne -ethanol", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(3, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(28, identifiedNames.get(0).getEnd());
	}

	@Test
	public void erroneousSpaces10() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("5-amino-2-[2- (4-methoxyphenyl) vinyl] -4,6-dimorpholinopyrimidine").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("5-amino-2-[2-(4-methoxyphenyl) vinyl]-4,6-dimorpholinopyrimidine", identifiedNames.get(0).getChemicalName());
		assertEquals("5-amino-2-[2- (4-methoxyphenyl) vinyl] -4,6-dimorpholinopyrimidine", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(3, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(66, identifiedNames.get(0).getEnd());
	}

	@Test
	public void erroneousSpaces11() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("5-amino-2- [2- (4-methoxyphenyl) vinyl] -4- (4-methylpiperazin-1-yl) -6-morpholino pyrimidine").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("5-amino-2-[2-(4-methoxyphenyl) vinyl]-4-(4-methylpiperazin-1-yl)-6-morpholino pyrimidine", identifiedNames.get(0).getChemicalName());
		assertEquals("5-amino-2- [2- (4-methoxyphenyl) vinyl] -4- (4-methylpiperazin-1-yl) -6-morpholino pyrimidine", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(7, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(93, identifiedNames.get(0).getEnd());
	}

	@Test
	public void twoEntitiesDueToEntityType() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("the hydrazone carbonohydrazonoyl dicyanide").extractNames();
		assertEquals(2, identifiedNames.size());
		assertEquals("hydrazone", identifiedNames.get(0).getChemicalName());
		assertEquals("hydrazone", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.family, identifiedNames.get(0).getNameType());
		assertEquals(1, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(4, identifiedNames.get(0).getStart());
		assertEquals(13, identifiedNames.get(0).getEnd());

		assertEquals("carbonohydrazonoyl dicyanide", identifiedNames.get(1).getChemicalName());
		assertEquals("carbonohydrazonoyl dicyanide", identifiedNames.get(1).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(1).getNameType());
		assertEquals(2, identifiedNames.get(1).getWordPositionStartIndice());
		assertEquals(3, identifiedNames.get(1).getWordPositionEndIndice());
		assertEquals(14, identifiedNames.get(1).getStart());
		assertEquals(42, identifiedNames.get(1).getEnd());
	}

	@Test
	public void intendedSpace1() throws Exception{
		//counter example
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("benzene ethane").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("benzene ethane", identifiedNames.get(0).getChemicalName());
		assertEquals("benzene ethane", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(14, identifiedNames.get(0).getEnd());
	}

	@Test
	public void intendedSpace2() throws Exception{
		//counter example
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("benzene acetate").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("benzene acetate", identifiedNames.get(0).getChemicalName());
		assertEquals("benzene acetate", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(15, identifiedNames.get(0).getEnd());
	}

	@Test
	public void intendedSpace3() throws Exception{
		//counter example
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("acetic anhydride").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("acetic anhydride", identifiedNames.get(0).getChemicalName());
		assertEquals("acetic anhydride", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(16, identifiedNames.get(0).getEnd());
	}

	@Test
	public void intendedSpace4() throws Exception{
		//counter example
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("8 heptane").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("heptane", identifiedNames.get(0).getChemicalName());
		assertEquals("heptane", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(1, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(2, identifiedNames.get(0).getStart());
		assertEquals(9, identifiedNames.get(0).getEnd());
	}

	@Test
	public void intendedSpace5() throws Exception{
		//counter example
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("acetic acid (8)").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("acetic acid", identifiedNames.get(0).getChemicalName());
		assertEquals("acetic acid", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(11, identifiedNames.get(0).getEnd());
	}

	@Test
	public void nonChemicalButOpsinParsable1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("period").extractNames();//german for periodo
		assertEquals(0, identifiedNames.size());
	}

	@Test
	public void nonChemicalButOpsinParsable2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("periodic").extractNames();//periodic acid
		assertEquals(0, identifiedNames.size());
		identifiedNames = new DocumentToStructures("periodic acid").extractNames();
		assertEquals(1, identifiedNames.size());
		assertEquals("periodic acid", identifiedNames.get(0).getChemicalName());
		assertEquals("periodic acid", identifiedNames.get(0).getTextValue());
		assertEquals(NameType.complete, identifiedNames.get(0).getNameType());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(1, identifiedNames.get(0).getWordPositionEndIndice());
		assertEquals(0, identifiedNames.get(0).getStart());
		assertEquals(13, identifiedNames.get(0).getEnd());
	}

	@Test
	public void nonChemicalButOpsinParsable3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("then on").extractNames();
		assertEquals(0, identifiedNames.size());
	}

	@Test
	public void nonChemicalButOpsinParsable4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("then one").extractNames();
		assertEquals(0, identifiedNames.size());
	}

	@Test
	public void nonChemicalButOpsinParsable5() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("then it").extractNames();
		assertEquals(0, identifiedNames.size());
	}

	@Test
	public void nonChemicalButOpsinParsable6() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("then at").extractNames();
		assertEquals(0, identifiedNames.size());
	}

	@Test
	public void nonChemicalButOpsinParsable7() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("prism an").extractNames();
		assertEquals(0, identifiedNames.size());
	}

	@Test
	public void nonChemicalButOpsinParsable8() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("val in").extractNames();
		assertEquals(0, identifiedNames.size());
	}

	@Test
	public void nonChemicalButOpsinParsable9() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = new DocumentToStructures("a brine").extractNames();
		assertEquals(0, identifiedNames.size());
	}
}
