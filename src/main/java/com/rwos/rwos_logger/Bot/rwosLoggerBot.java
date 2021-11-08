package com.rwos.rwos_logger.Bot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import com.rwos.rwos_logger.Entity.User;
import com.rwos.rwos_logger.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.starter.AfterBotRegistration;

@Component
public class rwosLoggerBot extends TelegramLongPollingBot {

    @Autowired
    private UserService userService;

    BotSession session = null;

    @Value("${app.chatid}")
    private String teamChatID;

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            SendMessage message = new SendMessage();

            if (command.equals("/start")) {
                message.setText("Welcome to RWOS Logger! Send /menu to begin.");
            }
            if (command.equals("/menu")) {

                message.setText("Choose an option:");
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
                InlineKeyboardButton signInButton = new InlineKeyboardButton();
                signInButton.setText("Sign In");
                signInButton.setCallbackData("signin");
                rowInline1.add(signInButton);

                InlineKeyboardButton signOutButton = new InlineKeyboardButton();
                signOutButton.setText("Sign Out");
                signOutButton.setCallbackData("signout");
                rowInline1.add(signOutButton);

                rowsInline.add(rowInline1);

                List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
                InlineKeyboardButton statusButton = new InlineKeyboardButton();
                statusButton.setText("Employee Status");
                statusButton.setCallbackData("status");
                rowInline2.add(statusButton);

                rowsInline.add(rowInline2);

                List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
                InlineKeyboardButton afkButton = new InlineKeyboardButton();
                afkButton.setText("AFK");
                afkButton.setCallbackData("afk");
                rowInline3.add(afkButton);

                InlineKeyboardButton backButton = new InlineKeyboardButton();
                backButton.setText("Back");
                backButton.setCallbackData("back");
                rowInline3.add(backButton);

                rowsInline.add(rowInline3);

                List<InlineKeyboardButton> rowInline4 = new ArrayList<>();
                InlineKeyboardButton leaveButton = new InlineKeyboardButton();
                leaveButton.setText("Leave");
                leaveButton.setCallbackData("leave");
                rowInline4.add(leaveButton);

                rowsInline.add(rowInline4);

                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
            }

            message.setChatId(update.getMessage().getChatId().toString());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            String chat_id = update.getCallbackQuery().getMessage().getChatId().toString();
            SendMessage message = new SendMessage();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String date_time = dtf.format(now);
            String date = date_time.split(" ")[0];
            String time = date_time.split(" ")[1];

            System.out.println("callback name: " + update.getCallbackQuery().getFrom().getFirstName());

            User user = new User();

            String name = update.getCallbackQuery().getFrom().getFirstName() + " "
                    + update.getCallbackQuery().getFrom().getLastName();
            String firstName = update.getCallbackQuery().getFrom().getFirstName();
            String lastName = update.getCallbackQuery().getFrom().getLastName();
            Long userId = update.getCallbackQuery().getFrom().getId();
            user.setDateLog(date);
            user.setTimeLog(time);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUserId(userId);

            boolean statusCheck = false;
            if (call_data.equals("signin")) {
                message.setText(
                        date + ": " + update.getCallbackQuery().getFrom().getFirstName() + " has signed in at " + time);
                user.setStatus("Online");

            } else if (call_data.equals("signout")) {
                message.setText(date + ": " + name + " has signed out at " + time);
                user.setStatus("Offline");

            } else if (call_data.equals("afk")) {
                message.setText(date + ": " + name + " is going " + call_data.toUpperCase() + " at " + time);
                user.setStatus("AFK");

            } else if (call_data.equals("back")) {
                message.setText(date + ": " + name + " is " + call_data.toUpperCase() + " at " + time);
                user.setStatus("Online");

            } else if (call_data.equals("leave")) {
                message.setText(date + ": " + name + " is " + call_data.toUpperCase() + "ING for the day at " + time);
                user.setStatus("Leave");

            } else if (call_data.equals("status")) {
                statusCheck = true;
                List<User> allUser = userService.getAllStatus();
                String listData = "Status:";
                for (User e : allUser) {
                    listData += "\n";
                    listData += e.getFirstName() + ": " + e.getStatus();
                }
                message.setText(listData);

            }
            System.out.println(user.toString());
            if (!statusCheck)
                userService.check(user);
            message.setChatId(teamChatID);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String getBotUsername() {
        return "rwoslogger_bot";
    }

    @Override
    public String getBotToken() {
        return "2083850789:AAEkOGOdbhUsEJM78SMJ_C4tY7l6F2FEcmw";
    }

    @AfterBotRegistration
    public void afterBotHookWithSession(BotSession session) {
        System.out.println("Session stored");
        this.session = session;
    }

    @PreDestroy
    public void destroyComponent() {
        System.out.println("destroing component");
        session.stop();
    }

}
