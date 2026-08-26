package com.example.it_iap.service;

import com.example.it_iap.AI.TokenUsageAdvisor;
import com.example.it_iap.dto.chatbot.request.ChatRequest;
import com.example.it_iap.dto.chatbot.response.ChatbotResponse;
import com.example.it_iap.entity.ChatSession;
import com.example.it_iap.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CHAT_BOT_SERVICE")
public class ChatbotService {
    @Qualifier("memoryChatClient")
    private final ChatClient memoryChatClient;

    private final ChatSessionService chatSessionService;

    public ChatbotResponse chatbot (ChatRequest request){
        ChatSession chatSession = chatSessionService.getChatSession(request.getSessionId());
        String systemPromptTemplate = chatSession.getPromptVersion().getPromptContent();

        String aiResponse = memoryChatClient
                .prompt()
                .system(systemPromptTemplate)
                .user(request.getUserMessage())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatSession.getId()))
                .advisors(new TokenUsageAdvisor(chatSessionService, chatSession))
                .options(OpenAiChatOptions.builder()
                        .model(chatSession.getPromptVersion().getModel()))
                .call()
                .content();

        return new ChatbotResponse(aiResponse);
    }
}
