package task.task.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.task.DTO.UserTypeDTO;
import task.task.Entity.UserType;
import task.task.Mapper.UserTypeMapper;
import task.task.Repository.UserTypeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserTypeServiceImp implements UserTypeService{
    @Autowired
    private UserTypeRepository userTypeRepository;

    private final UserTypeMapper userTypeMapper = UserTypeMapper.INSTANCE;

    @Override
    public UserTypeDTO createUserType(UserTypeDTO userTypeDTO) {
        UserType userType = userTypeMapper.toEntity(userTypeDTO);
        UserType createdUserType = userTypeRepository.save(userType);
        return userTypeMapper.toDTO(createdUserType);
    }

    @Override
    public List<UserTypeDTO> fetchAllUserTypes() {
        List<UserType> userTypes = userTypeRepository.findAll();
        return userTypes.stream()
                .map(userTypeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserTypeDTO getUserTypeById(int typeId) {
        return userTypeRepository.findById(typeId)
                .map(userTypeMapper::toDTO)
                .orElse(null);
    }
}

