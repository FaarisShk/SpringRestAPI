package com.mongo.example.mongodbexample.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongo.example.mongodbexample.Models.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, Integer> {
    
}
