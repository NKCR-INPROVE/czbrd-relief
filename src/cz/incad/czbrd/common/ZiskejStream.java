/* *****************************************************************************
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd.common;

import cz.incad.czbrd.common.OneRecord.Exemplar;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *******************************************************************************
 *
 * @author Marek
 */
public class ZiskejStream implements Serializable {

    private static final Logger LOG = Logger.getLogger(ZiskejStream.class.getName());

    /**
     ***************************************************************************
     * soukromý defaultní konstruktor
     */
    public ZiskejStream() {
    }

    /**
     * povolení vlastníci záznamů jsou ABA001, BOA001, OSA001 a OLA001
     */


    /**
     * Metoda otavírá a vrací stream z definovaného linku
     * TimeOut je nastaven na 60 sc.
     * @param link (String) - link pro otevření spojení
     * @return InputStream
     * @throws IOException 
     */
    private InputStream openConnection(String link) throws IOException {
        //if (!"http".equals(link.substring(0, 4))) { link = "http://" + link; }
        URL url = new URL(link);
        if (LOG.isLoggable(Level.FINE)) { LOG.log(Level.FINE, url.toExternalForm()); }
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setConnectTimeout(60 * 1000);
        huc.setReadTimeout(60 * 1000);
        huc.connect();
        return huc.getInputStream();        
    }
    
    /**
     * Metoda otevírá a vrací stream ze souboru
     * @param link (String) - link pro otevření spojení
     * @return InputStream
     * @throws IOException  
     */
    private InputStream openConnectionFile(String fileName) throws IOException {
        if (!Utils.jePrazdne(fileName)) {
            File soubor = new File(fileName);
            InputStream fileInputStream = new FileInputStream(soubor);
            return fileInputStream;
        } else {
            return null;
        }
    }
    
    /**
     * Metoda získává záznam titulu včetně exemplářů.
     *  Pokud je vyplněno pole sysno, je načten záznam přímo dle sysno, jinak je hledán podle čČNB či čarovéhoKódu
     * @param linkStart - úvodní část linku, která se posléze použije na vytvoření dceřiného linku pro čtení exempláře/ů
     * @param base - base záznamu
     * @param sysno - sysno hledaného záznamu, pokud je známo
     * @param ccnb - čČNB hledaného záznamu, pokud je známo
     * @param carKod - čarovýKód hledaného záznamu, pokud je znám
     * @param organizationStr - označení organizace vlastnící hledaný záznam
     * @return OneRecord
     */
    public OneRecord getStream(String linkStart, String base, String sysno, String ccnb, String carKod, String organizationStr) {
        if (!Utils.jePrazdne(linkStart)) {
            if (!Utils.jePrazdne(sysno)) { // sysno je vyplněné, načteme záznam přímo, pokud záznam nebyl načten, je čtení standardní cestou.
                OneRecord oneRecordSysno = new OneRecord();
                String localBase = vratLocalBase(base, organizationStr);
                String linkNew = "";
                linkNew = linkStart + "?op=ill_get_doc&doc_number=" + sysno + "&library=" + localBase;

                oneRecordSysno = getStreamDetail(linkNew, sysno, base, organizationStr);
                if (!Utils.jePrazdne(oneRecordSysno.getLeader())) {
                    return oneRecordSysno;
                }
            }

            OneRecord oneRecord = new OneRecord();
            String linkKonec = "";
            if (Utils.jePrazdne(base)) { base = "nkc"; }
            if (!Utils.jePrazdne(ccnb)) {
                return getStreamFind(linkStart, "?op=find&request=cnb=" + ccnb + "&base=" + base, base, organizationStr);
            } else if (!Utils.jePrazdne(carKod)) {
                if ("OSA001".equals(organizationStr)) {
                    // teoreticky lze použít o bar místo BRC, ale jejich administrátor doporučil BRC
                    return getStreamFind(linkStart, "?op=find&request=BRC=" + carKod + "&base=" + base, base, organizationStr);
                } else {
                    return getStreamFind(linkStart, "?op=find&request=bar=" + carKod + "&base=" + base, base, organizationStr);
                }
            }
        }
        return null;
    }

