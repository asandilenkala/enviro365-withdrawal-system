package com.enviro.assessment.junior.asandile.mapper;

import com.enviro.assessment.junior.asandile.dto.response.UserResponseDTO;
import com.enviro.assessment.junior.asandile.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper for User entity to DTO conversion
 *
 * @author Asandile
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Convert User entity to UserResponseDTO
     *
     * @param user user entity
     * @return user response DTO
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "enabled", source = "enabled")
    @Mapping(target = "investorId", expression = "java(user.getInvestor() != null ? user.getInvestor().getId() : null)")
    @Mapping(target = "investorName", expression = "java(user.getInvestor() != null ? user.getInvestor().getFirstName() + \" \" + user.getInvestor().getLastName() : null)")
    UserResponseDTO toDto(User user);
}