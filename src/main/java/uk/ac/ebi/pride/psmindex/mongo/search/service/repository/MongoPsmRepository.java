package uk.ac.ebi.pride.psmindex.mongo.search.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.pride.psmindex.mongo.search.model.MongoPsm;

import java.util.Collection;
import java.util.List;

/** The Mongo PSM respository. */
@Repository
public interface MongoPsmRepository extends MongoRepository<MongoPsm, String> {

  // Project accession methods
  /**
   * Finds a list of PSMs in a collection of IDs.
   *
   * @param ids a collection of ID to search for
   * @return a list of PSMs corresponding to the provided ID.
   */
  List<MongoPsm> findByIdIn(Collection<String> ids);

  /**
   * A sorted list of PSMs in a collection of IDs.
   *
   * @param ids a collection of ID to search for
   * @param sort how the result should be sorted
   * @return a list of PSMs corresponding to the provided IDs.
   */
  List<MongoPsm> findByIdIn(Collection<String> ids, Sort sort);

  // Project accession query methods
  /**
   * Finds a PSM by a project accession.
   *
   * @param projectAccession the project accession to search for
   * @return a list of PSMs corresponding to the provided project accession
   */
  List<MongoPsm> findByProjectAccession(String projectAccession);

  /**
   * Finds a page of PSMs by a project accession and pageable.
   *
   * @param projectAccession the project accession to search for
   * @param pageable the page to request for
   * @return a list of PSMs corresponding to the provided project accession
   */
  Page<MongoPsm> findByProjectAccession(String projectAccession, Pageable pageable);

  /**
   * Finds a page of PSMs by an assay accession and pageable.
   *
   * @param assayAcession the project accession to search for
   * @param pageable the page to request for
   * @return a list of PSMs corresponding to the provided project accession
   */
  Page<MongoPsm> findByAssayAccession(String assayAcession, Pageable pageable);

  /**
   * Counts how many PSMs are for a project accession.
   *
   * @param projectAccession the project accession to search for
   * @return the number of PSMs corresponding to the provided project accession
   */
  long countByProjectAccession(String projectAccession);

  /**
   * Counts how many PSMs are for an assay accession
   *
   * @param assayAccession the assay accession
   * @return the number of PSMs corresponding to the provided project accession
   */
  long countByAssayAccession(String assayAccession);
}
