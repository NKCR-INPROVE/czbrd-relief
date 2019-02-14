/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd;

import com.amaio.plaant.businessFunctions.AddException;
import com.amaio.plaant.businessFunctions.UpdateException;
import com.amaio.plaant.businessFunctions.ValidationException;
import com.amaio.plaant.sync.Record;
import cz.incad.relief3.core.Entity_A;
import java.io.Serializable;

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
public class ZasahyEntity extends Entity_A implements Serializable {

    public static final String F_odkyselovani_INT = "odkyselovani";
    public static final String F_restaurovani_INT = "restaurovani";
    public static final String F_konzervace_INT = "konzervace";
    public static final String F_mechCisteni_INT = "mechCisteni";
    public static final String F_chemCisteni_INT = "chemCisteni";
    public static final String F_dezinfekce_INT = "dezinfekce";
    public static final String F_jine_INT = "jine";
    public static final String F_kontrolniPruzkum_INT = "kontPruzkum";
    public static final String F_poZivelneKatastrofe_INT = "poZivKat";
    public static final String F_univerzalniPrevazba_INT = "univerzalniPrevazba";
    public static final String F_historizujiciPrevazba_INT = "historizujiciPrevazba";

    /**
     ***************************************************************************
     *
     * @param rec
     * @return
     * @throws AddException
     */
    @Override
    public Record onCreateLocal(Record rec) throws AddException {
        super.onCreateLocal(rec);
        rec.getSimpleField(F_odkyselovani_INT).setValue(0);
        rec.getSimpleField(F_restaurovani_INT).setValue(0);
        rec.getSimpleField(F_konzervace_INT).setValue(0);
        rec.getSimpleField(F_mechCisteni_INT).setValue(0);
        rec.getSimpleField(F_chemCisteni_INT).setValue(0);
        rec.getSimpleField(F_dezinfekce_INT).setValue(0);
        rec.getSimpleField(F_jine_INT).setValue(0);
        rec.getSimpleField(F_kontrolniPruzkum_INT).setValue(0);
        rec.getSimpleField(F_poZivelneKatastrofe_INT).setValue(0);
        rec.getSimpleField(F_univerzalniPrevazba_INT).setValue(0);
        rec.getSimpleField(F_historizujiciPrevazba_INT).setValue(0);

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
        return this.onCreateUpdate(rec);
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
        return this.onCreateUpdate(rec);
    }

    /**
     ***************************************************************************
     *
     * @param rec
     * @return
     */
    private Record onCreateUpdate(Record rec) throws ValidationException {

        return rec;
    }

}
