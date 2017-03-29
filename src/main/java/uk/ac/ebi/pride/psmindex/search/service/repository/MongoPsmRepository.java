package uk.ac.ebi.pride.psmindex.search.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.pride.psmindex.search.model.MongoPsm;

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

    // Project accession query methods
  List<MongoPsm> findByProjectAccession(String projectAccession);
}
