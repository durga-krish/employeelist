package com.durga.employeelist.controller;


import com.durga.employeelist.model.Employee;
import com.durga.employeelist.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        return employeeRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PostMapping
    public ResponseEntity createEmployee(@RequestBody Employee employee, BindingResult bindingResult) throws URISyntaxException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        Employee savedEmployee = employeeRepository.save(employee);
        ResponseEntity<Object> ResponseEntity = null;
        return ResponseEntity.created(new URI("/employees/" + savedEmployee.getId())).build();
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@RequestBody Employee employee, @PathVariable int id) {
        Employee currentEmployee = employeeRepository.findById(id).orElseThrow(RuntimeException::new);
        currentEmployee.setName(employee.getName());
        currentEmployee.setEmail(employee.getEmail());
        currentEmployee.setBirthDate(employee.getBirthDate());
        currentEmployee.setPhone(employee.getPhone());
        currentEmployee.setJobPosition(employee.getJobPosition());
        currentEmployee.setBio(employee.getBio());
        currentEmployee.setHiringDate(employee.getHiringDate());
        currentEmployee.setActive(employee.isActive());
        currentEmployee.setOfficeId(employee.getOfficeId());
        currentEmployee.setDepartmentId(employee.getDepartmentId());
        currentEmployee = employeeRepository.save(currentEmployee);

        return ResponseEntity.ok(currentEmployee).getBody();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable int id) {
        employeeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    }

