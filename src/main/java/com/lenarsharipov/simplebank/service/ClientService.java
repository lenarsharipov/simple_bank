package com.lenarsharipov.simplebank.service;

import com.lenarsharipov.simplebank.dto.account.TransferDto;
import com.lenarsharipov.simplebank.dto.client.CreateClientDto;
import com.lenarsharipov.simplebank.dto.client.CreatedClientDto;
import com.lenarsharipov.simplebank.dto.client.PageClientDto;
import com.lenarsharipov.simplebank.dto.email.CreateEmailDto;
import com.lenarsharipov.simplebank.dto.email.CreatedEmailDto;
import com.lenarsharipov.simplebank.dto.filter.ClientFiltersDto;
import com.lenarsharipov.simplebank.dto.phone.CreatePhoneDto;
import com.lenarsharipov.simplebank.dto.phone.CreatedPhoneDto;
import com.lenarsharipov.simplebank.exception.IllegalUserStateException;
import com.lenarsharipov.simplebank.exception.InsufficientFundsException;
import com.lenarsharipov.simplebank.exception.ResourceNotFoundException;
import com.lenarsharipov.simplebank.exception.UnableToTransferException;
import com.lenarsharipov.simplebank.mapper.ClientServiceMapper;
import com.lenarsharipov.simplebank.model.*;
import com.lenarsharipov.simplebank.repository.ClientRepository;
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
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ClientService {

    public static final String INCREASE_PERCENTAGE_STEP = "1.05";
    public static final String MAX_VALUE_IN_SCHEDULED_INCREASE = "3.07";
    public static final int MAX_TRANSFER_ATTEMPTS = 20;

    private final ClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;

    private final ClientServiceMapper mapper;

    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void increaseBalances() {
        List<Client> clients = clientRepository.findAll();
        for (var user : clients) {
            increaseUserBalance(user);
        }
    }

    private void increaseUserBalance(Client client) {
        BigDecimal maxPossibleBalance = client.getAccount()
                .getInitialDeposit()
                .multiply(new BigDecimal(MAX_VALUE_IN_SCHEDULED_INCREASE));
        BigDecimal currBalance = client.getAccount().getBalance();
        BigDecimal increased = currBalance.multiply(new BigDecimal(INCREASE_PERCENTAGE_STEP));

        boolean isUpdated = false;
        int attempts = 0;
        if (currBalance.compareTo(maxPossibleBalance) < 0) {
            while (!isUpdated) {
                try {
                    currBalance = increased.compareTo(maxPossibleBalance) > 0
                            ? maxPossibleBalance
                            : increased;
                    client.getAccount().setBalance(currBalance);
                    isUpdated = true;
                } catch (ObjectOptimisticLockingFailureException e) {
                    attempts++;
                    if (attempts == MAX_TRANSFER_ATTEMPTS) {
                        throw new UnableToTransferException(
                                "Unable to proceed transfer operation");
                    }
                    client = getById(client.getId());
                }
            }
        }
    }

    @Transactional
    public void transfer(Long senderUserId, TransferDto dto) {
        int attempts = 0;
        boolean isUpdated = false;
        Client sender = getById(senderUserId);
        Client receiver = getById(dto.receiverUserId());
        if (sender.getAccount().getBalance().compareTo(dto.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        while (!isUpdated) {
            try {
                decreaseBalance(sender, dto.amount());
                increaseBalance(receiver, dto.amount());
                isUpdated = true;
            } catch (ObjectOptimisticLockingFailureException e) {
                attempts++;
                if (attempts == MAX_TRANSFER_ATTEMPTS) {
                    throw new UnableToTransferException(
                            "Unable to proceed transfer operation");
                }
                sender = getById(sender.getId());
                receiver = getById(receiver.getId());
            }
        }
    }

    private void increaseBalance(Client client, BigDecimal amount) {
        BigDecimal balance = client.getAccount().getBalance();
        balance = balance.add(amount);
        client.getAccount().setBalance(balance);
    }

    private void decreaseBalance(Client client, BigDecimal amount) {
        BigDecimal balance = client.getAccount().getBalance();
        balance = balance.subtract(amount);
        client.getAccount().setBalance(balance);
    }

    private Client getById(Long userId) {
        return clientRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }

    @Transactional
    public CreatedClientDto create(CreateClientDto dto) {
        Account account = mapper.toAccountEntity(dto);
        User user = mapper.toUserEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        Phone phone = Phone.of(dto.phone());
        Client client = mapper.toClientEntity(dto, user, account, phone);
        clientRepository.save(client);
        return mapper.toCreatedClientDto(client, user);
    }

    @Transactional
    public CreatedPhoneDto addPhone(Long clientId, CreatePhoneDto dto) {
        Client client = getById(clientId);
        Phone phone = Phone.of(dto.phone());
        client.getPhones().put(phone.getExternalId(), phone);
        return mapper.toCreatedPhoneDto(phone);
    }

    @Transactional
    public CreatedPhoneDto updatePhone(Long clientId,
                                       String externalId,
                                       CreatePhoneDto dto) {
        Client client = getById(clientId);
        Phone phone = getPhone(client, externalId);
        phone.setNumber(dto.phone());
        return mapper.toCreatedPhoneDto(phone);
    }

    @Transactional
    public void deletePhone(Long clientId, String externalId) {
        Client client = getById(clientId);
        if (client.getPhones().size() == 1) {
            throw new IllegalUserStateException("Client must have at least 1 phone");
        }
        client.getPhones().remove(externalId);
    }

    private Phone getPhone(Client client, String externalId) {
        return Optional.ofNullable(client.getPhones().get(externalId))
                .orElseThrow(() -> new ResourceNotFoundException("Phone not found"));
    }

    public boolean isPhoneOwner(Long clientId, String externalId) {
        return getById(clientId).getPhones().containsKey(externalId);
    }

    @Transactional
    public CreatedEmailDto addEmail(Long clientId, CreateEmailDto dto) {
        Client client = getById(clientId);
        Email email = Email.of(null, dto.email());
        client.getEmails().add(email);
        return mapper.toCreatedEmailDto(email);
    }

    @Transactional
    public CreatedEmailDto updateEmail(Long clientId,
                                       Long emailId,
                                       CreateEmailDto dto) {
        Client client = getById(clientId);
        Email email = getEmail(emailId, client);
        email.setAddress(dto.email());
        return mapper.toCreatedEmailDto(email);
    }

    @Transactional
    public void deleteEmail(Long clientId, Long emailId) {
        Client client = getById(clientId);
        if (client.getEmails().size() == 1) {
            throw new IllegalUserStateException("Client must have at least 1 email");
        }
        Email email = getEmail(emailId, client);
        client.getEmails().remove(email);
    }

    private Email getEmail(Long emailId, Client client) {
        return client.getEmails().stream()
                .filter(email -> Objects.equals(email.getId(), emailId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));
    }

    public boolean isEmailOwner(Long clientId, Long emailId) {
        Client client = getById(clientId);
        return client.getEmails().stream()
                .anyMatch(email -> Objects.equals(email.getId(), emailId));
    }

    public boolean isUserOwner(Long clientId, Long userId) {
        var result = false;
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isPresent()) {
            result = Objects.equals(optionalClient.get().getUser().getId(), userId);
        }
        return result;
    }

    public PageClientDto search(ClientFiltersDto filters, Pageable pageable) {
        Specification<Client> specification = mapper.toClientSpecification(filters);
        Page<Client> userPage = clientRepository.findAll(specification, pageable);
        return mapper.toPageClientDto(userPage);
    }
}