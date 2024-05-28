package com.lenarsharipov.simplebank.service;

import com.lenarsharipov.simplebank.dto.phone.CreatePhoneDto;
import com.lenarsharipov.simplebank.dto.phone.CreatedPhoneDto;
import com.lenarsharipov.simplebank.exception.ResourceNotFoundException;
import com.lenarsharipov.simplebank.mapper.PhoneMapper;
import com.lenarsharipov.simplebank.model.Phone;
import com.lenarsharipov.simplebank.repository.PhoneRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PhoneService {

    private final PhoneRepository phoneRepository;

    @Transactional
    public Phone create(String phone) {
        return phoneRepository.save(PhoneMapper.toEntity(phone));
    }

    @Transactional
    public CreatedPhoneDto update(Long phoneId,
                                  CreatePhoneDto dto) {
        Phone phone = getById(phoneId);
        phone.setNumber(dto.getPhone());
        return PhoneMapper.toDto(phone);
    }

    @Transactional
    public void delete(Long phoneId) {
        phoneRepository.deleteById(phoneId);
    }

    public Phone getById(Long phoneId) {
        return phoneRepository.findById(phoneId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Phone not found"));
    }
}