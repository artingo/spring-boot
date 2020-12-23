package de.karrieretutor.springboot.service;

import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.ProduktRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProduktService {
    private static final Logger LOG = LoggerFactory.getLogger(ProduktService.class);

    @Autowired
    private ProduktRepository produktRepository;
    private List<Produkt> cachedProdukte = new ArrayList<>();

    @PostConstruct
    @Transactional(readOnly = true)
    public List<Produkt> ladeProdukte() {
        if (cachedProdukte.isEmpty()) {
            LOG.info("loading products from server");
            produktRepository.findAll().forEach(cachedProdukte::add);
        }
        return cachedProdukte;
    }

    @Transactional(readOnly = true)
    public Produkt getProdukt(Long id) {
        return findProdukt(id);
    }

    public void updateProdukt(Produkt produkt) {
        Produkt neuesProdukt = produktRepository.save(produkt);
        LOG.info("updating product: " + neuesProdukt.getId());
        Produkt cachedProdukt = findProdukt(neuesProdukt.getId());
        if (cachedProdukt != null) {
            cachedProdukte.set(cachedProdukte.indexOf(cachedProdukt), neuesProdukt);
        } else {
            cachedProdukte.add(neuesProdukt);
        }
    }

    public String deleteProdukt(Long id) {
        LOG.info("deleting product: " + id);
        Optional<Produkt> produktDB = produktRepository.findById(id);
        if (produktDB.isPresent()) {
            Produkt loeschProdukt = produktDB.get();
            produktRepository.delete(loeschProdukt);
            Produkt cachedProdukt = findProdukt(id);
            if (cachedProdukt != null) {
                cachedProdukte.remove(cachedProdukt);
            }
            return loeschProdukt.getName();
        }
        return null;
    }

    public Produkt findProdukt(Long id) {
        if (id == null) return null;
        if (cachedProdukte.isEmpty()) {
            cachedProdukte = ladeProdukte();
        }
        Optional<Produkt> optional = cachedProdukte.stream().filter(p -> p.getId() == id).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }


}
