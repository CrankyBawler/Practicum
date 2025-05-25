package com.practicum.practicum.service;

import com.google.auth.totp.Totp;
import com.practicum.practicum.model.TwoFactorAuth;
import com.practicum.practicum.repository.TwoFactorAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TwoFactorAuthService {

    private final TwoFactorAuthRepository authRepo;

    public String setupGoogleAuth(String userId) {
        // Генерируем случайный секретный ключ
        byte[] secret = new byte[6];
        new SecureRandom().nextBytes(secret);
        String encodedSecret = Base64.getEncoder().encodeToString(secret);

        // Сохраняем в БД
        TwoFactorAuth user2fa = new TwoFactorAuth();
        user2fa.setUserId(userId);
        user2fa.setMethod("GOOGLE_AUTH");
        user2fa.setSecretKey(encodedSecret);
        user2fa.setEnabled(false);
        authRepo.save(user2fa);

        return encodedSecret;
    }

    public boolean verifyCode(String userId, String code) {
        Optional<TwoFactorAuth> opt = authRepo.findById(userId);
        if (opt.isEmpty()) return false;

        String secret = opt.get().getSecretKey();

        Totp totp = Totp.createRaw(secret, Totp.HashAlgorithm.SHA1, 6, 30);

        try {
            long currentCode = Long.parseLong(code);
            if (totp.verify(currentCode)) {
                // Активируем 2FA
                UserTwoFactor user2fa = opt.get();
                user2fa.setEnabled(true);
                authRepo.save(user2fa);
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    public boolean disableTwoFactor(String userId, String code) {
        Optional<UserTwoFactor> opt = authRepo.findById(userId);
        if (opt.isEmpty()) return false;

        String secret = opt.get().getSecretKey();
        Totp totp = Totp.createRaw(secret, Totp.HashAlgorithm.SHA1, 6, 30);

        try {
            long currentCode = Long.parseLong(code);
            if (totp.verify(currentCode)) {
                authRepo.deleteById(userId);
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }
}
//import com.practicum.practicum.DTO.*;
//import com.practicum.practicum.model.TwoFactorAuth;
//import com.practicum.practicum.repository.TwoFactorAuthRepository;
//import com.warrenstrange.googleauth.GoogleAuthenticator;
//import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class TwoFactorAuthService {
//
//    private final TwoFactorAuthRepository twoFactorAuthRepository;
//    private final GoogleAuthenticator googleAuthenticator;
//
//    public SetupResponse setup(SetupRequest request) {
//        // Генерируем секретный ключ
//        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
//
//        // Сохраняем в БД
//        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
//        twoFactorAuth.setUserId(request.getUserId());
//        twoFactorAuth.setSecret(key.getKey());
//        twoFactorAuth.setMethod(request.getMethod());
//        twoFactorAuth.setEnabled(false); // Пока не подтвержден
//
//        twoFactorAuthRepository.save(twoFactorAuth);
//
//        return new SetupResponse(key.getKey());
//    }
//
//    public VerifyResponse verify(VerifyRequest request) {
//        Optional<TwoFactorAuth> optionalAuth = twoFactorAuthRepository.findById(request.getUserId());
//
//        if (optionalAuth.isEmpty()) {
//            return new VerifyResponse("failed");
//        }
//
//        TwoFactorAuth twoFactorAuth = optionalAuth.get();
//        boolean isCodeValid = googleAuthenticator.authorize(twoFactorAuth.getSecret(), Integer.parseInt(request.getCode()));
//
//        if (isCodeValid) {
//            twoFactorAuth.setEnabled(true);
//            twoFactorAuthRepository.save(twoFactorAuth);
//            return new VerifyResponse("success");
//        } else {
//            return new VerifyResponse("failed");
//        }
//    }
//
//    public DisableResponse disable(DisableRequest request) {
//        Optional<TwoFactorAuth> optionalAuth = twoFactorAuthRepository.findById(request.getUserId());
//
//        if (optionalAuth.isEmpty()) {
//            return new DisableResponse("failed");
//        }
//
//        TwoFactorAuth twoFactorAuth = optionalAuth.get();
//
//        // Проверяем код перед отключением
//        boolean isCodeValid = googleAuthenticator.authorize(twoFactorAuth.getSecret(), Integer.parseInt(request.getCode()));
//
//        if (isCodeValid) {
//            twoFactorAuth.setEnabled(false);
//            twoFactorAuthRepository.save(twoFactorAuth);
//            return new DisableResponse("disabled");
//        } else {
//            return new DisableResponse("failed");
//        }
//    }
//}