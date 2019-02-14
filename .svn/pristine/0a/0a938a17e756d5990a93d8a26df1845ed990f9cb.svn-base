/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd;

import com.amaio.plaant.businessFunctions.AddException;
import com.amaio.plaant.businessFunctions.UpdateException;
import com.amaio.plaant.businessFunctions.ValidationException;
import com.amaio.plaant.sync.Record;
import cz.incad.relief3.core.Record_A;
import java.io.Serializable;

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
public class PrilohaEntity extends Record_A implements Serializable {

    public static final String F_typPrilohy_STR = "typPrilohy";
    public static final String F_typPapiru_STR = "typPapiru";
    public static final String F_typTisku_STR = "typTisku";
    public static final String F_barvaTisku_STR = "barvaTisku";
    public static final String F_cEvidenceMereniPriloha_COM = "cEvidenceMereniPriloha";
    public static final String F_vizitka_STR = "vizitka";
    public static final String F_nazevPrilohy_STR = "nazevPrilohy";
    public static final String F_note_STR = "note";
    public static final String F_rExemplar_STR = "rExemplar";
    public static final String F_strana_STR = "strana";
    //public static final String F__STR                = "";

    /**
     ***************************************************************************
     *
     * @return @throws AddException
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
     * @return @throws ValidationException
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
     * @param record
     * @return
     */
    private Record onCreateUpdate(Record rec) throws ValidationException {
        //vynulovani hodnot pokud je nastaven typ jako CD,GRAMOFONOVADESKA,DISKETA
        if ("cd".equals(rec.getSimpleField(F_typPrilohy_STR).getValue()) || "gramofonovadeska".equals(rec.getSimpleField(F_typPrilohy_STR).getValue()) || "disketa".equals(rec.getSimpleField(F_typPrilohy_STR).getValue())) {
            rec.getSimpleField(F_typPapiru_STR).setValue(null);
            rec.getSimpleField(F_typTisku_STR).setValue(null);
            rec.getSimpleField(F_barvaTisku_STR).setValue(null);
        }

        //Vytvoříme vizitku
        rec.getSimpleField(F_vizitka_STR).setValue(createVizitka(rec));

        return rec;
    }

    /**
     ***************************************************************************
     * nazev / autor carKod, signatura
     *
     * @param rec záznam musí být z agendy PrilohaEntity.
     * @return
     */
    public static String createVizitka(Record rec) {
        StringBuilder sb;

        sb = new StringBuilder(0);
        if (rec.getSimpleField(F_nazevPrilohy_STR).getValue() != null) {
            sb.append(rec.getSimpleField(F_nazevPrilohy_STR).getValue());
        }
        if (rec.getSimpleField(F_note_STR).getValue() != null) {
            sb.append("\n");
            sb.append(rec.getSimpleField(F_note_STR).getValue());
        }

        if (sb.toString().length() < 1) {
            return null;
        }
        return sb.toString();
    }

}
