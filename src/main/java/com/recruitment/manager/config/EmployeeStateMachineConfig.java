package com.recruitment.manager.config;


import com.recruitment.manager.enums.EmployeeEvents;
import com.recruitment.manager.enums.EmployeeStates;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import static com.recruitment.manager.Parameters.EMPLOYEE_ID_HEADER;


/**
 * Created by Chaklader on Feb, 2021
 */
@EnableStateMachineFactory
@Configuration
@Slf4j
public class EmployeeStateMachineConfig extends StateMachineConfigurerAdapter<EmployeeStates, EmployeeEvents> {


    /**
     * define the state change for the finite state machine based on the events
     *
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<EmployeeStates, EmployeeEvents> transitions) throws Exception {

        transitions

            .withExternal()
            .source(EmployeeStates.ADDED).target(EmployeeStates.IN_CHECK).event(EmployeeEvents.CHECK)

            .and()
            .withExternal()
            .source(EmployeeStates.IN_CHECK).target(EmployeeStates.APPROVED).event(EmployeeEvents.APPROVE)

            .and()
            .withExternal()
            .source(EmployeeStates.APPROVED).target(EmployeeStates.ACTIVE).event(EmployeeEvents.ACTIVE);

    }

    /**
     * define the states (ADDED, IN_CHECK, APPROVE, ACTIVE) for the finite state machine
     *
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<EmployeeStates, EmployeeEvents> states) throws Exception {

        states.withStates()
            .initial(EmployeeStates.ADDED)
            .stateEntry(EmployeeStates.ADDED, context -> {

                Long employeeId = (Long) context.getExtendedState().getVariables().getOrDefault(EMPLOYEE_ID_HEADER, -1L);

                log.info("Employee with id: " + employeeId + " is included to the platform with the ADDED state");
            })

            .state(EmployeeStates.IN_CHECK)
            .state(EmployeeStates.APPROVED)

            .end(EmployeeStates.ACTIVE);
    }

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        return modelMapper;
    }
}
