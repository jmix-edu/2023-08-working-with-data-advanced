package com.company.jmixpm.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@JmixEntity
@Table(name = "EMPLOYEE_DETAILS")
@Entity
public class EmployeeDetails {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "PASSPOSRT_NUMBER")
    private String passposrtNumber;

    @Column(name = "EXPIRY_DATE")
    private LocalDate expiryDate;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "employeeDetails")
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPassposrtNumber() {
        return passposrtNumber;
    }

    public void setPassposrtNumber(String passposrtNumber) {
        this.passposrtNumber = passposrtNumber;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}