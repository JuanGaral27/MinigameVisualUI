package src.model;

import java.awt.*;

public class player {
    public int x, y, w, h;
    public int speed = 7;
    public boolean movingLeft = false;
    public boolean movingRight = false;
    public int panelWidth;

    public player(int x, int y, int w, int h, int panelWidth) {
        this.x = x; this.y = y; this.w = w; this.h = h;
        this.panelWidth = panelWidth;
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
