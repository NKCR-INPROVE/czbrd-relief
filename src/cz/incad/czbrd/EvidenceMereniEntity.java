/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd;

import com.amaio.plaant.businessFunctions.AddException;
import com.amaio.plaant.businessFunctions.AnnotationKeys;
import com.amaio.plaant.businessFunctions.TriggerContext;
import com.amaio.plaant.businessFunctions.UpdateException;
import com.amaio.plaant.businessFunctions.ValidationException;
import com.amaio.plaant.desk.QueryException;
import com.amaio.plaant.metadata.Column;
import com.amaio.plaant.metadata.Columns;
import com.amaio.plaant.metadata.Filter;
import com.amaio.plaant.metadata.Metadata;
import com.amaio.plaant.sync.Record;
import com.amaio.plaant.sync.UniqueKey;
import com.amaio.util.ResourceBundleHelper;
import com.amaio.util.SingletonHashMap;
import cz.incad.czbrd.common.DBB;
import cz.incad.czbrd.common.ReliefUser;
import cz.incad.czbrd.common.Utils;
import cz.incad.czbrd.dLists.SLMetodaOdkyseleni;
import cz.incad.czbrd.dLists.SLMetodaOdkyseleniTechnologie;
import cz.incad.czbrd.dLists.SLdruhZasahuEnum;
import cz.incad.relief3.core.Record_A;
import cz.incad.relief3.core.tools.Commons;
import cz.incad.relief3.core.tools.Exceptions;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *******************************************************************************
 *
 * @author martin
 */
public class EvidenceMereniEntity extends Record_A implements Serializable {

    private static final Logger LOG = Logger.getLogger(EvidenceMereniEntity.class.getName());
    public static final String F_barevnostA_STR = "barevnostA";
    public static final String F_barevnostB_STR = "barevnostB";
    public static final String F_barevnostE_STR = "barevnostE";
    public static final String F_barevnostKde_STR = "barevnostKde";
    public static final String F_barevnostL_STR = "barevnostL";
    public static final String F_barevnostStr_STR = "barevnostStr";
    public static final String F_hmotnost_NUM = "hmotnost";
    public static final String F_druhZasahu_STR = "druhZasahu";
    public static final String F_pObaMechChybCast_BOO = "pObaMechChybCast";
    public static final String F_pObaMechPrehPrek_BOO = "pObaMechPrehPrek";
    //public static final String F_pObaMechCinHlodHmyz_BOO = "pObaMechCinHlodHmyz";
    public static final String F_pObaMechKrehkost_BOO = "pObaMechKrehkost";
    public static final String F_pObaMechLepPas_BOO = "pObaMechLepPas";
    public static final String F_pObaMechNeni_BOO = "pObaMechNeni";
    public static final String F_pObaMechTrhliny_BOO = "pObaMechTrhliny";
    public static final String F_pObaBioHmyz_BOO = "pObaBioHmyz";
    public static final String F_pObaBioHlodavci_BOO = "pObaBioHlodavci";
    public static final String F_pObaBioPlisBak_BOO = "pObaBioPlisBak";
    public static final String F_pObaBioNeni_BOO = "pObaBioNeni";
    public static final String F_pObaChemVoda_BOO = "pObaChemVoda";
    public static final String F_pObaChemJinTek_BOO = "pObaChemJinTek";
    public static final String F_pObaChemNeni_BOO = "pObaChemNeni";
    public static final String F_pObaChemMast_BOO = "pObaChemMast";
    public static final String F_pObaChemPrach_BOO = "pObaChemPrach";
    public static final String F_pObaChemTepPos_BOO = "pObaChemTepPos";
    public static final String F_pObaChemBarSkv_BOO = "pObaChemBarSkv";
    public static final String F_pObaChemNecTex_BOO = "pObaChemNecTex";
    public static final String F_pObaChemZdeg_BOO = "pObaChemZdeg";
    public static final String F_tEMPriloha_TAB = "tEMPriloha";
    public static final String F_rExemplar_REF = "rExemplar";
    public static final String F_rExemplarCom_REF = "rExemplar_EvidenceMereniCom";
    public static final String F_cOrganization_STR = "cOrganization";
    public static final String F_pMechChybCast_BOO = "pMechChybCast";
    public static final String F_pMechPrehPrek_BOO = "pMechPrehPrek";
    public static final String F_pMechSlepList_BOO = "pMechSlepList";
    //public static final String F_pMechCinHlodHmyz_BOO = "pMechCinHlodHmyz";
    public static final String F_pMechKreh_BOO = "pMechKreh";
    public static final String F_pMechNeni_BOO = "pMechNeni";
    public static final String F_pMechLepPas_BOO = "pMechLepPas";
    public static final String F_pChemVoda_BOO = "pChemVoda";
    public static final String F_pChemJinTek_BOO = "pChemJinTek";
    public static final String F_pChemMast_BOO = "pChemMast";
    public static final String F_pChemPrach_BOO = "pChemPrach";
    public static final String F_pChemTepPos_BOO = "pChemTepPos";
    public static final String F_pChemBarSkvr_BOO = "pChemBarSkvr";
    public static final String F_pChemNeni_BOO = "pChemNeni";
    public static final String F_pBioHmyz_BOO = "pBioHmyz";
    public static final String F_pBioHlodavci_BOO = "pBioHlodavci";
    public static final String F_pBioPlisBak_BOO = "pBioPlisBak";
    public static final String F_pBioZvirExk_BOO = "pBioZvirExk";
    public static final String F_pBioNeni_BOO = "pBioNeni";
    public static final String F_pPapMechChybCast_BOO = "pPapMechChybCast";
    public static final String F_pPapMechChybCelStr_BOO = "pPapMechChybCelStr";
    public static final String F_pPapMechChybCelStrVal_STR = "pPapMechChybCelStrVal";
    public static final String F_pPapMechPrehPrek_BOO = "pPapMechPrehPrek";
    public static final String F_pPapMechSlepList_BOO = "pPapMechSlepList";
    //public static final String F_pPapMechCinHlodHmyz_BOO = "pPapMechCinHlodHmyz";
    public static final String F_pPapMechKrehkost_BOO = "pPapMechKrehkost";
    public static final String F_pPapMechLepPas_BOO = "pPapMechLepPas";
    public static final String F_pPapMechNeni_BOO = "pPapMechNeni";
    public static final String F_pPapMechTrhliny_BOO = "pPapMechTrhliny";
    public static final String F_pPapBioHmyz_BOO = "pPapBioHmyz";
    public static final String F_pPapBioHlodavci_BOO = "pPapBioHlodavci";
    public static final String F_pPapBioPlisBak_BOO = "pPapBioPlisBak";
    public static final String F_pPapBioNeni_STR = "pPapBioNeni";
    public static final String F_pPapChemVoda_BOO = "pPapChemVoda";
    public static final String F_pPapChemJinTek_BOO = "pPapChemJinTek";
    public static final String F_pPapChemMast_BOO = "pPapChemMast";
    public static final String F_pPapChemPrach_BOO = "pPapChemPrach";
    public static final String F_pPapChemTepPos_BOO = "pPapChemTepPos";
    public static final String F_pPapChemBarSkv_BOO = "pPapChemBarSkv";
    public static final String F_pPapChemNecTex_BOO = "pPapChemNecTex";
    public static final String F_pPapChemZdeg_BOO = "pPapChemZdeg";
    public static final String F_pPapChemNeni_BOO = "pPapChemNeni";
    public static final String F_posDesky_STR = "posDesky";
    public static final String F_posHrbetnik_STR = "posHrbetnik";
    public static final String F_posKapitalek_STR = "posKapitalek";
    public static final String F_posZalStuzka_STR = "posZalStuzka";
    public static final String F_posPredsadka_STR = "posPredsadka";
    public static final String F_posVazba_STR = "posVazba";
    public static final String F_posObalka_STR = "posObalka";
    public static final String F_mechPosPoznamka_STR = "mechPosPoznamka";
    public static final String F_obalKde_STR = "obalKde";
    public static final String F_obalPH_NUM = "obalPH";
    public static final String F_obalZpusob_STR = "obalZpusob";
    public static final String F_cEvidenceMereni_STR = "cEvidenceMereni";
    public static final String F_bibNazev_STR = "bibNazev";
    public static final String F_pPoznamka_STR = "pPoznamka";
    public static final String F_bibCarKod_STR = "bibCarKod";
    public static final String F_pPapPoznamka_STR = "pPapPoznamka";
    public static final String F_obalkaZpusob_STR = "obalkaZpusob";
    public static final String F_obalkaKde_STR = "obalkaKde";
    public static final String F_obalkaPH_NUM = "obalkaPH";
    public static final String F_kBlokZpusob_STR = "kBlokZpusob";
    public static final String F_kBlokKde_STR = "kBlokKde";
    public static final String F_kBlokStrana_STR = "kBlokStrana";
    public static final String F_kBlokPH_NUM = "kBlokPH";
    public static final String F_merPoznamky_STR = "merPoznamky";
    public static final String F_tSoubor_TAB = "tSoubor";

