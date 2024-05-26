package com.lenarsharipov.simplebank.service;

import com.lenarsharipov.simplebank.dto.account.TransferDto;
import com.lenarsharipov.simplebank.dto.email.CreateEmailDto;
import com.lenarsharipov.simplebank.dto.email.CreatedEmailDto;
import com.lenarsharipov.simplebank.dto.filter.FiltersDto;
import com.lenarsharipov.simplebank.dto.phone.CreatePhoneDto;
import com.lenarsharipov.simplebank.dto.phone.CreatedPhoneDto;
import com.lenarsharipov.simplebank.dto.user.CreateUserDto;
import com.lenarsharipov.simplebank.dto.user.CreatedUserDto;
import com.lenarsharipov.simplebank.dto.user.PageUserDto;
import com.lenarsharipov.simplebank.exception.IllegalUserStateException;
import com.lenarsharipov.simplebank.exception.InsufficientFundsException;
import com.lenarsharipov.simplebank.exception.ResourceNotFoundException;
import com.lenarsharipov.simplebank.mapper.*;
import com.lenarsharipov.simplebank.model.Account;
import com.lenarsharipov.simplebank.model.Email;
import com.lenarsharipov.simplebank.model.Phone;
import com.lenarsharipov.simplebank.model.User;
import com.lenarsharipov.simplebank.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    public static final String INCREASE_PERCENTAGE_STEP = "1.05";
    public static final String MAX_VALUE_IN_SCHEDULED_INCREASE = "3.07";

    private final UserRepository userRepository;

    private final AccountService accountService;
    private final EmailService emailService;
    private final PhoneService phoneService;

    private final PasswordEncoder passwordEncoder;

    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void increaseBalances() {
        List<User> users = userRepository.findAll();
        for (var user : users) {
            boolean isUpdated =false;
            while (!isUpdated) {
                try {
                    BigDecimal maxBalance = user.getAccount()
                            .getInitialDeposit()
                            .multiply(new BigDecimal(MAX_VALUE_IN_SCHEDULED_INCREASE));
                    BigDecimal newBalance = user.getAccount()
                            .getBalance()
                            .multiply(new BigDecimal(INCREASE_PERCENTAGE_STEP));
                    if (newBalance.compareTo(maxBalance) > 0) {
                        newBalance = maxBalance;
                    }
                    user.getAccount().setBalance(newBalance);
                    userRepository.save(user);
                    isUpdated = true;
                } catch (ObjectOptimisticLockingFailureException e) {
                    user = getById(user.getId());
                }
            }
        }
    }

    @Transactional
    public void transfer(Long senderUserId,
                         TransferDto dto) {
        boolean isUpdated = false;
        User sender = getById(senderUserId);
        User receiver = getById(dto.receiverUserId());
        if (sender.getAccount().getBalance().compareTo(dto.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        while (!isUpdated) {
            try {
                sender.decreaseBalance(dto.amount());
                receiver.increaseBalance(dto.amount());
                userRepository.saveAll(List.of(sender, receiver));
                isUpdated = true;
            } catch (ObjectOptimisticLockingFailureException e) {
                sender = getById(sender.getId());
                receiver = getById(receiver.getId());
            }
        }
    }

    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));
    }

    @Transactional
    public CreatedUserDto create(CreateUserDto dto) {
        User user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        Account account = accountService.create(dto.getInitialBalance());
        user.setAccount(account);
        Email email = emailService.create(dto.getEmail());
        Phone phone = phoneService.create(dto.getPhone());
        user.addEmail(email);
        user.addPhone(phone);
        userRepository.save(user);
        return UserMapper.toCreatedUserDto(user);
    }

    @Transactional
    public CreatedPhoneDto addPhone(Long userId,
                                    CreatePhoneDto dto) {
        User user = getById(userId);
        Phone phone = phoneService.create(dto.getPhone());
        user.addPhone(phone);
        userRepository.save(user);
        return PhoneMapper.toDto(phone);
    }

    @Transactional
    public CreatedPhoneDto updatePhone(Long userId,
                                       Long phoneId,
                                       CreatePhoneDto dto) {
        User user = getById(userId);
        Phone phone = phoneService.update(phoneId, dto);
        user.updatePhone(phone);
        userRepository.save(user);
        return PhoneMapper.toDto(phone);
    }

    @Transactional
    public void deletePhone(Long userId,
                            Long phoneId) {
        User user = getById(userId);
        if (user.getPhones().size() == 1) {
            throw new IllegalUserStateException(
                    "User must have at least 1 phone");
        }
        user.deletePhone(phoneService.getById(phoneId));
        phoneService.delete(phoneId);
        userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));
    }

    @Transactional
    public CreatedEmailDto addEmail(Long userId,
                                    CreateEmailDto dto) {
        User user = getById(userId);
        Email email = emailService.create(dto.getEmail());
        user.addEmail(email);
        userRepository.save(user);
        return EmailMapper.toDto(email);
    }

    @Transactional
    public CreatedEmailDto updateEmail(Long userId,
                                       Long emailId,
                                       CreateEmailDto dto) {
        User user = getById(userId);
        Email email = emailService.update(emailId, dto);
        user.updateEmail(email);
        userRepository.save(user);
        return EmailMapper.toDto(email);
    }

    @Transactional
    public void deleteEmail(Long userId,
                            Long emailId) {
        User user = getById(userId);
        if (user.getEmails().size() == 1) {
            throw new IllegalUserStateException(
                    "User must have at least 1 email");
        }
        user.deleteEmail(emailService.getById(emailId));
        emailService.delete(emailId);
        userRepository.save(user);
    }

    public boolean isPhoneOwner(Long userId,
                                Long phoneId) {
        User user = getById(userId);
        Phone phone = phoneService.getById(phoneId);
        return user.hasPhone(phone);
    }

    public boolean isEmailOwner(Long userId,
                                Long emailId) {
        User user = getById(userId);
        Email email = emailService.getById(emailId);
        return user.hasEmail(email);
    }

    public PageUserDto search(FiltersDto filters,
                              Pageable pageable) {
        Specification<User> spec = UserSpecMapper.toSpecification(filters);
        Page<User> userPage = userRepository.findAll(spec, pageable);
        return UserPageMapper.toPageUserDto(userPage);
    }
}