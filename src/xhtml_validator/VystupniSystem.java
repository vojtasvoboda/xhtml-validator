/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xhtml_validator;

/**
 *
 * @author Vojta
 */
public class VystupniSystem {

    // zapne / vypne odkazy
    boolean odkazy = true;

    // vystupni text
    String vystup;
    // jeden radek textu
    String line;
    int aktualniRadek;
    boolean errorLine = false;

    /**
     * konstruktor
     */
    public VystupniSystem() {
        vystup = "";
        line = "";
        aktualniRadek = 0;
    }

    /**
     *
     * Prida do vystupu tag, tj. text s odkazem
     *
     * @param tag
     * @param error
     */
    public void addTag(String tag, int radka) {
        if (aktualniRadek < radka) addLine();
        if (odkazy) {
            this.line += "<a href=\"http://www.w3schools.com/tags/tag_" + tag + ".asp\"><strong>" + tag + "</strong></a> ";
        } else {
            this.line += tag + "";
        }
    }

    /**
     *
     * Prida do vystupu element, tj. text bez odkazu
     *
     * @param element
     * @param error
     */
    public void addElement(String element, int radka) {
        if (aktualniRadek < radka) addLine();
        this.line += "" + element;
    }

    public void setErrorLine(boolean error) {
        this.errorLine = error;
    }

    /**
     * Prida novou radku do vystupu
     */
    public void addLine() {

        aktualniRadek++;

        String radek = line;
        if (errorLine) {
            radek = "<p class=\"error\">" + line + "</p>";
        } else {
            radek = "<p>" + line + "</p>";
        }

        vystup = vystup + radek + "" + "\n";
        line = "";
        errorLine = false;

    }

    /**
     * Vytiskne vystup na konzoli
     */
    public void printVystup() {
        System.out.println(vystup);
    }

    public String getVystup() {
        return vystup;
    }
}
