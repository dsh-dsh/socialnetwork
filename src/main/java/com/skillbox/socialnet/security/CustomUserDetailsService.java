package com.skillbox.socialnet.security;

import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetails setUserDetails(Person person) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ADMIN");
        return CustomUserDetails.builder()
                .userName(person.getEMail())
                .password(person.getPassword())
                .authorities(authorities)
                .build();
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = userService.getPersonByEmail(username);
        // TODO PasswordEncoder
        return person == null ? null : setUserDetails(person);
    }
}
