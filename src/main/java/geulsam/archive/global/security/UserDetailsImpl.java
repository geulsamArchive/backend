package geulsam.archive.global.security;

import geulsam.archive.domain.user.entity.Level;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * UserDetails 객체의 구현
 */
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Integer userId;
    private String password;
    private Level level;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + level.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Integer getUserId(){
        return userId;
    }

    /** 사용불가*/
    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
