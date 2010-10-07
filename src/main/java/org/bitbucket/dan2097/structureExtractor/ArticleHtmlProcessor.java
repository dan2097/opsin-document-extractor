package org.bitbucket.dan2097.structureExtractor;

import java.io.File;
import java.io.StringReader;
import java.util.List;
import java.util.Stack;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import uk.ac.cam.ch.wwmm.opsin.XOMTools;

public class ArticleHtmlProcessor {
	
	public static Document buildXomDoucmentFromArticle(String fileLocation) throws Exception{
		return buildXomDoucmentFromArticle(new File(fileLocation));
	}
	
	public static Document buildXomDoucmentFromArticle(File f) throws Exception{
	    XMLReader tagsoup = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
	    Builder xomBuilder = new Builder(tagsoup);
	    String html = FileUtils.readFileToString(f);
	    html = html.replaceAll("\\r\\n", "\\n");
	    
	   return  xomBuilder.build(new StringReader(html));
	}

	public static String convertArticleDocumentToString(Document journalArticle) throws Exception{
	    Element rootElement = journalArticle.getRootElement();
	    List<Element> articleBody = XOMTools.getDescendantElementsWithTagNameAndAttribute(rootElement, "div", "id", "articleBody");
	    if (articleBody.size() !=1){
	    	throw new Exception("Unexpected article contents. Is this a Journal of Medicinal Chemistry article?");
	    }
	    Element rootArticle =articleBody.get(0);
	    Stack<Node> elementStack = new Stack<Node>();
	    elementStack.add(rootArticle);
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
				    if (localName.equals("p")){
				    	articleSb.append("\n");
				    }
				    else if (localName.equals("ol")){
				    	articleSb.append("\n");
				    }
				    else if (localName.equals("ul")){
				    	articleSb.append("\n");
				    }
				    else if (localName.equals("li")){
				    	articleSb.append("\n");
				    }
				    else if (localName.equals("ol")){
				    	articleSb.append("\n");
				    }  
				    else if (localName.equals("dl")){
				    	articleSb.append("\n");
				    }
				    else if (localName.equals("dt")){
				    	articleSb.append("\n");
				    }
				    else if (localName.equals("dd")){
				    	articleSb.append("\n");
				    }
				    else if (localName.equals("br")){
				    	articleSb.append("\n");
				    }
				    else if (localName.equals("div")){
				    	articleSb.append("\n");
				    }
			    }
			}
	    }
	    return articleSb.toString();
	}
}
