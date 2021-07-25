package com.stee1ix.collegegrievancesystem;

public class Complaint {
    String subject, message, studentId, name, reply;

    public Complaint(String subject, String message, String reply, String studentId, String name) {
        this.subject = subject;
        this.message = message;
        this.reply = reply;
        this.studentId = studentId;
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return name;
    }

    public void setStudentName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
