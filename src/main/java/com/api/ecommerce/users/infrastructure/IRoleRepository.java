package com.api.ecommerce.users.infrastructure;

import com.api.ecommerce.users.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Long> {}
