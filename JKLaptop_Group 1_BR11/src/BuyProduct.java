import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class BuyProduct extends JInternalFrame implements ActionListener, MouseListener {
	JPanel mainPanel, northPanel, centerPanel, chosenProd,southPanel;
	JLabel titleLbl, productIdLbl, productNameLbl,  productPriceLbl, productBrandLbl, qtyLbl, ratingLbl;
	JLabel productIdAns, productNameAns,  productPriceAns, productBrandAns, ratingAns;
	JSpinner qtySpin;
	JButton atcBtn;
	JTable productTbl;
	DefaultTableModel productDtm;
	JScrollPane prodJsp;
	Connect c;
	private String prodIdSelect = null;
	private int tempPrice;
	private int maxValue;
	JDesktopPane desktopPane;
	private String userId;

	public BuyProduct(Connect conn, JDesktopPane desktopPane, String userId){
		c = conn;
		this.userId=userId;
		this.desktopPane=desktopPane;
		init();
		setTitle("Buy Product");
		setClosable(true);
		setMaximizable(true);
		setResizable(false);

		setSize(800, 600);
	}


	public void init() {
		
		//panel
		mainPanel = new JPanel(new BorderLayout());
		northPanel= new JPanel();
		chosenProd = new JPanel(new GridLayout(6,2,3,3));
		centerPanel=new JPanel(new BorderLayout());
		centerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
		southPanel = new JPanel();
		chosenProd.setPreferredSize(new Dimension(600,200));
		
		//title
		titleLbl= new JLabel("Our Product");
		titleLbl.setFont(new Font("Arial", Font.BOLD, 25));

		productIdLbl = new JLabel("Product ID");
		productIdAns = new JLabel("-");

		productNameLbl= new JLabel("Product Name");
		productNameAns = new JLabel("-");
		
		productPriceLbl = new JLabel("Product Price");
		productPriceAns = new JLabel("-");
		
		productBrandLbl = new JLabel("Product Brand");
		productBrandAns = new JLabel("-");
		
		qtyLbl = new JLabel("Quantity");
		qtySpin = new JSpinner();
		qtySpin.setEnabled(false);
		qtySpin.setEnabled(false);
		
		ratingLbl = new JLabel("Rating");
		ratingAns = new JLabel("-");
		
		//add ke panel 
		chosenProd.add(productIdLbl);
		chosenProd.add(productIdAns);
		chosenProd.add(productNameLbl);
		chosenProd.add(productNameAns);
		chosenProd.add(productPriceLbl);
		chosenProd.add(productPriceAns);
		chosenProd.add(productBrandLbl);
		chosenProd.add(productBrandAns);
		chosenProd.add(qtyLbl);
		chosenProd.add(qtySpin);
		chosenProd.add(ratingLbl);
		chosenProd.add(ratingAns);


		northPanel.add(titleLbl);
		mainPanel.add(northPanel,BorderLayout.NORTH);

		atcBtn = new JButton("Add to Cart");
		atcBtn.setEnabled(false);
		atcBtn.addActionListener(this);
		
		try {
			initTable();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		productTbl.addMouseListener(this);


		centerPanel.add(prodJsp,BorderLayout.NORTH);
		centerPanel.add(chosenProd,BorderLayout.EAST);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		southPanel.add(atcBtn);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		add(mainPanel);


	}


	public void initTable() {
		ResultSet rs;
		Object [] colName = {
				"ProductID","BrandName","ProductName","ProductPrice","ProductQty","ProductRating"
		};
		
		//membuat table agar tidak bisa diedit saat sudah diexecute
		productDtm = new DefaultTableModel(colName,0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};;
		
		productTbl = new JTable(productDtm);
		prodJsp = new JScrollPane();
		prodJsp.setViewportView(productTbl);
		prodJsp.setPreferredSize(new Dimension(700,300));



		String query = "SELECT ProductID, BrandName, ProductName, ProductPrice, ProductStock, ProductRating FROM product p JOIN Brand b ON p.BrandID=b.BrandID";
		try {
			rs = c.executeQuery(query);
			while (rs.next()) {
				Vector<Object>obj = new Vector<>();
				if(Integer.parseInt(rs.getString("productstock"))>0) {
					for(int i=1;i<=rs.getMetaData().getColumnCount();i++) {
						obj.add(rs.getObject(i));
					};

					productDtm.addRow(obj);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

		if(arg0.getSource() == productTbl) {
			try {
				int row = productTbl.getSelectedRow();
				prodIdSelect = productTbl.getValueAt(row, 0).toString();
				
				if(!prodIdSelect.isEmpty()) {
					formAnswer(prodIdSelect,row);
				}


			} catch (Exception e) {
				// TODO: handle exception
			}
		}


	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==atcBtn) {

			String queryCheck = "SELECT productid from cart WHERE userid='%s'";
			queryCheck = String.format(queryCheck, userId);
			String query = "INSERT INTO cart(userid, productid, qty) VALUES('%s','%s','%s')";
			String update = "UPDATE cart SET qty = qty+'%s' WHERE productid = '%s' AND userid ='%s'";
			String qtyProd = qtySpin.getValue().toString();
			query = String.format(query, userId, prodIdSelect, qtyProd);
			update = String.format(update, qtyProd, prodIdSelect, userId);
			int spinValue = Integer.parseInt(qtySpin.getValue().toString());
			
			
			//validasi total qty
			if(!validQty(spinValue)) return;
			
			
			//validasi apakah produknya sudah pernah ada yang masuk dalam cart atau belum
			if(!validateCartAdd(queryCheck)) {
				c.executeUpdate(query);
			}
			else {
				c.executeUpdate(update);
			}

			updateQty();

			desktopPane.remove(this);
			Cart cartForm = new Cart(c,userId);
			desktopPane.add(cartForm);
			cartForm.show();


		}

	}

	public void formAnswer(String prodIdSelect, int row) {
		productIdAns.setText(prodIdSelect);
		productBrandAns.setText(productTbl.getValueAt(row, 1).toString());
		productNameAns.setText(productTbl.getValueAt(row, 2).toString());
		productPriceAns.setText(productTbl.getValueAt(row, 3).toString());
		ratingAns.setText(productTbl.getValueAt(row, 5).toString());
		qtySpin.setEnabled(true);
		qtySpin.setModel(new SpinnerNumberModel(1, 0, 100, 1));

		maxValue=Integer.parseInt(productTbl.getValueAt(row, 4).toString());
		

		atcBtn.setEnabled(true);

	}


	public boolean validQty(int spinValue) {
		if (spinValue == 0) {
			JOptionPane.showMessageDialog(this, "Quantity minimum is 1", "Message",
					JOptionPane.INFORMATION_MESSAGE);
			qtySpin.setValue(1);
			spinValue = 1;
			return false;
		}

		else if (spinValue > maxValue) {
			JOptionPane.showMessageDialog(this, "Quantity cannot be more than available stock", "Message",
					JOptionPane.INFORMATION_MESSAGE);
			qtySpin.setValue(maxValue);
			spinValue = maxValue;
			return false;
		}
		return true;
	}


	public void updateQty() {
		// TODO Auto-generated method stub
		String query = "UPDATE product set productstock=productstock-%s WHERE productid = '%s' ";
		query = String.format(query, qtySpin.getValue().toString(), prodIdSelect);
		c.executeUpdate(query);
	}


	public void resetAllField() {
		productIdAns.setText("-");
		productBrandAns.setText("-");
		productNameAns.setText("-");
		productPriceAns.setText("-");
		ratingAns.setText("-");
		qtySpin.setValue(1);
		qtySpin.setEnabled(false);

	}



	public boolean validateCartAdd(String queryCheck) {
		ResultSet rs = null;
		String checkProdId;
		try {
			rs=c.executeQuery(queryCheck);
			while (rs.next()) {
				checkProdId = rs.getString("productId");
				if (checkProdId.equals(prodIdSelect)) {
					return true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}



}




