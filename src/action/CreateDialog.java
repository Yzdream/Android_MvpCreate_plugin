package action;

import org.apache.http.util.TextUtils;

import javax.swing.*;
import java.awt.event.*;

public class CreateDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JLabel Txttip;


    private DialogCallBack mCallBack;

    public static final String FRAGMENT = "1";
    public static final String ACTIVITY = "2";
    public static final String ALL = "3";
    private String type = FRAGMENT;

    public CreateDialog(DialogCallBack callBack) {
        this.mCallBack = callBack;
        setContentPane(contentPane);
        setModal(true);
        setTitle("Mvp Create Helper");
        setSize(300, 150);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);
        Txttip.setVisible(false);



        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        if (null != mCallBack){
            if (TextUtils.isEmpty(textField1.getText().trim())){
                Txttip.setVisible(true);
                Txttip.setText("No filename is created");
                return;
            }
            if (TextUtils.isEmpty(textField2.getText().trim())) {
                Txttip.setVisible(true);
                Txttip.setText("No create type, fragment or activity or all");
                return;
            }else if ("fragment".equals(textField2.getText().trim().toLowerCase())){
                type = FRAGMENT;
            }else if ("activity".equals(textField2.getText().trim().toLowerCase())){
                type = ACTIVITY;
            }else if ("all".equals(textField2.getText().trim().toLowerCase())){
                type = ALL;
            }else{
                Txttip.setVisible(true);
                Txttip.setText("Type only fragment and activity and all");
                return;
            }

            mCallBack.ok(type,textField1.getText().trim());
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

//    public static void main(String[] args) {
//        CreateDialog dialog = new CreateDialog(new DialogCallBack() {
//            @Override
//            public void ok(String type, String moduleName) {
//
//            }
//        });
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }

    public interface DialogCallBack{
        void ok(String type, String moduleName);
    }
}
