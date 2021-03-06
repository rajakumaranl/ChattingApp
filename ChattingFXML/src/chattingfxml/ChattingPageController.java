/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattingfxml;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

/**
 * FXML Controller class
 *
 * @author rajakumaran
 */
public class ChattingPageController implements Initializable {

    @FXML private Label userName;
    @FXML private TextArea chatArea;
    @FXML private TextField toMsg;
    @FXML private TextField newName;
    @FXML private TextField toAddress;
    @FXML private Button sendBtn;
    @FXML private ListView listView;
    private XMPPConnection connection;
    private Chat chat ;
    private String xmppServerIP="10.10.1.113";
    MessageListener incomingMessageListener;
    private String fromUser;
    @FXML private TextField name;
    public Presence status;
    //private Presence userStatus;
    public void setUserName(String uName){
        userName.setText(uName);
        fromUser=uName;
    }
    
    public void setXMPPConnection(XMPPConnection con) throws XMPPException{
        this.connection = con;
        chatMessageListner();
        incomingMessage();
        System.out.println("Connection object instace got...s");
        getBuddylist(); 
        
    }
    
    public final ObservableList<String> currentList=FXCollections.observableArrayList();        
       
    @FXML
    public void addName(ActionEvent event) throws Exception {
        String newBuddy=newName.getText();
        currentList.add(newBuddy+"@"+xmppServerIP);
        createEntry(newBuddy, newBuddy);
        newName.clear();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}  
    
    private void chatMessageListner(){
        incomingMessageListener = new MessageListener() {
        @Override
            public void processMessage(Chat chat, Message msg) {
                chatArea.appendText(msg.getFrom()+" : "+msg.getBody()+"\n");
                String newBuddies[]=msg.getFrom().split("@");
                try{
                createEntry(newBuddies[0], newBuddies[0]);
                }catch(Exception e){
                    System.out.println("Exception while adding the incoming user in Roster list "+e);
                }
            }
        };
    }
    
    public void getBuddylist(){
        
        try{
            currentList.clear();
            listView.getSelectionModel().selectedItemProperty().addListener(
                             new ChangeListener<String>() {
                @Override 
                public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                    String address=t1.split(" ")[0];
                    
                    toAddress.setText(address);
                }
            });
            Roster roster = connection.getRoster();
            Collection<RosterEntry> entries = roster.getEntries();		
            rosterListner(roster);
            for(RosterEntry entry : entries) {
                roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
                currentList.add(entry.getUser()+" "+roster.getPresence(entry.getUser()));
            }            
            listView.setItems(currentList);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
 
    @FXML
    public void sendMessage(ActionEvent event) throws XMPPException, Exception {
           
        String toBuddy=toAddress.getText();
        String fromBuddy = fromUser;
        String msgToSend = toMsg.getText();
        System.out.println("From = " +fromBuddy+" Message = " + toMsg.getText());
        chatMessage(fromBuddy,toBuddy,msgToSend);
        
        if(!msgToSend.isEmpty()){
            if(!fromBuddy.isEmpty()){
                try{
                    chat.sendMessage(msgToSend);
                    toMsg.clear();
                }
                catch(Exception e){
                    System.out.println("Exception in Sending message "+e);
                }
            }
        }
    }
    
    public void createEntry(String buddyname, String nickname) throws Exception {
        String user= buddyname+"@"+xmppServerIP;
        Roster roster = connection.getRoster();
        Collection<RosterEntry> entries = roster.getEntries();		
        for(RosterEntry entry : entries) {
            System.out.println(String.format("Buddy:%1$s - Status:%2$s", entry.getName(), entry.getStatus()));
            if(!user.equalsIgnoreCase(entry.getName())){
                System.out.println(String.format("Creating entry for buddy '%1$s' with name %2$s", user, nickname));
                roster.createEntry(user, nickname, null);
                roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
            }else{
                System.out.println(String.format("Buddy '%1$s' with name %2$s is already existing in buddylist", user, nickname));
            }
        }
    }
    
    public void chatMessage(String fromuser,String toadd,String msgToSend){
        String toBuddyMail = toadd;
        chatArea.appendText("You : "+msgToSend+"\n");
        chat = connection.getChatManager().createChat(toBuddyMail, incomingMessageListener);
    }

    private void incomingMessage(){
        connection.getChatManager().addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean bln) {
                
                chat.addMessageListener(incomingMessageListener);
            }
        });
    }
    
    public void rosterListner(Roster roster){
        
        roster.addRosterListener(new RosterListener() {
            @Override
            public void entriesDeleted(Collection<String> addresses) { getBuddylist();}
            @Override
            public void entriesUpdated(Collection<String> addresses) { getBuddylist();}
            @Override
            public void presenceChanged(Presence presence) { getBuddylist(); }
            @Override
            public void entriesAdded(Collection<String> clctn) { getBuddylist(); }
        });
    }
}