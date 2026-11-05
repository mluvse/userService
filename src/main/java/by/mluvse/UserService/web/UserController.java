package by.mluvse.UserService.web;

import by.mluvse.UserService.dto.CardDto;
import by.mluvse.UserService.dto.CreateCardDto;
import by.mluvse.UserService.dto.UserDto;
import by.mluvse.UserService.entity.AuthUser;
import by.mluvse.UserService.service.PaymentCardService;
import by.mluvse.UserService.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final PaymentCardService cardService;

    public UserController(UserService userService, PaymentCardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto userDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(userDto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    public ResponseEntity<UserDto> get(@PathVariable Long id){
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDto>> getAll(@RequestParam(required = false) String name,
                                                @RequestParam(required = false) String surname,
                                                Pageable pageable){
        return ResponseEntity.ok(userService.getAll(name,surname,pageable));
    }

    @GetMapping("/{id}/cards")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    public ResponseEntity<List<CardDto>> getCards(@PathVariable Long id){
        return ResponseEntity.ok(userService.getCards(id));
    }

    @PostMapping("/{id}/cards")
    public ResponseEntity<CardDto> addCard(@PathVariable Long id,
                                           @Valid @RequestBody CreateCardDto body,
                                           @AuthenticationPrincipal AuthUser user) {
        CardDto dto = new CardDto(null, id, body.number(), body.holder(),
                body.expirationDate(), true);
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.create(dto, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @Valid @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.update(id,userDto));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> changeStatus(@PathVariable Long id,
                                                @RequestParam boolean active) {
        return ResponseEntity.ok(userService.changeStatus(id, active));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
