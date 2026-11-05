package by.mluvse.UserService.dto;

import by.mluvse.UserService.entity.User;
import org.mapstruct.*;

@Mapper(componentModel ="spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = PaymentCardMapper.class)
public interface UserMapper {

    @Mapping(target = "active", source = "active")
    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cards", source = "cards", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    User toEntity(UserDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cards", ignore = true)
    User updateEntity(UserDto dto, @MappingTarget User user);

    @AfterMapping
    default void linkCards(@MappingTarget User user){
        if (user.getCards() == null) {
            return;
        }
        user.getCards().forEach(card -> card.setUser(user));
    }
}
