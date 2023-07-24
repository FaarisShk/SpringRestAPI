package com.mongo.example.mongodbexample.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mongo.example.mongodbexample.Models.Employee;
import com.mongo.example.mongodbexample.repository.EmployeeRepository;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Integer id) {
        return employeeRepository.findById(id);
    }

    public Employee addEmployee(Employee newEmployee) {
        // Save the new employee to the database
        Employee savedEmployee = employeeRepository.save(newEmployee);

        // Send an email to the Level 1 manager
        sendEmailToManager(newEmployee);

        return savedEmployee;
    }

    private void sendEmailToManager(Employee newEmployee) {
        // Your email server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Your email account credentials
        final String username = "officialfaarisshk@gmail.com";
        final String password = "jadhdbtzcuupipon";

        // Create a custom Authenticator subclass to provide authentication
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        // Create a Session instance with the provided properties and authentication
        Session session = Session.getInstance(props, authenticator);

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("level1_manager@example.com"));
            message.setSubject("New Employee Added");
            message.setText(newEmployee.getName() + " will now work under you. City is " +
                    newEmployee.getCity() + " and email is " + newEmployee.getEmail());

            // Send the email
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(Integer id, Employee updatedEmployee) {
        Employee existingEmployee = employeeRepository.findById(id).orElse(null);
        if (existingEmployee != null) {
            existingEmployee.setName(updatedEmployee.getName());
            existingEmployee.setCity(updatedEmployee.getCity());
            existingEmployee.setEmail(updatedEmployee.getEmail());
            existingEmployee.setParentId(updatedEmployee.getParentId());
            employeeRepository.save(existingEmployee);
        }
    }

    public void deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
    }

    public Page<Employee> getAllEmployeesWithPaginationAndSorting(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public Employee getNthLevelManager(Integer employeeId, int level) {
        return findNthLevelManagerRecursive(employeeId, level, 0);
    }

    private Employee findNthLevelManagerRecursive(Integer employeeId, int targetLevel, int currentLevel) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            if (currentLevel == targetLevel) {
                return employee;
            } else {
                return findNthLevelManagerRecursive(employee.getParentId(), targetLevel, currentLevel + 1);
            }
        }
        return null;
    }
}

