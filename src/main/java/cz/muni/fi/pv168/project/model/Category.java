package cz.muni.fi.pv168.project.model;

public class Category {
    private String name;
    private int distance;
    private int rides;

    public Category(String name) {

        this.name = name;
        this.distance = 0;
        this.rides = 0;
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
        this.distance = distance;
    }

    public int getRides() {
        return rides;
    }

    public void setRides(int rides) {
        this.rides = rides;
    }
}
