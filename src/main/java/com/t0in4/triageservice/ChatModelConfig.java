package com.t0in4.triageservice;

import chat.giga.client.GigaChatClient;
import chat.giga.client.GigaChatClientImpl;
import chat.giga.client.auth.AuthClient;
import chat.giga.client.auth.AuthClientBuilder;
import chat.giga.langchain4j.GigaChatChatModel;
import chat.giga.langchain4j.GigaChatChatRequestParameters;
import chat.giga.model.ModelName;
import chat.giga.model.Scope;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.service.output.JsonSchemas;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatModelConfig {

    @Bean
    public ChatModel gigaChatChatModel() {
        Dotenv dotenv = Dotenv.load();
        String authKey = dotenv.get("GIGACHAT_AUTH_KEY");
        AuthClient authClient = AuthClient.builder()
                .withOAuth(AuthClientBuilder.OAuthBuilder.builder()
                        .scope(Scope.GIGACHAT_API_PERS)
                        .authKey(authKey)
                        .build())
                .build();

        return GigaChatChatModel.builder()
                .authClient(authClient)
                .defaultChatRequestParameters(GigaChatChatRequestParameters.builder()
                        .modelName(ModelName.GIGA_CHAT_2)
                        .temperature(0.0)
                        .responseFormat(ResponseFormat.builder()
                                .type(ResponseFormatType.JSON)
                                .jsonSchema(JsonSchemas.jsonSchemaFrom(TriageService.class).get())
                                .build())
                        .build())
                .build();
    }
}
