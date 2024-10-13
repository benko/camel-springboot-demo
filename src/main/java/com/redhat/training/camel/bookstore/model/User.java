package com.redhat.training.camel.bookstore.model;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

// skipFirstLine must only be used when reading the entire file
@CsvRecord(separator = ";") // , skipFirstLine = true)
public class User {
    @DataField(pos = 1)
    private int id;
    @DataField(pos = 2)
    private String accessCode;
    @DataField(pos = 3)
    private String recoveryCode;
    @DataField(pos = 4)
    private String firstName;
    @DataField(pos = 5)
    private String lastName;
    @DataField(pos = 6)
    private String department;
    @DataField(pos = 7)
    private String location;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getAccessCode() {
        return accessCode;
    }
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
    public String getRecoveryCode() {
        return recoveryCode;
    }
    public void setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String toString() {
        return "[User: id=" + id + ", name=" + firstName + " " + lastName + "]";
    }
}
