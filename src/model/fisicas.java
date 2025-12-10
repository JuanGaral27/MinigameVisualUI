package src.game;

import java.awt.*;
import java.util.Random;

public class fisicas {
    int x, y, w, h;
    int vy; 
    int scoreValue;
    Color color;

    public fisicas(int x, int y, int w, int h, int vy, int scoreValue, Color color) {
        this.x = x; this.y = y; this.w = w; this.h = h;
        this.vy = vy;
        this.scoreValue = scoreValue;
        this.color = color;
    }

    public void update() {
        y += vy;
    }

    public void render(Graphics2D g) {
        g.setColor(color);
        g.fillOval(x, y, w, h);

        g.setColor(new Color(255,255,255,120));
        g.fillOval(x + w/4, y + h/6, w/4, h/4);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }

    public int getScoreValue() { return scoreValue; }

    public static fisicas random(int panelWidth) {
        Random r = new Random();
        int size = r.nextInt(14) + 20;
        int x = r.nextInt(Math.max(1, panelWidth - size - 16)) + 8;
        int speed = r.nextInt(3) + 2;
        int val = (speed >= 4) ? 2 : 1;
        Color color = randomColor(r);

        return new fisicas(x, -size, size, size, speed, val, color);
    }

    private static Color randomColor(Random r) {
        Color[] colors = {
            new Color(220, 20, 60),
            new Color(255, 165, 0),
            new Color(255, 223, 0),
            new Color(34, 139, 34),
            new Color(65,105,225),
            new Color(199,21,133)
        };
        return colors[r.nextInt(colors.length)];
    }
}

