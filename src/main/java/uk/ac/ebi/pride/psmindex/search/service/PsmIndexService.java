package uk.ac.ebi.pride.psmindex.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.pride.psmindex.search.model.Psm;
import uk.ac.ebi.pride.psmindex.search.service.repository.PsmRepository;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * Created by tobias on 08/03/2017.
 */
@Service
public class PsmIndexService {

  private static Logger logger = LoggerFactory.getLogger(PsmIndexService.class.getName());

  @Resource
  private PsmRepository psmRepository;

  public PsmIndexService() {
  }

  public void setPsmRepository(PsmRepository psmRepository) {
    this.psmRepository = psmRepository;
  }

  @Transactional
  public void save(Psm psm) {
    psmRepository.save(psm);
  }

  @Transactional
  public void save(Iterable<Psm> psms) {
    if (psms==null || !psms.iterator().hasNext())
      logger.debug("No PSMs to save");
    else {
      if (logger.isDebugEnabled()) {
        debugSavePsm(psms);
      }
      psmRepository.save(psms);
    }
  }

  private void debugSavePsm(Iterable<Psm> psms) {
    int i = 0;
    for (Psm psm : psms) {
      logger.debug("Saving PSM " + i + " with ID: " + psm.getId());
      logger.debug("Project accession: " + psm.getProjectAccession());
      logger.debug("Assay accession: " + psm.getAssayAccession());
      logger.debug("Peptide sequence: " + psm.getPeptideSequence());
      logger.debug("Spectrum id: " + psm.getSpectrumId());
      i++;
    }
  }

  @Transactional
  public void delete(Psm psm){
    psmRepository.delete(psm);
  }

  @Transactional
  public void delete(Iterable<Psm> psms){
    if (psms==null || !psms.iterator().hasNext())
      logger.info("No PSMs to delete");
    else {
      psmRepository.delete(psms);
    }
  }

  @Transactional
  public void deleteAll() {
    psmRepository.deleteAll();
  }

  @Transactional
  public void deleteByProjectAccession(String projectAccession) {
    //Possible improvement, retrieve the ids to be deleted instead of the objects
    psmRepository.delete(psmRepository.findByProjectAccession(projectAccession));
  }

  @Transactional
  public Iterable<Psm> save(Collection<Psm> psms) {
    return psmRepository.save(psms);
  }

}

