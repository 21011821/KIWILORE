import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * Represents the main game screen for Kiwi Lore.
 * This class is instantiated after a game mode is selected from the MainMenu.
 */
public class KiwiLoreGame extends GameEngine {

    // Enum to represent the game mode
    public enum GameMode {
        STORY,
        MULTIPLAYER
    }

    private GameMode currentMode;
    private String gameModeText;

    // Fonts
    private final Font INFO_FONT_LARGE = new Font("Arial", Font.BOLD, 30);
    private final Font INFO_FONT_SMALL = new Font("Arial", Font.PLAIN, 20);

    
    public KiwiLoreGame(int width, int height, GameMode mode) {
        super(width, height); // Call GameEngine constructor
        this.currentMode = mode;
    }

    /**
     * Initializes the game.
     * This is called once by the GameEngine after the window is set up.
     */
    @Override
    public void init() {
        if (mFrame != null) {
            mFrame.setTitle("KIWI LORE - Playing!");
        }

        // Set text based on game mode
        if (currentMode == GameMode.STORY) {
            gameModeText = "Story Mode";
        } else if (currentMode == GameMode.MULTIPLAYER) {
            gameModeText = "Multiplayer Mode";
        } else {
            gameModeText = "Unknown Mode"; // Fallback
        }

        System.out.println("KiwiLoreGame initialized in " + gameModeText);
        // TODO: Initialize game assets, player, levels, etc. based on the currentMode
    }

    /**
     * Updates the game state. Called repeatedly by the GameEngine.
     * @param dt Time delta since the last update in seconds.
     */
    @Override
    public void update(double dt) {
        // TODO: Implement game logic updates (player movement, AI, physics, etc.)
        // For example:
        // player.update(dt);
        // for (Enemy enemy : enemies) {
        //     enemy.update(dt);
        // }
        // checkCollisions();
    }

    /**
     * Paints the game components. Called repeatedly by the GameEngine.
     */
    @Override
    public void paintComponent() {
        // Set a different background for the game
        changeBackgroundColor(new Color(60, 179, 113)); // MediumSeaGreen
        clearBackground(width(), height());

        // Display the current game mode
        changeColor(white);
        String playingText = "Now Playing: " + gameModeText;
        FontMetrics fmLarge = mGraphics.getFontMetrics(INFO_FONT_LARGE);
        int playingTextWidth = fmLarge.stringWidth(playingText);
        drawBoldText((width() - playingTextWidth) / 2.0, height() / 2.0 - 20, playingText, INFO_FONT_LARGE.getName(), INFO_FONT_LARGE.getSize());

        // Placeholder text for game content
        String placeholderText = "(Actual game content would be here!)";
        FontMetrics fmSmall = mGraphics.getFontMetrics(INFO_FONT_SMALL);
        int placeholderTextWidth = fmSmall.stringWidth(placeholderText);
        drawText((width() - placeholderTextWidth) / 2.0, height() / 2.0 + 30, placeholderText, INFO_FONT_SMALL.getName(), INFO_FONT_SMALL.getSize());

        String escText = "Press ESC to (placeholder) return to Menu";
         int escTextWidth = fmSmall.stringWidth(escText);
        drawText((width() - escTextWidth) / 2.0, height() - 50, escText, INFO_FONT_SMALL.getName(), INFO_FONT_SMALL.getSize());

        // TODO: Draw game characters, level, HUD, etc.
        // For example:
        // level.draw(mGraphics);
        // player.draw(mGraphics);
        // for (Enemy enemy : enemies) {
        //    enemy.draw(mGraphics);
        // }
        // hud.draw(mGraphics);
    }

    /**
     * Handles key press events.
     * @param e The KeyEvent.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("ESC pressed in game. Placeholder for returning to menu.");
            // TODO: Implement logic to return to the main menu.
            // This would involve disposing the current game frame and creating a new MainMenu instance.
            // For now, we can just close the game.
            if (mFrame != null) {
                mFrame.dispose(); // Close the current game window
            }
            // To return to menu properly:
            // MainMenu gameMenu = new MainMenu(width(), height()); // or a saved initial width/height
            // GameEngine.createGame(gameMenu, 60);
            // System.exit(0); // Or a more graceful shutdown
        }

        // TODO: Handle other game-specific key presses (movement, actions, etc.)
        // For example:
        // if (e.getKeyCode() == KeyEvent.VK_W) { player.moveForward(); }
    }

    // Other listener methods (can be left empty or implemented as needed)
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}

    // Main method for testing KiwiLoreGame directly (optional)
    /*
    public static void main(String[] args) {
        KiwiLoreGame game = new KiwiLoreGame(800, 600, GameMode.STORY);
        GameEngine.createGame(game, 60);
    }
    */
}
