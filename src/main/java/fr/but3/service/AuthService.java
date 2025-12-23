package fr.but3.service;

import fr.but3.model.Principal;
import fr.but3.model.User;
import fr.but3.repository.UserRepository;
import fr.but3.utils.MD5Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    public static class AuthException extends RuntimeException {
        private final String code;
        public AuthException(String code) {
            super(code);
            this.code = code;
        }
        public String getCode() { return code; }
    }

    private final UserRepository userRepository;
    private final MailService mailService;

    public AuthService(UserRepository userRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    public Principal authenticate(String name, String password) {

        if (name == null || password == null || name.isBlank() || password.isBlank()) {
            throw new AuthException("missing");
        }

        String hash = MD5Util.md5(password);

        User user = userRepository.findByNameAndPwd(name, hash).orElse(null);
        if (user == null) {
            throw new AuthException("badcreds");
        }

        return new Principal(user.getId(), user.getName(), user.getRole());
    }

    @Transactional
    public Principal register(String name, String email, String password, String confirm) {

        if (name == null || email == null || password == null || confirm == null ||
                name.isBlank() || email.isBlank() || password.isBlank() || confirm.isBlank()) {
            throw new AuthException("missing");
        }

        if (!password.equals(confirm)) {
            throw new AuthException("nomatch");
        }

        if (userRepository.existsByName(name) || userRepository.existsByEmail(email)) {
            throw new AuthException("exists");
        }

        String hash = MD5Util.md5(password);
        User user = userRepository.save(new User(name, email, hash, "USER"));

        mailService.sendSafe(
                user.getEmail(),
                "Bienvenue sur BookTonDJ",
                "Bonjour " + user.getName() + ",\n\nVotre compte a bien été créé."
        );

        return new Principal(user.getId(), user.getName(), user.getRole());
    }
}