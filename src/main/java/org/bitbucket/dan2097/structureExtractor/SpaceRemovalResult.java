/**
 * 
 */
package org.bitbucket.dan2097.structureExtractor;

import uk.ac.cam.ch.wwmm.opsin.ParseRulesResults;

class SpaceRemovalResult{
	final boolean success;
	final ParseRulesResults parseRulesResults;
	final String inputString;
	final Integer spacesRemoved;

	public SpaceRemovalResult(boolean success, ParseRulesResults prr, String inputString, Integer spacesRemoved) {
		this.success = success;
		this.spacesRemoved = spacesRemoved;
		this.inputString = inputString;
		this.parseRulesResults = prr;
	}
	
	/**
	 * Was space removal successful
	 * @return
	 */
	boolean isSuccess() {
		return success;
	}

	/**
	 * Null if success=false
	 * @return
	 */
	ParseRulesResults getParseRulesResults() {
		return parseRulesResults;
	}
	
	/**
	 * Null if success=false
	 * @return
	 */
	String getInputString() {
		return inputString;
	}

	/**
	 * Null if success=false
	 * @return
	 */
	Integer getSpacesRemoved() {
		return spacesRemoved;
	}
}