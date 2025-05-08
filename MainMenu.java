import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;


public class MainMenu extends GameEngine {

    // Enum to manage different menu states
    private enum MenuState {
        MAIN,
        SETTINGS,
        GAME_MODE_SELECTION
    }
    private MenuState currentMenuState = MenuState.MAIN;

    // Button regions
    private Rectangle2D playButtonRect;
    private Rectangle2D settingsButtonRect;
    private Rectangle2D muteButtonRect;
    private Rectangle2D storyModeButtonRect;
    private Rectangle2D multiplayerButtonRect;

    // Resolution settings
    private Dimension[] availableResolutions;
    private Rectangle2D[] resolutionButtonRects;
    private Dimension currentResolution; // Will store the active resolution

    // Button text
    private final String playButtonText = "Play";
    private final String settingsButtonText = "Settings";
    private final String storyModeButtonText = "Story Mode";
    private final String multiplayerButtonText = "Multiplayer";
    private String muteButtonDisplayString = "🔊";

    // Music state
    private AudioClip backgroundMusic;
    private boolean isMuted = false;

    // Constants for button styling and layout
    private final int BUTTON_WIDTH = 220;
    private final int BUTTON_HEIGHT = 60;
    private final int MUTE_BUTTON_SIZE = 45;
    private final int RESOLUTION_BUTTON_WIDTH = 200;
    private final int RESOLUTION_BUTTON_HEIGHT = 40;
    private final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 28);
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 70);
    private final Font MUTE_BUTTON_FONT = new Font("Arial", Font.PLAIN, 28);
    private final Font INFO_FONT = new Font("Arial", Font.PLAIN, 18);
    private final Font SUB_HEADER_FONT = new Font("Arial", Font.BOLD, 40);
    private final Font RESOLUTION_FONT = new Font("Arial", Font.PLAIN, 18);


    /**
     * Constructor for the MainMenu.
     * @param width The width of the game window.
     * @param height The height of the game window.
     */
    public MainMenu(int width, int height) {
        super(width, height); // Calls the GameEngine constructor
        currentResolution = new Dimension(width, height); // Initialize currentResolution
    }

    /**
     * Initializes the main menu.
     */
    @Override
    public void init() {
        if (mFrame != null) {
            mFrame.setTitle("KIWI LORE - Main Menu");
        }

        // Define available resolutions
        availableResolutions = new Dimension[] {
            new Dimension(800, 600),
            new Dimension(1024, 768),
            new Dimension(1280, 720)
        };
        // Ensure initial resolution is in the list or add it (optional, for highlighting)
        boolean initialResFound = false;
        for (Dimension res : availableResolutions) {
            if (res.width == width() && res.height == height()) {
                initialResFound = true;
                currentResolution = res; // Make sure currentResolution is one of the objects from the array for == comparison
                break;
            }
        }
        // If initial resolution wasn't in the presets, you might want to handle it,
        // for now, currentResolution is already set in constructor.

        resolutionButtonRects = new Rectangle2D[availableResolutions.length];
        recalculateButtonPositions(); // Calculate all button positions

        updateMuteButtonText();

        backgroundMusic = loadAudio("sounds/background_music.wav");
        if (backgroundMusic != null) {
            if (!isMuted) {
                startAudioLoop(backgroundMusic, -10.0f);
            }
        } else {
            System.out.println("Warning: 'sounds/background_music.wav' not found. Background music will not play.");
        }
    }

    /**
     * Recalculates positions of all UI elements.
     * Call this after window size changes if elements are not purely relative.
     */
    private void recalculateButtonPositions() {
        int centerX = width() / 2;

        // Main Menu Buttons
        int playButtonY = height() / 2 - BUTTON_HEIGHT - 20;
        int settingsButtonY = height() / 2 + BUTTON_HEIGHT / 2 + 10;
        playButtonRect = new Rectangle2D.Double(centerX - BUTTON_WIDTH / 2, playButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButtonRect = new Rectangle2D.Double(centerX - BUTTON_WIDTH / 2, settingsButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Game Mode Selection Buttons
        int storyModeButtonY = height() / 2 - BUTTON_HEIGHT - 5;
        int multiplayerButtonY = height() / 2 + BUTTON_HEIGHT / 2 - 5;
        storyModeButtonRect = new Rectangle2D.Double(centerX - BUTTON_WIDTH / 2, storyModeButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        multiplayerButtonRect = new Rectangle2D.Double(centerX - BUTTON_WIDTH / 2, multiplayerButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Mute Button
        muteButtonRect = new Rectangle2D.Double(width() - MUTE_BUTTON_SIZE - 20, height() - MUTE_BUTTON_SIZE - 20, MUTE_BUTTON_SIZE, MUTE_BUTTON_SIZE);

        // Resolution Buttons (in settings menu)
        int firstResButtonY = height() / 2 - 30; // Starting Y for the first resolution button
        for (int i = 0; i < availableResolutions.length; i++) {
            resolutionButtonRects[i] = new Rectangle2D.Double(
                centerX - RESOLUTION_BUTTON_WIDTH / 2,
                firstResButtonY + i * (RESOLUTION_BUTTON_HEIGHT + 10), // 10px spacing
                RESOLUTION_BUTTON_WIDTH,
                RESOLUTION_BUTTON_HEIGHT
            );
        }
    }


    /**
     * Updates the game state.
     * @param dt Time delta since the last update.
     */
    @Override
    public void update(double dt) {
        // No dynamic updates needed for this menu currently.
    }

    /**
     * Paints the menu components.
     */
    @Override
    public void paintComponent() {
        changeBackgroundColor(new Color(135, 206, 250));
        clearBackground(width(), height());

        changeColor(new Color(0, 100, 0));
        String gameTitle = "KIWI LORE";
        FontMetrics titleFm = mGraphics.getFontMetrics(TITLE_FONT); // Get FontMetrics for accurate centering
        int titleWidth = titleFm.stringWidth(gameTitle);
        drawBoldText((width() - titleWidth) / 2.0, 100, gameTitle, TITLE_FONT.getName(), TITLE_FONT.getSize());

        if (currentMenuState == MenuState.MAIN) {
            paintMainMenu();
        } else if (currentMenuState == MenuState.SETTINGS) {
            paintSettingsMenu();
        } else if (currentMenuState == MenuState.GAME_MODE_SELECTION) {
            paintGameModeSelectionMenu();
        }

        paintMuteButton();
    }

    private void paintMainMenu() {
        changeColor(new Color(34, 139, 34));
        drawSolidRectangle(playButtonRect.getX(), playButtonRect.getY(), playButtonRect.getWidth(), playButtonRect.getHeight());
        changeColor(white);
        drawCenteredText(playButtonText, playButtonRect, BUTTON_FONT);

        changeColor(new Color(255, 165, 0));
        drawSolidRectangle(settingsButtonRect.getX(), settingsButtonRect.getY(), settingsButtonRect.getWidth(), settingsButtonRect.getHeight());
        changeColor(white);
        drawCenteredText(settingsButtonText, settingsButtonRect, BUTTON_FONT);
    }

    private void paintGameModeSelectionMenu() {
        changeColor(black);
        String selectModeHeader = "Select Game Mode";
        FontMetrics headerFm = mGraphics.getFontMetrics(SUB_HEADER_FONT);
        int headerWidth = headerFm.stringWidth(selectModeHeader);
        drawBoldText((width() - headerWidth) / 2.0, height() / 2.0 - BUTTON_HEIGHT - 50, selectModeHeader, SUB_HEADER_FONT.getName(), SUB_HEADER_FONT.getSize());

        changeColor(new Color(70, 130, 180));
        drawSolidRectangle(storyModeButtonRect.getX(), storyModeButtonRect.getY(), storyModeButtonRect.getWidth(), storyModeButtonRect.getHeight());
        changeColor(white);
        drawCenteredText(storyModeButtonText, storyModeButtonRect, BUTTON_FONT);

        changeColor(new Color(220, 20, 60));
        drawSolidRectangle(multiplayerButtonRect.getX(), multiplayerButtonRect.getY(), multiplayerButtonRect.getWidth(), multiplayerButtonRect.getHeight());
        changeColor(white);
        drawCenteredText(multiplayerButtonText, multiplayerButtonRect, BUTTON_FONT);

        changeColor(black);
        String escText = "Press ESC to go back";
        FontMetrics infoFm = mGraphics.getFontMetrics(INFO_FONT);
        int escTextWidth = infoFm.stringWidth(escText);
        drawText((width() - escTextWidth) / 2.0, height() - 60, escText, INFO_FONT.getName(), INFO_FONT.getSize());
    }

    private void paintSettingsMenu() {
        changeColor(black);
        String settingsHeader = "Settings";
        FontMetrics headerFm = mGraphics.getFontMetrics(SUB_HEADER_FONT);
        int headerWidth = headerFm.stringWidth(settingsHeader);
        drawBoldText((width() - headerWidth) / 2.0, height() / 2.0 - 160, settingsHeader, SUB_HEADER_FONT.getName(), SUB_HEADER_FONT.getSize());

        // Draw Resolution Options
        String resolutionLabel = "Resolution:";
        FontMetrics resLabelFm = mGraphics.getFontMetrics(BUTTON_FONT); // A bit larger font for the label
        int resLabelWidth = resLabelFm.stringWidth(resolutionLabel);
        drawBoldText((width() - resLabelWidth) / 2.0, height() / 2.0 - 100, resolutionLabel, BUTTON_FONT.getName(), BUTTON_FONT.getSize());


        for (int i = 0; i < availableResolutions.length; i++) {
            Dimension res = availableResolutions[i];
            Rectangle2D btnRect = resolutionButtonRects[i];
            String resText = res.width + " x " + res.height;

            // Highlight current resolution
            if (res.equals(currentResolution)) {
                changeColor(new Color(100, 180, 100)); // A green highlight for selected
            } else {
                changeColor(new Color(180, 180, 220)); // Light purple for others
            }
            drawSolidRectangle(btnRect.getX(), btnRect.getY(), btnRect.getWidth(), btnRect.getHeight());
            changeColor(black);
            drawCenteredText(resText, btnRect, RESOLUTION_FONT);
        }

        changeColor(black);
        String escText = "Press ESC to go back";
        FontMetrics infoFm = mGraphics.getFontMetrics(INFO_FONT);
        int escTextWidth = infoFm.stringWidth(escText);
        drawText((width() - escTextWidth) / 2.0, height() - 60, escText, INFO_FONT.getName(), INFO_FONT.getSize());
    }

    private void paintMuteButton() {
        changeColor(isMuted ? new Color(150, 150, 150) : new Color(100, 100, 100));
        drawSolidRectangle(muteButtonRect.getX(), muteButtonRect.getY(), muteButtonRect.getWidth(), muteButtonRect.getHeight());
        changeColor(white);
        drawCenteredText(muteButtonDisplayString, muteButtonRect, MUTE_BUTTON_FONT);
    }

    /**
     * Utility method to draw text centered within a rectangle.
     * @param text The string to draw.
     * @param rect The rectangle to center the text in.
     * @param font The font to use for the text.
     */
    private void drawCenteredText(String text, Rectangle2D rect, Font font) {
        if (mGraphics == null) return; // Should not happen if paintComponent is called
        FontMetrics fm = mGraphics.getFontMetrics(font);
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent() - fm.getDescent(); // More accurate height

        double x = rect.getX() + (rect.getWidth() - textWidth) / 2;
        double y = rect.getY() + (rect.getHeight() - textHeight) / 2 + fm.getAscent();

        mGraphics.setFont(font); // Ensure the correct font is set in mGraphics
        mGraphics.drawString(text, (int)x, (int)y);
    }


    private void updateMuteButtonText() {
        muteButtonDisplayString = isMuted ? "🔇" : "🔊";
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point clickPoint = e.getPoint();

        if (muteButtonRect.contains(clickPoint)) {
            isMuted = !isMuted;
            updateMuteButtonText();
            if (backgroundMusic != null) {
                if (isMuted) {
                    stopAudioLoop(backgroundMusic);
                    System.out.println("Background music muted.");
                } else {
                    startAudioLoop(backgroundMusic, -10.0f);
                    System.out.println("Background music unmuted.");
                }
            }
            return;
        }

        if (currentMenuState == MenuState.MAIN) {
            if (playButtonRect.contains(clickPoint)) {
                System.out.println("Play button clicked! Transitioning to game mode selection...");
                currentMenuState = MenuState.GAME_MODE_SELECTION;
            } else if (settingsButtonRect.contains(clickPoint)) {
                System.out.println("Settings button clicked!");
                currentMenuState = MenuState.SETTINGS;
                recalculateButtonPositions(); // Recalculate positions when entering settings, in case window size changed
            }
        } else if (currentMenuState == MenuState.GAME_MODE_SELECTION) {
            if (storyModeButtonRect.contains(clickPoint)) {
                System.out.println("Story Mode button clicked! (Placeholder - starting story mode...)");
                // TODO: Transition to Story Mode
            } else if (multiplayerButtonRect.contains(clickPoint)) {
                System.out.println("Multiplayer button clicked! (Placeholder - starting multiplayer...)");
                // TODO: Transition to Multiplayer Mode
            }
        } else if (currentMenuState == MenuState.SETTINGS) {
            for (int i = 0; i < resolutionButtonRects.length; i++) {
                if (resolutionButtonRects[i].contains(clickPoint)) {
                    Dimension selectedRes = availableResolutions[i];
                    if (!selectedRes.equals(currentResolution)) {
                        System.out.println("Resolution changed to: " + selectedRes.width + "x" + selectedRes.height);
                        currentResolution = selectedRes;
                        // GameEngine's setWindowSize will handle the actual resize
                        // and trigger a repaint via pack()
                        setWindowSize(selectedRes.width, selectedRes.height);
                        // Button positions will be updated in the next paint cycle
                        // because paintComponent calls recalculateButtonPositions implicitly
                        // via width() and height() being updated by GameEngine.
                        // However, to ensure immediate update of settings screen specific buttons:
                        recalculateButtonPositions();
                    }
                    return; // Consume click
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (currentMenuState == MenuState.SETTINGS || currentMenuState == MenuState.GAME_MODE_SELECTION) {
                currentMenuState = MenuState.MAIN;
                System.out.println("Exited to main menu via ESC key.");
                recalculateButtonPositions(); // Recalculate for main menu layout
            }
        }
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        MainMenu gameMenu = new MainMenu(800, 600);
        GameEngine.createGame(gameMenu, 60);
    }
}
