package cz.muni.fi.pv168.project.export.service;

import cz.muni.fi.pv168.project.export.format.Format;

import java.util.Collection;

public interface ExportService {
    int[] exportData(String filePath);

    Collection<Format> getFormats();
}
