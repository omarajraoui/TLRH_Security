package com.tlrhv3.sec.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

 @Entity
 @Data @AllArgsConstructor @NoArgsConstructor
public class User implements UserDetails {
         @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
         private Long id;
         @NonNull
         private String username ;
         @NonNull
         private String email ;
        @NonNull
        @JsonIgnore
        private String password;

     @OneToOne(fetch = FetchType.LAZY,mappedBy = "user")
     private RefreshToken refreshToken;

        //another problem with register
     public User(String username, String email, String encode) {
     }


     @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

     public void setId(Long id) {
         this.id = id;
     }

     public Long getId() {
         return id;
     }
 }
