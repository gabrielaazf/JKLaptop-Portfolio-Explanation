import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ProductForm extends JInternalFrame implements ActionListener, MouseListener {
	Connect c;
	JDesktopPane desktopPane;
	JPanel mainPanel, prodAllPanel, prodListPanel, insertListPanel, fieldPanel, buttonPanel;
	JLabel titleLabel, prodIdLabel, prodNameLabel, prodPriceLabel, prodRateLabel, prodStockLabel, prodBrandLabel;
	JTextField prodIdTxt, prodNameTxt, prodPriceTxt;
	JSpinner rateSpin, stokSpin;
	JComboBox<String> brandBox;
	Vector<String> listBrand;	
	JButton insertButton,updateButton, deleteButton, submitButton, cancelButton;
	JTable prodTable;
	DefaultTableModel dtmProd;
	JScrollPane prodScrollPane;
	private boolean validateData = false;
	private int row;
	private String action;


	public ProductForm(Connect conn, JDesktopPane desktopPane) {
		// TODO Auto-generated constructor stub
		this.c = conn;
		this.desktopPane=desktopPane;

		init();

		setTitle("Manage Product");
		setClosable(true);
		setMaximizable(true);
		setResizable(false);

		setSize(1200, 500);
	}
	private void init() {
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(5,5,5,5));
		prodAllPanel = new JPanel(new GridLayout(2, 1));
		prodListPanel =new JPanel(new BorderLayout());
		insertListPanel =new JPanel(new BorderLayout(2,10));
		insertListPanel.setPreferredSize(new Dimension(1000,200));
		fieldPanel = new JPanel(new GridLayout(6,2,2,0));
		buttonPanel = new JPanel(new GridLayout(5,1,2,5));

		titleLabel = new JLabel("Product List", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

		initTable();


		prodIdLabel = new JLabel("ProductID");
		prodIdTxt = new JTextField();
		prodIdTxt.setEnabled(false);
		prodNameLabel = new JLabel("Product Name");
		prodNameTxt= new JTextField();
		prodNameTxt.setEnabled(false);
		prodPriceLabel = new JLabel("Product Price");
		prodPriceTxt= new JTextField();
		prodPriceTxt.setEnabled(false);
		prodRateLabel= new JLabel("Product Rating");
		rateSpin = new JSpinner(new SpinnerNumberModel());
		rateSpin.setValue(1);
		rateSpin.setEnabled(false);
		prodStockLabel= new JLabel("Product Stock");
		stokSpin = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
		stokSpin.setEnabled(false);
		prodBrandLabel= new JLabel("Product Brand");
		//addListBrand
		listBrand = new Vector<>();
		addBrandList();
		brandBox = new JComboBox<>(listBrand);
		brandBox.setSelectedItem(null);
		brandBox.setEnabled(false);


		//addKePanel

		prodAllPanel.add(prodScrollPane, BorderLayout.CENTER);

		mainPanel.add(titleLabel,BorderLayout.NORTH);

		fieldPanel.add(prodIdLabel);
		fieldPanel.add(prodIdTxt);
		fieldPanel.add(prodNameLabel);
		fieldPanel.add(prodNameTxt);
		fieldPanel.add(prodPriceLabel);
		fieldPanel.add(prodPriceTxt);
		fieldPanel.add(prodRateLabel);
		fieldPanel.add(rateSpin);
		fieldPanel.add(prodStockLabel);
		fieldPanel.add(stokSpin);
		fieldPanel.add(prodBrandLabel);
		fieldPanel.add(brandBox);

		insertButton = new JButton("Insert");
		insertButton.setPreferredSize(new Dimension(350,50));
		updateButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		submitButton = new JButton("Submit");
		submitButton.setEnabled(false);
		cancelButton = new JButton("Cancel");
		cancelButton.setEnabled(false);

		buttonPanel.add(insertButton);
		buttonPanel.add(updateButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);

		insertListPanel.add(fieldPanel,BorderLayout.CENTER);
		insertListPanel.add(buttonPanel, BorderLayout.EAST);

		prodAllPanel.add(insertListPanel);
		mainPanel.add(prodAllPanel,BorderLayout.CENTER);

		add(mainPanel);

		//addListener
		prodTable.addMouseListener(this);
		insertButton.addActionListener(this);
		updateButton.addActionListener(this);
		deleteButton.addActionListener(this);
		submitButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}

	public void initTable() {

		Object [] colName = {
				"ProductID","BrandName", "ProductName", "ProductPrice","ProductStock","ProductRating"
		};

		dtmProd = new DefaultTableModel(colName,0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		prodTable = new JTable(dtmProd);
		prodScrollPane = new JScrollPane();
		prodScrollPane.setViewportView(prodTable);
		prodScrollPane.setPreferredSize(new Dimension(1000,350));

		insertTblData();
		

	}

	private void insertTblData() {
		ResultSet rs;

		String query = "SELECT ProductID, brandname, productname, productprice, productstock, productrating FROM product p JOIN brand b ON b.brandid=p.brandid";

		try {
			rs=c.executeQuery(query);
			while (rs.next()) {
				Vector<Object>obj = new Vector<>();

				for(int i=1;i<=rs.getMetaData().getColumnCount();i++) {
					obj.add(rs.getObject(i));
				};
				dtmProd.addRow(obj);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void addBrandList() {
		ResultSet rs;
		String query = "SELECT brandname from brand";
		rs = c.executeQuery(query);
		try {
			while(rs.next()) {
				String temp;
				temp = rs.getString("brandname");
				listBrand.add(temp);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==prodTable) {
			validateData = false;
			row = prodTable.getSelectedRow();
			String temp = (String) prodTable.getValueAt(row,1);
			if(!temp.isEmpty()) {
				validateData=true;
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
	public void actionPerformed(ActionEvent e) {
		
		ResultSet rs ;
		// TODO Auto-generated method stub
		if(e.getSource()==insertButton) {
			prodTable.clearSelection();
			String productId = getProductId();
			prodIdTxt.setText(productId);
			enableField();
			changeButton();
			action = "insert";

		}

		if(e.getSource()==updateButton||e.getSource()==deleteButton) {
			if(validateData&& e.getSource()==updateButton) {
				inputDataField();
				changeButton();
				action="update";

			}
			//dalam action delete, sesuai dengan soal maka tidak ada preview produk yang akan didelete, dalam arti langsung didelete produk yg diselect
			else if(validateData&& e.getSource()==deleteButton) {
				String queryDelete = "DELETE FROM product WHERE productid = '%s'";
				queryDelete=String.format(queryDelete,prodTable.getValueAt(row, 0).toString());
				c.executeUpdate(queryDelete);
				JOptionPane.showMessageDialog(this, "Delete Success");
				resetAll();
				dtmProd.setRowCount(0);
				insertTblData();
			}
			else {
				JOptionPane.showMessageDialog(this, "You must select the data on the table!");
				return;
			}
		}

		if(e.getSource()==cancelButton) {
			resetAll();
		}

		if(e.getSource()==submitButton) {
			
			if(!checkData()) 
				return;

			
			//cari brandId
			String brandName = brandBox.getSelectedItem().toString();
			String checkBrandId = "SELECT BrandId FROM Brand WHERE brandname = '%s'";
			checkBrandId = String.format(checkBrandId, brandName);
			String brandId = null;
			try {
				rs = c.executeQuery(checkBrandId);
				rs.next();
				brandId = rs.getString("brandid");
			} catch (SQLException e1) {
			}
				
			
			if(action.equals("insert")) {
				String queryInsert = "INSERT INTO product (productId, productName, productPrice, productRating, productStock, brandId) VALUES('%s','%s', '%s', '%s', '%s','%s')";
				queryInsert = String.format(queryInsert, prodIdTxt.getText(), prodNameTxt.getText(), prodPriceTxt.getText(), rateSpin.getValue().toString(), stokSpin.getValue().toString(), brandId);
				c.executeUpdate(queryInsert);
				JOptionPane.showMessageDialog(this, "Insert Success");
			}

			if(action.equals("update")) {
				String queryUpdate = "UPDATE product SET productname = '%s', productPrice = '%s', productRating = '%s', productStock = '%s', brandid = '%s' WHERE productid = '%s'";
				queryUpdate = String.format(queryUpdate, prodNameTxt.getText(), prodPriceTxt.getText(), rateSpin.getValue().toString(), stokSpin.getValue().toString(), brandId,prodIdTxt.getText());
				c.executeUpdate(queryUpdate);
				JOptionPane.showMessageDialog(this, "Update Success");

			}
			resetAll();
			dtmProd.setRowCount(0);
			insertTblData();
		}


	}

	
	public boolean checkData() {
		
		String prodNameTemp = prodNameTxt.getText();
		String prodPriceTemp = prodPriceTxt.getText();
		Integer rateSpinTemp = (Integer) rateSpin.getValue();
		Integer stokSpinTemp = (Integer) stokSpin.getValue();

		if (prodNameTemp.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Product Name must be fill!", "Message",
					JOptionPane.INFORMATION_MESSAGE);
			return false;

		}  
		if (prodPriceTemp.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Product Price must be fill!", "Message",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (brandBox.getSelectedItem()==null) {
			JOptionPane.showMessageDialog(this, "Product Brand must be fill!", "Message",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		} 

		if (rateSpinTemp<1||rateSpinTemp > 10) {
			JOptionPane.showMessageDialog(this, "Product Rating must be 1 to 10", "Message",
					JOptionPane.INFORMATION_MESSAGE);
			if(rateSpinTemp<1) rateSpin.setValue(1);
			else rateSpin.setValue(10);
			return false;
		}
		
		
		for(int i=0;i<prodPriceTemp.length();i++) {
			if(!Character.isDigit(prodPriceTemp.charAt(i))){
				JOptionPane.showMessageDialog(this, "Price must be numeric!");
			System.out.println("hello");
			return false;
			}
		}

		return true;

	}


	private void inputDataField() {
		prodIdTxt.setText(prodTable.getValueAt(row, 0).toString());
		prodNameTxt.setText(prodTable.getValueAt(row, 2).toString());
		prodNameTxt.setEnabled(true);
		prodPriceTxt.setText(prodTable.getValueAt(row, 3).toString());
		prodPriceTxt.setEnabled(true);
		rateSpin.setValue(Integer.parseInt(prodTable.getValueAt(row, 5).toString()));
		rateSpin.setEnabled(true);
		stokSpin.setValue(Integer.parseInt(prodTable.getValueAt(row, 4).toString()));
		stokSpin.setEnabled(true);
		for(int i=0; i<listBrand.size();i++) {
			if(prodTable.getValueAt(row, 1).toString().equals(listBrand.get(i))) {
				brandBox.setSelectedIndex(i);
			}
		}
		brandBox.setEnabled(true);

	}

	private void resetAll() {
		insertButton.setEnabled(true);
		updateButton.setEnabled(true);
		deleteButton.setEnabled(true);
		submitButton.setEnabled(false);
		cancelButton.setEnabled(false);
		prodNameTxt.setEnabled(false);
		prodPriceTxt.setEnabled(false);
		rateSpin.setEnabled(false);
		stokSpin.setEnabled(false);
		brandBox.setEnabled(false);
		prodIdTxt.setText("");
		prodNameTxt.setText("");
		prodPriceTxt.setText("");
		rateSpin.setValue(1);
		stokSpin.setValue(1);
		brandBox.setSelectedItem(null);
		prodTable.clearSelection();
		validateData = false;
	}

	private void changeButton() {
		insertButton.setEnabled(false);
		updateButton.setEnabled(false);
		deleteButton.setEnabled(false);
		submitButton.setEnabled(true);
		cancelButton.setEnabled(true);
	}

	private void enableField() {
		prodNameTxt.setEnabled(true);
		prodPriceTxt.setEnabled(true);
		rateSpin.setEnabled(true);
		stokSpin.setEnabled(true);
		brandBox.setEnabled(true);
	}

	private String getProductId() {
		String prodId= null;
		ResultSet rs;
		boolean validateId = true;
		try {
			do {
				prodId="PD";
				for (int i=0;i<3;i++) {
					int rand= (int)(Math.random()*10);
					prodId+=rand;
				}

				validateId=true;

				String queryCheck="SELECT productid FROM product";				
				rs = c.executeQuery(queryCheck);

				while (rs.next()) {
					String temp = rs.getObject(1).toString();
					if(temp.equals(prodId)) {
						validateId=false;
					};
				} 
			} while(!validateId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prodId;
	}	

}
