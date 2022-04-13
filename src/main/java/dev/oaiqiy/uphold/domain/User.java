package dev.oaiqiy.uphold.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;
    private String password;

    private String nickname;
    private String headshot;
    private Boolean sex;
    private Integer age;


    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER,targetEntity = Authority.class)
    private List<Authority> authorities = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.MERGE,targetEntity = Gym.class)
    private List<Gym> collection =new ArrayList<>(10);

    public void addAuthority(Authority authority){
        authorities.add(authority);
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
        return phone;
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

    public User(String phone, String password, String nickname) {
        this.phone = phone;
        this.password = password;
        this.nickname = nickname;
    }
}
