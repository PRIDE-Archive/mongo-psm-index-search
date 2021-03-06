package uk.ac.ebi.pride.psmindex.mongo.search.indexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.psmindex.mongo.search.model.MongoPsm;
import uk.ac.ebi.pride.psmindex.mongo.search.service.MongoPsmIndexService;
import uk.ac.ebi.pride.psmindex.mongo.search.service.MongoPsmSearchService;
import uk.ac.ebi.pride.psmindex.mongo.search.util.MongoPsmMzTabBuilder;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

/**
 * Indexes a project's PSMs in Mongo.
 */
public class MongoProjectPsmIndexer {

  private static Logger logger = LoggerFactory.getLogger(MongoProjectPsmIndexer.class.getName());

  @Resource
  private MongoPsmIndexService mongoPsmIndexService;
  @Resource
  private MongoPsmSearchService mongoPsmSearchService;

  /**
   * Constructor, sets the indexing service.
   * @param mongoPsmIndexService the indexing service
   */
  public MongoProjectPsmIndexer(MongoPsmIndexService mongoPsmIndexService, MongoPsmSearchService mongoPsmSearchService) {
    this.mongoPsmIndexService = mongoPsmIndexService;
    this.mongoPsmSearchService = mongoPsmSearchService;
  }

  /**
   * Indexes all PSMs for all assay for a project
   * @param projectAccession the project's accession number to index
   * @param assayAccession the project's assay accession number to index
   * @param mzTabFile the mzTab file tom index
   */
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
    logger.info("(MongoDB) Found " + psms.size() + " PSMs for project: " + projectAccession + " and assay: " + assayAccession
      + " in " + ChronoUnit.SECONDS.between(startTime,Instant.now()) +  " seconds");
    startTime = Instant.now();
    mongoPsmIndexService.save(psms); // add all PSMs to index
    logger.info("(MongoDB) DONE indexing all PSMs for project :" + projectAccession + " assay: " + assayAccession
      + " in " + ChronoUnit.SECONDS.between(startTime,Instant.now()) + " seconds");
  }

  /**
   * Deletes all PSMs for a project.
   * @param projectAccession the project's accession number to delete PSMs
   */
  public void deleteAllPsmsForProject(String projectAccession) {
	logger.info("Starting to delete PSMs");
	int MAX_PAGE_SIZE = 1000;
    long psmCount = mongoPsmSearchService.countByProjectAccession(projectAccession);
    List<MongoPsm> initialPsmsFound;
	logger.info("Found " + psmCount + " psms to delete");
	if (0 < psmCount) {
      for (int i = 0; i < (psmCount / MAX_PAGE_SIZE) + 1; i++) {
        initialPsmsFound =
            mongoPsmSearchService
                .findByProjectAccession(projectAccession, new PageRequest(i, MAX_PAGE_SIZE))
                .getContent();
		logger.info("Deleting psm page: " + i + " of " + (psmCount / MAX_PAGE_SIZE));
        mongoPsmIndexService.delete(initialPsmsFound);
      }
	  logger.info("Finished deleting all psm pages.");
    }
  }
}
