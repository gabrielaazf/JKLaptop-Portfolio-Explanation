import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class BrandForm extends JInternalFrame implements ActionListener, MouseListener {
	Connect c;
	JDesktopPane desktopPane;
	JPanel mainPanel, brandListPanel, manageBrandPanel, buttonAllPanel, buttonSubmitCancel, inputManagePanel,bottomPanel;
	JLabel titleLbl, brandIdLbl, brandNameLbl;
	JTextField brandIdTxt, brandNameTxt;
	JButton insertBtn, updateBtn, deleteBtn, submitBtn, cancelBtn;
	JTable brandTbl;
	JScrollPane spBrand;
	DefaultTableModel dtmBrand;
	private String action;
	private int row;
	private boolean validateSelectData = false;
	public BrandForm(Connect conn, JDesktopPane desktopPane) {

		this.c = conn;
		this.desktopPane=desktopPane;

		init();

		setTitle("Manage Brand");
		setClosable(true);
		setMaximizable(true);
		setResizable(false);

		setSize(1000, 600);
	}
	public void init() {
		mainPanel = new JPanel(new GridLayout(2, 1,0,10));
		mainPanel.setBorder(new EmptyBorder(5,5,5,5));
		brandListPanel = new JPanel(new BorderLayout());
		bottomPanel = new JPanel(new BorderLayout(40,10));
		manageBrandPanel = new JPanel(new BorderLayout(10,20));
		buttonAllPanel = new JPanel(new GridLayout(3,1,40,10));
		buttonSubmitCancel = new JPanel(new GridLayout(2,1,40,10));
		inputManagePanel = new JPanel(new GridLayout(2,2,20,0));
		inputManagePanel.setPreferredSize(new Dimension(660,120));

		titleLbl = new JLabel("Brand List", JLabel.CENTER);
		titleLbl.setFont(new Font("Arial", Font.BOLD, 24));

		brandIdLbl = new JLabel("Brand ID");
		brandNameLbl = new JLabel("Brand Name");

		brandIdTxt = new JTextField();
		brandNameTxt = new JTextField();

		brandIdTxt.setEnabled(false);
		brandNameTxt.setEnabled(false);

		insertBtn = new JButton("Insert");
		insertBtn.setPreferredSize(new Dimension(330,50));
		insertBtn.addActionListener(this);
		updateBtn= new JButton("Update");
		updateBtn.setPreferredSize(new Dimension(330,50));
		updateBtn.addActionListener(this);
		deleteBtn= new JButton("Delete");
		deleteBtn.setPreferredSize(new Dimension(330,50));
		deleteBtn.addActionListener(this);
		submitBtn= new JButton("Submit");
		submitBtn.setPreferredSize(new Dimension(330,50));
		submitBtn.addActionListener(this);
		cancelBtn = new JButton("Cancel");
		cancelBtn.setPreferredSize(new Dimension(330,50));
		cancelBtn.addActionListener(this);

		submitBtn.setEnabled(false);
		cancelBtn.setEnabled(false);

		initTbl();
		brandTbl.addMouseListener(this);

		brandListPanel.add(titleLbl, BorderLayout.NORTH);
		brandListPanel.add(spBrand,BorderLayout.CENTER);

		buttonAllPanel.add(insertBtn);
		buttonAllPanel.add(updateBtn);
		buttonAllPanel.add(deleteBtn);
		buttonSubmitCancel.add(submitBtn);
		buttonSubmitCancel.add(cancelBtn);

		inputManagePanel.add(brandIdLbl);
		inputManagePanel.add(brandIdTxt);
		inputManagePanel.add(brandNameLbl);
		inputManagePanel.add(brandNameTxt);

		manageBrandPanel.add(inputManagePanel,BorderLayout.CENTER);
		manageBrandPanel.add(buttonAllPanel,BorderLayout.EAST);

		bottomPanel.add(manageBrandPanel,BorderLayout.NORTH);
		bottomPanel.add(buttonSubmitCancel,BorderLayout.EAST);

		mainPanel.add(brandListPanel);
		mainPanel.add(bottomPanel);


		add(mainPanel);
	}

	public void initTbl() {

		Object [] colName = {
				"BrandID","BrandName"
		};

		dtmBrand = new DefaultTableModel(colName,0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// untuk membuat agar table yang tampil tidak bisa diedit
				return false;
			}
		};
		brandTbl = new JTable(dtmBrand);
		spBrand = new JScrollPane();
		spBrand.setViewportView(brandTbl);
		spBrand.setPreferredSize(new Dimension(800,300));

		insertTblData();
	}
	private void insertTblData() {
		ResultSet rs;
		String query = "SELECT brandid, brandname FROM brand";

		try {
			rs = c.executeQuery(query);
			while (rs.next()) {
				Vector<Object>obj = new Vector<>();

				for(int i=1;i<=rs.getMetaData().getColumnCount();i++) {
					obj.add(rs.getObject(i));
				};


				dtmBrand.addRow(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==brandTbl) {
			validateSelectData = false;
			row = brandTbl.getSelectedRow();
			String temp = (String) brandTbl.getValueAt(0, 1);
			if(!temp.isEmpty()) {
				validateSelectData=true;

			}
		}



	}
	
	private void inputDataField() {
		brandIdTxt.setText(brandTbl.getValueAt(row, 0).toString());
		brandNameTxt.setText(brandTbl.getValueAt(row, 1).toString());
		brandNameTxt.setEnabled(true);
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==insertBtn) {
			String brandId = getBrandId();
			brandIdTxt.setText(brandId);
			brandNameTxt.setEnabled(true);
			changeButton();
			action = "insert";
		}

		if(e.getSource()==updateBtn||e.getSource()==deleteBtn) {
			if(validateSelectData) {
				
				//dalam action delete, sesuai dengan soal maka tidak ada preview produk yang akan didelete, dalam arti langsung didelete produk yg diselect
				if(e.getSource()==deleteBtn) {
					String queryDelete = "DELETE FROM brand WHERE brandId = '%s'";
					queryDelete=String.format(queryDelete,brandTbl.getValueAt(row, 0));
					c.executeUpdate(queryDelete);
					JOptionPane.showMessageDialog(this, "Delete Success");
					resetSelection();
					resetAll();
					dtmBrand.setRowCount(0);
					insertTblData();
					return;
				}
				inputDataField();
				changeButton();
				action = "update";
				resetSelection();
			}
			else {
				JOptionPane.showMessageDialog(this, "You must select the data on the table!");
				return;
			}
		}

		if(e.getSource()==cancelBtn) {
			resetAll();
			resetSelection();
		}
		if(e.getSource()==submitBtn) {
			if(action.equals("insert")) {
				if(brandNameTxt.getText().isEmpty()) {
					JOptionPane.showMessageDialog(this, "Brand Name must be fill");
					return;
				}
				String queryInsert = "INSERT INTO brand (brandId, brandName) VALUES('%s','%s')";
				queryInsert = String.format(queryInsert, brandIdTxt.getText(), brandNameTxt.getText());
				c.executeUpdate(queryInsert);
				JOptionPane.showMessageDialog(this, "Insert Success");
			}
			if(action.equals("update")) {
				if(brandNameTxt.getText().isEmpty()) {
					JOptionPane.showMessageDialog(this, "Brand Name must be fill");
					return;
				}
				String queryUpdate = "UPDATE brand SET brandname = '%s' WHERE brandid = '%s'";
				queryUpdate = String.format(queryUpdate, brandNameTxt.getText(), brandIdTxt.getText());
				c.executeUpdate(queryUpdate);
				JOptionPane.showMessageDialog(this, "Update Success");

			}
			resetAll();
			dtmBrand.setRowCount(0);
			insertTblData();
		}

	}
	private void resetSelection() {
		brandTbl.clearSelection();
		validateSelectData= false;
	}
	private void resetAll() {
		insertBtn.setEnabled(true);
		updateBtn.setEnabled(true);
		deleteBtn.setEnabled(true);
		submitBtn.setEnabled(false);
		cancelBtn.setEnabled(false);
		brandIdTxt.setText("");
		brandNameTxt.setText("");
		brandNameTxt.setEnabled(false);

	}
	private void changeButton() {
		insertBtn.setEnabled(false);
		updateBtn.setEnabled(false);
		deleteBtn.setEnabled(false);
		submitBtn.setEnabled(true);
		cancelBtn.setEnabled(true);

	}
	private String getBrandId() {
		String brandId = null;
		ResultSet rs;
		boolean validateId = true;
		try {
			do {
				brandId="BR";
				for (int i=0;i<3;i++) {
					int rand= (int)(Math.random()*10);
					brandId+=rand;
				}

				validateId=true;

				String queryCheck="SELECT brandid FROM brand";				
				rs = c.executeQuery(queryCheck);

				while (rs.next()) {
					String temp = rs.getObject(1).toString();
					if(temp.equals(brandId)) {
						validateId=false;
					};
				} 
			} while(!validateId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return brandId;
	}
}
