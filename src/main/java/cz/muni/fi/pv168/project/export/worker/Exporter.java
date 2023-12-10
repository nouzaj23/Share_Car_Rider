package cz.muni.fi.pv168.project.export.worker;

import cz.muni.fi.pv168.project.export.format.Format;
import java.util.Collection;

public interface Exporter {

    void exportData(String filePath);

    Collection<Format> getFormats();
}
