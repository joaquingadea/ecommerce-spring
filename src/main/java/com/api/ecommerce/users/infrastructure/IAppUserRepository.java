package com.api.ecommerce.users.infrastructure;

import com.api.ecommerce.users.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAppUserRepository extends JpaRepository<AppUser,Long> {}
