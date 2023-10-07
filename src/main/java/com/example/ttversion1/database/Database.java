package com.example.ttversion1.database;


import com.example.ttversion1.login.repository.AccountRepo;
import com.example.ttversion1.login.repository.DecentralizationRepo;
import com.example.ttversion1.login.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class Database {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    private final BCryptPasswordEncoder passwordEncoder;
    private DecentralizationRepo decentralizationRepo;
    @Autowired
    private UserRepo userRepo;
    public Database(BCryptPasswordEncoder passwordEncoder, DecentralizationRepo decentralizationRepo) {
        this.passwordEncoder = passwordEncoder;
        this.decentralizationRepo = decentralizationRepo;
    }


    @Bean
    CommandLineRunner initDatabase(AccountRepo accountRepo) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                    Decentralization roleadmin= new Decentralization();
//                    roleadmin.setAuthorityname("ADMIN");
//                    roleadmin.setUpdateAt(Constants.getCurrentDay());
//                    roleadmin.setCreatedAt(Constants.getCurrentDay());
//                    Decentralization roleuser= new Decentralization();
//                    roleuser.setAuthorityname("USER");
//                    roleuser.setUpdateAt(Constants.getCurrentDay());
//                    roleuser.setCreatedAt(Constants.getCurrentDay());
//                    decentralizationRepo.save(roleadmin);
//                    decentralizationRepo.save(roleuser);

//                User admin = new User();
//                admin.setUsername("admin@gmail.com");
//                admin.setEmail("admin@gmail.com");
//                admin.setPhone("0000000000");
//                admin.setCreatedAt(Constants.getCurrentDay());
//                admin.setUpdateAt(Constants.getCurrentDay());
//                admin.setAddress("Thanh Xuân");
//                userRepo.save(admin);
//                Account account = new Account();
//                account.setUsername("admin@edushop.com");
//                account.setStatus(0);
//                account.setAvatar("avatar");
//                account.setEnabled(true);
//                Optional<Decentralization> userRole = decentralizationRepo.findById(2);
//                Optional<Decentralization> adminRole = decentralizationRepo.findById(1);
//                Set<Decentralization> roles = new HashSet<>();
//                roles.add(userRole.get());
//                roles.add(adminRole.get());
//                account.setRoles(roles);
//                account.setUpdateAt(Constants.getCurrentDay());
//                account.setCreatedAt(Constants.getCurrentDay());
//                account.setPassword(passwordEncoder.encode("admin"));
//
//                account.setUser(admin);
//                System.out.println(account);
//                accountRepo.save(account);


            }
        };
    }
}
