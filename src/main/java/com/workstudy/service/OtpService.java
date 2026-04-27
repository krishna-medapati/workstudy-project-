package com.workstudy.service;

import com.workstudy.entity.Otp;
import com.workstudy.repository.OtpRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {
    private final OtpRepository otpRepository;
    private final JavaMailSender mailSender;

    public OtpService(OtpRepository otpRepository, JavaMailSender mailSender) {
        this.otpRepository = otpRepository;
        this.mailSender = mailSender;
    }

    @Transactional
    public void generateAndSendOtp(String email) {
        otpRepository.deleteByEmail(email);
        String code = String.format("%06d", new Random().nextInt(999999));
        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode(code);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Work-Study Login OTP");
        message.setText("Your OTP is: " + code + "\n\nValid for 5 minutes.\n\nWork-Study Management System");
        mailSender.send(message);
    }

    @Transactional
    public boolean verifyOtp(String email, String code) {
        return otpRepository.findTopByEmailOrderByIdDesc(email).map(otp -> {
            if (otp.getCode().equals(code) && otp.getExpiresAt().isAfter(LocalDateTime.now())) {
                otpRepository.deleteByEmail(email);
                return true;
            }
            return false;
        }).orElse(false);
    }
}
