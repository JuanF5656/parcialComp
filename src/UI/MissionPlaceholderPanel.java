package UI;

import javax.swing.*;
import java.awt.*;

public class MissionPlaceholderPanel extends JPanel {

    public MissionPlaceholderPanel(String missionTitle, String algorithmNames) {
        setLayout(new GridBagLayout());

        JLabel label = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "<h2>" + missionTitle + "</h2>"
                        + "<p>(" + algorithmNames + ")</p>"
                        + "<p>Not wired up yet — coming soon.</p>"
                        + "</div></html>",
                SwingConstants.CENTER
        );
        label.setForeground(Color.GRAY);

        add(label);
    }
}