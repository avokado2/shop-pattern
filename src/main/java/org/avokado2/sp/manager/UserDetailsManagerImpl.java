package org.avokado2.sp.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.avokado2.sp.dao.UserRepository;
import org.avokado2.sp.model.UserImpl;
import org.avokado2.sp.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsManagerImpl implements UserDetailsManager {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static final String PLAYER_AUTH = "PLAYER";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserEntity> users = userRepository.findByLogin(username);
        if (users.size() == 0) {
            return null;
        }
        if (users.size() > 1) {
            throw  new RuntimeException("more than one user with this nickname was found");
        }
        UserEntity userEntity = users.get(0);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userEntity.getRole().name());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        UserImpl implUser = new UserImpl(userEntity.getLogin(), userEntity.getPasswordHash(), !userEntity.isBlocked(),
                true, true, !userEntity.isBlocked(), authorities, userEntity.getId());
        return implUser;
    }

    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        List<UserEntity> players = userRepository.findByLogin(username);

        return players.size() > 0;
    }
}
