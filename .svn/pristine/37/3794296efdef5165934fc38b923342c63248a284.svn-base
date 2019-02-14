/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd;

import com.amaio.plaant.businessFunctions.AddException;
import com.amaio.plaant.businessFunctions.UpdateException;
import com.amaio.plaant.businessFunctions.ValidationException;
import com.amaio.plaant.sync.BinaryValue;
import com.amaio.plaant.sync.Record;
import cz.incad.relief3.core.Record_A;
import cz.incad.relief3.core.tools.Utilities;
import java.io.Serializable;

/**
 *******************************************************************************
 *
 * @author martin
 */
public class SouborEntity extends Record_A implements Serializable {

    public static final String F_fileBinary_BIN = "file";
    public static final String F_fileName_STR = "fileName";
    public static final String F_fileSize_INT = "fileSize";
    public static final String F_fileMimetype_STR = "fileMimetype";
    public static final String F_note_STR = "note";
    public static final String F_rEvidenceMereni_REF = "rEvidenceMereni";
    public static final String F_rEvidenceMereniPriloha_REF = "rEvidenceMereniPriloha";

    /**
     ***************************************************************************
     *
     * @return @throws AddException
     */
    @Override
    public Record onCreateLocal(Record rec) throws AddException {
        rec = super.onCreateLocal(rec);
        return rec;
    }

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
        //Zpracovani souboru
        BinaryValue bindata = (BinaryValue) rec.getBinaryField(SouborEntity.F_fileBinary_BIN).getValue();
        if (!Utilities.isBinDataDeleted(bindata)) {
            rec.getSimpleField(SouborEntity.F_fileName_STR).setValue(bindata.getName());
            rec.getSimpleField(SouborEntity.F_fileSize_INT).setValue(Integer.parseInt(Utilities.bytes2Kilobytes(bindata.getSize())));
            rec.getSimpleField(SouborEntity.F_fileMimetype_STR).setValue(bindata.getMimeType().getBaseType());
        } else {
            rec.getSimpleField(SouborEntity.F_fileName_STR).setValue(null);
            rec.getSimpleField(SouborEntity.F_fileSize_INT).setValue(null);
            rec.getSimpleField(SouborEntity.F_fileMimetype_STR).setValue(null);
        }
        return rec;
    }

}
