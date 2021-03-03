package com.recruitment.manager.config;


import com.recruitment.manager.statemachine.enums.EmployeeStates;
import com.recruitment.manager.statemachine.enums.EmployeeEvents;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import static com.recruitment.manager.util.MessageConstant.EMPLOYEE_ID_HEADER;


/**
 * Created by Chaklader on Feb, 2021
 */
@Slf4j
@Configuration
@EnableStateMachineFactory
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

    @Override
    public void configure(StateMachineConfigurationConfigurer<EmployeeStates, EmployeeEvents> config) throws Exception {

        StateMachineListenerAdapter<EmployeeStates, EmployeeEvents> adapter = new StateMachineListenerAdapter<>() {

            @Override
            public void stateChanged(State<EmployeeStates, EmployeeEvents> from, State<EmployeeStates, EmployeeEvents> to) {

                log.info(String.format("state changed  from:  %s  to:  %s", from.getId().name(), to.getId().name()));
            }
        };

        config.withConfiguration().autoStartup(false).listener(adapter);
    }

}
