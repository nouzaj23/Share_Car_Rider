package cz.muni.fi.pv168.project.business.model;

public class Template extends AbstractRide{

    public Template(String name, int passengers, Currency currency, Category category,
                String from, String to, int distance) {
        super(name, passengers, currency, category, from, to, distance);
    }

    public static Template exampleTemplate() {
        return new Template("Template", 2, null, Category.exampleCategory(), "Home", "Work", 2);
    }

}