    /**
     * Metoda získává záznam titulu včetně exemplářů.
     *  Z předchozí metody je vrácen link, kterým se hledá set záznamu.
     * @param linkStart - úvodní část linku, která se posléze použije na vytvoření dceřiného linku pro čtení exempláře/ů
     * @param linkKonec - koncová část linku, pouze pro čtení titulu
     * @param base - base záznamu
     * @param organizationStr - označení organizace vlastnící hledaný záznam
     * @return OneRecord
     */
    private OneRecord getStreamFind(String linkStart, String linkKonec, String base, String organizationStr) {
        OneRecord oneRecord = new OneRecord();
        InputStream streamLocal = null;
        DocumentBuilderFactory dbFactoryLocal = null;
        DocumentBuilder dBuilderLocal = null;
        Document docLocal = null;
        String link = linkStart + linkKonec;
        String chyba = "";
        try {
            int pocetOpakovani = 0;
            Boolean opakuj = true;
            while ((opakuj) && (pocetOpakovani<5) ) {
                try {
                    streamLocal = prevedLinkNaStream(link);
                    if (streamLocal==null) { throw new IOException("chybný link v 'getStreamFind2'"); }
                    dbFactoryLocal = DocumentBuilderFactory.newInstance();
                    dBuilderLocal = dbFactoryLocal.newDocumentBuilder();
                    docLocal = dBuilderLocal.parse(streamLocal);
                    opakuj = false;
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, " chyba io v 'getStreamFind': " + ex.getMessage());
                } catch (ParserConfigurationException ex) {
                    LOG.log(Level.SEVERE, " chyba parsovani v 'getStreamFind': " + ex.getMessage());
                    opakuj = false;
                } catch (SAXException ex) {
                    LOG.log(Level.SEVERE, " chyba sax v 'getStreamFind': " + ex.getMessage());            
                    opakuj = false;
                }
                pocetOpakovani++;
            }

            if (docLocal!=null) { //povedlo se načíst dokument - zpracováváme
                String setNumber = "";
                String numberRecord = "";
                String numberEntries = "";
                NodeList nListIdentifiers = docLocal.getElementsByTagName("find");
                Node nChildBase = nListIdentifiers.item(0);
                NodeList nlChild = nChildBase.getChildNodes();
                for (int i=0; i<nlChild.getLength(); i++) {
                    Node nChild = nlChild.item(i);
                    if ("set_number".equals(nChild.getNodeName())) {
                        setNumber = Utils.vratString(nChild);
                    } else if ("no_records".equals(nChild.getNodeName())) {
                        numberRecord = Utils.vratString(nChild);
                    } else if ("no_entries".equals(nChild.getNodeName())) {
                        numberEntries = Utils.vratString(nChild);
                    } else if ("error".equals(nChild.getNodeName())) {
                        chyba = Utils.vratString(nChild);
                    }
                    //LOG.log(Level.INFO, "  nChild: " + nChild.toString() + " - " + nListIdentifiers.toString());
                }
                //LOG.log(Level.INFO, "  find: 1 - " + setNumber + " - " + numberRecord);
                if (!Utils.jePrazdne(chyba)) {
                    LOG.log(Level.SEVERE, "Záznam nenalezen, hledání vrátlo chybu: " + chyba);
                    return null;
                } 
                if ((!Utils.jePrazdne(setNumber)) & (!Utils.jePrazdne(numberRecord))) {
                    String linkNew = "";
                    if (numberEntries.equals(numberRecord)) {
                        if ("OSA001".equals(organizationStr)) {
                            //mají vlastní pravidla jak načítat záznam
                            linkNew = "?op=present&set_no=" + setNumber+ "&set_entry=" + numberEntries + "&format=marc";
                        } else {
                            linkNew = "?op=present&set_no=" + setNumber+ "&set_entry=" + numberEntries + "&base=" + base;
                        }
                    } else {
                        if ("OSA001".equals(organizationStr)) {
                            //mají vlastní pravidla jak načítat záznam
                            linkNew = "?op=present&set_no=" + setNumber+ "&set_entry=" + numberEntries + "," + numberRecord + "&format=marc";
                        } else {
                            linkNew = "?op=present&set_no=" + setNumber+ "&set_entry=" + numberEntries + "," + numberRecord + "&base=" + base;
                        }
                    }
                    return getStreamFind2(linkStart, linkNew, base, organizationStr);
                }
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Chyba při zpracování streamu v 'getStreamFind': " + ex.getMessage());
        }
        return null;
    }

