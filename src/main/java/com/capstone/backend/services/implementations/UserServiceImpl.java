package com.capstone.backend.services.implementations;

import com.capstone.backend.dtos.PageableResponse;
import com.capstone.backend.dtos.UserDto;
import com.capstone.backend.entities.User;
import com.capstone.backend.exceptions.ResourceAlreadyExistsException;
import com.capstone.backend.exceptions.ResourceNotFoundException;
import com.capstone.backend.helpers.PageResponseHelper;
import com.capstone.backend.repositories.UserRepository;
import com.capstone.backend.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${user.profile.image.path}")
    private String imagePath;

    @Override
    public UserDto createUser(UserDto userDto) {
//        User user = dtoToEntity(userDto);
//        User savedUser = userRepository.save(user);
//        return entityToDto(savedUser);

        if (userRepository.existsByEmail(userDto.email())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        User user = mapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.password()));
        User savedUser = userRepository.save(user);
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto , String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setGender(userDto.gender());
        user.setAbout(userDto.about());
        user.setImageName(userDto.imageName());

        User updatedUser = userRepository.save(user);

        return entityToDto(updatedUser);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with the id not found"));

        //delete user profile image
        String fullPath = imagePath + user.getImageName();
        Path path = Paths.get(fullPath);

        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new ResourceNotFoundException("User image not found in folder...!!!");
        }

        userRepository.delete(user);
    }

//    @Override
//    public void deactivateUser(String userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//        user.setActive(false);
//        userRepository.save(user);
//
//    }

//    @Override
//    public Page<UserDto> getAllUsers(int pageNumber, int pageSize) {
//        // Create page request with sorting (optional)
//        Pageable pageable = PageRequest.of(pageNumber, pageSize);
//
//        // Get paginated results from repository
//        Page<User> userPage = userRepository.findAll(pageable);
//
//        // Convert Page<User> to Page<UserDto>
//        return userPage.map(this::entityToDto);
//    }

//    @Override
//    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
//        // Create sort direction
//        Sort.Direction direction = sortDir.equalsIgnoreCase("desc")
//                ? Sort.Direction.DESC
//                : Sort.Direction.ASC;
//
//        // Create pageable with sorting
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
//
//        // Get paginated results
//        Page<User> userPage = userRepository.findAll(pageable);
//
//        // Convert to DTO list
//        List<UserDto> dtoList = userPage.getContent()
//                .stream()
//                .map(this::entityToDto)
//                .collect(Collectors.toList());
//
//        // Build response using Builder pattern
//        return PageableResponse.<UserDto>builder()
//                .content(dtoList)
//                .pageNumber(userPage.getNumber())
//                .pageSize(userPage.getSize())
//                .totalElement(userPage.getTotalElements())  // Fixed: was getTotalPages()
//                .totalPages(userPage.getTotalPages())
//                .lastPage(userPage.isLast())
//                .build();
//    }

    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Create sort direction
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        // Create pageable with sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));

        // Get paginated results
        Page<User> userPage = userRepository.findAll(pageable);

        // Use PageResponseHelper to convert and build response
        return PageResponseHelper.getPageableResponse(userPage, UserDto.class);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with this Id"));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new ResourceNotFoundException("User found with this given email"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> usersByKeyword = userRepository.findByKeyword(keyword);
        return usersByKeyword.stream().map(u-> entityToDto(u)).collect(Collectors.toList());
    }

    private User dtoToEntity(UserDto userDto) {
        return mapper.map(userDto , User.class);
    }

    private UserDto entityToDto(User user) {
        return mapper.map(user ,  UserDto.class);
    }
}
