/* *****************************************************************************
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd.common;

import cz.incad.commontools.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *******************************************************************************
 * Objekt je připravene pro údaje titulu, včetně exmplářů, zpracované z XML z Alephu
 * Jednotlivé atributy jsou pojmenované podle tagů z XML Alephu. 
 * Plněny jsou pouze zde definované atributy, ostatní zdrojové tagy jsou ignorovány
 * Pro data exempláře/ů jse zde definován samostatný objekt Exemplar, který je formou listu připojen k objektu titulu (OreRecord).
 * Všechny atributy jsou privátní, k jejich hodnotám se lze dostat pouze přes, k tomu navržené, veřejné metody.
 * Metody jsou připraveny jak pro vracení jednoho údaje, tak i vracení celého setu, pokud se jedná o list.
 * Pokud se jedná o atribut složitějšího tvaru, je použit objekt Subrecord, kde je uložen jak index tagu, tak i jeho hodnota: HashMap<String, String>
 * Pro práci se SubRecordem jsou opět připraveny funkce - set a get.
 * 
 * 
 * @author Marek
 */
public class OneRecord {
    private static final Logger LOG = Logger.getLogger(OneRecord.class.getName());
    
    private String sysNo;
    private String leader;
    private String pole001;
    private String pole003;
    private String pole005;
    private String pole007;
    private String pole008;
    private List<SubRecord> pole015;
    private List<SubRecord> pole020;
    private List<SubRecord> pole022;
    private SubRecord pole024;
    private SubRecord pole040;
    private SubRecord pole080;
    private SubRecord pole100;
    private List<SubRecord> pole245; //názvy
    private List<SubRecord> pole246; //variantní názvy
    private SubRecord pole250;
    private SubRecord pole260;
    private SubRecord pole264;
    private SubRecord pole300;
    private SubRecord pole490;
    private SubRecord pole500;
    private SubRecord pole653;
    private List<SubRecord> pole700;
    private SubRecord pole790;
    private SubRecord pole830;
    private SubRecord pole856;
    private SubRecord pole901;
    private List<SubRecord> pole902;
    private SubRecord pole910;
    private SubRecord pole911;
    private SubRecord pole964;
    private SubRecord pole998;
    private List<Exemplar> exemplar;
    private int pocetExemplaru;
    
    public OneRecord() {
        this.sysNo = "";
        this.leader = "";
        this.pole001 = "";
        this.pole003 = "";
        this.pole005 = "";
        this.pole007 = "";
        this.pole008 = "";
        this.pole015 = new ArrayList<SubRecord>();
        this.pole020 = new ArrayList<SubRecord>();
        this.pole022 = new ArrayList<SubRecord>();
        this.pole024 = new SubRecord();
        this.pole040 = new SubRecord();
        this.pole080 = new SubRecord();
        this.pole100 = new SubRecord();
        this.pole245 = new ArrayList<SubRecord>(); //názvy
        this.pole246 = new ArrayList<SubRecord>(); //variantní názvy
        this.pole250 = new SubRecord();
        this.pole260 = new SubRecord();
        this.pole264 = new SubRecord();
        this.pole300 = new SubRecord();
        this.pole490 = new SubRecord();
        this.pole500 = new SubRecord();
        this.pole653 = new SubRecord();
        this.pole700 = new ArrayList<SubRecord>();
        this.pole790 = new SubRecord();
        this.pole830 = new SubRecord();
        this.pole856 = new SubRecord();
        this.pole901 = new SubRecord();
        this.pole902 = new ArrayList<SubRecord>();
        this.pole910 = new SubRecord();
        this.pole911 = new SubRecord();
        this.pole964 = new SubRecord();
        this.pole998 = new SubRecord();
        
        this.exemplar = new ArrayList<Exemplar>();
        this.pocetExemplaru = 0;
        
    }
    
