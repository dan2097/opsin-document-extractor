package org.bitbucket.dan2097.structureExtractor;

import uk.ac.cam.ch.wwmm.opsin.OpsinResult;

public class IdentifiedChemicalName {

	private final int start;
	private final String chemicalName;
	private final String textValue;
	private OpsinResult opsinResult;
	
	OpsinResult getOpsinResult() {
		return opsinResult;
	}

	public void setOpsinResult(OpsinResult opsinResult) {
		this.opsinResult = opsinResult;
	}

	/**
	 * Construct from the starting word indice, the chemicalName as reported by OPSIN and the orginal text value
	 * @param start
	 * @param chemicalName
	 * @param textValue
	 */
	public IdentifiedChemicalName(int start, String chemicalName, String textValue) {
		this.start =start;
		this.chemicalName =chemicalName;
		this.textValue =textValue;
	}

	/**
	 * Gets the chemical name identified by OPSIN
	 * Normalisation and white space removal may have occurred compared to the original text which can be retrieved with getTextValue
	 * @return
	 */
	public String getChemicalName() {
		return chemicalName;
	}
	
	/**
	 * Gets the text from which the chemical name was found. Usually you actually want getChemicalName as this will not include trailing punctuation
	 * @return
	 */
	public String getTextValue() {
		return textValue;
	}

	/**
	 * Returns the indice of the starting word.
	 * Remember the input to the program is whiteSpace separated words
	 * @return
	 */
	public int getWordPositionIndice() {
		return start;
	}
}
