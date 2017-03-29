package uk.ac.ebi.pride.psmindex.search.util;

import junit.framework.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MongoMongoPsmSequenceCleanerTest {

  private static final String PEPSEQ_CLEAN = "MDPNTIIEALR";

  private static final String PEPSEQ_WITH_STAR = "M*DPNTIIEALR";
  private static final String PEPSEQ_WITH_AT = "M@DPNTIIEALR";
  private static final String PEPSEQ_WITH_NUMBERS = "M1DP2NT4IIEALR";
  private static final String PEPSEQ_WITH_INVALID_AA = "MoDPNTIIEALR";
  private static final String PEPSEQ_LC = "mdpntiiealr";

  @Test
  public void testCleanPeptideSequence() throws Exception {
    assertEquals(PEPSEQ_CLEAN, MongoPsmSequenceCleaner.cleanPeptideSequence(PEPSEQ_WITH_STAR));
    assertEquals(PEPSEQ_CLEAN, MongoPsmSequenceCleaner.cleanPeptideSequence(PEPSEQ_WITH_AT));
    assertEquals(PEPSEQ_CLEAN, MongoPsmSequenceCleaner.cleanPeptideSequence(PEPSEQ_WITH_NUMBERS));
    assertEquals(PEPSEQ_CLEAN, MongoPsmSequenceCleaner.cleanPeptideSequence(PEPSEQ_WITH_INVALID_AA));
    assertEquals(PEPSEQ_CLEAN, MongoPsmSequenceCleaner.cleanPeptideSequence(PEPSEQ_WITH_INVALID_AA));
    assertEquals(PEPSEQ_CLEAN, MongoPsmSequenceCleaner.cleanPeptideSequence(PEPSEQ_CLEAN));
    assertEquals(PEPSEQ_CLEAN, MongoPsmSequenceCleaner.cleanPeptideSequence(PEPSEQ_LC));
  }

  @Test
  public void testCleanNullPeptideSequence() throws Exception {
    Assert.assertEquals(null, MongoPsmSequenceCleaner.cleanPeptideSequence(null));
  }
}
