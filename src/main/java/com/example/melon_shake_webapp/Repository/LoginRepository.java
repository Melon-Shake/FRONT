package com.example.melon_shake_webapp.Repository;

import com.example.melon_shake_webapp.data.LoginData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoginRepository extends JpaRepository<LoginData,Integer> {
    @Query("select password from user where email is dd")

}
