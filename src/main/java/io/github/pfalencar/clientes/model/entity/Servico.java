package io.github.pfalencar.clientes.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_cliente") //define uma FK. A tabela de Servico vai ter uma FK para a tabela de Cliente.
    private Cliente cliente;

    @Column
    private BigDecimal valor;
}
