package uk.ac.ebi.pride.psmindex.mongo.search.util;

/** This class builds a PSM ID String. */
public class MongoPsmIdBuilder {

  public static final String SEPARATOR = "_";

  /**
   * This class combines relevant ID together to produce a spectrum ID.
   *
   * @param projectAccession the project accession
   * @param assayAccession the assay accession
   * @param reportedId the reported ID
   * @param proteinAccession the protein accession
   * @param peptideSequence the sequence of the peptide
   * @return the spectrum ID
   */
  public static String getId(
      String projectAccession,
      String assayAccession,
      String reportedId,
      String proteinAccession,
      String peptideSequence) {
    return projectAccession
        + SEPARATOR
        + assayAccession
        + SEPARATOR
        + reportedId
        + SEPARATOR
        + proteinAccession
        + SEPARATOR
        + peptideSequence;
  }

  public static String getProjectAccession(String id) {
    return id.split(SEPARATOR)[0];
  }

  public static String getAssayAccession(String id) {
    return id.split(SEPARATOR)[1];
  }

  public static String getReportedId(String id) {
    return id.split(SEPARATOR)[2];
  }

  public static String getProteinAccession(String id) {
    return id.split(SEPARATOR)[3];
  }

  public static String getPeptideSequence(String id) {
    return id.split(SEPARATOR)[4];
  }
}
