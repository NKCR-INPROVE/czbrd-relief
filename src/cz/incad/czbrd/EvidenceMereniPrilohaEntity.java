/* *****************************************************************************
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cz.incad.czbrd;

import com.amaio.plaant.businessFunctions.AddException;
import com.amaio.plaant.businessFunctions.AnnotationKeys;
import com.amaio.plaant.businessFunctions.TriggerContext;
import com.amaio.plaant.businessFunctions.UpdateException;
import com.amaio.plaant.businessFunctions.ValidationException;
import com.amaio.plaant.sync.Record;
import cz.incad.czbrd.common.ReliefUser;
import cz.incad.czbrd.common.Utils;
import cz.incad.czbrd.dLists.SLMetodaOdkyseleni;
import cz.incad.czbrd.dLists.SLMetodaOdkyseleniTechnologie;
import cz.incad.relief3.core.Record_A;
import cz.incad.relief3.core.tools.Exceptions;
import java.io.Serializable;

/**
 *******************************************************************************
 *
 * @author martin
 */
public class EvidenceMereniPrilohaEntity extends Record_A implements Serializable {

    public static final String F_rPriloha_REF = "rPriloha";

    public static final String F_merZpusob_STR = "merZpusob";
    public static final String F_merKde_STR = "merKde";
    public static final String F_merStrana_STR = "merStrana";
    public static final String F_merPH_NUM = "merPH";
    public static final String F_note_STR = "note";

    public static final String F_pMechChybCast_BOO = "pMechChybCast";
    public static final String F_pMechPrehPrek_BOO = "pMechPrehPrek";
    //public static final String F_pMechCinHlodHmyz_BOO = "pMechCinHlodHmyz";
    public static final String F_pMechKrehkost_BOO = "pMechKrehkost";
    public static final String F_pMechLepPas_BOO = "pMechLepPas";
    public static final String F_pMechNeni_BOO = "pMechNeni";
    public static final String F_pMechTrhliny_BOO = "pMechTrhliny";

    public static final String F_pBioHmyz_BOO = "pBioHmyz";
    public static final String F_pBioHlodavci_BOO = "pBioHlodavci";
    public static final String F_pBioPlisBak_BOO = "pBioPlisBak";
    public static final String F_pBioNeni_BOO = "pBioNeni";

    public static final String F_pChemNecTex_BOO = "pChemNecTex";
    public static final String F_pChemZdeg_BOO = "pChemZdeg";
    public static final String F_pChemMast_BOO = "pChemMast";
    public static final String F_pChemNeni_BOO = "pChemNeni";
    public static final String F_pChemPrach_BOO = "pChemPrach";
    public static final String F_pChemBarSkv_BOO = "pChemBarSkv";
    public static final String F_pChemJinTek_BOO = "pChemJinTek";
    public static final String F_pChemVoda_BOO = "pChemVoda";
    public static final String F_pChemTepPos_BOO = "pChemTepPos";

    public static final String F_barevnostL_STR = "barevnostL";
    public static final String F_barevnostA_STR = "barevnostA";
    public static final String F_barevnostB_STR = "barevnostB";
    public static final String F_barevnostE_STR = "barevnostE";
    public static final String F_hmotnost_NUM = "hmotnost";
    public static final String F_barevnostKde_STR = "barevnostKde";

    public static final String F_ptaAnalyzy_STR = "ptaAnalyzy";
    public static final String F_ptaCisteni_STR = "ptaCisteni";
    public static final String F_ptaPouzPros_STR = "ptaPouzPros";
    public static final String F_ptaZasah_STR = "ptaZasah";

    public static final String F_moAplikace_STR = "moAplikace";
    public static final String F_moCMetodaOdkyseleni_STR_COM = "moCMetodaOdkyseleni";
    public static final String F_moCTechnologie_STR_COM = "moCTechnologie";

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

    public static final String F_zmcPurus_BOO = "zmcPurus";
    public static final String F_zmcVysavac_BOO = "zmcVysavac";
    public static final String F_zmcWallmaster_BOO = "zmcWallmaster";
    public static final String F_zmcWishab_BOO = "zmcWishab";
    public static final String F_zmcJine_BOO = "zmcJine";
    
    public static final String F_barevnostTypPapiru_STR = "barevnostTypPapiru";
    public static final String F_barevnostPH_STR = "barevnostPH";
    public static final String F_barevnostPolymerStup_INT = "barevnostPolymerStup";
    public static final String F_barevnostPevnostTah_INT = "barevnostPevnostTah";
    public static final String F_barevnostPevnostTahOhyb_INT = "barevnostPevnostTahOhyb";
    public static final String F_barevnostObsahLignin_INT = "barevnostObsahLignin";
    public static final String F_barevnostObsahBilkovin_INT = "barevnostObsahBilkovin";
    public static final String F_barevnostObsahPryskyrice_INT = "barevnostObsahPryskyrice";
    public static final String F_barevnostPritomnostOzp_STR = "barevnostPritomnostOzp";

