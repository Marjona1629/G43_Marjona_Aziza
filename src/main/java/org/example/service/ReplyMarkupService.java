package org.example.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyMarkupService {
    public ReplyKeyboardMarkup keyboardMaker(String[][] buttons){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (String[] button : buttons) {
            KeyboardRow row = new KeyboardRow();
            for (String s : button) {
                KeyboardButton keyboardButton = new KeyboardButton(s);
                row.add(keyboardButton);
            }
            keyboardRows.add(row);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup keyboardMaker(List<String> cardNumbers) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (String cardNumber : cardNumbers) {
            KeyboardRow row = new KeyboardRow();
            KeyboardButton keyboardButton = new KeyboardButton(cardNumber);
            row.add(keyboardButton);
            keyboardRows.add(row);
        }

        KeyboardRow backRow = new KeyboardRow();
        backRow.add(new KeyboardButton("back"));
        keyboardRows.add(backRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }
}