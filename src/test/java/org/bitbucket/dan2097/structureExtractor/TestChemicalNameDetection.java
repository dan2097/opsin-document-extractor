package org.bitbucket.dan2097.structureExtractor;

import java.util.List;

import org.junit.Test;
import static junit.framework.Assert.*;

public class TestChemicalNameDetection {
	
	@Test
	public void testTrivalCase1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("propane");
		assertEquals(1, identifiedNames.size());
		assertEquals("propane", identifiedNames.get(0).getChemicalName());
		assertEquals("propane", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testTrivalCase2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("ethyl chloride");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl chloride", identifiedNames.get(0).getChemicalName());
		assertEquals("ethyl chloride", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}

	@Test
	public void testTrivalCase3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("ethylchloride");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethylchloride", identifiedNames.get(0).getChemicalName());
		assertEquals("ethylchloride", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}

	@Test
	public void testTrivalCase4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("ethanoic acid ethyl ester");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getChemicalName());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testTrivalCase5() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("acetic acid");
		assertEquals(1, identifiedNames.size());
		assertEquals("acetic acid", identifiedNames.get(0).getChemicalName());
		assertEquals("acetic acid", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testTrivalCase6() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("vitamin C");
		assertEquals(1, identifiedNames.size());
		assertEquals("vitamin c", identifiedNames.get(0).getChemicalName());
		assertEquals("vitamin C", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testStartsWithChemical() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("ethanoic acid ethyl ester is mixed with...");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getChemicalName());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testEndsWithChemical() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("foo was added to ethanoic acid ethyl ester");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getChemicalName());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getTextValue());
		assertEquals(4, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testHyphensInsteadOfSpaces() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("ethyl-isopropyl-ether");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl-isopropyl-ether", identifiedNames.get(0).getChemicalName());
		assertEquals("ethyl-isopropyl-ether", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testEndingPunctuation1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("2,2-Bis[4-(methacryloxyethoxy)phenyl]propane: 48 parts by mass");
		assertEquals(1, identifiedNames.size());
		assertEquals("2,2-bis[4-(methacryloxyethoxy)phenyl]propane", identifiedNames.get(0).getChemicalName());
		assertEquals("2,2-Bis[4-(methacryloxyethoxy)phenyl]propane:", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testEndingPunctuation2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("ethyl chloride: 1 mol/dm3");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl chloride", identifiedNames.get(0).getChemicalName());
		assertEquals("ethyl chloride:", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testEndingPunctuation3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("and butane.");
		assertEquals(1, identifiedNames.size());
		assertEquals("butane", identifiedNames.get(0).getChemicalName());
		assertEquals("butane.", identifiedNames.get(0).getTextValue());
		assertEquals(1, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testEndingPunctuation4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("and ethyl chloride.");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl chloride", identifiedNames.get(0).getChemicalName());
		assertEquals("ethyl chloride.", identifiedNames.get(0).getTextValue());
		assertEquals(1, identifiedNames.get(0).getWordPositionStartIndice());
	}

	@Test
	public void testSynonym() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione (caffeine)");
		assertEquals(1, identifiedNames.size());
		assertEquals("1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione (caffeine)", identifiedNames.get(0).getChemicalName());//this behaviour is not ideal
		assertEquals("1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione (caffeine)", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testNonchemicalBrackets() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("energy drink (1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione: 250mg and water: 250ml");
		assertEquals(2, identifiedNames.size());
		assertEquals("1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione", identifiedNames.get(0).getChemicalName());
		assertEquals("water", identifiedNames.get(1).getChemicalName());
		assertEquals("(1,3,7-trimethyl-1H-purine-2,6(3H,7H)-dione:", identifiedNames.get(0).getTextValue());
		assertEquals("water:", identifiedNames.get(1).getTextValue());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(5, identifiedNames.get(1).getWordPositionStartIndice());
	}
	
	@Test
	public void testAdjacentNonNameInformation1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("benzene( 50 ml)");
		assertEquals(1, identifiedNames.size());
		assertEquals("benzene", identifiedNames.get(0).getChemicalName());
		assertEquals("benzene(", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testAdjacentNonNameInformation2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("benzene(50 ml)");
		assertEquals(1, identifiedNames.size());
		assertEquals("benzene", identifiedNames.get(0).getChemicalName());
		assertEquals("benzene(50", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}

	@Test
	public void testList() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("An alkane or alkyl halide such as: ethane, propane, butane, ethyl chloride, propyl bromide or butyl chloride.");
		assertEquals(6, identifiedNames.size());
		assertEquals("ethane", identifiedNames.get(0).getChemicalName());
		assertEquals("propane", identifiedNames.get(1).getChemicalName());
		assertEquals("butane", identifiedNames.get(2).getChemicalName());
		assertEquals("ethyl chloride", identifiedNames.get(3).getChemicalName());
		assertEquals("propyl bromide", identifiedNames.get(4).getChemicalName());
		assertEquals("butyl chloride", identifiedNames.get(5).getChemicalName());
		assertEquals("ethane,", identifiedNames.get(0).getTextValue());
		assertEquals("propane,", identifiedNames.get(1).getTextValue());
		assertEquals("butane,", identifiedNames.get(2).getTextValue());
		assertEquals("ethyl chloride,", identifiedNames.get(3).getTextValue());
		assertEquals("propyl bromide", identifiedNames.get(4).getTextValue());
		assertEquals("butyl chloride.", identifiedNames.get(5).getTextValue());
		assertEquals(7, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(8, identifiedNames.get(1).getWordPositionStartIndice());
		assertEquals(9, identifiedNames.get(2).getWordPositionStartIndice());
		assertEquals(10, identifiedNames.get(3).getWordPositionStartIndice());
		assertEquals(12, identifiedNames.get(4).getWordPositionStartIndice());
		assertEquals(15, identifiedNames.get(5).getWordPositionStartIndice());
	}
	
	@Test
	public void testPatentText1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("As the solvent, although not particularly limited, for example, tetrahydrofuran, toluene, dioxane , N,N-dimethylformamide, N-methylpyrrolidone, water and the like may be used alone or in combination.");
		assertEquals(6, identifiedNames.size());
		assertEquals("tetrahydrofuran", identifiedNames.get(0).getChemicalName());
		assertEquals("toluene", identifiedNames.get(1).getChemicalName());
		assertEquals("dioxane", identifiedNames.get(2).getChemicalName());
		assertEquals("N,N-dimethylformamide", identifiedNames.get(3).getChemicalName());
		assertEquals("N-methylpyrrolidone", identifiedNames.get(4).getChemicalName());
		assertEquals("water", identifiedNames.get(5).getChemicalName());
		assertEquals("tetrahydrofuran,", identifiedNames.get(0).getTextValue());
		assertEquals("toluene,", identifiedNames.get(1).getTextValue());
		assertEquals("dioxane", identifiedNames.get(2).getTextValue());
		assertEquals("N,N-dimethylformamide,", identifiedNames.get(3).getTextValue());
		assertEquals("N-methylpyrrolidone,", identifiedNames.get(4).getTextValue());
		assertEquals("water", identifiedNames.get(5).getTextValue());		
		assertEquals(9, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(10, identifiedNames.get(1).getWordPositionStartIndice());
		assertEquals(11, identifiedNames.get(2).getWordPositionStartIndice());
		assertEquals(13, identifiedNames.get(3).getWordPositionStartIndice());
		assertEquals(14, identifiedNames.get(4).getWordPositionStartIndice());
		assertEquals(15, identifiedNames.get(5).getWordPositionStartIndice());
		
	}
	
	@Test
	public void testPatentText2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("Preparation of 2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl] methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfonylethoxy)pyrimidine");
		assertEquals(1, identifiedNames.size());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl] methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfonylethoxy)pyrimidine", identifiedNames.get(0).getChemicalName());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl] methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfonylethoxy)pyrimidine", identifiedNames.get(0).getTextValue());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void testPatentText3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("By using 2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine instead of 2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethylbenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine, reactions and treatments were performed in the same manner as those of Example 3 to obtain the title compound as reddish brown oil.");
		assertEquals(2, identifiedNames.size());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine", identifiedNames.get(0).getChemicalName());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethylbenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine", identifiedNames.get(1).getChemicalName());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine", identifiedNames.get(0).getTextValue());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethylbenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine,", identifiedNames.get(1).getTextValue());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(5, identifiedNames.get(1).getWordPositionStartIndice());
	}

	@Test
	public void testPatentText4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("By using 2,3-difluorobenzyl bromide instead of 3-trifluoromethylbenzyl bromide,");
		assertEquals(2, identifiedNames.size());
		assertEquals("2,3-difluorobenzyl bromide", identifiedNames.get(0).getChemicalName());
		assertEquals("3-trifluoromethylbenzyl bromide", identifiedNames.get(1).getChemicalName());
		assertEquals("2,3-difluorobenzyl bromide", identifiedNames.get(0).getTextValue());
		assertEquals("3-trifluoromethylbenzyl bromide,", identifiedNames.get(1).getTextValue());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
		assertEquals(6, identifiedNames.get(1).getWordPositionStartIndice());
	}
	
	@Test
	public void testPatentText5() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("Synthesis of Compounds 6a-n, 1-[4-(5-Bromopyrimidin-2-ylsulfanyl)phenyl]-3-(2-nitrobenzoyl)-urea (6n)");
		assertEquals(1, identifiedNames.size());
		assertEquals("1-[4-(5-bromopyrimidin-2-ylsulfanyl)phenyl]-3-(2-nitrobenzoyl)-urea", identifiedNames.get(0).getChemicalName());
		assertEquals("1-[4-(5-Bromopyrimidin-2-ylsulfanyl)phenyl]-3-(2-nitrobenzoyl)-urea", identifiedNames.get(0).getTextValue());
		assertEquals(4, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void erroneousSpaces1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("added to ethyl benzene and");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl benzene", identifiedNames.get(0).getChemicalName());//erroneous space will be fixed by OPSIN later
		assertEquals("ethyl benzene", identifiedNames.get(0).getTextValue());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void erroneousSpaces2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("added to eth ane and");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethane", identifiedNames.get(0).getChemicalName());
		assertEquals("eth ane", identifiedNames.get(0).getTextValue());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
	}

	@Test
	public void erroneousSpaces3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("added to et hane and");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethane", identifiedNames.get(0).getChemicalName());
		assertEquals("et hane", identifiedNames.get(0).getTextValue());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void erroneousSpaces4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("added to 2-[N-[ 3-(N-cyclopentylmethyl-N-ethyl)amino-6- methoxypyrid in-2-yl]methyl-N-(3-t rifluoromethylbe nzyl)]amino-5-(2-methylsulfinylethoxy)py rimidine and");
		assertEquals(1, identifiedNames.size());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethylbenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine", identifiedNames.get(0).getChemicalName());
		assertEquals("2-[N-[ 3-(N-cyclopentylmethyl-N-ethyl)amino-6- methoxypyrid in-2-yl]methyl-N-(3-t rifluoromethylbe nzyl)]amino-5-(2-methylsulfinylethoxy)py rimidine", identifiedNames.get(0).getTextValue());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void erroneousSpaces5() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("adde to ethylbenz ene and");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethylbenzene", identifiedNames.get(0).getChemicalName());
		assertEquals("ethylbenz ene", identifiedNames.get(0).getTextValue());
		assertEquals(2, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void erroneousSpaces6() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("added to the 2-nitrobenzene sulfonamide 50");
		assertEquals(1, identifiedNames.size());
		assertEquals("2-nitrobenzenesulfonamide", identifiedNames.get(0).getChemicalName());
		assertEquals("2-nitrobenzene sulfonamide", identifiedNames.get(0).getTextValue());
		assertEquals(3, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void erroneousSpaces7() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("benzene -ethanol");
		assertEquals(1, identifiedNames.size());
		assertEquals("benzene-ethanol", identifiedNames.get(0).getChemicalName());
		assertEquals("benzene -ethanol", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void intendedSpace1() throws Exception{
		//counter example
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("benzene ethane");
		assertEquals(1, identifiedNames.size());
		assertEquals("benzene ethane", identifiedNames.get(0).getChemicalName());
		assertEquals("benzene ethane", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
	
	@Test
	public void intendedSpace2() throws Exception{
		//counter example
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("benzene acetate");
		assertEquals(1, identifiedNames.size());
		assertEquals("benzene acetate", identifiedNames.get(0).getChemicalName());
		assertEquals("benzene acetate", identifiedNames.get(0).getTextValue());
		assertEquals(0, identifiedNames.get(0).getWordPositionStartIndice());
	}
}
