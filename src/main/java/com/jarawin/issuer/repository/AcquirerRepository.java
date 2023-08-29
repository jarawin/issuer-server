package com.jarawin.issuer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jarawin.issuer.entity.Acquirer;

@Repository
public interface AcquirerRepository extends JpaRepository<Acquirer, String> {
}
