package com.aidsyla.banksystemdemo.repository;

import com.aidsyla.banksystemdemo.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    Bank findByName(String name);
    Optional<Bank> findById(Long id);
}
