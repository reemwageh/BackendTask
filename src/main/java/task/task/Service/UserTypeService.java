package task.task.Service;

import task.task.DTO.UserTypeDTO;
import task.task.Entity.UserType;

import java.util.List;
import java.util.Optional;

public interface UserTypeService {

    UserTypeDTO createUserType(UserTypeDTO userTypeDTO);

    List<UserTypeDTO> fetchAllUserTypes();

    UserTypeDTO getUserTypeById(int typeId);
}