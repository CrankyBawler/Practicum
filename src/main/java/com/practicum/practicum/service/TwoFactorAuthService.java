import com.practicum.practicum.DTO.*;
import com.practicum.practicum.model.TwoFactorAuth;
import com.practicum.practicum.repository.TwoFactorAuthRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TwoFactorAuthService {

    private final TwoFactorAuthRepository twoFactorAuthRepository;
    private final GoogleAuthenticator googleAuthenticator;

    public SetupResponse setup(SetupRequest request) {
        // Генерируем секретный ключ
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();

        // Сохраняем в БД
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setUserId(request.getUserId());
        twoFactorAuth.setSecret(key.getKey());
        twoFactorAuth.setMethod(request.getMethod());
        twoFactorAuth.setEnabled(false); // Пока не подтвержден

        twoFactorAuthRepository.save(twoFactorAuth);

        return new SetupResponse(key.getKey());
    }

    public VerifyResponse verify(VerifyRequest request) {
        Optional<TwoFactorAuth> optionalAuth = twoFactorAuthRepository.findById(request.getUserId());

        if (optionalAuth.isEmpty()) {
            return new VerifyResponse("failed");
        }

        TwoFactorAuth twoFactorAuth = optionalAuth.get();
        boolean isCodeValid = googleAuthenticator.authorize(twoFactorAuth.getSecret(), Integer.parseInt(request.getCode()));

        if (isCodeValid) {
            twoFactorAuth.setEnabled(true);
            twoFactorAuthRepository.save(twoFactorAuth);
            return new VerifyResponse("success");
        } else {
            return new VerifyResponse("failed");
        }
    }

    public DisableResponse disable(DisableRequest request) {
        Optional<TwoFactorAuth> optionalAuth = twoFactorAuthRepository.findById(request.getUserId());

        if (optionalAuth.isEmpty()) {
            return new DisableResponse("failed");
        }

        TwoFactorAuth twoFactorAuth = optionalAuth.get();

        // Проверяем код перед отключением
        boolean isCodeValid = googleAuthenticator.authorize(twoFactorAuth.getSecret(), Integer.parseInt(request.getCode()));

        if (isCodeValid) {
            twoFactorAuth.setEnabled(false);
            twoFactorAuthRepository.save(twoFactorAuth);
            return new DisableResponse("disabled");
        } else {
            return new DisableResponse("failed");
        }
    }
}