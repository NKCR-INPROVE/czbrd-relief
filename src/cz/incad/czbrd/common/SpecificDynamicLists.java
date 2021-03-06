/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd.common;

import com.amaio.plaant.dbdef.ListSource;
import com.amaio.plaant.dbdef.ListSourceItem;
import com.amaio.plaant.dbdef.ListValue;
import cz.incad.relief3.core.tools.DirectConnection;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
public class SpecificDynamicLists implements ListSource, Serializable {

    private static final Logger LOG = Logger.getLogger(SpecificDynamicLists.class.getName());
    private final Object[] parameters;
    private static final HashMap<String, ListSourceItem[]> allLists = new HashMap<String, ListSourceItem[]>();

    /**
     ***************************************************************************
     *
     * @param parameters
     */
    public SpecificDynamicLists(Object[] parameters) {
        this.parameters = parameters;
    }

    /**
     ***************************************************************************
     *
     * @param listName
     * @return
     */
    public ListSourceItem[] getList(String listName) {
        ListSourceItem[] customListFinal = new ListSourceItem[0];
        List<ListSourceItem> newList = new LinkedList<ListSourceItem>();
        Connection conn = DirectConnection.getConnection();
        Statement stmtSQL = null;
        ResultSet rsSQL = null;

        try {
            if ("ListUsersAdministration_byOrganization".equals(listName)) {
                /*
                 * Parameters
                 * 0 - Organization
                 */
                if (parameters.length != 1 || parameters[0] == null || ((String) parameters[0]).length() == 0) {
                    LOG.log(Level.WARNING, "Spatne definovany dynamicky list.");
                    return customListFinal;
                }

                customListFinal = allLists.get(listName + "_" + parameters[0]);
                if (customListFinal == null || customListFinal.length == 0) {
                    allLists.put(listName + "_" + parameters[0], reloadList(listName + "_" + parameters[0]));
                    customListFinal = allLists.get(listName + "_" + parameters[0]);
                    if (customListFinal == null) {
                        return customListFinal;
                    }
                }
                return newList.toArray(customListFinal);

            } else if ("".equals(listName)) {
                return customListFinal;
            } else if ("".equals(listName)) {
                return customListFinal;
            } else if ("".equals(listName)) {
                return customListFinal;
            } else {
                // Nebyl nalezen zadny list
                LOG.log(Level.SEVERE, "WARNING: nebyl nalezen spacificky dynamicky list: " + listName);
                return customListFinal;
            }

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Vyjímka při generování custom Dynamic Listu: " + listName, ex);
            return customListFinal;
        } finally {
            try {
                if (rsSQL != null) {
                    rsSQL.close();
                }
                if (stmtSQL != null) {
                    stmtSQL.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                LOG.log(Level.OFF, "Vyjímka při uzavírání připojení do databáze.", ex);
            }
        }
    }

    /**
     ***************************************************************************
     *
     * @param listName
     * @return
     */
    private ListSourceItem[] reloadList(String listName) {
        ListSourceItem[] customListFinal = new ListSourceItem[0];
        List<ListSourceItem> newList = new LinkedList<ListSourceItem>();
        Connection conn = DirectConnection.getConnection();
        Statement stmtSQL = null;
        ResultSet rsSQL = null;

        try {
            if (listName.startsWith("ListUsersAdministration_byOrganization")) {

                //pridani prazdne hodnoty
                newList.add(new ListValue("", "[null]"));
                stmtSQL = conn.createStatement();
                rsSQL = stmtSQL.executeQuery("select value,cz from dlists where classname = 'cz.incad.nkp.digital.DpracovnikUkol' order by cz");
                while (rsSQL.next()) {
                    // 2 = i18n, 1 = value (DB) !!!
                    newList.add(new ListValue(rsSQL.getString(2), rsSQL.getString(1)));
                }
                return newList.toArray(customListFinal);

            } else if ("".equals(listName)) {
                return customListFinal;
            } else if ("".equals(listName)) {
                return customListFinal;
            } else if ("".equals(listName)) {
                return customListFinal;
            } else {
                // Nebyl nalezen zadny list
                LOG.log(Level.SEVERE, "WARNING: nebyl nalezen specificky dynamicky list: " + listName);
                return customListFinal;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Vyjímka při generování custom Dynamic Listu: " + listName, ex);
            return customListFinal;
        } finally {
            try {
                if (rsSQL != null) {
                    rsSQL.close();
                }
                if (stmtSQL != null) {
                    stmtSQL.close();
                }
                if (conn != null) {
                    conn.close();
                }
                //ReliefLogger.severe("SPECIFICKE DLISTY - FINALY BLOK");
            } catch (SQLException ex) {
                LOG.log(Level.OFF, "Vyjímka při uzavírání připojení do databáze.", ex);
            }
        }
    }

}
