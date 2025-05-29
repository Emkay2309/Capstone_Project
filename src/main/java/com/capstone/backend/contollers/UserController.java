package com.capstone.backend.contollers;

import com.capstone.backend.dtos.ApiResponseMessage;
import com.capstone.backend.dtos.PageableResponse;
import com.capstone.backend.dtos.UserDto;
import com.capstone.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<>(createdUser , HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto , @PathVariable String userId) {
        UserDto updatedUser = userService.updateUser(userDto,userId);
        return new ResponseEntity<>(updatedUser , HttpStatus.ACCEPTED);
    }

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage.builder().message("User is deleted successfully...!!!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(message , HttpStatus.OK);
    }

    //get all
//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public Page<UserDto> getAllUsers(
//            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
//            @RequestParam(value = "pageSize", defaultValue = "10" , required = false) int pageSize) {
//        return userService.getAllUsers(pageNumber, pageSize);
//    }

    @GetMapping

    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10" , required = false) int pageSize,
            @RequestParam(value = "sortBy" , defaultValue = "name" , required = false) String sortBy,
            @RequestParam(value = "sortDir" , defaultValue = "asc" , required = false) String sortDir

    ) {
        return new ResponseEntity<>(userService.getAllUsers(pageNumber, pageSize,sortBy,sortDir) , HttpStatus.OK);
    }

    //get single user by id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getUserById(userId) , HttpStatus.OK);
    }

    //get user by email
    @GetMapping("email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String userEmail) {
        return new ResponseEntity<>(userService.getUserByEmail(userEmail) , HttpStatus.OK);
    }

    //get user by keyword
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> getUserByKeyword(@PathVariable String keyword) {
        return new ResponseEntity<>(userService.searchUser(keyword), HttpStatus.OK);
    }

}
