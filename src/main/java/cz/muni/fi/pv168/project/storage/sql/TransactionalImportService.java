package cz.muni.fi.pv168.project.storage.sql;

import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.export.service.ImportService;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionExecutor;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;

public class TransactionalImportService implements ImportService {

    private final ImportService importService;

    private final TransactionExecutor transactionExecutor;

    public TransactionalImportService(ImportService importService, TransactionExecutor transactionExecutor) {
        this.importService = importService;
        this.transactionExecutor = transactionExecutor;
    }

    @Override
    public int[] importData(String filePath) {
        AtomicReference<int[]> result = new AtomicReference<>();

        transactionExecutor.executeInTransaction(() -> {
            result.set(importService.importData(filePath));
        });

        return result.get();
    }

    @Override
    public Collection<Format> getFormats() {
        return importService.getFormats();
    }
}
