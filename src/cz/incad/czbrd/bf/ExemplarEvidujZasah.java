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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
public class ExemplarEvidujZasah extends BussinesFunction_A implements Serializable {
    private static final Logger LOG = Logger.getLogger(ExemplarEntity.class.getName());
    
    public static final String EVIDENCE_MERENI_CLASSNAME = "cz.incad.czbrd.EvidenceMereni";
    public static final String EVIDENCE_MERENI_PRILOHA_CLASSNAME = "cz.incad.czbrd.EvidenceMereniPriloha";
    public static final String EXEMPLAR_CLASSNAME = "cz.incad.czbrd.Exemplar";
    public static final String F_zasah_STR = "zasah";
    private Object zasah;
    private final List<String> lFieldsToIgnore_EvidenceMereni = new LinkedList<String>();
    private final List<String> lFieldsToIgnore_EvidenceMereniPriloha = new LinkedList<String>();

    /**
     ***************************************************************************
     * Vnořená třída.
     * Lze do ní uložit informace o recordu
     */
    class OneRecordInfo implements Serializable {

        private final String classname_evidenceMereni = Commons.getClassNameWithoutEntity(EvidenceMereniEntity.class.getName());
        private final Object recID;
        private final Object recCreUser;
        private final Object recCreDate;
        private final Object recEdiUser;
        private final Object recEdiDate;

        //Evidence měření
        private Object m_cOrganization;

        /**
         ***********************************************************************
         * Konstruktor
         */
        OneRecordInfo(Record rec) {
            this.recID = rec.getSimpleField(Record_A.F_recID_STR).getValue();
            this.recCreUser = rec.getSimpleField(Record_A.F_recCreUser_STR).getValue();
            this.recCreDate = rec.getSimpleField(Record_A.F_recCreDate_DAT).getValue();
            this.recEdiUser = rec.getSimpleField(Record_A.F_recEdiUser_STR).getValue();
            this.recEdiDate = rec.getSimpleField(Record_A.F_recEdiDate_DAT).getValue();

            //pole ze záznamu evidence měření
            if (this.classname_evidenceMereni.equals(rec.getDomain().getQName())) {
                this.m_cOrganization = rec.getSimpleField(EvidenceMereniEntity.F_cOrganization_STR).getValue();
            }
        }

        /**
         ***********************************************************************
         * Metoda nastaví Záznamu z parametru nové hodnoty
         *
         * @param rec
         */
        public void setRecord(Record rec) {
            rec.getSimpleField(Record_A.F_recID_STR).setValue(this.recID);
            rec.getSimpleField(Record_A.F_recCreUser_STR).setValue(this.recCreUser);
            rec.getSimpleField(Record_A.F_recCreDate_DAT).setValue(this.recCreDate);
            rec.getSimpleField(Record_A.F_recEdiUser_STR).setValue(this.recEdiUser);
            rec.getSimpleField(Record_A.F_recEdiDate_DAT).setValue(this.recEdiDate);

            //pole ze záznamu evidence měření
            if (this.classname_evidenceMereni.equals(rec.getDomain().getQName())) {
                rec.getSimpleField(EvidenceMereniEntity.F_cOrganization_STR).setValue(this.m_cOrganization);
            }
        }
    }

