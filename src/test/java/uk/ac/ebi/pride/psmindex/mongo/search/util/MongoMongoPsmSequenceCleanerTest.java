package uk.ac.ebi.pride.psmindex.mongo.search.util;

import junit.framework.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests cleaning PSM sequences.
 */
public class MongoMongoPsmSequenceCleanerTest {

  private static final String PEPSEQ_CLEAN = "MDPNTIIEALR";

  private static final String PEPSEQ_WITH_STAR = "M*DPNTIIEALR";
  private static final String PEPSEQ_WITH_AT = "M@DPNTIIEALR";
  private static final String PEPSEQ_WITH_NUMBERS = "M1DP2NT4IIEALR";
  private static final String PEPSEQ_WITH_INVALID_AA = "MoDPNTIIEALR";
  private static final String PEPSEQ_LC = "mdpntiiealr";

  /**
   * Tests cleaning peptide sequences.
   */
  @Test
  public void testCleanPeptideSequence() {
    assertEquals(PEPSEQ_CLEAN, MongoPsmSequenceCleaner.cleanPeptideSequence(PEPSEQ_WITH_STAR));
    assertEquals(PEPSEQ_CLEAN, MongoPsmSequenceCleaner.cleanPeptideSequence(PEPSEQ_WITH_AT));
    assertEquals(PEPSEQ_CLEAN, MongoPsmSequenceCleaner.cleanPeptideSequence(PEPSEQ_WITH_NUMBERS));
    assertEquals(PEPSEQ_CLEAN, MongoPsmSequenceCleaner.cleanPeptideSequence(PEPSEQ_WITH_INVALID_AA));
    assertEquals(PEPSEQ_CLEAN, MongoPsmSequenceCleaner.cleanPeptideSequence(PEPSEQ_WITH_INVALID_AA));
    assertEquals(PEPSEQ_CLEAN, MongoPsmSequenceCleaner.cleanPeptideSequence(PEPSEQ_CLEAN));
    assertEquals(PEPSEQ_CLEAN, MongoPsmSequenceCleaner.cleanPeptideSequence(PEPSEQ_LC));
  }

  /**
   * Tests cleaning a null sequence.
   */
  @Test
  public void testCleanNullPeptideSequence() {
    Assert.assertEquals(null, MongoPsmSequenceCleaner.cleanPeptideSequence(null));
  }
}
