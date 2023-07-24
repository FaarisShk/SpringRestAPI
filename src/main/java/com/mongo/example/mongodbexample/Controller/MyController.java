package com.mongo.example.mongodbexample.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mongo.example.mongodbexample.Models.Employee;
import com.mongo.example.mongodbexample.Service.EmployeeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class MyController {

    @Autowired
    private EmployeeService employeeService;
    

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Optional<Employee> getEmployeeById(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee newEmployee) {
        Employee savedEmployee = employeeService.addEmployee(newEmployee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public void updateEmployee(@PathVariable Integer id, @RequestBody Employee updatedEmployee) {
        employeeService.updateEmployee(id, updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
    }

    @GetMapping("/all")
    public Page<Employee> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return employeeService.getAllEmployeesWithPaginationAndSorting(pageable);
    }

    //http://localhost:4557/employees/all?page=2&size=5&sortBy=name where name,page,size can be varied  

    @GetMapping("/{id}/manager")
    public Employee getNthLevelManager(@PathVariable Integer id, @RequestParam int level) {
        return employeeService.getNthLevelManager(id, level);
    }

    // http://localhost:4557/employees/6,11,16-or any /manager?level=2 To check level 2
    // http://localhost:4557/employees/2-20/manager?level=1 To check level 1 

    

}
