OPSIN Document Extractor
========================

This library uses OPSIN to find chemical names in free text that are parsable by OPSIN.
This works by determining which words are parsable by OPSIN and assuming that contiguous
space separated chemical words form part of the same chemical name.
As OPSIN's parser is quite fast, in practice this is also a fast way of doing chemical
entity recognition for systematic chemical names.

### Example Usage

	String input = "Pyridine and benzene are chemicals. ethylpyridines are a family of chemicals";
	List<IdentifiedChemicalName> chemicalNames = new DocumentToStructures(input).extractNames();
	for (IdentifiedChemicalName chemicalName : chemicalNames) {
		System.out.println(chemicalName.getStart() + "\t" + chemicalName.getEnd());//Character offsets of start/end of chemical name
		System.out.println(chemicalName.getTextValue());//The string of text between the start and the end of the chemical name
		System.out.println(chemicalName.getChemicalName());//The chemical name after character/case normalization
		System.out.println(chemicalName.getNameType());//The type of chemical name (complete/part/family/polymer)
	}

[![Build Status](https://travis-ci.com/dan2097/opsin-document-extractor.svg?branch=master)](https://travis-ci.com/dan2097/opsin-document-extractor)
