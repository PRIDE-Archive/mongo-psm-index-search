package uk.ac.ebi.pride.psmindex.mongo.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import uk.ac.ebi.pride.psmindex.mongo.search.model.MongoPsm;
import uk.ac.ebi.pride.psmindex.mongo.search.service.repository.MongoPsmRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tobias on 08/03/2017.
 */
@Service
public class MongoPsmSearchService {

  @Resource
  private MongoPsmRepository mongoPsmRepository;

  @Autowired
  private MongoOperations mongoOperations;

  public MongoPsmSearchService() {
  }

  public void setMongoPsmRepository(MongoPsmRepository mongoPsmRepository) {
    this.mongoPsmRepository = mongoPsmRepository;
  }

  public MongoPsm findById(String id) {
    return mongoPsmRepository.findOne(id);
  }

  public List<MongoPsm> findByIdIn(Collection<String> ids) {
    return mongoPsmRepository.findByIdIn(ids);
  }

  public List<MongoPsm> findByIdIn(Collection<String> ids, Sort sort) {
    return mongoPsmRepository.findByIdIn(ids, sort);
  }

  public List<MongoPsm> findByProjectAccession(String projectAccession) {
    return mongoPsmRepository.findByProjectAccession(projectAccession);
  }


}