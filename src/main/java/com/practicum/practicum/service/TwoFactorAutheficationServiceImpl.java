package com.practicum.practicum.service;

import com.practicum.practicum.DTO.SetupResponse;
import com.practicum.practicum.DTO.SetupRequest;
import com.practicum.practicum.model.TwoFactorAuthefication;
import com.practicum.practicum.repository.TwoFactorAutheficationRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TwoFactorAutheficationServiceImpl implements TwoFactorAutheficationService {
    private final TwoFactorAutheficationRepository twoFactorAutheficationRepository;
    private final GoogleAuthenticator googleAuthenticator;

    public SetupResponse setup(SetupRequest request) {
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        TwoFactorAuthefication twoFactorAuthefication = new TwoFactorAuthefication();
        twoFactorAuthefication.setUserId(request.getUserId());
        twoFactorAuthefication.setSecret(key.getKey());
        twoFactorAuthefication.setMethod(request.getMethod());
        twoFactorAuthefication.setEnabled(false);

        twoFactorAutheficationRepository.saveAndFlush(twoFactorAuthefication);


        return new SetupResponse(key.getKey());
    }


}
