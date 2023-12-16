package cz.muni.fi.pv168.project.export.batch;


import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;

import java.util.Collection;

public record Batch(Collection<Ride> rides,
                    Collection<Category> categories,
                    Collection<Template> templates,
                    Collection<Currency> currencies) {
}
