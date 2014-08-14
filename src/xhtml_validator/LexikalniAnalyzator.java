package xhtml_validator;
import java.util.Scanner;

enum LexikalniSymboly {
    LESS,           // <
    LESS_SLASH,     // </
    GREATER,        // >
    GREATER_SLASH,  // />
    ASSIGN,         // =
    QUOTES,         // "
    kwDOCTYPE,      // !DOCTYPE
    kwHTML,         // html
    kwHEAD,         // head
    kwMETA,         // meta
    kwTITLE,        // title
    kwLINK,         // link
    kwSCRIPT,       // script
    kwBODY,         // body
    kwDIV,          // div
    IDENT,          // atribut
    VALUE,          // hodnota
    CHAR,           // znak
    COMMENT,        // komentar
    EOI             // konec souboru, end of input
}

/**
 *
 * @author Vojta
 *
 * LEXIKALNI ANALYZATOR
 *
 */
public class LexikalniAnalyzator {
    
    // cteni vstupu
    private Scanner scanner;
    private String buf = null;
    private int index;
    private int ch;
    private String komentar;
    // private StringBuilder hodnotaAtributu;
    private String ident;
    private char znak;
    // retezec, ktery dostanem ze vstupniho systemu
    private String retezecVstupnichZnaku;
    // cislo ctene radky
    private int cisloRadky;
    // vstupni system, ktery vrati rezetec ctenych znaku
    VstupniSystem vstupSyst = null;

    /**
     * konstruktor - vytvori novou instanci LexAn
     */
    public LexikalniAnalyzator(String soubor) {
        
        // pocatecni inicializace
        retezecVstupnichZnaku = "";
        cisloRadky = 0;
        // hodnotaAtributu = new StringBuilder();
        index = 0;
        scanner = null;
        buf = null;

        System.out.println("LexAn: vstupni soubor je " + soubor);
        
        // vytvori se instance Vstupniho Systemu, ktery vrati posloupnost znaku
        GUI_Main.getInstance().printMessage("Vytvářím VstupniSystem...");
        vstupSyst = new VstupniSystem(soubor);
        // precteme vstupni retezec znaku
        retezecVstupnichZnaku = vstupSyst.getRetezecZnaku();
        // retezec vstupnich znaku prectenych ze VstupnihoSystemu
        scanner = new Scanner(retezecVstupnichZnaku);

        ch = ctiZnak();
        System.out.println("LexAn:0: Prvni cteny znak je " + (char) ch);
    }


    /**
     * precte jeden znak ze vstupu
     * 
     * @return int
     */
    private int ctiZnak() {
        while (buf == null || index >= buf.length()) {
            if (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (scanner.hasNext())
                    buf = line + '\n';
                else buf = line;
                ++cisloRadky;
                index = 0;
            } else {
                scanner.close();
                return -1;
            }
        }
        return buf.charAt(index++);
    }    
    
    
    /**
     * vrati lexikalni symbol z vyctovyho typu LexikalniSymboly
     */
    public LexikalniSymboly ctiSymbol() {
        
        LexikalniSymboly symbol = LexikalniSymboly.IDENT;
        
        // mezery preskakujeme
        while (Character.isWhitespace(ch) & ch != -1) ch = ctiZnak();

        // precteny znak je <
        if (ch == '<') {
            ch = ctiZnak();

            // precteny znak je </
            if (ch == '/') {
                symbol = LexikalniSymboly.LESS_SLASH;
                ch = ctiZnak();

            // precteny znak je <!
            } else if (ch == '!') {
                ch = ctiZnak();
                if (ch == 'D') {
                    if (rozpoznejDoctype()) {
                        symbol = LexikalniSymboly.kwDOCTYPE;
                    } else {
                        symbol = LexikalniSymboly.CHAR;
                    }
                } else if (ch == '-') {
                    rozpoznejKomentar();
                    symbol = LexikalniSymboly.COMMENT;
                }

            // precteny znak je <
            } else {
                symbol = LexikalniSymboly.LESS;
            }

        // precteny znak je "
        } else if (ch == '\"') {
            ch = ctiZnak();
            symbol = LexikalniSymboly.QUOTES;
            
        // precteny znak je >
        } else if (ch == '>') {
            ch = ctiZnak();
            symbol = LexikalniSymboly.GREATER;
            
            
        // precteny znak je /
        } else if (ch == '/') {
            ch = ctiZnak();
            
            // precteny znak je />
            if (ch == '>') {
                symbol = LexikalniSymboly.GREATER_SLASH;
                ch = ctiZnak();
            } else {
                symbol = rozpoznejIdent();
            }
            
        // precteny znak je =
        } else if (ch == '=') {
            ch = ctiZnak();
            symbol = LexikalniSymboly.ASSIGN;

        // precteny znak je pismeno
        // } else if (Character.isLetterOrDigit(ch)) {
        //    symbol = rozpoznejIdent();

        // konec souboru
        } else if (ch == -1) {
            symbol = LexikalniSymboly.EOI;
            System.out.println("LexAn:" + this.getCisloRadky() +
                                                  ": Precteno EOI.");
            
        // ostatni
        } else {
            // znak = (char) ch;
            // ch = ctiZnak();
            // symbol = LexikalniSymboly.CHAR;
            symbol = rozpoznejIdent();
        }
        
        if (symbol == LexikalniSymboly.COMMENT) {
            symbol = ctiSymbol();
        }

        // vratime typ precteneho symbolu
        return symbol;
        
    }

