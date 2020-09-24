package de.karrieretutor.springboot.service;

import de.karrieretutor.springboot.domain.Kunde;
import de.karrieretutor.springboot.domain.KundenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
public class KundenService {

    Logger LOG = LoggerFactory.getLogger(KundenService.class);

    @Autowired
    KundenRepository kundeRepository;

    @Transactional(readOnly = true)
    public  Kunde lade(Long kundenId) {
        Optional<Kunde> kunde = this.kundeRepository.findById(kundenId);
        return kunde.isPresent() ? kunde.get() : null;
    }

    @Transactional
    public Long speichern(Kunde kunde) {
        try {
            kundeRepository.save(kunde);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return -1L;
        }
        return kunde.getId();
    }


}
