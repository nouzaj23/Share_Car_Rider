package cz.muni.fi.pv168.project.model;

import java.time.LocalDate;

public class Ride {
    private String name;
    private int passengers;
    private Currency currency;
    private Category category;
    private String from;
    private String to;
    private int distance;
    private float hours;
    private LocalDate date;
    private boolean isCommitted = false;

    public Ride(String name, int passengers, Currency currency, Category category,
                String from, String to, int distance) {
        this.name = name;
        this.passengers = passengers;
        this.currency = currency;
        this.category = category;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        if (passengers >= 0) {
            this.passengers = passengers;
        }
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getDistance() {
        return distance;
    }

    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public static Ride exampleRide() {
        return new Ride("Sluzobka", 3, Currency.CZK, null, "Doma", "Práce", 2);
    }

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
