package com.lenarsharipov.simplebank.service;

import com.lenarsharipov.simplebank.dto.account.TransferDto;
import com.lenarsharipov.simplebank.exception.InsufficientFundsException;
import com.lenarsharipov.simplebank.exception.ResourceNotFoundException;
import com.lenarsharipov.simplebank.model.Account;
import com.lenarsharipov.simplebank.model.User;
import com.lenarsharipov.simplebank.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTransferInsufficientFunds() {
        User sender = new User();
        sender.setId(1L);
        sender.setAccount(new Account(1L, 0L, new BigDecimal(20), new BigDecimal(20)));

        User receiver = new User();
        receiver.setId(2L);
        receiver.setAccount(new Account(2L, 0L, new BigDecimal(100), new BigDecimal(100)));

        TransferDto transferDto = new TransferDto(BigDecimal.valueOf(50), 2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        assertThrows(InsufficientFundsException.class,
                () -> userService.transfer(1L, transferDto));
    }

    @Test
    void testTransferUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        TransferDto transferDto = new TransferDto(BigDecimal.valueOf(50), 2L);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.transfer(1L, transferDto));
    }
}