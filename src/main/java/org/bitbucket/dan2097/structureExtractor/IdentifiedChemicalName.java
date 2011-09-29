package org.bitbucket.dan2097.structureExtractor;

public class IdentifiedChemicalName {

	private final int start;
	private final int end;
	private final int startWordIndice;
	private final int endWordIndice;
	private final String chemicalName;
	private final String textValue;
	private final NameType nameType;

	/**
	 * Construct from the starting word indice, the ending word indice
	 * the absolute start indice, the absolute end indice
	 * the chemicalName as reported by OPSIN and the original text value
	 * @param startWordIndice
	 * @param endWordIndice
	 * @param start
	 * @param end
	 * @param chemicalName
	 * @param textValue
	 * @param nameType
	 */
	public IdentifiedChemicalName(int startWordIndice, int endWordIndice, int start, int end, String chemicalName, String textValue, NameType nameType) {
		this.startWordIndice = startWordIndice;
		this.endWordIndice = endWordIndice;
		this.start = start;
		this.end = end;
		this.chemicalName = chemicalName;
		this.textValue = textValue;
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
	 * Gets the text from which the chemical name was found
	 * @return
	 */
	public String getTextValue() {
		return textValue;
	}

	/**
	 * Returns the indice of the starting word.
	 * NOTE: Words are determined using whiteSpace as a delimiter
	 * @return
	 */
	public int getWordPositionStartIndice() {
		return startWordIndice;
	}
	
	/**
	 * Returns the indice of the final word.
	 * NOTE: Words are determined using whiteSpace as a delimiter
	 * @return
	 */
	public int getWordPositionEndIndice() {
		return endWordIndice;
	}
	
	/**
	 * Returns the start of the chemical name as a character offset from the start of the input string
	 * @return
	 */
	public int getStart() {
		return start;
	}

	/**
	 * Returns the end of the chemical name as a character offset from the start of the input string
	 * @return
	 */
	public int getEnd() {
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
