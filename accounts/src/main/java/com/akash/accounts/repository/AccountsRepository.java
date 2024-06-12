package com.akash.accounts.repository;

import com.akash.accounts.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    void deleteByCustomerId(Long customerId);
    Optional<Accounts> findByCustomerId(Long customerId);
}
