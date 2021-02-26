package com.recruitment.manager.config;

import com.recruitment.manager.enums.EmployeeEvents;
import com.recruitment.manager.enums.EmployeeStates;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;


/**
 * Created by Chaklader on Feb, 2021
 */

@EnableStateMachineFactory
@Configuration
@Log
public class EmployeeStateMachineConfig extends StateMachineConfigurerAdapter<EmployeeStates, EmployeeEvents> {


    private static final String EMPLOYEE_ID = "employeeId";


    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        return modelMapper;
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<EmployeeStates, EmployeeEvents> transitions) throws Exception {

        transitions

            .withExternal().source(EmployeeStates.ADDED).target(EmployeeStates.IN_CHECK).event(EmployeeEvents.CHECK)

            .and()
            .withExternal().source(EmployeeStates.IN_CHECK).target(EmployeeStates.APPROVED).event(EmployeeEvents.APPROVE)

            .and()
            .withExternal().source(EmployeeStates.APPROVED).target(EmployeeStates.ACTIVE).event(EmployeeEvents.ACTIVE);

    }

    @Override
    public void configure(StateMachineStateConfigurer<EmployeeStates, EmployeeEvents> states) throws Exception {

        states.withStates()
            .initial(EmployeeStates.ADDED)
            .stateEntry(EmployeeStates.ADDED, context -> {

                Long employeeId = (Long) context.getExtendedState().getVariables().getOrDefault(EMPLOYEE_ID, -1L);

                log.info("Employee with id: " + employeeId + " is added to the platform.");
                log.info("entering ADDED state.");

            })

            .state(EmployeeStates.IN_CHECK)
            .state(EmployeeStates.APPROVED)

            .end(EmployeeStates.ACTIVE);
    }

    @Bean
    public ModelMapper makeModelMapper() {

        return new ModelMapper();
    }
}
