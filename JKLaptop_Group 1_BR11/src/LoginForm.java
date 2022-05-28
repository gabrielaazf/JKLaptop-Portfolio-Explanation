import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LoginForm extends JFrame implements ActionListener{

	JLabel titleLbl;
	JLabel usernameLbl;
	JLabel passLbl;

	JTextField usernameTxt;
	JPasswordField passTxt;

	JButton submitBtn;
	JButton registBtn;

	Connect c ;

	public LoginForm() {
		c = new Connect();
		//Layout
		setLayout(new BorderLayout());

		JPanel panelNorth = new JPanel();
		panelNorth.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelNorth.setBorder(new EmptyBorder(5,15,0,15));
		titleLbl = new JLabel("Login");
		titleLbl.setFont(new Font("Arial", Font.BOLD, 24));
		panelNorth.add(titleLbl);
		add(panelNorth, "North");

		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new GridLayout(2,2,10,15));
		panelCenter.setBorder(new EmptyBorder(0, 15, 0, 15));

		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER,100,0));
		panelSouth.setBorder(new EmptyBorder(5, 15, 10, 15));

		registBtn = new JButton("Register");
		submitBtn = new JButton("Submit");

		submitBtn.setPreferredSize(new Dimension(100,30));
		registBtn.setPreferredSize(new Dimension(100, 30));
		panelSouth.add(registBtn);
		panelSouth.add(submitBtn);
		add(panelSouth, "South");

		//action listen button submit
		submitBtn.addActionListener(this);
		registBtn.addActionListener(this);
		//Username
		usernameLbl = new JLabel("Username");
		usernameTxt = new JTextField();
		panelCenter.add(usernameLbl);
		panelCenter.add(usernameTxt);

		//Password
		passLbl = new JLabel("Password");
		passTxt = new JPasswordField();
		panelCenter.add(passLbl);
		panelCenter.add(passTxt);

		add(panelCenter, "Center");



		//Frame
		setTitle("JKLaptop");
		setSize(new Dimension(500, 225));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);


	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==submitBtn) {
			validateLogin();
		}
		
		if(e.getSource()==registBtn) {
			this.dispose();
			new RegistrationForm().setVisible(true);;
		}

	}


	private void validateLogin() {
		ResultSet rs;
		boolean usernameCheck = false;
		boolean passwordCheck = false;
		//query-query yang akan dipakai utk execute database
		String queryUsername = "SELECT username FROM user";
		String queryIdPassword = "SELECT userId, userPassword FROM user WHERE username='%s'";
		String queryRole = "SELECT userRole FROM user WHERE userid='%s'";
		
		//ambil data dari field
		String usernameTemp = usernameTxt.getText().trim();
		String passTemp = new String(passTxt.getPassword());
		
		//variabel utk penerimaan data
		String username = null;
		String userId = null;
		String roleLogin = null;
		
		if (usernameTemp.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Username field must be filled", "Message",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		} else if(passTemp.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Password field must be filled", "Message",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		try {
			rs = c.executeQuery(queryUsername);
			while (rs.next()) {
				username = rs.getString("username");
				if (usernameTemp.equals(username)) {
					usernameCheck = true;
					break;
				}
			}
			
			
			//pengecekan dilakukan dengan mencari username yang diinputkan, apabila ada akan return true
			//kemudian dilanjutkan dengan mengecek password nya, cek password akan dilakukan kepada setiap username yang sesuai (karena pada soal tidakk ditulis bahwa username harus unique)
			// setelah password ditemukan maka akan diambil userid nya
			if (usernameCheck) {
				queryIdPassword = String.format(queryIdPassword, usernameTemp);
				rs = c.executeQuery(queryIdPassword);
				while(rs.next()) {
					String password = rs.getString("userPassword");
					if (passTemp.equals(password)) {
						passwordCheck = true;
						userId= rs.getString("userId");
					}
				}
			}
			
			if (!usernameCheck || !passwordCheck) {
				JOptionPane.showMessageDialog(this, "Inputted username and password is invalid " , "Message", JOptionPane.WARNING_MESSAGE);
			}
			
			if(usernameCheck&&passwordCheck) {
				int role = 0;
				queryRole = String.format(queryRole, userId);
				rs = c.executeQuery(queryRole);
				rs.next();
				roleLogin = rs.getString("userRole");
				if(roleLogin.equalsIgnoreCase("Member"))
					role =0;
				else if(roleLogin.equalsIgnoreCase("Admin"))
					role=1;
				dispose();
				new MainForm(role, userId).setVisible(true);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}


