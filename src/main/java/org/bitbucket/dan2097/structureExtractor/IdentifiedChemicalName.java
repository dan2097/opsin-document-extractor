package org.bitbucket.dan2097.structureExtractor;

import uk.ac.cam.ch.wwmm.opsin.OpsinResult;

public class IdentifiedChemicalName {

	private final int start;
	private final String value;
	private OpsinResult opsinResult;
	
	OpsinResult getOpsinResult() {
		return opsinResult;
	}

	public void setOpsinResult(OpsinResult opsinResult) {
		this.opsinResult = opsinResult;
	}

	public IdentifiedChemicalName(int start, String value) {
		this.start =start;
		this.value =value;

	}

	public String getValue() {
		return value;
	}

	public int getStart() {
		return start;
	}
}
