package br.com.rafael.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Controller
@RestController
@RequestMapping("/users")

public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel)
    {
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if(user != null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
        }

        var passwordHashred = BCrypt.withDefaults()
        .hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashred);

        //userModel.setPassword("ABDC");


        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
        //System.out.println(userModel.getUsername());
    }
    
}
