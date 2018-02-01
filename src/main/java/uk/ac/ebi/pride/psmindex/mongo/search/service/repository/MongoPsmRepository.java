package uk.ac.ebi.pride.psmindex.mongo.search.service.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.pride.psmindex.mongo.search.model.MongoPsm;

import java.util.Collection;
import java.util.List;

/**
 * The Mongo PSM respository.
 */
@Repository
public interface MongoPsmRepository extends MongoRepository<MongoPsm, String> {

  // Project accession methods
  /**
   * Finds a PSM by an ID.
   * @param id the ID to search for
   * @return a list of PSMs corresponding to the provided ID.
   */
  List<MongoPsm> findById(String id);

  /**
   * Finds a list of PSMs in a collection of IDs.
   * @param ids a collection of ID to search for
   * @return a list of PSMs corresponding to the provided ID.
   */
  List<MongoPsm> findByIdIn(Collection<String> ids);

  /**
   * A sorted list of PSMs in a collection of IDs.
   * @param ids a collection of ID to search for
   * @param sort how the result should be sorted
   * @return a list of PSMs corresponding to the provided IDs.
   */
  List<MongoPsm> findByIdIn(Collection<String> ids, Sort sort);

  // Project accession query methods
  /**
   * Finds a PSM by a project accession.
   * @param projectAccession the project accession to search for
   * @return  a list of PSMs corresponding to the provided project accession
   */
  List<MongoPsm> findByProjectAccession(String projectAccession);

  /**
   * Counts how many PSMs are for a project accession.
   * @param projectAccession the project accession to search for
   * @return  the number of PSMs corresponding to the provided project accession
   */
  long countByProjectAccession(String projectAccession);
}
