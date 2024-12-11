package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.client.CreateClientDto;
import com.lenarsharipov.simplebank.model.Role;
import com.lenarsharipov.simplebank.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private static final String PASSWORD = "P4ssword";

    @Test
    public void testToEntity() {
        CreateClientDto dto = CreateClientDto.builder()
                .username("testUsername")
                .password(PASSWORD)
                .fullName("Ivan Ivanov")
                .birthDate(LocalDate.of(2000, 1, 1))
                .email("i.ivanov@test.com")
                .phone("123456789")
                .initialBalance(new BigDecimal(100))
                .build();

        User user = userMapper.toUserEntity(dto);

        assertNotNull(user);
        assertEquals(dto.username(), user.getUsername());
        assertEquals(user.getRole(), Role.ROLE_CLIENT);
        assertNotNull(user.getPassword());
        assertTrue(passwordEncoder.matches(PASSWORD, user.getPassword()));
    }
}