    public void fill(Node vstup) {
        String tag = "";
        NodeList nlChild = vstup.getChildNodes();
        for (int i=0; i<nlChild.getLength(); i++) {
            Node nChild = nlChild.item(i);
            if ("leader".equals(nChild.getNodeName())) {
                this.leader = nChild.getTextContent();
                System.out.println(" leader");
            } else if ("controlfield".equals(nChild.getNodeName())) {
                NamedNodeMap nlAttribute = nChild.getAttributes();
                tag = "";
                for (int j=0; j<nlAttribute.getLength(); j++) {
                    Node nChildAttrigute = nlAttribute.item(j);
                    if ("tag".equals(nChildAttrigute.getNodeName())) {
                        if ("".equals(tag)) tag = Utils.vratString(nChildAttrigute);
                    }
                }
                System.out.println(" controlfield: " + tag);
                if ("001".equals(tag)) {
                    this.pole001 = Utils.vratString(nChild);
                } else if ("003".equals(tag)) {
                    this.pole003 = Utils.vratString(nChild);
                } else if ("005".equals(tag)) {
                    this.pole005 = Utils.vratString(nChild);
                } else if ("007".equals(tag)) {
                    this.pole007 = Utils.vratString(nChild);
                } else if ("008".equals(tag)) {
                    this.pole008 = Utils.vratString(nChild);
                } else {
                    LOG.log(Level.SEVERE, "neznámý parametr: " + tag);
                    //LOG.log(Level.SEVERE, "neznámý parametr: " + tag + " -||- " + Thread.currentThread().getStackTrace()[2].getLineNumber());
                }
            } else if ("datafield".equals(nChild.getNodeName())) {
                NamedNodeMap nlAttribute = nChild.getAttributes();
                tag = "";
                for (int j=0; j<nlAttribute.getLength(); j++) {
                    Node nChildAttrigute = nlAttribute.item(j);
                    if ("tag".equals(nChildAttrigute.getNodeName())) {
                        if ("".equals(tag)) tag = Utils.vratString(nChildAttrigute);
                    }
                }
                SubRecord subRecord = new SubRecord();
                subRecord.fillFromNode(nChild);
                if ("015".equals(tag)) {
                    this.pole015.add(subRecord);
                } else if ("020".equals(tag)) {
                    this.pole020.add(subRecord);
                } else if ("022".equals(tag)) {
                    this.pole022.add(subRecord);
                } else if ("024".equals(tag)) {
                    this.pole024 = subRecord;
                } else if ("040".equals(tag)) {
                    this.pole040 = subRecord;
                } else if ("080".equals(tag)) {
                    this.pole080 = subRecord;
                } else if ("100".equals(tag)) {
                    this.pole100 = subRecord;
                } else if ("245".equals(tag)) {
                    this.pole245.add(subRecord);
                } else if ("246".equals(tag)) {
                    this.pole246.add(subRecord);
                } else if ("250".equals(tag)) {
                    this.pole250 = subRecord;
                } else if ("260".equals(tag)) {
                    this.pole260 = subRecord;
                } else if ("264".equals(tag)) {
                    this.pole264 = subRecord;
                } else if ("300".equals(tag)) {
                    this.pole300 = subRecord;
                } else if ("490".equals(tag)) {
                    this.pole490 = subRecord;
                } else if ("500".equals(tag)) {
                    this.pole500 = subRecord;
                } else if ("653".equals(tag)) {
                    this.pole653 = subRecord;
                } else if ("700".equals(tag)) {
                    this.pole700.add(subRecord);
                } else if ("790".equals(tag)) {
                    this.pole790 = subRecord;
                } else if ("830".equals(tag)) {
                    this.pole830 = subRecord;
                } else if ("856".equals(tag)) {
                    this.pole856 = subRecord;
                } else if ("901".equals(tag)) {
                    this.pole901 = subRecord;
                } else if ("902".equals(tag)) {
                    this.pole902.add(subRecord);
                } else if ("910".equals(tag)) {
                    this.pole910 = subRecord;
                } else if ("911".equals(tag)) {
                    this.pole911 = subRecord;
                } else if ("964".equals(tag)) {
                    this.pole964 = subRecord;
                } else if ("998".equals(tag)) {
                    this.pole998 = subRecord;
                } else {
                    LOG.log(Level.SEVERE, "neznámé pole tag v datafiledu: " + tag);
                }
            }
        }
        
        System.out.println(" zaznam: " + this.toString());
    }
    
    /**
     * nastaví pole sysno, které není součástí finálního XML dokumentu, proto se musí nastavovat separátně
     * @param hodnota 
     */
    public void setSysno(String hodnota) {
        if (!Utils.jePrazdne(hodnota)) this.sysNo = hodnota;
    }
    
    /**
     * 
     * @return vrací aktuální sysno
     */
    public String getSysno() {
        return this.sysNo;
    }
    
    //vyzkouset na docNumber=000199131 http://aleph.nkp.cz/X?op=item-data&doc_number=000199131&base=nkc
    public void fillExemplar(Node vstup) {
        int pocetExemplaruLocal = 0;
        NodeList nlChild = vstup.getChildNodes();
        for (int l=1; l<nlChild.getLength(); l++) {
            Node nChild = nlChild.item(l);
            if ("item".equals(nChild.getNodeName())) {
                Exemplar exemplarLocal = new Exemplar();
                pocetExemplaruLocal++;
                exemplarLocal.fillFromNode(nChild);
                LOG.log(Level.SEVERE, "" + exemplarLocal.toString());
                exemplar.add(exemplarLocal);
            }
        }
        if (pocetExemplaruLocal>0) {
            this.pocetExemplaru = pocetExemplaruLocal;
        }
    }
    
