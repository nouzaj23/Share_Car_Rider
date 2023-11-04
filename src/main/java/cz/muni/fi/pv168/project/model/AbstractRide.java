package cz.muni.fi.pv168.project.model;

public abstract class AbstractRide {
    protected String name;
    protected int passengers;
    protected Currency currency;
    protected Category category;
    protected String from;
    protected String to;
    protected int distance;
    protected float hours;

    public AbstractRide(String name, int passengers, Currency currency, Category category,
                    String from, String to, int distance) {
        this.name = name;
        this.passengers = passengers;
        this.currency = currency;
        this.category = category;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public static AbstractRide exampleAbstractRide() {
        return new AbstractRide("Work trip", 2, Currency.CZK, Category.exampleCategory(), "Home", "Work", 2) {
        };
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

    public void setDistance(int distance) {
        if (distance >= 0) {
            this.distance = distance;
        }
    }
}
