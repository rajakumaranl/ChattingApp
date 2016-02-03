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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * FXML Controller class
 *
 * @author rajakumaran
 */
public class UserCreationFXMLController implements Initializable {
    
    @FXML private Button createNewUserBtn;
    @FXML private Button createAnotherBtn;
    @FXML private Button cancelBtn;
    @FXML private Button resetBtn;
    @FXML TextField userName;
    @FXML TextField fullName;
    @FXML PasswordField password;
    @FXML TextField eMail;
    //@FXML TextField serverAddress;
    @FXML private Label alertmsg;
    private Parent root;
    private Stage stage;
    private final String server="10.10.1.113";
    private final int port=5223;
    private ConnectionConfiguration config;
    private XMPPConnection connection;
    //private TextField serveraddress;
    
    
    @FXML
    public void createNewUser(ActionEvent event) {
        String username=userName.getText(); 
        String name=fullName.getText();
        String pass=password.getText(); 
        //String xmppServerIP=serverAddress.getText();
        String EMailID=eMail.getText();
        try {
            
            Map<String, String> attributes = new HashMap<String, String>();
            attributes.put("username", username);
            attributes.put("name", name);
            attributes.put("password", pass);
            attributes.put("email", EMailID);
            AccountManager oAccountManager = connection.getAccountManager();
            oAccountManager.createAccount(username, pass, attributes);
            resetPage(event);
            alertmsg.setText("User "+username+" is Created Successfully");
        } catch (XMPPException ex) {
            System.out.println("Exception"+ex);
            ex.printStackTrace();
            //Logger.getLogger(ChattingPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    public void resetPage(ActionEvent event){
        userName.clear();;
        fullName.clear();
        password.clear();
        eMail.clear();
    }
    
    @FXML
    public void cancelPage(ActionEvent event){
        try {
            stage = (Stage) cancelBtn.getScene().getWindow();
            stage.setTitle("New User Creation");
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            scene.getStylesheets().add(ChattingFXML.class.getResource("Login.css").toExternalForm());
            stage.show();
        } catch (IOException ex) {
            System.out.println("Exception"+ex);
            ex.printStackTrace();
        }
    }
    
    @FXML
    public void createAnotherUser(ActionEvent event){
        try {
            createNewUser(event);
            resetPage(event);
            stage.setTitle("New User Creation");
            Parent root = FXMLLoader.load(getClass().getResource("UserCreationFXML.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            scene.getStylesheets().add(ChattingFXML.class.getResource("Login.css").toExternalForm());
            stage.show();
        } catch (IOException ex) {
            System.out.println("Exception"+ex);
            ex.printStackTrace();            
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    void setXMPPConnection(XMPPConnection connection) {
        this.connection = connection;
        
        System.out.println("Connection object instace got...s");

    }
    
}
