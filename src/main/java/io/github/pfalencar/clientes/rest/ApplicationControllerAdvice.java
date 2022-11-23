package io.github.pfalencar.clientes.rest;

import io.github.pfalencar.clientes.rest.exception.ApiErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe para tratamento de erro
 * Quando a validação dos campos do cliente ocorrer e retornar que os campos são obrigatórios, etc.
 * Para a mensagem de retorno ficar mais legível para o front pegar as mensagens e mostrar para o usuário final.
 *
 * Para isso anotamos a classe com @ControllerAdvice, que identifica um esteriótipo de uma classe que vai receber
 * requisições quando houver um erro, vai interceptar o erro (as exceptions) e pode modificar o retorno da requisição
 * (vou criar uma resposta para a requisição com as mensagens de erro).
 * @RestControllerAdvice já tem a annotation @ControllerAdvice e a @ResponseBody, que evita ter de colocar esta última
 * annotation em todos os retornos dos métodos para dizer que "Object" é o retorno da requisição.
 */
@RestControllerAdvice
public class ApplicationControllerAdvice {

    /**
     * para tratar um erro específico precisa-se de um ExceptionHandler
     * retorno um Object que vai ser um objeto específico padronizado para mostrar os erros
     * Criamos este método que vai receber a exception que é lançada
     * Quando é uma exception de validação, quando o @Valid faz a validação e verifica que tem erro de validação
     * é uma exception chamada de MethodArgumentNotValidException = exceção de argumento não válido do método
     * e queremos interceptar esta exception quando ela ocorrer e para isso criamos nesta classe um ExceptionHandler.
     * A @ExceptionHandler tenho que mapear, dizendo qual exception devo receber para entrar neste ExceptionHandler.
     * E coloco a exception como argumento do método também, se eu quiser fazer algo com o erro que está vindo.
     * Porque o objeto MethodArgumentNotValidException vai me dar as mensagens de erro de validação, por isso
     * vou receber como parâmetro para utilizar estas mensagens.
     * BindingResult é o resultado da validação. É o objeto que tem o resultado inteiro da validação.
     * E o BindingResult tem o getAllErrors(), que é um array de erros. Tem o campo, msg do erro, etc.
     * Vou coletar esses erros em um array de stream, pq só nos interessa as mensagens.
     * Depois utilizar o método map() que serve para pegar um objeto e transformar em outro, neste caso, uma
     * lista do retorno do getAllErrors(), que é uma List<ObjectError> e vou transformar ele numa lista de String.
     * o map(objectError -> é o tipo de retorno do getAllErrors()
     * e para transformar ele em String uso objectError.getDefaultMessage(), que retorna a msg do @NotNull, por exemplo.
     * Desta forma transformo o array de erros em array de mensagens, porque quero somente as mensagens de erro que
     * ocorreram na validação.
     * O método retorna uma nova Stream de String = Stream<R>. String porque o getDefaultMessage() retorna uma String.
     * O método collect() pega tudo o que está na stream e coleta como uma lista. Vai pegar o Stream de String e
     * transformar em uma lista de Strings.
     * Depois é só enviar essa mensagem como objeto para não ter mais um JSON gigante como resposta da requisição
     * quando der erro de validação.
     *
     * Temos que criar um objeto padronizado que vai representar o retorno da API quando ocorrer o erro de validação.
     * Esse objeto vai pegar esse array de strings do handleValidationErrors e colocar dentro dele os campos para termos
     * um objeto organizado. Esse objeto será o rest.exception.ApiErrors
     * E agora em vez de o método retornar um Object ele vai retornar um ApiErrors
     * É importante colocar o código de erro correto, porque se retornar um OK, quando o client for fazer a requisição
     * não vai conseguir capturar esse erro/exception. Ele vai pensar que deu tudo certo e vai enviar a msg de sucesso
     * para o usuário.
     * Com o BAD_REQUEST vai lançar uma exception na aplicação do client, para ele poder tratar esse erro mostrando as
     * mensagens para o usuário, dizendo que ocorreu um erro de validação.
     *
     * Commo já estamos fazendo o tratamento de erro nesta classe, ele vai aplicar o @ExceptionHandler em todos os
     * lugares da aplicação back-end que estiverem com o @Valid.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //erro 400
    public ApiErrors handleValidationErrors(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> messages = bindingResult.getAllErrors()
                .stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());
        ApiErrors apiErrors = new ApiErrors(messages);
        return apiErrors;
    }

    /**
     * Fazendo o tratamento de erros para NOT_FOUND, que é aparece ainda uma resposta desconfigurada no Postman.
     * Quando procuramos por um cliente cujo id não existe (usado nos métodos atualizar, deletar e acharPorId)
     *
     * Vou receber como parâmetro o ResponseStatusException para poder pegar a mensagem que foi lançada.
     * Não coloco um @ResponseStatus neste caso, porque nos métodos atualizar, etc. já passamos o código de status
     * como parâmetro. Então não faz sentido eu mapear o código do erro aqui.
     * Para confirmar, é só ver que não vai retornar OK, mas sim NOT_FOUND como especificado nos métodos atualizar, etc.
     * do controller.
     * Também não vou retornar um ApiErrors, mas sim um objeto chamado ResponseEntity. Este objeto posso retornar da
     * forma que eu quiser, posso colocar um objeto como corpo da resposta, um código de status.
     * No ResponseEntity pode colocar código de status, pode colocar o objeto, que vai corpo da resposta. Serve para
     * fazer retornos dinâmicos.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity handleResponseStatusException(ResponseStatusException ex) {
        String mensagemErro = ex.getMessage(); //já tem a msg(reason) que escrevi no controller e o código de status
        HttpStatus codigoStatus = ex.getStatus();
        ApiErrors apiErrors = new ApiErrors(mensagemErro);
        ResponseEntity responseEntity = new ResponseEntity(apiErrors, codigoStatus);
        return responseEntity;
    }
}
