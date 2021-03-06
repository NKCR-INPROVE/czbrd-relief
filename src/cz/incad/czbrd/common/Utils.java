/* *****************************************************************************
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd.common;

import com.amaio.plaant.sync.Record;
import com.amaio.plaant.sync.SimpleField;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Node;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import cz.incad.czbrd.common.OneRecord.SubRecord;

/**
 *******************************************************************************
 *
 * @author Martin
 */
public class Utils implements Serializable {

    private static final Logger LOG = Logger.getLogger(Utils.class.getName());
    private static final String defaultOddelovacCarka = ", ";
    private static final String defaultOddelovacEnter = "\n";

    /**
     ***************************************************************************
     * soukromý defaultní konstruktor
     */
    private Utils() {
    }

    /**
     ***************************************************************************
     * Metoda kontroluje hodnotu pro PH. Povolené hodnoty jsou 1-12.
     *
     * @param value
     * @return
     */
    public static boolean isValid_pH(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Integer) {
            return !(((Integer) value) < 1 || ((Integer) value) > 12);
        }
        if (value instanceof Long) {
            return !(((Long) value).compareTo(new Long(1)) < 0 || ((Long) value).compareTo(new Long(12)) > 0);
        }
        if (value instanceof BigDecimal) {
            return !(((BigDecimal) value).compareTo(new BigDecimal(1)) < 0 || ((BigDecimal) value).compareTo(new BigDecimal(14)) > 0);
        }
        return !(((Double) value).compareTo(new Double(1)) < 0 || ((Double) value).compareTo(new Double(12)) > 0);
    }

    /**
     ***************************************************************************
     * Metoda kontroluje hodnotu pro Hmotnost. Povolené hodnoty jsou kladná
     * čísla.
     *
     * @param value
     * @return
     */
    public static boolean isValid_Hmotnost(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Integer) {
            return !(((Integer) value) < 0 || ((Integer) value) > 999999);
        }
        if (value instanceof Long) {
            return !(((Long) value).compareTo(new Long(0)) < 0 || ((Long) value).compareTo(new Long(999999)) > 0);
        }
        if (value instanceof BigDecimal) {
            return !(((BigDecimal) value).compareTo(new BigDecimal(0)) < 0 || ((BigDecimal) value).compareTo(new BigDecimal(999999)) > 0);
        }
        return !(((Double) value).compareTo(new Double(0)) < 0 || ((Double) value).compareTo(new Double(999999)) > 0);
    }

    /**
     ***************************************************************************
     * Metoda zaokrouhlí hodnotu v poli na 2 desetiná místa. Zaokrouhlí jen
     * BigDecimal a Double, ostatní ignoruje a nechá hodnotu bezeze změny.
     *
     * @param sf
     */
    public static void roundSimpleField(SimpleField sf) {
        Object value = sf.getValue();

        if (value == null || value instanceof Long || value instanceof Integer) {
            LOG.log(Level.WARNING, "DEBUG - Value is null or Long or Integer");
        } else if (value instanceof BigDecimal) {
            LOG.log(Level.WARNING, "DEBUG - Value BigDecimal");
            sf.setValue(BigDecimal.valueOf(Double.valueOf((Long) (Math.round((((BigDecimal) value).multiply(new BigDecimal(100))).doubleValue()))) / 100));
        } else if (value instanceof Double) {
            LOG.log(Level.WARNING, "DEBUG - Value Double");
            sf.setValue((Double.valueOf((Long) (Math.round((((Double) value) * 100)))) / 100));
        }
    }

    /**
     ***************************************************************************
     *
     * @param sfNeni
     * @param rec
     * @param values
     */
    public static void onePoskozeniValidation(Record rec, SimpleField sfNeni, String[] values) {
        Object fieldValue;

        for (String value : values) {
            fieldValue = rec.getFieldValue(value);
            if (fieldValue == null) {
                fieldValue = Boolean.FALSE;
            }
            if ((Boolean) fieldValue) {
                sfNeni.setValue(false);
                return;
            }
        }
        sfNeni.setValue(true);
    }

    public static Boolean jePrazdne(String valueStr) {
        Boolean vysledek = false;
        if ((valueStr==null) || (valueStr.isEmpty()))  {
            return true;
        }
        return vysledek;
    }

    public static Boolean jePrazdne(List<String> valueStr) {
        Boolean vysledek = false;
        if ((valueStr==null) || (valueStr.isEmpty()) || (valueStr.size()==0))  {
            return true;
        }
        return vysledek;
    }
    
    public static Boolean jePrazdne(Boolean valueStr) {
        Boolean vysledek = false;
        if ((valueStr==null) || (!valueStr.booleanValue()))  {
            return true;
        }
        return vysledek;
    }
    
    public static String vratString(Node input) {
        if (!jePrazdne(input.getTextContent())) {
            return input.getTextContent();
        } else if (!jePrazdne(input.getNodeValue())) {
            return input.getNodeValue();
        }
        return null;
    }

    public static String normalize(String vstup) {
        return vstup.trim().replace("  ", " ");
    }

    public static String toJson(Object vstup){
        String vystup = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            vystup = mapper.writeValueAsString(vstup);
        } catch (JsonProcessingException ex) {
            System.out.println("chyba konverze na JSOU: " + ex.getMessage());
        }
        
        return vystup;
    }
    
    public static String ListToString(List<String> seznamHodnot) {
        String vystup = "";
        if (!jePrazdne(seznamHodnot)) {
            for (int i=0; i<seznamHodnot.size(); i++) {
                if (i>0) vystup = vystup + "', '";
                vystup = vystup + seznamHodnot.get(i);
            }
        }
        return vystup;
    }

    public static String vratStringSOddelovacem(String nazevPole, String vstup) {
        return vratStringSOddelovacem(nazevPole, vstup, defaultOddelovacEnter);
    }

    public static String vratStringSOddelovacem(String nazevPole, List<String> vstup) {
        return vratStringSOddelovacem(nazevPole, ListToString(vstup));
    }

    public static String vratStringSOddelovacem(String nazevPole, SubRecord vstup) {
        return vratStringSOddelovacem(nazevPole, vstup.toString());
    }

    public static String vratStringSOddelovacem(String nazevPole, SubRecord vstup, String oddelovac) {
        return vratStringSOddelovacem(nazevPole, vstup.toString(), oddelovac);
    }

    public static String vratStringSOddelovacemLS(String nazevPole, List<SubRecord> vstup) {
        String vystup = "";
        String oddelovac = "";
        for (int i=0; i<vstup.size(); i++) {
            vystup += vratStringSOddelovacem("", vstup.get(i), (i<vstup.size() ? defaultOddelovacCarka : defaultOddelovacEnter));
        }
        if (!"".equals(vystup)) {
            if (defaultOddelovacCarka.equals(vystup.substring(vystup.length()-2))) { vystup = vystup.substring(0, vystup.length()-2) + defaultOddelovacEnter; }
            vystup = nazevPole + ": " + vystup;
        }
        return vystup;
    }

    public static String vratStringSOddelovacem(String nazevPole, String vstup, String oddelovac) {
        String vysledek = "";
        if (!Utils.jePrazdne(vstup)) {
            if (!"".equals(nazevPole)) { vysledek = nazevPole + ": "; }
            vysledek = vysledek + vstup + oddelovac;
        }
        return vysledek;
    }

}
