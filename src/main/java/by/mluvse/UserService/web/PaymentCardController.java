package by.mluvse.UserService.web;

import by.mluvse.UserService.dto.CardDto;
import by.mluvse.UserService.dto.UpdateCardDto;
import by.mluvse.UserService.entity.AuthUser;
import by.mluvse.UserService.service.PaymentCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class PaymentCardController {

    private final PaymentCardService cardService;

    @GetMapping("/{id}")
    public ResponseEntity<CardDto> get(@PathVariable Long id,
                                                 @AuthenticationPrincipal AuthUser user) {
        return ResponseEntity.ok(cardService.getById(id, user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CardDto>> getAll(@RequestParam(required = false) String name,
                                                @RequestParam(required = false) String surname,
                                                Pageable pageable) {
        return ResponseEntity.ok(cardService.getAll(name, surname, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDto> update(@PathVariable Long id,
                                          @Valid @RequestBody UpdateCardDto body,
                                          @AuthenticationPrincipal AuthUser user) {
        CardDto dto = new CardDto(id, null, body.number(), body.holder(),
                body.expirationDate(), null);
        return ResponseEntity.ok(cardService.update(id, dto, user));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CardDto> changeStatus(@PathVariable Long id,
                                                @RequestParam boolean active,
                                                @AuthenticationPrincipal AuthUser user) {
        return ResponseEntity.ok(cardService.changeStatus(id, active, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal AuthUser user) {
        cardService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
