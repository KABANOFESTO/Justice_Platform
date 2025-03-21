package org.Government.JusticePlatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.Government.JusticePlatform.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetPasswordToken(String resetPasswordToken);
}