    /**
     * pomocný objekt na uložení hodnot
     */
    public class SubRecord {
        private HashMap<String, String> hodnoty;
        
        /**
         * constructor
         */
        public SubRecord() {
            this.hodnoty = new HashMap<String, String>();
        }
        
        /**
         * nastavuje hodnotu dle klíče
         * @param klic
         * @param hodnota 
         */
        public void set(String klic, String hodnota) {
            this.hodnoty.put(klic, hodnota);
        }
        
        /**
         * vrací hodnotu dle klíče
         * @param klic
         * @return 
         */
        public String get(String klic) {
            try {
                return this.hodnoty.get(klic);
            } catch (Exception e) {
                return null;
            }
        }
        
        /**
         * plní hodnoty z node
         * @param vstup (node)
         */
        public void fillFromNode(Node vstup) {
            String code = "";
            NamedNodeMap nlAttribute = vstup.getAttributes();
            for (int l=0; l<nlAttribute.getLength(); l++) {
                Node nChild = nlAttribute.item(l);
                if (!"tag".equals(nChild.getNodeName())) {
                    if (!Utils.jePrazdne(nChild.getNodeValue())) this.set(nChild.getNodeName(), nChild.getNodeValue());
                }
            }
            NodeList nlChild = vstup.getChildNodes();
            for (int l=1; l<nlChild.getLength(); l++) {
                code = "";
                Node nChild = nlChild.item(l);
                if (!"subfield".equals(nChild.getNodeName())) {
                    //System.out.println(" záznam není subfield: " + nChild.toString());
                } else {
                    NamedNodeMap nlChildAttribute = nChild.getAttributes();
                    for (int k=0; k<nlChildAttribute.getLength(); k++) {
                        Node nChildChild = nlChildAttribute.item(k);
                        if ("code".equals(nChildChild.getNodeName())) {
                            code = Utils.vratString(nChildChild);
                        }
                    }
                    if (!"".equals(code)) {
                        if (!Utils.jePrazdne(Utils.vratString(nChild))) this.set(code, Utils.vratString(nChild));

                    }
                }
            }
        }
        
        public String toString() {
            String vysledek = "";
            if (this.hodnoty != null) { vysledek = this.hodnoty.toString(); }
            return vysledek;
        }
        
    }
    
    public class Exemplar {
        private String signatura;
        private String knihovna;
        private String dilciKnihovna;
        private String umisteni;
        private String signaturaStudovny;
        private String carovyKod;
        private String poznamka;
        
        public Exemplar() {
            this.signatura = "";
            this.knihovna = "";
            this.dilciKnihovna = "";
            this.umisteni = "";
            this.signaturaStudovny = "";
            this.carovyKod = "";
            this.poznamka = "";
        }

        public void setSignatura(String hodnota) {
            if (!Utils.jePrazdne(hodnota)) this.signatura = hodnota;
        }

        public String getSignatura() {
            return this.signatura;
        }
        
        public void setKnihovna(String hodnota) {
            if (!Utils.jePrazdne(hodnota)) this.knihovna = hodnota;
        }

        public String getKnihovna() {
            return this.knihovna;
        }
        
        public void setDilciKnihovna(String hodnota) {
            if (!Utils.jePrazdne(hodnota)) this.dilciKnihovna = hodnota;
        }

        public String getDilciKnihovna() {
            return this.dilciKnihovna;
        }
        
        public void setUmisteni(String hodnota) {
            if (!Utils.jePrazdne(hodnota)) this.umisteni = hodnota;
        }

        public String getUmisteni() {
            return this.umisteni;
        }
        
        public void setSignaturaStudovny(String hodnota) {
            if (!Utils.jePrazdne(hodnota)) this.signaturaStudovny = hodnota;
        }

        public String getSignaturaStudovny() {
            return this.signaturaStudovny;
        }
        
        public void setCarovyKod(String hodnota) {
            if (!Utils.jePrazdne(hodnota)) this.carovyKod = hodnota;
        }

        public String getCarovyKod() {
            return this.carovyKod;
        }
        
        public void setPoznamka(String hodnota) {
            if (!Utils.jePrazdne(hodnota)) this.poznamka = hodnota;
        }

        public String getPoznamka() {
            return this.poznamka;
        }
        
