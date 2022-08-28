package com.minel.app.EmployeeRecordApplication.Util;

import com.minel.app.EmployeeRecordApplication.Model.Employee;

import java.util.List;
import java.util.Random;

public class EmployeeUtil {

    public static Employee getRandomEmployee(List<Employee> employeeList) {
        Random randomNumber = new Random();
        if(employeeList.size() > 0) {
            return employeeList.get(randomNumber.nextInt(employeeList.size()));
        } else {
            return null;
        }
    }
}
