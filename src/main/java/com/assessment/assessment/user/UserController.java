package com.assessment.assessment.user;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.assessment.assessment.exceptions.UserNotFoundException;
import com.assessment.assessment.user.dto.CreateUserDTO;
import com.assessment.assessment.user.dto.UpdateUserDTO;

@Controller
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @QueryMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @QueryMapping
    public List<User> getUserByFirstName(@Argument("firstName") String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    @MutationMapping
    public User createUser(@Argument("createUserDto") CreateUserDTO createUserDTO) {
        User user = new User(createUserDTO.getFirstName(), createUserDTO.getLastName(), createUserDTO.getPhone(), createUserDTO.getDateOfBirth());
        return userRepository.save(user);
    }

    @MutationMapping
    public User updateUser(@Argument("updateUserDto") UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(updateUserDTO.getId())
            .orElseThrow(() -> new UserNotFoundException("User with ID " + updateUserDTO.getId() + " not found"));
        user.setFirstName(updateUserDTO.getFirstName());
        user.setLastName(updateUserDTO.getLastName());
        user.setPhone(updateUserDTO.getPhone());
        user.setDateOfBirth(updateUserDTO.getDateOfBirth());
        return userRepository.save(user);
        
    }

    @MutationMapping
    public User deleteUser(@Argument("id") Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        userRepository.delete(user);
        return user;
    }
}