        public void fillFromNode(Node vstup) {
            NodeList nlChild = vstup.getChildNodes();
            for (int l=1; l<nlChild.getLength(); l++) {
                Node nChild = nlChild.item(l);
                if ("barcode".equals(nChild.getNodeName())) { setCarovyKod(Utils.vratString(nChild)); }
                if ("sub-library".equals(nChild.getNodeName())) { setDilciKnihovna(Utils.vratString(nChild)); }
                if ("collection".equals(nChild.getNodeName())) { setUmisteni(Utils.vratString(nChild)); }
                if ("note".equals(nChild.getNodeName())) { setPoznamka(Utils.vratString(nChild)); }
                if ("call-no-1".equals(nChild.getNodeName())) { setSignatura(Utils.vratString(nChild)); }
                if ("call-no-2".equals(nChild.getNodeName())) { setSignaturaStudovny(Utils.vratString(nChild)); }
                if ("library".equals(nChild.getNodeName())) { setKnihovna(Utils.vratString(nChild)); }
            }
        }
        
        public String toString() {
            String vysledek = "";

            vysledek += Utils.vratStringSOddelovacem(" Exemplar", "");
            vysledek += Utils.vratStringSOddelovacem("  barcode", getCarovyKod());
            vysledek += Utils.vratStringSOddelovacem("  library", getKnihovna());
            vysledek += Utils.vratStringSOddelovacem("  sub-library", getDilciKnihovna());
            vysledek += Utils.vratStringSOddelovacem("  signarura", getSignatura());
            vysledek += Utils.vratStringSOddelovacem("  signaruraStudovny", getSignaturaStudovny());
            vysledek += Utils.vratStringSOddelovacem("  collection", getUmisteni());

            return vysledek;
        }
    }
    
    public String toString() {
        String vysledek = "";
        
        vysledek += Utils.vratStringSOddelovacem("sysno", this.sysNo);
        vysledek += Utils.vratStringSOddelovacem("leader", this.leader);
        vysledek += Utils.vratStringSOddelovacem("pole001", this.pole001);
        vysledek += Utils.vratStringSOddelovacem("pole003", this.pole003);
        vysledek += Utils.vratStringSOddelovacem("pole005", this.pole005);
        vysledek += Utils.vratStringSOddelovacem("pole007", this.pole007);
        vysledek += Utils.vratStringSOddelovacem("pole008", this.pole008);
        vysledek += Utils.vratStringSOddelovacemLS("pole015", this.pole015);
        vysledek += Utils.vratStringSOddelovacemLS("pole020", this.pole020);
        vysledek += Utils.vratStringSOddelovacemLS("pole022", this.pole022);
        vysledek += Utils.vratStringSOddelovacem("pole024", this.pole024);
        vysledek += Utils.vratStringSOddelovacem("pole040", this.pole040);
        vysledek += Utils.vratStringSOddelovacem("pole080", this.pole080);
        vysledek += Utils.vratStringSOddelovacem("pole100", this.pole100);
        vysledek += Utils.vratStringSOddelovacemLS("pole245", this.pole245); //názvy
        vysledek += Utils.vratStringSOddelovacemLS("pole246", this.pole246); //variantní názvy
        vysledek += Utils.vratStringSOddelovacem("pole250", this.pole250);
        vysledek += Utils.vratStringSOddelovacem("pole260", this.pole260);
        vysledek += Utils.vratStringSOddelovacem("pole264", this.pole264);
        vysledek += Utils.vratStringSOddelovacem("pole300", this.pole300);
        vysledek += Utils.vratStringSOddelovacem("pole490", this.pole490);
        vysledek += Utils.vratStringSOddelovacem("pole500", this.pole500);
        vysledek += Utils.vratStringSOddelovacem("pole653", this.pole653);
        vysledek += Utils.vratStringSOddelovacemLS("pole700", this.pole700);
        vysledek += Utils.vratStringSOddelovacem("pole790", this.pole790);
        vysledek += Utils.vratStringSOddelovacem("pole830", this.pole830);
        vysledek += Utils.vratStringSOddelovacem("pole856", this.pole856);
        vysledek += Utils.vratStringSOddelovacem("pole901", this.pole901);
        vysledek += Utils.vratStringSOddelovacemLS("pole902", this.pole902);
        vysledek += Utils.vratStringSOddelovacem("pole910", this.pole910);
        vysledek += Utils.vratStringSOddelovacem("pole911", this.pole911);
        vysledek += Utils.vratStringSOddelovacem("pole964", this.pole964);
        vysledek += Utils.vratStringSOddelovacem("pole998", this.pole998);
        vysledek += Utils.vratStringSOddelovacem("pocetExemplaru", "" + this.pocetExemplaru);
        for (int i=0; i<exemplar.size(); i++) {
            vysledek += exemplar.get(i).toString();
        }
        
        return vysledek;
    }
    
