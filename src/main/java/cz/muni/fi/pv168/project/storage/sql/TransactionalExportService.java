package cz.muni.fi.pv168.project.storage.sql;

import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.export.service.ExportService;
import cz.muni.fi.pv168.project.export.service.ImportService;
import cz.muni.fi.pv168.project.export.service.ProgressCallback;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionExecutor;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

public class TransactionalExportService implements ExportService {

    private final ExportService exportService;

    private final TransactionExecutor transactionExecutor;

    public TransactionalExportService(ExportService exportService, TransactionExecutor transactionExecutor) {
        this.exportService = exportService;
        this.transactionExecutor = transactionExecutor;
    }

    @Override
    public int[] exportData(String filePath) {
        AtomicReference<int[]> result = new AtomicReference<>();

        transactionExecutor.executeInTransaction(() -> {
            result.set(exportService.exportData(filePath));
        });

        return result.get();
    }

    @Override
    public Collection<Format> getFormats() {
        return exportService.getFormats();
    }
}
