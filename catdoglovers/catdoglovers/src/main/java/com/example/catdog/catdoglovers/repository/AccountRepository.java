package com.example.catdog.catdoglovers.repository;

import com.example.catdog.catdoglovers.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value="SELECT acc.id FROM Account acc WHERE acc.username LIKE CONCAT('%', :username, '%')", nativeQuery = true)
    Long findIdByUsername(String username);
    Account findFirstByUsername(String username);
    Account findByUsername(String username);

    Account findByEmail(String email);
    @Query(value = "select * from account where verification_code = :code", nativeQuery = true)
    Account findByVerificationCode(String code);

    @Query(value = "select a.account_id from account_roles a where role_id = 1", nativeQuery = true)
    Long getUserAccountId();

}
