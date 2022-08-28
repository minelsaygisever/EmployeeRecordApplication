package com.minel.app.EmployeeRecordApplication.Controller;

import com.minel.app.EmployeeRecordApplication.Model.MonthlyPrizeDrawResult;
import com.minel.app.EmployeeRecordApplication.Model.RequestGetEmployeesWithDateAndAmount;
import com.minel.app.EmployeeRecordApplication.Model.RequestUpdateOfficeLocation;
import com.minel.app.EmployeeRecordApplication.Model.Employee;
import com.minel.app.EmployeeRecordApplication.Repo.EmployeeRepository;
import com.minel.app.EmployeeRecordApplication.Repo.MonthlyPrizeDrawResultRepository;
import com.minel.app.EmployeeRecordApplication.Util.EmployeeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    MonthlyPrizeDrawResultRepository monthlyPrizeDrawResultRepository;

    // An endpoint that adds new employee (Create)
    @PostMapping(value = "/saveEmployee")
    public void saveEmployee(@RequestBody Employee employee) {
        employeeRepository.save(employee);
    }

    // An endpoint that returns all the employees (Read)
    @GetMapping(value = "/getEmployees")
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    // An endpoint that returns specific employee (Read)
    @GetMapping(value = "/getEmployee/{id}")
    public Employee getEmployee(@PathVariable long id) {
        if(employeeRepository.existsById(id)) {
            return employeeRepository.findById(id).get();
        } else {
            return null;
        }
    }

    // An endpoint that updates specific employee (Update)
    @PutMapping(value = "/updateEmployee/{id}")
    public void updateEmployee(@PathVariable long id, @RequestBody Employee employee) {
        if(employeeRepository.existsById(id)) {
            Employee updatedEmployee = employeeRepository.findById(id).get();
            updatedEmployee.setFirstName(employee.getFirstName());
            updatedEmployee.setLastName(employee.getLastName());
            updatedEmployee.setOccupation(employee.getOccupation());
            updatedEmployee.setIncome(employee.getIncome());
            updatedEmployee.setStartDate(employee.getStartDate());
            updatedEmployee.setDepartment(employee.getDepartment());
            updatedEmployee.setOfficeLocation(employee.getOfficeLocation());
            employeeRepository.save(updatedEmployee);
        }
    }

    // An endpoint that deletes specific employee (Delete)
    @DeleteMapping(value = "/deleteEmployee/{id}")
    public void deleteEmployee(@PathVariable long id) {
        if(employeeRepository.existsById(id)) {
            Employee deleteEmployee = employeeRepository.findById(id).get();
            employeeRepository.delete(deleteEmployee);
        }
    }

    // An endpoint that returns all the employees that started after a specific date
    // and their income is greater than specific amount.
    @PostMapping(value="/getEmployeesWithDateAndAmount")
    public List<Employee> getEmployeesWithDateAndAmount(@RequestBody RequestGetEmployeesWithDateAndAmount request) {
        return employeeRepository.
                findByIncomeGreaterThanAndStartDateGreaterThan(request.getAmount(), request.getStartDate());
    }

    // An endpoint that updates the office location of all the employees of a specific department.
    @PutMapping(value="/updateOfficeLocation")
    public void updateOfficeLocation(@RequestBody RequestUpdateOfficeLocation request) {
        employeeRepository.updateAddress(request.getDepartment(), request.getOfficeLocation());
    }


    // A service method that returns a random employee for a monthly prize draw every month at a specific time and
    // saves it to the database. With an endpoint to call the winner every month.

    // @Scheduled(cron = "*/30 * * * * *") // every 30 seconds - for testing
    @Scheduled(cron = "0 0 0 1 * ?") // at 00:00 on day-of-month 1
    public void monthlyPrizeDraw() {
        Employee winner = EmployeeUtil.getRandomEmployee(employeeRepository.findAll());
        if(Objects.nonNull(winner)) {
            MonthlyPrizeDrawResult monthlyPrizeDrawResult = new MonthlyPrizeDrawResult();
            monthlyPrizeDrawResult.setEmployee(winner);
            monthlyPrizeDrawResult.setDrawDate(new Date());
            monthlyPrizeDrawResultRepository.save(monthlyPrizeDrawResult);
        }
    }

    // An endpoint to call the winner of the month
    @GetMapping(value="/getWinner")
    public Employee getWinner() {
        MonthlyPrizeDrawResult lastDraw =  monthlyPrizeDrawResultRepository.findFirstByOrderByDrawDateDesc();
        if(Objects.nonNull(lastDraw)) {
            return lastDraw.getEmployee();
        } else {
            return null;
        }
    }
}