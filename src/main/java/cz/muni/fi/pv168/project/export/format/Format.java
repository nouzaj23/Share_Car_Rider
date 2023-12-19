package cz.muni.fi.pv168.project.export.format;

import java.util.Collection;
import java.util.Collections;

public record Format(String name, Collection<String> extensions) {
        @Override
        public Collection<String> extensions() {
            return Collections.unmodifiableCollection(extensions);
        }
}
