package org.example.service;

import org.example.entity.User;
import org.example.utils.States;
import org.example.utils.TestConnection;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.example.utils.Util.*;

public class BotService extends TelegramLongPollingBot {

    private final static UserService userService = new UserService();
    private final static TestConnection testConnection = TestConnection.getInstance();

    @Override
    public void onUpdateReceived(Update update) {
        if (update == null) {
            System.out.println("Update is null");
            return;
        }

        if (update.hasMessage() && update.getMessage().getChat() != null) {
            handleIncomingMessage(update);
        } else {
            System.out.println("something went wrong");
        }
    }

    private void handleIncomingMessage(Update update) {
        testConnection.getConnection();
        Message message = update.getMessage();
        Long chatId = message.getChat().getId();
        Optional<User> optionalUser = getUserById(chatId);

        User user1;
        if (optionalUser.isEmpty()) {
            User user = new User(chatId, "no-name", States.REGISTRATION);
            save(user);
            user1 = user;
        } else {
            if (optionalUser.get().getName().equals("no-name")){
                user1 = optionalUser.get();
                user1.setState(States.REGISTRATION);
            } else {
                user1 = optionalUser.get();
            }
        }

        userService.messageHandler(update, user1);
    }

    public void executeMessages(SendMessage... messages) {
        for (SendMessage message : messages) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "t.me/g43AzizaMarjona_bot";
    }

    @Override
    public String getBotToken() {
        return "7342035529:AAEWDjLQ1fT27l3OJHqVffoD-elWfhIxly0";
    }

    private static BotService botService;
    public static BotService getInstance(){
        if (botService == null){
            botService = new BotService();
        }
        return botService;
    }
}