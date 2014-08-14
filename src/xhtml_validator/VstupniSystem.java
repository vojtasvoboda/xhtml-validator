package xhtml_validator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Vojta
 *
 * Tato trida je prvni v retezci Validatoru a ma za ukol otevrit soubor,
 * nacist z neho posloupnost znaku a ulozit do typu promenne retezecZnaku
 *
 */
public class VstupniSystem {

    private String cestaSouboru = "";
    private FileReader fileReader;
    private String retezecZnaku;

    /**
     * konstruktor - vytvori novy VstupniSystem
     */
    public VstupniSystem(String cesta) {
        
        this.cestaSouboru = cesta;
        System.out.println("VstupniSystem - vstupni soubor je " + cestaSouboru);
        System.out.println("");
        
        /**
         * vytvori FileReader pro cteni ze souboru
         */
        try {
            fileReader = new FileReader(cestaSouboru);
            GUI_Main.getInstance().printMessage("VstupniSystem: Načtení souboru OK.");
        } catch (FileNotFoundException ex) {
            GUI_Main.getInstance().printMessage("VstupniSystem: Soubor nenalezen!");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            GUI_Main.getInstance().printMessage("VstupniSystem: IOException!");
        }
        
        if (fileReader != null) {

            /**
             * naplni promennou retezecZnaku
             * vstupni soubor se cte po znacich (ctou se ASCII hodnoty, ktery
             * se pak prevedou na znak pomoci toString()
             */

            StringBuffer buffer = new StringBuffer(2000);

            try {

                BufferedReader stream = new BufferedReader(new InputStreamReader(new FileInputStream(cestaSouboru)));
                char [] lines = new char[2000];
                int read;
                while ((read = stream.read(lines)) != -1)
                    buffer.append(lines, 0, read);
                    stream.close();
                fileReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            this.retezecZnaku = buffer.toString();

        }

    }


    /**
     * vrati nacteny retezec znaku
     */
    public String getRetezecZnaku() {
        
        return retezecZnaku;
        
    }

}