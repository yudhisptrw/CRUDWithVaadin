package com.example.crud_with_vaadin.backend.repositories;

import com.example.crud_with_vaadin.backend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<AppUser, Long>
{
    List<AppUser> findUserByJob (String job);
    AppUser findById(long id);
}
