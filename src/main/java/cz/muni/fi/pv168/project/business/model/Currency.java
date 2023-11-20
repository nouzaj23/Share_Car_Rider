package cz.muni.fi.pv168.project.business.model;

public enum Currency {

    CZK,
    EUR,
    USD;

    public String getName() {
        return this.toString();
    }
}
