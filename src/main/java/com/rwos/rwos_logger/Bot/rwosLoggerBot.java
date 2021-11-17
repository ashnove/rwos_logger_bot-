package com.rwos.rwos_logger.Bot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PreDestroy;

import com.rwos.rwos_logger.Entity.MemberEvent;
import com.rwos.rwos_logger.Entity.TeamMember;
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
    private String FRESHER_TEAM_CHAT_ID;

    private static String SIGNED_IN = "Online";
    private static String SIGNED_OFF = "Offline";
    private static String AFK = "AFK";
    private static String LUNCH = "Lunch";
    private static String BACK = "Online";
    private static String LEAVE = "Leave";


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

                InlineKeyboardButton lunchButton = new InlineKeyboardButton();
                lunchButton.setText("Lunch");
                lunchButton.setCallbackData("lunch");
                rowInline4.add(lunchButton);

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
            String LOGGER_CHAT_ID = update.getCallbackQuery().getMessage().getChatId().toString();
            SendMessage message = new SendMessage();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String date_time = dtf.format(now);
            String date = date_time.split(" ")[0];
            String time = date_time.split(" ")[1];

            System.out.println("callback name: " + update.getCallbackQuery().getFrom().getFirstName());

            TeamMember teamMember = new TeamMember();

            String member_name = update.getCallbackQuery().getFrom().getFirstName() + " "
                    + update.getCallbackQuery().getFrom().getLastName();

            Long userId = update.getCallbackQuery().getFrom().getId();
            teamMember.setUserId(userId);
            teamMember.setMember_name(member_name);

            boolean statusCheck = false;
            boolean invalidLog = false;
            String currentStatus = "";

            MemberEvent lastLoggedEvent = new MemberEvent();
            lastLoggedEvent = userService.getLastLoggedData(teamMember);
            String lastLoggedStatus = SIGNED_OFF;

            if(!Objects.isNull(lastLoggedEvent)){
                lastLoggedStatus = lastLoggedEvent.getEvent_type();
            }
            System.out.println("lastLoggedStatus: " + lastLoggedStatus);
            String setText = "";
            if (call_data.equals("signin")) {
                
                if(lastLoggedStatus.equals(SIGNED_OFF)){
                    setText = date + ": " + update.getCallbackQuery().getFrom().getFirstName() + " has signed in at " + time;
                    currentStatus = SIGNED_IN;
                }
                else{
                    invalidLog = true;
                    setText = "You are already Signed In!";
                }
                

            } else if (call_data.equals("signout")) {
                if(lastLoggedStatus.equals(SIGNED_IN) || lastLoggedStatus.equals(LEAVE)){
                    setText = date + ": " + member_name + " has signed out at " + time;
                    currentStatus = SIGNED_OFF;
                }
                else{
                    invalidLog = true;
                    if(lastLoggedStatus.equals(SIGNED_OFF)) setText = "You have already Signed Off!";
                    if(lastLoggedStatus.equals(LEAVE)) setText = "You are on Leave!";
                }

            } else if (call_data.equals("afk")) {
                if(lastLoggedStatus.equals(SIGNED_IN)){
                    setText = date + ": " + member_name + " is going " + call_data.toUpperCase() + " at " + time;
                    currentStatus = AFK;
                }
                else{
                    invalidLog = true;
                    if(lastLoggedStatus.equals(AFK) || lastLoggedStatus.equals(LUNCH) ) setText = "You are already AFK!";
                    if(lastLoggedStatus.equals(SIGNED_OFF)) setText = "You have Signed Off already!";
                    if(lastLoggedStatus.equals(LEAVE)) setText = "You are on Leave!";
                }

            } else if (call_data.equals("back")) {
                if(lastLoggedStatus.equals(AFK) || lastLoggedStatus.equals(LUNCH)){
                    setText = date + ": " + member_name + " is " + call_data.toUpperCase() + " at " + time;
                    currentStatus = BACK;
                }
                else{
                    invalidLog = true;
                    setText = "You are already available!";
                    if(lastLoggedStatus.equals(SIGNED_OFF)) setText = "You have Signed Off already!";
                    if(lastLoggedStatus.equals(LEAVE)) setText = "You are on Leave!";
                }

            } else if (call_data.equals("leave")) {
                setText = date + ": " + member_name + " is going on " + call_data.toUpperCase() + " at " + time;
                currentStatus = LEAVE;
            } else if (call_data.equals("lunch")) {
                if(lastLoggedStatus.equals(SIGNED_IN)){
                    setText = date + ": " + member_name + " is going on a " + call_data.toUpperCase() + " BREAK at " + time;
                    currentStatus = LUNCH;
                }
                else{
                    invalidLog = true;
                    if(lastLoggedStatus.equals(AFK) || lastLoggedStatus.equals(LUNCH) ) setText = "You are already AFK!";
                    if(lastLoggedStatus.equals(SIGNED_OFF)) setText = "You have Signed Off already!";
                    if(lastLoggedStatus.equals(LEAVE)) setText = "You are on Leave!";
                }

            } else if (call_data.equals("status")) {
                statusCheck = true;
                List<MemberEvent> allEmployee = userService.getAllEmployeeStausDetails();
                String listData = "Status:";
                for (MemberEvent eachEmployeeStatus : allEmployee) {
                    listData += "\n";
                    try {
                        String employeeName = userService.getNameByUserId(eachEmployeeStatus.getUserId_fk());
                        listData += employeeName + ": " + eachEmployeeStatus.getEvent_type();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                message.setText(listData);

            }
            System.out.println(teamMember.toString());
            MemberEvent memberEvent = new MemberEvent();
            List<MemberEvent> status = new ArrayList<>();
            
            if(!statusCheck) message.setText(setText);
            if (!statusCheck && !invalidLog) {
                memberEvent.setEvent_type(currentStatus);
                status.add(memberEvent);
                teamMember.setMember_events(status);
                userService.addLog(teamMember);
                message.setChatId(LOGGER_CHAT_ID);
            }
            else{ 
                message.setChatId(LOGGER_CHAT_ID);
            }
            
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
        System.out.println("Session stored...");
        this.session = session;
    }

    @PreDestroy
    public void destroyComponent() {
        System.out.println("Destroying Component...");
        session.stop();
    }

}
