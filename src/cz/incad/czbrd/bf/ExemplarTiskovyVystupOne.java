package cz.incad.czbrd.bf;

import com.amaio.plaant.businessFunctions.*;
import com.amaio.plaant.sync.Record;
import cz.incad.czbrd.EvidenceMereniEntity;
import cz.incad.czbrd.EvidenceMereniPrilohaEntity;
import cz.incad.czbrd.ExemplarEntity;
import cz.incad.czbrd.PrilohaEntity;
import cz.incad.relief3.core.BussinesFunction_A;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 *******************************************************************************
 * Bussines funkce generující tiskový výstup v HTML
 *
 * @author adam.foglar@incad.cz - korektor martin.novacek@incad.cz
 */
public class ExemplarTiskovyVystupOne extends BussinesFunction_A implements Serializable {

    /**
     ***************************************************************************
     * Metoda otevře nové okno prohlížeče a do něj vygeneruje tiskový výstup v
     * HTML. Původní okno se po vytisknutí samo zavře.
     *
     * @return
     * @throws ValidationException
     * @throws WizardException
     */
    public WizardMessage startWizard() throws ValidationException, WizardException {
        WizardMessage wm = new WizardMessage();
        int selectedRecordCount;
        Record recExemplar;
        RecordsIterator ritExemplar;
        Record recEvidenceMereni;
        Record recPriloha;
        Record recPrilohaEvidenceMereni;

        String poskozeniPrilohy_bilogockePoskozeni;
        String poskozeniPrilohy_mechanickePoskozeni;
        String poskozeniPrilohy_chemickePoskozeni;
        String merStrana;
        String typPrilohy;
        String typPapiru;
        String typTisku;
        String barvaTisku;
        String nazevPrilohy;
        String merKde;
        String s_merPH;
        String barevnostL;
        String barevnostA;
        String barevnostE;
        String barevnostB;
        String s_hmotnost;
        String s_merNote;
        String s_merPoznamka;
        String s_p_Poznamka;
        String s_PPapPoznamka;
        String s_mechPosPoznamka;
        String celkovePoskozeni;
        String s_prilohaCount;
        String s_kBlokPH;
        String s_obalkaPH;
        String s_obalPH;
        String s_obalkaZpusob;
        String s_kBlokZpusob;
        String s_merZpusob;
        String s_posDesky;
        String s_posHrbetnik;
        String s_posKapitalek;
        String s_posZalStuzka;
        String s_posPredsadka;
        String s_posVazba;
        String s_posObalka;
        String se_typTisku;
        String se_zbarveniPapiru;
        String se_typPapiru;
        String neuplnostExemplare;
        String material;
        String ochrannyObal;
        String druhVazby;
        String typVazby;
        String typFondu;
        String s_se_pismo;

        //otestovani, jestli je vybran prave jeden zaznam
        selectedRecordCount = getWCC().getSelectedRecords().getRecordsCount();
        if (selectedRecordCount != 1) {
            throw new WizardException("Pro tuto funkci musí být vybrán právě jeden záznam.");
        }

        ritExemplar = getWCC().getSelectedRecords();
        recExemplar = ritExemplar.nextRecord();

        RecordsIterator ritHistorieMereni = recExemplar.getTableField(ExemplarEntity.F_tEvidenceMereni_TAB).getTableRecords();
        RecordsIterator ritPriloha = recExemplar.getTableField(ExemplarEntity.F_tPriloha_TAB).getTableRecords();
        recEvidenceMereni = recExemplar.getCompositeField(EvidenceMereniEntity.F_cEvidenceMereni_STR).getCompositeRecord();

        s_kBlokPH = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_kBlokPH_NUM).getValue());
        s_obalkaPH = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_obalkaPH_NUM).getValue());
        s_obalPH = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_obalPH_NUM).getValue());
        s_obalkaZpusob = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_obalkaZpusob_STR).getValue());
        s_kBlokZpusob = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_kBlokZpusob_STR).getValue());
        s_posDesky = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_posDesky_STR).getValue());
        s_posHrbetnik = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_posHrbetnik_STR).getValue());
        s_posKapitalek = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_posKapitalek_STR).getValue());
        s_posZalStuzka = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_posZalStuzka_STR).getValue());
        s_posPredsadka = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_posPredsadka_STR).getValue());
        s_posVazba = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_posVazba_STR).getValue());
        s_posObalka = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_posObalka_STR).getValue());
        typFondu = getStringFromValue(recExemplar.getSimpleField(ExemplarEntity.F_fyzTypFondu_STR).getValue());
        typVazby = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_fyzTypVazby_STR).getValue());
        druhVazby = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_fyzDruhVazby_STR).getValue());
        ochrannyObal = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_fyzOchrannyObal_STR).getValue());
        material = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_fyzMaterial_STR).getValue());
        neuplnostExemplare = getStringFromValue(recExemplar.getSimpleField(ExemplarEntity.F_fyzNeupExem_STR).getValue());
        se_typPapiru = getStringFromValue(recExemplar.getSimpleField(ExemplarEntity.F_fyzTypPapiru_STR).getValue());
        se_zbarveniPapiru = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_fyzZabarveniPapiru_STR).getValue());
        s_se_pismo = getStringFromValue(recExemplar.getSimpleField(ExemplarEntity.F_fyzPismo_STR).getValue());
        se_typTisku = getStringFromValue(recExemplar.getSimpleField(ExemplarEntity.F_fyzTypTisku_STR).getValue());

        if (ritPriloha.getRecordsCount() == 0) {
            //Záznam bez přílohy
            s_prilohaCount = "0 příloh";
            typPrilohy = "";
            typPapiru = "";
            typTisku = "";
            barvaTisku = "";
            nazevPrilohy = "";
            poskozeniPrilohy_bilogockePoskozeni = "";
            poskozeniPrilohy_mechanickePoskozeni = "";
            poskozeniPrilohy_chemickePoskozeni = "";

            merStrana = "";
            merKde = "";
            s_merPH = "";
            s_merZpusob = "";

            barevnostL = "";
            barevnostA = "";
            barevnostE = "";
            barevnostB = "";
            s_hmotnost = "";
            s_merNote = "";
        } else {
            //Začátek přílohy
            s_prilohaCount = "1. ze " + ritPriloha.getRecordsCount();
            recPriloha = ritPriloha.nextRecord();
            recPrilohaEvidenceMereni = recPriloha.getCompositeField(PrilohaEntity.F_cEvidenceMereniPriloha_COM).getCompositeRecord();

            typPrilohy = getStringFromValue(recPriloha.getSimpleField(PrilohaEntity.F_typPrilohy_STR).getValue());
            typPapiru = getStringFromValue(recPriloha.getSimpleField(PrilohaEntity.F_typPapiru_STR).getValue());
            typTisku = getStringFromValue(recPriloha.getSimpleField(PrilohaEntity.F_typTisku_STR).getValue());
            barvaTisku = getStringFromValue(recPriloha.getSimpleField(PrilohaEntity.F_barvaTisku_STR).getValue());
            nazevPrilohy = getStringFromValue(recPriloha.getSimpleField(PrilohaEntity.F_nazevPrilohy_STR).getValue());
            poskozeniPrilohy_bilogockePoskozeni = getPoskozeniList(recPrilohaEvidenceMereni, new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pBioHlodavci_BOO, "hlo."), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pBioHmyz_BOO, "hm."), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pBioPlisBak_BOO, "pl./bak."), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pBioNeni_BOO, "Není"));
            //poskozeniPrilohy_mechanickePoskozeni = getPoskozeniList(recPrilohaEvidenceMereni, new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pMechChybCast_BOO, "ch.část"), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pMechCinHlodHmyz_BOO, "hlo./hm."), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pMechKrehkost_BOO, "křeh."), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pMechNeni_BOO, "Není"), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pMechLepPas_BOO, "lep.pás."), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pMechPrehPrek_BOO, "přeh./překl."));
            poskozeniPrilohy_mechanickePoskozeni = getPoskozeniList(recPrilohaEvidenceMereni, new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pMechChybCast_BOO, "ch.část"), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pMechTrhliny_BOO, "trhliny"), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pMechKrehkost_BOO, "křeh."), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pMechNeni_BOO, "Není"), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pMechLepPas_BOO, "lep.pás."), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pMechPrehPrek_BOO, "přeh./překl."));
            poskozeniPrilohy_chemickePoskozeni = getPoskozeniList(recPrilohaEvidenceMereni, new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pChemJinTek_BOO, "j. tek."), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pChemMast_BOO, "mast."), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pChemNeni_BOO, "Není"), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pChemPrach_BOO, "prach"), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pChemBarSkv_BOO, "bar.skvr."), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pChemTepPos_BOO, "tep.poš."), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pChemVoda_BOO, "voda"), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pChemZdeg_BOO, "zdeg."), new OnePoskozeni(EvidenceMereniPrilohaEntity.F_pChemNecTex_BOO, "nečit.text"));

            merStrana = getStringFromValue(recPriloha.getSimpleField(PrilohaEntity.F_strana_STR).getValue());
            merKde = getStringFromValue(recPrilohaEvidenceMereni.getSimpleField(EvidenceMereniPrilohaEntity.F_merKde_STR).getValue());
            s_merPH = getStringFromValue(recPrilohaEvidenceMereni.getSimpleField(EvidenceMereniPrilohaEntity.F_merPH_NUM).getValue());
            s_merZpusob = getStringFromValue(recPrilohaEvidenceMereni.getSimpleField(EvidenceMereniPrilohaEntity.F_merZpusob_STR).getValue());

            barevnostL = getStringFromValue(recPrilohaEvidenceMereni.getSimpleField(EvidenceMereniPrilohaEntity.F_barevnostL_STR).getValue());
            barevnostA = getStringFromValue(recPrilohaEvidenceMereni.getSimpleField(EvidenceMereniPrilohaEntity.F_barevnostA_STR).getValue());
            barevnostE = getStringFromValue(recPrilohaEvidenceMereni.getSimpleField(EvidenceMereniPrilohaEntity.F_barevnostE_STR).getValue());
            barevnostB = getStringFromValue(recPrilohaEvidenceMereni.getSimpleField(EvidenceMereniPrilohaEntity.F_barevnostB_STR).getValue());
            s_hmotnost = getStringFromValue(recPrilohaEvidenceMereni.getSimpleField(EvidenceMereniPrilohaEntity.F_hmotnost_NUM).getValue());
            s_merNote = (recPrilohaEvidenceMereni.getSimpleField(EvidenceMereniPrilohaEntity.F_note_STR).getValue() != null) ? "Ano" : "Ne";
            //konec přílohy
        }

        s_mechPosPoznamka = (recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_mechPosPoznamka_STR).getValue() != null) ? "Ano" : "Ne";
        s_PPapPoznamka = (recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_pPapPoznamka_STR).getValue() != null) ? "Ano" : "Ne";
        s_p_Poznamka = (recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_pPoznamka_STR).getValue() != null) ? "Ano" : "Ne";
        s_merPoznamka = (recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_merPoznamky_STR).getValue() != null) ? "Ano" : "Ne";

        String poskozeniDokumentu_bilogockePoskozeni = getPoskozeniList(recEvidenceMereni, new OnePoskozeni(EvidenceMereniEntity.F_pBioHlodavci_BOO, "hlo."), new OnePoskozeni(EvidenceMereniEntity.F_pBioHmyz_BOO, "hm."), new OnePoskozeni(EvidenceMereniEntity.F_pBioNeni_BOO, "Není"), new OnePoskozeni(EvidenceMereniEntity.F_pBioPlisBak_BOO, "pl./bak."), new OnePoskozeni(EvidenceMereniEntity.F_pBioZvirExk_BOO, "exkr."));
        //String poskozeniDokumentu_mechanickePoskozeni = getPoskozeniList(recEvidenceMereni, new OnePoskozeni(EvidenceMereniEntity.F_pMechChybCast_BOO, "ch.část"), new OnePoskozeni(EvidenceMereniEntity.F_pMechCinHlodHmyz_BOO, "hlo./hm."), new OnePoskozeni(EvidenceMereniEntity.F_pMechKreh_BOO, "křeh."), new OnePoskozeni(EvidenceMereniEntity.F_pMechNeni_BOO, "Není"), new OnePoskozeni(EvidenceMereniEntity.F_pMechPrehPrek_BOO, "přeh./překl."), new OnePoskozeni(EvidenceMereniEntity.F_pMechSlepList_BOO, "slep.l."), new OnePoskozeni(EvidenceMereniEntity.F_pMechLepPas_BOO, "lep.pás."));
        String poskozeniDokumentu_mechanickePoskozeni = getPoskozeniList(recEvidenceMereni, new OnePoskozeni(EvidenceMereniEntity.F_pMechChybCast_BOO, "ch.část"), new OnePoskozeni(EvidenceMereniEntity.F_pMechKreh_BOO, "křeh."), new OnePoskozeni(EvidenceMereniEntity.F_pMechNeni_BOO, "Není"), new OnePoskozeni(EvidenceMereniEntity.F_pMechPrehPrek_BOO, "přeh./překl."), new OnePoskozeni(EvidenceMereniEntity.F_pMechSlepList_BOO, "slep.l."), new OnePoskozeni(EvidenceMereniEntity.F_pMechLepPas_BOO, "lep.pás."));
        String poskozeniDokumentu_chemickePoskozeni = getPoskozeniList(recEvidenceMereni, new OnePoskozeni(EvidenceMereniEntity.F_pChemJinTek_BOO, "j. tek."), new OnePoskozeni(EvidenceMereniEntity.F_pChemMast_BOO, "mast."), new OnePoskozeni(EvidenceMereniEntity.F_pChemNeni_BOO, "Není"), new OnePoskozeni(EvidenceMereniEntity.F_pChemPrach_BOO, "prach"), new OnePoskozeni(EvidenceMereniEntity.F_pChemBarSkvr_BOO, "bar.skvr."), new OnePoskozeni(EvidenceMereniEntity.F_pChemTepPos_BOO, "tep.poš."), new OnePoskozeni(EvidenceMereniEntity.F_pChemVoda_BOO, "voda"));
        String poskozeniPapiru_chemickePoskozeni = getPoskozeniList(recEvidenceMereni, new OnePoskozeni(EvidenceMereniEntity.F_pPapChemJinTek_BOO, "j. tek."), new OnePoskozeni(EvidenceMereniEntity.F_pPapChemMast_BOO, "mast."), new OnePoskozeni(EvidenceMereniEntity.F_pPapChemNecTex_BOO, "nečit.text"), new OnePoskozeni(EvidenceMereniEntity.F_pPapChemPrach_BOO, "prach"), new OnePoskozeni(EvidenceMereniEntity.F_pPapChemBarSkv_BOO, "bar.skvr."), new OnePoskozeni(EvidenceMereniEntity.F_pPapChemTepPos_BOO, "tep.poš."), new OnePoskozeni(EvidenceMereniEntity.F_pPapChemVoda_BOO, "voda"), new OnePoskozeni(EvidenceMereniEntity.F_pPapChemZdeg_BOO, "zdeg."), new OnePoskozeni(EvidenceMereniEntity.F_pPapChemNeni_BOO, "Není"));
        //String poskozeniPapiru_mechanickePoskozeni = getPoskozeniList(recEvidenceMereni, new OnePoskozeni(EvidenceMereniEntity.F_pPapMechChybCast_BOO, "ch.část"), new OnePoskozeni(EvidenceMereniEntity.F_pMechCinHlodHmyz_BOO, "hlo./hm."), new OnePoskozeni(EvidenceMereniEntity.F_pMechKreh_BOO, "křeh."), new OnePoskozeni(EvidenceMereniEntity.F_pMechPrehPrek_BOO, "přeh./překl."), new OnePoskozeni(EvidenceMereniEntity.F_pMechSlepList_BOO, "slep.l."), new OnePoskozeni(EvidenceMereniEntity.F_pPapMechNeni_BOO, "Není"), new OnePoskozeni(EvidenceMereniEntity.F_pPapMechChybCelStr_BOO, "ch.str."));
        String poskozeniPapiru_mechanickePoskozeni = getPoskozeniList(recEvidenceMereni, new OnePoskozeni(EvidenceMereniEntity.F_pPapMechChybCast_BOO, "ch.část"), new OnePoskozeni(EvidenceMereniEntity.F_pPapMechKrehkost_BOO, "křeh."), new OnePoskozeni(EvidenceMereniEntity.F_pPapMechPrehPrek_BOO, "přeh./překl."), new OnePoskozeni(EvidenceMereniEntity.F_pPapMechSlepList_BOO, "slep.l."), new OnePoskozeni(EvidenceMereniEntity.F_pPapMechNeni_BOO, "Není"), new OnePoskozeni(EvidenceMereniEntity.F_pPapMechChybCelStr_BOO, "ch.str."));
        String poskozeniPapiru_bilogockePoskozeni = getPoskozeniList(recEvidenceMereni, new OnePoskozeni(EvidenceMereniEntity.F_pPapBioHlodavci_BOO, "hlo."), new OnePoskozeni(EvidenceMereniEntity.F_pPapBioHmyz_BOO, "hm."), new OnePoskozeni(EvidenceMereniEntity.F_pPapBioPlisBak_BOO, "pl./bak."), new OnePoskozeni(EvidenceMereniEntity.F_pPapBioNeni_STR, "Není"));
        String poskozeniObalky_bilogockePoskozeni = getPoskozeniList(recEvidenceMereni, new OnePoskozeni(EvidenceMereniEntity.F_pObaBioHlodavci_BOO, "hlo."), new OnePoskozeni(EvidenceMereniEntity.F_pObaBioHmyz_BOO, "hm."), new OnePoskozeni(EvidenceMereniEntity.F_pObaBioPlisBak_BOO, "pl./bak."), new OnePoskozeni(EvidenceMereniEntity.F_pObaBioNeni_BOO, "Není"));
        String poskozeniObalky_mechanickePoskozeni = getPoskozeniList(recEvidenceMereni, new OnePoskozeni(EvidenceMereniEntity.F_pObaMechChybCast_BOO, "ch.část"), new OnePoskozeni(EvidenceMereniEntity.F_pObaMechTrhliny_BOO, "trhliny"), new OnePoskozeni(EvidenceMereniEntity.F_pObaMechKrehkost_BOO, "křeh."), new OnePoskozeni(EvidenceMereniEntity.F_pObaMechNeni_BOO, "Není"), new OnePoskozeni(EvidenceMereniEntity.F_pObaMechLepPas_BOO, "lep.pás."));
        String poskozeniObalky_chemickePoskozeni = getPoskozeniList(recEvidenceMereni, new OnePoskozeni(EvidenceMereniEntity.F_pObaChemJinTek_BOO, "j. tek."), new OnePoskozeni(EvidenceMereniEntity.F_pObaChemMast_BOO, "mast."), new OnePoskozeni(EvidenceMereniEntity.F_pObaChemNeni_BOO, "Není"), new OnePoskozeni(EvidenceMereniEntity.F_pObaChemPrach_BOO, "prach"), new OnePoskozeni(EvidenceMereniEntity.F_pObaChemBarSkv_BOO, "bar.skvr."), new OnePoskozeni(EvidenceMereniEntity.F_pObaChemTepPos_BOO, "tep.poš."), new OnePoskozeni(EvidenceMereniEntity.F_pObaChemVoda_BOO, "tep.poš."));

        String carKod = getStringFromValue(recExemplar.getSimpleField(ExemplarEntity.F_bibCarKod_STR).getValue());
        String signatura = getStringFromValue(recExemplar.getSimpleField(ExemplarEntity.F_bibSignatura_STR).getValue());
        String nazev = getStringFromValue(recExemplar.getSimpleField(ExemplarEntity.F_bibNazev_STR).getValue());
        String autor = getStringFromValue(recExemplar.getSimpleField(ExemplarEntity.F_bibAutor_STR).getValue());
        String rokVydani = getStringFromValue(recExemplar.getSimpleField(ExemplarEntity.F_bibRokVydani_STR).getValue());
        String ChybStrana_Val = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_pPapMechChybCelStrVal_STR).getValue());
        String kBlokKde = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_kBlokKde_STR).getValue());
        String kBlokStrana = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_kBlokStrana_STR).getValue());
        String obalkaKde = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_obalkaKde_STR).getValue());
        String recID = getStringFromValue(recExemplar.getSimpleField(EvidenceMereniEntity.F_recID_STR).getValue());
        String editoval = getStringFromValue(recExemplar.getSimpleField(EvidenceMereniEntity.F_recEdiUser_STR).getValue());
        String druhZasahu = getStringFromValue(recEvidenceMereni.getSimpleField(EvidenceMereniEntity.F_druhZasahu_STR).getValue());
        String s_editDate = getStringFromValue(recExemplar.getSimpleField(EvidenceMereniEntity.F_recEdiDate_DAT).getValue());

        if ("nadeskach".equals(s_posObalka)) {
            s_posObalka = "Na deskách";
        } else if ("vevazana".equals(s_posObalka)) {
            s_posObalka = "Vevázaná";
        } else if ("neni".equals(s_posObalka)) {
            s_posObalka = "Není";
        } else {
            s_posObalka = "";
        }

        if ("tuzka".equals(s_merZpusob)) {
            s_merZpusob = "Tužka";
        } else if ("elektroda".equals(s_merZpusob)) {
            s_merZpusob = "Elektroda";
        }

        if ("tuzka".equals(s_kBlokZpusob)) {
            s_kBlokZpusob = "Tužka";
        } else if ("elektroda".equals(s_kBlokZpusob)) {
            s_kBlokZpusob = "Elektroda";
        }

        if ("tuzka".equals(s_obalkaZpusob)) {
            s_obalkaZpusob = "Tužka";
        } else if ("elektroda".equals(s_obalkaZpusob)) {
            s_obalkaZpusob = "Elektroda";
        }

        if ((poskozeniDokumentu_bilogockePoskozeni.contains("Není") || poskozeniDokumentu_bilogockePoskozeni.length() == 0) && (poskozeniDokumentu_chemickePoskozeni.contains("Není") || poskozeniDokumentu_chemickePoskozeni.length() == 0) && (poskozeniDokumentu_mechanickePoskozeni.contains("Není") || poskozeniDokumentu_mechanickePoskozeni.length() == 0) && (poskozeniPapiru_chemickePoskozeni.contains("Není") || poskozeniPapiru_chemickePoskozeni.length() == 0) && (poskozeniPapiru_bilogockePoskozeni.contains("Není") || poskozeniPapiru_bilogockePoskozeni.length() == 0) && (poskozeniPapiru_mechanickePoskozeni.contains("Není") || poskozeniPapiru_mechanickePoskozeni.length() == 0) && (poskozeniObalky_bilogockePoskozeni.contains("Není") || poskozeniObalky_bilogockePoskozeni.length() == 0) && (poskozeniObalky_chemickePoskozeni.contains("Není") || poskozeniObalky_chemickePoskozeni.length() == 0) && (poskozeniObalky_mechanickePoskozeni.contains("Není") || poskozeniObalky_mechanickePoskozeni.length() == 0) && (poskozeniPrilohy_bilogockePoskozeni.contains("Není") || poskozeniPrilohy_bilogockePoskozeni.length() == 0) && (poskozeniPrilohy_chemickePoskozeni.contains("Není") || poskozeniPrilohy_chemickePoskozeni.length() == 0) && (poskozeniPrilohy_mechanickePoskozeni.contains("Není") || poskozeniPrilohy_mechanickePoskozeni.length() == 0)) {
            celkovePoskozeni = "Ne";
        } else {
            celkovePoskozeni = "Ano";
        }

        if (ritHistorieMereni.getRecordsCount() > 0) {
            wm.setHtml(true);
            wm.addLine("<html>"
                    + "<head>"
                    + "<title>Formulář - průzkum novodobých fondů</title>"
                    + "<SCRIPT LANGUAGE=\"JavaScript\">"
                    + "myRef = window.open('','mywin','left=20,top=20,width=500,height=500,toolbar=1,resizable=1');"
                    + "myRef.document.write('"
                    + "<tr><br><td><table class=\"inline\" >"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=center > <label for=\"DruhZasahu\"><b>Druh zásahu</b></label> </th> <td class=\"col1\" align=center> <label for=\"prilohaZaznamu\" ><b>Příloha záznamu</b></label> </td></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=center> <input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" value=\"" + druhZasahu + "\" readonly> </th> <td class=\"col1\" align=center> <input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" value=\"" + nazevPrilohy + "\" readonly> </td></tr>"
                    + "</td></tr></table>"
                    + "<tr><br><td><table class=\"inline\" >"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" colspan=4 align=left><b><u><i><span style=\"color: green;\">Mechanické poškození dokumentu</span></i></u></b></th> <th width=10></th> <th class=\"col0\" align=left colspan=2><b><u><i><span style=\"color: green;\">Poškození knižního bloku</span></i></u></b> <th width=10></th> <th class=\"col0\" size=60 colspan=4 align=left><b><u><i><span style=\"color: green;\">Poškození obálky</span></i></u></b></th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Desky: </b></th> <th class=\"col0\" align=left> <input type=\"text\" size=1 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_posDesky + "\" readonly></th>  <th class=\"col0\" align=left> <b>Kapitálek: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=1 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_posKapitalek + "\" readonly> </th> <th width=10></th><th class=\"col0\" align=left> <b>Předsádka: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=1 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_posPredsadka + "\" readonly> </th> <th width=10></th> <th class=\"col0\" align=left> <b>Obálka: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=10 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_posObalka + "\" readonly> </th><th class=\"col0\" align=left> <b>Mechanické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + poskozeniObalky_mechanickePoskozeni + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Hřbetník: </b></th> <th class=\"col0\" align=left> <input type=\"text\" size=1 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_posHrbetnik + "\" readonly></th>  <th class=\"col0\" align=left> <b>Zál. stužka: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=1 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_posZalStuzka + "\" readonly> </th> <th width=10></th><th class=\"col0\" align=left> <b>Vazba: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=1 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_posVazba + "\" readonly> </th> <th width=10></th> <th class=\"col0\" align=left> <b>Poznámka: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=10 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_mechPosPoznamka + "\" readonly> </th><th class=\"col0\" align=left> <b>Biologické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + poskozeniObalky_bilogockePoskozeni + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left></th> <th class=\"col0\" align=left></th> <th class=\"col0\" align=left></th> <th class=\"col0\" align=left></th> <th width=10></th> <th class=\"col0\" align=left></th><th class=\"col0\" align=left> </th> <th width=10></th> <th class=\"col0\" align=left> </th><th class=\"col0\" align=left> </th><th class=\"col0\" align=left> <b>Chemické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + poskozeniObalky_chemickePoskozeni + "\" readonly> </th></tr>"
                    + "</td></tr></table>"
                    + "<tr><br><td><table class=\"inline\" >"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left size=60 colspan=2> <label for=\"FondType\"><b><u><i><span style=\"color: green;\">Poškození papíru</span></i></u></b></label> </th> <th class=\"col0\"align=left size=60 colspan=2> <label for=\"FondType\"><b><u><i><span style=\"color: green;\">Poškození dokumentu</span><i></u></b></label> </th> <th class=\"col0\"align=left colspan = 2> <label for=\"FondType\"><b><u><i><span style=\"color: #663300;\">Měření obálka</span></i></u></b></label> </th> <th class=\"col0\" colspan=4 align=left> <label for=\"FondType\"><b><u><i><span style=\"color: #663300;\">Měření knižní blok</span></i></u></b></label> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Mechanické: </b> </th> <th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + poskozeniPapiru_mechanickePoskozeni + "\" readonly> </th> <th class=\"col0\" align=left> <b>Mechanické: </b></th><th class=\"col0\"  align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + poskozeniDokumentu_mechanickePoskozeni + "\" readonly> </th><th class=\"col0\" align=left> <b>Způsob: </b></th><th class=\"col0\" align=left> <input type=\"text\"size=10 STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + s_obalkaZpusob + "\" readonly> </th><th class=\"col0\" align=left> <b>Způsob: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=10 STYLE=\"color: #000000; background-color:  #C0C0C0;\" value=\"" + s_kBlokZpusob + "\" readonly> </th><th class=\"col0\" align=left> <b>Strana: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=5 STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + kBlokStrana + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Biologické: </b></th> <th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + poskozeniPapiru_bilogockePoskozeni + "\" readonly> </th><th class=\"col0\" align=left> <b>Biologické: </b></th><th class=\"col0\"  align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + poskozeniDokumentu_bilogockePoskozeni + "\" readonly> </th><th class=\"col0\" align=left> <b>Kde: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=10 STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + obalkaKde + "\" readonly> </th><th class=\"col0\" align=left> <b>Kde: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=10 STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + kBlokKde + "\" readonly> </th><th class=\"col0\" align=left> <b>pH: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=5 STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + s_kBlokPH + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Chemické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + poskozeniPapiru_chemickePoskozeni + "\" readonly> </th><th class=\"col0\" align=left> <b>Chemické: </b></th><th class=\"col0\"  align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + poskozeniDokumentu_chemickePoskozeni + "\" readonly> </th><th class=\"col0\" align=left> <b>pH: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=10 STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + s_obalkaPH + "\" readonly> </th></tr> "
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Poznámka: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60v STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + s_PPapPoznamka + "\" readonly> </th><th class=\"col0\" align=left> <b>Poznámka: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + s_p_Poznamka + "\" readonly> </th><th class=\"col0\" align=left></th> <th class=\"col0\" align=left></th> <th class=\"col0\" align=left> <b>Poznámka: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=10 STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + s_merPoznamka + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Chybějící strana: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + ChybStrana_Val + "\" readonly> </th></tr>"
                    + "</td></tr></table>"
                    + "<tr><br><td><table class=\"inline\" >"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left colspan=2> <label for=\"FondType\"><b><u><i><span style=\"color: #3300FF;\">Přílohy - (" + s_prilohaCount + ")</span></i></u></b></label> </th> <th class=\"col0\" size=60 align=left colspan=2> <label for=\"FondType\"><b><u><i><span style=\"color: #3300FF;\">Poškození</span></i></u></b></label> </th> <th class=\"col0\" colspan=4 align=left> <label for=\"FondType\"><b><u><i><span style=\"color: #3300FF;\">Měření příloha</span><i></u></b></label> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Typ přílohy: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=7 STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + getHumanName_Priloha(typPrilohy) + "\" readonly> </th><th class=\"col0\" align=left> <b>Mechanické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + poskozeniPrilohy_mechanickePoskozeni + "\" readonly> </th><th class=\"col0\" align=left> <b>Způsob: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=7 STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + s_merZpusob + "\" readonly> </th><th class=\"col0\" align=left> <b>Strana: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=5 STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + merStrana + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Typ papíru: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=7 STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + getHumanName_Priloha(typPapiru) + "\" readonly> </th><th class=\"col0\" align=left> <b>Biologické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + poskozeniPrilohy_bilogockePoskozeni + "\" readonly> </th><th class=\"col0\" align=left> <b>Kde: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=7 STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + merKde + "\" readonly> </th><th class=\"col0\" align=left> <b>pH: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=5 STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + s_merPH + "\"  readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Typ tisku: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=7 STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + getHumanName_Priloha(typTisku) + "\" readonly> </th><th class=\"col0\" align=left> <b>Chemické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + poskozeniPrilohy_chemickePoskozeni + "\" readonly> </th></tr> "
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Barva tisku: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=7e STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + getHumanName_Priloha(barvaTisku) + "\" readonly> </th> <th class=\"col0\" align=left></th> <th class=\"col0\" align=left></th> <th class=\"col0\" align=left> <b>Poznámka: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=7 STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + s_merNote + "\" readonly> </th><th class=\"col0\" align=left></th><th class=\"col0\" align=left> <b>Barevnost CIELAB </b></th><th class=\"col0\" align=right size=1> <b> L </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + barevnostL + "\" size=5 readonly> </th><th class=\"col0\" align=left size=5> <b> a </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + barevnostA + "\" size=5 readonly> </th><th class=\"col0\" align=left size=5> <b> b </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + barevnostB + "\" size=5 readonly> </th> <th class=\"col0\" align=left size=5> <b> E* </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + barevnostE + "\" size=5 readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left> <b>Hmotnost: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + s_hmotnost + "\" size=5 readonly> </th></tr>"
                    + "</td></tr></table>"
                    + "<tr><br><td><table class=\"inline\" >"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left colspan=3> <label for=\"FondType\"><b><u><i><span style=\"color: #FF9900;\">Identifikace záznamu</span></i></u></b></label> </th> <th class=\"col0\" align=left colspan=2> <label for=\"FondType\"><b><u><i><span style=\"color: #FF9900;\">Přiložené soubory</span></i></u></b></label> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Jméno: </b></th><th class=\"col0\" align=left> <b>Datum: </b></th><th class=\"col0\" align=left> <b>ID záznamu: </b></th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #FF9900;\" value=\"" + editoval + "\" readonly> </th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #FF9900;\" value=\"" + s_editDate + "\" readonly> </th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #FF9900;\" value=\"" + recID + "\" readonly> </th></tr>"
                    + "</td></tr></table>"
                    + "');"
                    //vytiskne stranku
                    + "myRef.print();"
                    + "window.close('5000')"
                    + "</SCRIPT>"
                    + "</head>"
                    + "<body>"
                    + "</body>"
                    + "</html>");
        } else {
            wm.setHtml(true);
            wm.addLine("<html>"
                    + "<head>"
                    + "<title>Formulář - Následný průzkum po změnách</title>"
                    + "<SCRIPT LANGUAGE=\"JavaScript\">"
                    + "myRef = window.open('','mywin','left=20,top=20,width=500,height=500,toolbar=1,resizable=1');"
                    + "myRef.document.write('"
                    + "<tr><br><td> <td><table class=\"inline\" >"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <label><b>Čárový kód</b></label> </th> <td class=\"col1\" align=left> <label><b>Příloha záznamu</b></label></td> <td class=\"col1\" align=left> <label><b>Signatura</b></label></td> <td class=\"col1\" align=left> <label><b>Název</b></label></td> <td class=\"col1\" align=left> <label><b>Autor</b></label></td> <td class=\"col1\" align=left> <label><b>Rok vydání</b></label></td></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" value=\"" + carKod + "\" readonly> </th> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" readonly></td> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" value=\"" + signatura + "\" readonly></td> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" value=\"" + nazev + "\" readonly></td> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" value=\"" + autor + "\" readonly></td> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" value=\"" + rokVydani + "\" readonly></td></tr>"
                    + "</td></tr></table>"
                    + "<tr><td> <td><table class=\"inline\">"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <label><b>Průzkum</b></label> </th> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" readonly> </th> <td class=\"col1\" align=left> <label><b>Údaje o zpracování</b></label></td> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" readonly> </th> <td class=\"col1\" align=left> <label><b>Poškození</b></label></td> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" value=\"" + celkovePoskozeni + "\" readonly> </th></tr>"
                    + "</td></tr></table>"
                    + "<tr><br><td> <td><table class=\"inline\" >"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <label><b>Typ fondu</b></label> </th> <td class=\"col1\" align=left> <label><b>Typ vazby</b></label></td> <td class=\"col1\" align=left> <label><b>Druh vazby</b></label></td> <td class=\"col1\" align=left> <label><b>Ochranný obal</b></label></td> <td class=\"col1\" align=left> <label><b>Materiál</b></label></td> <td class=\"col1\" align=left> <label><b>pH materiálu</b></label></td> <td class=\"col1\" align=left> <label><b>Neúplnost exempláře</b></label></td></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" value=\"" + getHumanName_Vanilla(typFondu) + "\" readonly> </th> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" value=\"" + getHumanName_Vanilla(typVazby) + "\" readonly></td> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" value=\"" + getHumanName_Vanilla(druhVazby) + "\" readonly></td> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" value=\"" + getHumanName_Vanilla(ochrannyObal) + "\" readonly></td> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" value=\"" + getHumanName_Vanilla(material) + "\" readonly></td> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + s_obalPH + "\" readonly></td> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #FF9999;\" value=\"" + neuplnostExemplare + "\" readonly></td></tr>"
                    + "</td></tr></table>"
                    + "<tr><br><td> <td><table class=\"inline\" >"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left colspan=4> <label><b><u><i><span style=\"color: #9933FF;\">Stav exempláře</span></i></u></b></label> </th> <th class=\"col0\" colspan=4 align=left><b><u><i><span style=\"color: green;\">Mechanické poškození dokumentu</span></i></u></b></span></th> <th class=\"col0\" align=left colspan=2><b><u><i><span style=\"color: green;\">Poškození knižního bloku</span></i></u></b></th> <th class=\"col0\" colspan=4 align=left size=60><b><u><i><span style=\"color: green;\">Poškození obálky</span></i></u></b></th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <label><b>Typ papíru</b></label></th> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #9999FF;\" value=\"" + getHumanName_Vanilla(se_typPapiru) + "\" readonly></td> <th class=\"col0\" align=left><label><b>Zbarvení papíru</b></label></th> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #9999FF;\" value=\"" + getHumanName_Vanilla(se_zbarveniPapiru) + "\" readonly></td> <th class=\"col0\" align=left> <b>Desky: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=1 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_posDesky + "\" readonly> </th><th class=\"col0\" align=left> <b>Kapitálek: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=1 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_posKapitalek + "\" readonly> </th><th class=\"col0\" align=left> <b>Předsádka: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=1 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_posPredsadka + "\" readonly> </th><th class=\"col0\" align=left> <b>Obálka: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_posObalka + "\" readonly> </th><th class=\"col0\" align=left> <b>Mechanické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + poskozeniObalky_mechanickePoskozeni + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left><label><b>Písmo</b></label></th> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #9999FF;\" value=\"" + s_se_pismo + "\" readonly></td> <th class=\"col0\" align=left><label><b>Typ tisku</b></label></th> <td class=\"col1\" align=left><input type=\"text\" STYLE=\"color: #000000; background-color: #9999FF;\" value=\"" + getHumanName_Vanilla(se_typTisku) + "\" readonly></td> <th class=\"col0\" align=left> <b>Hřbetník: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=1 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_posHrbetnik + "\" readonly> </th><th class=\"col0\" align=left> <b>Zál. stužka: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=1 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_posZalStuzka + "\" readonly> </th><th class=\"col0\" align=left> <b>Vazba: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=1 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_posVazba + "\" readonly> </th><th class=\"col0\" align=left> <b>Poznámka: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + s_mechPosPoznamka + "\" readonly> </th><th class=\"col0\" align=left> <b>Biologické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + poskozeniObalky_bilogockePoskozeni + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left> </th><th class=\"col0\" align=left></th><th class=\"col0\" align=left> </th><th class=\"col0\" align=left></th><th class=\"col0\" align=left> </th><th class=\"col0\" align=left> </th><th class=\"col0\" align=left> </th><th class=\"col0\" align=left> <b>Chemické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #99FF99;\" value=\"" + poskozeniObalky_chemickePoskozeni + "\" readonly> </th></tr>"
                    + "</td></tr></table>"
                    + "<tr><td><table class=\"inline\" >"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left colspan=2 size=60> <label><b><u><i><span style=\"color: green;\">Poškození papíru</span></i></u></b></label> </th> <th class=\"col0\"align=left colspan=2 size=60> <label><b><u><i><span style=\"color: green;\">Poškození dokumentu</span><i></u></b></label> </th> <th class=\"col0\"align=left colspan = 2> <label for=\"FondType\"><b><u><i><span style=\"color: green;\">Měření obálka</span></i></u></b></label> </th> <th class=\"col0\" colspan=4 align=left> <label for=\"FondType\"><b><u><i><span style=\"color: green;\">Měření knižní blok</span></i></u></b></label> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Mechanické: </b> </th> <th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + poskozeniPapiru_mechanickePoskozeni + "\" readonly> </th> <th class=\"col0\" align=left> <b>Mechanické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + poskozeniDokumentu_mechanickePoskozeni + "\" readonly> </th><th class=\"col0\" align=left> <b>Způsob: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" size=8 value=\"" + s_obalkaZpusob + "\" readonly> </th><th class=\"col0\" align=left> <b>Způsob: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" size=8 value=\"" + s_kBlokZpusob + "\" readonly> </th><th class=\"col0\" align=left> <b>Strana: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" size=8 value=\"" + kBlokStrana + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Biologické: </b></th> <th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + poskozeniPapiru_bilogockePoskozeni + "\" readonly> </th><th class=\"col0\" align=left> <b>Biologické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + poskozeniDokumentu_bilogockePoskozeni + "\" readonly> </th><th class=\"col0\" align=left> <b>Kde: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" size=8 value=\"" + obalkaKde + "\" readonly> </th><th class=\"col0\" align=left> <b>Kde: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" size=8 value=\"" + kBlokKde + "\" readonly> </th><th class=\"col0\" align=left> <b>pH: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" size=8 value=\"" + s_kBlokPH + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Chemické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + poskozeniPapiru_chemickePoskozeni + "\" readonly> </th><th class=\"col0\" align=left> <b>Chemické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + poskozeniDokumentu_chemickePoskozeni + "\" readonly> </th><th class=\"col0\" align=left> <b>pH: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" size=8 value=\"" + s_obalkaPH + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Poznámka: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + s_PPapPoznamka + "\" readonly> </th><th class=\"col0\" align=left> <b>Poznámka: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + s_p_Poznamka + "\" readonly> </th><th class=\"col0\" align=left></th> <th class=\"col0\" align=left></th> <th class=\"col0\" align=left> <b>Poznámka: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" size=8 value=\"" + s_merPoznamka + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Chybějící strana: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #66FF33;\" value=\"" + ChybStrana_Val + "\" readonly> </th></tr>"
                    + "</td></tr></table>"
                    + "<tr><br><td><table class=\"inline\">"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left colspan=2> <label for=\"FondType\"><b><u><i><span style=\"color: #3300FF;\">Přílohy - (" + s_prilohaCount + ")</span></i></u></b></label> </th> <th class=\"col0\" align=left colspan=2 size=60> <label for=\"FondType\"><b><u><i><span style=\"color: #3300FF;\">Poškození</span></i></u></b></label> </th> <th class=\"col0\" colspan=4 align=left> <label for=\"FondType\"><b><u><i><span style=\"color: #3300FF;\">Měření obálka</span><i></u></b></label> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Typ přílohy: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + getHumanName_Priloha(typPrilohy) + "\" readonly> </th><th class=\"col0\" align=left> <b>Mechanické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + poskozeniPrilohy_mechanickePoskozeni + "\" readonly> </th><th class=\"col0\" align=left> <b>Způsob: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" size=8 value=\"" + s_merZpusob + "\" readonly> </th><th class=\"col0\" align=left> <b>Strana: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + merStrana + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Typ papíru: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + getHumanName_Priloha(typPapiru) + "\" readonly> </th><th class=\"col0\" align=left> <b>Biologické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + poskozeniPrilohy_bilogockePoskozeni + "\" readonly> </th><th class=\"col0\" align=left> <b>Kde: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" size=8 value=\"" + merKde + "\" readonly> </th><th class=\"col0\" align=left> <b>pH: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + s_merPH + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Typ tisku: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + getHumanName_Priloha(typTisku) + "\" readonly> </th><th class=\"col0\" align=left> <b>Chemické: </b></th><th class=\"col0\" align=left> <input type=\"text\" size=60 STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + poskozeniPrilohy_chemickePoskozeni + "\" readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Barva tisku: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + getHumanName_Priloha(barvaTisku) + "\" readonly> </th> <th class=\"col0\" align=left></th> <th class=\"col0\" align=left></th> <th class=\"col0\" align=left> <b>Poznámka: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #33CCFF;\" value=\"" + s_merNote + "\" size=5 readonly> </th><th class=\"col0\" align=left></th><th class=\"col0\" align=left size=17> <b>Barevnnost CIELAB </b></th><th align=\"center\"> <b> L </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + barevnostL + "\" size=5 readonly> </th><th class=\"col0\" align=left size=5> <b> a </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + barevnostA + "\" size=5 readonly> </th><th class=\"col0\" align=left size=5> <b> b </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" size=5 value=\"" + barevnostB + "\" readonly> </th> <th class=\"col0\" align=left size=5> <b> E* </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" value=\"" + barevnostE + "\" size=5 readonly> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left></th><th class=\"col0\" align=left> <b>Hmotnost: </b></th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #C0C0C0;\" size=5 value=\"" + s_hmotnost + "\" readonly> </th></tr>"
                    + "</td></tr></table>"
                    + "<tr><br><td><table class=\"inline\">"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left colspan=3> <label for=\"FondType\"><b><u><i><span style=\"color: #FF9900;\">Identifikace záznamu</span></i></u></b></label> </th> <th class=\"col0\" align=left colspan=2> <label for=\"FondType\"><b><u><i><span style=\"color: #FF9900;\">Přiložené soubory</span></i></u></b></label> </th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <b>Jméno: </b></th><th class=\"col0\" align=left> <b>Datum: </b></th><th class=\"col0\" align=left> <b>ID záznamu: </b></th></tr>"
                    + "<tr class=\"row0\">"
                    + "<th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #FF9900;\" value=\"" + editoval + "\" readonly> </th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #FF9900;\" value=\"" + s_editDate + "\" readonly> </th><th class=\"col0\" align=left> <input type=\"text\" STYLE=\"color: #000000; background-color: #FF9900;\" value=\"" + recID + "\" readonly> </th></tr>"
                    + "</td></tr></table><br>"
                    + "');"
                    + "myRef.print();"
                    + "window.close('5000')"
                    + "</SCRIPT>"
                    + "</head>"
                    + "<body>"
                    + "</body>"
                    + "</html>");
            //+ ""
        }

        return wm;
    }

    /**
     ***************************************************************************
     * Metoda neni implementována
     *
     * @param panelName
     * @return
     * @throws ValidationException
     * @throws WizardException
     */
    public WizardMessage panelLeave(String panelName) throws ValidationException, WizardException {
        return null;
    }

    /**
     ***************************************************************************
     * Metoda neni implementována
     *
     * @return
     * @throws ValidationException
     * @throws WizardException
     * @throws ApplicationErrorException
     */
    public WizardMessage runBusinessMethod() throws ValidationException, WizardException, ApplicationErrorException {
        return null;
    }

    /**
     ***************************************************************************
     *
     * @param recEvidenceMereni
     * @param poskozeni
     * @return
     */
    private static String getPoskozeniList(Record recEvidenceMereni, OnePoskozeni... poskozeni) {
        StringBuilder sb = new StringBuilder(0);
        for (int i = 0; i < poskozeni.length; i++) {
            if ((Boolean) recEvidenceMereni.getSimpleField(poskozeni[i].field).getValue()) {
                if (sb.length() > 1) {
                    sb.append(", ");
                }
                sb.append(poskozeni[i].value);
            }
        }
        return sb.toString();
    }

    //Tohle by chtělo ještě předělat už jen pro to že jsou tam duplicitní klíče (zzzJiny) s odlišnými hodnotami, takže v některých případech funkce vrací špatnou hodnotu a je nepřehledné k čemu ty hodnoty vlastné patří
    /**
     ***************************************************************************
     *
     * @param value
     * @return
     */
    private String getHumanName_Priloha(String value) {
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("cd", "SLPrilohaTypPrilohy.cd");
        hm.put("gramofonovadeska", "SLPrilohaTypPrilohy.gramofonovadeska");
        hm.put("disketa", "SLPrilohaTypPrilohy.disketa");
        hm.put("mapa", "SLPrilohaTypPrilohy.mapa");
        hm.put("strih", "SLPrilohaTypPrilohy.strih");
        hm.put("rys", "SLPrilohaTypPrilohy.rys");
        hm.put("zzzJiny", "SLPrilohaTypPrilohy.jiny");
        hm.put("stejnyjakokb", "SLPrilohaTypPapiru.stejnyjakokb");
        hm.put("kridovy", "SLPrilohaTypPapiru.kridovy");
        hm.put("rucni", "SLPrilohaTypPapiru.rucni");
        hm.put("drevity", "SLPrilohaTypPapiru.drevity");
        hm.put("zzzjiny", "SLPrilohaTypPapiru.jiny");
        hm.put("grafika", "SLPrilohaTypTisku.grafika");
        hm.put("hlubotisk", "SLPrilohaTypTisku.hlubotisk");
        hm.put("plochytisk", "SLPrilohaTypTisku.plochytisk");
        hm.put("strih", "SLPrilohaTypTisku.strih");
        hm.put("mapa", "SLPrilohaTypTisku.mapa");
        hm.put("rys", "SLPrilohaTypTisku.rys");
        hm.put("zzzJiny", "SLPrilohaTypTisku.jiny");
        hm.put("cerna", "SLPrilohaBarvaTisku.cerna");
        hm.put("jednobarevna", "SLPrilohaBarvaTisku.jednobarevna");
        hm.put("zzzjina", "SLPrilohaBarvaTisku.jina");

        String exit = hm.get(value);
        if (exit == null) {
            return value;
        }
        return getWCC().getLocalizedString(exit);
    }

    //Tohle by chtělo ještě předělat už jen pro to že jsou tam duplicitní klíče (zzzJiny) s odlišnými hodnotami, takže v některých případech funkce vrací špatnou hodnotu a je nepřehledné k čemu ty hodnoty vlastné patří
    /**
     ***************************************************************************
     *
     * @param value
     * @return
     */
    private String getHumanName_Vanilla(String value) {
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("svetly", "SLZabarveniPapiru.svetly");
        hm.put("tmavy", "SLZabarveniPapiru.tmavy");
        hm.put("barevny", "SLZabarveniPapiru.barevny");
        hm.put("jiny", "SLZabarveniPapiru.jiny");
        hm.put("stejnyjakokb", "SLTypPapiru.stejnyjakokb");
        hm.put("kridovy", "SLTypPapiru.kridovy");
        hm.put("rucni", "SLTypPapiru.rucni");
        hm.put("drevity", "SLTypPapiru.drevity");
        hm.put("zzzjiny", "SLTypPapiru.jiny");
        hm.put("novinovy", "SLTypPapiru.novinovy");
        hm.put("grafika", "SLTypTisku.grafika");
        hm.put("hlubotisk", "SLTypTisku.hlubotisk");
        hm.put("plochytisk", "SLTypTisku.plochytisk");
        hm.put("strih", "SLTypTisku.strih");
        hm.put("mapa", "SLTypTisku.mapa");
        hm.put("rys", "SLTypTisku.rys");
        hm.put("zzzJiny", "SLTypTisku.jiny");
        hm.put("hudebniny", "Hudebniny");
        hm.put("periodikum", "Periodikum");
        hm.put("monografie", "Monografie");
        hm.put("pevna", "SLTypVazby.pevna");
        hm.put("polotuha", "SLTypVazby.polotuha");
        hm.put("brozovana", "SLTypVazby.brozovana");
        hm.put("brozura", "SLTypVazby.brozura");
        hm.put("deskyskapsou", "SLTypVazby.deskyskapsou");
        hm.put("celopapirova", "SLDruhVazby.celopapirova");
        hm.put("celoplatena", "SLDruhVazby.celoplatena");
        hm.put("celokozena", "SLDruhVazby.celokozena");
        hm.put("poloplatena", "SLDruhVazby.poloplatena");
        hm.put("polokozena", "SLDruhVazby.polokozena");
        hm.put("satenova", "SLDruhVazby.satenova");
        hm.put("zzzjiny", "SLDruhVazby.jiny");
        hm.put("krabice", "SLOchrannyObal.krabice");
        hm.put("desky", "SLOchrannyObal.desky");
        hm.put("neni", "SLOchrannyObal.neni");
        hm.put("tubus", "SLOchrannyObal.tubus");
        hm.put("obalka", "SLOchrannyObal.obalka");
        hm.put("archivnilepenka", "SLMaterial.archivnilepenka");
        hm.put("puvodni", "SLMaterial.puvodni");
        hm.put("nevhodne", "SLMaterial.nevhodne");
        hm.put("svetly", "SLZabarveniPapiru.svetly");

        String exit = hm.get(value);
        if (exit == null) {
            return value;
        }
        return getWCC().getLocalizedString(exit);
    }

    /**
     ***************************************************************************
     *
     * @param value
     * @return
     */
    private static String getStringFromValue(Object value) {
        if (value == null) {
            return "";
        }

        //String
        if (String.class.isInstance(value)) {
            if (((String) value).startsWith("x0")) {
                return ((String) value).substring(2);
            }
            if ("!!".equals((String) value)) {
                return "";
            }
            return (String) value;
            //BigBecimal
        } else if (BigDecimal.class.isInstance(value)) {
            return ((BigDecimal) value).toString();
            //Date
        } else if (Date.class.isInstance(value)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            return sdf.format((Date) value);
        }

        return null;
    }

}

/**
 *******************************************************************************
 *
 * @author martin.novacek@incad.cz
 */
class OnePoskozeni implements Serializable {

    public String field;
    public String value;

    public OnePoskozeni(String field, String value) {
        this.field = field;
        this.value = value;
    }

}
