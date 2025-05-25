package com.practicum.practicum.repository;

import com.practicum.practicum.model.TwoFactorAuthefication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorAutheficationRepository extends JpaRepository<TwoFactorAuthefication, String> {

}
