/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package membermerge;

import ch.skema.data.Mahnung;
import ch.skema.data.Mitglied;
import ch.skema.data.Privatstunde;
import ch.skema.data.Pruefung;
import ch.skema.data.Rechnung;
import ch.skema.data.Settings;
import ch.skema.data.Vertrag;
import ch.skema.data.Vertragsstop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Cyrill
 */
public class MemberMerge {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.out.println("Arguments missing");
            System.out.println("first argument should be db source path");
            System.out.println("second argument should be db target path");
            return;
        } else {
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

            String source = args[0];
            String target = args[1];
            EntityManager sourceEm = DBEntityManager.createInstance(source);
            EntityManager targetEm = DBEntityManager.createInstance(target);

            Settings sourceSettings = getSetting(sourceEm);
            Settings targetSettings = getSetting(targetEm);

            Query getAllMemberToMerge = sourceEm.createNativeQuery("select * from mitglied where name is not null", Mitglied.class);
            List<Mitglied> allMembersToMerge = getAllMemberToMerge.getResultList();


            int counter = 0;

            for (Mitglied oldM : allMembersToMerge) {
                System.out.println("importing " + oldM.getName() + " " + oldM.getVorname() + "....");


                Mitglied newM = getNewM(oldM);
                newM.setId(IDGenerator.getNextID(newM, targetEm));

                targetEm.getTransaction().begin();
                targetEm.persist(newM);
                targetEm.getTransaction().commit();


                System.out.println("\tSaved imported member with id " + newM.getId());





                List<Pruefung> pruefungen = (List<Pruefung>) sourceEm.createNativeQuery("Select * from Pruefung  where mitgliedid = " + oldM.getId(), Pruefung.class).getResultList();
                if (pruefungen.size() == 0) {
                    System.out.println("\tno exams available to import");
                } else {
                    System.out.println("\ttrying to import exams..");
                    for (Pruefung p : pruefungen) {
                        Pruefung newPruefung = new Pruefung();
                        newPruefung.setDatum(p.getDatum());
                        newPruefung.setKommentar(p.getKommentar());
                        newPruefung.setPruefungslevelid(p.getPruefungslevelid());
                        newPruefung.setMitgliedid(newM);
                        newPruefung.setId(IDGenerator.getNextID(newPruefung, targetEm));
                        System.out.println("\t\t" + newPruefung.getPruefungslevelid().getBeschreibung() + " " + newPruefung.getPruefungslevelid().getRubrikid().getBeschreibung());

                        targetEm.getTransaction().begin();
                        targetEm.persist(newPruefung);
                        targetEm.getTransaction().commit();
                    }
                }



                Query query = sourceEm.createNamedQuery("Privatstunde.findByMitglied");
                query.setParameter("id", oldM);
                List<Privatstunde> privatstunden = query.getResultList();

                if (privatstunden.size() == 0) {
                    System.out.println("\tno privatelessons available to import");
                } else {
                    System.out.println("\ttrying to import privatlessons");
                    for (Privatstunde p : privatstunden) {
                        Privatstunde newPrivatstunde = new Privatstunde();
                        newPrivatstunde.setDatum(p.getDatum());
                        newPrivatstunde.setNotiz(p.getNotiz());
                        newPrivatstunde.setZeit(p.getZeit());
                        newPrivatstunde.setMitgliedid(newM);
                        System.out.println("\t\t" + sdf.format(newPrivatstunde.getDatum()));
                        targetEm.getTransaction().begin();
                        targetEm.persist(newPrivatstunde);
                        targetEm.getTransaction().commit();
                    }
                }



                query = sourceEm.createNamedQuery("Vertrag.findByMitgliedId");
                query.setParameter("id", oldM);

                List<Vertrag> vertraege = query.getResultList();
                if (vertraege.size() == 0) {
                    System.out.println("\tno contracts available to import");
                } else {
                    System.out.println("\ttrying to import contracts");
                    for (Vertrag v : vertraege) {
                        Vertrag ve = new Vertrag();
                        ve.setAktiv(v.getAktiv());
                        ve.setRubrikid(v.getRubrikid());
                        ve.setStartdatum(v.getStartdatum());
                        ve.setMitgliederid(newM);
                        ve.setId(IDGenerator.getNextID(ve, targetEm));

                        System.out.println("\t\t" + ve.getRubrikid().getBeschreibung());
                        targetEm.getTransaction().begin();
                        targetEm.persist(ve);
                        targetEm.getTransaction().commit();
                    }
                }

                query = sourceEm.createNamedQuery("Vertragsstop.findByMitgliedId");
                query.setParameter("id", oldM);
                List<Vertragsstop> vertragsstops = query.getResultList();

                if (vertragsstops.size() == 0) {
                    System.out.println("\tno VS available to import");
                } else {
                    System.out.println("\ttrying to import VS");
                    for (Vertragsstop vs : vertragsstops) {
                        Vertragsstop newvs = new Vertragsstop();
                        newvs.setEnddatum(vs.getEnddatum());
                        newvs.setStartdatum(vs.getStartdatum());
                        newvs.setMitgliedid(newM);
                        newvs.setId(IDGenerator.getNextID(newvs, targetEm));
                        System.out.println("\t\t VS: "+sdf.format(newvs.getStartdatum())+" - "+sdf.format(newvs.getEnddatum()));
                        targetEm.getTransaction().begin();
                        targetEm.persist(newvs);
                        targetEm.getTransaction().commit();
                    }
                }



                query = sourceEm.createNamedQuery("Rechnung.findByMitglied");
                query.setParameter("id", oldM);

                List<Rechnung> rechnungen = query.getResultList();

                if (rechnungen.size() == 0) {
                    System.out.println("\tno invoices available to import");
                } else {
                    System.out.println("\ttrying to import invoices");
                    for (Rechnung oldRechnung : rechnungen) {
                        Rechnung newRechnung = new Rechnung();
                        newRechnung.setEingang(oldRechnung.getEingang());
                        newRechnung.setEsr(oldRechnung.getEsr());
                        newRechnung.setFaelligkeit(oldRechnung.getFaelligkeit());
                        newRechnung.setNotiz(oldRechnung.getNotiz());
                        newRechnung.setMitgliedid(newM);
                        newRechnung.setId(IDGenerator.getNextID(newRechnung, targetEm));

                        System.out.println("\t\tfälligkeit:" + sdf.format(newRechnung.getFaelligkeit()));
                        targetEm.getTransaction().begin();
                        targetEm.persist(newRechnung);
                        targetEm.getTransaction().commit();

                        System.out.print("\t\ttrying to copy doc file to new location.... ");

                        String oldRechnungPath = getDocumentRoot(sourceSettings) + "Rechnungen/" + oldRechnung.getDokname();
                        String newRechnungPath = getDocumentRoot(targetSettings) + "Rechnungen/" + newRechnung.getDokname();

                        File oldRechnungFile = new File(oldRechnungPath + ".doc");
                        if (oldRechnungFile.exists()) {
                            File newRechnungFile = new File(newRechnungPath + ".doc");
                            try {
                                copyFile(oldRechnungFile, newRechnungFile);
                            } catch (Exception e) {
                                System.out.println("faild");
                            }
                            System.out.println("done: " + newRechnungFile.getAbsolutePath());
                        } else {
                            oldRechnungFile = new File(oldRechnungPath + ".docx");
                            if (oldRechnungFile.exists()) {
                                File newRechnungFile = new File(newRechnungPath + ".docx");
                                try {
                                    copyFile(oldRechnungFile, newRechnungFile);
                                } catch (Exception e) {
                                    System.out.println("faild");
                                }
                                System.out.println("done: " + newRechnungFile.getAbsolutePath());
                            } else {
                                System.out.println("no matching file found");
                            }

                        }

                        query = sourceEm.createNamedQuery("Mahnung.findByRechnungsID");
                        query.setParameter("id", oldRechnung);
                        List<Mahnung> mahnungen = query.getResultList();

                        if (mahnungen.size() == 0) {
                            System.out.println("\t\tno mahnung to import available");
                        } else {
                            System.out.println("\t\ttrying to import mahnungen");
                            for (Mahnung oldMahnung : mahnungen) {
                                Mahnung newMahnung = new Mahnung();
                                newMahnung.setFaelligkeit(oldMahnung.getFaelligkeit());
                                newMahnung.setRechnungid(newRechnung);

                                System.out.println("\t\t\tfälligkeit:" + sdf.format(newMahnung.getFaelligkeit()));
                                targetEm.getTransaction().begin();
                                targetEm.persist(newMahnung);
                                targetEm.getTransaction().commit();


                                System.out.print("\t\t\ttrying to copy doc file to new location.... ");

                                String oldMahnungPath = getDocumentRoot(sourceSettings) + "Mahnungen/" + oldMahnung.getDokname();
                                String newMahnungPath = getDocumentRoot(targetSettings) + "Mahnungen/" + newMahnung.getDokname();

                                File oldMahnungFile = new File(oldMahnungPath + ".doc");
                                if (oldMahnungFile.exists()) {
                                    File newMahnungFile = new File(newMahnungPath + ".doc");
                                    try {
                                        copyFile(oldMahnungFile, newMahnungFile);
                                    } catch (Exception e) {
                                        System.out.println("faild");
                                    }
                                    System.out.println("done: " + newMahnungFile.getAbsolutePath());
                                } else {
                                    oldMahnungFile = new File(oldMahnungPath + ".docx");
                                    if (oldMahnungFile.exists()) {
                                        File newMahnungFile = new File(newMahnungPath + ".docx");
                                        try {
                                            copyFile(oldRechnungFile, newMahnungFile);
                                        } catch (Exception e) {
                                            System.out.println("faild");
                                        }
                                        System.out.println("done: " + newMahnungFile.getAbsolutePath());
                                    } else {
                                        System.out.println("no matching file found");
                                    }

                                }



                            }

                        }



                    }

                }

                counter++;


            }




            System.out.println("");
            System.out.println("\nMerge finished.");
            System.out.println(counter + " member imported to new db");
            System.out.println("To close this programm press any key...");
            System.in.read();
        }
    }

    private static Mitglied getNewM(Mitglied m) {
        Mitglied newM = new Mitglied();
        newM.setAustrittsdatum(m.getAustrittsdatum());
        newM.setBild(m.getBild());
        newM.setEintrittsdatum(m.getEintrittsdatum());
        newM.setEmail(m.getEmail());
        newM.setFamilienrabat(m.getFamilienrabat());
        newM.setGeburtstag(m.getGeburtstag());
        newM.setIntervallid(m.getIntervallid());
        newM.setLetzteZahlung(m.getLetzteZahlung());
        newM.setName(m.getName());
        newM.setNotiz(m.getNotiz());
        newM.setOrt(m.getOrt());
        newM.setPlz(m.getPlz());
        newM.setPrivatschüler(m.getPrivatschueler());
        newM.setRechnungsanschrift(m.getRechnungsanschrift());
        newM.setRechnungsort(m.getRechnungsort());
        newM.setRechnungsplz(m.getRechnungsplz());
        newM.setRechnungsstrasse(m.getRechnungsstrasse());
        newM.setStrasse(m.getStrasse());
        newM.setTelg(m.getTelg());
        newM.setTelm(m.getTelm());
        newM.setTelp(m.getTelp());
        newM.setTribe(m.getTribe());
        newM.setUserechnungsadresse(m.getUserechnungsadresse());
        newM.setVorname(m.getVorname());
        newM.setZahlungsfaelligkeit(m.getZahlungsfaelligkeit());
        newM.setZahlungskategorieid(m.getZahlungskategorieid());
        newM.setJahreslizenz(m.getJahreslizenz());
        newM.setJahreslizenzabgerechnet(m.isJahreslizenzabgerechnet());
        newM.setJahreslizenzMonatBezahlt(m.getJahreslizenzMonatBezahlt());
        return newM;
    }

    private static Settings getSetting(EntityManager em) {

        Query q = em.createNamedQuery("Settings.findById");
        q.setParameter("id", 1);
        return (Settings) q.getSingleResult();
    }

    private static String getDocumentRoot(Settings settings) {
        String customDocumentRoot = settings.getCustomdocumentroot();
        if (customDocumentRoot == null) {
            return System.getProperty("user.home") + "/MitgliederVerwaltung/";
        } else {
            return customDocumentRoot + "/MitgliederVerwaltung/";
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
