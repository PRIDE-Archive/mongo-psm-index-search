package uk.ac.ebi.pride.psmindex.mongo.search.util;

import org.junit.Assert;
import org.junit.Test;


/** Tests building PSM IDs. */
public class MongoPsmIdBuilderTest {

  private static final String PROJECT_ACCESSION = "PXT000111";
  private static final String ASSAY_ACCESSION = "12345";
  private static final String PROTEIN_ACCESSION = "P12345";
  private static final String REPORTED_ID = "12345";
  private static final String PEP_SEQUENCE = "ABCDEF";

  private static final String ID =
      PROJECT_ACCESSION
          + MongoPsmIdBuilder.SEPARATOR
          + ASSAY_ACCESSION
          + MongoPsmIdBuilder.SEPARATOR
          + REPORTED_ID
          + MongoPsmIdBuilder.SEPARATOR
          + PROTEIN_ACCESSION
          + MongoPsmIdBuilder.SEPARATOR
          + PEP_SEQUENCE;

  /** Tests getting an ID. */
  @Test
  public void testGetId() {
    String newID =
        MongoPsmIdBuilder.getId(PROJECT_ACCESSION, ASSAY_ACCESSION, REPORTED_ID, PROTEIN_ACCESSION, PEP_SEQUENCE);
    Assert.assertEquals(ID, newID);
  }

  /** Tests getting a project accession. */
  @Test
  public void testGetProjectAccession() {
    Assert.assertEquals(PROJECT_ACCESSION, MongoPsmIdBuilder.getProjectAccession(ID));
  }

  /** Tests getting an assay accession. */
  @Test
  public void testGetAssayAccession() {
    Assert.assertEquals(ASSAY_ACCESSION, MongoPsmIdBuilder.getAssayAccession(ID));
  }

  /** Tests getting a reported ID. */
  @Test
  public void testGetReportedId() {
    Assert.assertEquals(REPORTED_ID, MongoPsmIdBuilder.getReportedId(ID));
  }

  /** Tests getting a project accession. */
  @Test
  public void testGetProteinAccession() {
    Assert.assertEquals(PROTEIN_ACCESSION, MongoPsmIdBuilder.getProteinAccession(ID));
  }

  /** Tests getting a peptide sequence. */
  @Test
  public void testGetPeptideSequence() {
    Assert.assertEquals(PEP_SEQUENCE, MongoPsmIdBuilder.getPeptideSequence(ID));
  }
}
