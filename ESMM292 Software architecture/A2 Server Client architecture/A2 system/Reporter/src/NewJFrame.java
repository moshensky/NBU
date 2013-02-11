
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;

/******************************************************************************
* File:NewJFrame.java
* Course: 17655
* Project: Assignment 2
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 November 2009 - Initial rewrite of original assignment 2 (ajl).
*
* This class defines a GUI application that allows EEP order takers to enter
* phone orders into the database. 
*
******************************************************************************/

/**
 *
 * @author lattanze
 */
public class NewJFrame extends javax.swing.JFrame {

    String versionID = "v2.10.10";

    private String USER_NAME = "";
    private String DB_URL="";
    /** Creates new form NewJFrame */
    public NewJFrame() {
        initComponents();
        jLabel1.setText("Reporter " + versionID);
    }


    public void WriteFile(String aFileName, String aText)
    {

        PrintStream outs = null;			// File stream reference.
        //Create PrintStream to output file
        try {
            outs = new PrintStream(new FileOutputStream(aFileName, false));
            outs.print(aText);
        } catch (FileNotFoundException e) {
            System.out.println(this.getName() + "::Cant Open file for writing!");
            return;
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabelUser = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Report creator");

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("Get Login/Logout log");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Get Ordering log");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Get Shipping log");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel2.setText("Messages:");

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane3.setViewportView(jTextArea3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelUser)
                        .addGap(158, 158, 158)
                        .addComponent(jLabel1)
                        .addGap(33, 33, 33)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)
                        .addGap(242, 242, 242))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 368, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(446, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabelUser)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(128, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        // jButton1 is responsible for querying the inventory database and
        // getting the tree inventory. Once retieved, the tree inventory is
        // displayed in jTextArea1. From here the user can select an inventory
        // item by triple clicking the item.

        // Database parameters
        Boolean connectError = false;       // Error flag
        Connection DBConn = null;           // MySQL connection handle
        String errString = null;            // String for displaying errors
        String msgString = null;            // String for displaying non-error messages
        ResultSet res = null;               // SQL query result set pointer
        Statement s = null;                 // SQL statement pointer

        // Connect to the inventory database
        try
        {
            msgString = ">> Establishing Driver...";
            jTextArea1.setText("\n"+msgString);

            //load JDBC driver class for MySQL
            Class.forName( "com.mysql.jdbc.Driver" );

            msgString = ">> Setting up URL...";
            jTextArea1.append("\n"+msgString);

            //define the data source
            String SQLServerIP = DB_URL;
            String sourceURL = "jdbc:mysql://" + SQLServerIP + ":3306/orderinfo";

            msgString = ">> Establishing connection with: " + sourceURL + "...";
            jTextArea1.append("\n"+msgString);

            //create a connection to the db - note the default account is "remote"
            //and the password is "remote_pass" - you will have to set this
            //account up in your database

            DBConn = DriverManager.getConnection(sourceURL,Constants.DB_USER,Constants.DB_PASS);

        } catch (Exception e) {

            errString =  "\nProblem connecting to database:: " + e;
            jTextArea1.append(errString);
            connectError = true;

        } // end try-catch

        // If we are connected, then we get the list of trees from the
        // inventory database
        
        if ( !connectError )
        {
            try
            {
                s = DBConn.createStatement();
                res = s.executeQuery( "SELECT user_name, event, event_date FROM log order by event_id" );

                //Display the data in the textarea
                
                jTextArea1.setText("");

                while (res.next())
                {
                    msgString = "User: "+res.getString(1) + ", Event: " + res.getString(2) + ", Datetime: " + res.getString(3);
                    jTextArea1.append(msgString+"\n");

                } // while
                
            } catch (Exception e) {

                errString =  "\nProblem getting tree inventory:: " + e;
                jTextArea1.append(errString);

            } // end try-catch

            Calendar rightNow = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateTimeStamp =format.format(rightNow.getTime());
            String filename="Log-in-out-"+dateTimeStamp+".log";
            WriteFile(filename, jTextArea1.getText());
            JOptionPane.showMessageDialog(this, "Log file with order creators'login/logout data is created:"+filename);

        } // if connect check
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // jButton2 is responsible for querying the inventory database and
        // getting the seed inventory. Once retieved, the seed inventory is
        // displayed in jTextArea1. From here the user can select an inventory
        // item by triple clicking the item.

        Boolean connectError = false;       // Error flag
        Connection DBConn = null;           // MySQL connection handle
        String errString = null;            // String for displaying errors
        String msgString = null;            // String for displaying non-error messages
        ResultSet res = null;               // SQL query result set pointer
        Statement s = null;                 // SQL statement pointer

        // Connect to the inventory database
        try
        {
            msgString = ">> Establishing Driver...";
            jTextArea1.setText("\n"+msgString);

            //load JDBC driver class for MySQL
            Class.forName( "com.mysql.jdbc.Driver" );

            msgString = ">> Setting up URL...";
            jTextArea1.append("\n"+msgString);

            //define the data source
            String SQLServerIP = DB_URL;
            String sourceURL = "jdbc:mysql://" + SQLServerIP + ":3306/orderinfo";

            msgString = ">> Establishing connection with: " + sourceURL + "...";
            jTextArea1.append("\n"+msgString);

            //create a connection to the db - note the default account is "remote"
            //and the password is "remote_pass" - you will have to set this
            //account up in your database

            DBConn = DriverManager.getConnection(sourceURL,Constants.DB_USER,Constants.DB_PASS);

        } catch (Exception e) {

            errString =  "\nProblem connecting to database:: " + e;
            jTextArea1.append(errString);
            connectError = true;

        } // end try-catch

        // If we are connected, then we get the list of seeds from the
        // inventory database

        if ( !connectError )
        {
            try
            {
                s = DBConn.createStatement();
                res = s.executeQuery( "SELECT order_creator, order_id, order_date FROM orders ORDER BY order_id" );

                //Display the data in the textarea
                
                jTextArea1.setText("");

                while (res.next())
                {
                    msgString = "Order creator:"+res.getString(1) + ", Order number: " + res.getString(2) +
                          ", Datetime: " + res.getString(3);
                    jTextArea1.append(msgString+"\n");

                } // while

            } catch (Exception e) {

                errString =  "\nProblem getting seed inventory:: " + e;
                jTextArea1.append(errString);

            } // end try-catch
            Calendar rightNow = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateTimeStamp =format.format(rightNow.getTime());
            String filename="Log-orders-"+dateTimeStamp+".log";
            WriteFile(filename, jTextArea1.getText());
            JOptionPane.showMessageDialog(this, "Log file with orders created is created:"+filename);

        } // if connect check
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // jButton3 is responsible for querying the inventory database and
        // getting the shrub inventory. Once retieved, the shrub inventory is
        // displayed in jTextArea1. From here the user can select an inventory
        // item by triple clicking the item.

        Boolean connectError = false;       // Error flag
        Connection DBConn = null;           // MySQL connection handle
        String errString = null;            // String for displaying errors
        String msgString = null;            // String for displaying non-error messages
        ResultSet res = null;               // SQL query result set pointer
        Statement s = null;                 // SQL statement pointer

        // Connect to the inventory database
        try
        {
            msgString = ">> Establishing Driver...";
            jTextArea1.setText("\n"+msgString);

            //load JDBC driver class for MySQL
            Class.forName( "com.mysql.jdbc.Driver" );

            msgString = ">> Setting up URL...";
            jTextArea1.append("\n"+msgString);

            //define the data source
            String SQLServerIP = DB_URL;
            String sourceURL = "jdbc:mysql://" + SQLServerIP + ":3306/orderinfo";

            msgString = ">> Establishing connection with: " + sourceURL + "...";
            jTextArea1.append("\n"+msgString);

            //create a connection to the db - note the default account is "remote"
            //and the password is "remote_pass" - you will have to set this
            //account up in your database

            DBConn = DriverManager.getConnection(sourceURL,Constants.DB_USER,Constants.DB_PASS);

        } catch (Exception e) {

            errString =  "\nProblem connecting to database:: " + e;
            jTextArea1.append(errString);
            connectError = true;

        } // end try-catch

        // If we are connected, then we get the list of shrubs from the
        // inventory database

        if ( !connectError )
        {
            try
            {
                s = DBConn.createStatement();
                 res = s.executeQuery( "SELECT order_id, shipped_date FROM orders where shipped=1 ORDER BY order_id" );

                //Display the data in the textarea

                jTextArea1.setText("");

                while (res.next())
                {
                     msgString = "Order number: " + res.getString(1) +
                          ", Datetime: " + res.getString(2);
                    jTextArea1.append(msgString+"\n");

                } // while

            } catch (Exception e) {

                errString =  "\nProblem getting shrubs inventory:: " + e;
                jTextArea1.append(errString);

            } // end try-catch
              Calendar rightNow = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateTimeStamp =format.format(rightNow.getTime());
            String filename="Log-shipping-"+dateTimeStamp+".log";
            WriteFile(filename, jTextArea1.getText());
            JOptionPane.showMessageDialog(this, "Log file with order shipping is created:"+filename);
        } // if connect check
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
      
    }//GEN-LAST:event_formWindowClosing

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NewJFrame mainf = new NewJFrame();
                mainf.setVisible(true);
               
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelUser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea3;
    // End of variables declaration//GEN-END:variables

}
