package task.task.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import task.task.DTO.UserDTO;
import task.task.Entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);

    List<UserDTO> usersToUserDTOs(List<User> users);
}

