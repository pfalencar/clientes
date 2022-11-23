package io.github.pfalencar.clientes.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;

//posso criar vários arquivos de properties para outros idiomas e de acordo com o local direcionar para o arquivo que
//tem as mensagens no idioma daquele país.

//***@Configuration transforma uma classe em classe de configuração.***
@Configuration
public class InternacionalizacaoConfig {

    /**
     * 1º- messageSource(): definir um arquivo de mensagens = messageSource
     * 2º- validatorFactoryBean(): definir o bean que será utlizado dentro da validação do bean validation e carregar nele
     *     o messageSource que foi configurado anteriormente
     * 3º- Desta forma podemos criar as mensagens no arquivo resources.messages.properties e interpolar nas entidades.
     * 4º- Depois colocar as mensagens na classe Cliente para fazer interpolação.
     */


    //O @Bean serve para quando o spring escanear esta classe de configuração, ele registrá-lo dentro do contexto.
    //este método vai servir para definir um arquivo que vai ser a fonte das mensagens que serão utilizadas no sistema.
    @Bean
    public MessageSource messageSource() {
        //o arquivo com as mensagens será o resources.messages.properties
        //para configurarmos um arquivo properties temos que tratá-lo como resource bundle (indica arquivo .properties),
        // então é desta forma que configuramos ele aqui neste método:
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");  //classpath=raiz: caminho do arquivo. Nome do arquivo (sem o .properties) de mensagem que vou configurar.
        messageSource.setDefaultEncoding("ISO-8859-1"); //entende os caracteres em português
        messageSource.setDefaultLocale(Locale.getDefault()); //vai pegar o local do SO e vai ver que é Brasil
        return messageSource;
    }

    //LocalValidatorFactoryBean é o objeto que faz a integração/interpolação (pegar a chave da message do @NotEmpty e
    // trocar pelo valor correspondente que está no arquivo messages.properties)
    // entre as mensagens e a nossa especificação de validação
    //do java. Que são o @NotEmpty e @NotNull, que está na classe Cliente.

    /**
     *  Erro 500 "Internal Server Error": during persist time
     * Porque estamos configurando o spring para trabalhar integração com a especificação bean Validation e a nossa
     * validação está apenas na entidade. Então esse erro bem na hora de fazer a persistência.
     * Teria que fazer antes de chegar no Hibernate? SIM!!!!
     *
     * Quando o Hibernate vai persistir também faz a leitura dessas informações, validando também.
     *
     * Porém, precisamos dizer para o spring que ele deve validar antes, ou seja, na fase da requisição.
     * Tem que colocar no controller a annotation @Valid junto com o @RequestBody.
     *
     * Assim, a leitura das annotations @NotEmpty, etc. vai ocorrer as também no momento da requisição.
     * Desta forma ele vai entrar no controller, que vai fazer a validação e se as informações estiverem inválidas, vai
     * lançar o erro 400.
     *
     * Só com o @NotEmpty já daria erro 500, como deu nos meus testes do vídeo 30 ao enviar {"nome":"", "cpf": "983920"}
     * @return validatorFactoryBean
     */
    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean() {
        //tenho que dizer que a fonte de mensagens é o messageSource: o método acima
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
