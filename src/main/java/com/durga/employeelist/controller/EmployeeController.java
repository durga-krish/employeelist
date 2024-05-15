package com.durga.employeelist.controller;

import com.durga.employeelist.model.Employee;
import com.durga.employeelist.repository.DepartmentRepository;
import com.durga.employeelist.repository.EmployeeRepository;
import com.durga.employeelist.repository.LocationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, LocationRepository locationRepository) {
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
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee, BindingResult bindingResult) throws URISyntaxException {
        logger.info("Creating employee: {}", employee);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            logger.error("Validation errors: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Employee savedEmployee = employeeRepository.save(employee);
            return ResponseEntity.created(new URI("/employees/" + savedEmployee.getId())).body(savedEmployee);
        } catch (Exception e) {
            logger.error("Error creating employee", e);
            return ResponseEntity.status(500).body("Error creating employee: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee, @PathVariable int id) {
        try {
            Employee currentEmployee = employeeRepository.findById(id).orElseThrow(RuntimeException::new);
            currentEmployee.setName(employee.getName());
            currentEmployee.setEmail(employee.getEmail());
            currentEmployee.setBirthDate(employee.getBirthDate());
            currentEmployee.setPhone(employee.getPhone());
            currentEmployee.setJobPosition(employee.getJobPosition());
            currentEmployee.setBio(employee.getBio());
            currentEmployee.setHiringDate(employee.getHiringDate());
            currentEmployee.setActive(employee.isActive());
            currentEmployee.setDepartment(employee.getDepartment());
            currentEmployee.setLocation(employee.getLocation());
            currentEmployee = employeeRepository.save(currentEmployee);
            return ResponseEntity.ok(currentEmployee);
        } catch (Exception e) {
            logger.error("Error updating employee", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable int id) {
        try {
            employeeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting employee", e);
            return ResponseEntity.status(500).body(null);
        }
    }
}
