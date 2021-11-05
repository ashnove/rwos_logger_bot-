package com.rwos.rwos_logger.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String userName;
    @Column(nullable = false, length = 20)
    private String firstName;
    @Column(nullable = false, length = 20)
    private String lastName;
    private String dateLog;
    private String timeLog;
    private String status;

    public Users() {
        this.status = "Offline";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getDateLog() {
        return dateLog;
    }

    public void setDateLog(String dateLog) {
        this.dateLog = dateLog;
    }

    public String getTimeLog() {
        return timeLog;
    }

    public void setTimeLog(String timeLog) {
        this.timeLog = timeLog;
    }

    @Override
    public String toString() {
        return "Users [dateLog=" + dateLog + ", firstName=" + firstName + ", id=" + id + ", lastName=" + lastName
                + ", status=" + status + ", timeLog=" + timeLog + ", userName=" + userName + "]";
    }

}
