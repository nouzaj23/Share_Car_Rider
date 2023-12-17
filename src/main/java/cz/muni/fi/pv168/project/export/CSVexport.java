package cz.muni.fi.pv168.project.export;

import cz.muni.fi.pv168.project.export.batch.Batch;
import cz.muni.fi.pv168.project.export.batch.BatchExporter;
import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.model.Ride;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CSVexport implements BatchExporter {
    private static final String SEPARATOR = ",";

    @Override
    public void exportBatch(Batch batch, String filePath) {

        try (var writer = Files.newBufferedWriter(Path.of(filePath), StandardCharsets.UTF_8)) {
            for (var ride : batch.rides()) {
                String line = createCsvLine(ride);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException exception) {
            throw new RuntimeException("Unable to write to file", exception);
        }
    }

    private String createCsvLine(Ride ride) {
        return serializeEmployee(ride);
    }

    private String serializeEmployee(Ride ride) {
        return String.join(SEPARATOR,
                ride.getName(),
                Integer.toString(ride.getPassengers()),
                ride.getCurrency().getName(),
                Float.toString(ride.getFuelExpenses()),
                ride.getCategory().getName(),
                ride.getFrom(),
                ride.getTo(),
                Integer.toString(ride.getDistance()),
                ride.getDate().format(DateTimeFormatter.ofPattern("dd MM yy"))
                );
    }

    @Override
    public Format getFormat() {
        return new Format("CSV", List.of("csv"));
    }
}
