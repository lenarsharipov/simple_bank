package com.lenarsharipov.simplebank.controller;

import org.junit.jupiter.api.Test;

import com.lenarsharipov.simplebank.dto.account.TransferDto;
import com.lenarsharipov.simplebank.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ClientController(clientService)).build();
    }

    @Test
    @WithMockUser
    void testTransfer() throws Exception {
        Mockito.doNothing().when(clientService).transfer(Mockito.anyLong(), Mockito.any(TransferDto.class));

        mockMvc.perform(post("/clients/1/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"receiverUserId\": 2, \"amount\": 100}"))
                .andExpect(status().isOk());
    }
}
