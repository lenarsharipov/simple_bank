package com.lenarsharipov.simplebank.service;

import com.lenarsharipov.simplebank.dto.account.TransferDto;
import com.lenarsharipov.simplebank.dto.client.CreateClientDto;
import com.lenarsharipov.simplebank.dto.client.CreatedClientDto;
import com.lenarsharipov.simplebank.dto.email.CreateEmailDto;
import com.lenarsharipov.simplebank.dto.filter.ClientFiltersDto;
import com.lenarsharipov.simplebank.dto.phone.CreatePhoneDto;
import com.lenarsharipov.simplebank.mapper.ClientServiceMapper;
import com.lenarsharipov.simplebank.model.*;
import com.lenarsharipov.simplebank.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ClientServiceMapper mapper;


    @InjectMocks
    private ClientService clientService;

    private Client client;
    private Account account;
    private User user;
    private Phone phone;
    private TransferDto transferDto;
    private CreateClientDto createClientDto;
    private CreatedClientDto createdClientDto;
    private CreatePhoneDto createPhoneDto;
    private CreateEmailDto createEmailDto;
    private Pageable pageable;
    private ClientFiltersDto clientFiltersDto;
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String EMAIL = "test@email.com";
    public static final String PHONE_NUMBER = "123456789";
    public static final String FULL_NAME = "Ivan Ivanov";
    public static final String ENCODED_PASSWORD = "encodedPassword";
    public static final String ACCOUNT_NUMBER = "accountNumber";
    public static final LocalDate BIRTH_DATE = LocalDate.of(2000, 1, 1);
    public static final BigDecimal INITIAL_DEPOSIT = new BigDecimal(100);

    @BeforeEach
    public void setUp() {
        account = Account.of(1L, 0L, INITIAL_DEPOSIT, INITIAL_DEPOSIT);
        user = User.of(1L, USERNAME, ENCODED_PASSWORD, Role.ROLE_CLIENT);
        phone = Phone.of(PHONE_NUMBER);
        client = Client.builder()
                .id(1L)
                .user(user)
                .fullName(FULL_NAME)
                .birthDate(BIRTH_DATE)
                .account(account)
                .emails(List.of(Email.of(null, EMAIL)))
                .phones(Map.of(phone.getExternalId(), phone))
                .build();

//        transferDto = new TransferDto(new BigDecimal(100), 2L);
        createClientDto = CreateClientDto.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .fullName(FULL_NAME)
                .birthDate(BIRTH_DATE)
                .email(EMAIL)
                .phone(PHONE_NUMBER)
                .initialBalance(INITIAL_DEPOSIT)
                .build();

//        createPhoneDto = new CreatePhoneDto(PHONE_NUMBER);
//        createEmailDto = new CreateEmailDto(EMAIL);
//        pageable = mock(Pageable.class);
//        clientFiltersDto = mock(ClientFiltersDto.class);

        createdClientDto = CreatedClientDto.builder()
                .username(USERNAME)
                .fullName(FULL_NAME)
                .birthDate(BIRTH_DATE)
                .accountNo(ACCOUNT_NUMBER)
                .email(EMAIL)
                .phone(PHONE_NUMBER)
                .build();
    }

    @Test
    @DisplayName("Create new client")
    public void createClient() {
        when(mapper.toAccountEntity(createClientDto)).thenReturn(account);
        when(mapper.toUserEntity(createClientDto)).thenReturn(user);
        when(passwordEncoder.encode(createClientDto.password())).thenReturn(ENCODED_PASSWORD);
        when(Phone.of(createClientDto.phone())).thenReturn(phone);
        when(mapper.toClientEntity(createClientDto, user, account, phone))
                .thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(mapper.toCreatedClientDto(client, user)).thenReturn(createdClientDto);

        CreatedClientDto createdClient = clientService.create(createClientDto);

        assertNotNull(createdClient);
        verify(clientRepository, times(1)).save(client);
        verify(mapper, times(1)).toAccountEntity(createClientDto);
        verify(mapper, times(1)).toUserEntity(createClientDto);
        verify(passwordEncoder, times(1)).encode(createClientDto.password());
    }