    /**
     * Metoda získává záznam titulu včetně exemplářů.
     *  Z předchozí metody je vrácen link se setem, ze kterého se snažíme získat docNumber
     * @param linkStart - úvodní část linku, která se posléze použije na vytvoření dceřiného linku pro čtení exempláře/ů
     * @param linkKonec - koncová část linku, pouze pro čtení titulu
     * @param base - base záznamu
     * @param organizationStr - označení organizace vlastnící hledaný záznam
     * @return OneRecord
     */
    public OneRecord getStreamFind2(String linkStart, String linkKonec, String base, String organizationStr) {
        OneRecord oneRecord = new OneRecord();
        InputStream streamLocal = null;
        DocumentBuilderFactory dbFactoryLocal = null;
        DocumentBuilder dBuilderLocal = null;
        Document docLocal = null;
        String link = linkStart + linkKonec;
        String chyba = "";
        try {
            int pocetOpakovani = 0;
            Boolean opakuj = true;
            while ((opakuj) && (pocetOpakovani<5) ) {
                try {
                    streamLocal = prevedLinkNaStream(link);
                    if (streamLocal==null) { throw new IOException("chybný link v 'getStreamFind2'"); }
                    dbFactoryLocal = DocumentBuilderFactory.newInstance();
                    dBuilderLocal = dbFactoryLocal.newDocumentBuilder();
                    docLocal = dBuilderLocal.parse(streamLocal);
                    opakuj = false;
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, " chyba io v 'getStreamFind2': " + ex.getMessage());
                } catch (ParserConfigurationException ex) {
                    LOG.log(Level.SEVERE, " chyba parsovani v 'getStreamFind2': " + ex.getMessage());
                    opakuj = false;
                } catch (SAXException ex) {
                    LOG.log(Level.SEVERE, " chyba sax v 'getStreamFind2': " + ex.getMessage());            
                    opakuj = false;
                }
                pocetOpakovani++;
            }

            if (docLocal!=null) {
                //LOG.log(Level.INFO, "dokument nacten - parsuji");
                String setNumber = "";
                String numberRecord = "";
                String numberEntries = "";
                String docNumber = "";
                try {
                    NodeList nListIdentifiers = docLocal.getElementsByTagName("doc_number");
                    Node nChildBase = nListIdentifiers.item(0);
                    docNumber = Utils.vratString(nChildBase);
                } catch (Exception e) {
                    docNumber = "";
                }
                try {
                    NodeList nListIdentifiers = docLocal.getElementsByTagName("error");
                    Node nChildBase = nListIdentifiers.item(0);
                    chyba = Utils.vratString(nChildBase);
                } catch (Exception e) {
                    chyba = "";
                }
                if (!Utils.jePrazdne(chyba)) {
                    LOG.log(Level.SEVERE, "Záznam nenalezen, hledání vrátlo chybu: " + chyba);
                    return null;
                } else
                if (!Utils.jePrazdne(docNumber)) {
                    String linkNew = "";
                    String localBase = vratLocalBase(base, organizationStr);
                    linkNew = linkStart + "?op=ill_get_doc&doc_number=" + docNumber + "&library=" + localBase;
                    return getStreamDetail(linkNew, docNumber, localBase, organizationStr);
                }
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Chyba při zpracování streamu v 'getStreamFind2': " + ex.getMessage());
        }
        return null;
    }

    /**
     * Metoda vrací localBase dle organizace, případně dle base
     * @param base - base záznamu
     * @param cOrganization - označení organizace vlastnící hledaný záznam
     * @return String
     */
    private String vratLocalBase(String base, String cOrganization) {
        String localBase = "";
        if ("OSA001".equals(cOrganization)) {
            localBase = "mvk01";
        } else {
            if ("nkc".equals(base)) {
                localBase = "nkc01";
            } else if ("KATALOG".equals(base)) {
                localBase = "mvk01";
            } else if ("nkc".equals(base)) {
                localBase = "nkc01";
            }
        }
        return localBase;
    }
    
    /**
     * Metoda získává záznam titulu a následně voláním další metody i včetně exemplářů.
     *  Z předchozí metody byl dodán link s číslem záznamu. Zde se pokoušíme načíst záznam titulu
     * @param link
     * @param docNumber - číslo záznamu pro pozdější použití
     * @return OneRecord
     */
    public OneRecord getStreamDetail(String link, String docNumber, String base, String organizationStr) {
        OneRecord oneRecord = new OneRecord();
        InputStream streamLocal = null;
        DocumentBuilderFactory dbFactoryLocal = null;
        DocumentBuilder dBuilderLocal = null;
        Document docLocal = null;
        try {
            int pocetOpakovani = 0;
            Boolean opakuj = true;
            // Protože občas nezískáme data na první pokus, je potřeba několik opakování k úspěšnému získání dat.
            //  Pravděpodobně způsobeno přetíženým serverem. Pokud jsou data získána, opakování není spuštěno
            while ((opakuj) && (pocetOpakovani<5) ) {
                try {
                    //LOG.log(Level.INFO, "zkousim nacist data (Detail) - pokus c: " + pocetOpakovani);
                    streamLocal = prevedLinkNaStream(link);
                    if (streamLocal==null) { throw new IOException("chybný link v 'getStreamDetail'"); }
                    dbFactoryLocal = DocumentBuilderFactory.newInstance();
                    dBuilderLocal = dbFactoryLocal.newDocumentBuilder();
                    docLocal = dBuilderLocal.parse(streamLocal);
                    opakuj = false;
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, " chyba io v 'getStreamDetail': " + ex.getMessage());
                } catch (ParserConfigurationException ex) {
                    LOG.log(Level.SEVERE, " chyba parsovani v 'getStreamDetail': " + ex.getMessage());
                    opakuj = false;
                } catch (SAXException ex) {
                    LOG.log(Level.SEVERE, " chyba sax v 'getStreamDetail': " + ex.getMessage());            
                    opakuj = false;
                }
                pocetOpakovani++;
            }
            
            if (docLocal!=null) {
                NodeList nListIdentifiers = docLocal.getElementsByTagName("record");
                oneRecord.fill(nListIdentifiers.item(0));
                oneRecord.setSysno(docNumber);
                oneRecord = getStreamDetailExemplare(oneRecord, link, base, organizationStr);
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Chyba při zpracování streamu v 'getStreamDetail': " + ex.getMessage());
        }
        return oneRecord;
    }
    
