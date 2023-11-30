package cz.muni.fi.pv168.project.business.model;

import cz.muni.fi.pv168.project.business.guidProvider.UuidGuidProvider;

public class Currency extends Entity{
    private String code;
    private double conversionRatio;

    public Currency(String guid, String code, double conversionRatio) {
        super(guid);
        this.code = code;
        this.conversionRatio = conversionRatio;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getConversionRatio() {
        return conversionRatio;
    }

    public void setConversionRatio(double conversionRatio) {
        this.conversionRatio = conversionRatio;
    }

        public static Currency exampleCurrency(){
        return new Currency(UuidGuidProvider.newGuidStatic(), "USD", 0.045);
    }

    @Override
    public String toString() {
        return code;
    }
}
