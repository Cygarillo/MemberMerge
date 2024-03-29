/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skema.data;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.*;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Cyrill
 */
@Entity
@Table(name = "MITGLIED", catalog = "MITGLIEDERDB", schema = "PUBLIC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mitglied.findAll", query = "SELECT m FROM Mitglied m"),
    @NamedQuery(name = "Mitglied.findAllAktive", query = "SELECT m FROM Mitglied m  where m.id in (SELECT v.mitgliederid.id FROM Vertrag v where v.aktiv = true) and m.austrittsdatum is null"),
    @NamedQuery(name = "Mitglied.findAllOhneAustritt", query = "SELECT m FROM Mitglied m  where m.austrittsdatum is null"),
    @NamedQuery(name = "Mitglied.findAllPassive", query = "SELECT m FROM Mitglied m  where m.id not in (SELECT v.mitgliederid.id FROM Vertrag v where v.aktiv = true)"),
    @NamedQuery(name = "Mitglied.findAllByRubrik", query = "SELECT m FROM Mitglied m  where m.id in (SELECT v.mitgliederid.id FROM Vertrag v where v.rubrikid = :rubrik)"),
    @NamedQuery(name = "Mitglied.findAllByPruefungslevelId", query = "SELECT m FROM Mitglied m  where m.id in (SELECT p.mitgliedid.id FROM Pruefung p where p.pruefungslevelid.id = :id)"),
    @NamedQuery(name = "Mitglied.findAllOhnePruefung", query = "SELECT m FROM Mitglied m where m.id not in (SELECT p.mitgliedid.id FROM Pruefung p)"),
    @NamedQuery(name = "Mitglied.findAllPrivatschueler", query = "SELECT m FROM Mitglied m where m.privatschueler = true"),
    @NamedQuery(name = "Mitglied.findById", query = "SELECT m FROM Mitglied m WHERE m.id = :id"),
    @NamedQuery(name = "Mitglied.findByZahlungskategorie", query = "SELECT m FROM Mitglied m WHERE m.zahlungskategorieid = :zahlungskategorie"),
    @NamedQuery(name = "Mitglied.findByName", query = "SELECT m FROM Mitglied m WHERE m.name = :name"),
    @NamedQuery(name = "Mitglied.findByVorname", query = "SELECT m FROM Mitglied m WHERE m.vorname = :vorname"),
    @NamedQuery(name = "Mitglied.findByStrasse", query = "SELECT m FROM Mitglied m WHERE m.strasse = :strasse"),
    @NamedQuery(name = "Mitglied.findByPlz", query = "SELECT m FROM Mitglied m WHERE m.plz = :plz"),
    @NamedQuery(name = "Mitglied.findByOrt", query = "SELECT m FROM Mitglied m WHERE m.ort = :ort"),
    @NamedQuery(name = "Mitglied.findByTelp", query = "SELECT m FROM Mitglied m WHERE m.telp = :telp"),
    @NamedQuery(name = "Mitglied.findByTelg", query = "SELECT m FROM Mitglied m WHERE m.telg = :telg"),
    @NamedQuery(name = "Mitglied.findByTelm", query = "SELECT m FROM Mitglied m WHERE m.telm = :telm"),
    @NamedQuery(name = "Mitglied.findByEmail", query = "SELECT m FROM Mitglied m WHERE m.email = :email"),
    @NamedQuery(name = "Mitglied.findByGeburtstag", query = "SELECT m FROM Mitglied m WHERE m.geburtstag = :geburtstag"),
    @NamedQuery(name = "Mitglied.findByEintrittsdatum", query = "SELECT m FROM Mitglied m WHERE m.eintrittsdatum = :eintrittsdatum"),
    @NamedQuery(name = "Mitglied.findByTribe", query = "SELECT m FROM Mitglied m WHERE m.tribe = :tribe"),
    @NamedQuery(name = "Mitglied.findByFamilienrabat", query = "SELECT m FROM Mitglied m WHERE m.familienrabat = :familienrabat"),
    @NamedQuery(name = "Mitglied.findByAustrittsdatum", query = "SELECT m FROM Mitglied m WHERE m.austrittsdatum = :austrittsdatum"),
    @NamedQuery(name = "Mitglied.findAllOffeneRechnung", query = "SELECT m FROM Mitglied m WHERE m.id in(SELECT r.mitgliedid.id from Rechnung r where r.eingang is null)"),
    @NamedQuery(name = "Mitglied.findAllOhneAktuelleMember", query = "SELECT m FROM Mitglied m  where m.austrittsdatum is null and (m.jahreslizenz < :jahr or m.jahreslizenz is null)"),
    @NamedQuery(name = "Mitglied.findAllMemberbezahlt", query = "SELECT m FROM Mitglied m  where m.austrittsdatum is null and m.jahreslizenz = :jahr and (m.jahreslizenzabgerechnet=false or m.jahreslizenzabgerechnet is null)"),
    @NamedQuery(name = "Mitglied.findAllmemberAbgerechnet", query = "SELECT m FROM Mitglied m  where m.austrittsdatum is null and m.jahreslizenz = :jahr and m.jahreslizenzabgerechnet=true")})
