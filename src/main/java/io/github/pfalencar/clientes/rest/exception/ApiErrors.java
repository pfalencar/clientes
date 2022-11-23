package io.github.pfalencar.clientes.rest.exception;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    //o @Getter aqui é para criar o método getErrors() sem precisar escrever, somente para este atributo.
    @Getter
    private List<String> errors;

    public ApiErrors(List<String> errors) {
        this.errors = errors;
    }

    public ApiErrors (String message) {
        this.errors = Arrays.asList(message); //transformando um objeto em lista (String num Array)
    }
}
