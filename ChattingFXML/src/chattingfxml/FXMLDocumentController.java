/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattingfxml;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
/**
 *
 * @author rajakumaran
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML private Button login;
    @FXML private Button signup;
    @FXML TextField userName;
    @FXML PasswordField password;
    private Parent root;
    public Stage stage;
    private final String server="10.10.1.113";
    private final int port=5223;
    private ConnectionConfiguration config;
    private XMPPConnection connection;
    private TextField serveraddress;
    private Label alertmsg;
    //BuddylistManager buddyListmgr=new BuddylistManager();
    
    @FXML    
    private void performLogin(ActionEvent event) throws XMPPException {
        
        if(!userName.getText().isEmpty()){
            System.out.println("User name is : "+userName.getText());
            String loginName=userName.getText();
            System.out.println("LogIN User name is : "+loginName);
            stage = (Stage) login.getScene().getWindow();
            if (connection!=null && connection.isConnected()) {
                //connection.login("testxmpp", "test12");
                try{
                    connection.login(userName.getText(),password.getText());
                }catch(Exception e){
                   System.out.println("username or password wrong"+e);
                   alertmsg.setText("username or password wrong");
                }
            }
            try{
                FXMLLoader oFXMLLoader = new FXMLLoader(getClass().getResource("ChattingPage.fxml"));
                root = (Parent) oFXMLLoader.load();
                try{
                    System.out.println("User name is : "+loginName);
                    ChattingPageController oChatPageController = oFXMLLoader.getController();
                    oChatPageController.setXMPPConnection(connection);
                    oChatPageController.setUserName(loginName);
                    //buddyListmgr.setXMPPConnection(connection);
                    //buddyListmgr.getBuddyList(currentList, serveraddress, listView);
                }catch(Exception e){
                    System.out.println("Exception in setting username");
                    e.printStackTrace();
                }
            }catch(Exception e){
                System.out.println("Failed to load ChattingPage.fxml"+e);
            }
            stage.setTitle("Login");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(ChattingFXML.class.getResource("Login.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }   
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            
            System.out.println(String.format("Initializing connection to server %1$s port %2$d", server, port));
            config = new ConnectionConfiguration(server, port);
            config.setSASLAuthenticationEnabled(false);
            config.setSecurityMode(SecurityMode.disabled);
            connection = new XMPPConnection(config);
            connection.connect();
            System.out.println("Connected: " + connection.isConnected());  
        }catch(Exception e){
            System.out.println("username or password wrong"+e);
            alertmsg.setText("username or password wrong");
        }
    }
    
    @FXML
    public void createNewUser(ActionEvent event){
           
        stage = (Stage) signup.getScene().getWindow();
        if (connection!=null && connection.isConnected()) {

            try{
                FXMLLoader oFXMLLoader = new FXMLLoader(getClass().getResource("UserCreationFXML.fxml"));
                root = (Parent) oFXMLLoader.load();
                try{                    
                    UserCreationFXMLController oUserCreation = oFXMLLoader.getController();
                    oUserCreation.setXMPPConnection(connection);
                }catch(Exception e){
                    System.out.println("Exception in setting username");
                    e.printStackTrace();
                }
                stage.setTitle("New User Creation");
                Scene scene = new Scene(root);
                stage.setScene(scene);
                scene.getStylesheets().add(ChattingFXML.class.getResource("Login.css").toExternalForm());
                stage.show();
            }catch(Exception e){
                System.out.println("Failed to load ChattingPage.fxml"+e);
            }                
        }
    }
}
