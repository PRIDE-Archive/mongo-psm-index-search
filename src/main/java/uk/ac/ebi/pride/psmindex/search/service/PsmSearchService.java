package uk.ac.ebi.pride.psmindex.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import uk.ac.ebi.pride.psmindex.search.model.Psm;
import uk.ac.ebi.pride.psmindex.search.service.repository.PsmRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tobias on 08/03/2017.
 */
@Service
public class PsmSearchService {

  @Resource
  private PsmRepository psmRepository;

  @Autowired
  private MongoOperations mongoOperations;

  public PsmSearchService() {
  }

  public void setPsmRepository(PsmRepository psmRepository) {
    this.psmRepository = psmRepository;
  }

  public Psm findById(String id) {
    return psmRepository.findOne(id);
  }

  public List<Psm> findByIdIn(Collection<String> ids) {
    List<Psm> result = new ArrayList<>();
    ids.forEach(id -> result.add(psmRepository.findOne(id)));
    return result;
  }

  public List<Psm> findByPeptideSequence(String peptideSequence) {
    return psmRepository.findByPeptideSequence(peptideSequence);
  }
  public Page<Psm> findByPeptideSequence(String peptideSequence, Pageable pageable) {
    return psmRepository.findByPeptideSequence(peptideSequence, pageable);
  }

  public List<Psm> findPeptideSequencesByProjectAccession(String projectAccession) {
    return psmRepository.findPeptideSequencesByProjectAccession(projectAccession);
  }

  public Page<Psm> findByPeptideSequenceAndProjectAccession(String peptideSequence, String projectAccession, Pageable pageable) {
    return psmRepository.findByPeptideSequenceAndProjectAccession(peptideSequence, projectAccession, pageable);
  }

  public Page<Psm> findByPeptideSequenceAndAssayAccession(String peptideSequence, String assayAccession, Pageable pageable) {
    return  psmRepository.findByPeptideSequenceAndAssayAccession(peptideSequence, assayAccession, pageable);
  }

  public List<Psm> findByProjectAccession(String projectAccession) {
    return psmRepository.findByProjectAccession(projectAccession);
  }

  public  Page<Psm> findByProjectAccession(String projectAccession, Pageable pageable) {
    return psmRepository.findByProjectAccession(projectAccession, pageable);
  }

  public List<Psm> findByProjectAccessionIn(Collection<String> projectAccessions) {
    return psmRepository.findByProjectAccessionIn(projectAccessions);
  }

  public Page<Psm> findByProjectAccessionIn(Collection<String> projectAccessions, Pageable pageable) {
    return psmRepository.findByProjectAccessionIn(projectAccessions, pageable);
  }

  public Long countByProjectAccession(String projectAccession) {
    return psmRepository.countByProjectAccession(projectAccession);
  }

  public List<Psm> findByAssayAccession(String assayAccession) {
    return psmRepository.findByAssayAccession(assayAccession);
  }

  public Page<Psm> findByAssayAccession(String assayAccession, Pageable pageable) {
    return psmRepository.findByAssayAccession(assayAccession, pageable);
  }

  public List<Psm> findByAssayAccessionIn(Collection<String> assayAccessions) {
    return psmRepository.findByAssayAccessionIn(assayAccessions);
  }

  public Page<Psm> findByAssayAccessionIn(Collection<String> assayAccessions, Pageable pageable) {
    return psmRepository.findByAssayAccessionIn(assayAccessions, pageable);
  }

  public Long countByAssayAccession(String assayAccession) {
    return psmRepository.countByAssayAccession(assayAccession);
  }

  public List<Psm> findBySpectrumId(String spectrumId) {
    return psmRepository.findBySpectrumId(spectrumId);
  }
  public List<Psm> findBySpectrumIdIn(Collection<String> spectrumIds) {
    return psmRepository.findBySpectrumIdIn(spectrumIds);
  }

  public List<Psm> findByReportedId(String reportedId) {
    return psmRepository.findByReportedId(reportedId);
  }

  public List<Psm> findByReportedIdAndProjectAccession(String reportedId, String projectAccession) {
    return psmRepository.findByReportedIdAndProjectAccession(reportedId, projectAccession);
  }

  public List<Psm> findByReportedIdAndAssayAccession(String reportedId, String assayAccession) {
    return psmRepository.findByReportedIdAndAssayAccession(reportedId, assayAccession);
  }
  public List<Psm> findByReportedIdAndAssayAccessionAndProteinAccessionAndPeptideSequence(String reportedId, String assayAccession, String proteinAccession, String peptideSequence) {
    return psmRepository.findByReportedIdAndAssayAccessionAndProteinAccessionAndPeptideSequence(reportedId, assayAccession, proteinAccession, peptideSequence);
  }

  public List<Psm> findByProteinAccession(String proteinAccession) {
    return psmRepository.findByProteinAccession(proteinAccession);
  }

  public  List<Psm> findByProteinAccessionAndProjectAccession(String proteinAccession, String projectAccession) {
    return psmRepository.findByProteinAccessionAndProjectAccession(proteinAccession, projectAccession);
  }

  public List<Psm> findByProteinAccessionAndAssayAccession(String proteinAccession, String assayAccession) {
    return psmRepository.findByProteinAccessionAndAssayAccession(proteinAccession, assayAccession);
  }


}
