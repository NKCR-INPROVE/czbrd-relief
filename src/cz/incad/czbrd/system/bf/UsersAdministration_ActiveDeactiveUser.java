/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd.system.bf;

import com.amaio.plaant.businessFunctions.ApplicationErrorException;
import com.amaio.plaant.businessFunctions.RecordsIterator;
import com.amaio.plaant.businessFunctions.ValidationException;
import com.amaio.plaant.businessFunctions.WizardException;
import com.amaio.plaant.businessFunctions.WizardMessage;
import com.amaio.plaant.sync.Record;
import com.amaio.repository.RepositoryController;
import com.amaio.repository.RepositoryHandler;
import com.amaio.security.impl.NoSuchUserException;
import cz.incad.czbrd.common.ReliefUser;
import cz.incad.czbrd.system.UsersAdministrationEntity;
import cz.incad.relief3.core.BussinesFunction_A;
import cz.incad.relief3.core.tools.Commons;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *******************************************************************************
 *
 * @author martin
 */
public class UsersAdministration_ActiveDeactiveUser extends BussinesFunction_A implements Serializable {

    private static final Logger LOG = Logger.getLogger(UsersAdministration_ActiveDeactiveUser.class.getName());
    private Record recUser;
    private String login;
    private String nacionale;
    private boolean isActive;
    private String password;

    /**
     ***************************************************************************
     *
     * @return @throws ValidationException
     * @throws WizardException
     */
    public WizardMessage startWizard() throws ValidationException, WizardException {
        WizardMessage wm = new WizardMessage();
        RecordsIterator ritSelected;
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
        this.isActive = (Boolean) recUser.getSimpleField(UsersAdministrationEntity.F_isActive_BOO).getValue();

        wm.addLine("Uživatelské jméno: " + login);
        wm.addLine("Příjmení, jméno:   " + nacionale);
        wm.addLine("");
        if (isActive) {
            wm.addLine("Uživatel je aktivní a bude přepnut do stavu NEAKTIVNÍ.");
            this.password = "abrakadabra";
        } else {
            wm.addLine("Uživatel je neaktivní, bude aktivován a bude mu resetováno heslo.");
            wm.addLine("Při prvním přihlášení nebude zadávat heslo a systém ho vyzve k zadání nového hesla.");
            this.password = "";
        }

        return wm;
    }

    /**
     ***************************************************************************
     *
     * @param string
     * @return
     * @throws ValidationException
     * @throws WizardException
     */
    public WizardMessage panelLeave(String string) throws ValidationException, WizardException {
        return null;
    }

    /**
     ***************************************************************************
     *
     * @return @throws ValidationException
     * @throws WizardException
     * @throws ApplicationErrorException
     */
    public WizardMessage runBusinessMethod() throws ValidationException, WizardException, ApplicationErrorException {
        WizardMessage wm;

        RepositoryController.attach();
        RepositoryHandler rHandler = RepositoryController.getCurrentSession();
        try {
            rHandler.getRepositoryAdministrator().setPassword(this.login, this.password);
        } catch (NoSuchUserException ex) {
            LOG.log(Level.SEVERE, "Chyba pri zmene hesla uzivatele.\nLogin: " + login + "\nPass: " + this.password, ex);
            wm = new WizardMessage();
            wm.addLine("Nastala chyba při běhu funkce. Uživatel, kterému se snažíte změnit stav, již neexistuje v repository uživatelů Relief.");
            return wm;
        } finally {
            if (rHandler != null) {
                rHandler.close();
            }
            RepositoryController.detach();
        }

        // zmena stavu uživatele
        getWCC().addRootDomain(Commons.getDomain(Commons.getClassNameWithoutEntity(UsersAdministrationEntity.class.getName()), getWCC()));
        recUser.getSimpleField(UsersAdministrationEntity.F_isActive_BOO).setValue(!(Boolean) recUser.getSimpleField(UsersAdministrationEntity.F_isActive_BOO).getValue());
        getWCC().commit();

        return null;
    }

}
