package com.example.ttversion1.login.serviceImpl;


import com.example.ttversion1.login.dto.UserDTO;
import com.example.ttversion1.login.entity.User;
import com.example.ttversion1.login.repository.UserRepo;
import com.example.ttversion1.login.service.IUserService;
import com.example.ttversion1.shareds.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepo userRepo;
    private ModelMapper modelMapper= new ModelMapper();
    @Override
    public List<UserDTO> GetAll() {
        return userRepo.findAll().stream().map(
                user -> {
                    UserDTO userDTO = modelMapper.map(user,UserDTO.class);
                    return userDTO;
                }
        ).collect(Collectors.toList());
    }



    @Override
    public Optional<UserDTO> findUserByEmail(String email) {
        Optional<UserDTO> RS= userRepo.findByEmail(email.trim()).map(
                user -> {
                    UserDTO userDTO = modelMapper.map(user,UserDTO.class);
                    return userDTO;
                }
        );
        return RS;
    }

    @Override
    public void Save(User Obj) {
        userRepo.save(Obj);
    }

    @Override
    public void Insert(UserDTO userDTO) {
        Optional<User> user = userRepo.findByEmail(userDTO.getEmail().trim());
        if (user.isEmpty()){
            User newUser= new User();
            newUser.setEmail(userDTO.getEmail());
            newUser.setUsername(userDTO.getUsername());
            newUser.setPhone(userDTO.getPhone().trim());
            newUser.setAddress(userDTO.getAddress());
            newUser.setUpdateAt(Constants.getCurrentDay());
            newUser.setCreatedAt(Constants.getCurrentDay());
            userRepo.save(newUser);
        }
    }

    @Override
    public void Update(UserDTO userDTO) {
        Optional<User> user = userRepo.findByEmail(userDTO.getEmail().trim());

        if (user.isPresent()){
            user.get().setUsername(userDTO.getUsername());
            user.get().setPhone(userDTO.getPhone().trim());
            user.get().setAddress(userDTO.getAddress());
            user.get().setUpdateAt(Constants.getCurrentDay());
            userRepo.save( user.get());
        }
    }



    @Override
    public void DeleteByEmail(String email) {
        Optional<User> RS= userRepo.findByEmail(email.trim());
        if (RS.isPresent()){
            userRepo.delete(RS.get());
        }
    }
}
