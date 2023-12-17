package cz.muni.fi.pv168.project.model;

public enum Currency {

    CZK,
    EUR,
    USD;

    public String getName() {
        return this.toString();
    }
}
