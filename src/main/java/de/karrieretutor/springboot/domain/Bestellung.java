package de.karrieretutor.springboot.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.karrieretutor.springboot.enums.BestellStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
public class Bestellung {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonFormat(pattern = "dd.MM.yyyy hh:mm")
    private LocalDateTime datum;

    private BestellStatus status;

    @ManyToOne
    private Kunde kunde;

    @OneToMany(mappedBy = "bestellung", cascade = ALL)
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
        if (!this.kunde.getBestellungen().contains(this)) {
            this.kunde.getBestellungen().add(this);
        }
    }

    public List<BestelltesProdukt> getProdukte() {
        return produkte;
    }
    public void setProdukte(List<BestelltesProdukt> produkte) {
        produkte.forEach(p -> p.setBestellung(this));
        this.produkte = produkte;
    }

    public String getGesamtpreis() {
        double gesamtpreis = 0;
        for(BestelltesProdukt p : produkte) {
            gesamtpreis += p.getProdukt().getPreis();
        }
        return String.format("%.2f", gesamtpreis);

    }
}
