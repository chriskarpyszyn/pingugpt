package dev.chriskarpyszyn.pingugpt;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionScopedChatMemory {

    private final ChatMemory chatMemory = new InMemoryChatMemory();

    public ChatMemory getChatMemory() {
        return chatMemory;
    }
}