public class Mitglied implements Serializable, MitgliederDBPersistenceInterface {

    @Column(name = "GEBURTSTAG")
    @Temporal(TemporalType.DATE)
    private Date geburtstag;
    @Column(name = "EINTRITTSDATUM")
    @Temporal(TemporalType.DATE)
    private Date eintrittsdatum;
    @Column(name = "TRIBE")
    private boolean tribe;
    @Column(name = "JAHRESLIZENZ")
    private Integer jahreslizenz;
    @Column(name = "JAHRESLIZENZABGERECHNET")
    private boolean jahreslizenzabgerechnet;
    @Column(name = "FAMILIENRABAT")
    private boolean familienrabat;
    @Column(name = "AUSTRITTSDATUM")
    @Temporal(TemporalType.DATE)
    private Date austrittsdatum;
    @Lob
    @Column(name = "BILD")
    private byte[] bild;
    @Column(name = "ZAHLUNGSFAELLIGKEIT")
    @Temporal(TemporalType.DATE)
    private Date zahlungsfaelligkeit;
    @Column(name = "USERECHNUNGSADRESSE")
    private boolean userechnungsadresse;
    @Column(name = "PRIVATSCHUELER")
    private boolean privatschueler;
    @Column(name = "RECHNUNGSANSCHRIFT")
    private String rechnungsanschrift;
    @Column(name = "RECHNUNGSSTRASSE")
    private String rechnungsstrasse;
    @Column(name = "RECHNUNGSPLZ")
    private Integer rechnungsplz;
    @Column(name = "RECHNUNGSORT")
    private String rechnungsort;
    @OneToMany(mappedBy = "mitgliedid")
    private Collection<Dokument> dokumentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mitgliedid")
    private Collection<Privatstunde> privatstundeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mitgliedid")
    private Collection<Vertragsstop> vertragsstopCollection;
    @JoinColumn(name = "INTERVALLID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Zahlungsintervall intervallid;
    private static final long serialVersionUID = 1L;
    @Id
//   @GeneratedValue(strategy = GenerationType.)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "VORNAME")
    private String vorname;
    @Column(name = "STRASSE")
    private String strasse;
    @Column(name = "PLZ")
    private Integer plz;
    @Column(name = "ORT")
    private String ort;
    @Column(name = "TELP")
    private String telp;
    @Column(name = "TELG")
    private String telg;
    @Column(name = "TELM")
    private String telm;
    @Column(name = "EMAIL")
    private String email;
    @Lob
    @Column(name = "NOTIZ")
    private String notiz;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mitgliederid")
    private Collection<Vertrag> vertragCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mitgliedid")
    private Collection<Austritt> austrittCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mitgliedid")
    private Collection<Rechnung> rechnungCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mitgliedid")
    private Collection<Adresswechsel> adresswechselCollection;
    @JoinColumn(name = "ZAHLUNGSKATEGORIEID", referencedColumnName = "ID")
    @ManyToOne
    private Zahlungskategorie zahlungskategorieid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mitgliedid")
    private Collection<Pruefung> pruefungCollection;
    @Column(name = "LETZTEZAHLUNG")
    @Temporal(TemporalType.DATE)
    private Date letztezahlung;
    
    @Column(name="JahreslizenzMonatBezahlt")
    private Integer jahreslizenzMonatBezahlt;

    

    public Mitglied() {
    }

    public Mitglied(Integer id) {
        this.id = id;
    }

    public Mitglied(Integer id, String name, String vorname, Date eintrittsdatum) {
        this.id = id;
        this.name = name;
        this.vorname = vorname;
        this.eintrittsdatum = eintrittsdatum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public Integer getPlz() {
        return plz;
    }

    public void setPlz(Integer plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getTelg() {
        return telg;
    }

    public void setTelg(String telg) {
        this.telg = telg;
    }

    public String getTelm() {
        return telm;
    }

    public void setTelm(String telm) {
        this.telm = telm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getGeburtstag() {
        return geburtstag;
    }

    public void setGeburtstag(Date geburtstag) {
        this.geburtstag = geburtstag;
    }

    public Date getEintrittsdatum() {
        return eintrittsdatum;
    }

    public void setEintrittsdatum(Date eintrittsdatum) {
        this.eintrittsdatum = eintrittsdatum;
    }

    public boolean getTribe() {
        return tribe;
    }

    public void setTribe(boolean tribe) {
        this.tribe = tribe;
    }

    public boolean getFamilienrabat() {
        return familienrabat;
    }

    public void setFamilienrabat(boolean familienrabat) {
        this.familienrabat = familienrabat;
    }

    public Date getAustrittsdatum() {
        return austrittsdatum;
    }

    public void setAustrittsdatum(Date austrittsdatum) {
        this.austrittsdatum = austrittsdatum;
    }

    public String getNotiz() {
        return notiz;
    }

    public void setNotiz(String notiz) {
        this.notiz = notiz;
    }

    public byte[] getBild() {
        return bild;
    }

    public void setBild(byte[] bild) {
        this.bild = bild;
    }

    @XmlTransient
    public Collection<Vertrag> getVertragCollection() {
        return vertragCollection;
    }

    public void setVertragCollection(Collection<Vertrag> vertragCollection) {
        this.vertragCollection = vertragCollection;
    }

    @XmlTransient
    public Collection<Austritt> getAustrittCollection() {
        return austrittCollection;
    }

    public void setAustrittCollection(Collection<Austritt> austrittCollection) {
        this.austrittCollection = austrittCollection;
    }

    @XmlTransient
    public Collection<Rechnung> getRechnungCollection() {
        return rechnungCollection;
    }

    public void setRechnungCollection(Collection<Rechnung> rechnungCollection) {
        this.rechnungCollection = rechnungCollection;
    }

    @XmlTransient
    public Collection<Adresswechsel> getAdresswechselCollection() {
        return adresswechselCollection;
    }

    public void setAdresswechselCollection(Collection<Adresswechsel> adresswechselCollection) {
        this.adresswechselCollection = adresswechselCollection;
    }

    public Zahlungskategorie getZahlungskategorieid() {
        return zahlungskategorieid;
    }

    public void setZahlungskategorieid(Zahlungskategorie zahlungskategorieid) {
        this.zahlungskategorieid = zahlungskategorieid;
    }

    @XmlTransient
    public Collection<Pruefung> getPruefungCollection() {
        return pruefungCollection;
    }

    public void setPruefungCollection(Collection<Pruefung> pruefungCollection) {
        this.pruefungCollection = pruefungCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mitglied)) {
            return false;
        }
        Mitglied other = (Mitglied) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ch.skema.data.Mitglied[ id=" + id + " ] : " + vorname + " " + name;
    }

    public Zahlungsintervall getIntervallid() {
        return intervallid;
    }

    public void setIntervallid(Zahlungsintervall intervallid) {
        this.intervallid = intervallid;
    }

    @XmlTransient
    public Collection<Vertragsstop> getVertragsstopCollection() {
        return vertragsstopCollection;
    }

    public void setVertragsstopCollection(Collection<Vertragsstop> vertragsstopCollection) {
        this.vertragsstopCollection = vertragsstopCollection;
    }

    public Date getZahlungsfaelligkeit() {
        return zahlungsfaelligkeit;
    }

    public void setZahlungsfaelligkeit(Date zahlungsfaelligkeit) {
        this.zahlungsfaelligkeit = zahlungsfaelligkeit;
    }

    public boolean getUserechnungsadresse() {
        return userechnungsadresse;
    }

    public void setUserechnungsadresse(boolean userechnungsadresse) {
        this.userechnungsadresse = userechnungsadresse;
    }

    public String getRechnungsanschrift() {
        return rechnungsanschrift;
    }

    public void setRechnungsanschrift(String rechnungsanschrift) {
        this.rechnungsanschrift = rechnungsanschrift;
    }

    public String getRechnungsstrasse() {
        return rechnungsstrasse;
    }

    public void setRechnungsstrasse(String rechnungsstrasse) {
        this.rechnungsstrasse = rechnungsstrasse;
    }

    public Integer getRechnungsplz() {
        return rechnungsplz;
    }

    public void setRechnungsplz(Integer rechnungsplz) {
        this.rechnungsplz = rechnungsplz;
    }

    public String getRechnungsort() {
        return rechnungsort;
    }

    public void setRechnungsort(String rechnungsort) {
        this.rechnungsort = rechnungsort;
    }

    public boolean getPrivatschueler() {
        return privatschueler;
    }

    public void setPrivatschüler(boolean privatschueler) {
        this.privatschueler = privatschueler;
    }

    @XmlTransient
    public Collection<Dokument> getDokumentCollection() {
        return dokumentCollection;
    }

    public void setDokumentCollection(Collection<Dokument> dokumentCollection) {
        this.dokumentCollection = dokumentCollection;
    }

    @XmlTransient
    public Collection<Privatstunde> getPrivatstundeCollection() {
        return privatstundeCollection;
    }

    public void setPrivatstundeCollection(Collection<Privatstunde> privatstundeCollection) {
        this.privatstundeCollection = privatstundeCollection;
    }

    public HashMap<String, String> getAnrede() {
        HashMap<String, String> anrede = new HashMap<String, String>();
        String emptyVal = "";

        if (userechnungsadresse) {
            if (getRechnungsanschrift() != null) {
                anrede.put("%Vorname%", getRechnungsanschrift());
            } else {
                anrede.put("%Vorname%", emptyVal);
            }
            anrede.put("%Name%", emptyVal);
            if (getRechnungsstrasse() != null) {
                anrede.put("%Adresse%", getRechnungsstrasse());
            } else {
                anrede.put("%Adresse%", emptyVal);
            }
            if (getRechnungsplz() != null) {
                anrede.put("%Plz%", Integer.toString(getRechnungsplz()));
            } else {
                anrede.put("%Plz%", emptyVal);
            }
            if (getRechnungsort() != null) {
                anrede.put("%Ort%", getRechnungsort());
            } else {
                anrede.put("%Ort%", emptyVal);
            }
        } else {


            if (getVorname() != null) {
                anrede.put("%Vorname%", getVorname());
            } else {
                anrede.put("%Vorname%", emptyVal);
            }
            if (getName() != null) {
                anrede.put("%Name%", getName());
            } else {
                anrede.put("%Name%", emptyVal);
            }
            if (getStrasse() != null) {
                anrede.put("%Adresse%", getStrasse());
            } else {
                anrede.put("%Adresse%", emptyVal);
            }
            if (getPlz() != null) {
                anrede.put("%Plz%", Integer.toString(getPlz()));
            } else {
                anrede.put("%Plz%", emptyVal);
            }
            if (getOrt() != null) {
                anrede.put("%Ort%", getOrt());
            } else {
                anrede.put("%Ort%", emptyVal);
            }

        }
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);
        anrede.put("%Datum%", df.format(Calendar.getInstance().getTime()).toString());

        return anrede;

    }

    public Date getLetzteZahlung() {
        if (letztezahlung == null) {
            Date d = null;

            for (Rechnung r : rechnungCollection) {
                if (r.getEingang() != null) {
                    if (d == null) {
                        d = r.getEingang();
                    } else {
                        if (d.before(r.getEingang())) {
                            d = r.getEingang();
                        }
                    }
                }
            }
            return d;
        } else {
            return letztezahlung;
        }
    }
    
    public void setLetzteZahlung(Date letzteZahlung){
        this.letztezahlung = letzteZahlung;
    }

    public boolean isPassiv() {

        for (Vertrag vertrag : vertragCollection) {
            if (vertrag.getAktiv()) {
                return false;
            }
        }
        return true;
    }
    
    
    public Integer getJahreslizenz() {
        return jahreslizenz;
    }

    public void setJahreslizenz(Integer jahreslizenz) {
        this.jahreslizenz = jahreslizenz;
    }

    public boolean isJahreslizenzabgerechnet() {
        return jahreslizenzabgerechnet;
    }

    public void setJahreslizenzabgerechnet(boolean jahreslizenzabgerechnet) {
        this.jahreslizenzabgerechnet = jahreslizenzabgerechnet;
    }
    
    public Integer getJahreslizenzMonatBezahlt() {
        return jahreslizenzMonatBezahlt;
    }

    public void setJahreslizenzMonatBezahlt(Integer jahreslizenzMonatBezahlt) {
        this.jahreslizenzMonatBezahlt = jahreslizenzMonatBezahlt;
    }   
}
