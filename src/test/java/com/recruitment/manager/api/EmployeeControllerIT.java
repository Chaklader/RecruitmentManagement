package com.recruitment.manager.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment.manager.dto.AddressDto;
import com.recruitment.manager.dto.EmployeeDto;
import com.recruitment.manager.entity.Employee;
import com.recruitment.manager.enums.EmployeeEvents;
import com.recruitment.manager.enums.EmployeeStates;
import com.recruitment.manager.repo.EmployeeRepository;
import com.recruitment.manager.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
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

    @Mock
    StateMachine<EmployeeStates, EmployeeEvents> stateMachine;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Test
    public void givenEmployeeDto_CreateEmployeeWithAddedState() throws Exception {

        EmployeeDto employeeDto = createEmployeeDto();

        ResultActions resultActions = mockMvc.perform(post("/api/employee/create")
                                                          .contentType(MediaType.APPLICATION_JSON)
                                                          .content(objectMapper.writeValueAsString(employeeDto)))
                                          .andExpect(status().isCreated());



//        mockMvc.perform(get("/api/castle/createarmy/"+ARMY_SIZE)
//                            .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.typeAndSizeMap.size()", is(2)))
//            .andExpect(jsonPath("$.totalSize", is(167)));

    }


    @Test
    public void givenEmployeeDto_CreateEmployee1WithAddedState() throws Exception {

        EmployeeDto employeeDto = createEmployeeDto();

        Employee em = modelMapper.map(employeeDto, Employee.class);


        when(employeeRepository.findById(any())).thenReturn(Optional.of(em));
        when(employeeService.build(any())).thenReturn(Pair.of(em, stateMachine));
        when(employeeService.changeToInCheckState(any(), any())).thenReturn(em);


        mockMvc.perform(put("/api/employee/incheck/" + 1L)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

//        mockMvc.perform(get("/api/castle/createarmy/"+ARMY_SIZE)
//                            .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.typeAndSizeMap.size()", is(2)))
//            .andExpect(jsonPath("$.totalSize", is(167)));
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