package io.github.pfalencar.clientes.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter@Setter  //depois de importar a biblioteca lombok no pom.xml consigo utilizar estas annotations.
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //IDENTITY deixa para o banco fazer o autoincremento.
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

//Antes de persistir no BD, executa esse método
//    @PrePersist
//    public void prePersist () {
//        setDataCadastro(LocalDate.now());
//    }


}
