package com.enigmacamp.loan_app.repository;

import com.enigmacamp.loan_app.constant.ERole;
import com.enigmacamp.loan_app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(ERole role);
}