//    @Test
//    @DisplayName("Add phone to existing client")
//    public void addPhone() {
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//        when(mapper.toCreatedPhoneDto(any(Phone.class))).thenReturn(new CreatedPhoneDto());
//
//        CreatedPhoneDto createdPhone = clientService.addPhone(1L, createPhoneDto);
//
//        assertNotNull(createdPhone);
//        assertTrue(client.getPhones().containsKey(createPhoneDto.phone()));
//        verify(clientRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Update phone of existing client")
//    public void updatePhone() {
//        client.getPhones().put("1", Phone.of("123456789"));
//
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//        when(mapper.toCreatedPhoneDto(any(Phone.class))).thenReturn(new CreatedPhoneDto());
//
//        CreatedPhoneDto updatedPhone = clientService.updatePhone(1L, "1", createPhoneDto);
//
//        assertNotNull(updatedPhone);
//        assertEquals("123456789", client.getPhones().get("1").getNumber());
//        verify(clientRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Delete phone from client, throws IllegalUserStateException if last phone")
//    public void deletePhone_LastPhone_ThrowsException() {
//        client.getPhones().put("1", Phone.of("123456789"));
//
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//
//        assertThrows(IllegalUserStateException.class, () -> {
//            clientService.deletePhone(1L, "1");
//        });
//
//        verify(clientRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Delete phone from client")
//    public void deletePhone() {
//        client.getPhones().put("1", Phone.of("123456789"));
//        client.getPhones().put("2", Phone.of("987654321"));
//
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//
//        clientService.deletePhone(1L, "1");
//
//        assertFalse(client.getPhones().containsKey("1"));
//        verify(clientRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Check if client is phone owner")
//    public void isPhoneOwner() {
//        client.getPhones().put("1", Phone.of("123456789"));
//
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//
//        boolean isOwner = clientService.isPhoneOwner(1L, "1");
//
//        assertTrue(isOwner);
//        verify(clientRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Add email to existing client")
//    public void addEmail() {
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//        when(mapper.toCreatedEmailDto(any(Email.class))).thenReturn(new CreatedEmailDto());
//
//        CreatedEmailDto createdEmail = clientService.addEmail(1L, createEmailDto);
//
//        assertNotNull(createdEmail);
//        assertTrue(client.getEmails().stream().anyMatch(email -> email.getAddress().equals(createEmailDto.email())));
//        verify(clientRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Update email of existing client")
//    public void updateEmail() {
//        Email email = Email.of(1L, "oldemail@example.com");
//        client.getEmails().add(email);
//
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//        when(mapper.toCreatedEmailDto(any(Email.class))).thenReturn(new CreatedEmailDto());
//
//        CreatedEmailDto updatedEmail = clientService.updateEmail(1L, 1L, createEmailDto);
//
//        assertNotNull(updatedEmail);
//        assertEquals(createEmailDto.email(), email.getAddress());
//        verify(clientRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Delete email from client, throws IllegalUserStateException if last email")
//    public void deleteEmail_LastEmail_ThrowsException() {
//        Email email = Email.of(1L, "email@example.com");
//        client.getEmails().add(email);
//
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//
//        assertThrows(IllegalUserStateException.class, () -> clientService.deleteEmail(1L, 1L));
//
//        verify(clientRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Delete email from client")
//    public void deleteEmail() {
//        Email email1 = Email.of(1L, "email1@example.com");
//        Email email2 = Email.of(2L, "email2@example.com");
//        client.getEmails().add(email1);
//        client.getEmails().add(email2);
//
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//
//        clientService.deleteEmail(1L, 1L);
//
//        assertFalse(client.getEmails().stream().anyMatch(email -> email.getId().equals(1L)));
//        verify(clientRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Check if client is email owner")
//    public void isEmailOwner() {
//        Email email = Email.of(1L, "email@example.com");
//        client.getEmails().add(email);
//
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//
//        boolean isOwner = clientService.isEmailOwner(1L, 1L);
//
//        assertTrue(isOwner);
//        verify(clientRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Check if client is user owner")
//    public void isUserOwner() {
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//
//        boolean isOwner = clientService.isUserOwner(1L, 1L);
//
//        assertTrue(isOwner);
//        verify(clientRepository, times(1)).findById(1L);
//    }
}