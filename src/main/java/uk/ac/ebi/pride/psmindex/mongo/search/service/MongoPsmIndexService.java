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
 * Created by tobias on 08/03/2017.
 */
@Service
public class MongoPsmIndexService {

  private static Logger logger = LoggerFactory.getLogger(MongoPsmIndexService.class.getName());

  @Resource
  private MongoPsmRepository mongoPsmRepository;

  public MongoPsmIndexService() {
  }

  public void setMongoPsmRepository(MongoPsmRepository mongoPsmRepository) {
    this.mongoPsmRepository = mongoPsmRepository;
  }

  @Transactional
  public void save(MongoPsm psm) {
    mongoPsmRepository.save(psm);
  }

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

  @Transactional
  public void delete(MongoPsm psm){
    mongoPsmRepository.delete(psm);
  }

  @Transactional
  public void delete(Iterable<MongoPsm> psms){
    if (psms==null || !psms.iterator().hasNext())
      logger.info("No PSMs to delete");
    else {
      mongoPsmRepository.delete(psms);
    }
  }

  @Transactional
  public void deleteAll() {
    mongoPsmRepository.deleteAll();
  }

  @Transactional
  public void deleteByProjectAccession(String projectAccession) {
    //Possible improvement, retrieve the ids to be deleted instead of the objects
    mongoPsmRepository.delete(mongoPsmRepository.findByProjectAccession(projectAccession));
  }

  @Transactional
  public Iterable<MongoPsm> save(Collection<MongoPsm> psms) {
    return mongoPsmRepository.save(psms);
  }

}

