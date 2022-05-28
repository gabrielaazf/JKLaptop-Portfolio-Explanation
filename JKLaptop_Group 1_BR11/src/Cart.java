import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.Calendar;

public class Cart extends JInternalFrame implements ActionListener{
	Connect c;
	private String userId;
	JLabel titleLbl, userIdLbl, usernameLbl, dateLbl, totalPriceLbl, detailLbl;
	JLabel userIdAns, usernameAns, dateAns, totalPriceAns;
	JTable cartTbl;
	DefaultTableModel cartDtm;
	JScrollPane cartJsp;
	JPanel mainPanel, panelTitle, panelDetail, panelTbl, panelBtn,panelAllData;
	JButton checkOut;

	public Cart(Connect c,  String userId) {
		this.c = c;
		this.userId=userId;

		init();
		setTitle("Cart");
		setClosable(true);
		setMaximizable(true);
		setResizable(false);

		setSize(800, 600);
	}


	private void init() {
		ResultSet rs;
		String userTemp = null;
		
		//panel
		mainPanel= new JPanel(new BorderLayout(5,2));
		panelAllData = new JPanel(new BorderLayout());
		panelAllData.setBorder(new EmptyBorder(0, 20, 0, 20));
		panelTitle = new JPanel();
		panelDetail = new JPanel(new GridLayout(0, 4,0,0));
		panelDetail.setPreferredSize(new Dimension(750, 200));
		panelTbl = new JPanel(new BorderLayout());
		panelBtn = new JPanel();
		checkOut = new JButton("Check Out");
		checkOut.addActionListener(this);

		titleLbl = new JLabel("Cart");
		titleLbl.setFont(new Font("Arial", Font.BOLD, 25));
		detailLbl = new JLabel("Detail", JLabel.CENTER);
		detailLbl.setFont(new Font("Arial", Font.BOLD, 18));

		userIdLbl = new JLabel("User ID:");
		userIdAns = new JLabel(userId);

		usernameLbl = new JLabel("Username:");
		String queryUsername = "SELECT username from user WHERE userID='%s'";
		queryUsername = String.format(queryUsername, userId);
		try {
			rs = c.executeQuery(queryUsername);
			rs.next();
			 userTemp = rs.getString("username");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		usernameAns = new JLabel(userTemp);
		dateLbl = new JLabel("Date:");
		
		dateAns = new JLabel(getDate());

		totalPriceLbl= new JLabel("Total Price");

		createTbl();

		int rowCount = cartTbl.getRowCount();
		int totalPrice=0;
		for(int i = 0;i<rowCount;i++) {
			int prodPrice = Integer.parseInt(cartTbl.getValueAt(i, 2).toString());
			int qtyProd = Integer.parseInt(cartTbl.getValueAt(i, 3).toString());
			totalPrice+=(prodPrice*qtyProd);
		}


		totalPriceAns = new JLabel(Integer.toString(totalPrice));

		panelTitle.add(titleLbl);
		panelDetail.add(userIdLbl);
		panelDetail.add(userIdAns);
		panelDetail.add(usernameLbl);
		panelDetail.add(usernameAns);
		panelDetail.add(dateLbl);
		panelDetail.add(dateAns);
		panelDetail.add(totalPriceLbl);
		panelDetail.add(totalPriceAns);

		panelTbl.add(detailLbl, BorderLayout.NORTH);
		panelTbl.add(cartJsp, BorderLayout.CENTER);

		panelBtn.add(checkOut);

		mainPanel.add(panelTitle, BorderLayout.NORTH);
		panelAllData.add(panelDetail,BorderLayout.NORTH);
		panelAllData.add(panelTbl,BorderLayout.CENTER);
		mainPanel.add(panelAllData,BorderLayout.CENTER);
		mainPanel.add(checkOut,BorderLayout.SOUTH);


		add(mainPanel);


	}
	
	//untuk mendapatkan tanggal hari ini
	public String getDate() {
		return (java.time.LocalDate.now().toString());  
	}

	public void createTbl() {
		ResultSet rs = null;
		Object [] colName = {
				"ProductID","ProductName","ProductPrice","Qty"
		};

		cartDtm = new DefaultTableModel(colName,0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		cartTbl = new JTable(cartDtm);
		cartJsp = new JScrollPane();
		cartJsp.setViewportView(cartTbl);
		cartJsp.setPreferredSize(new Dimension(750, 315));



		String query = "SELECT C.ProductID, ProductName, ProductPrice, Qty FROM cart c JOIN Product p ON p.ProductID=c.ProductID JOIN user u ON u.UserID=c.UserID WHERE C.UserID='%s'";

		query = String.format(query, userId);

		try {
			rs = c.executeQuery(query);
			while (rs.next()) {
				Vector<Object>obj = new Vector<>();
				
				for(int i=1;i<=rs.getMetaData().getColumnCount();i++) {
					obj.add(rs.getObject(i));
				};
				
				cartDtm.addRow(obj);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}



	@Override
	public void actionPerformed(ActionEvent e) {
		ResultSet rs;
		if(e.getSource()==checkOut) {
			String transactionId;
			boolean validateId = true;
			try {
				do {
					transactionId="TR";
					for (int i=0;i<3;i++) {
						int rand= (int)(Math.random()*10);
						transactionId+=rand;
					}
					
					validateId=true;

					String queryCheck="SELECT transactionid FROM headertransaction";
					rs = c.executeQuery(queryCheck);

					while (rs.next()) {
						String temp = rs.getObject(1).toString();
						if(temp.equals(transactionId)) {
							validateId=false;
						};
					} 

				} while(!validateId);
				
				//headertranscation
				String queryHeader = "INSERT INTO headertransaction(transactionid, userid, transactiondate) VALUES('%s','%s','%s')";
				queryHeader = String.format(queryHeader, transactionId, userId,getDate());
				c.executeUpdate(queryHeader);
				
				
				
				
				for(int i=0; i<cartTbl.getRowCount();i++) {
					String queryDetail= "INSERT INTO detailtransaction(transactionid, productid, qty) VALUES('%s','%s','%s')";
					queryDetail = String.format(queryDetail, transactionId, cartTbl.getValueAt(i, 0).toString(),cartTbl.getValueAt(i, 3).toString());
					c.executeUpdate(queryDetail);
					
				}
				String queryDelCart = "DELETE FROM cart WHERE userid='%s'";
				queryDelCart = String.format(queryDelCart, userId);
				c.executeUpdate(queryDelCart);
				
				JOptionPane.showMessageDialog(this, "Check Out Success!", "Message", JOptionPane.INFORMATION_MESSAGE);
				this.dispose();
				
			}

			
			catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}
}
