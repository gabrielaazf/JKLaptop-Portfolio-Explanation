import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainForm extends JFrame implements ActionListener {
	JDesktopPane desktopPane;
	Connect c;
	JMenuBar menuBar;
	JMenu transaction, logout, manage;
	JMenuItem buyProd, viewTrans, manageBrand, manageProd, logoutItm;
	private String userId;
	private int role;
	public MainForm(int role, String userId) {
		setLayout(new BorderLayout());
		
		//menggunakan desktoppane karena berdasar soal, ukuran setiap internal frame berbeda
		desktopPane = new JDesktopPane();
		setContentPane(desktopPane);
		this.userId=userId;
		this.role = role;
		c = new Connect();
		menuBar = new JMenuBar();
		
		initMenu();
		
		if (role==1) {
			manage.add(manageBrand);
			manage.add(manageProd);
			menuBar.add(manage);
			transaction.add(viewTrans);
			menuBar.add(transaction);
			menuBar.add(logout);
			
		} else if (role==0) {
			transaction.add(buyProd);
			transaction.add(viewTrans);
			menuBar.add(transaction);
			menuBar.add(logout);
		} 
		
		buyProd.addActionListener(this);
		viewTrans.addActionListener(this);
		manageBrand.addActionListener(this);
		manageProd.addActionListener(this);
		logoutItm.addActionListener(this);
		
		
		setTitle("Main Form");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setJMenuBar(menuBar);
		
		

		
	}
	

	private void initMenu() {
		transaction = new JMenu("Transaction");
		logout = new JMenu("Logout");
		manage = new JMenu("Manage");

		buyProd = new JMenuItem("Buy Product");
		viewTrans = new JMenuItem("View Transaction");
		manageBrand = new JMenuItem("Brand");
		manageProd = new JMenuItem("Product");
		logoutItm = new JMenuItem("Logout");
		logout.add(logoutItm);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		desktopPane.removeAll();
		if(e.getSource()==logoutItm) {
			this.dispose();
			new LoginForm().setVisible(true);
		}
		if(e.getSource()==buyProd) {
			BuyProduct bp= new BuyProduct(c, desktopPane,userId);
			desktopPane.add(bp);
			bp.setVisible(true);
		}
		
		if(e.getSource()==viewTrans) {
			ViewTransactionForm vtf = new ViewTransactionForm(c, desktopPane, userId, role);
			desktopPane.add(vtf);
			vtf.setVisible(true);
			
		}
		
		if(e.getSource()==manageBrand) {
			BrandForm brand = new BrandForm(c, desktopPane);
			desktopPane.add(brand);
			brand.setVisible(true);
		}
		
		if(e.getSource()==manageProd) {
			ProductForm pf = new ProductForm(c, desktopPane);
			desktopPane.add(pf);
			pf.setVisible(true);
		}
	}

}