    public static final String F_taAnalyzy_STR = "taAnalyzy";
    public static final String F_taCisteni_STR = "taCisteni";
    public static final String F_taZasah_STR = "taZasah";
    public static final String F_taPouzPros_STR = "taPouzPros";
    public static final String F_taDopRezim_STR = "taDopRezim";
    public static final String F_cRestaurovani_STR = "cRestaurovani";
    //public static final String F_cNavDopZasah_STR = "cNavDopZasah";
    public static final String F_ndzDezinfekce_BOO = "ndzDezinfekce";
    public static final String F_ndzKontAktPlis_BOO = "ndzKontAktPlis";
    public static final String F_ndzKonzObalka_BOO = "ndzKonzObalka";
    public static final String F_ndzMereniph_BOO = "ndzMereniph";
    public static final String F_ndzOchrannyObal_BOO = "ndzOchrannyObal";
    public static final String F_ndzOdkyseleni_BOO = "ndzOdkyseleni";
    public static final String F_ndzPozn_STR = "ndzPozn";

    //public static final String F_zCMechCisteni_STR_COM = "zCMechCisteni";
    //public static final String F_zCOprava_STR_COM = "zCOprava";
    public static final String F_zDezinfekcePozn_STR = "zDezinfekcePozn";
    public static final String F_zIsDezinfekce_BOO = "zIsDezinfekce";
    public static final String F_zMechCisteniPozn_STR = "zMechCisteniPozn";
    public static final String F_zOpravaPozn_STR = "zOpravaPozn";
    public static final String F_zPouzMatChem_STR = "zPouzMatChem";

    public static final String F_zoDoplneniPokryvuDesek_BOO = "zoDoplneniPokryvuDesek";
    public static final String F_zoDoplneniZtratListu_BOO = "zoDoplneniZtratListu";
    public static final String F_zoNasazeniHrbetniku_BOO = "zoNasazeniHrbetniku";
    public static final String F_zoOpravaTrhlinListu_BOO = "zoOpravaTrhlinListu";
    public static final String F_zoPripevneniDesky_BOO = "zoPripevneniDesky";
    public static final String F_zoVyrovnaniDeformaci_BOO = "zoVyrovnaniDeformaci";
    public static final String F_zoZajisteniUvolnenychCastiJaponskymPapirem_BOO = "zoZajisteniUvolnenychCastiJaponskymPapirem";
    public static final String F_zoZpevneniHranRohuListu_BOO = "zoZpevneniHranRohuListu";

    public static final String F_moAplikace_STR = "moAplikace";
    public static final String F_moCMetodaOdkyseleni_STR_COM = "moCMetodaOdkyseleni";
    public static final String F_moCTechnologie_STR_COM = "moCTechnologie";

    public static final String F_zmcPurus_BOO = "zmcPurus";
    public static final String F_zmcVysavac_BOO = "zmcVysavac";
    public static final String F_zmcWallmaster_BOO = "zmcWallmaster";
    public static final String F_zmcWishab_BOO = "zmcWishab";
    public static final String F_zmcJine_BOO = "zmcJine";

    public static final String F_fyzTypVazby_STR = "fyzTypVazby";
    public static final String F_fyzDruhVazby_STR = "fyzDruhVazby";
    public static final String F_fyzOchrannyObal_STR = "fyzOchrannyObal";
    public static final String F_fyzMaterial_STR = "fyzMaterial";
    public static final String F_fyzZabarveniPapiru_STR = "fyzZabarveniPapiru";
    //public static final String F__STR = "";
    
    public static final String F_poznTypologie_STR = "poznTypologie";
    public static final String F_speczneKnizniBlok_STR = "speczneKnizniBlok";
    public static final String F_speczneHrbetnik_STR = "speczneHrbetnik";
    public static final String F_speczneOrizka_STR = "speczneOrizka";
    public static final String F_speczneObalka_STR = "speczneObalka";
    public static final String F_specznePrachBarva_STR = "specznePrachBarva";
    public static final String F_specznePrachStruktura_STR = "specznePrachStruktura";
    public static final String F_specznePrachPoznamka_STR = "specznePrachPoznamka";
    public static final String F_speczneJineBarva_STR = "speczneJineBarva";
    public static final String F_speczneJineStruktura_STR = "speczneJineStruktura";
    public static final String F_speczneJinePoznamka_STR = "speczneJinePoznamka";
    
    public static final String F_zpcistRucniPevny_BOO = "zpcistRucniPevny";
    public static final String F_zpcistRucniPlasticky_BOO = "zpcistRucniPlasticky";
    public static final String F_zpcistRucniPruzny_BOO = "zpcistRucniPruzny";
    public static final String F_zpcistRucniSypky_BOO = "zpcistRucniSypky";
    public static final String F_zpcistRucniTextil_BOO = "zpcistRucniTextil";
    public static final String F_zpcistRucniOmeteni_BOO = "zpcistRucniOmeteni";
    public static final String F_zpcistManualElektricke_BOO = "zpcistManualElektricke";
    public static final String F_zpcistManualVzduch_BOO = "zpcistManualVzduch";
    public static final String F_zpcistManualVysavac_BOO = "zpcistManualVysavac";
    public static final String F_zpcistStrojniAutomat_BOO = "zpcistStrojniAutomat";
    public static final String F_zpcistStrojniPoloautomat_BOO = "zpcistStrojniPoloautomat";
    public static final String F_zpcistStrojniBox_BOO = "zpcistStrojniBox";
    public static final String F_zpcistPoznamka_BOO = "zpcistPoznamka";
    public static final String F_zpcistPopis_BOO = "zpcistPopis";   
    
