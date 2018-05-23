package uk.ac.ebi.pride.psmindex.mongo.search.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.pride.psmindex.mongo.search.model.MongoPsm;
import uk.ac.ebi.pride.psmindex.mongo.search.service.repository.MongoPsmRepository;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/** A service to search for PSMs with Mongo. */
@Service
public class MongoPsmSearchService {

  @Resource private MongoPsmRepository mongoPsmRepository;

  /** Initializes the service. */
  public MongoPsmSearchService() {}

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
   * Finds a PSM by an ID.
   *
   * @param id the ID to search for
   * @return a PSM corresponding to the provided ID.
   */
  @SuppressWarnings("WeakerAccess")
  public MongoPsm findById(String id) {
    return mongoPsmRepository.findById(id).orElse(new MongoPsm());
  }

  /**
   * Finds a list of PSMs in a collection of IDs.
   *
   * @param ids a collection of ID to search for
   * @param pageable the page to request for
   * @return a list of PSMs corresponding to the provided IDs.
   */
  @SuppressWarnings("WeakerAccess")
  public List<MongoPsm> findByIdIn(Collection<String> ids, Pageable pageable) {
    return mongoPsmRepository.findByIdIn(ids, pageable);
  }

  /**
   * Finds a PSM by a project accession.
   *
   * @param projectAccession the project accession to search for
   * @return a list of PSMs corresponding to the provided project accession
   */
  public List<MongoPsm> findByProjectAccession(String projectAccession) {
    return mongoPsmRepository.findByProjectAccession(projectAccession);
  }

  /**
   * Finds a page of PSMs by a project accession and pageable.
   *
   * @param projectAccession the project accession to search for
   * @param pageable the page to request for
   * @return a list of PSMs corresponding to the provided project accession
   */
  public Page<MongoPsm> findByProjectAccession(String projectAccession, Pageable pageable) {
    return mongoPsmRepository.findByProjectAccession(projectAccession, pageable);
  }

  /**
   * Finds a page of PSMs by an assay accession and pageable.
   *
   * @param assayAccession the project accession to search for
   * @param pageable the page to request for
   * @return a list of PSMs corresponding to the provided project accession
   */
  public Page<MongoPsm> findByAssayAccession(String assayAccession, Pageable pageable) {
    return mongoPsmRepository.findByAssayAccession(assayAccession, pageable);
  }

  /**
   * Counts how many PSMs are for a project accession.
   *
   * @param projectAccession the project accession to search for
   * @return the number of PSMs corresponding to the provided project accession
   */
  public long countByProjectAccession(String projectAccession) {
    return mongoPsmRepository.countByProjectAccession(projectAccession);
  }

  /**
   * Counts how many PSMs are for a assay accession and assay accession.
   *
   * @param assayAccession the assay accession to search for
   * @return the number of PSMs corresponding to the provided project accession
   */
  public long countByAssayAccession(String assayAccession) {
    return mongoPsmRepository.countByAssayAccession(assayAccession);
  }
}
