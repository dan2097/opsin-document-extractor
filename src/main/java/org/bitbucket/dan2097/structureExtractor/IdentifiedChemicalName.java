package org.bitbucket.dan2097.structureExtractor;

public class IdentifiedChemicalName {

	private final int start;
	private final int end;
	private final String chemicalName;
	private final String textValue;
	private final NameType nameType;
	/**
	 * Construct from the starting word indice, the final word indice, the chemicalName as reported by OPSIN and the original text value
	 * @param start
	 * @param end
	 * @param chemicalName
	 * @param textValue
	 * @param nameType
	 */
	public IdentifiedChemicalName(int start, int end,  String chemicalName, String textValue, NameType nameType) {
		this.start =start;
		this.end =end;
		this.chemicalName =chemicalName;
		this.textValue =textValue;
		this.nameType = nameType;
	}

	/**
	 * Gets the chemical name identified by OPSIN.
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
	public int getWordPositionStartIndice() {
		return start;
	}
	
	/**
	 * Returns the indice of the final word.
	 * Remember the input to the program is whiteSpace separated words
	 * @return
	 */
	public int getWordPositionEndIndice() {
		return end;
	}

	/**
	 * Is the name a complete name, partial name (i.e. a substituent/radical) or a class name
	 * @return
	 */
	public NameType getNameType() {
		return nameType;
	}
}
