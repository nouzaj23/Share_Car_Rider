package cz.muni.fi.pv168.project.model;

import java.time.LocalDateTime;

public class Ride {
    private String name;
    private int passengers;
    private Currency currency;
    private Category category;
    private LocalDateTime from;
    private LocalDateTime to;
    private int distance;

    public Ride(String name, int passengers, Currency currency, Category category,
                LocalDateTime from, LocalDateTime to, int distance) {
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

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        if (distance >= 0) {
            this.distance = distance;
        }
    }

    public static Ride exampleRide(){
        return new Ride("Sluzobka", 3, Currency.CZK, new Category("Sluzobna jazda"),
                        LocalDateTime.now(), LocalDateTime.now(), 100);
    }
}
