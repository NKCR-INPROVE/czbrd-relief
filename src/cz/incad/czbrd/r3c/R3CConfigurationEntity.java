/**
 * ****************************************************************************
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cz.incad.czbrd.r3c;

import com.amaio.plaant.businessFunctions.AddException;
import com.amaio.plaant.businessFunctions.AnnotationKeys;
import com.amaio.plaant.sync.Record;
import cz.incad.czbrd.common.ReliefUser;
import cz.incad.relief3.core.Configuration_A;
import java.io.Serializable;

/**
 *******************************************************************************
 *
 * @author martin
 */
public class R3CConfigurationEntity extends Configuration_A implements Serializable {

    public static final String CFG_CZBRD_REPOSITORY_XML = "CZBRD.REPOSITORY.XML";

    /**
     ***************************************************************************
     *
     * @param rec
     * @return
     */
    @Override
    public Record onGetRecord(Record rec) {
        ReliefUser ru = new ReliefUser(getTC());

        if (!ru.isSystemAdmin()) {
            rec.setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            rec.setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            rec.setAnnotation(AnnotationKeys.REMOVE_FORBIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        }

        super.onGetRecord(rec);
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

}
