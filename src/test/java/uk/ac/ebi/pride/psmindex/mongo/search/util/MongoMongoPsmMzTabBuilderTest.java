package uk.ac.ebi.pride.psmindex.mongo.search.util;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;
import uk.ac.ebi.pride.archive.dataprovider.param.CvParamProvider;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.utils.MZTabFileParser;
import uk.ac.ebi.pride.psmindex.mongo.search.model.MongoPsm;

import java.io.File;
import java.util.*;

import static junit.framework.Assert.*;

/**
 * Tests building PSMs from mzTab files.
 */
public class MongoMongoPsmMzTabBuilderTest {

  private static Logger logger = LoggerFactory.getLogger(MongoMongoPsmMzTabBuilderTest.class);
  private static ErrorLogOutputStream errorLogOutputStream = new ErrorLogOutputStream(logger);

  private static final String PROJECT_1_ACCESSION = "PXD000581";
  private static final String PROJECT_2_ACCESSION = "TST000121";
  private static final String PROJECT_1_ASSAY_1 = "32411";
  private static final String PROJECT_1_ASSAY_2 = "32416";
  private static final String PROJECT_2_ASSAY_1 = "00001";
  private static final String MOD_ACCESSION = "MOD:01214";
  private static final String MOD_NAME = "iodoacetamide - site C";
  private static final String FILE_PRE = ";PRIDE_Exp_Complete_Ac_";
  private static final String FILE_POST = ".xml;spectrum=";
  private static MZTabFile mzTabFileP1A1;
  private static MZTabFile mzTabFileP1A2;
  private static MZTabFile mzTabFileP2A1;
  private static final int NUM_ASSAYS = 2;


  /**
   * Reads in mzTab files.
   * @throws Exception problems reading mzTab files
   */
  @BeforeClass
  public static void initialise() throws Exception {
    mzTabFileP1A1 = new MZTabFileParser(
        new File("src/test/resources/submissions/2014/01/PXD000581/generated/PRIDE_Exp_Complete_Ac_32411.mztab"),
        errorLogOutputStream).getMZTabFile();
    mzTabFileP1A2 = new MZTabFileParser(
        new File("src/test/resources/submissions/2014/01/PXD000581/generated/PRIDE_Exp_Complete_Ac_32416.mztab"),
        errorLogOutputStream).getMZTabFile();
    mzTabFileP2A1 = new MZTabFileParser(
        new File("src/test/resources/submissions/TST000121/generated/PRIDE_Exp_Complete_Ac_00001.mztab"),
        errorLogOutputStream).getMZTabFile();
  }

  /**
   * Tests reading PSMs from mzTab files.
   * @throws Exception problems reading mzTab files
   */
  @Test
  public void testReadPsmsFromMzTabFilesDirectory() throws Exception {
    Map<String, List<MongoPsm>> psms = new HashMap<>();
    psms.put(PROJECT_1_ASSAY_1, MongoPsmMzTabBuilder.readPsmsFromMzTabFile(PROJECT_1_ACCESSION, PROJECT_1_ASSAY_1, mzTabFileP1A1));
    psms.put(PROJECT_1_ASSAY_2, MongoPsmMzTabBuilder.readPsmsFromMzTabFile(PROJECT_1_ACCESSION, PROJECT_1_ASSAY_2, mzTabFileP1A2));
    assertTrue(psms.size() == NUM_ASSAYS);
    for (Map.Entry<String, List<MongoPsm>> stringLinkedListEntry : psms.entrySet()) {
      for (MongoPsm psm : stringLinkedListEntry.getValue()) {
        assertTrue(psm.getSpectrumId().startsWith(PROJECT_1_ACCESSION + FILE_PRE + stringLinkedListEntry.getKey() + FILE_POST));
      }
    }
  }

