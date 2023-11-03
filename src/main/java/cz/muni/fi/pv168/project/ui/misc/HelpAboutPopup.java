package cz.muni.fi.pv168.project.ui.misc;

import javax.swing.*;

public class HelpAboutPopup extends JMenuItem {
    public HelpAboutPopup(String text) {
        super(text);
        this.addActionListener(e -> {
            String aboutInfo = "<html><body><h1>About Share Car Rider</h1>" +
                    "<p>This is an application for tracking of personal or business trips, " +
                    "created as a project within a scope of subject PV168</p>" +
                    "<br>" +
                    "<p><b>Authors:</b><br>" +
                    "Martin Tuček<br>" +
                    "Jan Nouza<br>" +
                    "Adam Paulen<br>" +
                    "Jonáš Jadrníček</p><br></body></html>";


            JOptionPane.showMessageDialog(null, aboutInfo,
                    "About", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
