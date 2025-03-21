package org.Government.JusticePlatform.security.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.Government.JusticePlatform.model.User;

import java.util.Collection;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JusticeDetails implements UserDetails {
    
    private Long id;
    private String email;
    private String password;
    private Collection<GrantedAuthority> authorities; 

   
    public static JusticeDetails buildUserDetails(User user) {
        
        return new JusticeDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                null 
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; 
    }

    @Override
    public String getPassword() {
        return password; 
    }

    @Override
    public String getUsername() {
        return email;  
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; 
    }

    @Override
    public boolean isEnabled() {
        return true;  
    }
}
