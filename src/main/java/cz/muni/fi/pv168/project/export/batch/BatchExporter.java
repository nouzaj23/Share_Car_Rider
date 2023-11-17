package cz.muni.fi.pv168.project.export.batch;

import cz.muni.fi.pv168.project.export.format.FileFormat;

public interface BatchExporter extends FileFormat {
    void exportBatch(Batch batch, String filePath);
}
