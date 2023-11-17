package cz.muni.fi.pv168.project.export.batch;

import cz.muni.fi.pv168.project.export.format.FileFormat;

public interface BatchImporter extends FileFormat {

    Batch importBatch(String filePath);
}