    public static final String F_barevnostTypPapiru_STR = "barevnostTypPapiru";  
    public static final String F_barevnostPH_STR = "barevnostPH";  
    public static final String F_barevnostPolymerStup_INT = "barevnostPolymerStup";  
    public static final String F_barevnostPevnostTah_INT = "barevnostPevnostTah";  
    public static final String F_barevnostPevnostTahOhyb_INT = "barevnostPevnostTahOhyb";  
    public static final String F_barevnostObsahLignin_INT = "barevnostObsahLignin";  
    public static final String F_barevnostObsahBilkovin_INT = "barevnostObsahBilkovin";  
    public static final String F_barevnostObsahPryskyrice_INT = "barevnostObsahPryskyrice";
    public static final String F_barevnostPritomnostOzp_BOO = "barevnostPritomnostOzp";
    
    public static final String F_POZNTYPOLOGIE_STR = "poznTypologie";
    
    public static final String VALIDATION_DONE = "validation_done";

    /**
     ***************************************************************************
     *
     * @param mtdt
     * @return
     * @throws QueryException
     */
    @Override
    public Metadata onMetadataChanged(Metadata mtdt) throws QueryException {
        Columns columns;
        Column column_cOrganization;
        ReliefUser ru;

        super.onMetadataChanged(mtdt);

        //Vyjímka pro jádro - používá se pro třídění
        Filter testFilter = mtdt.getFilter();
        if (testFilter != null) {
            for (int i = 0; i < testFilter.getRulesCount(); i++) {
                if ("ID".equals(testFilter.getRule(i).getColumn().getColumnName())) {
                    return mtdt;
                }
            }
        }

        ru = new ReliefUser(getTC());
        if (ru.isSystemAdmin()) {
            //System administrator - smí vidět všechno
            return mtdt;
        }

        //Přidáme security sloupec když chybí
        columns = mtdt.getColumns();
        if (!columns.containsColumn(F_cOrganization_STR)) {
            column_cOrganization = DBB.createColumn(EvidenceMereniEntity.class.getName(), F_cOrganization_STR);
            if (column_cOrganization != null) {
                columns.addColumn(column_cOrganization);
            }
        }

        return mtdt;
    }

    /**
     ***************************************************************************
     *
     * @param rec
     * @return
     */
    @Override
    public Record onGetRecord(Record rec) {

        //super.onGetRecord(rec);
        ReliefUser ru = new ReliefUser(getTC());
        String organizationRecord;
        String organizationUser;
        String userLogin;
        String userRecord;

        //Systémový administrátor je bez omezení
        if (ru.isSystemAdmin()) {
            return rec;
        }

        //Oprávnění pro ostatní uživatelské role
        organizationUser = ru.getOrganization();
        organizationRecord = (String) rec.getSimpleField(F_cOrganization_STR).getValue();
        userLogin = ru.getLogin();
        userRecord = (String) rec.getSimpleField(F_recCreUser_STR).getValue();

        //Kontorla na to jestli záznam i uživatel má přiřazenu organizaci
        if (organizationRecord == null || organizationUser == null) {
            rec.setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            rec.setAnnotation(AnnotationKeys.REMOVE_FORBIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            LOG.log(Level.WARNING, "User/Record is securityless: {0}/{1}", new Object[]{ru.getLogin(), rec.getSimpleField(F_recID_STR).getValue()});
            return rec;
        } else {
            //vynulujeme hodnoty anotací
            rec.setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.FALSE_VALUE);
            rec.setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.FALSE_VALUE);
            rec.setAnnotation(AnnotationKeys.REMOVE_FORBIDDEN_SECURITY_PROPERTY, AnnotationKeys.FALSE_VALUE);
        }

        if (ru.isExplorer()) {
            //průzkumník - smí editovat všechny záznamy své organizace
            if (!organizationUser.equals(organizationRecord)) {
                rec.setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            }
            //Kontrola jestli je uživatel korektor
            if (!ru.isCorrector()) {
                //Průzkumník - smí mazat jen své záznamy
                if (!userLogin.equals(userRecord)) {
                    rec.setAnnotation(AnnotationKeys.REMOVE_FORBIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
                }
            }
        } else if (ru.isEditorPh()) {               
                setFiledsForEditorPh(rec);
                rec.setAnnotation(AnnotationKeys.REMOVE_FORBIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            
        }  else {
            //ostatní uživatelé - nesmí nic.
            rec.setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            rec.setAnnotation(AnnotationKeys.REMOVE_FORBIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        }

        return rec;
    }

    /**
     ***************************************************************************
     *
     * @param rec
     * @return
     * @throws AddException
     */
    @Override
    public Record onCreateLocal(Record rec) throws AddException {
        LOG.log(Level.INFO, "create Local: " + (String) rec.getFieldValue(EvidenceMereniEntity.F_recID_STR));
        //Kontrola jestli uživatel smí zkládat záznamy
        ReliefUser ru = new ReliefUser(getTC());

        if (ru.getOrganization() == null || ru.getOrganization().length() == 0) {
            throw new AddException("Váš uživatelský účet nemá přiřazenu organizaci, což je podmínka nutná pro zakládání nových záznamů.");
        }

        if (!(ru.isExplorer() || ru.isSystemAdmin() || ru.isEditorPh())) {
            throw new AddException("Váš uživatelský účet nemá oprávnění pro zakládání nových záznamů v této agendě.");
        }

        if (rec.getFieldValue(Record_A.F_recID_STR) == null) {
            if (rec.getReferencedField(F_rExemplar_REF).getReferencedRecord() != null) {
                if (!ru.getOrganization().equals(rec.getReferencedField(F_rExemplar_REF).getReferencedRecord().getSimpleField(ExemplarEntity.F_cOrganization_STR).getValue())) {
                    throw new AddException("Záznam nadřízeného záznau patří jiné organizaci, nemáte oprávnění zakládat pod ním nové záznamy.");
                }
            }
        } else {
            //Vyčištění dat v případě že se jedná o záznam vytvořený kopií
            rec.getTableField(EvidenceMereniEntity.F_tSoubor_TAB).setKeys(new UniqueKey[]{});
            rec.getTableField(EvidenceMereniEntity.F_tEMPriloha_TAB).setKeys(new UniqueKey[]{});

            rec.getSimpleField(EvidenceMereniEntity.F_obalPH_NUM).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_obalkaPH_NUM).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_kBlokPH_NUM).setValue(null);

            rec.getSimpleField(EvidenceMereniEntity.F_kBlokZpusob_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_kBlokKde_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_kBlokStrana_STR).setValue(null);

            rec.getSimpleField(EvidenceMereniEntity.F_obalZpusob_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_obalKde_STR).setValue(null);

            rec.getSimpleField(EvidenceMereniEntity.F_obalkaKde_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_obalkaZpusob_STR).setValue(null);

            rec.getSimpleField(EvidenceMereniEntity.F_barevnostKde_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_barevnostStr_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_barevnostA_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_barevnostB_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_barevnostE_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_barevnostL_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_hmotnost_NUM).setValue(null);

            rec.getSimpleField(EvidenceMereniEntity.F_mechPosPoznamka_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_pPapPoznamka_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_pPoznamka_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_merPoznamky_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_pPapMechChybCelStrVal_STR).setValue(null);

            rec.getSimpleField(EvidenceMereniEntity.F_posDesky_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_posHrbetnik_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_posKapitalek_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_posZalStuzka_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_posPredsadka_STR).setValue(null);
            rec.getSimpleField(EvidenceMereniEntity.F_posVazba_STR).setValue(null);

            rec.getSimpleField(EvidenceMereniEntity.F_posObalka_STR).setValue(null);
        }

        super.onCreateLocal(rec);

        //Nastavíme defaultní hodnoty
        rec.getSimpleField(F_druhZasahu_STR).setValue(SLdruhZasahuEnum.puvodnistav.toString());
        //Nastavíme záznamu stejnou organizaci jako má uživatel
        rec.getSimpleField(ExemplarEntity.F_cOrganization_STR).setValue(ru.getOrganization());

        return rec;
    }

