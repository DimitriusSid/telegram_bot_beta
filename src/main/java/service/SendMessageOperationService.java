package service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

import static constant.VarConstant.*;

public class SendMessageOperationService {

    private final String GREETING_MESSAGE = "Привет, приступим к планировнию";
    private final String PLANNING_MESSAGE = "Вводите дела, после планирования нажмите кнопку \"Закончить планирование\"";
    private final String END_PLANNING_MESSAGE = "Планирование окончено, для просмотра нажмите кнопку \"Показать дела\"";
    private final String INSTRUCTIONS = "Хотите прочесть инструкцию";
    private final ButtonService buttonService = new ButtonService();

    public SendMessage crateGreetingMessage(Update update) {
        SendMessage message = createSimpleMessage(update, GREETING_MESSAGE);
        ReplyKeyboardMarkup keyboardMarkup =
                buttonService.setButtons(buttonService.createButtons(List.of(START_PLANNING, END_PLANNING, SHOW_DEALS)));
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    public SendMessage createPlaningMessage(Update update) {
        return createSimpleMessage(update, PLANNING_MESSAGE);
    }

    public SendMessage createEndPlaningMessage(Update update) {
        return createSimpleMessage(update, END_PLANNING_MESSAGE);
    }

    public SendMessage createSimpleMessage(Update update, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(message);
        return sendMessage;
    }

    public SendMessage createSimpleMessage(Update update, List<String> messages) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        StringBuilder message = new StringBuilder();
        for (String s : messages) {
            message.append(s + "\n");
        }
        sendMessage.setText(message.toString());
        return sendMessage;
    }

    public SendMessage createInstructionMessage(Update update) {
        SendMessage sendMessage = createSimpleMessage(update, INSTRUCTIONS);
        InlineKeyboardMarkup inlineKeyboardMarkup =
                buttonService.setInlineKeyMarkup(buttonService.createInLineButton(YES));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    public EditMessageText createEditMessage(Update update, String instruction) {
        EditMessageText editMessageText = new EditMessageText();
        long mesId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(Integer.valueOf(String.valueOf(mesId)));
        editMessageText.setText(instruction);
        return editMessageText;
    }
}
