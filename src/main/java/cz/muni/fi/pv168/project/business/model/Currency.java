package cz.muni.fi.pv168.project.business.model;

public class Currency extends Entity {


    private String name;
    private float rate;


    public Currency(String name, float rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public static Currency exampleCurrency(){
        return new Currency("USD", 18);
    }

    @Override
    public String toString() {
        return name;
    }
}
