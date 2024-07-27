package task.task.Service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import task.task.DTO.UserDTO;
import task.task.Entity.User;
import task.task.Repository.UserRepository;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Component
public class UserDetailsServiceImp implements UserDetailsService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent())
            return user.get();
        throw new RuntimeException("User is not authorized");
    }
}