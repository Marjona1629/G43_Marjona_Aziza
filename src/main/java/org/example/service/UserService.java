package org.example.service;

import org.example.entity.User;
import org.example.utils.States;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.example.utils.Util.*;

public class UserService {
    private final SendMessage sendMessage = new SendMessage();
    private final ReplyMarkupService replyService = new ReplyMarkupService();
    private final ServiceMethods serviceMethods = new ServiceMethods();
    private final BotService botService = BotService.getInstance();

    static Long currentCardID;

    public void messageHandler(Update update, User user) {
        Long id = update.getMessage().getChat().getId();

        sendMessage.setReplyMarkup(null);
        sendMessage.setChatId(id);
        String text = update.getMessage().getText();

        if (text != null) {
            switch (text) {
                case "/start" -> {
                    if (user.getName().equals("no-name")) {
                        serviceMethods.updateState(user, States.REGISTRATION);
                        sendMessage.setText("\uD83D\uDC4B Hello! Welcome to our Bot!");
                        botService.executeMessages(sendMessage);
                        sendMessage.setText("Please, enter your name");
                    } else {
                        serviceMethods.updateState(user, States.MAIN);
                        sendMessage.setText("\uD83D\uDC4B Hello! Welcome to our Bot!");
                    }
                }
                case "my cards" -> {
                    serviceMethods.updateState(user, States.MAIN);
                    String res = serviceMethods.showUserCards(user.getId());
                    if (res.isEmpty()){
                        res = "You have no cards yet";
                    }
                    sendMessage.setText(res);
                }
                case "add card" -> {
                    serviceMethods.updateState(user, States.ADDING_NEW_CARD);
                    sendMessage.setText("Enter card number: (NOTE: xxxx-xxxx-xxxx-xxxx)");
                }
                case "transfer" -> {

                }
                case "history" -> {

                }
                case "deposit" -> {

                }

                default -> {
                    if (user.getState() == States.REGISTRATION) {
                        if (text.matches("[a-zA-Z]{2,20}")) {
                            serviceMethods.updateUserName(user, text);
                            sendMessage.setText("Congrats! You have been registered");
                            sendMessage.setReplyMarkup(replyService.keyboardMaker(userMenu));
                            serviceMethods.updateState(user, States.MAIN);
                        } else {
                            sendMessage.setText("Name must include letters!");
                            serviceMethods.updateState(user, States.REGISTRATION);
                        }
                    } else if (user.getState() == States.ADDING_NEW_CARD){
                        if (text.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")){
                            if (!serviceMethods.checkDuplicateNumber(text)){
                                currentCardID = serviceMethods.addNewCard(user.getId(), text);
                                sendMessage.setText("Enter Password for this Card");
                                serviceMethods.updateState(user, States.PUTTING_CARD_PASSWORD);
                            } else {
                                sendMessage.setText("This kind of card is already exist");
                                serviceMethods.updateState(user, States.ADDING_NEW_CARD);
                            }
                        } else {
                            sendMessage.setText("Invalid card number format!");
                            serviceMethods.updateState(user, States.ADDING_NEW_CARD);
                        }

                    } else if (user.getState() == States.PUTTING_CARD_PASSWORD) {
                        serviceMethods.setCardPassword(text, currentCardID);
                        serviceMethods.updateState(user, States.PUTTING_CARD_BALANCE);
                        sendMessage.setText("Enter Balance for this Card");

                    } else if (user.getState() == States.PUTTING_CARD_BALANCE){
                        serviceMethods.setCardBalance(text, currentCardID);
                        sendMessage.setText("Card added successfully");
                        serviceMethods.updateState(user, States.MAIN);
                        sendMessage.setReplyMarkup(replyService.keyboardMaker(userMenu));

                    } else {
                        sendMessage.setText("You are on Main Page");
                        serviceMethods.updateState(user, States.MAIN);
                    }
                }
            }
            if (user.getState() != States.REGISTRATION
                    && user.getState() != States.ADDING_NEW_CARD
                    && user.getState() != States.PUTTING_CARD_PASSWORD
                    && user.getState() != States.PUTTING_CARD_BALANCE){
                sendMessage.setReplyMarkup(replyService.keyboardMaker(userMenu));
            }
            botService.executeMessages(sendMessage);
        }
    }
}