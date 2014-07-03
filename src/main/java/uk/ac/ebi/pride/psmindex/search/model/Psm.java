package uk.ac.ebi.pride.psmindex.search.model;

import org.apache.solr.client.solrj.beans.Field;
import uk.ac.ebi.pride.archive.dataprovider.identification.PeptideSequenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.param.CvParamProvider;
import uk.ac.ebi.pride.psmindex.search.util.helper.CvParamHelper;
import uk.ac.ebi.pride.psmindex.search.util.helper.ModificationHelper;
import uk.ac.ebi.pride.psmindex.search.util.helper.ModificationProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jose A. Dianes, Noemi del Toro
 * @version $Id$
 *
 */
//TODO Define interface:  ModificationProvider
public class Psm implements PeptideSequenceProvider {

    @Field(PsmFields.ID)
    private String id;

    //Internal id in the mzTabFile
    @Field(PsmFields.REPORTED_ID)
    private String reportedId;

    @Field(PsmFields.PEPTIDE_SEQUENCE)
    private String peptideSequence;

    // If the psm was discovered as a combination of several spectra, we will
    // simplify the case choosing only the first spectrum
    @Field(PsmFields.SPECTRUM_ID)
    private String spectrumId;

    @Field(PsmFields.PROTEIN_ACCESSION)
    private String proteinAccession;

    @Field(PsmFields.PROJECT_ACCESSION)
    private String projectAccession;

    @Field(PsmFields.ASSAY_ACCESSION)
    private String assayAccession;

    @Field(PsmFields.MODIFICATIONS)
    private List<String> modificationsAsString;

    @Field(PsmFields.UNIQUE)
    private Boolean unique;

    @Field(PsmFields.SEARCH_ENGINE)
    private List<String> searchEngineAsString;

    @Field(PsmFields.SEARCH_ENGINE_SCORE)
    private List<String> searchEngineScoreAsString;

    // If the psm was discovered as a combination of several spectra, we will
    // simplify the case choosing only the first spectrum and the first retention time
    @Field(PsmFields.RETENTION_TIME)
    private Double retentionTime;

    @Field(PsmFields.CHARGE)
    private Integer charge;

    @Field(PsmFields.EXPERIMENTAL_MASS_TO_CHARGE)
    private Double expMassToCharge;

    @Field(PsmFields.CALCULATED_MASS_TO_CHARGE)
    private Double calculatedMassToCharge;

    @Field(PsmFields.PRE_AMINO_ACID)
    private String preAminoAcid;

    @Field(PsmFields.POST_AMINO_ACID)
    private String postAminoAcid;

    @Field(PsmFields.START_POSITION)
    private Integer startPosition;

    @Field(PsmFields.END_POSITION)
    private Integer endPosition;

    private List<CvParamProvider> searchEngineScores;

    private List<CvParamProvider> searchEngines;

    private List<ModificationProvider> modifications;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportedId() {
        return reportedId;
    }

    public void setReportedId(String reportedId) {
        this.reportedId = reportedId;
    }

    public String getPeptideSequence() {
        return peptideSequence;
    }

    public void setPeptideSequence(String peptideSequence) {
        this.peptideSequence = peptideSequence;
    }

    public String getSpectrumId() {
        return spectrumId;
    }

    public void setSpectrumId(String spectrumId) {
        this.spectrumId = spectrumId;
    }

    public String getProteinAccession() {
        return proteinAccession;
    }

    public void setProteinAccession(String proteinAccession) {
        this.proteinAccession = proteinAccession;
    }

    public String getProjectAccession() {
        return projectAccession;
    }

    public void setProjectAccession(String projectAccession) {
        this.projectAccession = projectAccession;
    }

    public String getAssayAccession() {
        return assayAccession;
    }

    public void setAssayAccession(String assayAccession) {
        this.assayAccession = assayAccession;
    }

    public Boolean isUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public Iterable<CvParamProvider> getSearchEngines() {

        for (String se : searchEngineAsString) {
            searchEngines.add(CvParamHelper.convertFromString(se));
        }

        return searchEngines;
    }

    public void addSearchEngine(CvParamProvider searchEngine) {

        if (searchEngines == null) {
            searchEngines = new ArrayList<CvParamProvider>();
        }

        searchEngines.add(searchEngine);

        if (searchEngineAsString == null) {
            searchEngineAsString = new ArrayList<String>();
        }

        searchEngineAsString.add(CvParamHelper.convertToString(searchEngine));
    }

    public void setSearchEngines(List<CvParamProvider> searchEngines) {

        List<String> searchEngineAsString = new ArrayList<String>();

        for (CvParamProvider searchEngine : searchEngines) {
            searchEngineScoreAsString.add(CvParamHelper.convertToString(searchEngine));
        }

        this.searchEngines = searchEngines;
        this.searchEngineAsString = searchEngineAsString;
    }


    public Iterable<CvParamProvider> getSearchEngineScores() {

        for (String ses : searchEngineScoreAsString) {
            searchEngineScores.add(CvParamHelper.convertFromString(ses));
        }

        return searchEngineScores;
    }

    public void addSearchEngineScore(CvParamProvider searchEngineScore) {

        if (searchEngineScores == null) {
            searchEngineScores = new ArrayList<CvParamProvider>();
        }

        searchEngineScores.add(searchEngineScore);

        if (searchEngineScoreAsString == null) {
            searchEngineScoreAsString = new ArrayList<String>();
        }

        searchEngineScoreAsString.add(CvParamHelper.convertToString(searchEngineScore));
    }

    public void setSearchEngineScores(List<CvParamProvider> searchEngineScores) {

        List<String> searchEngineScoreAsString = new ArrayList<String>();

        for (CvParamProvider searchEngineScore : searchEngineScores) {
            searchEngineScoreAsString.add(CvParamHelper.convertToString(searchEngineScore));
        }

        this.searchEngineScores = searchEngineScores;
        this.searchEngineScoreAsString = searchEngineScoreAsString;
    }

    public Iterable<ModificationProvider> getModifications() {

        for (String mod : modificationsAsString) {
            modifications.add(ModificationHelper.convertFromString(mod));
        }

        return modifications;
    }

    public void addModification(ModificationProvider modification) {

        if (modifications == null) {
            modifications = new ArrayList<ModificationProvider>();
        }

        modifications.add(modification);

        if (modificationsAsString == null) {
            modificationsAsString = new ArrayList<String>();
        }

        modificationsAsString.add(ModificationHelper.convertToString(modification));

    }

    public void setModifications(List<ModificationProvider> modifications){

    }

    public Double getRetentionTime() {
        return retentionTime;
    }

    public void setRetentionTime(Double retentionTime) {
        this.retentionTime = retentionTime;
    }

    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    public Double getExpMassToCharge() {
        return expMassToCharge;
    }

    public void setExpMassToCharge(Double expMassToCharge) {
        this.expMassToCharge = expMassToCharge;
    }

    public Double getCalculatedMassToCharge() {
        return calculatedMassToCharge;
    }

    public void setCalculatedMassToCharge(Double calculatedMassToCharge) {
        this.calculatedMassToCharge = calculatedMassToCharge;
    }

    public String getPreAminoAcid() {
        return preAminoAcid;
    }

    public void setPreAminoAcid(String preAminoAcid) {
        this.preAminoAcid = preAminoAcid;
    }

    public String getPostAminoAcid() {
        return postAminoAcid;
    }

    public void setPostAminoAcid(String postAminoAcid) {
        this.postAminoAcid = postAminoAcid;
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }

    public Integer getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
    }

}
