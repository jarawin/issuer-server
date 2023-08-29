package com.jarawin.issuer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.jarawin.issuer.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
