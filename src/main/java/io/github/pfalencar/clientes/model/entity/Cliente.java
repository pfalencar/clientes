package io.github.pfalencar.clientes.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @Column(nullable = false, length = 150) //201 created com requisição "nome":"", mas erro 500 sem o atributo na requisição.
    @NotEmpty(message = "{campo.nome.obrigatorio}") //Testando somente com @NotEmpty => "nome":"" dá erro 500
    private String nome;

    @Column(nullable = false, length = 11)
    @NotNull(message = "{campo.cpf.obrigatorio}") //poderia colocar a mensagem direto aqui, mas fica chumbado.
    @CPF(message = "{campo.cpf.invalido}") //as chaves indica que é um expressão que deve ser interpolada.
    private String cpf;

    @Column(name = "data_cadastro", updatable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataCadastro;

//Antes de persistir no BD, executa esse método
    @PrePersist
    public void prePersist () {
        setDataCadastro(LocalDate.now());
    }


}
