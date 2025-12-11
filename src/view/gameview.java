package view;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Rectangle;
import model.gamemodel;

/**
 * Vista del juego: dibuja el estado provisto por gamemodel.
 * No contiene lógica del juego ni eventos.
 */
public class gameview extends JPanel {
    private final gamemodel model;
    private final Font hudFont = new Font("SansSerif", Font.BOLD, 18);
    private final Font titleFont = new Font("SansSerif", Font.BOLD, 36);

    public gameview(gamemodel model) {
        this.model = model;
        setPreferredSize(new java.awt.Dimension(model.width, model.height));
        setBackground(new Color(35, 120, 200));
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // suelo
        g.setColor(new Color(30, 90, 40));
        g.fillRect(0, model.height - 40, model.width, 40);

        // jugador (canasta)
        drawPlayer(g);

        // objetos que caen
        for (gamemodel.Falling f : model.items) {
            drawFalling(g, f);
        }

        // HUD
        g.setFont(hudFont);
        g.setColor(Color.WHITE);
        g.drawString("Puntuación: " + model.score, 12, 24);
        g.drawString("Vidas: " + model.lives, 12, 48);
        g.drawString("Pausa: " + (model.paused ? "ON" : "OFF") + "  (P para pausar)", model.width - 300, 24);

        // pantalla inicial
        if (!model.running) {
            g.setFont(titleFont);
            drawCenteredString(g, "Atrapa las frutas", titleFont, new Rectangle(0, model.height/2 - 100, model.width, 80));
            g.setFont(hudFont);
            drawCenteredString(g, "Usa ← → para mover. Presiona ENTER para iniciar.", hudFont, new Rectangle(0, model.height/2 - 20, model.width, 40));
            drawCenteredString(g, "P para pausar, Espacio para fruta especial (+2 pts).", hudFont, new Rectangle(0, model.height/2 + 10, model.width, 40));
        }

        if (model.paused && model.running) {
            g.setFont(titleFont);
            drawCenteredString(g, "PAUSADO", titleFont, new Rectangle(0, model.height/2 - 60, model.width, 120));
        }

        g.dispose();
    }

    private void drawPlayer(Graphics2D g) {
        gamemodel.Player p = model.player;
        g.setColor(new Color(120, 60, 20));
        g.fillRoundRect(p.x, p.y, p.w, p.h, 16, 16);

        g.setColor(new Color(200, 160, 100, 160));
        g.fillRoundRect(p.x + 6, p.y + 6, p.w - 12, p.h - 10, 12, 12);
    }

    private void drawFalling(Graphics2D g, gamemodel.Falling f) {
        g.setColor(f.color);
        g.fillOval(f.x, f.y, f.w, f.h);

        g.setColor(new Color(255,255,255,120));
        g.fillOval(f.x + f.w/4, f.y + f.h/6, f.w/4, f.h/4);
    }

    private void drawCenteredString(Graphics2D g, String text, Font font, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }
}
