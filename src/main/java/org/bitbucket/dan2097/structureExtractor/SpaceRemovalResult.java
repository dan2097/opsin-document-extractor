/**
 * 
 */
package org.bitbucket.dan2097.structureExtractor;

import uk.ac.cam.ch.wwmm.opsin.ParseRulesResults;

class SpaceRemovalResult{
	boolean isSuccess() {
		return success;
	}
	int getSpacesRemoved() {
		return spacesRemoved;
	}
	ParseRulesResults getParseRulesResults() {
		return parseRulesResults;
	}
	final boolean success;
	final int spacesRemoved;
	final ParseRulesResults parseRulesResults;
	public SpaceRemovalResult(boolean success, int spacesRemoved, ParseRulesResults prr) {
		this.success = success;
		this.spacesRemoved = spacesRemoved;
		this.parseRulesResults = prr;
	}
}