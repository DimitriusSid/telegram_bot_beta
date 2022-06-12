package bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.SendMessageOperationService;
import store.HashMapStore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

import static constant.VarConstant.*;

public class MyFirstBot extends TelegramLongPollingBot {

    private static final String BOT_TOKEN = "5525485207:AAHGXGzPO0kw-08VNfi7dYAJ9H_GWzYORM8";
    private static final String BOT_USERNAME = "MyFirstJavaSidBot";
    private boolean startPlaning;
    private HashMapStore store = new HashMapStore();

    SendMessageOperationService service = new SendMessageOperationService();


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            switch (update.getMessage().getText()) {
                case START:
                    executeMessage(service.crateGreetingMessage(update));
                    executeMessage(service.createInstructionMessage(update));
                    break;
                case START_PLANNING:
                    executeMessage(service.createPlaningMessage(update));
                    startPlaning = true;
                    break;
                case END_PLANNING:
                    startPlaning = false;
                    executeMessage(service.createEndPlaningMessage(update));
                    break;
                case SHOW_DEALS:
                    if (startPlaning == false) {
                        executeMessage(service.createSimpleMessage(update, store.selectAll(LocalDate.now())));
                    }
                    break;
                default:
                    if (startPlaning) {
                        store.save(LocalDate.now(),update.getMessage().getText());
                    }
            }
        }
        if (update.hasCallbackQuery()) {
            String instruction = "Бот-планировщик задач";
            String callDate = update.getCallbackQuery().getData();
            switch (callDate.toLowerCase()) {
                case YES:
                    EditMessageText text = service.createEditMessage(update, instruction);
                    try {
                        execute(text);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    private void executeMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
