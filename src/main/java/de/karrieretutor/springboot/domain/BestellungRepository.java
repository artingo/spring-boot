package de.karrieretutor.springboot.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BestellungRepository extends CrudRepository<Bestellung, Long> {
    List<Bestellung> findByKundeId(Long kundenId);
}
