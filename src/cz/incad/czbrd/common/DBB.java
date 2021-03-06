/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd.common;

import com.amaio.plaant.DbBrowser;
import com.amaio.plaant.metadata.Column;
import cz.incad.relief3.core.tools.Commons;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
public class DBB implements Serializable {

    private static final Logger LOG = Logger.getLogger(DBB.class.getName());
    public static DbBrowser dbb = Commons.getDbBrowser();

    /**
     ***************************************************************************
     *
     */
    public static void reSetDBB() {
        DBB.dbb = Commons.getDbBrowser();
    }

    /**
     ***************************************************************************
     *
     * @param classname
     * @param field
     * @return
     */
    public static Column createColumn(String classname, String field) {
        Column column = new Column();
        classname = Commons.getClassNameWithoutEntity(classname);

        try {
            column.setColumnName(field);
            column.setDbField(dbb.findObject(classname).getMember(field));
            return column;
        } catch (RemoteException ex1) {
            LOG.log(Level.SEVERE, null, ex1);
            try {
                column.setColumnName(field);
                reSetDBB();
                column.setDbField(dbb.findObject(classname).getMember(field));
                return column;
            } catch (RemoteException ex2) {
                LOG.log(Level.SEVERE, null, ex2);
                return null;
            }
        }
    }

}