  /**
   * Tests reading PSMs from ab mztab file, including extra comparison checks.
   * @throws Exception problems reading mzTab files
   */
  @Test
  public void testReadPsmFromMzTabFileAndCompare() throws Exception {
    Map<String, List<MongoPsm>> psms = new HashMap<>();
    psms.put(PROJECT_2_ASSAY_1, MongoPsmMzTabBuilder.readPsmsFromMzTabFile(PROJECT_2_ACCESSION, PROJECT_2_ASSAY_1, mzTabFileP2A1));
    assertTrue(psms.size() == 1);
    MongoPsm firstPsm = psms.entrySet().iterator().next().getValue().get(0);
    assertEquals("TST000121_00001_175_orf19/5636_QSTSSTPCPYWDTGCLCVMPQFAGAVGNCVAK", firstPsm.getId());
    assertEquals("175", firstPsm.getReportedId());
    assertEquals("TST000121;result_1_sample_1_dat.pride.xml;spectrum=175", firstPsm.getSpectrumId());
    assertEquals("orf19/5636", firstPsm.getProteinAccession());
    assertNull(firstPsm.isUnique());
    checkSearchEnginesScores(firstPsm.getSearchEngineScores());
    checkSearchEngine(firstPsm.getSearchEngines());
    checkModifications(firstPsm.getModifications());
    assertNull(firstPsm.getRetentionTime());
    assertNull(firstPsm.getCharge());
    assertEquals(1183.8615, firstPsm.getExpMassToCharge());
    assertNull(firstPsm.getCalculatedMassToCharge());
    assertNull(firstPsm.getPreAminoAcid());
    assertNull(firstPsm.getPostAminoAcid());
    assertEquals((Integer) 61, firstPsm.getStartPosition());
    assertEquals((Integer) 92, firstPsm.getEndPosition());
  }

  /**
   * Chceks modifications are correct.
   * @param modifications modifications to check
   */
  private void checkModifications(Iterable<ModificationProvider> modifications) {
    Iterator<ModificationProvider> iterator = modifications.iterator();
    ModificationProvider mod = iterator.next();
    assertEquals(MOD_ACCESSION, mod.getAccession());
    assertEquals(MOD_NAME, mod.getName());
    assertEquals((Integer) 8, mod.getMainPosition());
    mod = iterator.next();
    assertEquals(MOD_ACCESSION, mod.getAccession());
    assertEquals(MOD_NAME, mod.getName());
    assertEquals((Integer) 15, mod.getMainPosition());
    mod = iterator.next();
    assertEquals(MOD_ACCESSION, mod.getAccession());
    assertEquals(MOD_NAME, mod.getName());
    assertEquals((Integer) 17, mod.getMainPosition());
    mod = iterator.next();
    assertEquals(MOD_ACCESSION, mod.getAccession());
    assertEquals(MOD_NAME, mod.getName());
    assertEquals((Integer) 29, mod.getMainPosition());
  }

  /**
   * Checks search enginges are correct.
   * @param searchEngines  search enginges to check
   */
  private void checkSearchEngine(Iterable<CvParamProvider> searchEngines) {
    CvParamProvider cvParamProvider = searchEngines.iterator().next();
    assertEquals("MS", cvParamProvider.getCvLabel());
    assertEquals("MS:1001207", cvParamProvider.getAccession());
    assertEquals("Mascot", cvParamProvider.getName());
    assertEquals(null, cvParamProvider.getValue());
  }

  /**
   * Check search engine scores are correct.
   * @param searchEngineScores search engine scores to check.
   */
  private void checkSearchEnginesScores(Iterable<CvParamProvider> searchEngineScores) {
    CvParamProvider cvParamProvider = searchEngineScores.iterator().next();
    assertEquals("PRIDE", cvParamProvider.getCvLabel());
    assertEquals("PRIDE:0000069", cvParamProvider.getAccession());
    assertEquals("Mascot score", cvParamProvider.getName());
    assertEquals("91.24", cvParamProvider.getValue());
  }
}
