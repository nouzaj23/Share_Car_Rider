package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.business.model.Template;

import javax.swing.*;

public class TemplateRenderer extends AbstractRenderer<Template> {

    public TemplateRenderer(){
        super(Template.class);
    }
    @Override
    protected void updateLabel(JLabel label, Template value) {
        if ( value != null ){
            label.setText(value.getName());
        }
    }
}
