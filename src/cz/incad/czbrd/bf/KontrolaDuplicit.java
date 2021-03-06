/* *****************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.incad.czbrd.bf;

import com.amaio.plaant.businessFunctions.*;
import com.amaio.plaant.sync.Field;
import com.amaio.plaant.sync.Record;
import cz.incad.czbrd.EvidenceMereniEntity;
import cz.incad.czbrd.EvidenceMereniPrilohaEntity;
import cz.incad.czbrd.ExemplarEntity;
import cz.incad.czbrd.PrilohaEntity;
import cz.incad.czbrd.common.ReliefUser;
import cz.incad.relief3.core.BussinesFunction_A;
import cz.incad.relief3.core.Record_A;
import cz.incad.relief3.core.tools.Commons;
import cz.incad.relief3.core.tools.Exceptions;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cz.incad.r3tools.R3DirectConnection;
import java.sql.SQLException;

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
public class KontrolaDuplicit extends BussinesFunction_A implements Serializable {
    private static final Logger LOG = Logger.getLogger(ExemplarEntity.class.getName());
    
    public static final String EXEMPLAR_CLASSNAME = "cz.incad.czbrd.Exemplar";
    private final String sqlCcnbPart1 = "select bibcnb from exemplar";
    private final String sqlCcnbPart2 = "group by bibcnb having count(*)>1";
    private final String sqlCarKodPart1 = "select bibcarkod from exemplar ";
    private final String sqlCarKodPart2 = " group by bibcarkod having count(*)>1";
    private final String sqlSignaturaPart1 = "select bibsignatura from exemplar ";
    private final String sqlSignaturaPart2 = " group by bibsignatura having count(*)>1";
    private String organizationUser = "";


    /**
     ***************************************************************************
     *
     * @return @throws ValidationException
     * @throws WizardException
     */
    public WizardMessage startWizard() throws ValidationException, WizardException {
        ReliefUser ru;

        ru = new ReliefUser(getWCC());

        organizationUser = ru.getOrganization();

        if (organizationUser == null) {
            throw new WizardException("Váš uživatelský účet nemá nemá přiřazenu organizaci, kontaktujte vašeho administrátora.");
        }

        if (!(ru.isCorrector() || ru.isAppAdmin() || ru.isSystemAdmin())) {
            throw new WizardException("Pro tuto akci nemáte dostatečná oprávnění.");
        }

        return null;
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
        if ("Panel01".equals(panelName)) {
            Boolean spustit = (Boolean) getWCC().getWizardRecord().getSimpleField("spustit").getValue();
            if (!spustit) {
                throw new WizardException("Nebylo zaškrtuno pole 'Spustit', proto nedošlo ke spuštění funkce.");
            }
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
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String vysledekCcnb = "";
        String vysledekCk = "";
        String vysledekSignatura = "";
        String sqlPodminka = "where corganization='" + this.organizationUser + "'";
        String sqlPrikaz = "";
        Boolean pokracuj = true;
        LOG.log(Level.WARNING, "Začínám pracovat");
        
        try {
            conn = R3DirectConnection.getConnection();
            stmt = conn.createStatement();
            LOG.log(Level.WARNING, "Nuluji předchozí duplicity.");
            sqlPrikaz = "update exemplar set duplicitaCcnb=0 where duplicitaCcnb=1";
            //pokracuj = stmt.execute(sqlPrikaz);
            stmt.execute(sqlPrikaz);
            LOG.log(Level.SEVERE, "sqlPrikaz: " + sqlPrikaz + " || pokracuj: " + pokracuj);
            //if (pokracuj) {
                sqlPrikaz = "update exemplar set duplicitaCk=0 " + sqlPodminka + " and duplicitaCk=1";
                //pokracuj = stmt.execute(sqlPrikaz);
                stmt.execute(sqlPrikaz);
                LOG.log(Level.SEVERE, "sqlPrikaz: " + sqlPrikaz + " || pokracuj: " + pokracuj);
            //}
            //if (pokracuj) {
                sqlPrikaz = "update exemplar set duplicitaSignatura=0 " + sqlPodminka + " and duplicitaSignatura=1";
                //pokracuj = stmt.execute(sqlPrikaz);
                stmt.execute(sqlPrikaz);
                LOG.log(Level.SEVERE, "sqlPrikaz: " + sqlPrikaz + " || pokracuj: " + pokracuj);
            //}
            LOG.log(Level.WARNING, "Hledám duplicity.");
            //if (pokracuj) {
                sqlPrikaz = "update exemplar set duplicitaCcnb=1 where bibcnb in (" + this.sqlCcnbPart1 + " " + this.sqlCcnbPart2 + ")";
                //pokracuj = stmt.execute(sqlPrikaz);
                stmt.execute(sqlPrikaz);
                LOG.log(Level.SEVERE, "sqlPrikaz: " + sqlPrikaz + " || pokracuj: " + pokracuj);
            //}
            //if (pokracuj) {
                sqlPrikaz = "update exemplar set duplicitaCk=1 where bibcarkod in (" + this.sqlCarKodPart1 + " " + sqlPodminka + " " + this.sqlCarKodPart2 + ")";
                //pokracuj = stmt.execute(sqlPrikaz);
                stmt.execute(sqlPrikaz);
                LOG.log(Level.SEVERE, "sqlPrikaz: " + sqlPrikaz + " || pokracuj: " + pokracuj);
            //}
            //if (pokracuj) {
                sqlPrikaz = "update exemplar set duplicitaSignatura=1 where bibsignatura in (" + this.sqlSignaturaPart1 + " " + sqlPodminka + " " + this.sqlSignaturaPart2 + ")";
                //pokracuj = stmt.execute(sqlPrikaz);
                stmt.execute(sqlPrikaz);
                LOG.log(Level.SEVERE, "sqlPrikaz: " + sqlPrikaz + " || pokracuj: " + pokracuj);
            //}

            //if (pokracuj) {
                //sqlPrikaz = "select count(*) as pocet from exemplar where duplicitaCcnb=1 group by bibcnb";
                sqlPrikaz = "select count(*) as pocet from (select count(bibcnb) from exemplar where duplicitaCcnb=1 and bibcnb is not null group by bibcnb)";
                LOG.log(Level.SEVERE, "sqlPrikaz: " + sqlPrikaz);
                rs = stmt.executeQuery(sqlPrikaz);
                rs.next();
                vysledekCcnb = "Bylo nalezeno " + popisPoctu(rs.getInt("POCET")) + "čcnb.";
                sqlPrikaz = "select count(*) as pocet from (select count(*) from exemplar " + sqlPodminka + " and duplicitaCk=1 group by bibcarkod)";
                LOG.log(Level.SEVERE, "sqlPrikaz: " + sqlPrikaz);
                rs = stmt.executeQuery(sqlPrikaz);
                rs.next();
                vysledekCk = "Bylo nalezeno " + popisPoctu(rs.getInt("POCET")) + "čarových kódů.";
                sqlPrikaz = "select count(*) as pocet from (select count(*) from exemplar " + sqlPodminka + " and duplicitaSignatura=1 group by bibsignatura)";
                LOG.log(Level.SEVERE, "sqlPrikaz: " + sqlPrikaz);
                rs = stmt.executeQuery(sqlPrikaz);
                rs.next();
                vysledekSignatura = "Bylo nalezeno " + popisPoctu(rs.getInt("POCET")) + "signatur.";
            //}

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Chyba při zpracování SQL: " + sqlPrikaz + " . " + ex.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                LOG.log(Level.SEVERE, "Chyba při volání rolbacku " + ex.getMessage());
            }
        } finally {
            try {
                conn.commit();
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                if (conn!=null) conn.close();
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Chyba při uzavírání spojení: " + ex.getMessage());
            }
        }

        LOG.log(Level.WARNING, "Zpracováno");
        
        wm.addLine("Rekapitulace změn:");
        wm.addLine("");
        wm.addLine(vysledekCcnb);
        wm.addLine(vysledekCk);
        wm.addLine(vysledekSignatura);
        wm.addLine("");
        wm.addLine("Záznamy můžete filtrovat pomocí polí: 'Duplicita v čČNB', 'Duplicita v čarovém kódu', 'Duplicita v signatuře'");
        wm.addLine("");
        wm.addLine("Označování duplicit a jejich odznačení probíhá pouze pomocí této funkce. Po upravení záznamů spusťte tuto funkci znovu. Tak se úprava projeví i v označení duplicit.");

        return wm;
    }

    /**
     * dopíše text okolo počtu záznamů
     * @param pocetZaznamu
     * @return (String)
     */
    private String popisPoctu(int pocetZaznamu) {
        String vysledek = "";
        if (pocetZaznamu==1) {
            vysledek = "" + pocetZaznamu + " duplicitní záznam ";
        } else if ((pocetZaznamu>1) && (pocetZaznamu<5)) {
            vysledek = "" + pocetZaznamu + " duplicitní záznamy ";
            
        } else {
            vysledek = "" + pocetZaznamu + " duplicitních záznamů ";
        }
        
        return vysledek;
    }
}
