package uk.ac.ebi.pride.psmindex.search.indexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.psmindex.search.model.Psm;
import uk.ac.ebi.pride.psmindex.search.service.PsmIndexService;
import uk.ac.ebi.pride.psmindex.search.util.PsmMzTabBuilder;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

public class ProjectPsmIndexer {

  private static Logger logger = LoggerFactory.getLogger(ProjectPsmIndexer.class.getName());

  @Resource
  private PsmIndexService psmIndexService;


  public ProjectPsmIndexer(PsmIndexService psmIndexService) {
    this.psmIndexService = psmIndexService;
  }

  public void indexAllPsmsForProjectAndAssay(String projectAccession, String assayAccession, MZTabFile mzTabFile){
    List<Psm> psms = new LinkedList<>();
    Instant startTime = Instant.now();
    try {
      if (mzTabFile != null) {
        psms = PsmMzTabBuilder.readPsmsFromMzTabFile(projectAccession, assayAccession, mzTabFile);
      }
    } catch (Exception e) {
      logger.error("Cannot get PSMs from project: " + projectAccession + " and assay: " + assayAccession, e);
    }
    Instant endTime = Instant.now();
    logger.info("Found " + psms.size() + " PSMs for project: " + projectAccession + " and assay: " + assayAccession
      + " in " + ChronoUnit.SECONDS.between(startTime,endTime) +  "seconds");
    startTime = Instant.now();
    psmIndexService.save(psms); // add all PSMs to index
    logger.debug("COMMITTED " + psms.size() + " PSMs from project:" + projectAccession + " assay: " + assayAccession);
    endTime = Instant.now();
    logger.info("DONE indexing all PSMs for project " + projectAccession
      + " in " + ChronoUnit.SECONDS.between(startTime,endTime) + " seconds");
  }

  public void deleteAllPsmsForProject(String projectAccession) {
    psmIndexService.deleteByProjectAccession(projectAccession);
  }

}
