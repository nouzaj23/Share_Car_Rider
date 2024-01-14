package cz.muni.fi.pv168.project.business.model;

public class Category extends Entity {
    private String name;

    public Category(String guid, String name) {
        super(guid);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", guid='" + guid + '\'' +
                '}';
    }
}
