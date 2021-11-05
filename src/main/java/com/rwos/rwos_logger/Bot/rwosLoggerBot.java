package com.rwos.rwos_logger.Bot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.rwos.rwos_logger.Entity.Users;
import com.rwos.rwos_logger.Repository.UserRepository;
import com.rwos.rwos_logger.Service.UserService;
import com.rwos.rwos_logger.Utils.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class rwosLoggerBot extends TelegramLongPollingBot {

    @Autowired
    Controller call;
    static String name;
    static String userName;
    static String firstName;
    static String lastName;

    // @Value("${app.base.path}")
    // private String teamChatID;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            SendMessage message = new SendMessage();

            if (command.equals("/start")) {
                message.setText("Welcome to RWOS Logger! Send /menu to begin.");
            }
            if (command.equals("/menu")) {

                name = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();
                firstName = update.getMessage().getFrom().getFirstName();
                lastName = update.getMessage().getFrom().getLastName();
                userName = update.getMessage().getFrom().getUserName();

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

            if (command.equals("/myfirstname")) {
                System.out.println("FirstName: " + update.getMessage().getFrom().getFirstName());
                message.setText(update.getMessage().getFrom().getFirstName());
            }
            if (command.equals("/mylastname")) {
                System.out.println("FirstName: " + update.getMessage().getFrom().getLastName());
                message.setText(update.getMessage().getFrom().getLastName());
            }
            if (command.equals("/myfullname")) {
                System.out.println("FirstName: " + update.getMessage().getFrom().getFirstName() + " "
                        + update.getMessage().getFrom().getLastName());
                message.setText(update.getMessage().getFrom().getFirstName() + " "
                        + update.getMessage().getFrom().getLastName());
            }

            // System.out.println(update.getMessage().getChatId());
            message.setChatId(update.getMessage().getChatId().toString());
            // message.setChatId("-583756566");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            SendMessage message = new SendMessage();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String date_time = dtf.format(now);
            String date = date_time.split(" ")[0];
            String time = date_time.split(" ")[1];
            Users user = new Users();
            user.setDateLog(date);
            user.setTimeLog(time);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUserName(userName);

            if (call_data.equals("signin")) {
                // message.setChatId("-583756566");
                message.setText(date + ": " + name + " has signed in at " + time);
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
                // message.setText("Hey " + name.split(" ")[0] + ", No employees have registered
                // to the programme yet!");
                // List<Users> allUser = userService.getAllStatus();
                // System.out.println(allUser);
            }
            System.out.println(user.toString());

            try {
                call.userCheck(user);

            } catch (Exception e) {
                System.out.println("<><><> " + e.getMessage());
            }

            // try {
            // if (Objects.isNull(userRepo.findByUserName(user.getUserName())))
            // userRepo.save(user);
            // } catch (Exception e) {
            // System.out.println("repo() | " + e.getMessage());
            // }

            message.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
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

}
// current commands:-
// myfirstname - get your first name
// mylastname - get your last name
// myfullname - get your full name
// menu - get list of menus