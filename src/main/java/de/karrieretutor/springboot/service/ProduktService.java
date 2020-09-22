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
import java.util.Collections;
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

    public void updateProdukt(Produkt neuesProdukt) {
        LOG.info("updating product: " + neuesProdukt.getId());
        Produkt cachedProdukt = findProdukt(neuesProdukt.getId());
        if (cachedProdukt != null) {
            Collections.replaceAll(cachedProdukte, cachedProdukt, neuesProdukt);
        } else {
            cachedProdukte.add(neuesProdukt);
        }
    }

    public void deleteProdukt(Long id) {
        LOG.info("deleting product: " + id);
        Produkt delProdukt = findProdukt(id);
        if (delProdukt != null) {
            cachedProdukte.remove(delProdukt);
        }
    }

    private Produkt findProdukt(Long id) {
        Optional<Produkt> optional = cachedProdukte.stream().filter(p -> p.getId() == id).findFirst();
        return optional.isPresent()? optional.get() : null;
    }


}
