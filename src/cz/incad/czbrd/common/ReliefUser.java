/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd.common;

import com.amaio.plaant.businessFunctions.BusinessFunctionContext;
import com.amaio.plaant.businessFunctions.WizardException;
import com.amaio.plaant.businessFunctions.services.ServiceException;
import com.amaio.plaant.businessFunctions.services.user.UserInfo;
import com.amaio.plaant.businessFunctions.services.user.UserService;
import com.amaio.plaant.sync.Record;
import com.amaio.repository.RepositoryController;
import com.amaio.repository.RepositoryHandler;
import com.amaio.security.impl.NoSuchUserException;
import cz.incad.czbrd.system.UsersAdministrationEntity;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
public class ReliefUser implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReliefUser.class.getName());
    /**
     * Instance informací o uživateli
     */
    private UserInfo ui;

    /**
     * User Properties firstname
     */
    public static final String PROP_FIRSTNAME = "com.amaio.security.identity.first_name";
    /**
     * User Properties surname
     */
    public static final String PROP_SURNAME = "com.amaio.security.identity.last_name";
    /**
     * User Properties email
     */
    public static final String PROP_EMAIL = "com.amaio.security.identity.email";

    public static final String PROP_SYSTEM_ADMIN = "cz.incad.relief.czbrd.issystemadmin";
    public static final String PROP_APP_ADMIN = "cz.incad.relief.czbrd.isappadmin";
    public static final String PROP_CURATOR = "cz.incad.relief.czbrd.iscurator";
    public static final String PROP_EXPLORER = "cz.incad.relief.czbrd.isexplorer";
    public static final String PROP_CORRECTOR = "cz.incad.relief.czbrd.isCorrector";
    public static final String PROP_ORGANIZATION = "cz.incad.relief.czbrd.organization";
    public static final String PROP_EDITORPH =  "cz.incad.relief.czbrd.iseditorph";
    /**
     * Value true
     */
    public static final String PROP_VALUE_TRUE = "true";
    /**
     * Value false
     */
    public static final String PROP_VALUE_FALSE = "flase";

    /**
     ***************************************************************************
     * Konstruktor
     *
     * @param context
     */
    public ReliefUser(BusinessFunctionContext context) {
        UserService service = (UserService) context.getService(UserService.class.getName());
        try {
            ui = (UserInfo) service.execute(null);
        } catch (ServiceException ex) {
            LOG.log(Level.SEVERE, "Chyba pri vyzvedavani informaci o prihlasenem uzivateli.", ex);
        }
    }

    /**
     ***************************************************************************
     *
     * @return
     */
    public UserInfo getUserInfo() {
        return this.ui;
    }

    /**
     ***************************************************************************
     * Získá údaje o právě přihlášeném uživateli.
     *
     * @param context
     * @return Údaje o uživateli nebo null v případě chyby.Chyba spadne do logu.
     */
    public static UserInfo getUserInfo(BusinessFunctionContext context) {
        UserInfo ui = null;
        UserService service;

        service = (UserService) context.getService(UserService.class.getName());
        try {
            ui = (UserInfo) service.execute(null);
        } catch (ServiceException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return ui;
    }

    /**
     ***************************************************************************
     * Metoda vrací hodnotu vstupní ReliefUser Properties.
     *
     * @param property
     * @return Hodnota vstupní properties, v případě, že properties neexistuje,
     * vrátí prázdnou hodnotu ""
     */
    public String getProperties(String property) {
        return getUserInfo().getProperties().getProperty(property, "");
    }

    /**
     ***************************************************************************
     * Metoda vrátí LoginName právě přihlášeného užicatele.
     *
     * @return LoginName aktuálního uživatele
     */
    public String getLogin() {
        return getUserInfo().getLoginName();
    }

    /**
     ***************************************************************************
     *
     * @return
     */
    public boolean isSystemAdmin() {
        return PROP_VALUE_TRUE.equals(getProperties(PROP_SYSTEM_ADMIN));
    }

    /**
     ***************************************************************************
     *
     * @return
     */
    public boolean isAppAdmin() {
        return PROP_VALUE_TRUE.equals(getProperties(PROP_APP_ADMIN));
    }

    /**
     ***************************************************************************
     *
     * @return
     */
    public boolean isCurator() {
        return PROP_VALUE_TRUE.equals(getProperties(PROP_CURATOR));
    }

    /**
     ***************************************************************************
     *
     * @return
     */
    public boolean isExplorer() {
        return PROP_VALUE_TRUE.equals(getProperties(PROP_EXPLORER));
    }

    /**
     ***************************************************************************
     *
     * @return
     */
    public boolean isCorrector() {
        return PROP_VALUE_TRUE.equals(getProperties(PROP_CORRECTOR));
    }

    /**
     ***************************************************************************
     *
     * @return
     */
    public boolean isEditorPh() {
        return PROP_VALUE_TRUE.equals(getProperties(PROP_EDITORPH));
    }
    
    /**
     ***************************************************************************
     *
     * @return
     */
    public String getOrganization() {
        return getProperties(PROP_ORGANIZATION);
    }

    /**
     ***************************************************************************
     *
     * @param ru
     * @param recUser
     * @return
     */
    public static boolean isUserAllowedChangeOtherUser(ReliefUser ru, Record recUser) {
        if (ru.isSystemAdmin()) {
            return true;
        }

        //if (!(ru.isAppAdmin() || ru.isSystemAdmin())) return false;
        if (ru.getLogin().equals(recUser.getSimpleField(UsersAdministrationEntity.F_login_STR).getValue())) {
            return true;
        }
        if (ru.isSystemAdmin() && ((Boolean) recUser.getSimpleField(UsersAdministrationEntity.F_isSystemAdmin_BOO).getValue())) {
            return false;
        } else if (ru.isAppAdmin() && ((Boolean) recUser.getSimpleField(UsersAdministrationEntity.F_isSystemAdmin_BOO).getValue() || (Boolean) recUser.getSimpleField(UsersAdministrationEntity.F_isAppAdmin_BOO).getValue())) {
            return false;
        }

        return true;
    }

    /**
     ***************************************************************************
     *
     * @param userNameSource
     * @param userNameTarget
     * @throws WizardException
     */
    public static void copyUserProperties(String userNameSource, String userNameTarget) throws WizardException {
        Reader readIt;
        Writer writeIt;
        int data;

        RepositoryController.attach();
        RepositoryHandler rHandler = RepositoryController.getCurrentSession();
        try {
            readIt = rHandler.getRepositoryAdministrator().getIdentityPropsReader(userNameSource, "com.amaio.plaant.metadata");
            writeIt = rHandler.getRepositoryAdministrator().getIdentityPropsWriter(userNameTarget, "com.amaio.plaant.metadata");

            while ((data = readIt.read()) != -1) {
                writeIt.write(data);
            }
            writeIt.flush();
            readIt.close();
            writeIt.close();
            LOG.log(Level.FINE, "PROPERTIES COPYED SUCCESSFULLY:\nSourceUser: " + userNameSource + "\nTrgetUser: " + userNameTarget + "\n");
        } catch (NoSuchUserException ex) {
            LOG.log(Level.SEVERE, "Zdrojovy nebo cilovy uzivatel neexistuje v repository Relief.\nSourceUser: " + userNameSource + "\nTrgetUser: " + userNameTarget + "\n", ex);
            throw new WizardException("Zdrojový nebo cílový uživatel neexistuje v repository Relief. Nastavení nebylo zkopírováno. Systém je citlivý na malá/velká písmena.");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Chyba pri kopirovani Metadat\nSourceUser: " + userNameSource + "\nTrgetUser: " + userNameTarget + "\n", ex);
            throw new WizardException("IO chyba přímo při kopírování. Data Cílového uživatele mohou být poškozena. Nepřihlašujte se pod tímto uživatelem: " + userNameTarget + " dokud nezjistíte příčinu tété chyby a neopravíte její následky.");
        } catch (NullPointerException ex) {
            LOG.log(Level.SEVERE, "Chyba pri otvirani streamu pro kopirovani Metadat\nSourceUser: " + userNameSource + "\nTrgetUser: " + userNameTarget + "\n", ex);
            throw new WizardException("Zdrojový uživatel nemá žádné nastavení, které lze kopírovat. Nastavení nebylo zkopírováno.");
        } finally {
            if (rHandler != null) {
                rHandler.close();
            }
            RepositoryController.detach();
        }
    }

}
