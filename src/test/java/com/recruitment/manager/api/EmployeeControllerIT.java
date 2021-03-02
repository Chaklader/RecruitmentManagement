package com.recruitment.manager.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment.manager.dto.AddressDto;
import com.recruitment.manager.dto.EmployeeDto;
import com.recruitment.manager.entity.Employee;
import com.recruitment.manager.enums.EmployeeStates;
import com.recruitment.manager.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Mock
    private EmployeeService employeeService;

    @Autowired
    private ModelMapper modelMapper;


    @Test
    public void givenEmployeeDto_CreateEmployeeWithAddedState() throws Exception {

        EmployeeDto employeeDto = createEmployeeDto();

        mockMvc.perform(post("/api/employee/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(employeeDto)))
            .andExpect(status().isCreated());

    }


    @Test
    public void givenEmployeeDto_CreateEmployee1WithAddedState() throws Exception {

        EmployeeDto employeeDto = createEmployeeDto();

        Employee em = modelMapper.map(employeeDto, Employee.class);

        when(employeeService.changeToInCheckState(1L, "")).thenReturn(em);

        mockMvc.perform(put("/api/employee//incheck/"+ 1L)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    }


    private EmployeeDto createEmployeeDto() {

        EmployeeDto employeeDto = new EmployeeDto();


        employeeDto.setFirstName("Chaklader");
        employeeDto.setLastName("Arefe");
        employeeDto.setEmail("omi.chaklader@gmail.com");
        employeeDto.setPhoneNumber("541-754-3010");

        AddressDto addressDto = new AddressDto();

        addressDto.setStreet("Zeltingerstr. 35");
        addressDto.setState("Berlin");
        addressDto.setCountry("Germany");
        addressDto.setZip(12345);

        employeeDto.setAddressDto(addressDto);

        return employeeDto;
    }


}