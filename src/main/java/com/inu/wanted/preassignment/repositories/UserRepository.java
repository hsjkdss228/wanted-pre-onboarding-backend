package com.inu.wanted.preassignment.repositories;

import com.inu.wanted.preassignment.models.user.User;
import com.inu.wanted.preassignment.models.user.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UserId> {

}
