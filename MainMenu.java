import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
// GameEngine imports (assuming GameEngine.java is in the same package or correctly imported)
// import java.awt.image.*; // Not used in this menu directly but in GameEngine
// import java.io.*; // Not used in this menu directly but in GameEngine
// import java.util.Stack; // Used by GameEngine
// import java.util.Random; // Used by GameEngine
// import javax.imageio.*; // Used by GameEngine
// import javax.sound.sampled.*; // Used by GameEngine

public class MainMenu extends GameEngine {

    // Enum to manage different menu states
    private enum MenuState {
        MAIN,
        SETTINGS
    }
    private MenuState currentMenuState = MenuState.MAIN;

    // Button regions (using Rectangle2D for easy click detection)
    private Rectangle2D playButtonRect;
    private Rectangle2D settingsButtonRect;
    private Rectangle2D muteButtonRect;

    // Button text
    private final String playButtonText = "Play";
    private final String settingsButtonText = "Settings";
    private String muteButtonDisplayString = "🔊"; // Unicode for speaker high volume

    // Music state
    private AudioClip backgroundMusic;
    private boolean isMuted = false;

    // Constants for button styling and layout
    private final int BUTTON_WIDTH = 220;
    private final int BUTTON_HEIGHT = 60;
    private final int MUTE_BUTTON_SIZE = 45;
    private final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 28);
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 70);
    private final Font MUTE_BUTTON_FONT = new Font("Arial", Font.PLAIN, 28); // Font for Unicode speaker icons
    private final Font INFO_FONT = new Font("Arial", Font.PLAIN, 18);

    /**
     * Constructor for the MainMenu.
     * @param width The width of the game window.
     * @param height The height of the game window.
     */
    public MainMenu(int width, int height) {
        super(width, height); // Calls the GameEngine constructor
    }

    /**
     * Initializes the main menu.
     * This is called once by the GameEngine after the window is set up.
     */
    @Override
    public void init() {
        // Set the window title
        if (mFrame != null) { // mFrame is initialized in GameEngine's setupWindow
            mFrame.setTitle("KIWI LORE - Main Menu");
        }

        // Calculate positions for main menu buttons
        int centerX = width() / 2;
        int playButtonY = height() / 2 - BUTTON_HEIGHT - 5; // Position Play button slightly above center
        int settingsButtonY = height() / 2 + BUTTON_HEIGHT / 2 - 5; // Position Settings button slightly below center

        playButtonRect = new Rectangle2D.Double(centerX - BUTTON_WIDTH / 2, playButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButtonRect = new Rectangle2D.Double(centerX - BUTTON_WIDTH / 2, settingsButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Position mute button in the bottom-right corner
        muteButtonRect = new Rectangle2D.Double(width() - MUTE_BUTTON_SIZE - 20, height() - MUTE_BUTTON_SIZE - 20, MUTE_BUTTON_SIZE, MUTE_BUTTON_SIZE);
        updateMuteButtonText(); // Set initial mute button text

        // Load background music
        // IMPORTANT: Create a 'sounds' folder next to your .class files (or in JAR root)
        // and place 'background_music.wav' in it.
        backgroundMusic = loadAudio("sounds/background_music.wav");

        if (backgroundMusic != null) {
            if (!isMuted) {
                startAudioLoop(backgroundMusic, -10.0f); // Start music with a volume of -10dB
            }
        } else {
            System.out.println("Warning: 'sounds/background_music.wav' not found. Background music will not play.");
        }
    }

    /**
     * Updates the game state. Called repeatedly by the GameEngine.
     * @param dt Time delta since the last update in seconds.
     */
    @Override
    public void update(double dt) {
        // For this static menu, no per-frame logic updates are needed here.
        // Animations or timed events would go here.
    }

    /**
     * Paints the menu components. Called repeatedly by the GameEngine.
     */
    @Override
    public void paintComponent() {
        // Set background color (e.g., a light sky blue)
        changeBackgroundColor(new Color(135, 206, 250));
        clearBackground(width(), height()); // Clear the screen with the background color

        // Draw Game Title "KIWI LORE"
        changeColor(new Color(0, 100, 0)); // Dark green for title
        String gameTitle = "KIWI LORE";
        // Estimate title width for centering (GameEngine doesn't provide FontMetrics easily)
        // This is an approximation, adjust if text is not centered.
        int titleApproxWidth = gameTitle.length() * 35; // Rough estimate
        drawBoldText((width() - titleApproxWidth) / 2.0, 120, gameTitle, TITLE_FONT.getName(), TITLE_FONT.getSize());

        // Handle drawing based on the current menu state
        if (currentMenuState == MenuState.MAIN) {
            paintMainMenu();
        } else if (currentMenuState == MenuState.SETTINGS) {
            paintSettingsMenu();
        }

        // Draw Mute Button (always visible)
        paintMuteButton();
    }

    /**
     * Helper method to paint the main menu elements.
     */
    private void paintMainMenu() {
        // Draw Play Button
        changeColor(new Color(34, 139, 34)); // ForestGreen
        drawSolidRectangle(playButtonRect.getX(), playButtonRect.getY(), playButtonRect.getWidth(), playButtonRect.getHeight());
        changeColor(white);
        // Adjust text position for centering (approximate)
        drawText(playButtonRect.getX() + 75, playButtonRect.getY() + 40, playButtonText, BUTTON_FONT.getName(), BUTTON_FONT.getSize());

        // Draw Settings Button
        changeColor(new Color(255, 165, 0)); // Orange
        drawSolidRectangle(settingsButtonRect.getX(), settingsButtonRect.getY(), settingsButtonRect.getWidth(), settingsButtonRect.getHeight());
        changeColor(white);
        // Adjust text position for centering (approximate)
        drawText(settingsButtonRect.getX() + 50, settingsButtonRect.getY() + 40, settingsButtonText, BUTTON_FONT.getName(), BUTTON_FONT.getSize());
    }

    /**
     * Helper method to paint the settings menu elements.
     */
    private void paintSettingsMenu() {
        changeColor(black);
        drawBoldText(width() / 2.0 - 120, height() / 2.0 - 60, "Settings", "Arial", 40);
        drawText(width() / 2.0 - 150, height() / 2.0, "(Game settings would go here)", INFO_FONT.getName(), INFO_FONT.getSize());
        drawText(width() / 2.0 - 120, height() / 2.0 + 40, "Press ESC to go back", INFO_FONT.getName(), INFO_FONT.getSize());
        // You could add actual settings controls here (e.g., volume sliders, key bindings)
    }

    /**
     * Helper method to paint the mute button.
     */
    private void paintMuteButton() {
        changeColor(isMuted ? new Color(150, 150, 150) : new Color(100, 100, 100)); // Darker/Lighter Gray
        drawSolidRectangle(muteButtonRect.getX(), muteButtonRect.getY(), muteButtonRect.getWidth(), muteButtonRect.getHeight());
        changeColor(white);
        // Adjust text position for centering Unicode icon (approximate)
        drawText(muteButtonRect.getX() + 9, muteButtonRect.getY() + 33, muteButtonDisplayString, MUTE_BUTTON_FONT.getName(), MUTE_BUTTON_FONT.getSize());
    }

    /**
     * Updates the mute button's text based on the isMuted state.
     */
    private void updateMuteButtonText() {
        muteButtonDisplayString = isMuted ? "🔇" : "🔊"; // Unicode: Muted Speaker, Speaker High Volume
    }

    /**
     * Handles mouse press events.
     * @param e The MouseEvent.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Point clickPoint = e.getPoint();

        // Mute button is always active
        if (muteButtonRect.contains(clickPoint)) {
            isMuted = !isMuted;
            updateMuteButtonText();
            if (backgroundMusic != null) {
                if (isMuted) {
                    stopAudioLoop(backgroundMusic);
                    System.out.println("Background music muted.");
                } else {
                    startAudioLoop(backgroundMusic, -10.0f); // Restore music with volume
                    System.out.println("Background music unmuted.");
                }
            }
            return; // Consume click if it's on mute button
        }

        // Handle clicks based on current menu state
        if (currentMenuState == MenuState.MAIN) {
            if (playButtonRect.contains(clickPoint)) {
                System.out.println("Play button clicked! Starting game (placeholder)...");
                // TODO: Transition to the actual game state/screen
                // For now, you could exit, or change to a different GameEngine instance.
                // Example: mFrame.dispose(); // Closes the menu window
                // GameEngine.createGame(new YourActualGameClass(), 60);
            } else if (settingsButtonRect.contains(clickPoint)) {
                System.out.println("Settings button clicked!");
                currentMenuState = MenuState.SETTINGS;
            }
        } else if (currentMenuState == MenuState.SETTINGS) {
            // In settings, you might have other buttons (e.g., a "Back" button)
            // For now, only ESC key handles going back.
        }
    }

    /**
     * Handles key press events.
     * @param e The KeyEvent.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (currentMenuState == MenuState.SETTINGS) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                currentMenuState = MenuState.MAIN;
                System.out.println("Exited settings menu via ESC key.");
            }
        }
    }

    // Other mouse listener methods (can be left empty if not used)
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}

    // Key listener methods (can be left empty if not used)
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}


    /**
     * Main method to launch the Main Menu.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Create an instance of the MainMenu
        MainMenu gameMenu = new MainMenu(800, 600); // Window size 800x600 pixels

        // Use the GameEngine's static method to create and start the game
        GameEngine.createGame(gameMenu, 60); // Target 60 frames per second
    }
}
