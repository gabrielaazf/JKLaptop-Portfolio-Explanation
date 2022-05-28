import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ViewTransactionForm extends JInternalFrame implements MouseListener {
	Connect c;
	String userId;
	JDesktopPane desktopPane;
	private int role;
	JTable headerTable, detailTransTable;
	JPanel mainPanel, panelHeader, panelDetail;
	JLabel transListLbl,transDetailLbl;
	DefaultTableModel dtmTscList, dtmTscDetail;
	JScrollPane spTranscList, spTransDetail;
	public ViewTransactionForm(Connect conn, JDesktopPane desktopPane, String userId, int role) {
		this.c = conn;
		this.userId=userId;
		this.desktopPane=desktopPane;
		this.role=role;
		mainPanel = new JPanel(new GridLayout(2,1));
		panelHeader = new JPanel(new BorderLayout());
		panelDetail = new JPanel(new BorderLayout());
		init();
		setTitle("View Transaction");
		setClosable(true);
		setMaximizable(true);
		setResizable(false);

		setSize(1000, 600);
	}

	private void init() {
		transListLbl = new JLabel("Transaction List", JLabel.CENTER);
		transListLbl.setFont(new Font("Arial",Font.BOLD,24));
		transDetailLbl = new JLabel("Transaction Detail", JLabel.CENTER);
		transDetailLbl.setFont(new Font("Arial",Font.BOLD,24));
		
		try {
			initHeaderTable();
			initDetailTable();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		headerTable.addMouseListener(this);
		
		panelHeader.add(transListLbl, BorderLayout.NORTH);
		panelHeader.add(spTranscList, BorderLayout.CENTER);
		
		panelDetail.add(transDetailLbl, BorderLayout.NORTH);
		panelDetail.add(spTransDetail,BorderLayout.CENTER);
		
		mainPanel.add(panelHeader);
		mainPanel.add(panelDetail);
		
		add(mainPanel);
		
	}


	private void initHeaderTable() {
		ResultSet rs;
		
		Object [] colName = {
				"TransactionID", "User ID", "Transaction Date"
		};
		dtmTscList = new DefaultTableModel(colName,0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		headerTable= new JTable(dtmTscList);
		spTranscList = new JScrollPane();
		spTranscList.setViewportView(headerTable);
		spTranscList.setPreferredSize(new Dimension(940, 200));
		
		String query = "SELECT * FROM `headertransaction`";
		
		//masukin table dari sql
		if (role==0){
			query = "SELECT * FROM headertransaction WHERE userid = '%s'";
			query = String.format(query, userId);
		}
		
		try {
			rs = c.executeQuery(query);
			while (rs.next()) {
				Vector<Object>obj = new Vector<>();

				for(int i=1;i<=rs.getMetaData().getColumnCount();i++) {
					obj.add(rs.getObject(i));
				};


				dtmTscList.addRow(obj);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void initDetailTable() {
		Object [] colName = {
				"TransactionID", "ProductID", "Qty"
		};
		dtmTscDetail = new DefaultTableModel(colName,0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		detailTransTable= new JTable(dtmTscDetail);
		spTransDetail = new JScrollPane();
		spTransDetail.setViewportView(detailTransTable);
		spTransDetail.setPreferredSize(new Dimension(940, 200));	
		
	}
	
	private void addData(String transIdSelect) {
		ResultSet rs=null;
		String query = "SELECT * FROM detailtransaction where transactionid='%s'";
		query = String.format(query, transIdSelect);
		
		try {
			rs = c.executeQuery(query);
			while (rs.next()) {
				Vector<Object>obj = new Vector<>();

				for(int i=1;i<=rs.getMetaData().getColumnCount();i++) {
					obj.add(rs.getObject(i));
				};


				dtmTscDetail.addRow(obj);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		try {
			if(arg0.getSource()==headerTable) {
				dtmTscDetail.setRowCount(0);
				int row = headerTable.getSelectedRow();
				String transIdSelect = headerTable.getValueAt(row, 0).toString();
				if(!transIdSelect.isEmpty()) {
					addData(transIdSelect);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
