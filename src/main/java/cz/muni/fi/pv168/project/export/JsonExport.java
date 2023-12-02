package cz.muni.fi.pv168.project.export;

import com.fasterxml.jackson.databind.SerializationFeature;
import cz.muni.fi.pv168.project.export.batch.Batch;
import cz.muni.fi.pv168.project.export.batch.BatchExporter;
import cz.muni.fi.pv168.project.export.format.Format;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class JsonExport implements BatchExporter {
    @Override
    public void exportBatch(Batch batch, String filePath) {

        var system = new JsonSystemHelper(batch);
        try (var writer = Files.newBufferedWriter(Path.of(filePath), StandardCharsets.UTF_8)) {
            writer.write(Objects.requireNonNull(exportToJson(system)));
        } catch (IOException exception) {
            throw new RuntimeException("Unable to write to file", exception);
        }
    }

    private static String exportToJson(JsonSystemHelper system) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            return objectMapper.writeValueAsString(system);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Format getFormat() {
        return new Format("JSON", List.of("json"));
    }


}
