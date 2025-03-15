package org.Government.JusticePlatform.security.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.Government.JusticePlatform.model.User;
import org.Government.JusticePlatform.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class JusticeDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return JusticeDetails.buildUserDetails(user);
    }

}
