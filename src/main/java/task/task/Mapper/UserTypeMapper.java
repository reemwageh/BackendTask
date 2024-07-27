package task.task.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import task.task.DTO.UserTypeDTO;
import task.task.Entity.UserType;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface UserTypeMapper {
    UserTypeMapper INSTANCE = Mappers.getMapper(UserTypeMapper.class);

    UserTypeDTO toDTO(UserType userType);

    UserTypeDTO toDTO(Optional<UserType> userType);

    UserType toEntity(UserTypeDTO userTypeDTO);
}
