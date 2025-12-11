package controller;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import model.gamemodel;
import view.gameview;

/**
 * Controlador: gestiona entrada y ciclo (Timer). Contiene main() para arrancar.
 */
public class gamecontroller implements KeyListener, ActionListener {
    private final gamemodel model;
    private final gameview view;
    private final Timer timer;

    public gamecontroller(gamemodel model, gameview view) {
        this.model = model;
        this.view = view;

        // Timer a ~60 FPS (16 ms)
        this.timer = new Timer(16, this);

        // registrar key listener en la vista
        this.view.addKeyListener(this);
    }

    /**
     * Inicia el timer (el modelo puede estar en running=false hasta que el usuario pulse ENTER).
     */
    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // llamamos al modelo para que avance 1 frame
        model.updateFrame();

        // chequear game over
        if (model.isGameOver()) {
            timer.stop(); // detener ciclo
            // Mostrar diálogo (estamos en EDT porque Timer ejecuta en EDT)
            int opt = JOptionPane.showOptionDialog(view,
                    "Juego terminado\nPuntuación: " + model.score + "\n¿Jugar de nuevo?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    new Object[]{"Reiniciar", "Salir"},
                    "Reiniciar");
            if (opt == JOptionPane.YES_OPTION) {
                model.init();
                timer.start();
            } else {
                System.exit(0);
            }
        }

        // repintar la vista
        view.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_LEFT) model.player.movingLeft = true;
        if (k == KeyEvent.VK_RIGHT) model.player.movingRight = true;
        if (k == KeyEvent.VK_P) model.paused = !model.paused;
        if (k == KeyEvent.VK_ENTER) {
            if (!model.running) model.running = true;
        }
        if (k == KeyEvent.VK_SPACE) {
            if (model.running && !model.paused) model.spawnSpecialFruit();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_LEFT) model.player.movingLeft = false;
        if (k == KeyEvent.VK_RIGHT) model.player.movingRight = false;
    }

    @Override
    public void keyTyped(KeyEvent e) { /* no-op */ }

    /**
     * Main: crea model, view y controller. Solo estos 3 archivos están presentes.
     */
    public static void main(String[] args) {
        final gamemodel model = new gamemodel(600, 800);
        final gameview view = new gameview(model);
        final gamecontroller controller = new gamecontroller(model, view);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("MiniJuego - MVC (3 archivos)");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.add(view);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                // iniciar timer; el modelo espera a que el usuario pulse ENTER para 'running=true'
                controller.startTimer();

                // request focus para que reciba teclas
                view.requestFocusInWindow();
            }
        });
    }
}
