package uk.ac.ebi.pride.psmindex.search.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.pride.psmindex.search.model.Psm;

import java.util.Collection;
import java.util.List;

/**
 * Created by tobias on 08/03/2017.
 */
@Repository
public interface PsmRepository extends MongoRepository<Psm, String> {

  // Project accession methods
  List<Psm> findById(String id);
  List<Psm> findByIdIn(Collection<String> id);

  //Sequence query methods
  List<Psm> findByPeptideSequence(String peptideSequence);
  Page<Psm> findByPeptideSequence(String peptideSequence, Pageable pageable);
  List<Psm> findPeptideSequencesByProjectAccession(String projectAccession);
  Page<Psm> findByPeptideSequenceAndProjectAccession(String peptideSequence, String projectAccession, Pageable pageable);
  Page<Psm> findByPeptideSequenceAndAssayAccession(String peptideSequence, String assayAccession, Pageable pageable);

  // Project accession query methods
  List<Psm> findByProjectAccession(String projectAccession);
  Page<Psm> findByProjectAccession(String projectAccession, Pageable pageable);
  List<Psm> findByProjectAccessionIn(Collection<String> projectAccessions);
  Page<Psm> findByProjectAccessionIn(Collection<String> projectAccessions, Pageable pageable);
  Long countByProjectAccession(String projectAccession);

  // Assay accession query methods
  List<Psm> findByAssayAccession(String assayAccession);
  Page<Psm> findByAssayAccession(String assayAccession, Pageable pageable);
  List<Psm> findByAssayAccessionIn(Collection<String> assayAccessions);
  Page<Psm> findByAssayAccessionIn(Collection<String> assayAccessions, Pageable pageable);
  Long countByAssayAccession(String assayAccession);

  // Spectrum id query methods
  List<Psm> findBySpectrumId(String spectrumId);
  List<Psm> findBySpectrumIdIn(Collection<String> spectrumIds);

  // Reported id query methods
  List<Psm> findByReportedId(String reportedId);
  List<Psm> findByReportedIdAndProjectAccession(String reportedId, String projectAccession);
  List<Psm> findByReportedIdAndAssayAccession(String reportedId, String assayAccession);
  List<Psm> findByReportedIdAndAssayAccessionAndProteinAccessionAndPeptideSequence(String reportedId, String assayAccession, String proteinAccession, String peptideSequence);

  // Protein accession query methods
  List<Psm> findByProteinAccession(String proteinAccession);
  List<Psm> findByProteinAccessionAndProjectAccession(String proteinAccession, String projectAccession);
  List<Psm> findByProteinAccessionAndAssayAccession(String proteinAccession, String assayAccession);
}
