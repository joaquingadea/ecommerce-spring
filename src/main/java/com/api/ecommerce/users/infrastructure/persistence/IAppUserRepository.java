package com.api.ecommerce.users.infrastructure.persistence;

import com.api.ecommerce.users.domain.AppUser;
import com.api.ecommerce.users.dto.response.UserIdUsernameDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAppUserRepository extends JpaRepository<AppUser,Long>, JpaSpecificationExecutor<AppUser> {
    @Query("""
            SELECT u.id AS id, u.username AS username FROM AppUser u
            """)
    Page<UserIdUsernameDTO> findAllIdAndUsername(Pageable pageRequest);
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
