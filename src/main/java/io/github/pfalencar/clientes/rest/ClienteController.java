package io.github.pfalencar.clientes.rest;

import io.github.pfalencar.clientes.model.entity.Cliente;
import io.github.pfalencar.clientes.model.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

//@RestController  - para esta classe ser reconhecida no contexto da aplicação como um controlador REST.
// Que vai ser a classe que vai  criar nossa API de Clientes, que vai receber as requisições e enviar respostas HTTP REST
// para os nossos clients.
// Dentro da @RestController tem o @ResponseBody, que é para que o retorno de tudo nesta classe esteja no corpo da resposta da requisição.
//Dentro da @RestController tem a @ResponseBody, que mapeia o objeto de retorno do método salvar, para o corpo da minha resposta
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
    public Cliente salvar(@RequestBody @Valid Cliente cliente) {

        return clienteRepository.save(cliente);
    }

    @GetMapping
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @GetMapping("{id}")
    public Cliente acharPorId(@PathVariable Integer id) {
        return clienteRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

//Primeira abordagem do método delete():
//    @DeleteMapping("{id}")
//    public void deletar (@PathVariable Integer id) {
//        clienteRepository.deleteById(id);
//    }

    //Segunda abordagem do método delete():
    //Nesta abordagem verifico primeiro se o cliente existe na base de dados e posso ter uma ação contra isso (lanço status NOT_FOUND).
    //E se eu encontrar, deleto ele não retorno nada, só o status 204-NO_CONTENT

    //204-NO_CONTENT é um código de sucesso, indica que não há nenhum objeto/recurso de retorno
    //204-NO_CONTENT já é suficiente para o client (quem está consumindo esta API) saiba que o recurso foi deletado no servidor.

    //se encontrar o cliente, entra no método map(), senão lanço NOT_FOUND
    //findById retorna un Optional. Como veio um objeto, entra no map e pego o cliente.
    //map() recebe o objeto cliente que veio do findById e retorna qlqr objeto, o mesmo objeto ou outro
    //map() serve para fazer o mapeamento de um objeto para alguma coisa
    //dentro do map preciso deletar o cliente.

    //no map() preciso retornar algo, se eu retornar null entra na exceção NOT_FOUND,
    //também não vou retornar o cliente, porque não vou fazer nada com ele.
    //Então, retorno Void.TYPE para não ficar sem retorno ou retornar nulo.

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar (@PathVariable Integer id) {
        clienteRepository
                .findById(id)
                .map(cliente -> {
                    clienteRepository.delete(cliente);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar (@PathVariable Integer id, @RequestBody @Valid Cliente clienteAtualizado) {
        clienteRepository
                .findById(id)  //pega do id que foi passado na URL
                .map(cliente -> { //este é o cliente que foi encontrado pelo findById(id)
                    //clienteAtualizado.setId(cliente.getId());
                    //return clienteRepository.save(clienteAtualizado);
                    //ou
                    cliente.setNome(clienteAtualizado.getNome());
                    cliente.setCpf(clienteAtualizado.getCpf());
                    return clienteRepository.save(cliente);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }
}