    /**
     * Metoda získává záznam titulu včetně exemplářů.
     *  Z předchozí metody byl dodán link na hledání záznamu, zde je přetvořen na link pro hledání exempáře/ů tohoto záznamu a 
     *  výsledné XML jak pak rozparsováno na jednotlivé exempláře. Ty jsou dostupné formou listu v objektu OneRecord
     * @param oneRecord - záznam ttulu s údaji z Alephu
     * @param link - link na WS která vrací data
     * @param base - base knihovny pro načtení dat
     * @param organizationStr - označení organizace, která vlastní záznam
     * @return OneRecord - záznam s údaji z Alephu
     */
    public OneRecord getStreamDetailExemplare(OneRecord oneRecord, String link, String base, String organizationStr) {
        //kód na získání dat titulu (záznamu) nahradíme kódem na získání dat exemplářů
        String linkLocal = link.replace("?op=ill_get_doc&", "?op=item-data&");
        if ("OSA001".equals(organizationStr)) {
            //v linku, této organizace je vyžadována identifikace uživatele a jeho heslo, proto je zde toto rozdělení
            linkLocal += "&base=MVK01&user_name=x-item-dat&user_password=draQ6LwCVS";
        } else {
            linkLocal = linkLocal.substring(0, linkLocal.indexOf("&library="));
            linkLocal = linkLocal + "&base=" + base;
        }
        InputStream streamLocal = null;
        DocumentBuilderFactory dbFactoryLocal = null;
        DocumentBuilder dBuilderLocal = null;
        Document docLocal = null;
        try {
            int pocetOpakovani = 0;
            Boolean opakuj = true;
            while ((opakuj) && (pocetOpakovani<5) ) {
                try {
                    //LOG.log(Level.INFO, "zkousim nacist data (Detail) - pokus c: " + pocetOpakovani);
                    streamLocal = prevedLinkNaStream(linkLocal);
                    if (streamLocal==null) { throw new IOException("chybný link v 'getStreamDetailExemplare'"); }
                    dbFactoryLocal = DocumentBuilderFactory.newInstance();
                    dBuilderLocal = dbFactoryLocal.newDocumentBuilder();
                    docLocal = dBuilderLocal.parse(streamLocal);
                    opakuj = false;
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, " chyba io v 'getStreamDetailExemplare': " + ex.getMessage());
                } catch (ParserConfigurationException ex) {
                    LOG.log(Level.SEVERE, " chyba parsovani v 'getStreamDetailExemplare': " + ex.getMessage());
                    opakuj = false;
                } catch (SAXException ex) {
                    LOG.log(Level.SEVERE, " chyba sax v 'getStreamDetailExemplare': " + ex.getMessage());            
                    opakuj = false;
                }
                pocetOpakovani++;
            }
            
            if (docLocal!=null) {
                NodeList nList = docLocal.getElementsByTagName("item-data");
                oneRecord.fillExemplar(nList.item(0));
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Chyba při zpracování streamu exemplaru v 'getStreamDetailExemplare': " + ex.getMessage());
        }
        return oneRecord;
    }
    
    /**
     * metoda převádí link na stream. Otevře spojení a vrací InputStream
     * @param link link na zdroj dat (WS či soubor)
     * @return InputStream
     */
    private InputStream prevedLinkNaStream(String link) {
        InputStream streamLocal = null;
        if (!Utils.jePrazdne(link)) {
            try {
                if ("http".equals(link.substring(0, 4))) { //existují jen 2 varianty - soubor a WS
                    streamLocal = this.openConnection(link);
                } else {
    //            if (("/".equals(link.substring(0,1))) || (("\\:".equals(link.substring(1,2))) || ("\\:".equals(link.substring(2,3)))) || (("/:".equals(link.substring(1,2))) || ("/:".equals(link.substring(2,3))))) {
                    //připraveno i pro získávání dat z lokálního souboru - například pro testování
                    streamLocal = this.openConnectionFile(link);                    
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, " chyba io v 'prevedLinkNaStream': " + ex.getMessage());
            }
        }
        return streamLocal;
    }
    
}
