package de.karrieretutor.springboot.service;

import de.karrieretutor.springboot.domain.Kunde;
import de.karrieretutor.springboot.domain.KundenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class KundenService {
    Logger LOG = LoggerFactory.getLogger(KundenService.class);

    @Autowired
    KundenRepository kundeRepository;

    @Transactional(readOnly = true)
    public Kunde lade(Long kundenId) {
        if (kundenId == null) {
            return new Kunde();
        }
        Optional<Kunde> kunde = this.kundeRepository.findById(kundenId);
        return kunde.isPresent() ? kunde.get() : new Kunde();
    }


    @Transactional(readOnly = true)
    public Kunde findByEmail(String email) {
        List<Kunde> kunden = kundeRepository.findByEmail(email);
        return !kunden.isEmpty() ? kunden.get(0) : null;
    }

    @Transactional
    public Kunde speichern(Kunde kunde) {
        try {
            kunde = kundeRepository.save(kunde);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            kunde = null;
        }
        return kunde;
    }
}
