package com.minel.app.EmployeeRecordApplication.Repo;

import com.minel.app.EmployeeRecordApplication.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByIncomeGreaterThanAndStartDateGreaterThan(BigDecimal income, Date startDate);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.officeLocation = :officeLocation WHERE e.department = :department")
    void updateAddress(@Param("department") String department, @Param("officeLocation") String officeLocation);
}
