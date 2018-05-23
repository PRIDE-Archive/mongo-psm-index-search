package uk.ac.ebi.pride.psmindex.mongo.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import uk.ac.ebi.pride.psmindex.mongo.search.model.MongoPsm;
import uk.ac.ebi.pride.psmindex.mongo.search.service.repository.MongoPsmRepository;

import javax.annotation.Resource;
import java.util.Collection;

/** Indexes PSMs into Mongo. */
@Service
public class MongoPsmIndexService {

  private static Logger logger = LoggerFactory.getLogger(MongoPsmIndexService.class.getName());

  @Resource private MongoPsmRepository mongoPsmRepository;

  /** Initializes the service. */
  public MongoPsmIndexService() {}

  /**
   * Sets the Mongo repository.
   *
   * @param mongoPsmRepository the Mongo PSM repository.
   */
  @SuppressWarnings("WeakerAccess")
  public void setMongoPsmRepository(MongoPsmRepository mongoPsmRepository) {
    this.mongoPsmRepository = mongoPsmRepository;
  }

  /**
   * Saves a PSM to Mongo.
   *
   * @param psm the PSM to save.
   */
  @Transactional
  public void save(MongoPsm psm) {
    mongoPsmRepository.save(psm);
  }

  /**
   * Saves PSMs to Mongo.
   *
   * @param psms the PSMs to save.
   */
  @Transactional
  public void save(Collection<MongoPsm> psms) {
    if (CollectionUtils.isEmpty(psms)) logger.debug("No PSMs to save");
    else {
      if (logger.isDebugEnabled()) {
        debugSavePsm(psms);
      }
      mongoPsmRepository.saveAll(psms);
    }
  }

  /**
   * Output debug information related to PSMs.
   *
   * @param psms PSMs to debug
   */
  private void debugSavePsm(Collection<MongoPsm> psms) {
    int i = 0;
    for (MongoPsm psm : psms) {
      logger.debug("Saving PSM " + i + " with ID: " + psm.getId());
      logger.debug("Project accession: " + psm.getProjectAccession());
      logger.debug("Assay accession: " + psm.getAssayAccession());
      logger.debug("Peptide sequence: " + psm.getPeptideSequence());
      logger.debug("Spectrum id: " + psm.getSpectrumId());
      i++;
    }
  }

  /**
   * Deletes a PSM from Mongo.
   *
   * @param psm the PSM to delete
   */
  @Transactional
  public void delete(MongoPsm psm) {
    mongoPsmRepository.delete(psm);
  }

  /**
   * Deletes PSMs from Mongo.
   *
   * @param psms the PSMs to delete
   */
  @Transactional
  public void delete(Collection<MongoPsm> psms) {
    if (CollectionUtils.isEmpty(psms)) logger.info("No PSMs to delete");
    else {
      mongoPsmRepository.deleteAll(psms);
    }
  }

  /** Deletes all PSMs in Mongo. */
  @Transactional
  public void deleteAll() {
    mongoPsmRepository.deleteAll();
  }

  /**
   * Deletes all PSMs in Mongo for a project.
   *
   * @param projectAccession the project's accession number to delete PSMs
   */
  @Transactional
  public void deleteByProjectAccession(String projectAccession) {
    mongoPsmRepository.deleteAll(mongoPsmRepository.findByProjectAccession(projectAccession));
  }
}
