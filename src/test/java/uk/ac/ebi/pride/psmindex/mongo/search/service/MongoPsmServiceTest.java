package uk.ac.ebi.pride.psmindex.mongo.search.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QSort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;
import uk.ac.ebi.pride.indexutils.modifications.Modification;
import uk.ac.ebi.pride.psmindex.mongo.search.config.MongoTestConfiguration;
import uk.ac.ebi.pride.psmindex.mongo.search.model.MongoPsm;
import uk.ac.ebi.pride.psmindex.mongo.search.service.repository.MongoPsmRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/** Tests the PSM service. */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoTestConfiguration.class})
public class MongoPsmServiceTest {

  public static final long ZERO_DOCS = 0L;
  public static final long SINGLE_DOC = 1L;
  // PSM 1 test data
  private static final String PSM_1_ID = "TEST-PSM-ID1";
  private static final String PSM_1_REPORTED_ID = "TEST-PSM-REPORTED-ID1";
  private static final String PSM_1_SEQUENCE = "MLVEYTQNQKEVLSEKEKKLEEYK";
  private static final String PSM_1_SPECTRUM = "SPECTRUM-ID1";
  // PSM 2 test data
  private static final String PSM_2_ID = "TEST-PSM-ID2";
  private static final String PSM_2_REPORTED_ID = "TEST-PSM-REPORTED-ID2";
  private static final String PSM_2_SEQUENCE = "YSQPEDSLIPFFEITVPESQLTVSQFTLPK";
  private static final String PSM_2_SPECTRUM = "SPECTRUM-ID1";
  // PSM 3 test data
  private static final String PSM_3_ID = "TEST-PSM-ID3";
  private static final String PSM_3_REPORTED_ID = "TEST-PSM-REPORTED-ID3";
  private static final String PSM_3_SEQUENCE = "YSQPEDSLIPFFEITVPE";
  private static final String PSM_3_SPECTRUM = "SPECTRUM-ID3";
  private static final int NUM_TEST_PSMS = 3;
  private static final String PSM_ID_PREFIX = "TEST-PSM-ID";
  // Proteins
  private static final String PROTEIN_1_ACCESSION = "PROTEIN-1-ACCESSION";
  private static final String PROTEIN_2_ACCESSION = "PROTEIN-2-ACCESSION";
  private static final String PARTIAL_ACCESSION_WILDCARD = "PROTEIN-*";
  private static final String PARTIAL_ACCESSION_WILDCARD_END_1 = "*1-ACCESSION";
  private static final String PARTIAL_ACCESSION_WILDCARD_END_2 = "*2-ACCESSION";
  // Projects and assays
  private static final String PROJECT_1_ACCESSION = "PROJECT-1-ACCESSION";
  private static final String PROJECT_2_ACCESSION = "PROJECT-2-ACCESSION";
  private static final String ASSAY_1_ACCESSION = "ASSAY-1-ACCESSION";
  private static final String ASSAY_2_ACCESSION = "ASSAY-2-ACCESSION";
  // Modifications
  private static final Integer MOD_1_POS = 3;
  private static final Integer MOD_2_POS = 5;
  private static final String MOD_1_ACCESSION = "MOD:00696";
  private static final String MOD_2_ACCESSION = "MOD:00674";
  private static final String MOD_1_NAME = "phosphorylated residue";
  private static final String MOD_2_NAME = "amidated residue";
  private static final String MOD_1_SYNONYM = "phosphorylation";
  private static final String MOD_2_SYNONYM = "amidation";
  // Neutral Loss without mod
  private static final String NEUTRAL_LOSS_ACC = "MS:1001524";
  private static final String NEUTRAL_LOSS_NAME = "fragment neutral loss";
  private static final String NEUTRAL_LOSS_VAL = "63.998283";
  private static final String NEUTRAL_LOSS_POS = "7";
  // Neutral Loss with mod
  private static final String NEUTRAL_LOSS_MOD_POS = "3";
  // Sequences
  private static final String SEQUENCE_SUB = "IPFFEITVPE";
  private static final String SEQUENCE_LT_6 = "ITVPE";
  private static final String SEQUENCE_BT_100 =
      "MKLNPQQAPLYGDCVVTVLLAEEDKAEDDVVFYLVFLGSTLRHCTSTRKVSSDTLETIAP"
          + "GHDCCETVKVQLCASKEGLPVFVVAEEDFHFVQDEAYDAAQFLATSAGNQQALNFTRFLD";
  private List<MongoPsm> mongoPsms = new ArrayList<>();
  @Resource private MongoPsmIndexService mongoPsmIndexService;

  @Resource private MongoPsmSearchService mongoPsmSearchService;

  @Autowired private MongoPsmRepository mongoPsmRepository;

