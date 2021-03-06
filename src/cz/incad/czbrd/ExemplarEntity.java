/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd;

import com.amaio.plaant.businessFunctions.*;
import com.amaio.plaant.desk.QueryException;
import com.amaio.plaant.metadata.*;
import com.amaio.plaant.sync.Record;
import com.amaio.plaant.sync.UniqueKey;
import cz.incad.czbrd.common.DBB;
import cz.incad.czbrd.common.ReliefUser;
import cz.incad.czbrd.dLists.SLdruhZasahuEnum;
import cz.incad.relief3.core.Record_A;
import cz.incad.relief3.core.tools.Commons;
import cz.incad.relief3.core.tools.DirectConnection;
import cz.incad.relief3.core.tools.Exceptions;
import cz.incad.relief3.core.tools.ReliefFilter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
public class ExemplarEntity extends Record_A implements Serializable {

    private static final Logger LOG = Logger.getLogger(ExemplarEntity.class.getName());
    public static final String F_cEvidenceMereni_COM = "cEvidenceMereni";
    public static final String F_tEvidenceMereni_TAB = "tEvidenceMereni";
    public static final String F_tPriloha_TAB = "tPriloha";
    public static final String F_cZasahy_COM = "cZasahy";
    public static final String F_vizitka_STR = "vizitka";
    public static final String F_bibNazev_STR = "bibNazev";
    public static final String F_bibAutor_STR = "bibAutor";
    public static final String F_bibSignatura_STR = "bibSignatura";
    public static final String F_bibCarKod_STR = "bibCarKod";
    public static final String F_cOrganization_STR = "cOrganization";
    public static final String F_bibRokVydani_STR = "bibRokVydani";
    public static final String F_fyzPH_STR = "fyzPH";
    public static final String F_fyzTypFondu_STR = "fyzTypFondu";
    public static final String F_fyzNeupExem_STR = "fyzNeupExem";
    public static final String F_fyzTypPapiru_STR = "fyzTypPapiru";
    public static final String F_fyzPismo_STR = "fyzPismo";
    public static final String F_fyzTypTisku_STR = "fyzTypTisku";
    public static final String F_bibSigla_STR = "bibSigla";
    public static final String F_bibCNB_STR = "bibCNB";
    public static final String F_bibSysno_STR = "bibSysno";
    public static final String F_bibPole001_STR = "bibPole001";
    public static final String F_bibMistoVydani_STR = "bibMistoVydani";
    public static final String F_bibVydavatel_STR = "bibVydavatel";
    public static final String F_kontrolniExemplar_BOO = "kontrolniExemplar";
    public static final String F_provenienciZnakTyp_STR = "provenienciZnakTyp";
    public static final String F_fyzPoznamky_STR = "fyzPoznamky";
    public static final String F_vyrazenoZVyberu_BOO = "vyrazenoZVyberu";
    public static final String F_vyrazenoDuvod_STR = "vyrazenoDuvod";

    public static final String F_rUlozeni_REF = "rUlozeni";

