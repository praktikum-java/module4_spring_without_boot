package ru.practicum.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class UserControllerTestConfig {
    @Bean
    public UserService userService() {
        return mock(UserService.class);
    }
}
