/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd.system.bf;

import com.amaio.plaant.businessFunctions.ApplicationErrorException;
import com.amaio.plaant.businessFunctions.ValidationException;
import com.amaio.plaant.businessFunctions.WizardException;
import com.amaio.plaant.businessFunctions.WizardMessage;
import cz.incad.czbrd.system.UsersAdministrationEntity;
import cz.incad.relief3.core.BussinesFunction_A;
import java.io.Serializable;

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
public class UsersAdministration_setReliefAdmin extends BussinesFunction_A implements Serializable {

    public static final String F_password_STR = "password";

    /**
     ***************************************************************************
     *
     * @return @throws ValidationException
     * @throws WizardException
     */
    public WizardMessage startWizard() throws ValidationException, WizardException {
        if (getWCC().getSelectedKeys().length != 1) {
            throw new WizardException("Pro tuto funkci musí být vybrán právě jeden záznam.");
        }
        return null;
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
        if (!"abrakadabra".equals(getWCC().getWizardRecord().getSimpleField(UsersAdministration_setReliefAdmin.F_password_STR).getValue())) {
            throw new WizardException("Nezadali jste správné heslo.");
        }
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
        WizardMessage wm = new WizardMessage();
        getWCC().getSelectedRecords().nextRecord().getSimpleField(UsersAdministrationEntity.F_isSystemAdmin_BOO).setValue(true);
        getWCC().commit();
        return wm.addLine("Uživateli byla nastavena role Administrátor Relief.");
    }

}
