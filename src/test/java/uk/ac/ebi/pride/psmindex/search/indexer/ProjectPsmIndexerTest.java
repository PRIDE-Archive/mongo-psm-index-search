package uk.ac.ebi.pride.psmindex.search.indexer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;
import uk.ac.ebi.pride.indexutils.modifications.Modification;
import uk.ac.ebi.pride.indexutils.params.CvParam;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.utils.MZTabFileParser;
import uk.ac.ebi.pride.psmindex.search.model.Psm;
import uk.ac.ebi.pride.psmindex.search.service.PsmIndexService;
import uk.ac.ebi.pride.psmindex.search.service.PsmSearchService;
import uk.ac.ebi.pride.psmindex.search.util.ErrorLogOutputStream;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mongo-test-context.xml"})
public class ProjectPsmIndexerTest {
  private static Logger logger = LoggerFactory.getLogger(ProjectPsmIndexerTest.class);
  private static ErrorLogOutputStream errorLogOutputStream = new ErrorLogOutputStream(logger);

  private static final String PROJECT_1_ACCESSION = "PXD000581";
  private static final String PROJECT_2_ACCESSION = "TST000121";
  private static final String PROJECT_1_ASSAY_1 = "32411";
  private static final String PROJECT_1_ASSAY_2 = "32416";
  private static final String PROJECT_2_ASSAY_1 = "00001";
  private static final int NUM_PSMS_PROJECT_1 = 4;
  private static final int NUM_PSMS_P1A1 = 3;
  private static final int NUM_PSMS_P1A2 = 1;
  private static final int NUM_PSMS_PROJECT_2 = 7;
  private static final int NUM_PSMS_P2A1 = 7;
  private static final String PSM3_ID = "3";
  private static MZTabFile mzTabFileP1A1;
  private static MZTabFile mzTabFileP1A2;
  private static MZTabFile mzTabFileP2A1;
  private static final String PSM_3_ID = "TEST-PSM-ID3";
  private static final String PSM_3_REPORTED_ID = "TEST-PSM-REPORTED-ID3";
  private static final String PSM_3_SEQUENCE = "YSQPEDSLIPFFEITVPE";
  private static final String PSM_3_SPECTRUM = "SPECTRUM-ID3";
  private static final String PROTEIN_2_ACCESSION = "PROTEIN-2-ACCESSION";
  private static final String ASSAY_2_ACCESSION = "ASSAY-2-ACCESSION";
  private static final Integer MOD_1_POS = 3;
  private static final Integer MOD_2_POS = 5;
  private static final String MOD_1_ACCESSION = "MOD:00696";
  private static final String MOD_2_ACCESSION = "MOD:00674";
  private static final String MOD_1_NAME = "phosphorylated residue";
  private static final String MOD_2_NAME = "amidated residue";
  private static final String NEUTRAL_LOSS_ONT = "MS";
  private static final String NEUTRAL_LOSS_ACC = "MS:1001524";
  private static final String NEUTRAL_LOSS_NAME = "fragment neutral loss";
  private static final String NEUTRAL_LOSS_VAL = "63.998283";
  private static final Integer NEUTRAL_LOSS_POS = 7;

  private ProjectPsmIndexer projectPsmIndexer;

  @Resource
  private PsmIndexService psmIndexService;

  @Resource
  private PsmSearchService psmSearchService;

  @Before
  public void setup() throws Exception {
    projectPsmIndexer = new ProjectPsmIndexer(psmIndexService);
    deleteAllData();
    insertTestData();
  }

  private void deleteAllData() {
    psmIndexService.deleteAll();
  }

  private void insertTestData() throws IOException {
    mzTabFileP1A1 = new MZTabFileParser(
        new File("src/test/resources/submissions/2014/01/PXD000581/generated/PRIDE_Exp_Complete_Ac_32411.mztab"),
        errorLogOutputStream).getMZTabFile();
    mzTabFileP1A2 = new MZTabFileParser(
        new File("src/test/resources/submissions/2014/01/PXD000581/generated/PRIDE_Exp_Complete_Ac_32416.mztab"),
        errorLogOutputStream).getMZTabFile();
    mzTabFileP2A1 = new MZTabFileParser(
        new File("src/test/resources/submissions/TST000121/generated/PRIDE_Exp_Complete_Ac_00001.mztab"),
        errorLogOutputStream).getMZTabFile();
    projectPsmIndexer.indexAllPsmsForProjectAndAssay(PROJECT_1_ACCESSION, PROJECT_1_ASSAY_1, mzTabFileP1A1);
    projectPsmIndexer.indexAllPsmsForProjectAndAssay(PROJECT_1_ACCESSION, PROJECT_1_ASSAY_2, mzTabFileP1A2);
    projectPsmIndexer.indexAllPsmsForProjectAndAssay(PROJECT_2_ACCESSION, PROJECT_2_ASSAY_1, mzTabFileP2A1);
  }

  @After
  public void tearDown() throws Exception {
    projectPsmIndexer.deleteAllPsmsForProject(PROJECT_1_ACCESSION);
    projectPsmIndexer.deleteAllPsmsForProject(PROJECT_2_ACCESSION);
    List<Psm> psms = psmSearchService.findByAssayAccession(PROJECT_1_ASSAY_1);
    assertEquals(0, psms.size());
    psms = psmSearchService.findByAssayAccession(PROJECT_1_ASSAY_2);
    assertEquals(0, psms.size());
    psms = psmSearchService.findByProjectAccession(PROJECT_1_ACCESSION);
    assertEquals(0, psms.size());
    psms = psmSearchService.findByReportedId(PSM3_ID);
    assertEquals(0, psms.size());
  }

  @Test
  public void testIndexAllPsmsForProjectAndAssay() throws Exception {
    List<Psm> psms = psmSearchService.findByAssayAccession(PROJECT_1_ASSAY_1);
    assertEquals(NUM_PSMS_P1A1, psms.size());
    psms = psmSearchService.findByAssayAccession(PROJECT_1_ASSAY_2);
    assertEquals(NUM_PSMS_P1A2, psms.size());
    psms = psmSearchService.findByAssayAccession(PROJECT_2_ASSAY_1);
    assertEquals(NUM_PSMS_P2A1, psms.size());
    psms = psmSearchService.findByProjectAccession(PROJECT_1_ACCESSION);
    assertEquals(NUM_PSMS_PROJECT_1, psms.size());
    psms = psmSearchService.findByProjectAccession(PROJECT_2_ACCESSION);
    assertEquals(NUM_PSMS_PROJECT_2, psms.size());
  }

  @Test
  public void addPsmWithNeutralLoss() {
    Psm psm = new Psm();
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
    Modification mod3 = new Modification();
    mod3.addPosition(NEUTRAL_LOSS_POS, null);
    mod3.setNeutralLoss(new CvParam(NEUTRAL_LOSS_ONT, NEUTRAL_LOSS_ACC, NEUTRAL_LOSS_NAME, NEUTRAL_LOSS_VAL));
    List<ModificationProvider> modifications = new LinkedList<>();
    modifications.add(mod1);
    modifications.add(mod2);
    modifications.add(mod3);
    psm.setModifications(modifications);
    psmIndexService.save(psm);
  }
}
