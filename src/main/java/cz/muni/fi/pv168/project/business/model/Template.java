package cz.muni.fi.pv168.project.business.model;

import cz.muni.fi.pv168.project.business.guidProvider.UuidGuidProvider;

public class Template extends AbstractRide{

    public Template(String guid, String name, int passengers, Currency currency, Category category,
                String from, String to, int distance) {
        super(guid, name, passengers, currency, category, from, to, distance);
    }

    public static Template exampleTemplate(Currency currency) {
        return new Template(UuidGuidProvider.newGuidStatic(), "Template", 2, currency, Category.exampleCategory(), "Home", "Work", 2);
    }

}