    /**
     ***************************************************************************
     *
     * @param rec
     * @return
     * @throws AddException
     * @throws ValidationException
     */
    @Override
    public Record onCreate(Record rec) throws AddException, ValidationException {
        rec = super.onCreate(rec);
        return this.onCreateUpdate(rec);
    }

    /**
     ***************************************************************************
     *
     * @param rec
     * @return
     * @throws ValidationException
     * @throws UpdateException
     */
    @Override
    public Record onUpdate(Record rec) throws ValidationException, UpdateException {
        rec = super.onUpdate(rec);
        return this.onCreateUpdate(rec);
    }

    /**
     ***************************************************************************
     *
     * @param rec
     * @return
     */
    private Record onCreateUpdate(Record rec) throws ValidationException {
        ValidationException vex = Exceptions.getValiadtionException(getTC());
        String value;

        if (AnnotationKeys.TRUE_VALUE.equals(rec.getAnnotation("UlozitBezSpusteniValidaci"))) { //pro aktualizaci záznamu z dat v Alephu
            
        } else {
            //LOG.log(Level.WARNING, "validace mereni:");
            LOG.log(Level.INFO, "zaznam: " + (String) rec.getFieldValue(EvidenceMereniEntity.F_rExemplarCom_REF));
            if (AnnotationKeys.TRUE_VALUE.equals(rec.getAnnotation(EvidenceMereniEntity.VALIDATION_DONE))) {
                // kontrola již proběhla
            } else if (Boolean.TRUE.equals(rec.getAnnotation(Commons.ANNOTATION_VALIDATION_EXCEPTION_IGNORE_WARNING))) {
                LOG.log(Level.INFO, "záznam má anotaci nekontrolovat: " + (String) rec.getFieldValue(EvidenceMereniEntity.F_recID_STR));
                //pokud voláno z bussines funkce či importu, nevalidovat záznamy
            } else if (rec.getFieldValue(EvidenceMereniEntity.F_rExemplarCom_REF) == null) {
                LOG.log(Level.INFO, "záznam je historický: " + (String) rec.getFieldValue(EvidenceMereniEntity.F_recID_STR));
                //starsi verze co jsou historii - ty nekontrolovat
            } else {
                /*EvidenceMereniEntity.*/formValidation(rec, getTC(), vex);
            }
            //LOG.log(Level.WARNING, "validace mereni konec:");

            if (vex.isGravid() && !vex.isWarning()) {
                throw vex;
            }

            //Když je vybrán Ochranný obal není, tak se nastaví na NULL materiál
            if ("neni".equals(rec.getSimpleField(F_fyzOchrannyObal_STR).getValue()) || rec.getSimpleField(F_fyzOchrannyObal_STR).getValue() == null) {
                rec.getSimpleField(F_fyzMaterial_STR).setValue(null);
            }

            //Když je vybrána Obálka není, tak je hodnota nastavena na NULL
            //if ("neni".equals(rec.getSimpleField(F_posObalka_STR).getValue()) || rec.getSimpleField(F_posObalka_STR).getValue() == null) {
            if ("neni".equals(rec.getSimpleField(F_posObalka_STR).getValue())) {
                rec.getSimpleField(F_pObaMechChybCast_BOO).setValue(false);
                rec.getSimpleField(F_pObaMechPrehPrek_BOO).setValue(false);
                //rec.getSimpleField(F_pObaMechCinHlodHmyz_BOO).setValue(false);
                rec.getSimpleField(F_pObaMechKrehkost_BOO).setValue(false);
                rec.getSimpleField(F_pObaMechLepPas_BOO).setValue(false);
                rec.getSimpleField(F_pObaMechNeni_BOO).setValue(false);
                rec.getSimpleField(F_pObaMechTrhliny_BOO).setValue(false);
                rec.getSimpleField(F_pObaBioHmyz_BOO).setValue(false);
                rec.getSimpleField(F_pObaBioHlodavci_BOO).setValue(false);
                rec.getSimpleField(F_pObaBioPlisBak_BOO).setValue(false);
                rec.getSimpleField(F_pObaBioNeni_BOO).setValue(false);
                rec.getSimpleField(F_pObaChemNeni_BOO).setValue(false);
                rec.getSimpleField(F_pObaChemVoda_BOO).setValue(false);
                rec.getSimpleField(F_pObaChemJinTek_BOO).setValue(false);
                rec.getSimpleField(F_pObaChemMast_BOO).setValue(false);
                rec.getSimpleField(F_pObaChemPrach_BOO).setValue(false);
                rec.getSimpleField(F_pObaChemTepPos_BOO).setValue(false);
                rec.getSimpleField(F_pObaChemBarSkv_BOO).setValue(false);
                rec.getSimpleField(F_pObaChemNecTex_BOO).setValue(false);
                rec.getSimpleField(F_pObaChemZdeg_BOO).setValue(false);
            }

            value = (String) rec.getFieldValue(EvidenceMereniEntity.F_moCTechnologie_STR_COM);
            // dopočítání hodnoty aplikace metody odkyselení
            if (value != null) {
                if (SLMetodaOdkyseleniTechnologie.VALUE_H01.equals(value)) {
                    rec.getSimpleField(EvidenceMereniEntity.F_moAplikace_STR).setValue("ponor");
                } else if (SLMetodaOdkyseleniTechnologie.VALUE_H02.equals(value)) {
                    rec.getSimpleField(EvidenceMereniEntity.F_moAplikace_STR).setValue("ponor");
                } else if (SLMetodaOdkyseleniTechnologie.VALUE_H03.equals(value)) {
                    rec.getSimpleField(EvidenceMereniEntity.F_moAplikace_STR).setValue("ponor");
                } else if (SLMetodaOdkyseleniTechnologie.VALUE_H04.equals(value)) {
                    rec.getSimpleField(EvidenceMereniEntity.F_moAplikace_STR).setValue("postřik, ponor");
                } else if (SLMetodaOdkyseleniTechnologie.VALUE_H05.equals(value)) {
                    rec.getSimpleField(EvidenceMereniEntity.F_moAplikace_STR).setValue("naprašování");
                } else if (SLMetodaOdkyseleniTechnologie.VALUE_H06.equals(value)) {
                    rec.getSimpleField(EvidenceMereniEntity.F_moAplikace_STR).setValue("ponor");
                } else if (SLMetodaOdkyseleniTechnologie.VALUE_H07.equals(value)) {
                    rec.getSimpleField(EvidenceMereniEntity.F_moAplikace_STR).setValue("postřik");
                } else if (SLMetodaOdkyseleniTechnologie.VALUE_H08.equals(value)) {
                    rec.getSimpleField(EvidenceMereniEntity.F_moAplikace_STR).setValue("sendvič");
                }
            }

            // dopočítání hodnoty metody odkyselení
            if (value == null) {
                rec.getSimpleField(EvidenceMereniEntity.F_moCMetodaOdkyseleni_STR_COM).setValue(null);
            } else if (value.startsWith("H")) {
                rec.getSimpleField(EvidenceMereniEntity.F_moCMetodaOdkyseleni_STR_COM).setValue(SLMetodaOdkyseleni.VALUE_HROMADNA);
            } else {
                rec.getSimpleField(EvidenceMereniEntity.F_moCMetodaOdkyseleni_STR_COM).setValue(SLMetodaOdkyseleni.VALUE_INDIVIDUALNI);
            }

        }
        return rec;
    }

