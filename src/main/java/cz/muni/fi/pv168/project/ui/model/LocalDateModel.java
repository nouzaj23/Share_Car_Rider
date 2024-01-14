package cz.muni.fi.pv168.project.ui.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.jdatepicker.AbstractDateModel;

public class LocalDateModel extends AbstractDateModel<LocalDate> {
    public LocalDateModel() {
    }

    protected Calendar toCalendar(LocalDate from) {
        return GregorianCalendar.from(from.atStartOfDay(ZoneId.systemDefault()));
    }

    protected LocalDate fromCalendar(Calendar from) {
        return from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}