package org.bitbucket.dan2097.structureExtractor;

import java.util.List;

import org.junit.Test;
import static junit.framework.Assert.*;

public class TestChemicalNameDetection {
	
	@Test
	public void testTrivalCase1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("propane");
		assertEquals(1, identifiedNames.size());
		assertEquals("propane", identifiedNames.get(0).getValue());
		assertEquals(0, identifiedNames.get(0).getStart());
	}
	
	@Test
	public void testTrivalCase2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("ethyl chloride");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl chloride", identifiedNames.get(0).getValue());
		assertEquals(0, identifiedNames.get(0).getStart());
	}

	@Test
	public void testTrivalCase3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("ethylchloride");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethylchloride", identifiedNames.get(0).getValue());
		assertEquals(0, identifiedNames.get(0).getStart());
	}

	@Test
	public void testTrivalCase4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("ethanoic acid ethyl ester");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getValue());
		assertEquals(0, identifiedNames.get(0).getStart());
	}
	
	@Test
	public void testTrivalCase5() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("acetic acid");
		assertEquals(1, identifiedNames.size());
		assertEquals("acetic acid", identifiedNames.get(0).getValue());
		assertEquals(0, identifiedNames.get(0).getStart());
	}
	
	@Test
	public void testTrivalCase6() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("vitamin C");
		assertEquals(1, identifiedNames.size());
		assertEquals("vitamin c", identifiedNames.get(0).getValue());
		assertEquals(0, identifiedNames.get(0).getStart());
	}
	
	@Test
	public void testStartsWithChemical() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("ethanoic acid ethyl ester is mixed with...");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getValue());
		assertEquals(0, identifiedNames.get(0).getStart());
	}
	
	@Test
	public void testEndsWithChemical() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("foo was added to ethanoic acid ethyl ester");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethanoic acid ethyl ester", identifiedNames.get(0).getValue());
		assertEquals(4, identifiedNames.get(0).getStart());
	}
	
	@Test
	public void testHyphensInsteadOfSpaces() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("ethyl-isopropyl-ether");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl-isopropyl-ether", identifiedNames.get(0).getValue());
		assertEquals(0, identifiedNames.get(0).getStart());
	}
	
	@Test
	public void testEndingPunctuation1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("2,2-Bis[4-(methacryloxyethoxy)phenyl]propane: 48 parts by mass");
		assertEquals(1, identifiedNames.size());
		assertEquals("2,2-bis[4-(methacryloxyethoxy)phenyl]propane", identifiedNames.get(0).getValue());
		assertEquals(0, identifiedNames.get(0).getStart());
	}
	
	@Test
	public void testEndingPunctuation2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("ethyl chloride: 1 mol/dm3");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl chloride", identifiedNames.get(0).getValue());
		assertEquals(0, identifiedNames.get(0).getStart());
	}
	
	@Test
	public void testEndingPunctuation3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("and butane.");
		assertEquals(1, identifiedNames.size());
		assertEquals("butane", identifiedNames.get(0).getValue());
		assertEquals(1, identifiedNames.get(0).getStart());
	}
	
	@Test
	public void testEndingPunctuation4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("and ethyl chloride.");
		assertEquals(1, identifiedNames.size());
		assertEquals("ethyl chloride", identifiedNames.get(0).getValue());
		assertEquals(1, identifiedNames.get(0).getStart());
	}

	@Test
	public void testList() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("An alkane or alkyl halide such as: ethane, propane, butane, ethyl chloride, propyl bromide or butyl chloride.");
		assertEquals(6, identifiedNames.size());
		assertEquals("ethane", identifiedNames.get(0).getValue());
		assertEquals("propane", identifiedNames.get(1).getValue());
		assertEquals("butane", identifiedNames.get(2).getValue());
		assertEquals("ethyl chloride", identifiedNames.get(3).getValue());
		assertEquals("propyl bromide", identifiedNames.get(4).getValue());
		assertEquals("butyl chloride", identifiedNames.get(5).getValue());
		assertEquals(7, identifiedNames.get(0).getStart());
		assertEquals(8, identifiedNames.get(1).getStart());
		assertEquals(9, identifiedNames.get(2).getStart());
		assertEquals(10, identifiedNames.get(3).getStart());
		assertEquals(12, identifiedNames.get(4).getStart());
		assertEquals(15, identifiedNames.get(5).getStart());
	}
	
	@Test
	public void testPatentText1() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("As the solvent, although not particularly limited, for example, tetrahydrofuran, toluene, dioxane , N,N-dimethylformamide, N-methylpyrrolidone, water and the like may be used alone or in combination.");
		assertEquals(6, identifiedNames.size());
		assertEquals("tetrahydrofuran", identifiedNames.get(0).getValue());
		assertEquals("toluene", identifiedNames.get(1).getValue());
		assertEquals("dioxane", identifiedNames.get(2).getValue());
		assertEquals("N,N-dimethylformamide", identifiedNames.get(3).getValue());
		assertEquals("N-methylpyrrolidone", identifiedNames.get(4).getValue());
		assertEquals("water", identifiedNames.get(5).getValue());
	}
	
	@Test
	public void testPatentText2() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("Preparation of 2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl] methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfonylethoxy)pyrimidine");
		assertEquals(1, identifiedNames.size());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl] methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfonylethoxy)pyrimidine", identifiedNames.get(0).getValue());
	}
	
	@Test
	public void testPatentText3() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("By using 2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine instead of 2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethylbenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine, reactions and treatments were performed in the same manner as those of Example 3 to obtain the title compound as reddish brown oil.");
		assertEquals(2, identifiedNames.size());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethoxybenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine", identifiedNames.get(0).getValue());
		assertEquals("2-[N-[3-(N-cyclopentylmethyl-N-ethyl)amino-6-methoxypyridin-2-yl]methyl-N-(3-trifluoromethylbenzyl)]amino-5-(2-methylsulfinylethoxy)pyrimidine", identifiedNames.get(1).getValue());
	}

	@Test
	public void testPatentText4() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("By using 2,3-difluorobenzyl bromide instead of 3-trifluoromethylbenzyl bromide,");
		assertEquals(2, identifiedNames.size());
		assertEquals("2,3-difluorobenzyl bromide", identifiedNames.get(0).getValue());
		assertEquals("3-trifluoromethylbenzyl bromide", identifiedNames.get(1).getValue());
	}
	
	@Test
	public void testPatentText5() throws Exception{
		List<IdentifiedChemicalName> identifiedNames = DocumentToStructures.extractNames("Synthesis of Compounds 6a-n, 1-[4-(5-Bromopyrimidin-2-ylsulfanyl)phenyl]-3-(2-nitrobenzoyl)-urea (6n)");
		assertEquals(1, identifiedNames.size());
		assertEquals("1-[4-(5-bromopyrimidin-2-ylsulfanyl)phenyl]-3-(2-nitrobenzoyl)-urea", identifiedNames.get(0).getValue());
	}
}