    /**
     ***************************************************************************
     *
     * @param rec
     * @param context
     * @param vex
     */
    public static void formValidationExt(Record rec, TriggerContext context, ValidationException vex) {
        EvidenceMereniEntity.formValidation(rec, context, vex);
    }

    private static String nazevPole(String nazevPole, String localName, TriggerContext context) {
        String nazevPoleKonecne = null;
        nazevPoleKonecne = context.getLocalizedString(nazevPole);
        if (nazevPole.equals(nazevPoleKonecne)) {
            nazevPoleKonecne = context.getLocalizedString(localName + "." + nazevPole);
        }
        //LOG.log(Level.WARNING, "nazevPole: " + nazevPole);
        return nazevPoleKonecne;
    }
    
    /**
     ***************************************************************************
     *
     * @param rec
     * @param context
     * @param vex
     */
    private static void formValidation(Record rec, TriggerContext context, ValidationException vex) {
        // Kontrola Pokud je vazba nastavena na "jine", poznamka musi byt vyplnena
        //LOG.log(Level.WARNING, "kontrola na poznamku: " + rec.getFieldValue(EvidenceMereniEntity.F_fyzTypVazby_STR) + " - " + rec.getFieldValue(EvidenceMereniEntity.F_POZNTYPOLOGIE_STR));
        if (("zzzjina".equals(rec.getFieldValue(EvidenceMereniEntity.F_fyzTypVazby_STR))) && (rec.getFieldValue(EvidenceMereniEntity.F_POZNTYPOLOGIE_STR) == null)) {
//            vex.addField(EvidenceMereniEntity.F_POZNTYPOLOGIE_STR, nazevPole(rec.getLocalName(), EvidenceMereniEntity.F_POZNTYPOLOGIE_STR), "Typologie – Poznámky: Vyplňte pole Poznámky!", false);
            vex.addField(EvidenceMereniEntity.F_POZNTYPOLOGIE_STR, nazevPole(EvidenceMereniEntity.F_POZNTYPOLOGIE_STR, rec.getLocalName(), context), "Typologie – Poznámky: Vyplňte pole Poznámky!", false);
        }
        
        //kontroly na nova povinna pole - 2016.11.01
        if (rec.getFieldValue(EvidenceMereniEntity.F_fyzTypVazby_STR) == null) {
            vex.addField(EvidenceMereniEntity.F_fyzTypVazby_STR, nazevPole(EvidenceMereniEntity.F_fyzTypVazby_STR, rec.getLocalName(), context), "Identifikace exempláře – Typ fondu: Vyplňte pole Typ fondu!", false);
        }
        if (rec.getFieldValue(EvidenceMereniEntity.F_fyzDruhVazby_STR) == null) {
            vex.addField(EvidenceMereniEntity.F_fyzDruhVazby_STR, nazevPole(EvidenceMereniEntity.F_fyzDruhVazby_STR, rec.getLocalName(), context), "Identifikace exempláře – Druh vazby: Vyplňte pole Druh vazby!", false);
        }
        if (rec.getFieldValue(EvidenceMereniEntity.F_fyzOchrannyObal_STR) == null) {
            vex.addField(EvidenceMereniEntity.F_fyzOchrannyObal_STR, nazevPole(EvidenceMereniEntity.F_fyzOchrannyObal_STR, rec.getLocalName(), context), "Identifikace exempláře – Ochranný obal: Vyplňte pole Ochranný obal!", false);
        }
        if (rec.getFieldValue(EvidenceMereniEntity.F_fyzZabarveniPapiru_STR) == null) {
            vex.addField(EvidenceMereniEntity.F_fyzZabarveniPapiru_STR, nazevPole(EvidenceMereniEntity.F_fyzZabarveniPapiru_STR, rec.getLocalName(), context), "Identifikace exempláře – Zabarvení papíru: Vyplňte pole Zabarvení papíru!", false);
        }

        if (("elektroda".equals(rec.getFieldValue(EvidenceMereniEntity.F_obalkaZpusob_STR))) || ("tuzka".equals(rec.getFieldValue(EvidenceMereniEntity.F_obalkaZpusob_STR)))) {
            if (rec.getFieldValue(EvidenceMereniEntity.F_obalkaKde_STR) == null) {
                vex.addField(EvidenceMereniEntity.F_obalkaKde_STR, nazevPole(EvidenceMereniEntity.F_obalkaKde_STR, rec.getLocalName(), context), "Měření – Kde: Vyplňte pole Kde!", false);
            }
            if (rec.getFieldValue(EvidenceMereniEntity.F_obalkaPH_NUM) == null) {
                vex.addField(EvidenceMereniEntity.F_obalkaPH_NUM, nazevPole(EvidenceMereniEntity.F_obalkaPH_NUM, rec.getLocalName(), context), "Měření - PH: Vyplňte pole pH!", false);
            } else {
//            } else if ((Integer.parseInt(rec.getFieldValue(EvidenceMereniEntity.F_obalkaPH_NUM).toString()) < 1) || (Integer.parseInt(rec.getFieldValue(EvidenceMereniEntity.F_obalkaPH_NUM).toString()) > 12)) {
                //Zakrouhleni PH
                Utils.roundSimpleField(rec.getSimpleField(EvidenceMereniEntity.F_obalkaPH_NUM));
                //kontrola hodnoty pH Obalka
                if (!Utils.isValid_pH(rec.getFieldValue(EvidenceMereniEntity.F_obalkaPH_NUM))) {
                    vex.addField(EvidenceMereniEntity.F_obalkaPH_NUM, nazevPole(EvidenceMereniEntity.F_obalkaPH_NUM, rec.getLocalName(), context), "Měření - pH: Hodnota pH je mimo povolené rozmezí 1,0-12,0!", false);
                }
//                vex.addField(EvidenceMereniEntity.F_obalkaKde_STR, "Měření pH obálky: Hodnota pH je mimo povolené rozmezí 1,0-12,0!", false);
            }
        }
        if (("elektroda".equals(rec.getFieldValue(EvidenceMereniEntity.F_kBlokZpusob_STR))) || ("tuzka".equals(rec.getFieldValue(EvidenceMereniEntity.F_kBlokZpusob_STR)))) {
            if (rec.getFieldValue(EvidenceMereniEntity.F_kBlokKde_STR) == null) {
                vex.addField(EvidenceMereniEntity.F_kBlokKde_STR, nazevPole(EvidenceMereniEntity.F_kBlokKde_STR, rec.getLocalName(), context), "Měření – Kde: Vyplňte pole Kde!", false);
            }
            if (rec.getFieldValue(EvidenceMereniEntity.F_kBlokPH_NUM) == null) {
                vex.addField(EvidenceMereniEntity.F_kBlokPH_NUM, nazevPole(EvidenceMereniEntity.F_kBlokPH_NUM, rec.getLocalName(), context), "Měření - pH: Vyplňte pole pH!", false);
            } else {
//            } else if ((Integer.parseInt(rec.getFieldValue(EvidenceMereniEntity.F_kBlokPH_NUM).toString()) < 1) || (Integer.parseInt(rec.getFieldValue(EvidenceMereniEntity.F_kBlokPH_NUM).toString()) > 12)) {
                //Zakrouhleni PH
                Utils.roundSimpleField(rec.getSimpleField(EvidenceMereniEntity.F_kBlokPH_NUM));
                //kontrola hodnoty pH Knizni blok
                if (!Utils.isValid_pH(rec.getFieldValue(EvidenceMereniEntity.F_kBlokPH_NUM))) {
                    vex.addField(EvidenceMereniEntity.F_kBlokPH_NUM, nazevPole(EvidenceMereniEntity.F_kBlokPH_NUM, rec.getLocalName(), context), "Měření - pH: Hodnota pH je mimo povolené rozmezí 1,0-12,0!", false);
                }
//                vex.addField(EvidenceMereniEntity.F_kBlokPH_NUM, "Měření pH k.bloku: Hodnota pH je mimo povolené rozmezí 1,0-12,0!", false);
            }
        }
        if (("elektroda".equals(rec.getFieldValue(EvidenceMereniEntity.F_obalZpusob_STR))) || ("tuzka".equals(rec.getFieldValue(EvidenceMereniEntity.F_obalZpusob_STR)))) {
            if (rec.getFieldValue(EvidenceMereniEntity.F_obalKde_STR) == null) {
                vex.addField(EvidenceMereniEntity.F_obalKde_STR, nazevPole(EvidenceMereniEntity.F_obalKde_STR, rec.getLocalName(), context), "Měření - Kde: Vyplňte pole Kde!", false);
            }
            if (rec.getFieldValue(EvidenceMereniEntity.F_obalPH_NUM) == null) {
                vex.addField(EvidenceMereniEntity.F_obalPH_NUM, nazevPole(EvidenceMereniEntity.F_obalPH_NUM, rec.getLocalName(), context), "Měření - pH: Vyplňte pole pH!", false);
            } else {
//            } else if ((Integer.parseInt(rec.getFieldValue(EvidenceMereniEntity.F_obalPH_NUM).toString()) < 1) || (Integer.parseInt(rec.getFieldValue(EvidenceMereniEntity.F_obalPH_NUM).toString()) > 12)) {
                //Zakrouhleni PH
                Utils.roundSimpleField(rec.getSimpleField(EvidenceMereniEntity.F_obalPH_NUM));
                //kontrola hodnoty pH Obal
                if (!Utils.isValid_pH(rec.getFieldValue(EvidenceMereniEntity.F_obalPH_NUM))) {
                    vex.addField(EvidenceMereniEntity.F_obalPH_NUM, nazevPole(EvidenceMereniEntity.F_obalPH_NUM, rec.getLocalName(), context), "Měření - pH: Hodnota pH je mimo povolené rozmezí 1,0-12,0", false);
                }
//                vex.addField(EvidenceMereniEntity.F_obalPH_NUM, "Měření pH ochr.obalu: Hodnota pH je mimo povolené rozmezí 1,0-12,0!", false);
            }
        }
        // konec novych povinnych poli - 2016.11.01
        
        //Kontrola na vyplnění pole druh zásahu
        if (rec.getFieldValue(EvidenceMereniEntity.F_druhZasahu_STR) == null) {
            vex.addField(EvidenceMereniEntity.F_druhZasahu_STR, nazevPole(EvidenceMereniEntity.F_druhZasahu_STR, rec.getLocalName(), context), "Pole musí být vyplněno.", false);
        }
        
        // kontrola Restaurovani na vyplnění Zasah a PouziteProstredky u restaurovani
        if ("restaurovani".equals(rec.getFieldValue(EvidenceMereniEntity.F_druhZasahu_STR))) {
            if (rec.getFieldValue(EvidenceMereniEntity.F_taZasah_STR) == null) {
                vex.addField(EvidenceMereniEntity.F_taZasah_STR, nazevPole(EvidenceMereniEntity.F_taZasah_STR, rec.getLocalName(), context), "Poznámky k zásahu - Zásah: - Vyplňte pole Zásah!", false);
            }
            if (rec.getFieldValue(EvidenceMereniEntity.F_taPouzPros_STR) == null) {
                vex.addField(EvidenceMereniEntity.F_taPouzPros_STR, nazevPole(EvidenceMereniEntity.F_taPouzPros_STR, rec.getLocalName(), context), "Poznámky k zásahu - Použité prostředky: - Vyplňte pole Použité prostředky!", false);
            }
        }

        // kontrola Konzervace na vyplnění Zasah u konzervace
        if ("konzervace".equals(rec.getFieldValue(EvidenceMereniEntity.F_druhZasahu_STR))) {
            if (rec.getFieldValue(EvidenceMereniEntity.F_zPouzMatChem_STR) == null) {
                vex.addField(EvidenceMereniEntity.F_zPouzMatChem_STR, nazevPole(EvidenceMereniEntity.F_POZNTYPOLOGIE_STR, rec.getLocalName(), context), "Zásah - Použitý materiál/chemikálie: Vyplňte pole Použitý materiál/chemikálie!", false);
            }
        }
        
        //kontrola na vyplnění pole není a zároveň jiné volby u Poškození...
        //exemplar
        if ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pMechNeni_BOO))) {
            if (("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pMechChybCast_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pMechKreh_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pMechLepPas_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pMechPrehPrek_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pMechSlepList_BOO)))) {
                rec.getSimpleField(EvidenceMereniEntity.F_pMechNeni_BOO).setValue(Boolean.FALSE);
            }
        }
        if ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pBioNeni_BOO))) {
            if (("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pBioHlodavci_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pBioHmyz_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pBioPlisBak_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pBioZvirExk_BOO)))) {
                rec.getSimpleField(EvidenceMereniEntity.F_pBioNeni_BOO).setValue(Boolean.FALSE);
            }
        }
        if ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pChemNeni_BOO))) {
            if (("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pChemBarSkvr_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pChemJinTek_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pChemMast_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pChemPrach_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pChemTepPos_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pChemVoda_BOO)))) {
                rec.getSimpleField(EvidenceMereniEntity.F_pChemNeni_BOO).setValue(Boolean.FALSE);
            }
        }
        //obalka
        if ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaMechNeni_BOO))) {
            if (("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaMechChybCast_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaMechKrehkost_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaMechLepPas_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaMechPrehPrek_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaMechTrhliny_BOO)))) {
                rec.getSimpleField(EvidenceMereniEntity.F_pObaMechNeni_BOO).setValue(Boolean.FALSE);
            }
        }
        if ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaBioNeni_BOO))) {
            if (("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaBioHlodavci_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaBioHmyz_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaBioPlisBak_BOO)))) {
                rec.getSimpleField(EvidenceMereniEntity.F_pObaBioNeni_BOO).setValue(Boolean.FALSE);
            }
        }
        if ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaChemNeni_BOO))) {
            if (("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaChemBarSkv_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaChemJinTek_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaChemMast_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaChemPrach_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaChemTepPos_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pObaChemVoda_BOO)))) {
                rec.getSimpleField(EvidenceMereniEntity.F_pObaChemNeni_BOO).setValue(Boolean.FALSE);
            }
        }
        //papir
        if ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapMechNeni_BOO))) {
            if (("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapMechChybCast_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapMechChybCelStr_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapMechKrehkost_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapMechLepPas_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapMechPrehPrek_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapMechSlepList_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapMechTrhliny_BOO)))) {
                rec.getSimpleField(EvidenceMereniEntity.F_pPapMechNeni_BOO).setValue(Boolean.FALSE);
            }
        }
        if ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapBioNeni_STR))) {
            if (("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapBioHlodavci_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapBioHmyz_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapBioPlisBak_BOO)))) {
                rec.getSimpleField(EvidenceMereniEntity.F_pPapBioNeni_STR).setValue(Boolean.FALSE);
            }
        }
        if ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapChemNeni_BOO))) {
            if (("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapChemBarSkv_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapChemJinTek_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapChemMast_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapChemNecTex_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapChemPrach_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapChemTepPos_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapChemVoda_BOO)))
                    || ("true".equals(rec.getFieldValue(EvidenceMereniEntity.F_pPapChemZdeg_BOO)))) {
                rec.getSimpleField(EvidenceMereniEntity.F_pPapChemNeni_BOO).setValue(Boolean.FALSE);
            }
        }


        if (!Utils.isValid_Hmotnost(rec.getFieldValue(EvidenceMereniEntity.F_hmotnost_NUM))) {
            vex.addField(EvidenceMereniEntity.F_hmotnost_NUM, "Pole smí nabývat pouze hodnot 0 - 999999", false);
        }
        
        Utils.onePoskozeniValidation(
                rec,
                rec.getSimpleField(EvidenceMereniEntity.F_pObaMechNeni_BOO),
                new String[]{
                    EvidenceMereniEntity.F_pObaMechChybCast_BOO,
                    //EvidenceMereniEntity.F_pObaMechCinHlodHmyz_BOO,
                    EvidenceMereniEntity.F_pObaMechKrehkost_BOO,
                    EvidenceMereniEntity.F_pObaMechLepPas_BOO,
                    EvidenceMereniEntity.F_pObaMechPrehPrek_BOO,
                    EvidenceMereniEntity.F_pObaMechTrhliny_BOO
                }
        );

        Utils.onePoskozeniValidation(rec,
                rec.getSimpleField(EvidenceMereniEntity.F_pObaBioNeni_BOO),
                new String[]{
                    EvidenceMereniEntity.F_pObaBioHlodavci_BOO,
                    EvidenceMereniEntity.F_pObaBioHmyz_BOO,
                    EvidenceMereniEntity.F_pObaBioPlisBak_BOO
                }
        );

        Utils.onePoskozeniValidation(rec,
                rec.getSimpleField(EvidenceMereniEntity.F_pObaChemNeni_BOO),
                new String[]{
                    EvidenceMereniEntity.F_pObaChemBarSkv_BOO,
                    EvidenceMereniEntity.F_pObaChemJinTek_BOO,
                    EvidenceMereniEntity.F_pObaChemMast_BOO,
                    EvidenceMereniEntity.F_pObaChemNecTex_BOO,
                    EvidenceMereniEntity.F_pObaChemPrach_BOO,
                    EvidenceMereniEntity.F_pObaChemTepPos_BOO,
                    EvidenceMereniEntity.F_pObaChemVoda_BOO,
                    EvidenceMereniEntity.F_pObaChemZdeg_BOO
                }
        );

        Utils.onePoskozeniValidation(rec,
                rec.getSimpleField(EvidenceMereniEntity.F_pPapMechNeni_BOO),
                new String[]{
                    EvidenceMereniEntity.F_pPapMechChybCelStr_BOO,
                    EvidenceMereniEntity.F_pPapMechChybCast_BOO,
                    //EvidenceMereniEntity.F_pPapMechCinHlodHmyz_BOO,
                    EvidenceMereniEntity.F_pPapMechKrehkost_BOO,
                    EvidenceMereniEntity.F_pPapMechLepPas_BOO,
                    EvidenceMereniEntity.F_pPapMechPrehPrek_BOO,
                    EvidenceMereniEntity.F_pPapMechSlepList_BOO,
                    EvidenceMereniEntity.F_pPapMechTrhliny_BOO
                }
        );

        Utils.onePoskozeniValidation(rec,
                rec.getSimpleField(EvidenceMereniEntity.F_pPapBioNeni_STR),
                new String[]{
                    EvidenceMereniEntity.F_pPapBioHlodavci_BOO,
                    EvidenceMereniEntity.F_pPapBioHmyz_BOO,
                    EvidenceMereniEntity.F_pPapBioPlisBak_BOO
                }
        );

        Utils.onePoskozeniValidation(rec,
                rec.getSimpleField(EvidenceMereniEntity.F_pPapChemNeni_BOO),
                new String[]{
                    EvidenceMereniEntity.F_pPapChemBarSkv_BOO,
                    EvidenceMereniEntity.F_pPapChemJinTek_BOO,
                    EvidenceMereniEntity.F_pPapChemMast_BOO,
                    EvidenceMereniEntity.F_pPapChemNecTex_BOO,
                    EvidenceMereniEntity.F_pPapChemPrach_BOO,
                    EvidenceMereniEntity.F_pPapChemTepPos_BOO,
                    EvidenceMereniEntity.F_pPapChemVoda_BOO,
                    EvidenceMereniEntity.F_pPapChemZdeg_BOO
                }
        );

        Utils.onePoskozeniValidation(rec,
                rec.getSimpleField(EvidenceMereniEntity.F_pMechNeni_BOO),
                new String[]{
                    EvidenceMereniEntity.F_pMechChybCast_BOO,
                    //EvidenceMereniEntity.F_pMechCinHlodHmyz_BOO,
                    EvidenceMereniEntity.F_pMechKreh_BOO,
                    EvidenceMereniEntity.F_pMechLepPas_BOO,
                    EvidenceMereniEntity.F_pMechPrehPrek_BOO,
                    EvidenceMereniEntity.F_pMechSlepList_BOO
                }
        );

        Utils.onePoskozeniValidation(rec,
                rec.getSimpleField(EvidenceMereniEntity.F_pBioNeni_BOO),
                new String[]{
                    EvidenceMereniEntity.F_pBioHlodavci_BOO,
                    EvidenceMereniEntity.F_pBioHmyz_BOO,
                    EvidenceMereniEntity.F_pBioPlisBak_BOO,
                    EvidenceMereniEntity.F_pBioZvirExk_BOO
                }
        );

        Utils.onePoskozeniValidation(rec,
                rec.getSimpleField(EvidenceMereniEntity.F_pChemNeni_BOO),
                new String[]{
                    EvidenceMereniEntity.F_pChemBarSkvr_BOO,
                    EvidenceMereniEntity.F_pChemJinTek_BOO,
                    EvidenceMereniEntity.F_pChemMast_BOO,
                    EvidenceMereniEntity.F_pChemPrach_BOO,
                    EvidenceMereniEntity.F_pChemTepPos_BOO,
                    EvidenceMereniEntity.F_pChemVoda_BOO,}
        );
    }

    private void setFiledsForEditorPh(Record rec) {
        rec.getSimpleField(F_barevnostB_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostE_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostA_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostKde_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostL_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostStr_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_hmotnost_NUM).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_druhZasahu_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaMechChybCast_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaMechPrehPrek_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaMechLepPas_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaMechKrehkost_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaMechNeni_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaMechTrhliny_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaBioHmyz_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaBioHlodavci_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaBioPlisBak_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaBioNeni_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaChemVoda_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaChemJinTek_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaChemNeni_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaChemMast_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaChemPrach_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaChemTepPos_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaChemBarSkv_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaChemNecTex_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pObaChemZdeg_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pMechChybCast_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pMechPrehPrek_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pMechSlepList_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pMechNeni_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pMechLepPas_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pMechLepPas_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pChemVoda_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pChemJinTek_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pChemMast_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pChemPrach_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pChemTepPos_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pChemBarSkvr_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pChemNeni_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pBioHmyz_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pBioHlodavci_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pBioPlisBak_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pBioZvirExk_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pBioNeni_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapMechChybCast_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapMechChybCelStr_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapMechChybCelStrVal_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapMechPrehPrek_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapMechSlepList_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pMechKreh_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapMechKrehkost_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapMechLepPas_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapMechNeni_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapMechTrhliny_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapBioHmyz_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapBioHlodavci_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapBioPlisBak_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapBioNeni_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapChemVoda_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapChemJinTek_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapChemMast_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapChemPrach_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapChemTepPos_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapChemBarSkv_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapChemNecTex_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapChemZdeg_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pPapChemNeni_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_posHrbetnik_STR).setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_posKapitalek_STR).setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_posZalStuzka_STR).setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_posPredsadka_STR).setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_posVazba_STR).setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_posObalka_STR).setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_mechPosPoznamka_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_obalKde_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_obalPH_NUM).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_obalZpusob_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_pPoznamka_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_pPapPoznamka_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_obalkaZpusob_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_obalkaKde_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_obalkaPH_NUM).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_kBlokZpusob_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_kBlokKde_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_kBlokStrana_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_kBlokPH_NUM).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_merPoznamky_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        
        //rec.getSimpleField(F_poznTypologie_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_posDesky_STR).setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
       
        
        rec.getSimpleField(F_taAnalyzy_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_taCisteni_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_taZasah_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_taPouzPros_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_taDopRezim_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_cRestaurovani_STR).setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_ndzDezinfekce_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_ndzKontAktPlis_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_ndzKonzObalka_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_ndzMereniph_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_ndzOchrannyObal_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_ndzOdkyseleni_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_ndzPozn_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        
        //rec.getSimpleField(F_zDezinfekcePozn_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zIsDezinfekce_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_zMechCisteniPozn_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_zOpravaPozn_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zPouzMatChem_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        
        rec.getSimpleField(F_zoDoplneniPokryvuDesek_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoDoplneniZtratListu_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoNasazeniHrbetniku_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoOpravaTrhlinListu_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoPripevneniDesky_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoVyrovnaniDeformaci_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoZajisteniUvolnenychCastiJaponskymPapirem_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoZpevneniHranRohuListu_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        
        rec.getSimpleField(F_moAplikace_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_moCMetodaOdkyseleni_STR_COM).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_moCTechnologie_STR_COM).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        
        rec.getSimpleField(F_zmcPurus_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zmcVysavac_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zmcWallmaster_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zmcWishab_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zmcJine_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        
        rec.getSimpleField(F_fyzTypVazby_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_fyzDruhVazby_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_fyzOchrannyObal_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_fyzMaterial_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_fyzZabarveniPapiru_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        
        rec.getSimpleField(F_speczneKnizniBlok_STR).setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_speczneHrbetnik_STR).setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_speczneOrizka_STR).setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_speczneObalka_STR).setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_specznePrachBarva_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_specznePrachStruktura_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_specznePrachPoznamka_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_speczneJineBarva_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_speczneJineStruktura_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_speczneJinePoznamka_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
    
        
        rec.getSimpleField(F_zpcistRucniPevny_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zpcistRucniPlasticky_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zpcistRucniPruzny_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zpcistRucniSypky_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zpcistRucniTextil_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zpcistRucniOmeteni_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zpcistManualElektricke_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zpcistManualVzduch_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zpcistManualVysavac_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zpcistStrojniAutomat_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zpcistStrojniPoloautomat_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zpcistStrojniBox_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_zpcistPoznamka_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zpcistPopis_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE); 
        
        rec.getSimpleField(F_barevnostTypPapiru_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE); 
        rec.getSimpleField(F_barevnostPH_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE); 
        rec.getSimpleField(F_barevnostPolymerStup_INT).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE); 
        rec.getSimpleField(F_barevnostPevnostTah_INT).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE); 
        rec.getSimpleField(F_barevnostPevnostTahOhyb_INT).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE); 
        rec.getSimpleField(F_barevnostObsahLignin_INT).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE); 
        rec.getSimpleField(F_barevnostObsahBilkovin_INT).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE); 
        rec.getSimpleField(F_barevnostObsahPryskyrice_INT).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE); 
        rec.getSimpleField(F_barevnostPritomnostOzp_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE); 
        rec.getSimpleField(F_zpcistPopis_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE); 
        rec.getSimpleField(F_zpcistPopis_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE); 
        rec.getSimpleField(F_zpcistPopis_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);    
    }
}
