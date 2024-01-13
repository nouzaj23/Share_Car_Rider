package cz.muni.fi.pv168.project.storage.sql.entity;

import java.util.Objects;

public record CategoryEntity(Long id, String guid, String name) {
  
      public CategoryEntity(Long id, String guid, String name) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.name = Objects.requireNonNull(name, "number must not be null");
    }
}
