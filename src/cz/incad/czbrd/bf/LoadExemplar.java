/* *****************************************************************************
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cz.incad.czbrd.bf;

import com.amaio.plaant.businessFunctions.ApplicationErrorException;
import com.amaio.plaant.businessFunctions.ValidationException;
import com.amaio.plaant.businessFunctions.WizardException;
import com.amaio.plaant.businessFunctions.WizardMessage;
import cz.incad.czbrd.common.ReliefUser;
import cz.incad.czbrd.r3c.R3CConfigurationEntity;
import cz.incad.relief3.core.BussinesFunction_A;
import cz.incad.relief3.core.caches.CacheConfiguration;
import cz.incad.relief3.core.tools.Exceptions;
import cz.incad.relief3.core.tools.XML;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *******************************************************************************
 *
 * @author martin
 */
public class LoadExemplar extends BussinesFunction_A implements Serializable {

    private static final Logger LOG = Logger.getLogger(LoadExemplar.class.getName());
    public static final String f_Panel01_signatura = "signatura";
    private static final CacheExemplars allExemplars = new CacheExemplars();

    /**
     ***************************************************************************
     *
     * @return @throws ValidationException
     * @throws WizardException
     */
    public WizardMessage startWizard() throws ValidationException, WizardException {
        WizardMessage wm = new WizardMessage();
        ReliefUser ru = new ReliefUser(getWCC());
        if (!ru.isSystemAdmin()) {
            throw new WizardException("Váš uživatelský účet nemá oprávnění pro spuštění této funkce.");
        }

        return wm;
    }

    /**
     ***************************************************************************
     *
     * @param panelName
     * @return
     * @throws ValidationException
     * @throws WizardException
     */
    public WizardMessage panelLeave(String panelName) throws ValidationException, WizardException {
        ValidationException vex = Exceptions.getValidationException(getWCC());
        WizardMessage wm = new WizardMessage();

        //Panel01
        if ("Panel01".equals(panelName)) {
            Object signatura = getWCC().getWizardRecord().getSimpleField(f_Panel01_signatura).getValue();

            if (signatura == null) {
                vex.addField("Signatura", "Pole nesmí být prázdné.", false);
                throw vex;
            }

            //prohledavani XML podle signatury.
            //test na prazdnou cache, popripade se spusti reload cache.
            LoadExemplar.allExemplars.reloadIfEmpty();

        }

        //Panel02
        if ("Panel02".equals(panelName)) {

        }

        return wm;
    }

    /**
     ***************************************************************************
     *
     * @return @throws ValidationException
     * @throws WizardException
     * @throws ApplicationErrorException
     */
    public WizardMessage runBusinessMethod() throws ValidationException, WizardException, ApplicationErrorException {
        WizardMessage wm = new WizardMessage();
        //založí novej záznam dle vyzvednutých dat z XML
        return wm;
    }

}

/**
 *******************************************************************************
 * (Přepravka) Instance této třídy představuje jeden exemplář, respektive záznam
 * z XML katalogu
 *
 * @author martin
 */
class OneRecord {

    public String nazev;
    public String cnb;
    public String pole001;
    public String sysno;
    public String carkod;
    public String signatura;
    public String autor;
    public String rokVydani;
    public String mistoVydani;
    public String vzacnostNKC;
    public String vzacnostSKC;

    /**
     ***************************************************************************
     * Konstruktor
     */
    public OneRecord(String nazev, String cnb, String pole001, String sysno, String carkod, String signatura, String autor, String rokVydani, String mistoVydani, String vzacnostNKC, String vzacnostSKC) {
        this.nazev = nazev;
        this.cnb = cnb;
        this.pole001 = pole001;
        this.sysno = sysno;
        this.carkod = carkod;
        this.signatura = signatura;
        this.autor = autor;
        this.rokVydani = rokVydani;
        this.mistoVydani = mistoVydani;
        this.vzacnostNKC = vzacnostNKC;
        this.vzacnostSKC = vzacnostSKC;
    }

}

/**
 *******************************************************************************
 *
 * @author martin
 */
class CacheExemplars extends LinkedList<OneRecord> {

    private static final Logger LOG = Logger.getLogger(CacheExemplars.class.getName());

    /**
     * *************************************************************************
     *
     * @param signatura
     * @return
     */
    public OneRecord getExemplarBySignatura(String signatura) {
        return null;
    }

    /**
     ***************************************************************************
     * V případě že list je prázdný se znovu načte z XML
     */
    public void reloadIfEmpty() {
        if (!this.isEmpty()) {
            return;
        }
        File repository = new File(CacheConfiguration.getValue(R3CConfigurationEntity.CFG_CZBRD_REPOSITORY_XML));
        InputStream is;
        Document xmlDokument;
        NodeList nlAllRecords;
        NodeList nlOneRecord;
        DocumentBuilderFactory dbf;
        DocumentBuilder docBuilder;
        String nazev;
        String cnb;
        String pole001;
        String sysno;
        String carkod;
        String signatura;
        String autor;
        String rokVydani;
        String mistoVydani;
        String vzacnostNKC;
        String vzacnostSKC;

        //Vymazani stare cache
        this.clear();

        File[] xmlFiles = repository.listFiles();
        try {
            dbf = DocumentBuilderFactory.newInstance();
            docBuilder = dbf.newDocumentBuilder();
            //Cyklus zpravovávající soubory v repository, jeden za druhým.
            for (int i = 0; i < xmlFiles.length; i++) {
                //Zpracujeme jeden zaznam za druhym
                is = new FileInputStream(xmlFiles[i]);
                xmlDokument = docBuilder.parse(is);
                //Mame seznam vsech zaznamu
                nlAllRecords = xmlDokument.getChildNodes().item(0).getChildNodes();

                //Cyklus projíždí jeden Record za druhým
                for (int j = 0; j < nlAllRecords.getLength(); j++) {
                    //NodeList obsahuje i nějaké další sračky, proto tímto IFem vyfiltrovávám jen záznamy typu 1 což jsou kýžené záznamy Record.
                    if (nlAllRecords.item(j).getNodeType() == 1) {
                        //vynulování nového záznamu
                        nazev = null;
                        cnb = null;
                        pole001 = null;
                        sysno = null;
                        carkod = null;
                        signatura = null;
                        autor = null;
                        rokVydani = null;
                        mistoVydani = null;
                        vzacnostNKC = null;
                        vzacnostSKC = null;
                        nlOneRecord = nlAllRecords.item(j).getChildNodes();
                        //Procházíme jeden subfield za druhým a vybíráme z nich to co potřebujeme.
                        for (int k = 0; k < nlOneRecord.getLength(); k++) {
                            if ("001".equals(XML.getAttributValue(nlOneRecord.item(i), "tag"))) {
                                pole001 = nlOneRecord.item(i).getTextContent();
                            } else if ("ITM".equals(XML.getAttributValue(nlOneRecord.item(i), "tag"))) {
                                //zpracovani itemu.

                            }

                        }
                    }
                }

                //zavřeme soubor
                is.close();
            }

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Chyba pri zpracovani XML souboru.", ex);
        }
    }

}