  /** Ensures all existing data are deleted, and inserts test data. */
  @Before
  public void setUp() {
    mongoPsmIndexService.setMongoPsmRepository(mongoPsmRepository);
    mongoPsmSearchService.setMongoPsmRepository(mongoPsmRepository);
    mongoPsmIndexService.deleteAll();
    insertTestData();
  }

  /** Deletes test data */
  @After
  public void tearDown() {
    mongoPsmIndexService.deleteByProjectAccession(PROJECT_1_ACCESSION);
    mongoPsmIndexService.delete(mongoPsms.get(0));
    mongoPsmIndexService.delete(mongoPsms);
    mongoPsmIndexService.deleteAll();
  }

  /** Inserts test data. */
  private void insertTestData() {
    addPsm(
        PSM_1_ID,
        PSM_1_REPORTED_ID,
        PSM_1_SEQUENCE,
        PSM_1_SPECTRUM,
        PROTEIN_1_ACCESSION,
        PROJECT_1_ACCESSION,
        ASSAY_1_ACCESSION);
    addPsm(
        PSM_2_ID,
        PSM_2_REPORTED_ID,
        PSM_2_SEQUENCE,
        PSM_2_SPECTRUM,
        PROTEIN_2_ACCESSION,
        PROJECT_2_ACCESSION,
        ASSAY_2_ACCESSION);
    addPsm(
        PSM_3_ID,
        PSM_3_REPORTED_ID,
        PSM_3_SEQUENCE,
        PSM_3_SPECTRUM,
        PROTEIN_2_ACCESSION,
        PROJECT_2_ACCESSION,
        ASSAY_2_ACCESSION);
  }

  /** Tests searching by ID. */
  @Test
  public void testSearchById() {
    MongoPsm psm1 = mongoPsmSearchService.findById(PSM_1_ID);
    Assert.assertNotNull(psm1);
    Assert.assertEquals(PSM_1_ID, psm1.getId());
    MongoPsm psm2 = mongoPsmSearchService.findById(PSM_2_ID);
    Assert.assertNotNull(psm2);
    Assert.assertEquals(PSM_2_ID, psm2.getId());
    MongoPsm psm3 = mongoPsmSearchService.findById(PSM_3_ID);
    Assert.assertNotNull(psm3);
    Assert.assertEquals(PSM_3_ID, psm3.getId());
    Assert.assertEquals(
        3,
        mongoPsmSearchService
            .findByIdIn(mongoPsms.stream().map(MongoPsm::getId).collect(Collectors.toList()))
            .size());
    Assert.assertEquals(
        3,
        mongoPsmSearchService
            .findByIdIn(
                mongoPsms.stream().map(MongoPsm::getId).collect(Collectors.toList()), new QSort())
            .size());
  }

  /** Tests counting by project accession. */
  @Test
  public void testCountByProjectAccession() {
    long totalFound = mongoPsmSearchService.countByProjectAccession(PROJECT_1_ACCESSION);
    Assert.assertEquals(1, totalFound);
    totalFound = mongoPsmSearchService.countByProjectAccession(PROJECT_2_ACCESSION);
    Assert.assertEquals(2, totalFound);
  }

  /**
   * Adds a PSM
   *
   * @param psmId the PSM ID
   * @param psmReportedId the PSM reported ID
   * @param psmSequence the PSM sequence
   * @param psmSpectrum the PSM spectrum
   * @param proteinAcccession the protein accession
   * @param projectAccession the project accession
   * @param assayAccession the assay accession
   */
  private void addPsm(
      String psmId,
      String psmReportedId,
      String psmSequence,
      String psmSpectrum,
      String proteinAcccession,
      String projectAccession,
      String assayAccession) {
    MongoPsm psm = new MongoPsm();
    psm.setId(psmId);
    psm.setReportedId(psmReportedId);
    psm.setPeptideSequence(psmSequence);
    psm.setSpectrumId(psmSpectrum);
    psm.setProteinAccession(proteinAcccession);
    psm.setProjectAccession(projectAccession);
    psm.setAssayAccession(assayAccession);
    Modification mod1 = new Modification();
    mod1.addPosition(MOD_1_POS, null);
    mod1.setAccession(MOD_1_ACCESSION);
    mod1.setName(MOD_1_NAME);
    Modification mod2 = new Modification();
    mod2.addPosition(MOD_2_POS, null);
    mod2.setAccession(MOD_2_ACCESSION);
    mod2.setName(MOD_2_NAME);
    List<ModificationProvider> modifications = new LinkedList<>();
    modifications.add(mod1);
    modifications.add(mod2);
    psm.setModifications(modifications);
    mongoPsmIndexService.save(psm);
    mongoPsms.add(psm);
  }
}
