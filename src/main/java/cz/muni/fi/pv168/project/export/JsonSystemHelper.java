package cz.muni.fi.pv168.project.export;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.export.batch.Batch;


import java.util.Collection;

/**
 * @author Adam Paulen
 */
public class JsonSystemHelper {

    private Collection<Ride> rides;
    private Collection<Category> categories;
    private Collection<Template> templates;
    private Collection<Currency> currencies;

    public JsonSystemHelper(Batch batch) {
        rides = batch.rides();
        categories = batch.categories();
        templates = batch.templates();
        currencies = batch.currencies();
    }

    public void setRides(Collection<Ride> rides) {
        this.rides = rides;
    }

    public Collection<Ride> getRides() {
        return rides;
    }

    public Collection<Category> getCategories() {
        return categories;
    }

    public Collection<Template> getTemplates() {
        return templates;
    }

    public Collection<Currency> getCurrencies() {
        return currencies;
    }
}
