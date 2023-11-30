package cz.muni.fi.pv168.project.storage.sql.entity;

import java.util.Objects;

public record TemplateEntity(
  Long id,
  String guid,
  String name,
  int passengers,
  Long currencyId,
  Long categoryId,
  String from,
  String to,
  int distance
  ) {
      public TemplateEntity(
        Long id,
        String guid,
        String name,
        int passengers,
        Long currencyId,
        Long categoryId,
        String from,
        String to,
        int distance
      ) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.name = Objects.requireNonNull(name, "number must not be null");
        this.passengers = Objects.requireNonNull(passengers, "name must not be null");
        this.currencyId = Objects.requireNonNull(currencyId, "name must not be null");
        this.categoryId = Objects.requireNonNull(categoryId, "name must not be null");
        this.from = Objects.requireNonNull(from, "name must not be null");
        this.to = Objects.requireNonNull(to, "name must not be null");
        this.distance = Objects.requireNonNull(distance, "name must not be null");
    }

    public TemplateEntity(
        String guid,
        String name,
        int passengers,
        Long currencyId,
        Long categoryId,
        String from,
        String to,
        int distance
      ) {
        this(null, guid, name, passengers, currencyId, categoryId, from, to, distance);
    }
}
