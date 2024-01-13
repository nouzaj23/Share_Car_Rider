package cz.muni.fi.pv168.project.business.model;

import java.time.LocalDate;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;

public class Ride extends AbstractRide{
    private LocalDate date;

    private float fuelExpenses;

    public Ride(String guid, String name, int passengers, Currency currency, float fuelExpenses, Category category,
                String from, String to, int distance, LocalDate date) {
        super(guid, name, passengers, currency, category, from, to, distance);
        this.date = date;
        this.fuelExpenses = fuelExpenses;
    }

    public Template extractTemplate() {
        return new Template(GuidProvider.newGuid(), name, passengers, currency, category, from, to, distance);
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
    public String toString() {
        return "Ride{" +
                "date=" + date +
                ", fuelExpenses=" + fuelExpenses +
                ", name='" + name + '\'' +
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
