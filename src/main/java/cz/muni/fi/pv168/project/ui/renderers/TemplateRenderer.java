package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.model.Ride;

import javax.swing.*;

public class TemplateRenderer extends AbstractRenderer<Ride> {

    public TemplateRenderer(){
        super(Ride.class);
    }
    @Override
    protected void updateLabel(JLabel label, Ride value) {
        if ( value != null ){
            label.setText(value.getName());
        }
    }
}
