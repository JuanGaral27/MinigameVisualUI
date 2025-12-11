import controller.gamecontroller;
import model.gamemodel;
import view.gameview;

import javax.swing.*;

public class main {
    public static void Main(String[] args) {

        gamemodel model = new gamemodel(600, 800);
        gameview view = new gameview(model);
        gamecontroller controller = new gamecontroller(model, view);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MiniJuego !Atrapa las Frutas!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            controller.startTimer();
        });
    }
}
