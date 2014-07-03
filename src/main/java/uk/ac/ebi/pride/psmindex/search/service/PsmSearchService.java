package uk.ac.ebi.pride.psmindex.search.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.pride.psmindex.search.model.Psm;
import uk.ac.ebi.pride.psmindex.search.service.repository.SolrPsmRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Jose A. Dianes, Noemi del Toro
 * @version $Id$
 *          <p/>
 *          NOTE: protein accessions can contain chars that produce problems in solr queries ([,],:). They are replaced by _ when
 *          using the repository methods
 */
@Service
public class PsmSearchService {

    private SolrPsmRepository solrPsmRepository;

    public PsmSearchService(SolrPsmRepository solrPsmRepository) {
        this.solrPsmRepository = solrPsmRepository;
    }

    public void setSolrPsmRepository(SolrPsmRepository solrPsmRepository) {
        this.solrPsmRepository = solrPsmRepository;
    }

    // find by accession methods
    public List<Psm> findById(String id) {
        return solrPsmRepository.findById(id);
    }

    public List<Psm> findById(Collection<String> ids) {
        return solrPsmRepository.findByIdIn(ids);
    }

    // Sequence query methods
    public List<Psm> findByPeptideSequence(String peptideSequence) {
        return solrPsmRepository.findByPeptideSequence(peptideSequence);
    }

    public Page<Psm> findByPeptideSequence(String peptideSequence, Pageable pageable) {
        return solrPsmRepository.findByPeptideSequence(peptideSequence, pageable);
    }

    public List<Psm> findByPeptideSubSequence(String peptideSequence) {
        return solrPsmRepository.findByPeptideSequence("*"+peptideSequence+"*");
    }

    public Page<Psm> findByPeptideSubSequence(String peptideSequence, Pageable pageable) {
        return solrPsmRepository.findByPeptideSequence("*"+peptideSequence+"*", pageable);
    }

    public List<Psm> findByPeptideSequenceAndProjectAccession(String peptideSequence, String projectAccession) {
        return solrPsmRepository.findByPeptideSequenceAndProjectAccessions(peptideSequence,projectAccession);
    }
    public List<Psm> findByPeptideSubSequenceAndProjectAccession(String peptideSequence, String projectAccession) {
        return solrPsmRepository.findByPeptideSequenceAndProjectAccessions("*"+peptideSequence+"*",projectAccession);
    }

    public List<Psm> findByPeptideSequenceAndAssayAccession(String peptideSequence, String assayAccession) {
        return solrPsmRepository.findByPeptideSequenceAndAssayAccession(peptideSequence, assayAccession);
    }
    public List<Psm> findByPeptideSubSequenceAndAssayAccession(String peptideSequence, String assayAccession) {
        return solrPsmRepository.findByPeptideSequenceAndAssayAccession("*"+peptideSequence+"*", assayAccession);
    }

    // Project accession query methods
    public List<Psm> findByProjectAccession(String projectAccession) {
        return solrPsmRepository.findByProjectAccession(projectAccession);
    }

    public List<Psm> findByProjectAccession(Collection<String> projectAccessions) {
        return solrPsmRepository.findByProjectAccessionIn(projectAccessions);
    }

    public Page<Psm> findByProjectAccession(String projectAccession, Pageable pageable) {
        return solrPsmRepository.findByProjectAccession(projectAccession, pageable);
    }

    public Page<Psm> findByProjectAccession(Collection<String> projectAccessions, Pageable pageable) {
        return solrPsmRepository.findByProjectAccessionIn(projectAccessions, pageable);
    }

    // Assay accession query methods
    public List<Psm> findByAssayAccession(String assayAccession) {
        return solrPsmRepository.findByAssayAccession(assayAccession);
    }

    public List<Psm> findByAssayAccession(Collection<String> assayAccessions) {
        return solrPsmRepository.findByAssayAccessionIn(assayAccessions);
    }

    public Page<Psm> findByAssayAccession(String assayAccession, Pageable pageable) {
        return solrPsmRepository.findByAssayAccession(assayAccession, pageable);
    }

    public Page<Psm> findByAssayAccession(Collection<String> assayAccessions, Pageable pageable) {
        return solrPsmRepository.findByAssayAccessionIn(assayAccessions, pageable);
    }

    // Spectrum id query methods
    public List<Psm> findBySpectrumId(String spectrumId) {
        return solrPsmRepository.findBySpectrumId(spectrumId);
    }

    public List<Psm> findBySpectrumId(Collection<String> spectrumIds) {
        return solrPsmRepository.findBySpectrumIdIn(spectrumIds);
    }

    // Reported id query methods
    public List<Psm> findByReportedId(String reportedId) {
        return solrPsmRepository.findByReportedId(reportedId);
    }

    public List<Psm> findByReportedIdAndProjectAccession(String reportedId, String projectAccession) {
        return solrPsmRepository.findByReportedIdAndProjectAccession(reportedId, projectAccession);
    }

    public List<Psm> findByReportedIdAndAssayAccession(String reportedId, String assayAccession) {
        return solrPsmRepository.findByReportedIdAndAssayAccession(reportedId, assayAccession);
    }

    // Protein Accession query methods
    public List<Psm> findByProteinAccession(String proteinAccession) {
        return solrPsmRepository.findByProteinAccession(proteinAccession);
    }

    public List<Psm> findByProteinAccessionAndProjectAccession(String proteinAccession, String projectAccession) {
        return solrPsmRepository.findByProteinAccessionAndProjectAccession(proteinAccession, projectAccession);
    }

    public List<Psm> findByProteinAccessionAndAssayAccession(String proteinAccession, String assayAccession) {
        return solrPsmRepository.findByProteinAccessionAndAssayAccession(proteinAccession, assayAccession);
    }

    public List<String> findPeptideSequencesByProjectAccession(String projectAccession) {

        List<String> peptideSequences = new ArrayList<String>();
        List<Psm> psms = solrPsmRepository.findPeptideSequencesByProjectAccession(projectAccession);

        if (psms != null) {
            for (Psm psm : psms) {
                peptideSequences.add(psm.getPeptideSequence());
            }
        }

        return peptideSequences;
    }
}
