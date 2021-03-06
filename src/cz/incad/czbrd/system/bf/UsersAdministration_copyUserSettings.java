/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd.system.bf;

import com.amaio.plaant.businessFunctions.*;
import com.amaio.plaant.dbdef.ListSource;
import com.amaio.plaant.dbdef.ListSourceItem;
import com.amaio.plaant.dbdef.ListValue;
import com.amaio.plaant.sync.Record;
import cz.incad.czbrd.common.ReliefUser;
import cz.incad.czbrd.system.UsersAdministrationEntity;
import cz.incad.relief3.core.BussinesFunction_A;
import cz.incad.relief3.core.tools.DirectConnection;
import cz.incad.relief3.core.tools.Exceptions;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
public class UsersAdministration_copyUserSettings extends BussinesFunction_A implements Serializable {

    private static final Logger LOG = Logger.getLogger(UsersAdministration_copyUserSettings.class.getName());
    public static final String f_masterLoginName = "masterLoginName";
    public static final String panel_01 = "panel01";
    public static final String panel_02 = "panel02";
    private Object masterLoginName;
    private String recLogin;

    /**
     ***************************************************************************
     *
     * @return @throws ValidationException
     * @throws WizardException
     */
    public WizardMessage startWizard() throws ValidationException, WizardException {
        WizardMessage wm = new WizardMessage();
        RecordsIterator ritSelected;
        Record recUser;
        ReliefUser ru = new ReliefUser(getWCC());

        if (!(ru.isAppAdmin() || ru.isSystemAdmin())) {
            throw new WizardException("Nemáte oprávnění pro spuštění této funkce");
        }

        ritSelected = getWCC().getSelectedRecords();
        if (ritSelected.getRecordsCount() != 1) {
            throw new WizardException("Pro tuto funkci musí být vybrán právě jeden záznam.");
        }
        recUser = ritSelected.nextRecord();
        this.recLogin = (String) recUser.getSimpleField(UsersAdministrationEntity.F_login_STR).getValue();

        if (!ReliefUser.isUserAllowedChangeOtherUser(ru, recUser)) {
            throw new WizardException("Nemáte právo měnit záznam uživatele vyšší nebo stejné úrovně.");
        }

        if (ru.getLogin().equals(this.recLogin)) {
            throw new WizardException("Nelze měnit nastavení uživatele pod kterým jste přihlášeni.");
        }

        //připravíme hodnoty do dynamického listu.
        getWCC().getWizardRecord().getField(f_masterLoginName).setAnnotation(AnnotationKeys.LIST_SOURCE_CUSTOM_PROPERTY, new SpecificUsersList(ru));

        return wm;
    }

    /**
     ***************************************************************************
     *
     * @param panelName
     * @return
     * @throws ValidationException
     * @throws WizardException
     */
    public WizardMessage panelLeave(String panelName) throws ValidationException, WizardException {
        WizardMessage wm = new WizardMessage();
        ValidationException vex = Exceptions.getValidationException(getWCC());

        if (panelName.equals(panel_01)) {
            this.masterLoginName = getWCC().getWizardRecord().getSimpleField(f_masterLoginName).getValue();
            if (this.masterLoginName == null) {
                vex.addField("Uživatel", "Pole nesmí být prázdné.", false);
                throw vex;
            }

            //kontrolujeme zdali se uživatel nepokouší měnit nastavení zdrojového uživatele
            if (this.masterLoginName.equals(this.recLogin)) {
                vex.addField("Uživatel", "Zdrojový a cíloví uživatel je tentýž.", false);
                throw vex;
            }

            wm.addLine("Zdrojový uživatel: " + this.masterLoginName);
            wm.addLine("");
            wm.addLine("Uživatel kterému budeme nastavovat nové nastavení: " + this.recLogin);

        } else if (panelName.equals(panel_02)) {
        } else {
            throw new WizardException("Nepodporovaný panel funkce, kontaktujte dodavatele software.");
        }
        return wm;
    }

    /**
     ***************************************************************************
     *
     * @return @throws ValidationException
     * @throws WizardException
     * @throws ApplicationErrorException
     */
    public WizardMessage runBusinessMethod() throws ValidationException, WizardException, ApplicationErrorException {
        WizardMessage wm = new WizardMessage();
        ReliefUser.copyUserProperties((String) this.masterLoginName, recLogin);
        wm.addLine("Kopírování nastavení GUI dokončeno.");
        return wm;
    }

}

/**
 *******************************************************************************
 *
 * @author martin
 */
class UserDescription {

    public final String login;
    public final String nacionale;

    public UserDescription(String login, String nacionale) {
        this.login = login;
        this.nacionale = nacionale;
    }

}

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
class SpecificUsersList implements ListSource {

    private static final Logger LOG = Logger.getLogger(SpecificUsersList.class.getName());
    private final boolean isSystemAdmin;
    private final String organization;

    /**
     ***************************************************************************
     *
     * @param ru
     */
    public SpecificUsersList(ReliefUser ru) {
        this.isSystemAdmin = ru.isSystemAdmin();
        this.organization = ru.getOrganization();
    }

    /**
     ***************************************************************************
     *
     * @param listId
     * @return
     */
    public ListSourceItem[] getList(String listId) {
        ListSourceItem[] customListFinal = new ListSourceItem[0];
        List<ListSourceItem> newList = new LinkedList<ListSourceItem>();
        Connection conn = DirectConnection.getConnection();
        Statement stmtSQL = null;
        ResultSet rsSQL = null;
        StringBuilder sbSQL = new StringBuilder(0);

        try {
            newList.add(new ListValue("", "[null]"));
            stmtSQL = conn.createStatement();

            if (isSystemAdmin) {
                sbSQL.append("select login,name,surname,corganization from usersadministration order by login");
            } else {
                if (organization == null || organization.length() == 0) {
                    return customListFinal;
                }
                sbSQL.append("select login,name,surname,corganization from usersadministration where corganization = '").append(organization).append("' order by login");
            }

            rsSQL = stmtSQL.executeQuery(sbSQL.toString());
            while (rsSQL.next()) {
                // 2 = i18n, 1 = value (DB) !!!
                newList.add(new ListValue(rsSQL.getString("login") + " (" + rsSQL.getString("surname") + " " + rsSQL.getString("name") + " - " + rsSQL.getString("corganization") + ")", rsSQL.getString("login")));
            }
            return newList.toArray(customListFinal);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Vyjímka při generování custom Dynamic Listu:" + listId, ex);
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
                LOG.log(Level.WARNING, "Vyjímka při uzavírání připojení do databáze.", ex);
            }
        }
    }

}
