package com.recruitment.manager.service;

import com.recruitment.manager.dto.EmployeeDto;
import com.recruitment.manager.entity.Address;
import com.recruitment.manager.entity.Employee;
import com.recruitment.manager.statemachine.enums.EmployeeStates;
import com.recruitment.manager.repo.EmployeeRepository;
import com.recruitment.manager.util.MessageConstant;
import com.recruitment.manager.statemachine.enums.EmployeeEvents;
import com.recruitment.manager.repo.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.access.StateMachineFunction;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by Chaklader on Feb, 2021
 */
@Slf4j
@Service
public class EmployeeService {


    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    private final AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private final StateMachineFactory<EmployeeStates, EmployeeEvents> factory;


    public EmployeeService(EmployeeRepository employeeRepository, AddressRepository addressRepository, StateMachineFactory<EmployeeStates, EmployeeEvents> machineFactory) {

        this.employeeRepository = employeeRepository;
        this.addressRepository = addressRepository;

        this.factory = machineFactory;
    }


    /**
     * this method will create a new employee with ADDED state and the current date
     *
     * @param employeeDto
     * @return
     */
    public Employee createEmployee(EmployeeDto employeeDto) {


        try {
            Employee newEmployee = modelMapper.map(employeeDto, Employee.class);

            Address address = newEmployee.getAddress();

            if (address == null) {

                log.info("we are unable to acquire the address info for the employee.");
                return null;
            }

            this.addressRepository.save(address);

            newEmployee.setEmployeeState(EmployeeStates.ADDED);
            newEmployee.setCreationOn(new Date());

            Employee employee = this.employeeRepository.save(newEmployee);

            return employee;

        } catch (Exception ex) {

            log.error("Error occurred while creating a new employee. Error ::" + ex.getMessage());
        }

        return null;
    }

    /**
     * this method will change the employee state from the ADDED to the IN_CHECK
     *
     * @param employeeId
     * @param inCheckStateChangeMessage
     * @return
     */
    public Employee changeToInCheckState(Long employeeId, String inCheckStateChangeMessage) {

        System.out.println("Hello");
        Pair<Employee, StateMachine<EmployeeStates, EmployeeEvents>> stateMachinePair = this.build(employeeId);

        Employee employee = stateMachinePair.getFirst();


        if (employee.getEmployeeState() != EmployeeStates.ADDED) {

            log.info("The employee state is not ADDED. We can only change to the employee state IN_CHECK from the ADDED!");

            return employee;
        }

        StateMachine<EmployeeStates, EmployeeEvents> sm = stateMachinePair.getSecond();

        Message<EmployeeEvents> inCheckMessage = MessageBuilder.withPayload(EmployeeEvents.CHECK).setHeader(MessageConstant.EMPLOYEE_ID_HEADER, employeeId).setHeader("IN_CHECK_CHANGE_MESSAGE", inCheckStateChangeMessage).build();
        sm.sendEvent(inCheckMessage);

        return employee;
    }

    public Pair<Employee, StateMachine<EmployeeStates, EmployeeEvents>> build(Long employeeId) {

        Employee employee = this.employeeRepository.findById(employeeId).orElse(null);
        String employeeIdKey = Long.toString(Objects.requireNonNull(employee).getId());

        StateMachine<EmployeeStates, EmployeeEvents> stateMachine = this.factory.getStateMachine(employeeIdKey);
        stateMachine.stop();

        stateMachine.getStateMachineAccessor()
            .doWithAllRegions(new StateMachineFunction<StateMachineAccess<EmployeeStates, EmployeeEvents>>() {

                @Override
                public void apply(StateMachineAccess<EmployeeStates, EmployeeEvents> sma) {

                    sma.addStateMachineInterceptor(new StateMachineInterceptorAdapter<>() {

                        @Override
                        public void preStateChange(

                            State<EmployeeStates, EmployeeEvents> states,
                            Message<EmployeeEvents> messages, Transition<EmployeeStates, EmployeeEvents> transitions,
                            StateMachine<EmployeeStates, EmployeeEvents> stateMachine,
                            StateMachine<EmployeeStates, EmployeeEvents> rootStateMachine

                        ) {
                            Optional.ofNullable(messages).flatMap(

                                msg -> Optional.ofNullable(
                                    (Long) msg.getHeaders().getOrDefault(MessageConstant.EMPLOYEE_ID_HEADER, -1L))).ifPresent(

                                employeeId -> {

                                    Employee emp = employeeRepository.findById(employeeId).orElse(null);
                                    Objects.requireNonNull(emp).setEmployeeState(states.getId());

                                    employeeRepository.save(emp);
                                });

                        }
                    });

                    EmployeeStates employeeState = employee.getEmployeeState();

                    DefaultStateMachineContext<EmployeeStates, EmployeeEvents> smc = new DefaultStateMachineContext<>(employeeState,
                        null,
                        null,
                        null);

                    sma.resetStateMachine(smc);
                }
            });

        stateMachine.start();

        return Pair.of(employee, stateMachine);
    }
}
