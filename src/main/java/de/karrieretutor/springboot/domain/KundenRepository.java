package de.karrieretutor.springboot.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KundenRepository extends CrudRepository<Kunde, Long> {
    List<Kunde> findByEmail(String email);
}
