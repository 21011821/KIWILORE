import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class KiwiLoreGame extends GameEngine {

    public enum GameMode {
        STORY,
        MULTIPLAYER
    }

    private GameMode currentMode;
    private String gameModeText;

    private final Font INFO_FONT_LARGE = new Font("Arial", Font.BOLD, 30);
    private final Font INFO_FONT_SMALL = new Font("Arial", Font.PLAIN, 20);

    Image sheet;
    Image[] frames;
    int currentFrame;
    double animTime;
    double frameDuration = 0.1;

    int spriteX;
    int spriteY;
    int spriteSpeed = 100;
    boolean moving = false;
    boolean movingRight = false;

    int spriteWidth = 32;
    int spriteHeight = 32;

    double gravity = 500;
    double verticalVelocity = 0;
    int groundY;

    Image backgroundImage;
    int backgroundX = 0;
    int backgroundScrollSpeed = 50;

    int backgroundWidth; // Width of the background image
    int backgroundHeight;

    public KiwiLoreGame(int width, int height, GameMode mode) {
        super(width, height);
        this.currentMode = mode;
    }

    @Override
    public void init() {
        if (mFrame != null) {
            mFrame.setTitle("KIWI LORE - Playing!");
        }

        if (currentMode == GameMode.STORY) {
            gameModeText = "Story Mode";
        } else if (currentMode == GameMode.MULTIPLAYER) {
            gameModeText = "Multiplayer Mode";
        } else {
            gameModeText = "Unknown Mode";
        }

        System.out.println("KiwiLoreGame initialized in " + gameModeText);

        // = loadImage("KiwiSheet.png");
        /*frames = new Image[16];
        for (int iy = 0; iy < 4; iy++) {
            for (int ix = 0; ix < 4; ix++) {
                frames[iy * 4 + ix] = subImage(sheet, ix * 250, iy * 250, 250, 250);
            }
        }*/
        sheet = loadImage("sp.png");
        frames = new Image[16];
        for (int iy = 0; iy < 4; iy++) {
            for (int ix = 0; ix < 4; ix++) {
                frames[iy * 4 + ix] = subImage(sheet, ix * 250, iy * 240, 250, 225);
            }
        }

        animTime = 0;
        currentFrame = 0;

        spriteX = width() / 2 - spriteWidth / 2;
        groundY = height() - spriteHeight;
        spriteY = groundY;

        backgroundImage = loadImage("background.png");
        if (backgroundImage == null) {
            System.err.println("Error: Could not load background image!");
            backgroundWidth = width(); // Fallback if image fails to load
        } else {
            backgroundWidth = backgroundImage.getWidth(null);
            backgroundHeight = backgroundImage.getHeight(null);
        }
    }

    @Override
    public void update(double dt) {
        if (moving) {
            animTime += dt;
            if (animTime >= frameDuration) {
                currentFrame = (currentFrame + 1) % 15;
                animTime -= frameDuration;
            }
        } else {
            currentFrame = 0;
            animTime = 0;
        }

        verticalVelocity += gravity * dt;
        spriteY += verticalVelocity * dt;

        if (spriteY > groundY) {
            spriteY = groundY;
            verticalVelocity = 0;
        }

        boolean backgroundAtLeftEdge = backgroundX >= 0;
        boolean backgroundAtRightEdge = backgroundX <= -(backgroundWidth - width());
        boolean isCentered = Math.abs(spriteX - (width() / 2 - spriteWidth / 2)) < 1e-5;

        // Handle right movement
        if (moving && movingRight) {
            if (!backgroundAtRightEdge) {
                backgroundX -= backgroundScrollSpeed * dt;
                if (!isCentered) {
                    spriteX = width() / 2 - spriteWidth / 2; // Force center if not there
                }
            }
            // Apply sprite movement even if background is at edge or scrolling
            spriteX += spriteSpeed * dt;
            if (backgroundAtRightEdge && spriteX > width() - spriteWidth) {
                spriteX = width() - spriteWidth;
            } else if (!backgroundAtRightEdge && isCentered) {
                spriteX = width() / 2 - spriteWidth / 2; // Maintain center
            }
        }

        // Handle left movement
        if (moving && !movingRight) {
            if (!backgroundAtLeftEdge) {
                backgroundX += backgroundScrollSpeed * dt;
                if (!isCentered) {
                    spriteX = width() / 2 - spriteWidth / 2; // Force center if not there
                }
            }
            // Apply sprite movement even if background is at edge or scrolling
            spriteX -= spriteSpeed * dt;
            if (backgroundAtLeftEdge && spriteX < 0) {
                spriteX = 0;
            } else if (!backgroundAtLeftEdge && isCentered) {
                spriteX = width() / 2 - spriteWidth / 2; // Maintain center
            }
        }

        // Keep background within bounds (for safety)
        if (backgroundX > 0) backgroundX = 0;
        if (backgroundX < -(backgroundWidth - width())) backgroundX = -(backgroundWidth - width());
    }


    @Override
    public void paintComponent() {
        // Draw the background image within the panel bounds
        if (backgroundImage != null) {
            drawImage(backgroundImage, backgroundX, -400);
        } else {
            changeBackgroundColor(new Color(60, 179, 113));
            clearBackground(width(), height());
        }

        changeColor(white);
        String playingText = "Now Playing: " + gameModeText;
        FontMetrics fmLarge = mGraphics.getFontMetrics(INFO_FONT_LARGE);
        int playingTextWidth = fmLarge.stringWidth(playingText);
        drawBoldText((width() - playingTextWidth) / 2.0, 30, playingText, INFO_FONT_LARGE.getName(), INFO_FONT_LARGE.getSize());

        Image currentImage = frames[currentFrame];
        BufferedImage bufferedImage;
        AffineTransform tx;
        BufferedImage flippedImage;
        Graphics2D g2d;

        if (movingRight) {
            bufferedImage = (BufferedImage) currentImage;
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-bufferedImage.getWidth(null), 0);
            flippedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            g2d = flippedImage.createGraphics();
            g2d.drawImage(bufferedImage, tx, null);
            g2d.dispose();
            drawImage(flippedImage, spriteX, spriteY, spriteWidth, spriteHeight);
        } else {
            drawImage(currentImage, spriteX, spriteY, spriteWidth, spriteHeight);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE) {
            System.out.println("ESC pressed in game. Placeholder for returning to menu.");
            if (mFrame != null) {
                mFrame.dispose();
            }
        } else if (keyCode == KeyEvent.VK_LEFT) {
            moving = true;
            movingRight = false;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            moving = true;
            movingRight = true;
        } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_SPACE) {
            if (spriteY == groundY) {
                verticalVelocity = -300;
            }
            moving = true;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            moving = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_SPACE) {
            moving = false;
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        KiwiLoreGame game = new KiwiLoreGame(800, 600, GameMode.STORY);
        GameEngine.createGame(game, 60);
    }
}