package com.project.ElectronicStore.repositories;

import com.project.ElectronicStore.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
