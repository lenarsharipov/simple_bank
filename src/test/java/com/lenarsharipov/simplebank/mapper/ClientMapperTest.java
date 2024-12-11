package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.client.CreateClientDto;
import com.lenarsharipov.simplebank.model.Account;
import com.lenarsharipov.simplebank.model.Client;
import com.lenarsharipov.simplebank.model.Phone;
import com.lenarsharipov.simplebank.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientMapperTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    private CreateClientDto createClientDto;

    @BeforeEach
    void setUp() {
        createClientDto = CreateClientDto.builder()
                .username("testUser")
                .password("P4ssword")
                .fullName("Ivan Ivanov")
                .birthDate(LocalDate.of(2000, 1, 1))
                .email("test@example.com")
                .phone("123-456-7890")
                .initialBalance(new BigDecimal(100))
                .build();
    }

    @Test
    void testToClientEntity() {
        // Arrange
        User user = new User();
        Account account = new Account();

        when(userMapper.toUserEntity(any(CreateClientDto.class))).thenReturn(user);
        when(accountMapper.toAccountEntity(any(CreateClientDto.class))).thenReturn(account);

        // Act
        Client client = clientMapper.toClientEntity(createClientDto);

        // Assert
        assertNotNull(client);
        assertNull(client.getId());
        assertNotNull(client.getUser());
        assertNotNull(client.getAccount());
        assertEquals(user, client.getUser());
        assertEquals(account, client.getAccount());

        assertNotNull(client.getEmails());
        assertEquals(1, client.getEmails().size());
        assertEquals("test@example.com", client.getEmails().get(0).getAddress());

        assertNotNull(client.getPhones());
        assertEquals(1, client.getPhones().size());
        Phone phone = client.getPhones().values().iterator().next();
        assertEquals("123-456-7890", phone.getNumber());
        assertNotNull(phone.getExternalId());

        // Verify interactions with mocks
        verify(userMapper, times(1)).toUserEntity(any(CreateClientDto.class));
        verify(accountMapper, times(1)).toAccountEntity(any(CreateClientDto.class));
    }
}
