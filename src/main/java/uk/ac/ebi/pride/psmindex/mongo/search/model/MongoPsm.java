package uk.ac.ebi.pride.psmindex.mongo.search.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;
import uk.ac.ebi.pride.archive.dataprovider.identification.PeptideSequenceProvider;
import uk.ac.ebi.pride.archive.dataprovider.param.CvParamProvider;
import uk.ac.ebi.pride.indexutils.helpers.CvParamHelper;
import uk.ac.ebi.pride.indexutils.helpers.ModificationHelper;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "psm")
public class MongoPsm implements PeptideSequenceProvider {
  @Id
  private String id;
  private String reportedId;
  private String peptideSequence;
  private String spectrumId;   // If the psm was discovered as a combination of several spectra, we will  simplify the case choosing only the first spectrum
  private String proteinAccession;
  private String database;
  private String databaseVersion;
  private String projectAccession;
  private String assayAccession;
  private List<String> modificationsAsString;
  private List<String> modificationNames;
  private List<String> modificationAccessions;
  private Boolean unique;
  private List<String> searchEngineAsString;
  private List<String> searchEngineScoreAsString;
  private Double retentionTime;   // If the psm was discovered as a combination of several spectra, we will simplify the case choosing only the first spectrum and the first retention time
  private Integer charge;
  private Double expMassToCharge;
  private Double calculatedMassToCharge;
  private String preAminoAcid;
  private String postAminoAcid;
  private Integer startPosition;
  private Integer endPosition;

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

  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public String getDatabaseVersion() {
    return databaseVersion;
  }

  public void setDatabaseVersion(String databaseVersion) {
    this.databaseVersion = databaseVersion;
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

    List<CvParamProvider> searchEngines = new ArrayList<CvParamProvider>();

    if (searchEngineAsString != null) {
      for (String se : searchEngineAsString) {
        searchEngines.add(CvParamHelper.convertFromString(se));
      }
    }

    return searchEngines;
  }

  public void setSearchEngines(List<CvParamProvider> searchEngines) {

    if (searchEngines == null)
      return;

    List<String> searchEngineAsString = new ArrayList<String>();

    for (CvParamProvider searchEngine : searchEngines) {
      searchEngineScoreAsString.add(CvParamHelper.convertToString(searchEngine));
    }

    this.searchEngineAsString = searchEngineAsString;
  }

  public void addSearchEngine(CvParamProvider searchEngine) {

    if (searchEngineAsString == null) {
      searchEngineAsString = new ArrayList<String>();
    }

    searchEngineAsString.add(CvParamHelper.convertToString(searchEngine));
  }

  public Iterable<CvParamProvider> getSearchEngineScores() {

    List<CvParamProvider> searchEngineScores = new ArrayList<CvParamProvider>();

    if (searchEngineScoreAsString != null) {
      for (String ses : searchEngineScoreAsString) {
        searchEngineScores.add(CvParamHelper.convertFromString(ses));
      }
    }

    return searchEngineScores;
  }

  public void setSearchEngineScores(List<CvParamProvider> searchEngineScores) {

    if (searchEngineScores == null)
      return;

    List<String> searchEngineScoreAsString = new ArrayList<String>();

    for (CvParamProvider searchEngineScore : searchEngineScores) {
      searchEngineScoreAsString.add(CvParamHelper.convertToString(searchEngineScore));
    }

    this.searchEngineScoreAsString = searchEngineScoreAsString;
  }

  public void addSearchEngineScore(CvParamProvider searchEngineScore) {

    if (searchEngineScoreAsString == null) {
      searchEngineScoreAsString = new ArrayList<String>();
    }

    searchEngineScoreAsString.add(CvParamHelper.convertToString(searchEngineScore));
  }

  public Iterable<ModificationProvider> getModifications() {

    List<ModificationProvider> modifications = new ArrayList<ModificationProvider>();

    if (modificationsAsString != null) {
      for (String mod : modificationsAsString) {
        if(!mod.isEmpty()) {
          modifications.add(ModificationHelper.convertFromString(mod));
        }
      }
    }

    return modifications;
  }

  public void setModifications(List<ModificationProvider> modifications) {

    if (modifications == null)
      return;

    List<String> modificationsAsString = new ArrayList<String>();
    List<String> modificationNames = new ArrayList<String>();
    List<String> modificationAccessions = new ArrayList<String>();

    for (ModificationProvider modification : modifications) {
      modificationsAsString.add(ModificationHelper.convertToString(modification));
      modificationAccessions.add(modification.getAccession());
      modificationNames.add(modification.getName());
    }

    this.modificationsAsString = modificationsAsString;
    this.modificationAccessions = modificationAccessions;
    this.modificationNames = modificationNames;
  }

  public void addModification(ModificationProvider modification) {

    if (modificationsAsString == null) {
      modificationsAsString = new ArrayList<String>();
    }

    if (modificationAccessions == null) {
      modificationAccessions = new ArrayList<String>();
    }

    if (modificationNames == null) {
      modificationNames = new ArrayList<String>();
    }


    modificationsAsString.add(ModificationHelper.convertToString(modification));
    modificationAccessions.add(modification.getAccession());
    modificationNames.add(modification.getName());
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