    //public static final String f_ = "";
    /**
     ***************************************************************************
     *
     * @param record
     * @return
     * @throws AddException
     */
    @Override
    public Record onCreateLocal(Record record) throws AddException {
        record = super.onCreateLocal(record);
        return record;
    }

    @Override
    public Record onGetRecord(Record rec) {
        ReliefUser ru = new ReliefUser(getTC());
        if (ru.isEditorPh()) {
            setFiledsForEditorPh(rec);
            rec.setAnnotation(AnnotationKeys.REMOVE_FORBIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            return rec;
        }
        return super.onGetRecord(rec); //To change body of generated methods, choose Tools | Templates.
    }
    

    /**
     ***************************************************************************
     *
     * @param record
     * @return
     * @throws AddException
     * @throws ValidationException
     */
    @Override
    public Record onCreate(Record record) throws AddException, ValidationException {
        record = super.onCreate(record);
        return this.onCreateUpdate(record);
    }

    /**
     ***************************************************************************
     *
     * @param record
     * @return
     * @throws ValidationException
     * @throws UpdateException
     */
    @Override
    public Record onUpdate(Record record) throws ValidationException, UpdateException {
        record = super.onUpdate(record);
        return this.onCreateUpdate(record);
    }

    /**
     ***************************************************************************
     *
     * @param record
     * @return
     */
    private Record onCreateUpdate(Record rec) throws ValidationException {
        ValidationException vex = Exceptions.getValiadtionException(getTC());
        String value;

        EvidenceMereniPrilohaEntity.formValidation(rec, getTC(), vex);

        value = (String) rec.getFieldValue(EvidenceMereniPrilohaEntity.F_moCTechnologie_STR_COM);
        // dopočítání hodnoty aplikace metody odkyselení
        if (value != null) {
            if (SLMetodaOdkyseleniTechnologie.VALUE_H01.equals(value)) {
                rec.getSimpleField(EvidenceMereniPrilohaEntity.F_moAplikace_STR).setValue("ponor");
            } else if (SLMetodaOdkyseleniTechnologie.VALUE_H02.equals(value)) {
                rec.getSimpleField(EvidenceMereniPrilohaEntity.F_moAplikace_STR).setValue("ponor");
            } else if (SLMetodaOdkyseleniTechnologie.VALUE_H03.equals(value)) {
                rec.getSimpleField(EvidenceMereniPrilohaEntity.F_moAplikace_STR).setValue("ponor");
            } else if (SLMetodaOdkyseleniTechnologie.VALUE_H04.equals(value)) {
                rec.getSimpleField(EvidenceMereniPrilohaEntity.F_moAplikace_STR).setValue("postřik, ponor");
            } else if (SLMetodaOdkyseleniTechnologie.VALUE_H05.equals(value)) {
                rec.getSimpleField(EvidenceMereniPrilohaEntity.F_moAplikace_STR).setValue("naprašování");
            } else if (SLMetodaOdkyseleniTechnologie.VALUE_H06.equals(value)) {
                rec.getSimpleField(EvidenceMereniPrilohaEntity.F_moAplikace_STR).setValue("ponor");
            } else if (SLMetodaOdkyseleniTechnologie.VALUE_H07.equals(value)) {
                rec.getSimpleField(EvidenceMereniPrilohaEntity.F_moAplikace_STR).setValue("postřik");
            } else if (SLMetodaOdkyseleniTechnologie.VALUE_H08.equals(value)) {
                rec.getSimpleField(EvidenceMereniPrilohaEntity.F_moAplikace_STR).setValue("sendvič");
            }
        }

        // dopočítání hodnoty metody odkyselení
        if (value == null) {
            rec.getSimpleField(EvidenceMereniPrilohaEntity.F_moCMetodaOdkyseleni_STR_COM).setValue(null);
        } else if (value.startsWith("H")) {
            rec.getSimpleField(EvidenceMereniPrilohaEntity.F_moCMetodaOdkyseleni_STR_COM).setValue(SLMetodaOdkyseleni.VALUE_HROMADNA);
        } else {
            rec.getSimpleField(EvidenceMereniPrilohaEntity.F_moCMetodaOdkyseleni_STR_COM).setValue(SLMetodaOdkyseleni.VALUE_INDIVIDUALNI);
        }

        if (vex.isGravid() && !vex.isWarning()) {
            throw vex;
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
    private static void formValidation(Record rec, TriggerContext context, ValidationException vex) {
        //Zakrouhleni PH
        Utils.roundSimpleField(rec.getSimpleField(EvidenceMereniPrilohaEntity.F_merPH_NUM));
        //kontrola hodnoty pH Obalha
        if (!Utils.isValid_pH(rec.getFieldValue(EvidenceMereniPrilohaEntity.F_merPH_NUM))) {
            vex.addField(EvidenceMereniPrilohaEntity.F_merPH_NUM, "Pole smí nabývat pouze hodnot 0 - 14", false);
        }

        if (!Utils.isValid_Hmotnost(rec.getFieldValue(EvidenceMereniPrilohaEntity.F_hmotnost_NUM))) {
            vex.addField(EvidenceMereniPrilohaEntity.F_hmotnost_NUM, "Pole smí nabývat pouze hodnot 0 - 999999", false);
        }

        Utils.onePoskozeniValidation(rec,
                rec.getSimpleField(EvidenceMereniPrilohaEntity.F_pMechNeni_BOO),
                new String[]{
                    EvidenceMereniPrilohaEntity.F_pMechChybCast_BOO,
                    //EvidenceMereniPrilohaEntity.F_pMechCinHlodHmyz_BOO,
                    EvidenceMereniPrilohaEntity.F_pMechKrehkost_BOO,
                    EvidenceMereniPrilohaEntity.F_pMechLepPas_BOO,
                    EvidenceMereniPrilohaEntity.F_pMechPrehPrek_BOO,
                    EvidenceMereniPrilohaEntity.F_pMechTrhliny_BOO
                }
        );

        Utils.onePoskozeniValidation(rec,
                rec.getSimpleField(EvidenceMereniPrilohaEntity.F_pBioNeni_BOO),
                new String[]{
                    EvidenceMereniPrilohaEntity.F_pBioHlodavci_BOO,
                    EvidenceMereniPrilohaEntity.F_pBioHmyz_BOO,
                    EvidenceMereniPrilohaEntity.F_pBioPlisBak_BOO
                }
        );

        Utils.onePoskozeniValidation(rec,
                rec.getSimpleField(EvidenceMereniPrilohaEntity.F_pChemNeni_BOO),
                new String[]{
                    EvidenceMereniPrilohaEntity.F_pChemBarSkv_BOO,
                    EvidenceMereniPrilohaEntity.F_pChemJinTek_BOO,
                    EvidenceMereniPrilohaEntity.F_pChemMast_BOO,
                    EvidenceMereniPrilohaEntity.F_pChemNecTex_BOO,
                    EvidenceMereniPrilohaEntity.F_pChemPrach_BOO,
                    EvidenceMereniPrilohaEntity.F_pChemTepPos_BOO,
                    EvidenceMereniPrilohaEntity.F_pChemVoda_BOO,
                    EvidenceMereniPrilohaEntity.F_pChemZdeg_BOO
                }
        );
    }

    private void setFiledsForEditorPh(Record rec) {
        rec.getSimpleField(F_merZpusob_STR).setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_merKde_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_merStrana_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_merPH_NUM).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_note_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pMechChybCast_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pMechPrehPrek_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pMechKrehkost_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pMechLepPas_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pMechNeni_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pMechTrhliny_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pBioHmyz_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pBioHlodavci_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pBioPlisBak_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pBioNeni_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pChemPrach_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pChemBarSkv_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pChemJinTek_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pChemVoda_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_pChemTepPos_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostL_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostA_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostB_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostE_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_hmotnost_NUM).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostKde_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_ptaAnalyzy_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_ptaCisteni_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_ptaPouzPros_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_ptaZasah_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_moAplikace_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zDezinfekcePozn_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zIsDezinfekce_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zMechCisteniPozn_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zOpravaPozn_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zPouzMatChem_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoDoplneniPokryvuDesek_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoDoplneniZtratListu_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoNasazeniHrbetniku_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoOpravaTrhlinListu_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoPripevneniDesky_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoVyrovnaniDeformaci_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoZajisteniUvolnenychCastiJaponskymPapirem_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zoZpevneniHranRohuListu_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zmcPurus_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zmcVysavac_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zmcWallmaster_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zmcWishab_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_zmcJine_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostTypPapiru_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostPH_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostPolymerStup_INT).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostPevnostTah_INT).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostPevnostTahOhyb_INT).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostObsahLignin_INT).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostObsahBilkovin_INT).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostObsahPryskyrice_INT).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_barevnostPritomnostOzp_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
    }
}
