package com.inu.wanted.preassignment.repositories;

import com.inu.wanted.preassignment.models.company.Company;
import com.inu.wanted.preassignment.models.company.CompanyId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, CompanyId> {

}