    /**
     ***************************************************************************
     *
     * @param mtdt
     * @return
     * @throws QueryException
     */
    @Override
    public Metadata onMetadataChanged(Metadata mtdt) throws QueryException {
        Column column_cOrganization;
        Column column_cSignatura;
        Filter filter;
        Columns columns;
        ReliefUser ru;
        FilterRule frMandatory;
        FilterRule frMandatory2;
        FilterRule frLast;

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
        //Relief administrátor - smí vidět všechno
        if (ru.isSystemAdmin()) {
            return mtdt;
        }

        //Přidáme security sloupec když chybí
        columns = mtdt.getColumns();
        if (!columns.containsColumn(F_cOrganization_STR)) {
            column_cOrganization = DBB.createColumn(ExemplarEntity.class.getName(), F_cOrganization_STR);
            columns.addColumn(column_cOrganization);
        }

        //curator - smí vidět všechno
        if (ru.isCurator()) {
            return mtdt;
        }
        
        if (ru.isExplorer()) {
            //průzkumník - smí vidět záznamy své organizace
            filter = mtdt.getFilter();
            if (filter == null) {
                filter = new Filter();
            }

            //Vytvoříme si naše povinné filterrule
            column_cOrganization = DBB.createColumn(ExemplarEntity.class.getName(), F_cOrganization_STR);
            frMandatory = new FilterRule(Filter.AND_OP, 1, column_cOrganization, Filter.EQUAL_CRIT, ru.getOrganization(), 1, false, false);

            if (filter.getRulesCount() == 0) {
                filter.addRule(new FilterRule(Filter.AND_OP, 1, column_cOrganization, Filter.EQUAL_CRIT, ru.getOrganization(), 1, false, false));
            } else {
                frLast = filter.getRule(filter.getRulesCount() - 1);
                if (ReliefFilter.isFilterRuleEquals(frMandatory, frLast)) {
                    //poslední pravidlo je to naše pravidlo, takže OK
                } else {
                    //Vymažeme všechny předchozí výskyty povinného filterrule
                    for (int i = 0; i < filter.getRulesCount(); i++) {
                        if (ReliefFilter.isFilterRuleEquals(frMandatory, filter.getRule(i))) {
                            filter.removeRule(i);
                        }
                    }
                    filter.addRule(frMandatory);
                }
            }

        } else if(ru.isEditorPh()) {
            filter = mtdt.getFilter();
            if (filter == null) {
                filter = new Filter();
            }
            //Vytvoříme si naše povinné filterrule
            column_cOrganization = DBB.createColumn(ExemplarEntity.class.getName(), F_cOrganization_STR);
            column_cSignatura = DBB.createColumn(ExemplarEntity.class.getName(), F_bibSignatura_STR);
            frMandatory = new FilterRule(Filter.AND_OP, 1, column_cSignatura, Filter.BEGIN_CRIT, "54 E", 0, false, false);
            frMandatory2 = new FilterRule(Filter.OR_OP, 0, column_cSignatura, Filter.BEGIN_CRIT, "54E", 1, false, false);
            //frMandatory = new FilterRule(Filter.AND_OP, 1, column_cOrganization, Filter.EQUAL_CRIT, ru.getOrganization(), 1, false, false);

            if (filter.getRulesCount() == 0) {
                filter.addRule(frMandatory);
                filter.addRule(frMandatory2);
            } else {
                frLast = filter.getRule(filter.getRulesCount() - 2);
                if (ReliefFilter.isFilterRuleEquals(frMandatory, frLast)) {
                    //poslední pravidlo je to naše pravidlo, takže OK
                } else {
                    //Vymažeme všechny předchozí výskyty povinného filterrule
                    for (int i = 0; i < filter.getRulesCount(); i++) {
                        if (ReliefFilter.isFilterRuleEquals(frMandatory, filter.getRule(i))) {
                            filter.removeRule(i);
                        }
                    }
                    filter.addRule(frMandatory);
                    filter.addRule(frMandatory2);
                }
            }
            
        } else {
            //ostatní uživatelé - nesmí nic vidět
            filter = new Filter();
            column_cOrganization = DBB.createColumn(ExemplarEntity.class.getName(), F_cOrganization_STR);
            if (column_cOrganization != null) {
                filter.addRule(new FilterRule(Filter.AND_OP, 1, column_cOrganization, Filter.EQUAL_CRIT, "NO-PERMISSION", 1, false, false));
            }
        }

        mtdt.setFilter(filter);

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

        //Kontorla na to jestli záznam i organizace má přiřazenu organizaci
        if (organizationRecord == null || organizationUser == null) {
            rec.setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            rec.setAnnotation(AnnotationKeys.REMOVE_FORBIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            LOG.log(Level.WARNING, "User/Record is securityless: {0}/{1}", new Object[]{ru.getLogin(), rec.getSimpleField(F_recID_STR).getValue()});
            return rec;
        } else {
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
                //rec.setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
                setFiledsForEditorPh(rec);
                rec.setAnnotation(AnnotationKeys.REMOVE_FORBIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        } else {
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
        ReliefUser ru = new ReliefUser(getTC());

        //Kontrola jestli uživatel smí zkládat záznamy
        if (ru.getOrganization() == null || ru.getOrganization().length() == 0) {
            throw new AddException("Váš uživatelský účet nemá přiřazenu organizaci, což je podmínka nutná pro zakládání nových záznamů.");
        }

        if (!(ru.isExplorer() || ru.isSystemAdmin())) {
            throw new AddException("Váš uživatelský účet nemá oprávnění pro zakládání nových záznamů v této agendě.");
        }

        //Kontrola na záznam vytvořený kopií - vyčištění dat
        if (rec.getFieldValue(Record_A.F_recID_STR) != null) {
            rec.getSimpleField(ExemplarEntity.F_cOrganization_STR).setValue(null);
            rec.getTableField(ExemplarEntity.F_tEvidenceMereni_TAB).setKeys(new UniqueKey[]{});
            rec.getTableField(ExemplarEntity.F_tPriloha_TAB).setKeys(new UniqueKey[]{});
            rec.getSimpleField(ExemplarEntity.F_bibAutor_STR).setValue(null);
            rec.getSimpleField(ExemplarEntity.F_bibCNB_STR).setValue(null);
            rec.getSimpleField(ExemplarEntity.F_bibMistoVydani_STR).setValue(null);
            rec.getSimpleField(ExemplarEntity.F_bibNazev_STR).setValue(null);
            rec.getSimpleField(ExemplarEntity.F_bibPole001_STR).setValue(null);
            rec.getSimpleField(ExemplarEntity.F_bibRokVydani_STR).setValue(null);
            rec.getSimpleField(ExemplarEntity.F_bibSigla_STR).setValue(null);
            rec.getSimpleField(ExemplarEntity.F_bibSysno_STR).setValue(null);
            rec.getSimpleField(ExemplarEntity.F_bibVydavatel_STR).setValue(null);
            rec.getSimpleField(ExemplarEntity.F_fyzPH_STR).setValue(null);
            //rec.getSimpleField(ExemplarEntity.F_).setValue(null);
        }

        super.onCreateLocal(rec);

        //Nastavíme záznamu stejnou organizaci jako má uživatel
        rec.getSimpleField(ExemplarEntity.F_cOrganization_STR).setValue(ru.getOrganization());

        return rec;
    }

    /**
     ***************************************************************************
     *
     * @return @throws AddException
     * @throws ValidationException
     */
    @Override
    public Record onCreate(Record rec) throws AddException, ValidationException {
        ValidationException vex = Exceptions.getValiadtionException(getTC());
        rec = super.onCreate(rec);
        this.onCreateUpdate(rec, vex, Boolean.TRUE);
        Commons.evaluateValidiationException(vex, rec);

        return rec;
    }

    /**
     ***************************************************************************
     *
     * @return @throws ValidationException
     * @throws UpdateException
     */
    @Override
    public Record onUpdate(Record rec) throws ValidationException, UpdateException {
        ValidationException vex = Exceptions.getValiadtionException(getTC());
        rec = super.onUpdate(rec);
        this.onCreateUpdate(rec, vex, Boolean.FALSE);
        Commons.evaluateValidiationException(vex, rec);

        return rec;
    }

    /**
     ***************************************************************************
     *
     * @param record
     * @return
     */
    private void onCreateUpdate(Record rec, ValidationException vex, Boolean isCreate) throws ValidationException {
        RecordsIterator ritEvidenceMereni;
        Record recEvidenceMereni;
        Record recZasahy;
        Object value;
        
        if (AnnotationKeys.TRUE_VALUE.equals(rec.getAnnotation("UlozitBezSpusteniValidaci"))) { //pro aktualizaci záznamu z dat v Alephu
            
        } else {
            LOG.log(Level.WARNING, "validace Exemplare - anotace nastavena ");
            recEvidenceMereni = rec.getCompositeField(F_cEvidenceMereni_COM).getReferencedRecord();
            if (Boolean.TRUE.equals(rec.getAnnotation(Commons.ANNOTATION_VALIDATION_EXCEPTION_IGNORE_WARNING))) {
                //pokud voláno z bussines funkce či importu, nevalidovat záznamy
                ritEvidenceMereni = rec.getTableField(F_tEvidenceMereni_TAB).getTableRecords();
                while (ritEvidenceMereni.hasMoreRecords()) {
                    recEvidenceMereni = ritEvidenceMereni.nextRecord();
                    recEvidenceMereni.setAnnotation(Commons.ANNOTATION_VALIDATION_EXCEPTION_IGNORE_WARNING, Boolean.TRUE);
                }
            } else {
                LOG.log(Level.WARNING, "validace Exemplare - anotace nenastavena");
                ExemplarEntity.formValidation(rec, getTC(), vex, isCreate);
                LOG.log(Level.WARNING, "validace Evidence");
                EvidenceMereniEntity.formValidationExt(recEvidenceMereni, getTC(), vex);
                recEvidenceMereni.setAnnotation(EvidenceMereniEntity.VALIDATION_DONE, AnnotationKeys.TRUE_VALUE);
    //            ritEvidenceMereni = rec.getTableField(F_tEvidenceMereni_TAB).getTableRecords();
    //            while (ritEvidenceMereni.hasMoreRecords()) {
    //                recEvidenceMereni = ritEvidenceMereni.nextRecord();
    //              EvidenceMereniEntity.formValidationExt(recEvidenceMereni, getTC(), vex);
    //                recEvidenceMereni.setAnnotation(EvidenceMereniEntity.VALIDATION_DONE, AnnotationKeys.TRUE_VALUE);
    //            }
                if (vex.isGravid() && !vex.isWarning()) {
                    throw vex;
                }
            }

    //        //Když je vybrána archivní lepenka, tak je vynulováno pole Materiál
    //        if ("archivnilepenka".equals(rec.getSimpleField(F_fyzMaterial_STR).getValue()) || rec.getSimpleField(F_fyzMaterial_STR).getValue() == null) {
    //            rec.getSimpleField(F_fyzPH_STR).setValue(null);
    //        }

            //přepočítání počtů zásahů
            /*
             * Pole poctyZasahu
             * 0 - Odkyselování
             * 1 - Restaurování
             * 2 - Konzervace
             * 3 - Mechanické čištění
             * 4 - Chemické čištění
             * 5 - Dezinfekce
             * 6 - Jiné
             * 7 - Kontrolní průzkum
             * 8 - Po živelné katastrofě
             */
            Integer[] poctyZasahu = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            //Započítáme komponentu
            value = rec.getCompositeField(F_cEvidenceMereni_COM).getCompositeRecord().getSimpleField(EvidenceMereniEntity.F_druhZasahu_STR).getValue();
            countZasah(poctyZasahu, value);
            //Započítáme archivované záznamy
            ritEvidenceMereni = rec.getTableField(F_tEvidenceMereni_TAB).getTableRecords();
            while (ritEvidenceMereni.hasMoreRecords()) {
                recEvidenceMereni = ritEvidenceMereni.nextRecord();
                value = recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_druhZasahu_STR).getValue();
                countZasah(poctyZasahu, value);

            }
            //Zapíšeme napočítané hodnoty do komponenty Zasahy
            recZasahy = rec.getCompositeField(F_cZasahy_COM).getCompositeRecord();
            recZasahy.getSimpleField(ZasahyEntity.F_odkyselovani_INT).setValue(poctyZasahu[0]);
            recZasahy.getSimpleField(ZasahyEntity.F_restaurovani_INT).setValue(poctyZasahu[1]);
            recZasahy.getSimpleField(ZasahyEntity.F_konzervace_INT).setValue(poctyZasahu[2]);
            recZasahy.getSimpleField(ZasahyEntity.F_mechCisteni_INT).setValue(poctyZasahu[3]);
            recZasahy.getSimpleField(ZasahyEntity.F_chemCisteni_INT).setValue(poctyZasahu[4]);
            recZasahy.getSimpleField(ZasahyEntity.F_dezinfekce_INT).setValue(poctyZasahu[5]);
            recZasahy.getSimpleField(ZasahyEntity.F_jine_INT).setValue(poctyZasahu[6]);
            recZasahy.getSimpleField(ZasahyEntity.F_kontrolniPruzkum_INT).setValue(poctyZasahu[7]);
            recZasahy.getSimpleField(ZasahyEntity.F_poZivelneKatastrofe_INT).setValue(poctyZasahu[8]);
            recZasahy.getSimpleField(ZasahyEntity.F_univerzalniPrevazba_INT).setValue(poctyZasahu[9]);
            recZasahy.getSimpleField(ZasahyEntity.F_historizujiciPrevazba_INT).setValue(poctyZasahu[10]);

        }
        
        //Vytvoříme vizitku
        rec.getSimpleField(F_vizitka_STR).setValue(createVizitka(rec));
    }

    /**
     ***************************************************************************
     *
     * @param record
     * @throws DeleteException
     */
    @Override
    public void onRemove(Record record) throws DeleteException {
        super.onRemove(record);
        //ReliefUser ru = new ReliefUser(getTC());

    }

    /**
     ***************************************************************************
     *
     * @param rec
     * @param context
     * @param vex
     */
    private static void formValidation(Record rec, TriggerContext context, ValidationException vex, Boolean isCreate) {
        int id = 0;
        //Kontrola na to jestli je záznam duplicitní
        if (!isCreate) {
            id = Commons.getRecordId(rec);
        }
        if (ExemplarEntity.isDuplicity(DuplicityIdentificatorEnum.bibCarKod, (String) rec.getFieldValue(ExemplarEntity.F_bibCarKod_STR), (String) rec.getFieldValue(ExemplarEntity.F_cOrganization_STR), id)) {
            vex.addField(ExemplarEntity.F_bibCarKod_STR, "V systému již existuje záznam se stejným čárovým kódem. - Potenciální duplicita.", true);
        }
        if (ExemplarEntity.isDuplicity(DuplicityIdentificatorEnum.bibSignatura, (String) rec.getFieldValue(ExemplarEntity.F_bibSignatura_STR), (String) rec.getFieldValue(ExemplarEntity.F_cOrganization_STR), id)) {
            vex.addField(ExemplarEntity.F_bibSignatura_STR, "V systému již existuje záznam se stejnou signaturou. - Potenciální duplicita.", true);
        }
        
        //kontroly na nova povinna pole - 2016.11.01
        if (rec.getFieldValue(ExemplarEntity.F_fyzTypFondu_STR) == null) {
            vex.addField(ExemplarEntity.F_fyzTypFondu_STR, "Identifikace exempláře – Typ fondu: Vyplňte pole Typ fondu!", false);
        }
        if (rec.getFieldValue(ExemplarEntity.F_fyzTypPapiru_STR) == null) {
            vex.addField(ExemplarEntity.F_fyzTypPapiru_STR, "Identifikace exempláře – Typ papíru: Vyplňte pole Typ papíru!", false);
        }
        if (rec.getFieldValue(ExemplarEntity.F_fyzTypTisku_STR) == null) {
            vex.addField(ExemplarEntity.F_fyzTypTisku_STR, "Identifikace exempláře – Zabarvení papíru: Vyplňte pole Zabarvení papíru!", false);
        }
        // konec novych povinnych poli - 2016.11.01

    }

    /**
     ***************************************************************************
     *
     * @param poctyZasahu
     * @param value
     */
    private void countZasah(Integer[] poctyZasahu, Object value) {
        if (SLdruhZasahuEnum.puvodnistav.toString().equals(value)) {
        } else if (SLdruhZasahuEnum.odkyselovani.toString().equals(value)) {
            poctyZasahu[0] = ++poctyZasahu[0];
        } else if (SLdruhZasahuEnum.restaurovani.toString().equals(value)) {
            poctyZasahu[1] = ++poctyZasahu[1];
        } else if (SLdruhZasahuEnum.konzervace.toString().equals(value)) {
            poctyZasahu[2] = ++poctyZasahu[2];
        } else if (SLdruhZasahuEnum.mechanickecisteni.toString().equals(value)) {
            poctyZasahu[3] = ++poctyZasahu[3];
        } else if (SLdruhZasahuEnum.chemickecisteni.toString().equals(value)) {
            poctyZasahu[4] = ++poctyZasahu[4];
        } else if (SLdruhZasahuEnum.dezinfekce.toString().equals(value)) {
            poctyZasahu[5] = ++poctyZasahu[5];
        } else if (SLdruhZasahuEnum.jine.toString().equals(value)) {
            poctyZasahu[6] = ++poctyZasahu[6];
        } else if (SLdruhZasahuEnum.kontrolniPruzkum.toString().equals(value)) {
            poctyZasahu[7] = ++poctyZasahu[7];
        } else if (SLdruhZasahuEnum.poZivelneKatastrofe.toString().equals(value)) {
            poctyZasahu[8] = ++poctyZasahu[8];
        } else if (SLdruhZasahuEnum.univerzalniPrevazba.toString().equals(value)) {
            poctyZasahu[9] = ++poctyZasahu[9];
        } else if (SLdruhZasahuEnum.historizujiciPrevazba.toString().equals(value)) {
            poctyZasahu[10] = ++poctyZasahu[10];
        }
    }

    /**
     ***************************************************************************
     * nazev / autor carKod, signatura
     *
     * @param rec záznam musí být z agendy ExemplarEntity.
     * @return
     */
    public static String createVizitka(Record rec) {
        StringBuilder sb = new StringBuilder(0);

        if (rec.getSimpleField(F_bibNazev_STR).getValue() != null) {
            sb.append(rec.getSimpleField(F_bibNazev_STR).getValue());
        }
        sb.append(" / ");
        if (rec.getSimpleField(F_bibAutor_STR).getValue() != null) {
            sb.append(rec.getSimpleField(F_bibAutor_STR).getValue());
        }
        sb.append("\n");
        if (rec.getSimpleField(F_bibCarKod_STR).getValue() != null) {
            sb.append(rec.getSimpleField(F_bibCarKod_STR).getValue());
        }
        sb.append(", ");
        if (rec.getSimpleField(F_bibSignatura_STR).getValue() != null) {
            sb.append(rec.getSimpleField(F_bibSignatura_STR).getValue());
        }

        if (sb.toString().length() < 7) {
            return null;
        }
        return sb.toString();
    }

    /**
     ***************************************************************************
     *
     * @param field
     * @param fieldValue
     * @param organization
     * @param id
     * @return
     */
    private static boolean isDuplicity(DuplicityIdentificatorEnum field, String fieldValue, String organization, int id) {
        //Kontrola na to jestli je záznam duplicitní
        Connection conn = null;
        Statement stmt;
        ResultSet rs;
        String sql;

        if (organization != null && organization.length() != 0 && fieldValue != null && fieldValue.length() != 0) {
            try {
                Boolean navratovaHodnota = null;
                conn = DirectConnection.getConnection();
                stmt = conn.createStatement();
                sql = "SELECT ID FROM EXEMPLAR WHERE " + field + " = '" + fieldValue + "' AND CORGANIZATION = '" + organization + "'";
                LOG.log(Level.FINEST, "DEDUPLICITY SQL: {0}", sql);
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    if (rs.next()) {
                        rs.close();
                        stmt.close();
//                        navratovaHodnota = true;
                        return true;
                    } else if (id == 0) {
                        rs.close();
                        stmt.close();
//                        navratovaHodnota = true;
                        return true;
                    }
                } else {
                    rs.close();
                    stmt.close();
                    return false;
                }
//                Boolean navratovaHodnota = false;
//                if (navratovaHodnota == null) {
//                    if (id != rs.getInt("ID")) {
//                        navratovaHodnota = true;
//                    } else {
                        navratovaHodnota = false;
//                    }
//                }
                rs.close();
                stmt.close();
                //return (id != rs.getInt("ID"));
//                return navratovaHodnota;
                return false;
            } catch (Throwable ex) {
                LOG.log(Level.SEVERE, null, ex);
                return false;
            } finally {
                try {
                    conn.close();
                } catch (Throwable ex) {
                    LOG.log(Level.WARNING, "Unable close direct DB connection.");
                }
            }
        } else {
            LOG.log(Level.WARNING, "Zaznam nema organizaci nebo identifikator. {0}", id);
            return false;
        }
    }

    private void setFiledsForEditorPh(Record rec) {
        rec.getSimpleField(F_bibCarKod_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_vizitka_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_bibNazev_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_bibRokVydani_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_fyzPH_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_fyzTypFondu_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_fyzNeupExem_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_fyzTypPapiru_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_fyzPismo_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_bibSigla_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_bibCNB_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_bibSysno_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_bibPole001_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_bibMistoVydani_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_bibVydavatel_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_kontrolniExemplar_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_bibSignatura_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_bibAutor_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_fyzTypTisku_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_bibAutor_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_provenienciZnakTyp_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        //rec.getSimpleField(F_fyzPoznamky_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_vyrazenoZVyberu_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        rec.getSimpleField(F_vyrazenoDuvod_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
    }
}

/**
 *******************************************************************************
 *
 * @author Martin
 */
enum DuplicityIdentificatorEnum {

    bibCarKod,
    bibSignatura
}
