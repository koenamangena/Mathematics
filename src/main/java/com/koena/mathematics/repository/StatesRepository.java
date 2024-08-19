package com.koena.mathematics.repository;

import com.koena.mathematics.domain.States;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatesRepository extends CrudRepository<States, Long> {
    List<States> findByUserAndStartEqualsAndEndEquals(String user, LocalDateTime start, LocalDateTime end);
}
