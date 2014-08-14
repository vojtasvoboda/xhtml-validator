package xhtml_validator;

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
 */
public class SyntaktickyAnalyzator {
    
    // debug
    private boolean debug = false;
    // LexAn
    private LexikalniAnalyzator LexAn = null;
    // precteny symbol z LexAn
    private LexikalniSymboly symbol = null;
    // vytvorime zasobnik
    private Stack Stack = null;
    private String tag = "";
    // pocitadlo chyb
    private int ErrorCount = 0;
    // vystup
    private VystupniSystem VystupniSystem;

    // parove tagy
    private String[] pairTag = {"p","a","div","form","h1","h2","h3","h4","h5",
                                "h6","li","ol","select","span","strong","table",
                                "tr","td"};
    
    /**
     * Konstruktor
     */
    public SyntaktickyAnalyzator(String cesta) {
        
        konzole("Vytvářím LexAn...");
        LexAn = new LexikalniAnalyzator(cesta);
        Stack = new Stack(100);
        VystupniSystem = new VystupniSystem();

        CtiSymbol();    // pocatecni cteni lexikalniho symbolu
        System.out.println ("SyntAn:0: Prvni cteny symbol z LexAn je " + symbol);
        Document();       // start rekursivniho sestupu startovnim neterminalem
        VystupniSystem.addLine();
        
        if (symbol != LexikalniSymboly.EOI) {
            konzole("Syntaktická analýza neskončila EOI!");
        } else {
            konzole("Syntaktická analýza skončila správně EOI.");
        }

        // System.out.println("Vystup:");
        // System.out.println("");
        // VystupniSystem.printVystup();
        konzole("Vystup:\n");
        konzole(VystupniSystem.getVystup());
    }

    /**
     * Cteni symbolu z LexAnu
     */
    private void CtiSymbol() {
        symbol = LexAn.ctiSymbol();
        // debug
        if (debug) {
            if (symbol == LexikalniSymboly.IDENT) {
                if (debug) konzole("Precteno " + symbol + " (" + LexAn.getIdent() + ")", true);
            } else {
                if (debug) konzole("Precteno " + symbol, true);
            }
        }
    }


    /**
     * Metoda srovnej pro symbol
     */
    private void srovnej(LexikalniSymboly ls) {
        if (symbol == ls) {
            CtiSymbol();
        } else {
            konzole("Chyba - chyba srovnání, očekává se " + ls,true);
            VystupniSystem.setErrorLine(true);
            ErrorCount++;
        }
    }


    /**
     * Pravidlo DOCUMENT
     * 
     * DOCUMENT -> DOCTYPE DOCUMENT2
     * 
     */
    private void Document() {
        if (debug) konzole("Expanze DOCUMENT -> DOCTYPE DOCUMENT2", true);
        switch (symbol) {
            case kwDOCTYPE:
                Doctype();
                Document2();
                break;
            default:
                konzole("Chyba DOCUMENT -> DOCTYPE DOCUMENT2",true);
                konzole("Očekáván <html>!",true);
                ErrorCount++;
                break;
        }
    }

