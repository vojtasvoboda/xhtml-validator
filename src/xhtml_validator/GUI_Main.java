package xhtml_validator;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author  Vojta
 */
public class GUI_Main extends javax.swing.JFrame {
    
    protected static GUI_Main unikatniInstance = null;
    public String konzoleText = "";                         // stavajici text v konzoli

    public FileReader souborReader = null;
    public FileInputStream souborInputStream = null;        // vstupni soubor
    public BufferedReader souborBufferedReader = null;      // vstupni proud

    protected String cestaSouboru = "";
    protected String nazevSouboru = "";                     // nazev souboru
    public long velikostSouboru = 0;                        // velikost souboru
    
   
    /** Creates new form GUI_Main */
    public GUI_Main() {
        initComponents();
        this.setCenter(this);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        adresa = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        konzole = new javax.swing.JEditorPane();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("XHTML Validator 1.0 (svobovo3@fel.cvut.cz)");

        adresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                adresaKeyReleased(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jButton1.setText("Validovat");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("...");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SouborMouseClicked(evt);
            }
        });

        konzole.setEditable(false);
        jScrollPane1.setViewportView(konzole);

        jButton3.setText("Vypsat soubor");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(adresa, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(jButton2)
                    .addComponent(adresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        if (fileSizeOk(cestaSouboru)) {

            // zavolame syntakticky analyzator s parametrem, kterym predame cestu
            printMessage("Main: Vytvářím SyntAn...");
            SyntaktickyAnalyzator SyntAn = new SyntaktickyAnalyzator(cestaSouboru);

            this.printMessage(" ");
            printMessage("Main: Validace dokončena. Počet chyb: " + SyntAn.getErrorCount());

        } else {
            printMessage("Main: Soubor je příliš velký pro validaci!");
        }
        
        this.printMessage(" ");

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        String token = "";

        if (fileSizeOk(cestaSouboru)){

            this.printMessage("Main: Výpis souboru:");

            try {

                souborReader = new FileReader(cestaSouboru);
                souborBufferedReader = new BufferedReader(souborReader);

                while ((token = souborBufferedReader.readLine()) != null) {
                    this.printMessage(token);
                }

                souborBufferedReader.close();

            } catch (FileNotFoundException ex) {
                this.printMessage("Main: Soubor nenalezen, nebo není přístupný!");
                ex.printStackTrace();
            } catch (IOException e) {
                this.printMessage("Main: Chyba načtení souboru!");
                e.printStackTrace();
            }
        } else {
            this.printMessage("Main: Soubor je příliš velký pro výpis!");
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void adresaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_adresaKeyReleased
        
        cestaSouboru = adresa.getText();
        
        if (fileExist(cestaSouboru)) {
            setEnableButtons(true);
        } else {
            setEnableButtons(false);
        }
        
    }//GEN-LAST:event_adresaKeyReleased

    
    /**
     * vrati true, pokud soubor existuje
     */
    public static boolean fileExist(String soubor) {
        
        boolean vysledek = false;
        boolean isDir = true;
        File file = null;
        
        /**
         * otestujeme jestli soubor existuje
         */
        try {

            file = new File(soubor);
            vysledek = file.exists();
            isDir = file.isDirectory();

        } catch (SecurityException se) {

            se.printStackTrace();
            vysledek = false;

        } finally {
            
            // nakonec uvolnime objekt file
            if ( file != null ) file = null;

        }
        
        /**
         * otestujeme jestli je to slozka nebo soubor
         */
        if (vysledek == true) {
            if (isDir) {
                vysledek = false;
            }
        }
        
        return vysledek;
        
    }
    
    /**
     * vrati jestli je nacteny soubor mensi jak 64 kB
     */
    public boolean fileSizeOk(String soubor) {
        
        boolean small = false;
        File file = null;
        long maxDelka = 64768;      // 64 kB
        long delka = 65000;
        
        try {

            file = new File(soubor);
            if(file.exists()){
                delka = file.length();
            }

        } catch (SecurityException se) {

            se.printStackTrace();
            small = false;

        }
        
        if (delka < maxDelka){
            small = true;
            this.setVelikostSouboru(delka);
        }
        
        return small;
        
    }
    
    /**
     * metoda pro nacteni souboru z pevneho disku
     */
    private void SouborMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SouborMouseClicked

        if (nactiSoubor()) {

            try {

                String soubor = this.getCestaSouboru();
                souborInputStream = new FileInputStream(soubor);
                
                if (fileSizeOk(soubor)) {
                    setEnableButtons(true);
                    this.printMessage("Main: Soubor " + nazevSouboru + " úspěšně nahrán. Jeho velikost je "
                            + getVelikostSouboru() + "B.");
                } else {
                    this.printMessage("Main: Soubor " + nazevSouboru + " úspěšně nahrán, ale je příliš velký! Jeho velikost je "
                            + getVelikostSouboru()  + "B.");
                }
                
                this.printMessage(" ");
                

            } catch (FileNotFoundException ex) {

                ex.printStackTrace();
                setEnableButtons(false);
                this.printMessage("Main: Nebyl nalezen cílový soubor!");

            } catch (IOException e) {
            
                e.printStackTrace();
                setEnableButtons(false);
                this.printMessage("Main: Chyba nahrávání souboru!");
             
            }
            
        } else {
            
            setEnableButtons(false);
            this.printMessage("Main: Nebyl vybrán žádný soubor!");
            
        }
        
    }//GEN-LAST:event_SouborMouseClicked

    /**
     * metoda pro nacteni souboru
     * vraci TRUE, pokud byl soubor spravne nacten
     */
    protected boolean nactiSoubor(){

        File soubor = new File("");
        File cestaAplikace = new File(System.getProperty("user.dir"));
        
        JFileChooser vyberSoubor = new JFileChooser();
        vyberSoubor.setCurrentDirectory(cestaAplikace);
        vyberSoubor.setDialogTitle("Main: Vyberte soubor pro validaci...");
        vyberSoubor.setAcceptAllFileFilterUsed(false);
        int vysledek = vyberSoubor.showOpenDialog(this);

        if (vysledek == JFileChooser.APPROVE_OPTION) {

            this.setNazevSouboru(vyberSoubor.getSelectedFile().getName());
            this.setCestaSouboru(vyberSoubor.getSelectedFile().getPath());
            return true;

        } else {

            return false;

        }

    }
    
    /**
     *  metoda pro pridani zpravy do konzoly
     */
    public void printMessage(String text) {

        konzoleText = konzole.getText() + text + "\n";
        konzole.setText(konzoleText);

    }

    /**
     * do textoveho pole napise cestu k souboru
     */
    public void setCestaSouboru(String path) {
        
        adresa.setText(path);
        cestaSouboru = path;
        
    }

    /**
     * vrati cestu vstupniho souboru
     */
    public String getCestaSouboru() {
        
        return cestaSouboru;
        
    }

    /**
     * vrati nazev souboru s priponou
     */
    public String getNazevSouboru() {
        
        return nazevSouboru;
        
    }
    
    /**
     *
     */
    public void setNazevSouboru(String nazev) {
        
        nazevSouboru = nazev;
        
    }

    /**
     * metoda vycentruje okno doprostred plochy
     */
    public void setCenter(JFrame okno) {
        
        Double rozmerXX = okno.getSize().getWidth();
        int rozmerX = rozmerXX.intValue();
        Double rozmerYY = okno.getSize().getHeight();
        int rozmerY = rozmerYY.intValue();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenX = (screenSize.width / 2) - (rozmerX / 2);   // pulka plochy minus pulka okna X
        int screenY = (screenSize.height / 2) - (rozmerY / 2);  // pulka plochy minus pulka okna Y
        okno.setLocation(screenX, screenY);
        
    }

    /**
     * vrati instanci GUI, pokud existuje, jinak vytvori novou
     */
    public static GUI_Main getInstance(){

        if (unikatniInstance == null) {
            unikatniInstance = new GUI_Main();
        }

        return unikatniInstance;

    }

    /**
     *  vypina tlacitka pro validaci a vypis souboru
     */
    public void setEnableButtons(boolean visibility) {
         
         jButton1.setEnabled(visibility);
         jButton3.setEnabled(visibility);
         
     }

    /**
      * nastavi velikost souboru
      */
    public void setVelikostSouboru(long velikost) {
         this.velikostSouboru = velikost;
     }
     
    /**
      * precte velikost souboru
      */
    public long getVelikostSouboru() {

          return this.velikostSouboru;

      }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI_Main().setVisible(true);
            }
        });
    }
      
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField adresa;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JEditorPane konzole;
    // End of variables declaration//GEN-END:variables
    
}
