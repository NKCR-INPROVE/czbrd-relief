/* *****************************************************************************
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cz.incad.czbrd.r3c;

import com.amaio.plaant.businessFunctions.AddException;
import com.amaio.plaant.businessFunctions.AnnotationKeys;
import com.amaio.plaant.sync.Record;
import cz.incad.czbrd.common.ReliefUser;
import cz.incad.relief3.core.dynamicLists.DynamicList_A;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 *******************************************************************************
 *
 * @author martin
 */
public class R3CDLManyEntity extends DynamicList_A implements Serializable {

    public static final String F_value_STR = "value";
    public static final String F_cs_STR = "cs";
    public static final String F_en_STR = "en";
    public static final String F_de_STR = "de";
    public static final String F_fr_STR = "fr";
    public static final String F_note_STR = "note";
    public static final String F_primaryOrder_INT = "primaryOrder";
    public static final String F_cOrganization_STR = "cOrganization";

    /**
     ***************************************************************************
     *
     * @param rec
     * @return
     */
    @Override
    public Record onGetRecord(Record rec) {
        super.onGetRecord(rec);
        ReliefUser ru = new ReliefUser(getTC());

        if (!ru.isSystemAdmin()) {
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

        if (!ru.isSystemAdmin()) {
            throw new AddException("Váš uživatelský účet nemá oprávnění pro zakládání nových záznamů v této agendě.");
        }

        super.onCreateLocal(rec);
        return rec;
    }

    /**
     ***************************************************************************
     *
     * @param loc
     * @return
     */
    public List getLocalizedList(Locale loc) {
        return null;
    }

}
