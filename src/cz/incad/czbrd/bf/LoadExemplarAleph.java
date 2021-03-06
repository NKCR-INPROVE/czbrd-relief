package cz.incad.czbrd.bf;

import cz.incad.czbrd.ExemplarEntity;
import cz.incad.czbrd.common.ReliefUser;
import com.amaio.plaant.businessFunctions.*;
import com.amaio.plaant.sync.Field;
import com.amaio.plaant.sync.Record;
import cz.incad.czbrd.common.Utils;
import cz.incad.relief3.core.BussinesFunction_A;
import cz.incad.relief3.core.Record_A;
import cz.incad.relief3.core.tools.Commons;
import cz.incad.relief3.core.tools.Exceptions;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.rmi.CORBA.Util;

import cz.incad.czbrd.common.OneRecord;
import cz.incad.czbrd.common.OneRecord.Exemplar;
import cz.incad.czbrd.common.ZiskejStream;

/*
import com.amaio.plaant.DbBrowser;
import com.amaio.plaant.businessFunctions.AddException;
import com.amaio.plaant.businessFunctions.AnnotationKeys;
import com.amaio.plaant.businessFunctions.ApplicationErrorException;
import com.amaio.plaant.businessFunctions.RecordsIterator;
import com.amaio.plaant.businessFunctions.ValidationException;
import com.amaio.plaant.businessFunctions.WizardException;
import com.amaio.plaant.businessFunctions.WizardMessage;
import com.amaio.plaant.dbdef.ListSource;
import com.amaio.plaant.dbdef.ListSourceItem;
import com.amaio.plaant.dbdef.ListValue;
import com.amaio.plaant.desk.QueryException;
import com.amaio.plaant.metadata.Filter;
import com.amaio.plaant.sync.Domain;
import com.amaio.plaant.sync.Record;
import cz.incad.czbrd.ExemplarEntity;
import cz.incad.czbrd.common.ReliefUser;
//import cz.incad.r3tools.R3Commons;
//import cz.incad.r3tools.R3FilterTools;
import cz.incad.relief3.core.BussinesFunction_A;
import cz.incad.relief3.core.tools.Exceptions;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
//import radozah.RadoZah;
*/

/**
 *
 * @author AdamFoglar
 */
public class LoadExemplarAleph extends BussinesFunction_A implements Serializable {//extends BussinesFunction_A{
    private static final Logger LOG = Logger.getLogger(ExemplarEntity.class.getName());
    private Record recExemplar;
    private Record recForm;
    
    private String organizationStr;
    private String ccnbStr;
    private String carKodStr;
    private String pole001Str;
    private String nazevStr;
    private String signaturaStr;
    private String vydavatelStr;
    private String rokVydaniStr;
    private String linkStart = "";
    private String base = "";
    private String ccnbStrNew;
    private String carKodNewStr;
    private String pole001NewStr;
    private String nazevNewStr;
    private String rokVydaniNewStr;
    
    private String autorNewStr;
    private String mistoVydaniNewStr;
    private String signaturaNewStr;
    private String vydavatelNewStr;
    
    private String sysNoStr;
    private String sysNoNewStr;

    OneRecord oneRecord = new OneRecord();
    
