package cz.muni.fi.pv168.project.export;

import cz.muni.fi.pv168.project.export.batch.Batch;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.model.Template;

import java.util.Collection;

/**
 * @author Adam Paulen
 */
public class JsonSystemHelper {

    private Collection<Ride> rides;
    private Collection<Category> categories;
    private Collection<Template> templates;
    private Currency[] currencies = Currency.values();

    public JsonSystemHelper() {
    }
    public JsonSystemHelper(Batch batch) {
        rides = batch.rides();
        categories = batch.categories();
        templates = batch.templates();
    }

    public void setRides(Collection<Ride> rides) {
        this.rides = rides;
    }

    public void setCategories(Collection<Category> categories) {
        this.categories = categories;
    }

    public void setTemplates(Collection<Template> templates) {
        this.templates = templates;
    }

    public void setCurrencies(Currency[] currencies) {
        this.currencies = currencies;
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

    public Currency[] getCurrencies() {
        return currencies;
    }
}
