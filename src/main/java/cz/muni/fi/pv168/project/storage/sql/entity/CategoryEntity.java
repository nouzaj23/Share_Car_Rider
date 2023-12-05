package cz.muni.fi.pv168.project.storage.sql.entity;

import java.util.Objects;

public record CategoryEntity(Long id, String guid, String name, int distace, int rides) {
  
      public CategoryEntity(Long id, String guid, String name, int distace, int rides) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.name = Objects.requireNonNull(name, "number must not be null");
        this.distace = Objects.requireNonNull(distace, "name must not be null");
        this.rides = Objects.requireNonNull(rides, "name must not be null");
    }
}
