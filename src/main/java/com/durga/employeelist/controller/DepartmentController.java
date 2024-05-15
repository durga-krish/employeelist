package com.durga.employeelist.controller;

import com.durga.employeelist.model.Department;
import com.durga.employeelist.repository.DepartmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable int id) {
        return departmentRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) throws URISyntaxException {
        Department savedDepartment = departmentRepository.save(department);
        return ResponseEntity.created(new URI("/departments/" + savedDepartment.getId())).body(savedDepartment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@RequestBody Department department, @PathVariable int id) {
        Department currentDepartment = departmentRepository.findById(id).orElseThrow(RuntimeException::new);
        currentDepartment.setName(department.getName());
        currentDepartment.setActive(department.isActive());
        Department updatedDepartment = departmentRepository.save(currentDepartment);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable int id) {
        departmentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

