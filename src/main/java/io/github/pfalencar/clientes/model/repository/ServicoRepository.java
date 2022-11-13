package io.github.pfalencar.clientes.model.repository;

import io.github.pfalencar.clientes.model.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Integer> {
}
