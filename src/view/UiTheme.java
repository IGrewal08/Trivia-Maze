package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Central design tokens for the Trivia Maze GUI: a clean, modern light
 * theme. Every panel pulls its colors, fonts, and component styling from
 * here so the look stays consistent and is trivial to retheme in one place.
 *
 * <p>This class is intentionally not instantiable — it is a holder for
 * shared constants and small styling helpers.
 *
 * @author Anwar Noor
 * @version 1.0
 */
public final class UiTheme {

    // ---- Surfaces ---------------------------------------------------------

    /** App window background — soft off-white. */
    public static final Color BG = new Color(0xF5, 0xF7, 0xFA);

    /** Card / panel surface — pure white. */
    public static final Color SURFACE = new Color(0xFF, 0xFF, 0xFF);

    /** Hairline border color for cards and dividers. */
    public static final Color BORDER = new Color(0xE2, 0xE8, 0xF0);

    // ---- Text -------------------------------------------------------------

    /** Primary text — dark slate. */
    public static final Color TEXT = new Color(0x1F, 0x29, 0x37);

    /** Secondary / muted text. */
    public static final Color TEXT_MUTED = new Color(0x6B, 0x72, 0x80);

    // ---- Accents ----------------------------------------------------------

    /** Primary action color — modern blue. */
    public static final Color PRIMARY = new Color(0x2D, 0x7F, 0xF9);

    /** Darker primary, used for hover states. */
    public static final Color PRIMARY_DARK = new Color(0x1B, 0x5F, 0xCC);

    /** Success / correct — green. */
    public static final Color SUCCESS = new Color(0x16, 0xA3, 0x4A);

    /** Danger / incorrect / blocked — red. */
    public static final Color DANGER = new Color(0xDC, 0x2B, 0x2B);

    /** Warning / locked — amber. */
    public static final Color WARNING = new Color(0xF5, 0x9E, 0x0B);

    // ---- Maze-specific fills ---------------------------------------------

    /** Unvisited room fill on the map. */
    public static final Color ROOM_UNVISITED = new Color(0xE5, 0xE9, 0xF0);

    /** Visited room fill on the map. */
    public static final Color ROOM_VISITED = new Color(0xBF, 0xDB, 0xFE);

    /** Current room fill on the map. */
    public static final Color ROOM_CURRENT = new Color(0x2D, 0x7F, 0xF9);

    /** Room floor fill in the room view. */
    public static final Color FLOOR = new Color(0xFB, 0xFC, 0xFE);

    // ---- Fonts ------------------------------------------------------------

    /** Base font family, falling back to a sans logical font. */
    private static final String FAMILY = "Segoe UI";

    /** Large title font. */
    public static final Font TITLE = new Font(FAMILY, Font.BOLD, 26);

    /** Section heading font. */
    public static final Font HEADING = new Font(FAMILY, Font.BOLD, 16);

    /** Standard body font. */
    public static final Font BODY = new Font(FAMILY, Font.PLAIN, 14);

    /** Bold body font, used for emphasis and buttons. */
    public static final Font BODY_BOLD = new Font(FAMILY, Font.BOLD, 14);

    /** Small caption font for legends and hints. */
    public static final Font CAPTION = new Font(FAMILY, Font.PLAIN, 12);

    private UiTheme() {
        // no instances
    }

    /**
     * Wraps a component in a titled "card": white surface, hairline border,
     * a heading-styled title, and comfortable inner padding.
     *
     * @param theComponent the component to decorate
     * @param theTitle the card title
     */
    public static void asCard(final JComponent theComponent, final String theTitle) {
        final Border line = BorderFactory.createLineBorder(BORDER, 1, true);
        final Border titled = BorderFactory.createTitledBorder(
                line, theTitle,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                HEADING, TEXT_MUTED);
        final Border padded = BorderFactory.createCompoundBorder(
                titled, BorderFactory.createEmptyBorder(8, 10, 8, 10));
        theComponent.setBorder(padded);
        theComponent.setBackground(SURFACE);
    }

    /**
     * Styles a button as a flat, solid-color, modern control with a hover
     * effect. Uses {@link BasicButtonUI} so the fill color renders solid
     * regardless of the active look-and-feel.
     *
     * @param theButton the button to style
     * @param thePrimary true for a filled primary (blue) button; false for
     *                   a neutral light button
     */
    public static void styleButton(final JButton theButton, final boolean thePrimary) {
        final Color base = thePrimary ? PRIMARY : SURFACE;
        final Color hover = thePrimary ? PRIMARY_DARK : new Color(0xED, 0xF1, 0xF7);
        final Color fg = thePrimary ? Color.WHITE : TEXT;

        theButton.setUI(new BasicButtonUI());
        theButton.setFont(BODY_BOLD);
        theButton.setForeground(fg);
        theButton.setBackground(base);
        theButton.setOpaque(true);
        theButton.setFocusPainted(false);
        theButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        theButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(thePrimary ? PRIMARY_DARK : BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));

        theButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent theEvent) {
                if (theButton.isEnabled()) {
                    theButton.setBackground(hover);
                }
            }

            @Override
            public void mouseExited(final MouseEvent theEvent) {
                theButton.setBackground(base);
            }
        });
    }
}
