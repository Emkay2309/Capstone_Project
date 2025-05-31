package com.capstone.backend.contollers;

import com.capstone.backend.dtos.ApiResponseMessage;
import com.capstone.backend.dtos.ImageResponse;
import com.capstone.backend.dtos.PageableResponse;
import com.capstone.backend.dtos.UserDto;
import com.capstone.backend.services.FileService;
import com.capstone.backend.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;
    private Logger logger = LoggerFactory.getLogger(UserController.class);

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


    //upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage") MultipartFile image,
            @PathVariable String userId
    ) throws IOException {
        // 1. Upload the new image
        String newImageName = fileService.uploadFile(image, imageUploadPath);

        // 2. Get existing user data
        UserDto existingUser = userService.getUserById(userId);

        // 3. Create updated UserDto using builder pattern
        UserDto updatedUser = UserDto.builder()
                .id(existingUser.id())
                .name(existingUser.name())
                .email(existingUser.email())
                .password(existingUser.password())
                .gender(existingUser.gender())
                .about(existingUser.about())
                .imageName(newImageName)  // Set the new image name
                .build();

        // 4. Update the user in database
        UserDto savedUser = userService.updateUser(updatedUser,userId);

        // 5. Return response
        ImageResponse imageResponse = ImageResponse.builder().imageName(newImageName).success(true).status(HttpStatus.ACCEPTED).build();

        return new ResponseEntity<>(imageResponse ,HttpStatus.ACCEPTED);
    }
    
    //serve user image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId , HttpServletResponse response) throws IOException {
        
        //
        UserDto user = userService.getUserById(userId);
        logger.info("User image name : {} " , user.imageName());
        InputStream resource = fileService.getResource(imageUploadPath , user.imageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource,response.getOutputStream());
    }

}
