package uk.ac.ebi.pride.psmindex.mongo.search.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static uk.ac.ebi.pride.psmindex.mongo.search.util.MongoPsmIdBuilder.*;

/**
 * Tests building PSM IDs.
 */

public class MongoMongoPsmIdBuilderTest {

  private static final String PROJECT_ACCESSION = "PXT000111";
  private static final String ASSAY_ACCESSION = "12345";
  private static final String PROTEIN_ACCESSION = "P12345";
  private static final String REPORTED_ID = "12345";
  private static final String PEP_SEQUENCE = "ABCDEF";

  private static final String ID =
      PROJECT_ACCESSION + SEPARATOR +
          ASSAY_ACCESSION + SEPARATOR +
          REPORTED_ID + SEPARATOR +
          PROTEIN_ACCESSION + SEPARATOR +
          PEP_SEQUENCE;

  /**
   * Tests getting an ID.
   */
  @Test
  public void testGetId() {
    String newID = getId(PROJECT_ACCESSION, ASSAY_ACCESSION, REPORTED_ID, PROTEIN_ACCESSION, PEP_SEQUENCE);
    assertEquals(ID, newID);
  }

  /**
   * Tests getting a project accession.
   */
  @Test
  public void testGetProjectAccession() {
    assertEquals(PROJECT_ACCESSION, getProjectAccession(ID));
  }

  /**
   * Tests getting an assay accession.
   */
  @Test
  public void testGetAssayAccession() {
    assertEquals(ASSAY_ACCESSION, getAssayAccession(ID));
  }

  /**
   * Tests getting a reported ID.
   */
  @Test
  public void testGetReportedId() {
    assertEquals(REPORTED_ID, getReportedId(ID));
  }

  /**
   * Tests getting a project accession.
   */
  @Test
  public void testGetProteinAccession() {
    assertEquals(PROTEIN_ACCESSION, getProteinAccession(ID));
  }

  /**
   * Tests getting a peptide sequence.
   */
  @Test
  public void testGetPeptideSequence() {
    assertEquals(PEP_SEQUENCE, getPeptideSequence(ID));
  }
}
