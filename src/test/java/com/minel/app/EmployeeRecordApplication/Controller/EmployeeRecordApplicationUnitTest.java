package com.minel.app.EmployeeRecordApplication.Controller;

import com.minel.app.EmployeeRecordApplication.Model.Employee;
import com.minel.app.EmployeeRecordApplication.Model.MonthlyPrizeDrawResult;
import com.minel.app.EmployeeRecordApplication.Repo.EmployeeRepository;
import com.minel.app.EmployeeRecordApplication.Repo.MonthlyPrizeDrawResultRepository;
import com.minel.app.EmployeeRecordApplication.Util.EmployeeUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeRecordApplicationUnitTest {

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
		employeeRepository.save(employee);

		assertNotNull(employeeRepository.findById(1L));
	}

	@Test
	@Order(2)
	void testGetEmployees() {
		List<Employee> employeeList = employeeRepository.findAll();

		assertThat(employeeList).size().isGreaterThan(0);
	}

	@Test
	@Order(3)
	public void testGetEmployee() {
		Employee employee = employeeRepository.findById(1L).get();
		assertEquals("Test", employee.getFirstName());
	}

	@Test
	@Order(4)
	public void testUpdateEmployee() {
		Employee employee = employeeRepository.findById(1L).get();
		employee.setIncome(BigDecimal.valueOf(999999));
		employeeRepository.save(employee);

		assertNotEquals(BigDecimal.valueOf(1111), employeeRepository.findById(1L).get().getIncome());
	}

	@Test
	@Order(5)
	public void testUpdateOfficeLocation() {
		employeeRepository.updateAddress("TST", "TT");
		assertNotEquals("T", employeeRepository.findById(1L).get().getOfficeLocation());
	}

	@Test
	@Order(6)
	public void testMonthlyPrizeDraw() {
		Employee winner = EmployeeUtil.getRandomEmployee(employeeRepository.findAll());
		MonthlyPrizeDrawResult monthlyPrizeDrawResult = new MonthlyPrizeDrawResult();
		monthlyPrizeDrawResult.setEmployee(winner);
		monthlyPrizeDrawResult.setDrawDate(new Date());
		monthlyPrizeDrawResultRepository.save(monthlyPrizeDrawResult);

		assertEquals(winner.getId(), monthlyPrizeDrawResultRepository.findFirstByOrderByDrawDateDesc().getEmployee().getId());
	}

	@Test
	@Order(7)
	public void testDeleteEmployee() {
		employeeRepository.deleteById(1L);
		assertThat(employeeRepository.existsById(1L)).isFalse();
	}
}
