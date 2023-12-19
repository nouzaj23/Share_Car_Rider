package cz.muni.fi.pv168.project.business.model;

public class Template extends AbstractRide{

    public Template(String guid, String name, int passengers, Currency currency, Category category,
                String from, String to, int distance) {
        super(guid, name, passengers, currency, category, from, to, distance);
    }

    @Override
    public String toString() {
        return "Template{" +
                "name='" + name + '\'' +
                ", passengers=" + passengers +
                ", currency=" + currency +
                ", category=" + category +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", distance=" + distance +
                ", guid='" + guid + '\'' +
                '}';
    }
}
