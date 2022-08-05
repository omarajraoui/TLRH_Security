package com.tlrhv3.sec.Repository;

import com.tlrhv3.sec.Entity.RefreshToken;
import com.tlrhv3.sec.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

}
