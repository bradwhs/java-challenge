package jp.co.axa.apidemo;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.entities.Employee;
import org.mockito.Mockito;

import java.util.*;

@AutoConfigureMockMvc
@SpringBootTest
public class EmployeeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    EmployeeRepository employeeRepository;

    Employee EMPLOYEE_1 = new Employee("wong", 10000, "economics");
    Employee EMPLOYEE_2 = new Employee("kong", 20000, "pc");
    Employee EMPLOYEE_3 = new Employee("zong", 15000, "IT");

    // tests get request to get all employees
    @Test
    @WithMockUser(username = "sa", password = "123456", roles = "USER")
    public void getAllEmployee_success() throws Exception {
        List<Employee> employees = new ArrayList<>(Arrays.asList(EMPLOYEE_1, EMPLOYEE_2, EMPLOYEE_3));

        Mockito.when(employeeRepository.findAll()).thenReturn(employees);

        mockMvc.perform(MockMvcRequestBuilders
            .get("/api/v1/employees")
            .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[1].name", is("kong")));

    }

    // tests get request to retrieve employee by ID
    @Test
    @WithMockUser(username = "sa", password = "123456", roles = "USER")
    public void getEmployeeById_success() throws Exception {
        Mockito.when(employeeRepository.findById(1L)).thenReturn(java.util.Optional.of(EMPLOYEE_1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/employees/{employeeId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("wong")));
    }

    // tests post request to create employee
    @Test
    @WithMockUser(username = "sa", password = "123456", roles = "USER")
    public void createRecord_success() throws Exception {

        Mockito.when(employeeRepository.save(EMPLOYEE_1)).thenReturn(EMPLOYEE_1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(EMPLOYEE_1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andReturn();
    }

    // tests put request to update employee
    @Test
    @WithMockUser(username = "sa", password = "123456", roles = "USER")
    public void updateEmployee_success() throws Exception {
        Employee updEmployee = new Employee("hong", 20000, "economics");

        Mockito.when(employeeRepository.findById(EMPLOYEE_1.getId())).thenReturn(Optional.of(EMPLOYEE_1));
        Mockito.when(employeeRepository.save(updEmployee)).thenReturn(updEmployee);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/employees/{employeeId}", EMPLOYEE_1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updEmployee));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

}
