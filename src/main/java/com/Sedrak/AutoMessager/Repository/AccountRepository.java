package com.Sedrak.AutoMessager.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Sedrak.AutoMessager.Model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  List<Account> findAll();

  Optional<Account> findByUsername(String username);

}
