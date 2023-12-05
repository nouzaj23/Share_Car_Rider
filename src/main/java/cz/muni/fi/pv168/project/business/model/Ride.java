package cz.muni.fi.pv168.project.business.model;

import java.time.LocalDate;

public class Ride extends AbstractRide{
    private LocalDate date;

    private float fuelExpenses;
    private boolean isCommitted = false;

    public Ride(String name, int passengers, Currency currency, float fuelExpenses, Category category,
                String from, String to, int distance) {
        super(name, passengers, currency, category, from, to, distance);
        this.fuelExpenses = fuelExpenses;
    }

    public Template extractTemplate() {
        return new Template(name, passengers, currency, category, from, to, distance);
    }

    public static Ride exampleRide() {
        return new Ride("Sluzobka", 3, null,157, null, "Doma", "Práce", 2);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public float getFuelExpenses() {
        return fuelExpenses;
    }

    public void setFuelExpenses(float fuelExpenses) {
        this.fuelExpenses = fuelExpenses;
    }

    @Override
    public void setDistance(int distance) {
        if (distance >= 0) {
            var originalDistance = this.distance;
            this.distance = distance;
            if (category != null && isCommitted) {
                category.modifyDistanceFluent(distance - originalDistance);
            }
        }

    }

    public void setCommitted(boolean committed) {
        isCommitted = committed;
    }


}