    /**
     * Pravidlo DOCTYPE
     *
     * DOCTYPE -> kwDOCTYPE IDENTS
     * DOCTYPE -> epsilon
     *
     */
    private void Doctype() {
        if (debug) konzole("Expanze DOCTYPE -> kwDOCTYPE IDENTS", true);
        switch (symbol) {
            case kwDOCTYPE:
                VystupniSystem.addElement("&#60;&#33;DOCTYPE",getCisloRadky());
                srovnej(LexikalniSymboly.kwDOCTYPE);
                Idents();
                VystupniSystem.addElement("&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER);
                break;
            default:
                break;
        }
    }

    /**
     * Pravidlo DOCUMENT2
     *
     * DOCUMENT2 -> HTML_START HEAD BODY HTML_END
     * DOCUMENT2 -> epsilon
     *
     */
    private void Document2() {
        if (debug) konzole("Expanze DOCUMENT2 -> HTML_START HEAD BODY HTML_END", true);
        switch (symbol) {
            case LESS:
                Html_Start();
                Head();
                Body();
                Html_End();
                break;
            default:
                break;
        }
    }

    /**
     * Pravidlo HTML_START
     */
    private void Html_Start() {
        if (debug) konzole("Expanze HTML_START -> ...", true);
        switch (symbol) {
            case LESS:
                VystupniSystem.addElement("&#60;",getCisloRadky());
                srovnej(LexikalniSymboly.LESS);
                VystupniSystem.addTag("HTML",getCisloRadky());
                srovnej(LexikalniSymboly.kwHTML);
                Params();
                VystupniSystem.addElement("&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER);
                break;
            default:
                konzole("Chyba HTML_START -> < kwHTML PARAMS >",true);
                konzole("Očekáván <",true);
                ErrorCount++;
                break;
        }
    }

    /**
     * Pravidlo HTML_END
     */
    private void Html_End() {
        if (debug) konzole("Expanze HTML_END -> ...", true);
        switch (symbol) {
            case LESS_SLASH:
                VystupniSystem.addElement("&#60;&#47;",getCisloRadky());
                srovnej(LexikalniSymboly.LESS_SLASH);
                VystupniSystem.addTag("HTML",getCisloRadky());
                srovnej(LexikalniSymboly.kwHTML);
                VystupniSystem.addElement("&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER);
                break;
            default:
                konzole("Chyba HTML_END -> </ kwHTML >",true);
                konzole("Očekáván </",true);
                ErrorCount++;
                break;
        }
    }

    /**
     * Pravidlo HEAD
     */
    private void Head() {
        if (debug) konzole("Expanze HEAD -> ...", true);
        switch (symbol) {
            case LESS:
                VystupniSystem.addElement("&#60;",getCisloRadky()-1);
                srovnej(LexikalniSymboly.LESS);
                VystupniSystem.addTag("HEAD",getCisloRadky());
                srovnej(LexikalniSymboly.kwHEAD);
                VystupniSystem.addElement("&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER);
                Metas();
                Title();
                Links();
                Scripts();
                VystupniSystem.addElement("&#60;&#47;",getCisloRadky());
                srovnej(LexikalniSymboly.LESS_SLASH);
                VystupniSystem.addTag("HEAD",getCisloRadky());
                srovnej(LexikalniSymboly.kwHEAD);
                VystupniSystem.addElement("&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER);
                break;
            default:
                konzole("Chyba HEAD -> < kwHEAD > METAS TITLE LINKS SCRIPTS </ kwHEAD >",true);
                konzole("Očekáván <",true);
                ErrorCount++;
                break;
        }
    }


    /**
     * Pravidlo METAS
     */
    private void Metas() {
        if (debug) konzole("Expanze METAS -> ...", true);
        switch (symbol) {
            case LESS:
                Meta();
                Metas();
                break;
            default:
                break;
        }
    }


    /**
     * Pravidlo META
     */
    private void Meta() {
        if (debug) konzole("Expanze META -> ...", true);
        switch (symbol) {
            case LESS:
                VystupniSystem.addElement("&#60;",getCisloRadky());
                srovnej(LexikalniSymboly.LESS);
                VystupniSystem.addTag("META",getCisloRadky());
                srovnej(LexikalniSymboly.kwMETA);
                Params();
                VystupniSystem.addElement("&#47;&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER_SLASH);
                break;
            default:
                konzole("Chyba META -> < kwMETA PARAMS />",true);
                konzole("Očekáván <",true);
                ErrorCount++;
                break;
        }
    }


    /**
     * Pravidlo TITLE
     */
    private void Title() {
        if (debug) konzole("Expanze TITLE -> ...", true);
        switch (symbol) {
            case LESS:
                VystupniSystem.addElement("&#60;",getCisloRadky());
                srovnej(LexikalniSymboly.LESS);
                VystupniSystem.addTag("TITLE",getCisloRadky());
                srovnej(LexikalniSymboly.kwTITLE);
                VystupniSystem.addElement("&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER);
                Idents();
                VystupniSystem.addElement("&#60;&#47;",getCisloRadky());
                srovnej(LexikalniSymboly.LESS_SLASH);
                VystupniSystem.addTag("TITLE",getCisloRadky());
                srovnej(LexikalniSymboly.kwTITLE);
                VystupniSystem.addElement("&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER);
                break;
            default:
                break;
        }
    }


    /**
     * Pravidlo LINKS
     */
    private void Links() {
        if (debug) konzole("Expanze LINKS -> ...", true);
        switch (symbol) {
            case LESS:
                Link();
                Links();
                break;
            default:
                break;
        }
    }


    /**
     * Pravidlo LINK
     */
    private void Link() {
        if (debug) konzole("Expanze LINK -> ...", true);
        switch (symbol) {
            case LESS:
                VystupniSystem.addElement("&#60;",getCisloRadky());
                srovnej(LexikalniSymboly.LESS);
                VystupniSystem.addTag("LINK",getCisloRadky());
                srovnej(LexikalniSymboly.kwLINK);
                Params();
                VystupniSystem.addElement("&#47;&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER_SLASH);
                break;
            default:
                konzole("Chyba LINK -> < kwLINK PARAMS />",true);
                konzole("Očekáván <",true);
                ErrorCount++;
                break;
        }
    }


    /**
     * Pravidlo SCRIPTS
     */
    private void Scripts() {
        if (debug) konzole("Expanze SCRIPTS -> ...", true);
        switch (symbol) {
            case LESS:
                Script();
                Scripts();
                break;
            default:
                break;
        }
    }


    /**
     * Pravidlo SCRIPT
     */
    private void Script() {
        if (debug) konzole("Expanze SCRIPT -> ...", true);
        switch (symbol) {
            case LESS:
                VystupniSystem.addElement("&#60;",getCisloRadky());
                srovnej(LexikalniSymboly.LESS);
                VystupniSystem.addTag("SCRIPT",getCisloRadky());
                srovnej(LexikalniSymboly.kwSCRIPT);
                Params();
                VystupniSystem.addElement("&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER);
                Idents();
                VystupniSystem.addElement("&#60;&#47;",getCisloRadky());
                srovnej(LexikalniSymboly.LESS_SLASH);
                VystupniSystem.addTag("SCRIPT",getCisloRadky());
                srovnej(LexikalniSymboly.kwSCRIPT);
                VystupniSystem.addElement("&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER);
                break;
            default:
                konzole("Chyba SCRIPT -> < kwSCRIPT PARAMS > IDENTS </ kwSCRIPT >",true);
                konzole("Očekáván <",true);
                ErrorCount++;
                break;
        }
    }


    /**
     * Pravidlo BODY
     */
    private void Body() {
        if (debug) konzole("Expanze BODY -> ...", true);
        switch (symbol) {
            case LESS:
                VystupniSystem.addElement("&#60;",getCisloRadky());
                srovnej(LexikalniSymboly.LESS);
                VystupniSystem.addTag("BODY",getCisloRadky());
                srovnej(LexikalniSymboly.kwBODY);
                Params();
                VystupniSystem.addElement("&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER);
                Tags();
                VystupniSystem.addElement("&#60;&#47;",getCisloRadky());
                srovnej(LexikalniSymboly.LESS_SLASH);
                VystupniSystem.addTag("BODY",getCisloRadky());
                srovnej(LexikalniSymboly.kwBODY);
                VystupniSystem.addElement("&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER);
                break;
            default:
                konzole("Chyba BODY -> < kwBODY ...",true);
                konzole("Očekáván <",true);
                ErrorCount++;
                break;
        }
    }


    /**
     * Pravidlo TAGS
     */
    private void Tags() {
        if (debug) konzole("Expanze TAGS -> ...", true);
        switch (symbol) {
            case LESS:
                Tag();
                Tags();
                break;
            default:
                break;
        }
    }


    /**
     * Pravidlo TAG
     */
    private void Tag() {
        if (debug) konzole("Expanze TAG -> ...", true);
        switch (symbol) {
            case LESS:
                VystupniSystem.addElement("&#60;",getCisloRadky());
                srovnej(LexikalniSymboly.LESS);
                pushTag();
                // tady by mel byt taky addTag
                VystupniSystem.addElement(LexAn.getIdent(),getCisloRadky());
                srovnej(LexikalniSymboly.IDENT);
                Params();
                Tag2();
                break;
            default:
                konzole("Chyba TAG -> < kwIDENT PARAMS TAG2",true);
                konzole("Očekáván <",true);
                ErrorCount++;
                break;
        }
    }


    /**
     * Pravidlo TAG2
     */
    private void Tag2() {
        if (debug) konzole("Expanze TAG2 -> ...", true);
        switch (symbol) {
            case GREATER:
                VystupniSystem.addElement("&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER);
                Tags();
                Idents();
                Tags();
                VystupniSystem.addElement("&#60;&#47;",getCisloRadky());
                srovnej(LexikalniSymboly.LESS_SLASH);
                popTag();
                VystupniSystem.addTag(LexAn.getIdent(),getCisloRadky());
                srovnej(LexikalniSymboly.IDENT);
                VystupniSystem.addElement("&#62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER);
                break;
            case GREATER_SLASH:
                VystupniSystem.addElement("#&47;#&62;",getCisloRadky());
                srovnej(LexikalniSymboly.GREATER_SLASH);
                break;
            default:
                konzole("Chyba TAG2 -> ...",true);
                konzole("Očekáván > nebo />",true);
                ErrorCount++;
                break;
        }
    }


    /**
     * Pravidlo PARAMS
     */
    private void Params() {
        if (debug) konzole("Expanze PARAMS -> ...", true);
        switch (symbol) {
            case IDENT:
                Param();
                Params();
                break;
            default:
                break;
        }
    }


    /**
     * Pravidlo PARAM
     */
    private void Param() {
        if (debug) konzole("Expanze PARAM -> ...", true);
        switch (symbol) {
            case IDENT:
                VystupniSystem.addTag(" " + LexAn.getIdent(),getCisloRadky());
                srovnej(LexikalniSymboly.IDENT);
                VystupniSystem.addElement("&#61;",getCisloRadky());
                srovnej(LexikalniSymboly.ASSIGN);
                VystupniSystem.addElement("&#34;",getCisloRadky());
                srovnej(LexikalniSymboly.QUOTES);
                Idents();
                VystupniSystem.addElement("&#34; ",getCisloRadky());
                srovnej(LexikalniSymboly.QUOTES);
                break;
            default:
                konzole("Chyba PARAM -> ...",true);
                konzole("Očekáván IDENT",true);
                ErrorCount++;
                break;
        }
    }


    /**
     * Pravidlo IDENTS
     */
    private void Idents() {
        if (debug) konzole("Expanze IDENTS -> ...", true);
        switch (symbol) {
            case IDENT:
                // tady by melo byt taky addTag
                VystupniSystem.addElement(" " + LexAn.getIdent(),getCisloRadky());
                srovnej(LexikalniSymboly.IDENT);
                Idents();
                break;
            default:
                break;
        }
    }


    /**
     *
     * Zjisti, jestli je tag parovy
     *
     * @param tag
     * @return
     */
    private boolean isTagPair(String tag) {
        for (int i = 0 ; i < pairTag.length ; i++ ) {
            if (pairTag[i].equals(tag)) return true;
        }
        return false;
    }

    private void pushTag() {
        if (symbol == LexikalniSymboly.IDENT) {
            tag = LexAn.getIdent();
            if (isTagPair(tag)) {
                if (debug) System.out.println("SyntAn: Ukladame na zasobnik " + tag);
                Stack.push(tag); // ulozime symbol na zasobnik
            }
        }
    }
    
    private void popTag() {
        if (symbol == LexikalniSymboly.IDENT) {
            tag = LexAn.getIdent();
            if (isTagPair(tag)) {
                if (debug) System.out.println("SyntAn: Odebirame ze zasobniku " + tag);
                String ctenyTag = Stack.pop();
                if (!ctenyTag.equals(tag)) {
                    konzole("Chyba, očekává se tag " + ctenyTag + "!",true);
                    System.out.println("SyntAn: Očekává se tag " + ctenyTag + "!");
                    ErrorCount++;
                }
            }
        }
    }

    
    /**
     * 
     * Výpis do GUI
     * 
     * @param String text
     * @param boolean ukazatCisloRadku
     */
    private void konzole(String text, boolean cisloRadku) {
        if (cisloRadku) {
            GUI_Main.getInstance().printMessage("SyntAn:" + getCisloRadky() + ": " + text);
        } else {
            GUI_Main.getInstance().printMessage("SyntAn: " + text);
        }
    }
    
    private void konzole(String text) {
        GUI_Main.getInstance().printMessage("SyntAn: " + text);
    }

    public int getErrorCount() {
        return ErrorCount;
    }

    public int getCisloRadky() {
        return LexAn.getCisloRadky();
    }

    
    /**
     * 
     * Metoda MAIN pro debug
     * 
     * @param args
     */
    public static void main(String[] args) {
        
        String cesta = "C:\\Users\\Vojta\\Documents\\NetBeansProjects\\XHTML_Validator\\xhtml_validni.txt";
        LexikalniAnalyzator LexAnMain = new LexikalniAnalyzator(cesta);
        LexikalniSymboly symbolMain;
        
        try {
            while ((symbolMain = LexAnMain.ctiSymbol()) != LexikalniSymboly.EOI) {

                switch (symbolMain) {
                    case IDENT:
                        System.out.println(symbolMain + "(" + LexAnMain.getIdent() + ")");
                        break;
                    case COMMENT:
                        System.out.println(symbolMain + "(" + LexAnMain.getComment() + ")");
                        break;
                    case CHAR:
                        System.out.println(symbolMain + "(" + LexAnMain.getChar() + ")");
                        break;
                    case EOI:
                        break;
                    default:
                        System.out.println(symbolMain + "()");
                        break;
                }
            }

            System.out.println("");
            System.out.println("SyntAn: vsechno OK.");

        } catch (Exception e) {
            System.err.println("SyntAn:" + LexAnMain.getCisloRadky() + ": chyba ("+ e +").");
        }
    }
}