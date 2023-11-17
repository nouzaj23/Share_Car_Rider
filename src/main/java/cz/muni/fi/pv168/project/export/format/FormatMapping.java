package cz.muni.fi.pv168.project.export.format;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
public final class FormatMapping<T extends FileFormat> {

    private final Collection<T> fileFormats;
    private final Map<String, T> extensionMapping;

    public FormatMapping(Collection<T> fileFormats) {
        this.fileFormats = fileFormats;
        extensionMapping = fileFormats.stream()
                .map(f -> f.getFormat().extensions().stream()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                e -> f
                        )))
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public T findByExtension(String extension) {
        return extensionMapping.get(extension);
    }

    public Collection<Format> getFormats() {
        return fileFormats.stream()
                .map(T::getFormat)
                .toList();
    }
}
