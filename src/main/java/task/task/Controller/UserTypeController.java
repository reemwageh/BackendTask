package task.task.Controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import task.task.DTO.UserTypeDTO;
import task.task.Entity.UserType;
import task.task.Mapper.UserTypeMapper;
import task.task.Service.UserTypeService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Tag(name = "UserType Management")
public class UserTypeController {

    @Autowired
    private UserTypeService userTypeService;

    @Operation(
            description = "Post endpoint for UserType",
            summary = "This endpoint is used to Add new UserType"
    )
    @PostMapping("/create/userType")
    public UserTypeDTO addNewUserType(@RequestBody UserTypeDTO userTypeDTO) {
        return userTypeService.createUserType(userTypeDTO);
    }


    @Operation(
            description = "Get endpoint for All UserType",
            summary = "This endpoint is used to get all UserType stored in db"
    )
    @GetMapping("/get/userType/all")
    public List<UserTypeDTO> userTypeList() {
        return userTypeService.fetchAllUserTypes();
    }

    @Operation(
            description = "Get endpoint for UserType By ID",
            summary = "This endpoint is used to get UserType by ID"
    )
    @GetMapping("/get/userType/{typeId}")
    public UserTypeDTO getUserById(@PathVariable("typeId") int typeId){
        return userTypeService.getUserTypeById(typeId);
    }
}