    /**
     ***************************************************************************
     *
     * @return @throws ValidationException
     * @throws WizardException
     */
    public WizardMessage startWizard() throws ValidationException, WizardException {
        Record rec;
        ReliefUser ru;
        String organizationUser;
        String organizationRecord;

        //Kontroly
        if (getWCC().getSelectedRecords().getRecordsCount() != 1) {
            throw new WizardException("Pro tuto funkci musí být vybrán právě jeden záznam.");
        }
        rec = getWCC().getSelectedRecords().nextRecord();
        ru = new ReliefUser(getWCC());

        organizationUser = ru.getOrganization();
        organizationRecord = (String) rec.getSimpleField(ExemplarEntity.F_cOrganization_STR).getValue();

        if (organizationUser == null) {
            throw new WizardException("Váš uživatelský účet nemá nemá přiřazenu organizaci, kontaktujte vašeho administrátora.");
        }

        if (organizationRecord == null) {
            throw new WizardException("Vybraný záznam nemá přiřazenu organizaci, kontaktujte vašeho administrátora.");
        }

        if (!(ru.isExplorer() || ru.isSystemAdmin() || ru.isEditorPh())) {
            throw new WizardException("Pro tuto akci nemáte dostatečná oprávnění.");
        }

        if (!organizationUser.equalsIgnoreCase(organizationRecord)) {
            if(!ru.isEditorPh()) {
                throw new WizardException("Vybraný záznam patří jiné organizaci, nemáte oprávnění jej editovat.");
            }
        }

        //Naplníme pole názvy sloupců které se mají při čištění záznamu ignorovat
        //Evidence měření
        lFieldsToIgnore_EvidenceMereni.add(EvidenceMereniEntity.F_cOrganization_STR);
        lFieldsToIgnore_EvidenceMereni.add(EvidenceMereniEntity.F_obalkaZpusob_STR);
        lFieldsToIgnore_EvidenceMereni.add(EvidenceMereniEntity.F_obalkaKde_STR);
        lFieldsToIgnore_EvidenceMereni.add(EvidenceMereniEntity.F_kBlokZpusob_STR);
        lFieldsToIgnore_EvidenceMereni.add(EvidenceMereniEntity.F_kBlokKde_STR);
        lFieldsToIgnore_EvidenceMereni.add(EvidenceMereniEntity.F_kBlokStrana_STR);
        lFieldsToIgnore_EvidenceMereni.add(EvidenceMereniEntity.F_barevnostKde_STR);
        lFieldsToIgnore_EvidenceMereni.add(EvidenceMereniEntity.F_barevnostStr_STR);
        lFieldsToIgnore_EvidenceMereni.add(EvidenceMereniEntity.F_obalKde_STR);
        lFieldsToIgnore_EvidenceMereni.add(EvidenceMereniEntity.F_obalZpusob_STR);
        //Evidence měření přílohy
        lFieldsToIgnore_EvidenceMereniPriloha.add(EvidenceMereniPrilohaEntity.F_barevnostKde_STR);
        lFieldsToIgnore_EvidenceMereniPriloha.add(EvidenceMereniPrilohaEntity.F_merKde_STR);
        lFieldsToIgnore_EvidenceMereniPriloha.add(EvidenceMereniPrilohaEntity.F_merStrana_STR);
        lFieldsToIgnore_EvidenceMereniPriloha.add(EvidenceMereniPrilohaEntity.F_merZpusob_STR);

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
        if ("panel01".equals(panelName)) {
            ValidationException vex;
            zasah = getWCC().getWizardRecord().getSimpleField(F_zasah_STR).getValue();
            if (zasah == null) {
                vex = Exceptions.getValidationException(getWCC());
                vex.addField("Druh zásahu", "Pole nesmí být prázdné.", false);
                throw vex;
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
        int countEvidenceMereni,
                countPriloha;
        String idCislo_EvidenceMereni;
        Record recExemplar,
                recEvidenceMereni,
                recEvidenceMereniPriloha,
                recPriloha,
                recEMPriloha_evidenceMereni,
                recEMPriloha_exemplar;
        RecordsIterator ritPriloha_exemplar,
                ritEMPriloha_evidenceMereni;
        OneRecordInfo recInfo_recEvidenceMereni,
                recInfo_recEvidenceMereniPriloha;

        LOG.log(Level.WARNING, "Zacinam pracovat");
        //Načteme záznam Exempláře se kterým budeme pracovat
        recExemplar = getWCC().getSelectedRecords().nextRecord();
        LOG.log(Level.WARNING, "Nastavuju anotaci exemplare");
        recExemplar.setAnnotation(Commons.ANNOTATION_VALIDATION_EXCEPTION_IGNORE_WARNING, Boolean.TRUE);
        //založíme novej záznam evidence měření v historii měření.
        getWCC().addRootDomain(cz.incad.relief3.core.tools.Commons.getDomain(EXEMPLAR_CLASSNAME, getWCC()));
        try {
            recEvidenceMereni = getWCC().create(cz.incad.relief3.core.tools.Commons.getDomain(EVIDENCE_MERENI_CLASSNAME, getWCC()));
            // anotace aby neprobihala kontrola
            LOG.log(Level.WARNING, "Nastavuju anotaci evidence");
            recEvidenceMereni.setAnnotation(Commons.ANNOTATION_VALIDATION_EXCEPTION_IGNORE_WARNING, Boolean.TRUE);
            // konec anotace na kontrolu
            LOG.log(Level.WARNING, "zaklada se zaznam do mereni tabulkou");
            recExemplar.getTableField(ExemplarEntity.F_tEvidenceMereni_TAB).addKey(recEvidenceMereni.getKey());
            idCislo_EvidenceMereni = (String) recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_recID_STR).getValue();
        } catch (AddException ex) {
            Logger.getLogger(ExemplarEvidujZasah.class.getName()).log(Level.SEVERE, null, ex);
            throw new WizardException("V programu nastala neočeávaná chyba, kontaktujte tvůrce aplikace.");
        }

        //Zpracujeme přílohy - založíme strukturu
        ritPriloha_exemplar = recExemplar.getTableField(ExemplarEntity.F_tPriloha_TAB).getTableRecords();
        countPriloha = ritPriloha_exemplar.getRecordsCount();
        for (int i = 0; i < countPriloha; i++) {
            try {
                //založíme novej záznam evidence měření v evidenci měření z historie měření.
                recEvidenceMereniPriloha = getWCC().create(cz.incad.relief3.core.tools.Commons.getDomain(EVIDENCE_MERENI_PRILOHA_CLASSNAME, getWCC()));
                recEvidenceMereni.getTableField(EvidenceMereniEntity.F_tEMPriloha_TAB).addKey(recEvidenceMereniPriloha.getKey());

            } catch (AddException ex) {
                Logger.getLogger(ExemplarEvidujZasah.class.getName()).log(Level.SEVERE, null, ex);
                throw new WizardException("V programu nastala neočeávaná chyba, kontaktujte tvůrce aplikace.");
            }
        }
        //Uložíme nově založené záznay evidence měření příloha a příloha evidence měření příloha
        LOG.log(Level.WARNING, "Commit");
        getWCC().commit();
        LOG.log(Level.WARNING, "po commitu");

        //Provedeme migraci dat evidence měření pro exemplář
        getWCC().addRootDomain(cz.incad.relief3.core.tools.Commons.getDomain(EVIDENCE_MERENI_CLASSNAME, getWCC()));
        //Načteme záznam Exempláře se kterým budeme pracovat - po commitu musíme znovu
        recExemplar = getWCC().getSelectedRecords().nextRecord();
        RecordsIterator ritEvidenceMereni;

        // nastaveni anotaci - vlozeno
        ritEvidenceMereni = recExemplar.getTableField(ExemplarEntity.F_tEvidenceMereni_TAB).getTableRecords();
        countEvidenceMereni = ritEvidenceMereni.getRecordsCount();
        for (int i = 0; i < countEvidenceMereni; i++) {
            recEvidenceMereni = ritEvidenceMereni.nextRecord();
            LOG.log(Level.WARNING, "Prochazi se jednotlive evidence a nastavuje anotace");
            recEvidenceMereni.setAnnotation(Commons.ANNOTATION_VALIDATION_EXCEPTION_IGNORE_WARNING, Boolean.TRUE);
        }
        // konec nastaveni anotaci - vlozeno

        ritEvidenceMereni = recExemplar.getTableField(ExemplarEntity.F_tEvidenceMereni_TAB).getTableRecords();
        countEvidenceMereni = ritEvidenceMereni.getRecordsCount();
        for (int i = 0; i < countEvidenceMereni; i++) {
            recEvidenceMereni = ritEvidenceMereni.nextRecord();
            if (idCislo_EvidenceMereni.equals(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_recID_STR).getValue())) {
                break;
            }
        }

        //Uložíme si Record info o záznamu kompozitního pole z exempláře
        recInfo_recEvidenceMereni = new OneRecordInfo(recEvidenceMereni);
       LOG.log(Level.WARNING, "Migrace");
        migrate(recExemplar.getCompositeField(ExemplarEntity.F_cEvidenceMereni_COM).getCompositeRecord(), recEvidenceMereni);
        //Vyčistíme data ze záznamu v komponentě
       LOG.log(Level.WARNING, "Cisteni");
        clear(recExemplar.getCompositeField(ExemplarEntity.F_cEvidenceMereni_COM).getCompositeRecord());
        //Nastavíme RECORD hodnoty vyčištěnému záznamu v komponentě
        recInfo_recEvidenceMereni.setRecord(recExemplar.getCompositeField(ExemplarEntity.F_cEvidenceMereni_COM).getCompositeRecord());
        //Nastavíme hodnotu typu zásahu dle vybrané hodnoty na začátku funkce do komponenty.
        recExemplar.setAnnotation(Commons.ANNOTATION_VALIDATION_EXCEPTION_IGNORE_WARNING, Boolean.TRUE);
        Record recEvidenceMereniPomocne = recExemplar.getCompositeField(ExemplarEntity.F_cEvidenceMereni_COM).getCompositeRecord();
        recEvidenceMereniPomocne.getSimpleField(EvidenceMereniEntity.F_druhZasahu_STR).setValue(this.zasah);
        recEvidenceMereniPomocne.setAnnotation(Commons.ANNOTATION_VALIDATION_EXCEPTION_IGNORE_WARNING, Boolean.TRUE);
       
        ritEMPriloha_evidenceMereni = recEvidenceMereni.getTableField(EvidenceMereniEntity.F_tEMPriloha_TAB).getTableRecords();
        ritPriloha_exemplar = recExemplar.getTableField(ExemplarEntity.F_tPriloha_TAB).getTableRecords();
        countPriloha = ritPriloha_exemplar.getRecordsCount();

        for (int i = 0; i < countPriloha; i++) {
            recPriloha = ritPriloha_exemplar.nextRecord();
            recEMPriloha_evidenceMereni = ritEMPriloha_evidenceMereni.nextRecord();
            recEMPriloha_exemplar = recPriloha.getCompositeField(PrilohaEntity.F_cEvidenceMereniPriloha_COM).getCompositeRecord();
            recInfo_recEvidenceMereniPriloha = new OneRecordInfo(recEMPriloha_evidenceMereni);
            migrate(recEMPriloha_exemplar, recEMPriloha_evidenceMereni);
            //propojíme EvidenciMěřeníPřílohy s Přílohou
            recEMPriloha_evidenceMereni.getReferencedField(EvidenceMereniPrilohaEntity.F_rPriloha_REF).setKey(recPriloha.getKey());
            clear(recEMPriloha_exemplar);
            recInfo_recEvidenceMereniPriloha.setRecord(recPriloha.getCompositeField(PrilohaEntity.F_cEvidenceMereniPriloha_COM).getCompositeRecord());
        }
        LOG.log(Level.WARNING, "Druhy commit");
        getWCC().commit();
        return null;
    }

