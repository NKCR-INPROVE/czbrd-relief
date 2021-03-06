/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd.system;

import com.amaio.plaant.businessFunctions.*;
import com.amaio.plaant.desk.QueryException;
import com.amaio.plaant.metadata.*;
import com.amaio.plaant.sync.Record;
import com.amaio.repository.RepositoryController;
import com.amaio.repository.RepositoryHandler;
import com.amaio.security.impl.NoSuchRoleException;
import com.amaio.security.impl.NoSuchUserException;
import cz.incad.czbrd.common.DBB;
import cz.incad.czbrd.common.ReliefUser;
import cz.incad.relief3.core.Record_A;
import cz.incad.relief3.core.tools.Commons;
import cz.incad.relief3.core.tools.DirectConnection;
import cz.incad.relief3.core.tools.User;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
public class UsersAdministrationEntity extends Record_A implements Serializable {

    private static final Logger LOG = Logger.getLogger(UsersAdministrationEntity.class.getName());
    public static final String F_login_STR = "login";
    public static final String F_name_STR = "name";
    public static final String F_surname_STR = "surname";
    public static final String F_email_STR = "email";
    public static final String F_isSystemAdmin_BOO = "isSystemAdmin";
    public static final String F_isAppAdmin_BOO = "isAppAdmin";
    public static final String F_isCurator_BOO = "isCurator";
    public static final String F_isExplorer_BOO = "isExplorer";
    public static final String F_isCorrector_BOO = "isCorrector";
    public static final String F_note_STR = "note";
    public static final String F_cOrganization_STR = "cOrganization";
    public static final String F_isActive_BOO = "isActive";
    public static final String F_isEditorPh_BOO = "isEditorPh";

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
        if (ru.isSystemAdmin()) {
            //Administrátor Relief - smí vidět všechno
            return mtdt;
        }

        //Přidáme security sloupec když chybí
        columns = mtdt.getColumns();
        if (!columns.containsColumn(F_cOrganization_STR)) {
            column_cOrganization = DBB.createColumn(UsersAdministrationEntity.class.getName(), F_cOrganization_STR);
            if (column_cOrganization != null) {
                columns.addColumn(column_cOrganization);
            }
        }

