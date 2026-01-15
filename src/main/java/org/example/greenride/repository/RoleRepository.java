package org.example.greenride.repository;

import org.example.greenride.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository για διαχείριση Role entities (USER/ADMIN)
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Αναζήτηση ρόλου με βάση όνομα (π.χ. "ROLE_USER", "ROLE_ADMIN")
    Role findByName(String name);
}