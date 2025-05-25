package com.practicum.practicum.repository;

import com.practicum.practicum.model.TwoFactorAuthefication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwoFactorAutheficationRepository extends JpaRepository<TwoFactorAuthefication, String> {


}