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
import com.lenarsharipov.simplebank.exception.UnableToTransferException;
import com.lenarsharipov.simplebank.mapper.*;
import com.lenarsharipov.simplebank.model.Account;
import com.lenarsharipov.simplebank.model.Email;
import com.lenarsharipov.simplebank.model.Phone;
import com.lenarsharipov.simplebank.model.User;
import com.lenarsharipov.simplebank.repository.EmailRepository;
import com.lenarsharipov.simplebank.repository.PhoneRepository;
import com.lenarsharipov.simplebank.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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
import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    public static final String INCREASE_PERCENTAGE_STEP = "1.05";
    public static final String MAX_VALUE_IN_SCHEDULED_INCREASE = "3.07";
    public static final int MAX_TRANSFER_ATTEMPTS = 20;

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;

    private final PasswordEncoder passwordEncoder;

    @Scheduled(fixedRate = 60_000)
    @Transactional
    @SneakyThrows
    public void increaseBalances() {
        List<User> users = userRepository.findAll();
        for (var user : users) {
            BigDecimal maxPossibleBalance = user.getAccount()
                    .getInitialDeposit()
                    .multiply(new BigDecimal(MAX_VALUE_IN_SCHEDULED_INCREASE));
            BigDecimal currBalance = user.getAccount().getBalance();
            BigDecimal increased = currBalance
                    .multiply(new BigDecimal(INCREASE_PERCENTAGE_STEP));
            boolean isUpdated = false;
            int attempts = 0;
            if (currBalance.compareTo(maxPossibleBalance) < 0) {
                while (!isUpdated) {
                    try {
                        currBalance = increased.compareTo(maxPossibleBalance) > 0
                                ? maxPossibleBalance
                                : increased;
                        user.getAccount().setBalance(currBalance);
                        isUpdated = true;
                    } catch (ObjectOptimisticLockingFailureException e) {
                        if (attempts == MAX_TRANSFER_ATTEMPTS) {
                            throw new UnableToTransferException(
                                    "Unable to proceed transfer operation");
                        }
                        user = getById(user.getId());
                        attempts++;
                    }
                }
            }
        }
    }

    @Transactional
    @SneakyThrows
    public void transfer(Long senderUserId,
                         TransferDto dto) {
        int attempts = 0;
        boolean isUpdated = false;
        User sender = getById(senderUserId);
        User receiver = getById(dto.receiverUserId());
        if (sender.getAccount().getBalance().compareTo(dto.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        while (!isUpdated) {
            try {
                decreaseBalance(sender, dto.amount());
                increaseBalance(receiver, dto.amount());
                isUpdated = true;
            } catch (ObjectOptimisticLockingFailureException e) {
                if (attempts == MAX_TRANSFER_ATTEMPTS) {
                    throw new UnableToTransferException(
                            "Unable to proceed transfer operation");
                }
                sender = getById(sender.getId());
                receiver = getById(receiver.getId());
                attempts++;
            }
        }
    }

    private void increaseBalance(User user, BigDecimal amount) {
        BigDecimal balance = user.getAccount().getBalance();
        balance = balance.add(amount);
        user.getAccount().setBalance(balance);
    }

    private void decreaseBalance(User user, BigDecimal amount) {
        BigDecimal balance = user.getAccount().getBalance();
        balance = balance.subtract(amount);
        user.getAccount().setBalance(balance);
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
        Account account = Account.builder()
                .initialDeposit(dto.getInitialBalance())
                .balance(dto.getInitialBalance())
                .build();
        user.setAccount(account);
        Email email = new Email(null, dto.getEmail());
        Phone phone = new Phone(null, dto.getPhone());
        user.getEmails().add(email);
        user.getPhones().add(phone);
        userRepository.save(user);
        return UserMapper.toCreatedUserDto(user);
    }

    @Transactional
    public CreatedPhoneDto addPhone(Long userId,
                                    CreatePhoneDto dto) {
        User user = getById(userId);
        Phone phone = new Phone(null, dto.getPhone());
        user.getPhones().add(phone);
        return PhoneMapper.toDto(phone);
    }

    @Transactional
    public CreatedPhoneDto updatePhone(Long userId,
                                       Long phoneId,
                                       CreatePhoneDto dto) {
        User user = getById(userId);
        Phone phone = getPhone(phoneId, user);
        phone.setNumber(dto.getPhone());
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
        Phone phone = getPhone(phoneId, user);
        user.getPhones().remove(phone);
        phoneRepository.deleteById(phoneId);
    }

    private Phone getPhone(Long phoneId,
                           User user) {
        return user.getPhones().stream()
                .filter(p -> Objects.equals(p.getId(), phoneId))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Phone not found"));
    }

    public boolean isPhoneOwner(Long userId,
                                Long phoneId) {
        User user = getById(userId);
        return user.getPhones().stream()
                .anyMatch(p -> Objects.equals(p.getId(), phoneId));
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
        Email email = new Email(null, dto.getEmail());
        user.getEmails().add(email);
        return EmailMapper.toDto(email);
    }

    @Transactional
    public CreatedEmailDto updateEmail(Long userId,
                                       Long emailId,
                                       CreateEmailDto dto) {
        User user = getById(userId);
        Email email = getEmail(emailId, user);
        email.setAddress(dto.getEmail());
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
        Email email = getEmail(emailId, user);
        user.getEmails().remove(email);
        emailRepository.deleteById(emailId);
    }

    private Email getEmail(Long emailId,
                           User user) {
        return user.getEmails().stream()
                .filter(e -> Objects.equals(e.getId(), emailId))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Email not found"));
    }

    public boolean isEmailOwner(Long userId,
                                Long emailId) {
        User user = getById(userId);
        return user.getEmails().stream()
                .anyMatch(email -> Objects.equals(email.getId(), emailId));
    }

    public PageUserDto search(FiltersDto filters,
                              Pageable pageable) {
        Specification<User> spec = UserSpecMapper.toSpecification(filters);
        Page<User> userPage = userRepository.findAll(spec, pageable);
        return UserPageMapper.toPageUserDto(userPage);
    }
}