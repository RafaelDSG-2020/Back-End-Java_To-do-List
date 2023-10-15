package br.com.rafael.todolist.user;

//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;

//import org.hibernate.annotations.CreationTimestamp;

//import jakarta.persistence.Column;
//import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
// import lombok.Getter;
// import lombok.Setter;


// @Getter // Cria todos os Getters 
// @Setter // Cria todos os Setters
@Data //Com esse decorator do Lombok cria todos os getters e settes
@Entity(name = "tb_users") // Criação de tabela utiliza o Entity que é de entidade
public class UserModel {

    @Id // chave primaria
    @GeneratedValue(generator = "UUID")
    private UUID id;

    //@Column(name = "usuario") serve para criar a coluna
    //@Getter nesse posição apenas encima cria apenas para um atributos
    @Column(unique = true)
    private String username;
    private String name;
    private String password;


    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    
}
