package io.github.pfalencar.clientes;

import io.github.pfalencar.clientes.model.entity.Cliente;
import io.github.pfalencar.clientes.model.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class ClientesApplication {

    //Para testar se está gravando no BD
//    @Bean
//    public CommandLineRunner run(@Autowired ClienteRepository repository) {
//        return args -> {
//           // Cliente cliente = Cliente.builder().cpf("12345678900").nome("Fulano").build();
//            //repository.save(cliente);
//            Cliente cliente = new Cliente();
//            cliente.setNome("Ramira");
//            cliente.setCpf("123");
//            cliente.setDataCadastro(LocalDate.now());
//            repository.save(cliente);
//            System.out.println(cliente.toString());
//        };
//    }

    public static void main(String[] args) {
        SpringApplication.run(ClientesApplication.class, args);
    }
}
