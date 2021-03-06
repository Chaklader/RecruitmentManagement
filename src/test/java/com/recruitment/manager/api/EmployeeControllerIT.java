package com.recruitment.manager.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment.manager.dto.AddressDto;
import com.recruitment.manager.dto.EmployeeDto;
import com.recruitment.manager.entity.Employee;
import com.recruitment.manager.repo.AddressRepository;
import com.recruitment.manager.repo.EmployeeRepository;
import com.recruitment.manager.statemachine.enums.EmployeeStates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Chaklader on Mar, 2021
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerIT {


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Test
    public void givenEmployeeDto_CreateEmployeeWithAddedState() throws Exception {

        EmployeeDto employeeDto = createEmployeeDto();

        mockMvc.perform(post("/api/employee/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(employeeDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName", is("Chaklader")))
            .andExpect(jsonPath("$.lastName", is("Arefe")))
            .andExpect(jsonPath("$.age", is(23)))
            .andExpect(jsonPath("$.email", is("omi.chaklader@gmail.com")))
            .andExpect(jsonPath("$.phoneNumber", is("541-754-3010")))
            .andExpect(jsonPath("$.employeeState", is("ADDED")));

    }

    @Test
    public void givenEmployeeDto_Put() throws Exception {

        EmployeeDto employeeDto = createEmployeeDto();

        Employee employee = modelMapper.map(employeeDto, Employee.class);
        employee.setCreationOn(new Date());
        employee.setEmployeeState(EmployeeStates.ADDED);

        addressRepository.save(employee.getAddress());
        employeeRepository.save(employee);


        mockMvc.perform(put("/api/employee/incheck/" + 1L)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName", is("Chaklader")))
            .andExpect(jsonPath("$.lastName", is("Arefe")))
            .andExpect(jsonPath("$.age", is(23)))
            .andExpect(jsonPath("$.email", is("omi.chaklader@gmail.com")))
            .andExpect(jsonPath("$.phoneNumber", is("541-754-3010")))
            .andExpect(jsonPath("$.employeeState", is("IN_CHECK")));

    }

    private EmployeeDto createEmployeeDto() {

        EmployeeDto employeeDto = new EmployeeDto();


        employeeDto.setFirstName("Chaklader");
        employeeDto.setLastName("Arefe");
        employeeDto.setEmail("omi.chaklader@gmail.com");
        employeeDto.setPhoneNumber("541-754-3010");
        employeeDto.setAge(23);


        AddressDto addressDto = new AddressDto();

        addressDto.setStreet("Zeltingerstr. 35");
        addressDto.setState("Berlin");
        addressDto.setCountry("Germany");
        addressDto.setZip(12345);

        employeeDto.setAddressDto(addressDto);

        return employeeDto;
    }


}