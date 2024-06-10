package com.lenarsharipov.simplebank.controller;

import com.lenarsharipov.simplebank.dto.account.TransferDto;
import com.lenarsharipov.simplebank.dto.email.CreateEmailDto;
import com.lenarsharipov.simplebank.dto.email.CreatedEmailDto;
import com.lenarsharipov.simplebank.dto.filter.FiltersDto;
import com.lenarsharipov.simplebank.dto.phone.CreatePhoneDto;
import com.lenarsharipov.simplebank.dto.phone.CreatedPhoneDto;
import com.lenarsharipov.simplebank.dto.client.PageClientDto;
import com.lenarsharipov.simplebank.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/clients")
@AllArgsConstructor
@Tag(name = "User Controller", description = "User API")
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/{userId}/phones")
    @ResponseStatus(CREATED)
    @Operation(summary = "Add a new phone number to the specified client")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userId)")
    public CreatedPhoneDto addPhone(@PathVariable Long userId,
                                    @Valid @RequestBody CreatePhoneDto dto) {
        return clientService.addPhone(userId, dto);
    }

    @PutMapping("/{userId}/phones/{phoneId}")
    @Operation(summary = "Update an existing phone number of the specified client")
    @PreAuthorize("@customSecurityExpression.canAccessPhone(#userId, #phoneId)")
    public CreatedPhoneDto updatePhone(@PathVariable Long userId,
                                       @PathVariable Long phoneId,
                                       @Valid @RequestBody CreatePhoneDto dto) {
        return clientService.updatePhone(userId, phoneId, dto);
    }

    @DeleteMapping("/{userId}/phones/{phoneId}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = """
            Delete an existing phone number if it is not
            the last one of the specified client
            """)
    @PreAuthorize("@customSecurityExpression.canAccessPhone(#userId, #phoneId)")
    public void deletePhone(@PathVariable Long userId,
                            @PathVariable Long phoneId) {
        clientService.deletePhone(userId, phoneId);
    }

    @PostMapping("/{userId}/emails")
    @ResponseStatus(CREATED)
    @Operation(summary = "Add new email to the specified client")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userId)")
    public CreatedEmailDto addEmail(@PathVariable Long userId,
                                    @Valid @RequestBody CreateEmailDto dto) {
        return clientService.addEmail(userId, dto);
    }

    @PutMapping("/{userId}/emails/{emailId}")
    @Operation(summary = "Update an existing email of the specified client")
    @PreAuthorize("@customSecurityExpression.canAccessEmail(#userId, #emailId)")
    public CreatedEmailDto updateEmail(@PathVariable Long userId,
                                       @PathVariable Long emailId,
                                       @Valid @RequestBody CreateEmailDto dto) {
        return clientService.updateEmail(userId, emailId, dto);
    }

    @DeleteMapping("/{userId}/emails/{emailId}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = """
            Delete an existing email if it is not
            last one of the specified client
            """)
    @PreAuthorize("@customSecurityExpression.canAccessEmail(#userId, #emailId)")
    public void deleteEmail(@PathVariable Long userId,
                            @PathVariable Long emailId) {
        clientService.deleteEmail(userId, emailId);
    }

    @GetMapping("/search")
    @Operation(summary = "Dynamic filtered search with sorting. Result is paged")
    public PageClientDto search(FiltersDto filters,
                                @PageableDefault(direction = Sort.Direction.ASC)
                                Pageable pageable) {
        return clientService.search(filters, pageable);
    }

    @PostMapping("/{senderUserId}/transfer")
    @Operation(summary = "Transfer money")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#senderUserId)")
    public void transfer(@PathVariable Long senderUserId,
                         @Valid @RequestBody TransferDto dto) {
        clientService.transfer(senderUserId, dto);
    }
}