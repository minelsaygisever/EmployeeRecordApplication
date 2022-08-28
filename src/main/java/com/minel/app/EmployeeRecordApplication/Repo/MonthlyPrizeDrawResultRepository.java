package com.minel.app.EmployeeRecordApplication.Repo;

import com.minel.app.EmployeeRecordApplication.Model.MonthlyPrizeDrawResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyPrizeDrawResultRepository extends JpaRepository<MonthlyPrizeDrawResult, Long> {

    MonthlyPrizeDrawResult findFirstByOrderByDrawDateDesc();
}
