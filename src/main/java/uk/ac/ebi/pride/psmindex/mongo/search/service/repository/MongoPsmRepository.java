package uk.ac.ebi.pride.psmindex.mongo.search.service.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.pride.psmindex.mongo.search.model.MongoPsm;

import java.util.Collection;
import java.util.List;

/**
 * Created by tobias on 08/03/2017.
 */
@Repository
public interface MongoPsmRepository extends MongoRepository<MongoPsm, String> {

  // Project accession methods
  List<MongoPsm> findById(String id);
  List<MongoPsm> findByIdIn(Collection<String> id);
  List<MongoPsm> findByIdIn(Collection<String> id, Sort sort);

    // Project accession query methods
  List<MongoPsm> findByProjectAccession(String projectAccession);

  long countByProjectAccession(String projectAccession);
}