    /**
     ***************************************************************************
     * Přesune všechna pole typu SimpleField a klíče v polích typu TableField ze
     * záznamu Source do Target
     *
     * @param source
     * @param target
     */
    private void migrate(Record source, Record target) {
        Field[] pole = source.getFields();
        RecordsIterator rit;
        int countRit;
        Record rec;

        for (int i = 0; i < pole.length; i++) {
            if (pole[i].getType() == Field.SIMPLE) {
                target.getSimpleField(pole[i].getLocalName()).setValue(pole[i].getValue());
            } else if (pole[i].getType() == Field.TABLE) {
                rit = source.getTableField(pole[i].getLocalName()).getTableRecords();
                countRit = rit.getRecordsCount();
                for (int j = 0; j < countRit; j++) {
                    rec = rit.nextRecord();
                    target.getTableField(pole[i].getLocalName()).addKey(rec.getKey());
                }
            }
        }
    }

    /**
     ***************************************************************************
     * Všechna pole typu SimpleField nastaví na NULL
     *
     * @param rec
     */
    private void clear(Record rec) {
        String classname_evidenceMereni = Commons.getClassNameWithoutEntity(EvidenceMereniEntity.class.getName());
        String classname_evidenceMereniPriloha = Commons.getClassNameWithoutEntity(EvidenceMereniPrilohaEntity.class.getName());
        Field[] arrAllRecordFields = rec.getFields();
        String recordDomainName = rec.getDomain().getQName();
        String fieldName;

        for (int i = 0; i < arrAllRecordFields.length; i++) {
            if (arrAllRecordFields[i].getType() == Field.SIMPLE) {
                fieldName = arrAllRecordFields[i].getLocalName();
                if ((classname_evidenceMereni.equals(recordDomainName) && lFieldsToIgnore_EvidenceMereni.contains(fieldName)) || (classname_evidenceMereniPriloha.equals(recordDomainName) && lFieldsToIgnore_EvidenceMereniPriloha.contains(fieldName))) {
                    continue;
                }

                rec.getSimpleField(arrAllRecordFields[i].getLocalName()).setValue(null);
            }
        }
    }

}
