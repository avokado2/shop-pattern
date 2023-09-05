package org.avokado2.sp.manager;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.avokado2.sp.dao.UserRepository;
import org.avokado2.sp.model.UserImpl;
import org.avokado2.sp.model.UserEntity;
import org.avokado2.sp.model.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserManager {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    public boolean registerUser(String login, String password) {
        List<UserEntity> userEntities = userRepository.findByLogin(login);

        if (userEntities.size() > 0){
            return false;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setLogin(login);
        userEntity.setPasswordHash(passwordEncoder.encode(password));
        userEntity.setRole(UserRole.Customer);
        userRepository.save(userEntity);
        return true;
    }

    public int getCurrentPlayerId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("unlogged");
        }
        Object details = authentication.getPrincipal();
        if (!(details instanceof UserImpl)) {
            throw  new RuntimeException("unlogged");
        }
        UserImpl playerUser = (UserImpl) details;
        return playerUser.getId();
    }

    public UserEntity getCurrentPlayer() {
        UserEntity currentPlayer = userRepository.findById(getCurrentPlayerId());
        return currentPlayer;
    }

}
