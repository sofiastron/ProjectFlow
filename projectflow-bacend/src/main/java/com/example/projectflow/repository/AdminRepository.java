package com.example.projectflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.projectflow.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {}
