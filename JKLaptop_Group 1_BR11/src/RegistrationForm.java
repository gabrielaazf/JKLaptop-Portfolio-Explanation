import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.util.*;

public class RegistrationForm extends JFrame implements ActionListener{

	JPanel panelMain, panelNorth, panelSouth, panelCenter, panelGender, panelButton;	
	JLabel titleLabel, usernameLabel, emailLabel, passLabel, genderLabel, addresLabel;
	JTextField usernameTxtField, emailTxtField, addressTxtField;
	JPasswordField passField;
	JRadioButton femaleRadionBtn, maleRadioBtn;
	JButton submitBtn, loginBtn, resetBtn;
	//	JTextArea addressTxtArea;
	ButtonGroup genderBtnGroup;
	Connect c;
	ResultSet rs;
	private String generateID;


	public RegistrationForm() {
		// TODO Auto-generated constructor stub
		c = new Connect();
		init();

		setVisible(true);
		setSize(550, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("JKLaptop");
		setResizable(false);

	}
	public void init() {
		
		//pane;
		panelMain = new JPanel(new BorderLayout());
		panelNorth = new JPanel();
		panelSouth = new JPanel();
		panelCenter = new JPanel(new GridLayout(5 ,2, 0 ,25));
		panelGender = new JPanel();
		panelButton = new JPanel(new GridLayout(1,3,75,0));
		
		generateID=generateID();

		//Add Panel
		panelMain.add(panelNorth, BorderLayout.NORTH);
		panelMain.add(panelCenter, BorderLayout.CENTER);
		panelMain.add(panelSouth, BorderLayout.SOUTH);

		add(panelMain);

		panelCenter.setBorder(new EmptyBorder(30,30,0,20));

		//north panel
		titleLabel = new JLabel("JKLAPTOP");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		panelNorth.add(titleLabel);

		//center panel
		//Username
		usernameLabel = new JLabel("Username");
		usernameTxtField = new JTextField();

		panelCenter.add(usernameLabel);
		panelCenter.add(usernameTxtField);

		//Email
		emailLabel = new JLabel("Email");
		emailTxtField = new JTextField();

		panelCenter.add(emailLabel);
		panelCenter.add(emailTxtField);

		//Password
		passLabel = new JLabel("Password");
		passField=new JPasswordField();

		panelCenter.add(passLabel);
		panelCenter.add(passField);

		//Gender
		genderLabel = new JLabel("Gender");
		maleRadioBtn = new JRadioButton("Male");
		femaleRadionBtn = new JRadioButton("Female");

		genderBtnGroup = new ButtonGroup();
		genderBtnGroup.add(maleRadioBtn);
		genderBtnGroup.add(femaleRadionBtn);
		panelGender.add(maleRadioBtn);
		panelGender.add(femaleRadionBtn);

		panelCenter.add(genderLabel);
		panelCenter.add(panelGender);

		//Address
		addresLabel = new JLabel("Address");
		addressTxtField = new JTextField();

		panelCenter.add(addresLabel);
		panelCenter.add(addressTxtField);

		//south panel
		//Reset
		resetBtn = new JButton("Reset");
		resetBtn.addActionListener(this);
		panelButton.add(resetBtn);

		//Login
		loginBtn = new JButton("Login");
		loginBtn.addActionListener(this);
		panelButton.add(loginBtn);

		//Submit
		submitBtn = new JButton("Submit");
		submitBtn.addActionListener(this);
		panelButton.add(submitBtn);
		
		panelSouth.add(panelButton);



	}


	public void checkData() {
		//get data dari field
		String pwTemp = new String(passField.getPassword());
		String userTemp = usernameTxtField.getText();
		String emailTemp = emailTxtField.getText();
		String pilihan = null;
		//validasi radio button
		if(maleRadioBtn.isSelected()) pilihan="Male";
		if(femaleRadionBtn.isSelected()) pilihan ="Female";
		String addressTemp = addressTxtField.getText();
		String role = "Member";
		


		//validasiEmptyField
		if (userTemp.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Username must be filled!", "Message", 
					JOptionPane.INFORMATION_MESSAGE);
			return;
		} 
		else if(emailTemp.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Email must be filled!", "Message", 
					JOptionPane.INFORMATION_MESSAGE);
			return;
		} 
		else if(pwTemp.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Password must be filled!", "Message", 
					JOptionPane.INFORMATION_MESSAGE);
			return;
		} 
		else if (genderBtnGroup.getSelection()==null) {
			JOptionPane.showMessageDialog(this, "One gender must be selected!", "Message", 
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}  
		else if (addressTemp.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Address must be filled!", "Message", 
					JOptionPane.INFORMATION_MESSAGE);
			return;
		} 

		//validasiSetiapField
		if(!username()){
			return;
		}

		if(!email()){
			return;
		}

		if (!password()){
			return;
		}
		if(!address()) {
			return;
		}


		//addAllDatatoDATABASE
		//preparedStatement
		c.insertAccount(generateID, userTemp, emailTemp, pwTemp, pilihan, addressTemp, role);


		JOptionPane.showMessageDialog(this, "Register Success");
		
		closeForm();


	}

	public void closeForm() {
		this.dispose();

		new LoginForm().setVisible(true);
		
	}
	
	public String generateID() {
		
		String generateID = "US";
		boolean checkValid;
		do {
			for (int i=0;i<3;i++) {
				int rand= (int)(Math.random()*10);
				generateID+=rand;
			}
			checkValid=validationUserId(generateID);
		} while(!checkValid);
		
		return generateID;
	}
	
	public boolean validationUserId(String generateID) {
		String queryCheck="SELECT userid FROM user";
		rs = c.executeQuery(queryCheck);
		
	
		
		try {
			if(!rs.next())
				return true;
			
			while (rs.next()) {
				String temp = rs.getObject(1).toString();
				if(temp.equals(generateID)) {
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
	
	public boolean username() {
		//berdasarkan soal yang diberikan tidak terdapat keterangan bahwa username harus unique (berbeda satu dengan yang lain)
		//sehingga tidak dilakukan validasi terkait username yang sama
		try {
			String userTemp = usernameTxtField.getText();

			if (userTemp.length() < 5 || userTemp.length() > 20) {
				JOptionPane.showMessageDialog(this, "Username length must between 5 and 20 characters", "Message",
						JOptionPane.INFORMATION_MESSAGE);

				return false;
			}

		} catch (Exception e) {
			
		}
		return true;
	}


	public boolean email() {
		String emailTemp = emailTxtField.getText().toLowerCase();
		ArrayList<Integer>idxAt = new ArrayList<>();
		ArrayList<Integer> idxDot = new ArrayList<>();
		
		//add Index @
		for (int i = 0; i < emailTemp.length(); i++) {
			if(emailTemp.charAt(i)=='@')
				idxAt.add(i);
		}
		
		//add Index .
		for (int i = 0; i < emailTemp.length(); i++) {
			if(emailTemp.charAt(i)=='.') {
				idxDot.add(i);
			}
		}
		
		
		//validasi
		//tidak boleh sebalahan antara @ dan .
		try {
			for (int i = 0; i < idxAt.size(); i++) {
				for (int j = 0; j < idxDot.size(); j++) {
					if ((idxAt.get(i)+1)==idxDot.get(j)||(idxAt.get(i)-1)==idxDot.get(j)) {
						JOptionPane.showMessageDialog(this, "Email character '@' must not be next to '.'", "Message",
								JOptionPane.INFORMATION_MESSAGE);
						return false;
					} 
				}
			}
		} catch (Exception e) {
		}
		
		
		//tidak boleh start dengan @ atau .
		if (emailTemp.startsWith("@") || emailTemp.startsWith(".")) {
			JOptionPane.showMessageDialog(this, "Email input must not starts with '@' or '.'", "Message",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		//harus 1 @ dan 1 .
		if(idxAt.size()!=1||idxDot.size()!=1) {
			JOptionPane.showMessageDialog(this, "Email must not contains more than 1 '@' or '.'", "Message",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		
		//harus berakhir dengan .com
		if (!emailTemp.endsWith(".com")) {
			JOptionPane.showMessageDialog(this, "Email input must end with '.com'", "Message",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		return true;

	}

	public boolean password() {
		String passTemp = new String(passField.getPassword());
		boolean hasAlphabet = false;
		boolean hasNumeric = false;
		
		for (int i = 0; i < passTemp.length(); i++) {
			if(Character.isAlphabetic(passTemp.charAt(i))&&!hasAlphabet) {
				hasAlphabet = true;
			}
			
			if(Character.isDigit(passTemp.charAt(i))&&!hasNumeric) {
				hasNumeric=true;
			}
		}
		
		if (!(hasAlphabet&&hasNumeric)) {
			JOptionPane.showMessageDialog(this, "Password must alphanumeric", "Message",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		else {
			return true;
		}
	}

	public boolean address() {
		String addressTemp = addressTxtField.getText();
		if (!addressTemp.endsWith("Street")) {
			JOptionPane.showMessageDialog(this, "Address must ends with 'Street'", "Message",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		else return true;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == submitBtn) {
			checkData();
		}

		if(e.getSource()==loginBtn) {
			closeForm();
		}

		if(e.getSource()==resetBtn) {
			//reset semua data pada field
			usernameTxtField.setText("");
			emailTxtField.setText("");
			passField.setText("");
			genderBtnGroup.clearSelection();
			addressTxtField.setText("");
		}


	}
}



