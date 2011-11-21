package uk.ac.cam.ch.wwmm.opsin;

/**
 * Allows public access to the OPSIN PreProcessor and offers a method that does not throw exceptions
 * @author dl387
 *
 */
public class OpsinPreProcessorWrapper {

	/**
	 * Employs the OPSIN preprocessor to normalise the given word.
	 * @param word
	 * @return
	 */
	public static String normalise(String word){
		try {
			return PreProcessor.preProcess(word);
		} catch (PreProcessingException e) {
			return word;//word has unrecognised unicode character
		}
	}
}
