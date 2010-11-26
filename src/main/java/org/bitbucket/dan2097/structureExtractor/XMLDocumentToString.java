package org.bitbucket.dan2097.structureExtractor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;

import org.apache.commons.lang.StringEscapeUtils;

public class XMLDocumentToString {
	private Set<String> breakingTags = new HashSet<String>();

	public XMLDocumentToString() {
		addBreakingTag("p");
		addBreakingTag("ol");
	    addBreakingTag("ul");
	    addBreakingTag("li");
	    addBreakingTag("ol");
	    addBreakingTag("dl");
	    addBreakingTag("dt");
	    addBreakingTag("dd");
	    addBreakingTag("br");
	    addBreakingTag("div");
	}
	
	public boolean addBreakingTag(String tag){
		return breakingTags.add(tag);
	}
	
	public boolean removeBreakingTag(String tag){
		return breakingTags.remove(tag);
	}
	
	public Set<String> getBreakingTags() {
		return breakingTags;
	}
	
	public List<String> convertDocumentToNewLineDelimitedList(Document doc){
		List<String> newLineDelimitedList = new ArrayList<String>();
	    Element rootElement = doc.getRootElement();
	    Stack<Node> elementStack = new Stack<Node>();
	    elementStack.add(rootElement);
	    StringBuilder articleSb = new StringBuilder();
	    while (elementStack.size() > 0){
	    	Node currentNode = elementStack.pop();
			if (currentNode instanceof Text){
				articleSb.append(StringEscapeUtils.unescapeHtml(currentNode.getValue()));
			}
			else{
				for (int i = currentNode.getChildCount() -1 ; i >=0;  i--) {
					elementStack.add(currentNode.getChild(i));
				}
				if (currentNode instanceof Element){
					String localName = ((Element)currentNode).getLocalName();
					if (breakingTags.contains(localName)){
						newLineDelimitedList.add(articleSb.toString());
						articleSb = new StringBuilder();
					}
			    }
			}
	    }
	    if (!articleSb.toString().equals("")){
	    	newLineDelimitedList.add(articleSb.toString());
	    }
	    return newLineDelimitedList;
	}
}
