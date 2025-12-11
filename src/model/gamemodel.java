package model;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Modelo del juego: contiene estado y lógica (sin Swing).
 * Incluye las clases Player y Falling como parte del modelo para mantener
 * todo el estado en un solo fichero.
 */
public class gamemodel {
    public final int width;
    public final int height;

    public Player player;
    public List<Falling> items;

    public int score;
    public int lives;
    public boolean running;
    public boolean paused;

    private int spawnCounter;
    private int spawnRate;

    public gamemodel(int width, int height) {
        this.width = width;
        this.height = height;
        init();
    }

    public void init() {
        player = new Player(width / 2 - 40, height - 70, 80, 40, width);
        items = new ArrayList<Falling>();
        score = 0;
        lives = 3;
        running = false;
        paused = false;
        spawnCounter = 0;
        spawnRate = 45;
    }

    /**
     * Avanza una frame del juego. Debe llamarse desde el hilo de la UI (Timer).
     */
    public void updateFrame() {
        if (!running || paused) return;

        player.update();

        spawnCounter++;
        if (spawnCounter >= spawnRate) {
            spawnCounter = 0;
            spawnRate = Math.max(15, 45 - score / 5);
            items.add(Falling.random(width));
        }

        Iterator<Falling> it = items.iterator();
        while (it.hasNext()) {
            Falling f = it.next();
            f.update();

            if (f.getBounds().intersects(player.getBounds())) {
                score += f.getScoreValue();
                it.remove();
                continue;
            }

            if (f.y > height) {
                lives--;
                it.remove();
            }
        }
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    /**
     * Inserta una fruta especial (por ejemplo cuando el usuario pulsa SPACE).
     */
    public void spawnSpecialFruit() {
        // fruta en el centro superior con más velocidad y mayor valor
        items.add(new Falling(width/2 - 12, -30, 24, 24, 6, 2, Color.MAGENTA));
    }

    /* ---------------- Player (modelo) ---------------- */
    public static class Player {
        public int x, y, w, h;
        public int speed = 7;
        public boolean movingLeft = false;
        public boolean movingRight = false;
        public int panelWidth;

        public Player(int x, int y, int w, int h, int panelWidth) {
            this.x = x; this.y = y; this.w = w; this.h = h; this.panelWidth = panelWidth;
        }

        public void update() {
            if (movingLeft) x -= speed;
            if (movingRight) x += speed;

            if (x < 8) x = 8;
            if (x + w > panelWidth - 8) x = panelWidth - 8 - w;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, w, h);
        }
    }

    /* ---------------- Falling (modelo) ---------------- */
    public static class Falling {
        public int x, y, w, h;
        public int vy;
        public int scoreValue;
        public Color color;

        public Falling(int x, int y, int w, int h, int vy, int scoreValue, Color color) {
            this.x = x; this.y = y; this.w = w; this.h = h;
            this.vy = vy; this.scoreValue = scoreValue; this.color = color;
        }

        public void update() {
            y += vy;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, w, h);
        }

        public int getScoreValue() {
            return scoreValue;
        }

        public static Falling random(int panelWidth) {
            Random r = new Random();
            int size = r.nextInt(14) + 20; // 20..33
            int x = r.nextInt(Math.max(1, panelWidth - size - 16)) + 8;
            int speed = r.nextInt(3) + 2; // 2..4
            int val = (speed >= 4) ? 2 : 1;
            Color color = randomColor(r);
            return new Falling(x, -size, size, size, speed, val, color);
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
}
