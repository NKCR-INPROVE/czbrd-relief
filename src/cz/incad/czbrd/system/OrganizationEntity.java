/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd.system;

import com.amaio.plaant.businessFunctions.AddException;
import com.amaio.plaant.businessFunctions.AnnotationKeys;
import com.amaio.plaant.sync.Record;
import cz.incad.czbrd.common.ReliefUser;
import cz.incad.relief3.core.dynamicLists.DynamicListMany_A;
import cz.incad.relief3.core.dynamicLists.DynamicList_A;
import java.io.Serializable;

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
public class OrganizationEntity extends DynamicListMany_A implements Serializable {

    /**
     ***************************************************************************
     *
     * @param rec
     * @return
     */
    @Override
    public Record onGetRecord(Record rec) {
        ReliefUser ru = new ReliefUser(getTC());

        super.onGetRecord(rec);

        if (!ru.isSystemAdmin()) {
            rec.setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
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
        //Nastavíme defaultní hodnotu
        rec.getSimpleField(f_primaryOrder).setValue(0);
        return rec;
    }

    /**
     ***************************************************************************
     *
     * @return
     */
    @Override
    public String setSql() {
        return "SELECT " + DynamicList_A.f_value + ",{0} FROM Organization ORDER BY PRIMARYORDER{2}";
    }

}
