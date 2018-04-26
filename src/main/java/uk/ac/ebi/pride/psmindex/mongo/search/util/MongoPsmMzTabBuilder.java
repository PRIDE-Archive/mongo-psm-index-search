package uk.ac.ebi.pride.psmindex.mongo.search.util;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import uk.ac.ebi.pride.archive.utils.spectrum.SpectrumIDGenerator;
import uk.ac.ebi.pride.archive.utils.spectrum.SpectrumIdGeneratorPrideArchive;
import uk.ac.ebi.pride.jmztab.model.*;
import uk.ac.ebi.pride.psmindex.mongo.search.model.MongoPsm;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static uk.ac.ebi.pride.indexutils.helpers.CvParamHelper.convertToCvParamProvider;
import static uk.ac.ebi.pride.indexutils.helpers.ModificationHelper.convertToModificationProvider;
import static uk.ac.ebi.pride.psmindex.mongo.search.util.MongoPsmIdBuilder.getId;

public class MongoPsmMzTabBuilder {

  private static Logger logger = LoggerFactory.getLogger(MongoPsmMzTabBuilder.class.getName());

  /**
   * The map between the assay accession and the file need to be provided externally from the
   * database
   *
   * @return A map of assay accessions to PSMs
   */
  public static List<MongoPsm> readPsmsFromMzTabFile(
      String projectAccession, String assayAccession, MZTabFile mzTabFile) {
    List<MongoPsm> result = new LinkedList<>();
    if (mzTabFile != null) {
      result =
          convertFromMzTabPsmsToPrideArchivePsms(
              mzTabFile.getPSMs(), mzTabFile.getMetadata(), projectAccession, assayAccession);
      logger.debug(
          "Found " + result.size() + " psms for Assay " + assayAccession + " in file " + mzTabFile);
    }
    return result;
  }

