/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import static javafx.LoginAdminController.infoBox;
import static javafx.ManageAppointmentController.infoBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import static javax.swing.UIManager.getString;

/*******************************************************************************************************************/
public class DoctorPortalController implements Initializable {

    @FXML
    private JFXComboBox<String> combobox;
    @FXML
    private JFXTextField search_box;
    @FXML
    private JFXButton search_B;
     ObservableList<String> list2 = FXCollections.observableArrayList();
     private Connexion db;
    @FXML
    private JFXTextArea treatmentN_box;
    @FXML
    private JFXTextArea diagN_box;
    @FXML
    private JFXTextField age_box;
    @FXML
    private JFXTextArea treatmentE_box;
    @FXML
    private JFXTextArea diagE_box;
    @FXML
    private JFXTextField situation_box;
    public static String  idPatient=" ";
 /*************************************************************************************************************/
      @FXML
    private void combobox_MouseClicke(MouseEvent event) {
       
    }
 /************************************************************************************************************/
     public void loadData2(){
       list2.clear();
    
         try {
             Connection con=Connexion.ConnecrDB();
         
            ResultSet rs = con.createStatement().executeQuery("SELECT concat(concat(id_pat,' '),info_P) FROM rdv where date_rdv='" +ManagePatientController.currentDay()+"' ");//
            while (rs.next()) {
                //get string from db,whichever way 
                list2.add(new String(rs.getString(1)));
            }

        } catch (Exception ex) {
            System.err.println("Error"+ex);
        }
   }
  /***************************************************************************************************************/
   @FXML
    private void insertData(ActionEvent event) {
        
         String id =search_box.getText();
      String medicament =treatmentN_box.getText();
       String diag =diagN_box.getText();
     String patient_inf=combobox.selectionModelProperty().getValue().getSelectedItem();
    
         if(id.isEmpty()|| patient_inf.isEmpty() || medicament.isEmpty()|| diag.isEmpty())
         
         {
             ManageAppointmentController.infoBox2("Please Fill Out The Form ", null, "Form Error!");  
         }
         else{
        String sql="INSERT INTO traitement (id_pat,nom_medc) VALUES(?,?)";
        String sql2="INSERT INTO diagnostic (id_pat,nom_diag) VALUES(?,?)";
       Connection conn;
                   try {
                  conn=Connexion.ConnecrDB();
                  PreparedStatement preparedSt=conn.prepareStatement(sql);
                   preparedSt.setString(1,id);
                  preparedSt.setString(2, medicament);
                  preparedSt.execute();
                  PreparedStatement preparedSt2=conn.prepareStatement(sql2);
                  preparedSt2.setString(1,id);
                  preparedSt2.setString(2, diag);
                  preparedSt2.execute();
                  ManageAppointmentController.infoBox("Medical file Added Successfully", null, "Success");
              /*    search_box.clear();
                  treatmentN_box.clear();
                  diagN_box.clear();*/
              
                    } 
                   catch (Exception e)
                        {
                        //infoBox2("You should select a row", null, "Failed");     
                        }     
    }}
 /************************************************************************************************************/
    @FXML
    private void searchePatInfo(ActionEvent event) {
        String id =search_box.getText();
   String sqls="Select age, situation_fam ,nom_medc,nom_diag from patient,traitement,diagnostic where patient.id_pat= traitement.id_pat and diagnostic.id_pat = patient.id_pat and patient.id_pat='" +id+"'";
                                                                                              
   try{
               Connection conn=Connexion.ConnecrDB();
               PreparedStatement preparedSt=conn.prepareStatement(sqls);
               ResultSet result=preparedSt.executeQuery();      
               while(result.next())
               { 
                   age_box.setText(result.getString("age"));
                   situation_box.setText(result.getString("situation_fam"));
                   treatmentE_box.setText(result.getString("nom_medc"));
                   diagE_box.setText(result.getString("nom_diag"));
               }     
               }
   catch(Exception e){}
    }
   
 /****************************************************************************************************************/
     public int age_patient()
     {
        String id= search_box.getText();
         int age=0;String date_naiss="";
         String req="Select dateN_pat from patient where id_pat='" +id+"' ";
          try{
                            Connection conn=Connexion.ConnecrDB();
                            PreparedStatement preparedSt=conn.prepareStatement(req);
                            ResultSet result=preparedSt.executeQuery();

                            if(result.next())    date_naiss=result.getString(1);
                             
             }
          catch(Exception e){}  
        
          char c=date_naiss.charAt(4);
          String mano = date_naiss.substring(0,4); 
        int year = Integer.parseInt(mano); 
         return age=2018-year;
     }
     
 /***************************************************************************************************************/
    @FXML
    public void log_out(ActionEvent event) throws IOException {
        Parent loginAdmin = FXMLLoader.load(getClass().getResource("LoginDoc.fxml"));
           Scene ab = new Scene(loginAdmin);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(ab);
            window.show(); }   

/******************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData2();
     combobox.setItems(list2);
     db = new Connexion();
    // ManageAppointmentController. infoBox(age_patient()+"", null, "Success");
     
    }  

    @FXML
    private void print(ActionEvent event) throws IOException {
        idPatient=search_box.getText();
          Parent loginAdmin = FXMLLoader.load(getClass().getResource("Prescrition.fxml"));
           Scene ab = new Scene(loginAdmin);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(ab);
            window.show(); 
    }
    
    
     
    

    
    
  /***************************************************************************************************************/
       
    
}
