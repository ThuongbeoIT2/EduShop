package com.example.ttversion1.login.serviceImpl;

import com.example.ttversion1.entity.Order;
import com.example.ttversion1.login.dto.AccountDTO;
import com.example.ttversion1.login.dto.UserDTO;
import com.example.ttversion1.login.entity.Account;
import com.example.ttversion1.login.entity.Decentralization;
import com.example.ttversion1.login.entity.User;
import com.example.ttversion1.login.entity.VerificationToken;
import com.example.ttversion1.login.repository.AccountRepo;
import com.example.ttversion1.login.repository.DecentralizationRepo;
import com.example.ttversion1.login.repository.UserRepo;
import com.example.ttversion1.login.repository.VerificationTokenRepository;
import com.example.ttversion1.login.service.IAccountService;
import com.example.ttversion1.login.service.IDecentralizationService;
import com.example.ttversion1.login.service.IUserService;
import com.example.ttversion1.repository.OrderRepo;
import com.example.ttversion1.shareds.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService implements IAccountService {
    private final AccountRepo accountRepo;
    private final IDecentralizationService decentralizationService;
    private final DecentralizationRepo decentralizationRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final IUserService userService;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final VerificationTokenRepository tokenRepository;

    private ModelMapper modelMapper= new ModelMapper();

    public AccountService(AccountRepo accountRepo, IDecentralizationService decentralizationService, DecentralizationRepo decentralizationRepo, BCryptPasswordEncoder passwordEncoder, IUserService userService, UserRepo userRepo, OrderRepo orderRepo, VerificationTokenRepository tokenRepository) {
        this.accountRepo = accountRepo;
        this.decentralizationService = decentralizationService;
        this.decentralizationRepo = decentralizationRepo;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public List<AccountDTO> GetAllAdmin() {
        return accountRepo.getAllByRoleAdmin().stream().map(
                account -> {
                    AccountDTO accountDTO=modelMapper.map(account,AccountDTO.class);
                    accountDTO.setMatchingPassword("Không thể hiển thị");
                    accountDTO.setPassword("Không thể hiển thị");
                    Optional<Decentralization> decentralization= decentralizationService.GetByName("ROLE_ADMIN");
                    Set<Decentralization> roles = new HashSet<>();
                    roles.add(decentralization.get());
                    accountDTO.setDecentralizations(roles);
                    Optional<UserDTO> userDTO= userService.findUserByEmail(account.getUser().getEmail());
                    accountDTO.setUserDTO(userDTO.get());
                    return accountDTO;
                }
        ).collect(Collectors.toList());
    }

@Override
    public List<AccountDTO> GetAllUser() {
        return accountRepo.getAllByRoleUser().stream().map(
                account -> {
                    AccountDTO accountDTO=modelMapper.map(account,AccountDTO.class);
                    accountDTO.setMatchingPassword("Không thể hiển thị");
                    accountDTO.setPassword("Không thể hiển thị");
                    Optional<Decentralization> decentralization= decentralizationService.GetByName("ROLE_USER");
                    Set<Decentralization> roles = new HashSet<>();
                    roles.add(decentralization.get());
                    accountDTO.setDecentralizations(roles);
                    Optional<UserDTO> userDTO= userService.findUserByEmail(account.getUser().getEmail());
                    accountDTO.setUserDTO(userDTO.get());
                    return accountDTO;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public Optional<AccountDTO> GetByUsername(String username) {
        Optional<AccountDTO> RS = accountRepo.findByUsername(username.trim()).map(
                account -> {
                    AccountDTO accountDTO=modelMapper.map(account,AccountDTO.class);
                    Set<Decentralization> roles = new HashSet<>();
                    for (Decentralization role : account.getRoles()){
                        roles.add(role);
                    }
                    accountDTO.setDecentralizations(roles);
                    Optional<UserDTO> userDTO= userService.findUserByEmail(account.getUser().getEmail());
                    accountDTO.setUserDTO(userDTO.get());
                    return accountDTO;
                }
        );
        return RS;
    }

    @Override
    public Optional<AccountDTO> findAccountByEmail(String email) {
        Optional<AccountDTO> RS = accountRepo.findAccountByEmail(email.trim()).map(
                account -> {
                    AccountDTO accountDTO=modelMapper.map(account,AccountDTO.class);
                    Set<Decentralization> roles = new HashSet<>();
                    for (Decentralization role : account.getRoles()){
                        roles.add(role);
                    }
                    accountDTO.setDecentralizations(roles);
                    Optional<UserDTO> userDTO= userService.findUserByEmail(account.getUser().getEmail());
                    accountDTO.setUserDTO(userDTO.get());
                    return accountDTO;
                }
        );
        return RS;
    }

    @Override
    public void Save(Account Obj) {
        accountRepo.save(Obj);
    }

    @Override
    public void Insert(AccountDTO accountDTO) {
        Optional<Account> RS = accountRepo.findByUsername(accountDTO.getUsername().trim());
        Optional<User> user= userRepo.findByEmail(accountDTO.getUserDTO().getEmail().trim());
        if(RS.isEmpty() ){
            Account newObj= new Account();
            newObj.setUsername(accountDTO.getUsername().trim());
            newObj.setAvatar(accountDTO.getAvatar());
            newObj.setUser(user.get());
            newObj.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
            Decentralization decentralization= decentralizationRepo.findByName("USER").get();
            Set<Decentralization> roles = new HashSet<>();
            roles.add(decentralization);
            newObj.setRoles(roles);
            newObj.setStatus(0);// Khởi tạo trạng thái offline
            newObj.setCreatedAt(Constants.getCurrentDay());
            newObj.setUpdateAt(Constants.getCurrentDay());
            accountRepo.save(newObj);
            String token = UUID.randomUUID().toString();
            VerificationToken verificationToken = new VerificationToken(token, newObj);
            tokenRepository.save(verificationToken);
            Order order = new Order();
            order.setUpdatedAt(Constants.getCurrentDay());
            order.setCreatedAt(Constants.getCurrentDay());
            order.setUser(user.get());
            order.setOriginalprice(0);
            order.setActualprice(0);
            orderRepo.save(order);
        }
    }

    @Override
    public void Update(AccountDTO accountDTO) {
        Optional<Account> RS = accountRepo.findByUsername(accountDTO.getUsername().trim());
        if (RS.isPresent()){
            //Username khong duoc thay doi nhe. To thich the username co dinh dang @edu.com
            RS.get().setUpdateAt(Constants.getCurrentDay());
            RS.get().setAvatar(accountDTO.getAvatar());
          accountRepo.save(RS.get());
        }
    }

    @Override
    public void Delete(String username) {
        Optional<Account> RS = accountRepo.findByUsername(username.trim());
        if (RS.isPresent()){
            accountRepo.delete(RS.get());
        }
    }
}
