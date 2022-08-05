package com.tlrhv3.sec.Service;

import com.tlrhv3.sec.Entity.User;
import com.tlrhv3.sec.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

   private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("Username" + username + "Not found !"));
    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException("Username id " + id + "Not found !"));
    }
}
