package uk.ac.ebi.pride.psmindex.mongo.search.service;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;
import uk.ac.ebi.pride.indexutils.modifications.Modification;
import uk.ac.ebi.pride.psmindex.mongo.search.model.MongoPsm;

import javax.annotation.Resource;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-mongo-test-context.xml")
public class MongoPsmServiceTest {

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

  //Proteins
  private static final String PROTEIN_1_ACCESSION = "PROTEIN-1-ACCESSION";
  private static final String PROTEIN_2_ACCESSION = "PROTEIN-2-ACCESSION";

  private static final String PARTIAL_ACCESSION_WILDCARD = "PROTEIN-*";
  private static final String PARTIAL_ACCESSION_WILDCARD_END_1 = "*1-ACCESSION";
  private static final String PARTIAL_ACCESSION_WILDCARD_END_2 = "*2-ACCESSION";

  //Projects and assays
  private static final String PROJECT_1_ACCESSION = "PROJECT-1-ACCESSION";
  private static final String PROJECT_2_ACCESSION = "PROJECT-2-ACCESSION";
  private static final String ASSAY_1_ACCESSION = "ASSAY-1-ACCESSION";
  private static final String ASSAY_2_ACCESSION = "ASSAY-2-ACCESSION";

  //Modifications
  private static final Integer MOD_1_POS = 3;
  private static final Integer MOD_2_POS = 5;
  private static final String MOD_1_ACCESSION = "MOD:00696";
  private static final String MOD_2_ACCESSION = "MOD:00674";

  private static final String MOD_1_NAME = "phosphorylated residue";
  private static final String MOD_2_NAME = "amidated residue";

  private static final String MOD_1_SYNONYM = "phosphorylation";
  private static final String MOD_2_SYNONYM = "amidation";

  //Neutral Loss without mod
  private static final String NEUTRAL_LOSS_ACC = "MS:1001524";
  private static final String NEUTRAL_LOSS_NAME = "fragment neutral loss";
  private static final String NEUTRAL_LOSS_VAL = "63.998283";
  private static final String NEUTRAL_LOSS_POS = "7";

  //Neutral Loss with mod
  private static final String NEUTRAL_LOSS_MOD_POS = "3";

  //Sequences
  private static final String SEQUENCE_SUB = "IPFFEITVPE";
  private static final String SEQUENCE_LT_6 = "ITVPE";
  private static final String SEQUENCE_BT_100 =
      "MKLNPQQAPLYGDCVVTVLLAEEDKAEDDVVFYLVFLGSTLRHCTSTRKVSSDTLETIAP" +
          "GHDCCETVKVQLCASKEGLPVFVVAEEDFHFVQDEAYDAAQFLATSAGNQQALNFTRFLD";

  public static final long ZERO_DOCS = 0L;
  public static final long SINGLE_DOC = 1L;

  @Resource
  private MongoPsmIndexService mongoPsmIndexService;

  @Resource
  private MongoPsmSearchService mongoPsmSearchService;

  @Before
  public void setUp() throws Exception {
    deleteAllData();
    insertTestData();
  }

  @After
  public void tearDown() throws Exception {
    deleteAllData();
  }

  private void deleteAllData() {
    mongoPsmIndexService.deleteAll();
  }

  private void insertTestData() {
    addPsm_1();
    addPsm_2();
    addPsm_3();
  }

  @Test
  public void testSearchById() {
    MongoPsm psm1= mongoPsmSearchService.findById(PSM_1_ID);
    assertNotNull(psm1);
    assertEquals(PSM_1_ID, psm1.getId());
    MongoPsm psm2 = mongoPsmSearchService.findById(PSM_2_ID);
    assertNotNull(psm2);
    assertEquals(PSM_2_ID, psm2.getId());
    MongoPsm psm3 = mongoPsmSearchService.findById(PSM_3_ID);
    assertNotNull(psm3);
    assertEquals(PSM_3_ID, psm3.getId());

    long totalFound = mongoPsmSearchService.countByProjectAccession(PROJECT_1_ACCESSION);
    assertEquals(1, totalFound);
    totalFound = mongoPsmSearchService.countByProjectAccession(PROJECT_2_ACCESSION);
    assertEquals(2, totalFound);
  }

