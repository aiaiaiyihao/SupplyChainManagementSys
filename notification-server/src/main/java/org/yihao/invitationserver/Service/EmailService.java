package org.yihao.invitationserver.Service;

public interface EmailService {
    boolean sendEmail(String to, String subject, String body);
}
