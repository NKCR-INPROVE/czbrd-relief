/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd;

import com.amaio.plaant.businessFunctions.AddException;
import com.amaio.plaant.businessFunctions.AnnotationKeys;
import com.amaio.plaant.businessFunctions.UpdateException;
import com.amaio.plaant.businessFunctions.ValidationException;
import com.amaio.plaant.desk.QueryException;
import com.amaio.plaant.metadata.*;
import com.amaio.plaant.sync.Record;
import static cz.incad.czbrd.ExemplarEntity.F_cOrganization_STR;
import cz.incad.czbrd.common.DBB;
import cz.incad.czbrd.common.ReliefUser;
import cz.incad.relief3.core.Record_A;
import cz.incad.relief3.core.tools.ReliefFilter;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *******************************************************************************
 *
 * @author martin
 */
public class UlozeniEntity extends Record_A implements Serializable {

    private static final Logger LOG = Logger.getLogger(UlozeniEntity.class.getName());
    public static final String F_kde_STR = "kde";
    public static final String F_teplota_STR = "teplota";
    public static final String F_vlhkost_STR = "vlhkost";
    public static final String F_cOrganization_STR = "cOrganization";

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
        Filter filter;
        Columns columns;
        ReliefUser ru;
        FilterRule frMandatory;
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

        if (ru.isExplorer() | ru.isEditorPh()) {
            LOG.log(Level.WARNING, "in Editor PH line 83/ulozeni");
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
                    //TODO - odmazat všechny předchozí výskyty implicitního filtru
                    for (int i = 0; i < filter.getRulesCount(); i++) {
                        if (ReliefFilter.isFilterRuleEquals(frMandatory, filter.getRule(i))) {
                            filter.removeRule(i);
                        }
                    }
                    filter.addRule(frMandatory);
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
//        Columns columns;
//        Column column_cOrganization;
//        Filter filter;
//        ReliefUser ru;
//
//        super.onMetadataChanged(mtdt);
//
//        //Vyjímka pro jádro - používá se pro třídění
//        Filter testFilter = mtdt.getFilter();
//        if (testFilter != null) {
//            for (int i = 0; i < testFilter.getRulesCount(); i++) {
//                if ("ID".equals(testFilter.getRule(i).getColumn().getColumnName())) {
//                    return mtdt;
//                }
//            }
//        }
//
//        ru = new ReliefUser(getTC());
//        if (ru.isSystemAdmin()) {
//            //System administrator - smí vidět všechno
//            return mtdt;
//        }
//
//        //Přidáme security sloupec když chybí
//        columns = mtdt.getColumns();
//        if (!columns.containsColumn(F_cOrganization_STR)) {
//            column_cOrganization = DBB.createColumn(UlozeniEntity.class.getName(), F_cOrganization_STR);
//            if (column_cOrganization != null) {
//                columns.addColumn(column_cOrganization);
//            }
//        }
//
//        if (ru.isCurator()) {
//            //curator - smí vidět všechno
//            return mtdt;
//        }
//
//        filter = new Filter();
//        if (ru.isExplorer()) {
//            //průzkumník - smí vidět záznamy své organizace
//            column_cOrganization = DBB.createColumn(UlozeniEntity.class.getName(), F_cOrganization_STR);
//            if (column_cOrganization != null) {
//                columns.addColumn(column_cOrganization);
//                filter.addRule(new FilterRule(Filter.AND_OP, 1, column_cOrganization, Filter.EQUAL_CRIT, ru.getOrganization(), 1, false, false));
//            }
//        } else {
//            //ostatní uživatelé - nesmí nic vidět
//            column_cOrganization = DBB.createColumn(UlozeniEntity.class.getName(), F_cOrganization_STR);
//            if (column_cOrganization != null) {
//                columns.addColumn(column_cOrganization);
//                filter.addRule(new FilterRule(Filter.AND_OP, 1, column_cOrganization, Filter.EQUAL_CRIT, "NO-PERMISSION", 1, false, false));
//            }
//        }
//        mtdt.setFilter(filter);
//
//        return mtdt;
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
        String organizationUser;
        String organizationRecord;
        String userLogin;
        String userRecord;

        super.onGetRecord(rec);

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
            //Průzkumník - smí mazat jen své záznamy
            if (!userLogin.equals(userRecord)) {
                rec.setAnnotation(AnnotationKeys.REMOVE_FORBIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            }
        } else if (ru.isEditorPh()) {
            if(!organizationUser.equals(organizationRecord)) {
                rec.setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
                rec.setAnnotation(AnnotationKeys.REMOVE_FORBIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            }
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
        //Kontrola jestli uživatel smí zkládat záznamy
        ReliefUser ru = new ReliefUser(getTC());
        if (ru.getOrganization() == null || ru.getOrganization().length() == 0) {
            throw new AddException("Váš uživatelský účet nemá přiřazenu organizaci, což je podmínka nutná pro zakládání nových záznamů.");
        }

        if (!(ru.isExplorer() || ru.isSystemAdmin())) {
            throw new AddException("Váš uživatelský účet nemá oprávnění pro zakládání nových záznamů v této agendě.");
        }

        super.onCreateLocal(rec);

        //Nastavíme záznamu stejnou organizaci jako má uživatel
        rec.getSimpleField(F_cOrganization_STR).setValue(ru.getOrganization());

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

        return rec;
    }

}
