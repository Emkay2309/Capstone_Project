package com.capstone.backend.services;

import com.capstone.backend.dtos.PageableResponse;
import com.capstone.backend.dtos.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    //create
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto,String userId);

    //delete
    void deleteUser(String userId);

    //deactivate
//    void deactivateUser(String userId);

    //get all user
//    Page<UserDto> getAllUsers(int pageNumber, int pageSize);
    PageableResponse<UserDto> getAllUsers(int pageNumber , int pageSize, String sortBy, String sortDir);

    //get single user by id
    UserDto getUserById(String userEmail);

    //get single user by email
    UserDto getUserByEmail(String userId);

    //search user
    List<UserDto> searchUser(String keyword);




}
