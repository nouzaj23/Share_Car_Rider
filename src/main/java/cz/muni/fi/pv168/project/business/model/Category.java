package cz.muni.fi.pv168.project.business.model;

public class Category extends Entity {
    private String name;
    private int distance;
    private int rides;

    public Category(String guid, String name) {
        this(guid, name, 0, 0);
    }

    public Category(String guid, String name, int distance, int rides) {
        super(guid);
        this.name = name;
        this.distance = distance;
        this.rides = rides;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        if (distance >= 0) {
            this.distance = distance;
        }
    }

    public Category modifyDistanceFluent(int delta) {
        if (distance < -delta) {
            throw new IllegalArgumentException();
        }

        distance += delta;
        return this;
    }

    public int getRides() {
        return rides;
    }

    public void setRides(int rides) {
        if (rides >= 0) {
            this.rides = rides;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
