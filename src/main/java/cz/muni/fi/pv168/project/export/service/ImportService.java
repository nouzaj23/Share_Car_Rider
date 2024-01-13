package cz.muni.fi.pv168.project.export.service;

import cz.muni.fi.pv168.project.export.format.Format;

import java.util.Collection;

/**
 * Generic mechanism, allowing to import data from a file.
 */
public interface ImportService {

    int[] importData(String filePath);

    Collection<Format> getFormats();
}
