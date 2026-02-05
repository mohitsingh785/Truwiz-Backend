package org.Jtech.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
/**
 * Email Service
 *
 * Purpose:
 * Provides email-related functionality for the application,
 * primarily for sending OTP and notification emails.
 *
 * Scope:
 * - Sending OTP emails for authentication workflows
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * This service acts as a wrapper over the configured
 * JavaMailSender and should not contain business logic.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    /**
     * Send an OTP email to the specified recipient.
     *
     * @param toEmail recipient email address
     * @param subject email subject
     * @param body email body content
     */
    public void sendOtpEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("detoxify402@gmail.com"); // Replace with your email

        mailSender.send(message);
    }
}
