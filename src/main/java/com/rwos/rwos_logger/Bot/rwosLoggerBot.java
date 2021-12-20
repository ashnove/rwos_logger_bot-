package com.rwos.rwos_logger.Bot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import com.rwos.rwos_logger.Entity.MemberEvent;
import com.rwos.rwos_logger.Entity.TeamMember;
import com.rwos.rwos_logger.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
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

    Integer sentMessageId;
    BotSession session = null;
    boolean menuActive = false;     

    @Value("${app.chatid}")
    private String FRESHER_TEAM_CHAT_ID;

    @Value("${app.botid}")
    private String BOT_TOKEN;

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
                Message sentMessage = execute(message);
                sentMessageId = sentMessage.getMessageId();
                menuActive = true;
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            String LOGGER_CHAT_ID = update.getCallbackQuery().getMessage().getChatId().toString();
            SendMessage message = new SendMessage();
            Date currentTimeStamp = new Date();
            String date = getEventDate(currentTimeStamp);
            String time = getEventTime(currentTimeStamp);
            
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
            String setText = "";
            if (call_data.equals("signin")) {
                if(lastLoggedStatus.equals(SIGNED_OFF) || lastLoggedStatus.equals(LEAVE)){
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
                    if(lastLoggedStatus.equals(LUNCH)) setText = "You are at Lunch!";
                    if(lastLoggedStatus.equals(AFK)) setText = "You are AFK!";
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
                Formatter fmt = new Formatter();  
                fmt.format("%10s %13s %8s %8s %8s\n", "Date", "Name", "Status", "LogTime", "Duration");
                String dash = "";
                for(int i = 0; i < 10 + 13 + 8 + 8 + 8 + 4; i++){
                    dash += '-';
                }
                fmt.format("%s\n", dash);
                for (MemberEvent eachEmployeeStatus : allEmployee) {
                    try {
                        Date timeStampOld = eachEmployeeStatus.getEvent_timestamp();
                        Date timeStampNew = new Date();
                        String duration = getDuration(timeStampOld, timeStampNew);
                        String employeeName = userService.getNameByUserId(eachEmployeeStatus.getUserId_fk());
                        String event_time = getEventTime(eachEmployeeStatus.getEvent_timestamp());
                        String event_date = getEventDate(eachEmployeeStatus.getEvent_timestamp());
                        fmt.format("%10s %13s %8s %8s %8s\n", event_date, employeeName.split(" ")[0], eachEmployeeStatus.getEvent_type(), event_time, duration);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }   
                message.enableMarkdownV2(true);
                message.setText( "```" + fmt.toString() + "```");
                fmt.close();
            }
            MemberEvent memberEvent = new MemberEvent();
            List<MemberEvent> status = new ArrayList<>();
            
            if(!statusCheck) message.setText(setText);
            if (!statusCheck && !invalidLog) {
                memberEvent.setEvent_type(currentStatus);
                status.add(memberEvent);
                teamMember.setMember_events(status);
                userService.addLog(teamMember);
            }
            else{ 
                message.setChatId(LOGGER_CHAT_ID);
            }
            try {
                if (!statusCheck && !invalidLog) {
                    memberEvent.setEvent_type(currentStatus);
                    status.add(memberEvent);
                    teamMember.setMember_events(status);
                    userService.addLog(teamMember);
                    message.setChatId(LOGGER_CHAT_ID);
                    execute(message);
                    message.setChatId(FRESHER_TEAM_CHAT_ID);
                    execute(message);
                    message.setText("Testing");
                    execute(message);
                }else{
                    execute(message);    
                }        
                if(menuActive && !invalidLog){
                    DeleteMessage deleteMessage = new DeleteMessage(LOGGER_CHAT_ID, sentMessageId);
                    execute(deleteMessage);
                    menuActive = false;
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    String getDuration(Date timeStampOld, Date timeStampNew){
        String duration = "";
        Long timeDifference = timeStampNew.getTime() - timeStampOld.getTime();
        Long mins = TimeUnit.MILLISECONDS.toMinutes(timeDifference) % 60;
        Long hrs = TimeUnit.MILLISECONDS.toHours(timeDifference) % 24;
        Long days = TimeUnit.MILLISECONDS.toDays(timeDifference);
        if(days > 0) return days + " day" + ((days > 1) ? "s" : "");
        if(hrs > 0) duration += hrs + "h ";
        if(mins > 0)duration += mins + "m";
        else return "0m";
        return duration;
    }
    String getEventTime(Date timeStamp){
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        String time = timeFormatter.format(timeStamp.getTime());
        return time;
    }
    String getEventDate(Date timeStamp){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormatter.format(timeStamp.getTime());
        return date;
    }

    @Override
    public String getBotUsername() {
        return "rwoslogger_bot";
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @AfterBotRegistration
    public void afterBotHookWithSession(BotSession session) {
        // System.out.println("Initiating Session...");
        this.session = session;
    }

    @PreDestroy
    public void destroyComponent() {
        // System.out.println("Terminating Session...");
        session.stop();
    }

}
