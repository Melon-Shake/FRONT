package com.example.melon_shake_webapp.Repository;

import com.example.melon_shake_webapp.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);
}
