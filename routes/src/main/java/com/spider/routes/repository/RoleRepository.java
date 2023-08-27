package com.spider.routes.repository;

import com.spider.routes.model.Role;
import com.spider.routes.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    public Optional<Role> findByName(RoleName roleName);
}
