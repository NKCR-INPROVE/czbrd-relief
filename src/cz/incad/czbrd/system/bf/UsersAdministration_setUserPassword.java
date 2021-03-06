/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd.system.bf;

import com.amaio.plaant.businessFunctions.*;
import com.amaio.plaant.sync.Record;
import com.amaio.repository.RepositoryController;
import com.amaio.repository.RepositoryHandler;
import com.amaio.security.impl.NoSuchUserException;
import cz.incad.czbrd.common.ReliefUser;
import cz.incad.czbrd.system.UsersAdministrationEntity;
import cz.incad.relief3.core.BussinesFunction_A;
import cz.incad.relief3.core.tools.Exceptions;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
public class UsersAdministration_setUserPassword extends BussinesFunction_A implements Serializable {

    private static final Logger LOG = Logger.getLogger(UsersAdministration_setUserPassword.class.getName());
    public static final String f_password = "password";
    public static final String panel_01 = "panel01";
    public static final String panel_02 = "panel02";
    private Object password;
    private String login;
    private String nacionale;

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
        if (!ReliefUser.isUserAllowedChangeOtherUser(ru, recUser)) {
            throw new WizardException("Nemáte právo měnit záznam uživatele vyšší nebo stejné úrovně.");
        }

        this.login = (String) recUser.getSimpleField(UsersAdministrationEntity.F_login_STR).getValue();
        this.nacionale = recUser.getSimpleField(UsersAdministrationEntity.F_surname_STR).getValue() + " " + recUser.getSimpleField(UsersAdministrationEntity.F_name_STR).getValue();

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
            this.password = getWCC().getWizardRecord().getSimpleField(f_password).getValue();
            if (this.password == null) {
                vex.addField("Nové heslo", "Pole nesmí být prázdné.", false);
                throw vex;
            }
            wm.addLine("Uživatel:   " + this.login + " (" + this.nacionale + ")");
            wm.addLine("Nové heslo: " + this.password);

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

        RepositoryController.attach();
        RepositoryHandler rHandler = RepositoryController.getCurrentSession();
        try {
            rHandler.getRepositoryAdministrator().setPassword(this.login, (String) this.password);
        } catch (NoSuchUserException ex) {
            LOG.log(Level.SEVERE, "Chyba pri zmene hesla uzivatele.\nLogin: " + login + "\nPass: " + this.password, ex);
        } finally {
            if (rHandler != null) {
                rHandler.close();
            }
            RepositoryController.detach();
        }

        wm.addLine("Změna přístupového hesla dokončena.");
        wm.addLine("Uživatel:   " + this.login);
        wm.addLine("Nové heslo: " + this.password);
        LOG.log(Level.FINE, "Zmena pristupoveho hesla.\nPrihlasovaci jmeno: " + this.login + "\nNove heslo: " + this.password);
        return wm;
    }

}
