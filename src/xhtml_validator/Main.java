package xhtml_validator;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Vojta
 */
public class Main {

    /** konstruktor */
    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        setLook();                                  // nastavi LookAndFeel
        GUI_Main gui = GUI_Main.getInstance();      // graficke rozhrani
        gui.setVisible(true);
        gui.printMessage("Vyberte soubor pro validaci (max. 64kB).");

    }

    /**
     * nastaveni vzhledu grafickeho rozhrani (LookAndFeel)
     */
    private static void setLook() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        }
    }
}
