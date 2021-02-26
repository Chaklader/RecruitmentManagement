package com.recruitment.manager.service;

import com.recruitment.manager.entity.Employee;
import com.recruitment.manager.entity.EmployeeDto;
import com.recruitment.manager.enums.EmployeeEvents;
import com.recruitment.manager.enums.EmployeeStates;
import com.recruitment.manager.repo.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public static final String EMPLOYEE_ID_HEADER = "employeeId";

    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private final StateMachineFactory<EmployeeStates, EmployeeEvents> factory;


    public EmployeeService(EmployeeRepository employeeRepository, StateMachineFactory<EmployeeStates, EmployeeEvents> machineFactory) {

        this.employeeRepository = employeeRepository;

        this.factory = machineFactory;
    }

    public Employee createEmployee(EmployeeDto employeeDto) {

        try {

            Employee newEmployee = modelMapper.map(employeeDto, Employee.class);

            if (newEmployee != null) {

                newEmployee.setState(EmployeeStates.ADDED);
                newEmployee.setCreationOn(new Date());

                return this.employeeRepository.save(newEmployee);

            }
        } catch (Exception ex) {
            log.error("Error occurred while create new Plan ::" + ex.getMessage());
        }

        return null;
    }

//    public Employee createEmployee(String firstName, String lastName, String email) {
//
//        Employee em = new Employee(firstName, lastName, email);
//        Employee employee = this.employeeRepository.save(em);
//
//        return employee;
//    }

    public Optional<Employee> findById(Long id) {

        Optional<Employee> user = employeeRepository.findById(id);
        return user;
    }

    public List<Employee> findAll() {

        return (List<Employee>) employeeRepository.findAll();
    }

//    public Employee findEmployeeById(Long id) {
//
//        Employee employee = employeeRepository.findById(id).orElse(null);
//
//        return employee;
//    }

    public Employee check(Long employeeId, String paymentConfirmationNumber) {

        Map<Employee, StateMachine<EmployeeStates, EmployeeEvents>> stateMachineMap = this.build(employeeId);

        Map.Entry<Employee, StateMachine<EmployeeStates, EmployeeEvents>> machineEntry = stateMachineMap.entrySet().iterator().next();


        Employee emplo = machineEntry.getKey();

        if (emplo.getState() == EmployeeStates.IN_CHECK) {

            return emplo;
        }

        log.info("state = " + emplo.getState());

        StateMachine<EmployeeStates, EmployeeEvents> sm = machineEntry.getValue();


        Message<EmployeeEvents> checkMessage = MessageBuilder.withPayload(EmployeeEvents.CHECK).setHeader(EMPLOYEE_ID_HEADER, employeeId).setHeader("paymentConfirmationNumber", paymentConfirmationNumber).build();
        sm.sendEvent(checkMessage);

//        Employee emplo = machineEntry.getKey();

        log.info("state = " + emplo.getState());

        return emplo;
    }

//    public StateMachine<EmployeeStates, EmployeeEvents> approve(Long orderId) {
//
//        StateMachine<EmployeeStates, EmployeeEvents> sm = this.build(orderId);
//        Message<EmployeeEvents> fulFilmentMessage = MessageBuilder.withPayload(EmployeeEvents.APPROVE).setHeader(EMPLOYEE_ID_HEADER, orderId).build();
//
//        sm.sendEvent(fulFilmentMessage);
//
//        return sm;
//    }

//    public StateMachine<EmployeeStates, EmployeeEvents> active(Long employeeId) {
//
//        StateMachine<EmployeeStates, EmployeeEvents> sm = this.build(employeeId);
//        Message<EmployeeEvents> activeMessage = MessageBuilder.withPayload(EmployeeEvents.ACTIVE).setHeader(EMPLOYEE_ID_HEADER, employeeId).build();
//
//        sm.sendEvent(activeMessage);
//
//        return sm;
//    }

    /**
     * @param orderId
     * @return
     */
//    private StateMachine<EmployeeStates, EmployeeEvents> build(Long orderId) {
    private Map<Employee, StateMachine<EmployeeStates, EmployeeEvents>> build(Long orderId) {

        Employee employee = this.employeeRepository.findById(orderId).orElse(null);
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
                                    (Long) msg.getHeaders().getOrDefault(EMPLOYEE_ID_HEADER, -1L))).ifPresent(

                                employeeId -> {

                                    Employee emp = employeeRepository.findById(employeeId).orElse(null);
                                    Objects.requireNonNull(emp).setState(states.getId());
                                    employeeRepository.save(emp);
                                });

                        }
                    });

                    EmployeeStates employeeState = employee.getState();

                    DefaultStateMachineContext<EmployeeStates, EmployeeEvents> smc = new DefaultStateMachineContext<>(employeeState,
                        null,
                        null,
                        null);

                    sma.resetStateMachine(smc);
                }
            });

        stateMachine.start();

        Map<Employee, StateMachine<EmployeeStates, EmployeeEvents>> stateMachineMap = new LinkedHashMap<>();
        stateMachineMap.putIfAbsent(employee, stateMachine);

        return stateMachineMap;
    }
}
