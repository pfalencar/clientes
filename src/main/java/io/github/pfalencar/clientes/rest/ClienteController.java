package io.github.pfalencar.clientes.rest;

import io.github.pfalencar.clientes.model.entity.Cliente;
import io.github.pfalencar.clientes.model.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

//@RestController  - para esta classe ser reconhecida no contexto da aplicação como um controlador REST.
// Que vai ser a classe que vai  criar nossa API de Clientes, que vai receber as requisições e enviar respostas HTTP REST
// para os nossos clients.
// Dentro da @RestController tem o @ResponseBody, que é para que o retorno de tudo nesta classe esteja no corpo da resposta da requisição.
//Dentro da @RestController tem a @ResponseBody, que no mapeia o objeto de retorno, Cliente, para o corpo da minha resposta
//e vai em formato JSON.

//@RequestMapping serve para mapear qual é a URL base, que vai ser tratada dentro deste controller.

//Abordagem RESTful para manter as boas práticas de programção:
//Utilizar os verbos corretos: POST - PostMapping; GET - RequestMapping, etc.

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    //como a entidade cliente é simples, então vou trabalhar direto com o ClienteRepository em vez de passar por um ServiceRepository antes.
    // poderia utilizar @Autowired direto na variável, e assim não colocaria a clienteRepository no construtor. Assim:
    //@Autowired
    //private ClienteRepository clienteRepository;

    //Faz-se desta forma ao utilizar a variável no construtor
    private final ClienteRepository clienteRepository;

    //coloquei a injeção de dependência @Autowired no construtor, porque dependo obrigatoriamente do ClienteRepository
    //para que a minha classe ClienteController funcione.
    //Visto que se eu não tiver o repositório, como farei as operações de salvar o cliente, deletar, atualizar, etc.
    //Portanto, na construção da classe eu tenho que passar já a ClienteRepository.
    @Autowired
    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    //poderia também criar um setClienteRepository, porém a variável criada não pode ser final, porque final não aceita ser modificada.
    //Porém, isso significa que esta dependência é opcional. Você só chama o set quando você quiser settar essa propriedade.
    //O @Autowired no set, significa que essa dependência da classe ClienteController para com a classe ClienteRepository é opcional.
    //@Autowired
//    public void setClienteRepository (ClienteRepository repository) {
//        this.clienteRepository = repository;
//    }

    //vou receber um cliente via JSON.
    // Vou fazer uma requisição JSON no javascript object na nossa aplicação Angular,
    //que vai retornar um JSON com as propriedades do Cliente (nome, cpf).
    //Eu vou acionar o repositório para salvar meu cliente no BD
    //E vou retornar esse cliente salvo para ele com a resposta HTTP
    // Obs.: NÃO PRECISA PASSAR: dataCadastro - pq ela é automática, é a data que estou cadastrando.
    //                           id - é gerado pelo BD.
    //Para mapear uma URL para fazer esta requisição, utiliza-se @PostMapping
    // @PostMapping mapeia o método para uma requisição POST. Indica que vamos criar este recurso no servidor.
    //Utiliza-se o POST quando este cliente não está salvo ainda, estou salvando ele pela primeira vez.
    //Quando eu fizer um POST para a URL /api/clientes passando um cliente JSON, vai entrar neste método salvar
    //@ResponseStatus
    //Preciso definir qual é o meu código de status para dizer que ele foi salvo com sucesso.
    //Pode ser 200-OK ou 201-Created.
    // Ao utilizar RESTful, retornamos o código de status CREATED.
    //Se eu não colocar esta annotation, @ResponseStatus, ele vai sempre retornar o código de status 200-OK,
    //portanto, sempre que você quiser retornar o código de status 200-OK, não tem necessidade de colocar esta annotation.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);

    }

}
