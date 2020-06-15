package de.karrieretutor.springboot.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduktRepository extends CrudRepository<Produkt, Long> {}