  private void addPsm_1() {
    MongoPsm psm = new MongoPsm();
    psm.setId(PSM_1_ID);
    psm.setReportedId(PSM_1_REPORTED_ID);
    psm.setPeptideSequence(PSM_1_SEQUENCE);
    psm.setSpectrumId(PSM_1_SPECTRUM);
    psm.setProteinAccession(PROTEIN_1_ACCESSION);
    psm.setProjectAccession(PROJECT_1_ACCESSION);
    psm.setAssayAccession(ASSAY_1_ACCESSION);
    Modification mod1 = new Modification();
    mod1.addPosition(MOD_1_POS, null);
    mod1.setAccession(MOD_1_ACCESSION);
    mod1.setName(MOD_1_NAME);
    Modification mod2 = new Modification();
    mod2.addPosition(MOD_2_POS, null);
    mod2.setAccession(MOD_2_ACCESSION);
    List<ModificationProvider> modifications = new LinkedList<ModificationProvider>();
    modifications.add(mod1);
    modifications.add(mod2);
    mod2.setName(MOD_2_NAME);
    psm.setModifications(modifications);
    mongoPsmIndexService.save(psm);
  }

  private void addPsm_2() {
    MongoPsm psm = new MongoPsm();
    psm.setId(PSM_2_ID);
    psm.setReportedId(PSM_2_REPORTED_ID);
    psm.setPeptideSequence(PSM_2_SEQUENCE);
    psm.setSpectrumId(PSM_2_SPECTRUM);
    psm.setProteinAccession(PROTEIN_2_ACCESSION);
    psm.setProjectAccession(PROJECT_2_ACCESSION);
    psm.setAssayAccession(ASSAY_2_ACCESSION);
    Modification mod1 = new Modification();
    mod1.addPosition(MOD_1_POS, null);
    mod1.setAccession(MOD_1_ACCESSION);
    mod1.setName(MOD_1_NAME);
    Modification mod2 = new Modification();
    mod2.addPosition(MOD_2_POS, null);
    mod2.setAccession(MOD_2_ACCESSION);
    mod2.setName(MOD_2_NAME);
    List<ModificationProvider> modifications = new LinkedList<ModificationProvider>();
    modifications.add(mod1);
    modifications.add(mod2);
    psm.setModifications(modifications);
    mongoPsmIndexService.save(psm);
  }

  private void addPsm_3() {
    MongoPsm psm = new MongoPsm();
    psm.setId(PSM_3_ID);
    psm.setReportedId(PSM_3_REPORTED_ID);
    psm.setPeptideSequence(PSM_3_SEQUENCE);
    psm.setSpectrumId(PSM_3_SPECTRUM);
    psm.setProteinAccession(PROTEIN_2_ACCESSION);
    psm.setProjectAccession(PROJECT_2_ACCESSION);
    psm.setAssayAccession(ASSAY_2_ACCESSION);
    Modification mod1 = new Modification();
    mod1.addPosition(MOD_1_POS, null);
    mod1.setAccession(MOD_1_ACCESSION);
    mod1.setName(MOD_1_NAME);
    Modification mod2 = new Modification();
    mod2.addPosition(MOD_2_POS, null);
    mod2.setAccession(MOD_2_ACCESSION);
    mod2.setName(MOD_2_NAME);
    List<ModificationProvider> modifications = new LinkedList<ModificationProvider>();
    modifications.add(mod1);
    modifications.add(mod2);
    psm.setModifications(modifications);
    mongoPsmIndexService.save(psm);
  }
}