    public WizardMessage startWizard() throws ValidationException, WizardException {
        WizardMessage wm = new WizardMessage();
        ReliefUser ru;
        String organizationUser;
        sysNoStr = "";
        
        if (getWCC().getSelectedRecords().getRecordsCount() != 1) {
            throw new WizardException("Pro tuto funkci musí být vybrán právě jeden záznam.");
        }
        recExemplar = getWCC().getSelectedRecords().nextRecord();
        ru = new ReliefUser(getWCC());

        organizationUser = ru.getOrganization();
        organizationStr = (String) recExemplar.getSimpleField(ExemplarEntity.F_cOrganization_STR).getValue();

        if (organizationUser == null) {
            throw new WizardException("Váš uživatelský účet nemá nemá přiřazenu organizaci, kontaktujte vašeho administrátora.");
        }

        if (organizationStr == null) {
            throw new WizardException("Vybraný záznam nemá přiřazenu organizaci, kontaktujte vašeho administrátora.");
        }

        if (!(ru.isExplorer() || ru.isSystemAdmin())) {
            throw new WizardException("Pro tuto akci nemáte dostatečná oprávnění.");
        }

        if (!organizationUser.equalsIgnoreCase(organizationStr)) {
            throw new WizardException("Vybraný záznam patří jiné organizaci, nemáte oprávnění jej editovat.");
        }

        if (recExemplar.getSimpleField(ExemplarEntity.F_bibCNB_STR).getValue() == null) {
            ccnbStr = null;
        } else {
            ccnbStr = recExemplar.getSimpleField(ExemplarEntity.F_bibCNB_STR).getValue().toString();
        }
        if (recExemplar.getSimpleField(ExemplarEntity.F_bibCarKod_STR).getValue() == null) {
            carKodStr = null;
        } else {
            carKodStr = recExemplar.getSimpleField(ExemplarEntity.F_bibCarKod_STR).getValue().toString();
        }
        if (recExemplar.getSimpleField(ExemplarEntity.F_bibPole001_STR).getValue() == null) {
            pole001Str = null;
        } else {
            pole001Str = recExemplar.getSimpleField(ExemplarEntity.F_bibPole001_STR).getValue().toString();
        }
        if (recExemplar.getSimpleField(ExemplarEntity.F_bibNazev_STR).getValue() == null) {
            nazevStr = null;
        } else {
            nazevStr = recExemplar.getSimpleField(ExemplarEntity.F_bibNazev_STR).getValue().toString();
        }

        if (recExemplar.getSimpleField(ExemplarEntity.F_bibVydavatel_STR).getValue() == null) {
            vydavatelStr = null;
        } else {
            vydavatelStr = recExemplar.getSimpleField(ExemplarEntity.F_bibVydavatel_STR).getValue().toString();
        }
        if (recExemplar.getSimpleField(ExemplarEntity.F_bibRokVydani_STR).getValue() == null) {
            rokVydaniStr = null;
        } else {
            rokVydaniStr = recExemplar.getSimpleField(ExemplarEntity.F_bibRokVydani_STR).getValue().toString();
        }
        if (recExemplar.getSimpleField(ExemplarEntity.F_bibSignatura_STR).getValue() == null) {
            signaturaStr = null;
        } else {
            signaturaStr = recExemplar.getSimpleField(ExemplarEntity.F_bibSignatura_STR).getValue().toString();
        }
        
        if (recExemplar.getSimpleField(ExemplarEntity.F_bibSysno_STR).getValue() != null) {
            sysNoStr = recExemplar.getSimpleField(ExemplarEntity.F_bibSysno_STR).getValue().toString();
        }

        LOG.log(Level.SEVERE, "sysno: " + sysNoStr);
        LOG.log(Level.SEVERE, "ccnb: " + ccnbStr);
        LOG.log(Level.SEVERE, "ck: " + carKodStr);

        if (Utils.jePrazdne(ccnbStr)) {
            LOG.log(Level.SEVERE, "ccnb je prazdne");
        }
        if ((Utils.jePrazdne(carKodStr))) {
            LOG.log(Level.SEVERE, "ck je prazdne");
        }
        
        if (Utils.jePrazdne(ccnbStr) && (Utils.jePrazdne(carKodStr))) {
            throw new WizardException("Vybraný záznam nemá vyplněn čCNB ani čarový kód. Proto nelze automaticky aktualizovat.");
        }
        
        if (Utils.jePrazdne(organizationStr)) {
            throw new WizardException("Není vyplněn vlastník, nelze pokračovat.");
        }
        
        recForm = getWCC().getWizardRecord();
        if (!Utils.jePrazdne(ccnbStr)) { recForm.getSimpleField("ccnb").setValue(ccnbStr); }
        recForm.getSimpleField("ccnb").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        if (!Utils.jePrazdne(carKodStr)) { recForm.getSimpleField("carKod").setValue(carKodStr); }
        recForm.getSimpleField("carKod").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        if (!Utils.jePrazdne(carKodStr)) { recForm.getSimpleField("pole001").setValue(pole001Str); }
        recForm.getSimpleField("pole001").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        if (!Utils.jePrazdne(nazevStr)) { recForm.getSimpleField("nazev").setValue(nazevStr); }
        recForm.getSimpleField("nazev").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        if (!Utils.jePrazdne(rokVydaniStr)) { recForm.getSimpleField("rokVydani").setValue(rokVydaniStr); }
        recForm.getSimpleField("rokVydani").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        if (!Utils.jePrazdne(signaturaStr)) { recForm.getSimpleField("signatura").setValue(signaturaStr); }
        recForm.getSimpleField("signatura").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        if (!Utils.jePrazdne(vydavatelStr)) { recForm.getSimpleField("vydavatel").setValue(vydavatelStr); }
        recForm.getSimpleField("vydavatel").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);

