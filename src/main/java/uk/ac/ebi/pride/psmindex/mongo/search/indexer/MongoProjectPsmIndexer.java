package uk.ac.ebi.pride.psmindex.mongo.search.indexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.psmindex.mongo.search.model.MongoPsm;
import uk.ac.ebi.pride.psmindex.mongo.search.service.MongoPsmIndexService;
import uk.ac.ebi.pride.psmindex.mongo.search.util.MongoPsmMzTabBuilder;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

public class MongoProjectPsmIndexer {

  private static Logger logger = LoggerFactory.getLogger(MongoProjectPsmIndexer.class.getName());

  @Resource
  private MongoPsmIndexService mongoPsmIndexService;

  public MongoProjectPsmIndexer(MongoPsmIndexService mongoPsmIndexService) {
    this.mongoPsmIndexService = mongoPsmIndexService;
  }

  public void indexAllPsmsForProjectAndAssay(String projectAccession, String assayAccession, MZTabFile mzTabFile){
    List<MongoPsm> psms = new LinkedList<>();
    Instant startTime = Instant.now();
    try {
      if (mzTabFile != null) {
        psms = MongoPsmMzTabBuilder.readPsmsFromMzTabFile(projectAccession, assayAccession, mzTabFile);
      }
    } catch (Exception e) {
      logger.error("Cannot get PSMs from project: " + projectAccession + " and assay: " + assayAccession, e);
    }
    Instant endTime = Instant.now();
    logger.info("Found " + psms.size() + " PSMs for project: " + projectAccession + " and assay: " + assayAccession
      + " in " + ChronoUnit.SECONDS.between(startTime,endTime) +  " seconds");
    startTime = Instant.now();
    mongoPsmIndexService.save(psms); // add all PSMs to index
    logger.debug("COMMITTED " + psms.size() + " PSMs from project:" + projectAccession + " assay: " + assayAccession);
    endTime = Instant.now();
    logger.info("DONE indexing all PSMs for project :" + projectAccession + " assay: " + assayAccession
      + " in " + ChronoUnit.SECONDS.between(startTime,endTime) + " seconds");
  }

  public void deleteAllPsmsForProject(String projectAccession) {
    mongoPsmIndexService.deleteByProjectAccession(projectAccession);
  }

}
