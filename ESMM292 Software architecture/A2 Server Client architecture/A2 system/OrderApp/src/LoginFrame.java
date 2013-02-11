
import java.sql.*;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LoginFrame.java
 *
 * Created on Jan 28, 2013, 11:38:26 PM
 */

/**
 *
 * @author slav
 */
public class LoginFrame extends javax.swing.JFrame {

    private NewJFrame mainf;
    /** Creates new form LoginFrame */
    public LoginFrame() {
        initComponents();
    }

    public LoginFrame(NewJFrame aFrame)
    {
        this();
        mainf = aFrame;
        mainf.setVisible(false);
    }
    public boolean Login(String aUserName, String aPassword)
    {
         String usernameDB="";
            String passDB = "";

             Connection DBConn = null;
            ResultSet res = null;               // SQL query result set pointer
            Statement s = null;   
        try
            {
                         // SQL statement pointer

            Class.forName( "com.mysql.jdbc.Driver" );
             String SQLServerIP = jTextField2.getText();
            String sourceURL = "jdbc:mysql://" + SQLServerIP + ":3306/orderinfo";
            DBConn = DriverManager.getConnection(sourceURL,Constants.DB_USER,Constants.DB_PASS);
            s = DBConn.createStatement();
            res = s.executeQuery( "Select user_name, pass from users where role_id = 2 and user_name = '"+aUserName+"'" );

            if (res.next())
                {
                    usernameDB = res.getString(1);
                    passDB =  res.getString(2);
                } // while

            } catch (Exception e) {

                jLabel3.setText("Login failed!");
                return false;

            } // end try-catch
            if (aUserName.length() == 0 || aPassword.length() == 0)
            {
                jLabel3.setText("Enter Username and Password!");
                return false;
            }

            if (aUserName.compareTo(usernameDB)  != 0 || aPassword.compareTo(passDB) != 0)
            {
                jLabel3.setText("Wrong Username or Password!");
                return false;
            }



          //we are logged
          try
          {
              Calendar rightNow = Calendar.getInstance();
             DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String dateTimeStamp =dateFormat.format(rightNow.getTime());

              s = DBConn.createStatement();
              String logstm = ("INSERT INTO log (user_name, event, event_date)"+
                      "VALUES ('"+aUserName+"', 'login', '" + dateTimeStamp + "' );");
                    s = DBConn.createStatement();
            s.executeUpdate(logstm);


          } catch (Exception e) {

                jLabel3.setText("Login failed!");
                return false;
         } // end try-catch
         jLabel3.setText("");

         this.mainf.setUsrData(usernameDB, jTextField2.getText());
         this.mainf.setVisible(true);
         this.dispose();

        return true;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Login");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Username");

        jLabel2.setText("Password");

        jLabel3.setForeground(new java.awt.Color(255, 0, 0));

        jTextField2.setText("localhost");

        jLabel11.setText("Database IP:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE))
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(163, 163, 163))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(31, 31, 31))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String usr = jTextField1.getText();
        String pass = new String(jPasswordField1.getPassword());
        Login(usr, pass);
    }//GEN-LAST:event_jButton1ActionPerformed

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

}