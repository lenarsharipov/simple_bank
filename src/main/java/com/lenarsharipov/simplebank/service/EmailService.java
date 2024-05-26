package com.lenarsharipov.simplebank.service;

import com.lenarsharipov.simplebank.dto.email.CreateEmailDto;
import com.lenarsharipov.simplebank.exception.ResourceNotFoundException;
import com.lenarsharipov.simplebank.mapper.EmailMapper;
import com.lenarsharipov.simplebank.model.Email;
import com.lenarsharipov.simplebank.repository.EmailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final EmailRepository emailRepository;

    @Transactional
    public Email create(String emailAddress) {
        return emailRepository.save(EmailMapper.toEntity(emailAddress));
    }

    @Transactional
    public Email update(Long emailId,
                        CreateEmailDto dto) {
        Email email = getById(emailId);
        email.setAddress(dto.getEmail());
        return emailRepository.save(email);
    }

    @Transactional
    public void delete(Long emailId) {
        emailRepository.deleteById(emailId);
    }

    public Email getById(Long emailId) {
        return emailRepository.findById(emailId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Email not found"));
    }
}
