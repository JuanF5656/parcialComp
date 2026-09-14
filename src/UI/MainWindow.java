package UI;

import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow() {

        setTitle("The Feline Graph Chronicles");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Mission 1 — Minefield",
                new MissionPlaceholderPanel("Rescuing Nina from the Minefield", "BFS & DFS"));
        tabs.addTab("Mission 2 — Claude Accounts", new Mision2Panel());
        tabs.addTab("Mission 3 — Food Stash",
                new MissionPlaceholderPanel("The Ultimate Food Stash", "Floyd-Warshall & Bellman-Ford"));
        tabs.addTab("Mission 4 — Network", new Mision4Panel());

        add(tabs);

        setVisible(true);
    }
}