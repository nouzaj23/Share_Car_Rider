package cz.muni.fi.pv168.project.storage.sql.entity;

import java.util.Objects;

public record CurrencyEntity(Long id, String guid, String code, double conversionRatio) {
  
      public CurrencyEntity(Long id, String guid, String code, double conversionRatio) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.code = Objects.requireNonNull(code, "number must not be null");
        this.conversionRatio = Objects.requireNonNull(conversionRatio, "name must not be null");
    }
}
