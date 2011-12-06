package uk.ac.cam.ch.wwmm.opsin;

/**
 * Allows public access to the OPSIN PreProcessor and offers a method that does not throw exceptions
 * @author dl387
 *
 */
public class OpsinPreProcessorWrapper {

	/**
	 * Employs the OPSIN preprocessor to normalise the given word.
	 * In the case that OPSIN does not recognise a unicode character non-ASCII characters are replaced by underscores
	 * @param word
	 * @return
	 */
	public static String normalise(String word){
		try {
			return PreProcessor.preProcess(word);
		} catch (PreProcessingException e) {
			StringBuilder sb = new StringBuilder(word);
			for(int i=0;i<sb.length();i++) {
				char c = sb.charAt(i);
				if(c >= 128) {
					sb.replace(i, i+1, "_");//replace non ascii characters with underscore
				}
			}
			//word has unrecognised unicode character
			return sb.toString();
		}
	}
}
