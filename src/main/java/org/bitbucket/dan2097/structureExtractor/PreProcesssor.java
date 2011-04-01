package org.bitbucket.dan2097.structureExtractor;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reduced form of OPSIN's preprocessor
 * @author dl387
 *
 */
public class PreProcesssor {
	
	
	private static final Pattern MATCH_DOLLAR = Pattern.compile("\\$");
	private static final Pattern MATCH_SULPH = Pattern.compile("sulph");
	private static final HashMap<String, String> GREEK_MAP = new HashMap<String, String>();
	static {
		GREEK_MAP.put("a", "alpha");
		GREEK_MAP.put("b", "beta");
		GREEK_MAP.put("g", "gamma");
		GREEK_MAP.put("d", "delta");
		GREEK_MAP.put("e", "epsilon");
		GREEK_MAP.put("l", "lambda");
	}
	
	
	public static String preProcess(String sentence){
		sentence = processDollarPrefixedGreeks(sentence);
		sentence = convertNonAsciiAndNormaliseRepresentation(sentence);
		sentence = MATCH_SULPH.matcher(sentence).replaceAll("sulf");//correct British spelling to the IUPAC spelling
		return sentence;
	}

	private static String processDollarPrefixedGreeks(String sentence) {
		Matcher m = MATCH_DOLLAR.matcher(sentence);
		while (m.find()){
			if (sentence.length()>m.end()){
				String letter = sentence.substring(m.end(), m.end()+1).toLowerCase();
				if (GREEK_MAP.containsKey(letter)){
					sentence = sentence.substring(0, m.end()-1) +GREEK_MAP.get(letter) +  sentence.substring(m.end()+1);
					m = MATCH_DOLLAR.matcher(sentence);
				}
			}
		}
		return sentence;
	}
	
	/**Converts a unicode string into ISO-8859-1, converting greek letters
	 * to their names, and difficult characters to underscore.
	 *
	 * @param s The string to convert.
	 * @return The converted string.
	 * @throws PreProcessingException
	 */
	private static String convertNonAsciiAndNormaliseRepresentation(String s){
		StringBuilder sb = new StringBuilder(s);
		for(int i=0;i<sb.length();i++) {
			char c = sb.charAt(i);
			if(c >= 128) {
				sb.replace(i, i+1, getReplacementForNonASCIIChar(c));//replace non ascii characters with hard coded ascii strings
			}
			else if (c ==96){
				sb.replace(i, i+1, "'");//replace back ticks with apostrophe
			}
			else if (c ==34){
				sb.replace(i, i+1, "''");//replace quotation mark with two primes
			}
			else if (c<=31){
				sb.replace(i, i+1, "");//ignore control characters
			}
		}
		return sb.toString();
	}

    private static String getReplacementForNonASCIIChar(char c){
        switch (c) {
            case '\u03b1': return "alpha";//greeks
            case '\u03b2': return "beta";
            case '\u03b3': return "gamma";
            case '\u03b4': return "delta";
            case '\u03b5': return "epsilon";
            case '\u03b6': return "zeta";
            case '\u03b7': return "eta";
            case '\u03b8': return "theta";
            case '\u03b9': return "iota";
            case '\u03ba': return "kappa";
            case '\u03bb': return "lambda";
            case '\u03bc': return "mu";
            case '\u03bd': return "nu";
            case '\u03be': return "xi";
            case '\u03bf': return "omicron";
            case '\u03c0': return "pi";
            case '\u03c1': return "rho";
            case '\u03c2': return "stigma";
            case '\u03c3': return "sigma";
            case '\u03c4': return "tau";
            case '\u03c5': return "upsilon";
            case '\u03c6': return "phi";
            case '\u03c7': return "chi";
            case '\u03c8': return "psi";
            case '\u03c9': return "omega";

            case '\u00B1': return "+-";//plus minus symbol
            case '\u2213': return "-+";
            
            case '\u00C6': return "ae";//common ligatures
            case '\u00E6': return "ae";
            case '\u0152': return "oe";
            case '\u0153': return "oe";
            case '\u0132': return "ij";
            case '\u0133': return "ij";
            case '\u1D6B': return "ue";
            case '\uFB00': return "ff";
            case '\uFB01': return "fi";
            case '\uFB02': return "fl";
            case '\uFB03': return "ffi";
            case '\uFB04': return "ffl";
            case '\uFB06': return "st";
            
            case '\u00E9': return "e";//diacritics

            case '\u2018': return "'";//quotation marks and primes (map to apostrophe/s)
            case '\u2019': return "'";
            case '\u201B': return "'";
            case '\u2032': return "'";//primes
            case '\u2033': return "''";
            case '\u2034': return "'''";
            case '\u2057': return "''''";
            case '\u2035': return "'";//back primes
            case '\u2036': return "''";
            case '\u2037': return "'''";
            case '\u00B4': return "'";//accents
            case '\u02CA': return "'";
            case '\u0301': return "'";
            case '\u02DD': return "''";
            case '\u030B': return "''";

            case '\u2010': return "-";//dashes, hyphens and the minus sign
            case '\u2011': return "-";
            case '\u2012': return "-";
            case '\u2013': return "-";
            case '\u2014': return "-";
            case '\u2015': return "-";
            case '\u2212': return "-";
            
            case '\u00A0': return " ";//Non-breaking spaces
            case '\u2007': return " ";
            case '\u202F': return " ";
            
            case '\u200b': return "";//zero width space
            case '\u200d': return "";//zero width joiner

            case '\uFEFF': return "";//BOM-found at the start of some UTF files

            default: return "!";
        }
    }
}
