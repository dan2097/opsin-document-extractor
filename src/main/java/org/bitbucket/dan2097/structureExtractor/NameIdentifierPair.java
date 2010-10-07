package org.bitbucket.dan2097.structureExtractor;

public class NameIdentifierPair {
	public enum IdentifierType{
		alias,
		identifier
	}
	public final String name;
	public final String identifier;
	public final IdentifierType identifierType;
	
	public NameIdentifierPair(String name, String identifier, IdentifierType identifierType) {
		this.name = name;
		this.identifier = identifier;
		this.identifierType = identifierType;
	}
	
}


