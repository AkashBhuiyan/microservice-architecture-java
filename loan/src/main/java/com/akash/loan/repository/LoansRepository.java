package com.akash.loan.repository;


import com.akash.loan.entity.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author: Akash Bhuiyan
 * User:akashbhuiyan, Date:6/13/24
 */

@Repository
public interface LoansRepository extends JpaRepository<Loans, Long> {

    Optional<Loans> findByMobileNumber(String mobileNumber);

    Optional<Loans> findByLoanNumber(String loanNumber);

}
