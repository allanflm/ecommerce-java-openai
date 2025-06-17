package br.com.allanflm.ecommerce;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.Arrays;
import java.util.Scanner;

public class CategorizadorDeProdutos {
    public static void main(String[] args) {
        var leito = new Scanner(System.in);

        System.out.println("Digite as categorias validas: ");
        var categorias = leito.nextLine();

        while (true) {


            System.out.println("Digite o nome do produto: ");
            var user = leito.nextLine();

            var system = """
                        Você é um categorizador de produtos e deve responder apenas o nome da categoria do produto informado
                    
                        Escolha uma categoria dentra a lista abaixo:
                    
                        %s
                    
                        ######Exemplo de uso:
                    
                        Pergunta: Bola de futebol
                        Resposta: Esportes
                    
                        ######Regras a serem seguidas:
                        Caso o usuario pergunte algo que nao seja de categorizacao de produtos, voce deve responder que nao pode ajudar pois o seu papel é apenas responder a categoria dos produtos.
                    """.formatted(categorias);

            DispararRequisicao(user, system);
        }


    }

    public static void DispararRequisicao(String user, String system) {
        var chave = System.getenv("OPENAI_API_KEY");
        var service = new OpenAiService(chave, Duration.ofSeconds(30));

        var completionRequest = ChatCompletionRequest.builder().model("gpt-4")
                .messages(Arrays.asList(new ChatMessage(ChatMessageRole.USER.value(), user), new ChatMessage(ChatMessageRole.SYSTEM.value(), system)))
                .build();
        service.createChatCompletion(completionRequest).getChoices()
                .forEach(c -> System.out.println(c.getMessage().getContent()));
    }
}