        if (!Utils.jePrazdne(organizationStr)) {
            if ("ABA001".equals(organizationStr)) {
                linkStart = "http://aleph.nkp.cz/X";
                base = "nkc";
            } else if ("BOA001".equals(organizationStr)) {
                linkStart = "http://aleph.mzk.cz/X";
                base = "";
            } else if ("OLA001".equals(organizationStr)) {
                linkStart = "http://aleph.olp.cz/X";                
                base = "";
            } else if ("OSA001".equals(organizationStr)) {
                linkStart = "https://katalog.svkos.cz/X";                
                base = "KATALOG";
            }
            
            ZiskejStream ziskejStream = new ZiskejStream();
            oneRecord = ziskejStream.getStream(linkStart, base, sysNoStr, ccnbStr, carKodStr, organizationStr);
            if (oneRecord == null) {
                throw new WizardException("Záznam nebyl v Alephu nalezen, nelze pokračovat.");
            }
            //wm.addLine(oneRecord.toString());

            try {
                sysNoNewStr = oneRecord.getSysno();
            } catch (NullPointerException e) {
                sysNoNewStr = null;
            }

            try {
                pole001NewStr = oneRecord.getPole001();
            } catch (NullPointerException e) {
                pole001NewStr = null;
            }
            try {
                ccnbStrNew = oneRecord.getPole015().get("a");
            } catch (NullPointerException e) {
                ccnbStrNew = null;
            }
            try {
                nazevNewStr = oneRecord.getPole245().get("a"); // nazev
            } catch (NullPointerException e) {
                nazevNewStr = null;
            }
            try {
                String podNazev = "";
                podNazev = oneRecord.getPole245().get("b"); //podnazev
                if (podNazev!=null) { nazevNewStr = nazevNewStr + " " + podNazev; }
            } catch (NullPointerException e) { }
            try {
                String podNazev = "";
                podNazev = oneRecord.getPole245().get("c"); //podnazev
                if (podNazev!=null) { nazevNewStr = nazevNewStr + " " + podNazev; }
            } catch (NullPointerException e) { }
            try {
                rokVydaniNewStr = oneRecord.getPole260().get("c");
            } catch (NullPointerException e) {
                rokVydaniNewStr = null;
            }
            try {
                autorNewStr = oneRecord.getPole100().get("a");
            } catch (NullPointerException e) {
                autorNewStr = null;
            }
            if (autorNewStr==null) {
                try {
                    autorNewStr = oneRecord.getPole700Klic("a", ", ");
                } catch (NullPointerException e) {
                }
            }
            try {
                if ((oneRecord.getPole260().get("a")!=null) && (oneRecord.getPole260().get("b")!=null)) {
                    mistoVydaniNewStr = oneRecord.getPole260().get("a") + oneRecord.getPole260().get("b");
                } else {
                    mistoVydaniNewStr = "";
                    if (oneRecord.getPole260().get("a")!=null) { mistoVydaniNewStr += oneRecord.getPole260().get("a"); }
                    if (oneRecord.getPole260().get("b")!=null) { 
                        if (!"".equals(mistoVydaniNewStr)) { mistoVydaniNewStr += " "; }
                        mistoVydaniNewStr += oneRecord.getPole260().get("b");
                    }
                    if ("".equals(mistoVydaniNewStr)) { mistoVydaniNewStr = null; }
                }
            } catch (NullPointerException e) {
                mistoVydaniNewStr = null;
            }
            try {
                vydavatelNewStr = oneRecord.getPole260().get("b");
            } catch (NullPointerException e) {
                vydavatelNewStr = null;
            }

            if (oneRecord.getPocetExemplaru()>0) {
                if (1==oneRecord.getPocetExemplaru()) {
                    try {
                        carKodNewStr = oneRecord.getExemplar(0).getCarovyKod();
                    } catch (NullPointerException e) {
                        carKodNewStr = null;
                    }
                    try {
                        signaturaNewStr = oneRecord.getExemplar(0).getSignatura();
                    } catch (NullPointerException e) {
                        signaturaNewStr = null;
                    }
                    //zde jeste doplnit dilci knihovnu
                    //oneRecord.getExemplar(0).getDilciKnihovna();
                } else {
                    Boolean zaznamNalezen = false;
                    int cisloZaznamuLocal = 0;
                    if (!Utils.jePrazdne(this.carKodStr)) {
                        for (int i = 0; i<oneRecord.getPocetExemplaru(); i++) {
                            if (carKodStr.equals(oneRecord.getExemplar(i).getCarovyKod())) {
                                cisloZaznamuLocal = i;
                                zaznamNalezen = true;
                                try {
                                    carKodNewStr = oneRecord.getExemplar(cisloZaznamuLocal).getCarovyKod();
                                } catch (NullPointerException e) {
                                    carKodNewStr = null;
                                }
                                try {
                                    signaturaNewStr = oneRecord.getExemplar(cisloZaznamuLocal).getSignatura();
                                } catch (NullPointerException e) {
                                    signaturaNewStr = null;
                                }
                                //zde jeste doplnit dilci knihovnu
                                //oneRecord.getExemplar(0).getDilciKnihovna();
                            }
                        }
                    }
                    if (!zaznamNalezen) {
                        String caroveKodyLocal = "";
                        LOG.log(Level.SEVERE, " pocetExemplaru: " + oneRecord.getPocetExemplaru());
                        for (int i = 0; i<oneRecord.getPocetExemplaru(); i++) {
                            caroveKodyLocal = caroveKodyLocal + ", " + oneRecord.getExemplar(i).getCarovyKod() + " (" + oneRecord.getExemplar(i).getSignatura() + ")";
                        }
                        if (!Utils.jePrazdne(caroveKodyLocal)) {
                            caroveKodyLocal = caroveKodyLocal.substring(2);
                            throw new WizardException("Není jednoznačně identifikovatelný záznam z Alephu. Vyplňte prosím čarový kód do exempláře. Nalezené čarové kódy: " + caroveKodyLocal);
                        }
                    }
                }
            }
            
            if (!Utils.jePrazdne(ccnbStrNew)) { recForm.getSimpleField("ccnbNew").setValue(ccnbStrNew); }
            recForm.getSimpleField("ccnbNew").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            if (!Utils.jePrazdne(pole001NewStr)) { recForm.getSimpleField("pole001New").setValue(pole001NewStr); }
            recForm.getSimpleField("pole001New").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            if (!Utils.jePrazdne(nazevNewStr)) { recForm.getSimpleField("nazevNew").setValue(nazevNewStr); }
            recForm.getSimpleField("nazevNew").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            if (!Utils.jePrazdne(rokVydaniNewStr)) { recForm.getSimpleField("rokVydaniNew").setValue(rokVydaniNewStr); }
            recForm.getSimpleField("rokVydaniNew").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            if (!Utils.jePrazdne(vydavatelNewStr)) { recForm.getSimpleField("vydavatelNew").setValue(vydavatelNewStr); }
            recForm.getSimpleField("vydavatelNew").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            if (!Utils.jePrazdne(carKodNewStr)) { recForm.getSimpleField("carKodNew").setValue(carKodNewStr); }
            recForm.getSimpleField("carKodNew").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            if (!Utils.jePrazdne(signaturaNewStr)) { recForm.getSimpleField("signaturaNew").setValue(signaturaNewStr); }
            recForm.getSimpleField("signaturaNew").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        }

