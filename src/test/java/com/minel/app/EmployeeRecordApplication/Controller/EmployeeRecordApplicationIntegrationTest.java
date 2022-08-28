package com.minel.app.EmployeeRecordApplication.Controller;

import com.minel.app.EmployeeRecordApplication.Model.Employee;
import com.minel.app.EmployeeRecordApplication.Model.RequestUpdateOfficeLocation;
import com.minel.app.EmployeeRecordApplication.Repo.EmployeeRepository;
import com.minel.app.EmployeeRecordApplication.Repo.MonthlyPrizeDrawResultRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeRecordApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    MonthlyPrizeDrawResultRepository monthlyPrizeDrawResultRepository;

    @Test
    @Order(1)
    void testSaveEmployee() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Test");
        employee.setLastName("Person");
        employee.setOccupation("Dev");
        employee.setIncome(BigDecimal.valueOf(1111));
        employee.setStartDate(new Date());
        employee.setDepartment("TST");
        employee.setOfficeLocation("T");

        HttpEntity<Employee> request = new HttpEntity<>(employee);
        testRestTemplate.postForEntity("/saveEmployee", request, Employee.class);

        assertNotNull(employeeRepository.findById(1L));
    }

    @Test
    @Order(2)
    void getEmployees() {
        ResponseEntity<Employee[]> response =
                testRestTemplate.getForEntity("/getEmployees",Employee[].class);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    @Test
    @Order(3)
    public void testGetEmployee() {
        ResponseEntity<Employee> response =
                testRestTemplate.getForEntity("/getEmployee/1", Employee.class);
        assertThat(response.getBody()).isNotNull();
        assertEquals("Test", response.getBody().getFirstName());
    }

    @Test
    @Order(4)
    public void testUpdateEmployee() {
        Employee employee = employeeRepository.findById(1L).get();
        employee.setIncome(BigDecimal.valueOf(999999));

        HttpEntity<Employee> request = new HttpEntity<>(employee);
        testRestTemplate.exchange("/updateEmployee", HttpMethod.PUT, request, Employee.class);
        assertNotEquals(BigDecimal.valueOf(1111), employeeRepository.findById(1L).get().getIncome());
    }

    @Test
    @Order(5)
    public void testUpdateOfficeLocation() {
        RequestUpdateOfficeLocation requestUpdateOfficeLocation = new RequestUpdateOfficeLocation();
        requestUpdateOfficeLocation.setOfficeLocation("TT");
        requestUpdateOfficeLocation.setDepartment("TST");

        HttpEntity<RequestUpdateOfficeLocation> request = new HttpEntity<>(requestUpdateOfficeLocation);
        testRestTemplate.exchange("/updateOfficeLocation", HttpMethod.PUT, request, Employee.class);
        assertNotEquals("T", employeeRepository.findById(1L).get().getOfficeLocation());
    }

    @Test
    @Order(6)
    public void testGetWinner() {
        ResponseEntity<Employee> response =
                testRestTemplate.getForEntity("/getWinner", Employee.class);

        assertEquals(response.getBody().getId(), monthlyPrizeDrawResultRepository.findFirstByOrderByDrawDateDesc().getEmployee().getId());
    }

    @Test
    @Order(7)
    public void testDeleteEmployee() {
        testRestTemplate.delete("/deleteEmployee/1");
        assertThat(employeeRepository.existsById(1L)).isFalse();
    }


}