  /**
   * Converts from mzTab-PSMs to Archive-compatible PSMs.
   *
   * @param mzTabPsms maTab PSMs
   * @param metadata PSM metadata
   * @param projectAccession the Archive project accession number
   * @param assayAccession the Archive assay accession number
   * @return list of Archive-compatible PSMs.
   */
  private static LinkedList<MongoPsm> convertFromMzTabPsmsToPrideArchivePsms(
      Collection<PSM> mzTabPsms,
      Metadata metadata,
      String projectAccession,
      String assayAccession) {
    LinkedList<MongoPsm> result = new LinkedList<>();
    for (PSM mzTabPsm : mzTabPsms) {
      String cleanPepSequence =
          MongoPsmSequenceCleaner.cleanPeptideSequence(mzTabPsm.getSequence());
      MongoPsm newPsm = new MongoPsm();
      newPsm.setId(
          getId(
              projectAccession,
              assayAccession,
              mzTabPsm.getPSM_ID(),
              mzTabPsm.getAccession(),
              cleanPepSequence));
      newPsm.setReportedId(mzTabPsm.getPSM_ID());
      newPsm.setSpectrumId(createSpectrumId(mzTabPsm, projectAccession));
      newPsm.setPeptideSequence(cleanPepSequence);
      newPsm.setProjectAccession(projectAccession);
      newPsm.setAssayAccession(assayAccession);
      // To be compatible with the project index we don't clean the protein accession
      newPsm.setProteinAccession(mzTabPsm.getAccession());
      newPsm.setDatabase(mzTabPsm.getDatabase());
      newPsm.setDatabaseVersion(mzTabPsm.getDatabaseVersion());
      newPsm.setModifications(new LinkedList<>());
      if (mzTabPsm.getModifications() != null) {
        for (Modification mod : mzTabPsm.getModifications()) {
          newPsm.addModification(convertToModificationProvider(mod));
        }
      }
      MZBoolean unique = mzTabPsm.getUnique();
      if (unique != null) {
        newPsm.setUnique(MZBoolean.True.equals(unique));
      } else {
        newPsm.setUnique(null);
      }
      newPsm.setSearchEngines(new LinkedList<>());
      // If the mzTab search engine can not be converted SplitList can be null
      if (mzTabPsm.getSearchEngine() != null && !mzTabPsm.getSearchEngine().isEmpty()) {
        for (Param searchEngine : mzTabPsm.getSearchEngine()) {
          newPsm.addSearchEngine(convertToCvParamProvider(searchEngine));
        }
      }
      newPsm.setSearchEngineScores(new LinkedList<>());
      if (metadata.getPsmSearchEngineScoreMap() != null
          && !metadata.getPsmSearchEngineScoreMap().isEmpty()) {
        for (PSMSearchEngineScore psmSearchEngineScore :
            metadata.getPsmSearchEngineScoreMap().values()) {
          if (mzTabPsm.getSearchEngineScore(psmSearchEngineScore.getId()) != null) {
            /* We create a Param as the composition between the searchEngineScore stored in the metadata
            and the search engine score value stored in the psm */
            Param param = psmSearchEngineScore.getParam();
            String value = mzTabPsm.getSearchEngineScore(psmSearchEngineScore.getId()).toString();
            newPsm.addSearchEngineScore(
                convertToCvParamProvider(
                    param.getCvLabel(), param.getAccession(), param.getName(), value));
          }
        }
      }
      SplitList<Double> retentionTimes = mzTabPsm.getRetentionTime();
      if (!CollectionUtils.isEmpty(retentionTimes)) {
        newPsm.setRetentionTime(retentionTimes.get(0));
      }
      newPsm.setCharge(mzTabPsm.getCharge());
      newPsm.setExpMassToCharge(mzTabPsm.getExpMassToCharge());
      newPsm.setCalculatedMassToCharge(mzTabPsm.getCalcMassToCharge());
      newPsm.setPreAminoAcid(mzTabPsm.getPre());
      newPsm.setPostAminoAcid(mzTabPsm.getPost());
      if (mzTabPsm.getStart() != null) {
        newPsm.setStartPosition(mzTabPsm.getStart());
      }
      if (mzTabPsm.getEnd() != null) {
        newPsm.setEndPosition(mzTabPsm.getEnd());
      }
      result.add(newPsm);
    }
    return result;
  }

  /**
   * Creates a spectrum Id compatible with PRIDE Archive
   *
   * @param psm original mzTab psm
   * @param projectAccession PRIDE Archive accession
   * @return the spectrum Id or "unknown_id" if the conversion fails
   */
  private static String createSpectrumId(PSM psm, String projectAccession) {
    String msRunFileName;
    String identifierInMsRunFile;
    String spectrumId = "unknown_id";
    SpectraRef spectrum;
    SplitList<SpectraRef> spectra = psm.getSpectraRef();
    if (!CollectionUtils.isEmpty(spectra)) {
      // If the psm was discovered as a combination of several spectra, we will
      // simplify the case choosing only the first spectrum
      if (logger.isDebugEnabled()) {
        logger.debug("Found " + spectra.size() + " spectra for PSM " + psm.getPSM_ID());
      }
      spectrum = spectra.get(0);
      if (spectrum != null) {
        msRunFileName = extractFileName(spectrum.getMsRun().getLocation().getFile());
        identifierInMsRunFile = spectrum.getReference();
        if (msRunFileName != null
            && !msRunFileName.isEmpty()
            && identifierInMsRunFile != null
            && !identifierInMsRunFile.isEmpty()) {
          SpectrumIDGenerator spectrumIDGenerator = new SpectrumIdGeneratorPrideArchive();
          spectrumId =
              spectrumIDGenerator.generate(projectAccession, msRunFileName, identifierInMsRunFile);
        }
      }
    }
    return spectrumId;
  }

  private static String extractFileName(String filePath) {
    return FilenameUtils.getName(filePath);
  }
}
