package com.shoporder.config;

import com.shoporder.utils.QueueUtils;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.shoporder.utils.QueueUtils.*;

@Component
public class MQConfig {

    @Autowired
    private AmqpAdmin amqpAdmin;

    /**
     * AmqpAdmin
     * Ele lê todas as configurações que eu tenho de user, e me dá uma série de métodos com privilégio de administrador, pra criar coisas na fila
     */

    private Queue queue(String queueName){
        return new Queue(queueName, true, false, false);
    }

    /**
     * O Exchange é um protocolo online no qual o software de e-mail conecta-se ao servidor, realiza o sincronismo das mensagens e após esse processo mantém a conexão tempo real.
     */

    private DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE_NAME);
    }

    /**
     * Precisa ser criado um Binding (Vinculativo, em tradução livre) que vai realmente vincular a minha fila ao meu exchange
     */

    private Binding relate(Queue queue, DirectExchange exchange){
        return new Binding(queue.getName(), Binding.DestinationType.QUEUE, directExchange().getName(),
                queue.getName(), null);
    }

    /**
     * Agora, de fato, preciso construir tudo isso. Uma forma de relacionar tudo e construir de forma automática
     */

    @PostConstruct
    private void create(){
        Queue queue = queue(QUEUE_NAME); //crio a fila
        DirectExchange directExchange = directExchange(); //crio o directExchange
        Binding relate = relate(queue, directExchange);

        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareExchange(directExchange);
        amqpAdmin.declareBinding(relate);


    }


}
