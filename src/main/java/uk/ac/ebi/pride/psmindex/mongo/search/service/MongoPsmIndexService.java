package uk.ac.ebi.pride.psmindex.mongo.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.pride.psmindex.mongo.search.model.MongoPsm;
import uk.ac.ebi.pride.psmindex.mongo.search.service.repository.MongoPsmRepository;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * Indexes PSMs into Mongo.
 */
@Service
public class MongoPsmIndexService {

  private static Logger logger = LoggerFactory.getLogger(MongoPsmIndexService.class.getName());

  @Resource
  private MongoPsmRepository mongoPsmRepository;

  /**
   * Initializes the service.
   */
  public MongoPsmIndexService() {
  }

  /**
   * Sets the Mongo repository.
   * @param mongoPsmRepository the Mongo PSM repository.
   */
  public void setMongoPsmRepository(MongoPsmRepository mongoPsmRepository) {
    this.mongoPsmRepository = mongoPsmRepository;
  }

  /**
   * Saves a PSM to Mongo.
   * @param psm the PSM to save.
   */
  @Transactional
  public void save(MongoPsm psm) {
    mongoPsmRepository.save(psm);
  }

  /**
   * Saves PSMs to Mongo.
   * @param psms the PSMs to save.
   */
  @Transactional
  public void save(Iterable<MongoPsm> psms) {
    if (psms==null || !psms.iterator().hasNext())
      logger.debug("No PSMs to save");
    else {
      if (logger.isDebugEnabled()) {
        debugSavePsm(psms);
      }
      mongoPsmRepository.save(psms);
    }
  }

  /**
   * Output debug information related to PSMs.
   * @param psms PSMs to debug
   */
  private void debugSavePsm(Iterable<MongoPsm> psms) {
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
   * @param psm the PSM to delete
   */
  @Transactional
  public void delete(MongoPsm psm){
    mongoPsmRepository.delete(psm);
  }

  /**
   * Deletes PSMs from Mongo.
   * @param psms the PSMs to delete
   */
  @Transactional
  public void delete(Iterable<MongoPsm> psms){
    if (psms==null || !psms.iterator().hasNext())
      logger.info("No PSMs to delete");
    else {
      mongoPsmRepository.delete(psms);
    }
  }

  /**
   * Deletes all PSMs in Mongo.
   */
  @Transactional
  public void deleteAll() {
    mongoPsmRepository.deleteAll();
  }

  /**
   * Saves PSMs to Mongo.
   * @param psms the PSMs to save
   * @return PSMs that were successfully saved.
   */
  @Transactional
  public Iterable<MongoPsm> save(Collection<MongoPsm> psms) {
    return mongoPsmRepository.save(psms);
  }

}