        filter = new Filter();
        if (ru.isAppAdmin()) {
            //Administrátor aplikace - smí vidět záznamy své organizace
            column_cOrganization = DBB.createColumn(UsersAdministrationEntity.class.getName(), F_cOrganization_STR);
            if (column_cOrganization != null) {
                columns.addColumn(column_cOrganization);
                filter.addRule(new FilterRule(Filter.AND_OP, 1, column_cOrganization, Filter.EQUAL_CRIT, ru.getOrganization(), 1, false, false));
            }
        } else {
            //ostatní uživatelé - nesmí nic vidět
            column_cOrganization = DBB.createColumn(UsersAdministrationEntity.class.getName(), F_cOrganization_STR);
            if (column_cOrganization != null) {
                columns.addColumn(column_cOrganization);
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
        //super.onGetRecord(rec);
        Object isAppAdmin;
        Object isSystemAdmin;

        ReliefUser ru = new ReliefUser(getTC());
        if (ru.isSystemAdmin()) {
            //Administrátor relief, může všechno
        } else if (ru.isAppAdmin()) {
            //Administrátor aplikace
            isSystemAdmin = rec.getSimpleField(F_isSystemAdmin_BOO).getValue();
            isAppAdmin = rec.getSimpleField(F_isAppAdmin_BOO).getValue();

            if (isSystemAdmin == null) {
                isSystemAdmin = false;
            }

            if (isAppAdmin == null) {
                isAppAdmin = false;
            }

            if ((Boolean) isSystemAdmin || (Boolean) isAppAdmin) {
                rec.setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
                rec.setAnnotation(AnnotationKeys.REMOVE_FORBIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            } else {
                rec.getSimpleField(F_cOrganization_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
                rec.getSimpleField(F_isSystemAdmin_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
                rec.getSimpleField(F_isAppAdmin_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            }

        } else {
            //běžný uživatel, nemá na nic právo
            rec.setAnnotation(AnnotationKeys.HIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            rec.setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            rec.setAnnotation(AnnotationKeys.REMOVE_FORBIDDEN_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        }

        rec.getField(F_login_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
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

        if (ru.isSystemAdmin()) {

        } else if (ru.isAppAdmin()) {
            if (ru.getOrganization() == null || ru.getOrganization().length() == 0) {
                throw new AddException("Váš uživatelský účet nemá přiřazenu organizaci, což je podmínka nutná pro zakládání nových záznamů.");
            }

            rec.getSimpleField(F_cOrganization_STR).setValue(ru.getOrganization());
            rec.getSimpleField(F_cOrganization_STR).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            rec.getSimpleField(F_isSystemAdmin_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
            rec.getSimpleField(F_isAppAdmin_BOO).setAnnotation(AnnotationKeys.READ_ONLY_SECURITY_PROPERTY, AnnotationKeys.TRUE_VALUE);
        } else {
            throw new AddException("Váš uživatelský účet nemá oprávnění pro zakládání nových záznamů v této agendě.");
        }

        super.onCreateLocal(rec);
        rec.getSimpleField(F_isActive_BOO).setValue(true);
        rec.getSimpleField(F_isSystemAdmin_BOO).setValue(false);
        rec.getSimpleField(F_isAppAdmin_BOO).setValue(false);
        rec.getSimpleField(F_isCurator_BOO).setValue(false);
        rec.getSimpleField(F_isExplorer_BOO).setValue(false);
        rec.getSimpleField(F_isCorrector_BOO).setValue(false);
        rec.getSimpleField(F_isEditorPh_BOO).setValue(false);

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

        Connection conn = null;
        ResultSet rs;
        Statement stmt;
        String defaultPassword = null;
        ValidationException vex = Commons.getValidationException(getTC());

        //kontrola zdali je vyplněný login
        if (rec.getSimpleField(F_login_STR).getValue() == null) {
            //vyhození validační vyjímky
            vex.addField("Uživatelské jméno (Login)", "Povinné pole, zadejte Login uživatele.", false);
            throw vex;
        }

        //kontrola na validitu Login
        if (!Pattern.matches("[a-zA-Z0-9]*", (String) rec.getSimpleField(F_login_STR).getValue())) {
            vex.addField("Uživatelské jméno (Login)", "Pole obsahuje nepovolené znaky. Pole smí obsahovat pouze a-z,A-Z,0-9.", false);
            throw vex;
        }

        //Kontrola zdali tento uživatel již neexistuje v Databázi nebo repository aplikace.
        try {
            //Databázová část kontroly
            //vytvoříme si příme připojení do databáze
            conn = DirectConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select count(id) from UsersAdministration where login = '" + rec.getSimpleField(F_login_STR).getValue() + "'");
            rs.next();
            if (rs.getInt(1) == 1) {
                vex.addField("Uživatelské jméno (Login)", "Uživatel s tímto názvem již existuje.", false);
                throw vex;
            }
        } catch (SQLException ex1) {
            LOG.log(Level.SEVERE, "Chyba pri zpracovani SQL primou cestou.", ex1);
            throw new AddException("Nastala chyba při zpracování záznamu, zkuste akci opakovat, když bude chyba přetrvávat, kontaktujte dodavatele software.");
        } finally {
            //Uzavření přímého spojení do DB
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex2) {
                LOG.log(Level.SEVERE, "Chyba pri uzavirani primeho spojeni do DB.", ex2);
            }
        }

        String userLogin = ((String) rec.getSimpleField(F_login_STR).getValue());

        RepositoryController.attach();
        RepositoryHandler rHandler = RepositoryController.getCurrentSession();

        //v případě, že uživatel v repository neexistuje, tak se založí.
        if (!rHandler.getRepositoryAdministrator().identityExists(userLogin)) {
            try {
                rHandler.getRepositoryAdministrator().createIdentity(userLogin, defaultPassword, "admin");
                LOG.log(Level.FINE, "Zalozen novy uzivatel: " + userLogin);
            } catch (NoSuchRoleException ex) {
                LOG.log(Level.SEVERE, "Chyba pri zakladani uzivatele. Role 'admin' v repository neexistuje: " + userLogin, ex);
                vex.addField(userLogin, "Chyba při zakládání uživatele. Role 'admin' v repository neexistuje.", false);
                throw vex;
            }
        }

        //zavřeme handler
        rHandler.close();
        RepositoryController.detach();

        //nastavíme defaultní GUI nastavení uživatele
        try {
            ReliefUser.copyUserProperties("nastaveni", (String) rec.getSimpleField(UsersAdministrationEntity.F_login_STR).getValue());
        } catch (WizardException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        onCreateUpdate(rec);
        return rec;
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
        onCreateUpdate(rec);

        return rec;
    }

    /**
     ***************************************************************************
     *
     * @param rec
     * @throws ValidationException
     */
    private void onCreateUpdate(Record rec) throws ValidationException {
        Properties securityProperty;
        ValidationException vex = Commons.getValidationException(getTC());

        //Kontorla role Korektor, jestli je přítomna role průzkumník
        if (!(Boolean) rec.getSimpleField(F_isExplorer_BOO).getValue()) {
            rec.getSimpleField(F_isCorrector_BOO).setValue(false);
            vex.addField(F_isCorrector_BOO, "Role Korektor byla odebrána z důvodu absence role Průzkumník.", true);
        }

        //Načtení hodnot ze záznamu
        String userLogin = (String) rec.getSimpleField(F_login_STR).getValue();
        String userFirstName = (String) rec.getSimpleField(F_name_STR).getValue();
        String userSurname = (String) rec.getSimpleField(F_surname_STR).getValue();
        String userEmail = (String) rec.getSimpleField(F_email_STR).getValue();
        String userIsSystemAdmin = rec.getSimpleField(F_isSystemAdmin_BOO).getValue().toString();
        String userIsAppAdmin = rec.getSimpleField(F_isAppAdmin_BOO).getValue().toString();
        String userIsCurator = rec.getSimpleField(F_isCurator_BOO).getValue().toString();
        String userIsExplorer = rec.getSimpleField(F_isExplorer_BOO).getValue().toString();
        String userIsCorrector = rec.getSimpleField(F_isCorrector_BOO).getValue().toString();
        String userOrganization = (String) rec.getSimpleField(F_cOrganization_STR).getValue();
        String userIsEditorPh = rec.getSimpleField(F_isEditorPh_BOO).getValue().toString();
        
        LOG.log(Level.WARNING, "user is editor "+ userIsEditorPh);

        //Korekce dat pro uložení do Relief repository
        if (userFirstName == null) {
            userFirstName = "";
        }
        if (userSurname == null) {
            userSurname = "";
        }
        if (userEmail == null) {
            userEmail = "";
        }
        if (userOrganization == null) {
            userOrganization = "";
        }

        //Připojení do repository
        RepositoryController.attach();
        RepositoryHandler rHandler = RepositoryController.getCurrentSession();

        //nastavíme Properties uživateli.
        try {
            securityProperty = rHandler.getSecurityAdministrator().getIdentityProperties(userLogin);
            securityProperty.setProperty(ReliefUser.PROP_FIRSTNAME, userFirstName);
            securityProperty.setProperty(ReliefUser.PROP_SURNAME, userSurname);
            securityProperty.setProperty(ReliefUser.PROP_EMAIL, userEmail);
            securityProperty.setProperty(ReliefUser.PROP_ORGANIZATION, userOrganization);
            securityProperty.setProperty(ReliefUser.PROP_SYSTEM_ADMIN, userIsSystemAdmin);
            securityProperty.setProperty(ReliefUser.PROP_APP_ADMIN, userIsAppAdmin);
            securityProperty.setProperty(ReliefUser.PROP_CURATOR, userIsCurator);
            securityProperty.setProperty(ReliefUser.PROP_EXPLORER, userIsExplorer);
            securityProperty.setProperty(ReliefUser.PROP_CORRECTOR, userIsCorrector);
            securityProperty.setProperty(ReliefUser.PROP_EDITORPH, userIsEditorPh);
            rHandler.getSecurityAdministrator().setIdentityProperties(userLogin, securityProperty);
        } catch (NoSuchUserException ex) {
            vex.addField(userLogin, "Tento uživatel neexistuje v repository Relief.", false);
            throw vex;
        } finally {
            //uzavření repository
            closeRepository(rHandler);
        }

        //jestli vyvstala nějaká vyjímka, tak bude vyhozena.
        if (vex.isGravid()) {
            throw vex;
        }
    }

    /**
     ***************************************************************************
     *
     * @param rec
     * @throws DeleteException
     */
    @Override
    public void onRemove(Record rec) throws DeleteException {
        super.onRemove(rec);
        String userLogin = (String) rec.getSimpleField(F_login_STR).getValue();

        //Kontorly před smazáním uživatele
        if (userLogin.equalsIgnoreCase("admin")) {
            throw new DeleteException("Uživatele 'admin' není možné ze systému odstranit.");
        }

        if (userLogin.equalsIgnoreCase(User.getUserInfo(getTC()).getLoginName())) {
            throw new DeleteException("Nemůžete smazat uživatele, pod kterým jste přihlášeni.");
        }

        RepositoryController.attach();
        RepositoryHandler rHandler = RepositoryController.getCurrentSession();

        if (rHandler.getRepositoryAdministrator().identityExists(userLogin)) {
            rHandler.getRepositoryAdministrator().removeIdentity(userLogin);
            LOG.log(Level.FINE, "Uzivatel byl odstranen z repository: " + userLogin);
        } else {
            //uzivatel nebyl nalezen v Repository aplikace.
            LOG.log(Level.FINE, "Uzivael nebyl pri odstranovani z DB nalezen v repository: " + userLogin);
        }

        //zavřeme repository handler
        closeRepository(rHandler);
    }

    /**
     ***************************************************************************
     *
     * @param rHandler
     */
    private static void closeRepository(RepositoryHandler rHandler) {
        if (rHandler != null) {
            rHandler.close();
        }
        RepositoryController.detach();
    }

}
