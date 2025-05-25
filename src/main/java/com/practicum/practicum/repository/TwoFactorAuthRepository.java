package com.practicum.practicum.repository;




import com.practicum.practicum.model.TwoFactorAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorAuthRepository extends JpaRepository<TwoFactorAuth, String> {
}

