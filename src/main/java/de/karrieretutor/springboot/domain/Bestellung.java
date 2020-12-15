package de.karrieretutor.springboot.domain;

import de.karrieretutor.springboot.enums.BestellStatus;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static de.karrieretutor.springboot.Const.ORDER;
import static javax.persistence.CascadeType.ALL;

@Entity
public class Bestellung {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime datum;
    private BestellStatus status;

    @Valid
    @ManyToOne
    private Kunde kunde;

    @OneToMany(mappedBy = ORDER, cascade = ALL)
    private List<BestelltesProdukt> produkte = new ArrayList<>();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDatum() {
        return datum;
    }
    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public BestellStatus getStatus() {
        return status;
    }
    public void setStatus(BestellStatus status) {
        this.status = status;
    }

    public Kunde getKunde() {
        return kunde;
    }
    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }

    public List<BestelltesProdukt> getProdukte() {
        return produkte;
    }
    public void setProdukte(List<BestelltesProdukt> produkte) {
        this.produkte = produkte;
    }

    public String getGesamtpreis() {
        double gesamtpreis = 0;
        for(BestelltesProdukt p : produkte) {
            gesamtpreis += p.getProdukt().getPreis() * p.getAnzahl();
        }
        return String.format("%.2f", gesamtpreis);

    }

    public int getGesamtzahl() {
        int gesamtzahl = 0;
        for(BestelltesProdukt p : produkte) {
            gesamtzahl += p.getAnzahl();
        }
        return gesamtzahl;

    }

    @Override
    public String toString() {
        String result = "Bestellung{" +
                "id=" + id +
                ", datum=" + datum +
                ", status=" + status +
                ", kunde=" + kunde.getId() +
                ", produkte=[";
        for (BestelltesProdukt p : produkte)
            result += "id:"+p.getProdukt().getId() + ", anzahl:" + p.getAnzahl();
        result += "]\n}";
        return result;
    }
}
