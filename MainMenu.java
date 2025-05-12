import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
// GameEngine imports (assuming GameEngine.java is in the same package or correctly imported)

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
    private Dimension currentResolution;

    // Button text
    private final String playButtonText = "Play";
    private final String settingsButtonText = "Settings";
    private final String storyModeButtonText = "Story Mode";
    private final String multiplayerButtonText = "Multiplayer";
    private String muteButtonDisplayString = "🔊";

    // Music state
    private AudioClip backgroundMusic;
    private boolean isMuted = false;

    // Background Image
    private Image backgroundImage; // Added for background image

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
        super(width, height);
        currentResolution = new Dimension(width, height);
    }

    /**
     * Initializes the main menu.
     */
    @Override
    public void init() {
        if (mFrame != null) {
            mFrame.setTitle("KIWI LORE - Main Menu");
        }

        // Load background image
        // IMPORTANT: Place 'background.png' in the root directory of your compiled classes.
        backgroundImage = loadImage("background.png");
        if (backgroundImage == null) {
            System.out.println("Warning: 'background.png' not found. Will use solid color background.");
            // The GameEngine's loadImage already prints an error and exits if critical.
            // If it were to return null without exiting, we'd need a fallback here.
        }


        availableResolutions = new Dimension[] {
                new Dimension(800, 600),
                new Dimension(1024, 768),
                new Dimension(1280, 720)
        };
        boolean initialResFound = false;
        for (Dimension res : availableResolutions) {
            if (res.width == width() && res.height == height()) {
                initialResFound = true;
                currentResolution = res;
                break;
            }
        }
        if (!initialResFound) {
            // currentResolution keeps its constructor-set value.
        }

        resolutionButtonRects = new Rectangle2D[availableResolutions.length];
        recalculateButtonPositions();

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
     */
    private void recalculateButtonPositions() {
        int centerX = width() / 2;

        int playButtonY = height() / 2 - BUTTON_HEIGHT - 20;
        int settingsButtonY = height() / 2 + BUTTON_HEIGHT / 2 + 10;
        playButtonRect = new Rectangle2D.Double(centerX - BUTTON_WIDTH / 2, playButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButtonRect = new Rectangle2D.Double(centerX - BUTTON_WIDTH / 2, settingsButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);

        int storyModeButtonY = height() / 2 - BUTTON_HEIGHT - 5;
        int multiplayerButtonY = height() / 2 + BUTTON_HEIGHT / 2 - 5;
        storyModeButtonRect = new Rectangle2D.Double(centerX - BUTTON_WIDTH / 2, storyModeButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        multiplayerButtonRect = new Rectangle2D.Double(centerX - BUTTON_WIDTH / 2, multiplayerButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);

        muteButtonRect = new Rectangle2D.Double(width() - MUTE_BUTTON_SIZE - 20, height() - MUTE_BUTTON_SIZE - 20, MUTE_BUTTON_SIZE, MUTE_BUTTON_SIZE);

        int firstResButtonY = height() / 2 - 30 + 40;
        if (availableResolutions != null && resolutionButtonRects != null) {
            for (int i = 0; i < availableResolutions.length; i++) {
                resolutionButtonRects[i] = new Rectangle2D.Double(
                        centerX - RESOLUTION_BUTTON_WIDTH / 2,
                        firstResButtonY + i * (RESOLUTION_BUTTON_HEIGHT + 10),
                        RESOLUTION_BUTTON_WIDTH,
                        RESOLUTION_BUTTON_HEIGHT
                );
            }
        }
    }

    /**
     * Updates the game state.
     * @param dt Time delta since the last update.
     */
    @Override
    public void update(double dt) {
        // No dynamic updates needed.
    }

    /**
     * Paints the menu components.
     */
    @Override
    public void paintComponent() {
        if (mGraphics == null) {
            System.err.println("mGraphics is null in MainMenu.paintComponent. Repainting might fail.");
            return;
        }

        // Draw background image if loaded, otherwise a solid color
        if (backgroundImage != null) {
            drawImage(backgroundImage, 0, 0, width(), height()); // Draw stretched to fit
        } else {
            changeBackgroundColor(new Color(135, 206, 250)); // Fallback color
            clearBackground(width(), height());
        }

        // Draw Game Title "KIWI LORE"
        // Set title color to be visible against potentially dark backgrounds
        changeColor(new Color(255, 255, 224)); // Light Yellow, good contrast
        // Add a slight shadow for better readability on complex backgrounds
        String gameTitle = "KIWI LORE";
        FontMetrics titleFm = mGraphics.getFontMetrics(TITLE_FONT);
        int titleWidth = titleFm.stringWidth(gameTitle);
        int titleX = (width() - titleWidth) / 2;
        int titleY = 100;

        // Shadow
        changeColor(new Color(50,50,50,150)); // Dark semi-transparent shadow
        drawBoldText(titleX + 3, titleY + 3, gameTitle, TITLE_FONT.getName(), TITLE_FONT.getSize());
        // Actual Title
        changeColor(new Color(255, 255, 224));
        drawBoldText(titleX, titleY, gameTitle, TITLE_FONT.getName(), TITLE_FONT.getSize());


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
        // Play Button
        changeColor(new Color(34, 139, 34, 220)); // ForestGreen with some transparency
        drawSolidRectangle(playButtonRect.getX(), playButtonRect.getY(), playButtonRect.getWidth(), playButtonRect.getHeight());
        changeColor(white);
        drawCenteredText(playButtonText, playButtonRect, BUTTON_FONT);

        // Settings Button
        changeColor(new Color(255, 165, 0, 220)); // Orange with some transparency
        drawSolidRectangle(settingsButtonRect.getX(), settingsButtonRect.getY(), settingsButtonRect.getWidth(), settingsButtonRect.getHeight());
        changeColor(white);
        drawCenteredText(settingsButtonText, settingsButtonRect, BUTTON_FONT);
    }

    private void paintGameModeSelectionMenu() {
        // Background for text for better readability
        drawTextBackground(width()/2.0 - 200, height() / 2.0 - BUTTON_HEIGHT - 80, 400, 60);

        changeColor(black);
        String selectModeHeader = "Select Game Mode";
        FontMetrics headerFm = mGraphics.getFontMetrics(SUB_HEADER_FONT);
        int headerWidth = headerFm.stringWidth(selectModeHeader);
        drawBoldText((width() - headerWidth) / 2.0, height() / 2.0 - BUTTON_HEIGHT - 50, selectModeHeader, SUB_HEADER_FONT.getName(), SUB_HEADER_FONT.getSize());

        // Story Mode Button
        changeColor(new Color(70, 130, 180, 220)); // SteelBlue with transparency
        drawSolidRectangle(storyModeButtonRect.getX(), storyModeButtonRect.getY(), storyModeButtonRect.getWidth(), storyModeButtonRect.getHeight());
        changeColor(white);
        drawCenteredText(storyModeButtonText, storyModeButtonRect, BUTTON_FONT);

        // Multiplayer Button
        changeColor(new Color(220, 20, 60, 220)); // Crimson with transparency
        drawSolidRectangle(multiplayerButtonRect.getX(), multiplayerButtonRect.getY(), multiplayerButtonRect.getWidth(), multiplayerButtonRect.getHeight());
        changeColor(white);
        drawCenteredText(multiplayerButtonText, multiplayerButtonRect, BUTTON_FONT);

        drawTextBackground(width()/2.0 - 150, height() - 80, 300, 40);
        changeColor(black);
        String escText = "Press ESC to go back";
        FontMetrics infoFm = mGraphics.getFontMetrics(INFO_FONT);
        int escTextWidth = infoFm.stringWidth(escText);
        drawText((width() - escTextWidth) / 2.0, height() - 55, escText, INFO_FONT.getName(), INFO_FONT.getSize());
    }

    private void paintSettingsMenu() {
        // Background for text for better readability
        drawTextBackground(width()/2.0 - 200, height() / 2.0 - 210, 400, 60);
        drawTextBackground(width()/2.0 - 200, height() / 2.0 - 150, 400, 60);


        changeColor(black);
        String settingsHeader = "Settings";
        FontMetrics headerFm = mGraphics.getFontMetrics(SUB_HEADER_FONT);
        int headerWidth = headerFm.stringWidth(settingsHeader);
        drawBoldText((width() - headerWidth) / 2.0, height() / 2.0 - 180, settingsHeader, SUB_HEADER_FONT.getName(), SUB_HEADER_FONT.getSize());

        String resolutionLabel = "Resolution:";
        FontMetrics resLabelFm = mGraphics.getFontMetrics(BUTTON_FONT);
        int resLabelWidth = resLabelFm.stringWidth(resolutionLabel);
        drawBoldText((width() - resLabelWidth) / 2.0, height() / 2.0 - 120, resolutionLabel, BUTTON_FONT.getName(), BUTTON_FONT.getSize());


        if (availableResolutions != null && resolutionButtonRects != null) {
            for (int i = 0; i < availableResolutions.length; i++) {
                Dimension res = availableResolutions[i];
                if (i < resolutionButtonRects.length && resolutionButtonRects[i] != null) {
                    Rectangle2D btnRect = resolutionButtonRects[i];
                    String resText = res.width + " x " + res.height;

                    if (res.equals(currentResolution)) {
                        changeColor(new Color(100, 180, 100, 230)); // Highlight with transparency
                    } else {
                        changeColor(new Color(180, 180, 220, 220)); // Others with transparency
                    }
                    drawSolidRectangle(btnRect.getX(), btnRect.getY(), btnRect.getWidth(), btnRect.getHeight());
                    changeColor(black); // Text color
                    drawCenteredText(resText, btnRect, RESOLUTION_FONT);
                }
            }
        }

        drawTextBackground(width()/2.0 - 150, height() - 80, 300, 40);
        changeColor(black);
        String escText = "Press ESC to go back";
        FontMetrics infoFm = mGraphics.getFontMetrics(INFO_FONT);
        int escTextWidth = infoFm.stringWidth(escText);
        drawText((width() - escTextWidth) / 2.0, height() - 55, escText, INFO_FONT.getName(), INFO_FONT.getSize());
    }

    /**
     * Helper method to draw a semi-transparent background for text elements.
     * @param x The x-coordinate of the background.
     * @param y The y-coordinate of the background.
     * @param w The width of the background.
     * @param h The height of the background.
     */
    private void drawTextBackground(double x, double y, double w, double h) {
        saveCurrentTransform(); // Save current graphics state
        changeColor(new Color(200, 200, 200, 180)); // Light gray, semi-transparent
        drawSolidRectangle(x, y, w, h);
        restoreLastTransform(); // Restore graphics state (especially color)
    }


    private void paintMuteButton() {
        if (muteButtonRect == null) recalculateButtonPositions();
        changeColor(isMuted ? new Color(150, 150, 150, 200) : new Color(100, 100, 100, 200)); // With transparency
        drawSolidRectangle(muteButtonRect.getX(), muteButtonRect.getY(), muteButtonRect.getWidth(), muteButtonRect.getHeight());
        changeColor(white);
        drawCenteredText(muteButtonDisplayString, muteButtonRect, MUTE_BUTTON_FONT);
    }

    private void drawCenteredText(String text, Rectangle2D rect, Font font) {
        if (mGraphics == null || rect == null) return;
        FontMetrics fm = mGraphics.getFontMetrics(font);
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent() - fm.getDescent();

        double x = rect.getX() + (rect.getWidth() - textWidth) / 2;
        double y = rect.getY() + (rect.getHeight() - textHeight) / 2 + fm.getAscent();

        mGraphics.setFont(font);
        mGraphics.drawString(text, (int)x, (int)y);
    }

    private void updateMuteButtonText() {
        muteButtonDisplayString = isMuted ? "🔇" : "🔊";
    }

    private void startGame(KiwiLoreGame.GameMode mode) {
        System.out.println("Attempting to start " + mode.toString() + " with resolution " + currentResolution.width + "x" + currentResolution.height);
        if (mFrame != null) {
            mFrame.dispose();
        }
        if (backgroundMusic != null && backgroundMusic.getLoopClip() != null && backgroundMusic.getLoopClip().isRunning()) {
            stopAudioLoop(backgroundMusic);
        }

        KiwiLoreGame actualGame = new KiwiLoreGame(currentResolution.width, currentResolution.height, mode);
        GameEngine.createGame(actualGame, 60);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point clickPoint = e.getPoint();

        if (muteButtonRect != null && muteButtonRect.contains(clickPoint)) {
            isMuted = !isMuted;
            updateMuteButtonText();
            if (backgroundMusic != null) {
                if (isMuted) {
                    if(backgroundMusic.getLoopClip() != null && backgroundMusic.getLoopClip().isRunning()) {
                        stopAudioLoop(backgroundMusic);
                    }
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
                currentMenuState = MenuState.GAME_MODE_SELECTION;
                recalculateButtonPositions();
            } else if (settingsButtonRect.contains(clickPoint)) {
                currentMenuState = MenuState.SETTINGS;
                recalculateButtonPositions();
            }
        } else if (currentMenuState == MenuState.GAME_MODE_SELECTION) {
            if (storyModeButtonRect.contains(clickPoint)) {
                startGame(KiwiLoreGame.GameMode.STORY);
            } else if (multiplayerButtonRect.contains(clickPoint)) {
                startGame(KiwiLoreGame.GameMode.MULTIPLAYER);
            }
        } else if (currentMenuState == MenuState.SETTINGS) {
            if (resolutionButtonRects != null) {
                for (int i = 0; i < resolutionButtonRects.length; i++) {
                    if (resolutionButtonRects[i] != null && resolutionButtonRects[i].contains(clickPoint)) {
                        Dimension selectedRes = availableResolutions[i];
                        if (!selectedRes.equals(currentResolution)) {
                            currentResolution = selectedRes;
                            setWindowSize(selectedRes.width, selectedRes.height);
                            recalculateButtonPositions();
                        }
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (currentMenuState == MenuState.SETTINGS || currentMenuState == MenuState.GAME_MODE_SELECTION) {
                currentMenuState = MenuState.MAIN;
                recalculateButtonPositions();
            }
        }
    }



    public static void main(String[] args) {
        MainMenu gameMenu = new MainMenu(800, 600);
        GameEngine.createGame(gameMenu, 60);
    }
}
