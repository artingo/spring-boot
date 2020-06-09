package de.karrieretutor.springboot.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeinRepository extends CrudRepository<Wein, Long> {}
