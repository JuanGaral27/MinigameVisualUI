package src.game;

import javax.swing.*;

public class main {
    public static void Main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MiniJuego - Atrapa las frutas");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            gamepanel panel = new gamepanel(600, 800);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            panel.startGame();
        });
    }
}