    public String getLeader() { return this.leader; }
    
    public String getPole001() { return this.pole001; }
    
    public String getPole003() { return this.pole003; }
    
    public String getPole005() { return this.pole005; }
    
    public String getPole007() { return this.pole007; }
    
    public String getPole008() { return this.pole008; }
    
    public List<SubRecord> getPole015List() { return this.pole015; }
    
    public SubRecord getPole015Id(int i) { 
        try {
            if (this.pole015.size()>=i) {
                return this.pole015.get(i);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public SubRecord getPole015() {
        try {
            return this.pole015.get(0);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<SubRecord> getPole020List() { return this.pole020; }
    
    public SubRecord getPole020Id(int i) {
        try {
            if (this.pole020.size()>=i) {
                return this.pole020.get(i);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public SubRecord getPole020() {
        try {
            return this.pole020.get(0);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<SubRecord> getPole022List() { return this.pole022; }
    
    public SubRecord getPole022Id(int i) {
        try {
            if (this.pole022.size()>=i) {
                return this.pole022.get(i);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public SubRecord getPole022() {
        try {
            return this.pole022.get(0);
        } catch (Exception e) {
            return null;
        }
    }
    
    public SubRecord getPole024() { return this.pole024; }
    
    public SubRecord getPole040() { return this.pole040; }
    
    public SubRecord getPole080() { return this.pole080; }
    
    public SubRecord getPole100() { return this.pole100; }
    
    public List<SubRecord> getPole245List() { return this.pole245; }  //názvy
    
    public SubRecord getPole245Id(int i) {  //názvy
        try {
            if (this.pole245.size()>=i) {
                return this.pole245.get(i);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public SubRecord getPole245() {  //názvy
        try {
            return this.pole245.get(0);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<SubRecord> getPole246List() { return this.pole246; }  //variantní názvy
    
    public SubRecord getPole246Id(int i) {  //variantní názvy
        try {
            if (this.pole246.size()>=i) {
                return this.pole246.get(i);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public SubRecord getPole246() {  //variantní názvy
        try {
            return this.pole246.get(0);
        } catch (Exception e) {
            return null;
        }
    }
    
    public SubRecord getPole250() { return this.pole250; }
    
    public SubRecord getPole260() { return this.pole260; }
    
    public SubRecord getPole264() { return this.pole264; }
    
    public SubRecord getPole300() { return this.pole300; }
    
    public SubRecord getPole490() { return this.pole490; }
    
    public SubRecord getPole500() { return this.pole500; }
    
    public SubRecord getPole653() { return this.pole653; }
    
    public List<SubRecord> getPole700List() { return this.pole700; }
    
    public SubRecord getPole700Id(int i) {
        try {
            if (this.pole700.size()>=i) {
                return this.pole700.get(i);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public SubRecord getPole700() { 
        try {
            return this.pole700.get(0);
        } catch (Exception e) {
            return null;
        }
    }
    
    public String getPole700Klic(String klic, String oddelovac) {
        String vysledek = "";
        for (int i=0; i<this.pole700.size(); i++) {
            if ((!vysledek.isEmpty()) && (!oddelovac.isEmpty())) { vysledek += oddelovac; }
            vysledek += this.pole700.get(i).get(klic);
        }
        return vysledek;
    }

    public SubRecord getPole790() { return this.pole790; }
    
    public SubRecord getPole830() { return this.pole830; }
    
    public SubRecord getPole856() { return this.pole856; }
    
    public SubRecord getPole901() { return this.pole901; }
    
    public List<SubRecord> getPole902List() { return this.pole902; }
    
    public SubRecord getPole902Id(int i) {
        try {
            if (this.pole902.size()>=i) {
                return this.pole902.get(i);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public SubRecord getPole902() { 
        try {
            return this.pole902.get(0);
        } catch (Exception e) {
            return null;
        }
    }
    
    public SubRecord getPole910() { return this.pole910; }
    
    public SubRecord getPole911() { return this.pole911; }
    
    public SubRecord getPole964() { return this.pole964; }
    
    public SubRecord getPole998() { return this.pole998; }

    public int getPocetExemplaru() { return this.pocetExemplaru; }
    
    public Exemplar getExemplar(int i) { return this.exemplar.get(i); }

    public List<Exemplar> getExemplar() { return this.exemplar; }

    
}