        return wm;
     }

    public WizardMessage panelLeave(String panelName) throws ValidationException, WizardException {
        WizardMessage wm = new WizardMessage();

        if ("Panel01".equals(panelName)) {
            if ((Boolean) recForm.getFieldValue("zmenit") != true) {
                throw new WizardException("Nebylo zaškrtnuto pole 'Upravit záznam...'.");
            }

            recForm = getWCC().getWizardRecord();
            if (!Utils.jePrazdne(ccnbStrNew)) { recForm.getSimpleField("ccnbNew").setValue(ccnbStrNew); }
            recForm.getSimpleField("ccnbNew").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            //if (!Utils.jePrazdne(carKodStrNew)) { recForm.getSimpleField("carKodNew").setValue(carKodStrNew); }
            //recForm.getSimpleField("carKodNew").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            if (!Utils.jePrazdne(pole001NewStr)) { recForm.getSimpleField("pole001New").setValue(pole001NewStr); }
            recForm.getSimpleField("pole001New").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);

            if (!Utils.jePrazdne(nazevNewStr)) { recForm.getSimpleField("nazevNew").setValue(nazevNewStr); }
            recForm.getSimpleField("nazevNew").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            if (!Utils.jePrazdne(rokVydaniNewStr)) { recForm.getSimpleField("rokVydaniNew").setValue(rokVydaniNewStr); }
            recForm.getSimpleField("rokVydaniNew").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);

            if (!Utils.jePrazdne(autorNewStr)) { recForm.getSimpleField("autorNew").setValue(autorNewStr); }
            recForm.getSimpleField("autorNew").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            if (!Utils.jePrazdne(mistoVydaniNewStr)) { recForm.getSimpleField("mistoVydaniNew").setValue(mistoVydaniNewStr); }
            recForm.getSimpleField("mistoVydaniNew").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            if (!Utils.jePrazdne(vydavatelNewStr)) { recForm.getSimpleField("vydavatelNew").setValue(vydavatelNewStr); }
            recForm.getSimpleField("vydavatelNew").setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            
        } else if ("Panel02".equals(panelName)) {
            if ((Boolean) recForm.getFieldValue("potvrdit") != true) {
                throw new WizardException("Nebylo zaškrtnuto pole 'Potvrdit změny...'.");
            }
        }
        return wm;
    }

    public WizardMessage runBusinessMethod() throws ValidationException, WizardException, ApplicationErrorException {
        WizardMessage wm = new WizardMessage();

        if (!sysNoStr.equals(sysNoNewStr)) {
            if (!Utils.jePrazdne(sysNoNewStr)) {
                recExemplar.getSimpleField(ExemplarEntity.F_bibSysno_STR).setValue(sysNoNewStr);
            }
        }

        recExemplar.getSimpleField(ExemplarEntity.F_bibAutor_STR).setValue(autorNewStr);
        //recExemplar.getSimpleField(ExemplarEntity.F_bibCarKod_STR).setValue(carKodNewStr);
        recExemplar.getSimpleField(ExemplarEntity.F_bibCNB_STR).setValue(ccnbStrNew);
        recExemplar.getSimpleField(ExemplarEntity.F_bibMistoVydani_STR).setValue(mistoVydaniNewStr);
        recExemplar.getSimpleField(ExemplarEntity.F_bibNazev_STR).setValue(nazevNewStr);
        recExemplar.getSimpleField(ExemplarEntity.F_bibPole001_STR).setValue(pole001NewStr);
        recExemplar.getSimpleField(ExemplarEntity.F_bibRokVydani_STR).setValue(rokVydaniNewStr);
        recExemplar.getSimpleField(ExemplarEntity.F_bibSignatura_STR).setValue(signaturaNewStr);
        recExemplar.getSimpleField(ExemplarEntity.F_bibVydavatel_STR).setValue(vydavatelNewStr);
        
        recExemplar.setAnnotation("UlozitBezSpusteniValidaci", AnnotationKeys.TRUE_VALUE);
        
        getWCC().addRootDomain(recExemplar.getDomain());
        getWCC().commit();
        
        wm.addLine("Záznam byl změněn.");
        return wm;
    }
    

}