    /**
     * rozpozname jestli je to kwDOCTYPE
     */
    private boolean rozpoznejDoctype() {

        ch = ctiZnak();
        String doctype = "";

        while (ch != -1 & !Character.isWhitespace(ch)) {
            doctype += (char) ch;
            ch = ctiZnak();
        }

        if (doctype.equals("OCTYPE")) {
            return true;
        } else {
            GUI_Main.getInstance().printMessage("LexAn:" + this.getCisloRadky() +
                                                  ": Spatne zapsan DOCTYPE!");
            System.err.println("LexAn:" + this.getCisloRadky() + 
                                                  ": Spatne zapsan DOCTYPE!");
            return false;
        }
    }

    /**
     * rozpozna identifikator
     */
    private LexikalniSymboly rozpoznejIdent() {
        ident = "";

        while (ch != '>' & ch != '<' & ch != '='  & ch != '\"' & !Character.isWhitespace(ch)) {
            ident += (char) ch;
            ch = ctiZnak();
        }

        if (ident.equals("body")) return LexikalniSymboly.kwBODY;
        // if (ident.equals("div")) return LexikalniSymboly.kwDIV;
        if (ident.equals("head")) return LexikalniSymboly.kwHEAD;
        if (ident.equals("html")) return LexikalniSymboly.kwHTML;
        if (ident.equals("link")) return LexikalniSymboly.kwLINK;
        if (ident.equals("meta")) return LexikalniSymboly.kwMETA;
        if (ident.equals("script")) return LexikalniSymboly.kwSCRIPT;
        if (ident.equals("title")) return LexikalniSymboly.kwTITLE;

        return LexikalniSymboly.IDENT;
    }

    /**
     * rozpozname jestli to je komentar nebo ne
     */
    private void rozpoznejKomentar() {
        
        // pocatecni stav Q0
        int stav = 1;
        komentar = "";
        ch = ctiZnak();

        // uz mame nacteno posloupnost <!-
        // koncove stavy 5 (OK), 6 (zumpa)
        while ( ch != -1 && stav != 5 && stav != 6) {
            switch (stav) {
                case 1: /* Q1 */
                    if (ch == '-') {
                        stav = 2;
                    } else {
                        stav = 6;   // Q6 - zumpa
                    }
                    break;
                case 2: /* Q2 */
                    if (ch == '-') {
                        stav = 3;
                    } else {
                        // vnitrek komentare
                        komentar += (char) ch;
                    }
                    break;
                case 3: /* Q3 */
                    if (ch == '-') {
                        stav = 4;
                    } else if (ch == '>') {
                        stav = 6;
                    } else {
                        stav = 2;
                    }
                    break;
                case 4: /* Q4 */
                    if (ch == '>') {
                        stav = 5; // komentar OK
                    } else {
                        stav = 2;
                    }
                    break;
                default:
                    break;
            }
            // precteme nasledujici znak
            ch = ctiZnak();
        }

        // pokud neskoncime v koncovym stavu, tak chyba
        if (stav != 5) {
            GUI_Main.getInstance().printMessage("LexAn:" + this.getCisloRadky() +
                                                  ": Spatne zapsany komentar!");
            System.err.println("LexAn:" + this.getCisloRadky() +
                                                  ": Spatne zapsany komentar!");
        } else {
            System.out.println("LexAn:" + this.getCisloRadky() +
                                                  ": Komentar: " + komentar);
        }
    }    


    /**
     * Gettery pro vytahovani konkretnich hodnot
     * @return values
     */
    public int getCisloRadky(){
        return this.cisloRadky;
    }
    
    public String getIdent() {
        return this.ident;
    }

    public String getComment() {
        return komentar;
    }

    public char getChar() {
        return znak;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String soubor = "C:\\Users\\Vojta\\Documents\\NetBeansProjects\\Validator\\xhtml_validni.txt";
        LexikalniAnalyzator LexAnMain = new LexikalniAnalyzator(soubor);
        LexikalniSymboly symbolMain;

        try {
            symbolMain = LexAnMain.ctiSymbol();
            while ( symbolMain != LexikalniSymboly.EOI ) {
                switch (symbolMain) {
                    case IDENT:
                        System.out.println("<" + symbolMain + " \"" + LexAnMain.getIdent() + "\">");
                        break;
                    case COMMENT:
                        System.out.println("<" + symbolMain + " \"" + LexAnMain.getComment() + "\">");
                        break;
                    case EOI:
                        break;
                    default:
                        System.out.println("<" + symbolMain + ">");
                        break;
                }
                symbolMain = LexAnMain.ctiSymbol();
            }

            System.out.println("LexAn: Konec souboru.");

        } catch (Exception e) {
            System.err.println("LexAn:" + LexAnMain.getCisloRadky() + ": chyba ("+ e +").");
        }

    }

}