package uk.ac.ebi.pride.psmindex.search.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class cleans a PSM Sequence by converting it to uppercase, and removing any unknown characters.
 */
public class PsmSequenceCleaner {
  private static Logger logger = LoggerFactory.getLogger(PsmSequenceCleaner.class.getName());

  public static final String NO_PEPTIDE_REGEX = "[^ABCDEFGHIJKLMNPQRSTUVWXYZ]";

  /**
   * This class cleans a PSM Sequence by converting it to uppercase, and removing any unknown characters
   * based off the NO_PEPTIDE_REGEX.
   * @param peptideSequence the input peptideSequence, may be null or empty.
   * @return the cleaned peptide sequence, may be null.
   */
  public static String cleanPeptideSequence(String peptideSequence) {
    String result = null;
    logger.debug("Cleaing the input peptide sequence: " + peptideSequence);
    if(!StringUtils.isEmpty(peptideSequence)) {
      result = peptideSequence.toUpperCase().replaceAll(NO_PEPTIDE_REGEX, "");
    }
    logger.debug("Peptide sequence after cleaning: " + result);
    return result;
  }
}
