package ui;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import model.data.Customer;
import model.service.CustomerService;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class FrmMain extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tblCustomer;
	JScrollPane scrollPane = new JScrollPane();
		
	private JTextField txtPhone;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtNote;
	
	private static JTextField txtFind = new JTextField("0");
	

	CustomerService customerService = new CustomerService();
		
	private Object[][] getTableData() {		
		String key = txtFind.getText();
		List<Customer> listAllCustomer = customerService.getAllCustomer();
		
		List<Customer> reList = new ArrayList<>();
		
		for (Customer customer : listAllCustomer) {
			if (customer.getPhone().contains(key.trim())) {
				reList.add(customer);
			}
		}
		Object[][] objects = new Object[listAllCustomer.size()][4];
		for (int i = 0; i < listAllCustomer.size(); i++) {
			objects[i][0] = listAllCustomer.get(i).getPhone();
			objects[i][1] = listAllCustomer.get(i).getName();
			objects[i][2] = listAllCustomer.get(i).getAddress();
			objects[i][3] = listAllCustomer.get(i).getNote();
		}
		
		return objects;
	}
	
	private String[] tableHeader = { "Điện thoại", "Tên", "Địa chỉ", "Ghi chú" };
	
	private void refresh() {
		
		tblCustomer.setModel(new DefaultTableModel(getTableData(), tableHeader));
		tblCustomer.setColumnSelectionAllowed(true);
		scrollPane.setViewportView(tblCustomer);
	}
	
	private class addActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Customer customer = new Customer();
			customer.setPhone(txtPhone.getText());
			customer.setName(txtName.getText());
			customer.setAddress(txtAddress.getText());
			customer.setNote(txtNote.getText());
			
			if (customerService.addCustomer(customer)) {
				JOptionPane.showMessageDialog(null, "Đã thêm");
			} else {
				JOptionPane.showMessageDialog(null, "Không thể thêm người này");				
			}			
			txtPhone.setText("");
			txtName.setText("");
			txtAddress.setText("");
			txtNote.setText("");
			
			refresh();
		}
	}
	
	private class deleteActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (tblCustomer.getSelectedRow() != -1) {
				String phone = (String)tblCustomer.getValueAt(tblCustomer.getSelectedRow(), 0);
				if (JOptionPane.showConfirmDialog(null, "Bạn muốn xóa khách hàng này?") == 0) {
					customerService.deleteCustomer(phone);
					refresh();
				}		
			} else {
				JOptionPane.showMessageDialog(null, "Xin chọn khách hàng!");
			}
		}		
	}
	
	private class updateActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			int x = tblCustomer.getSelectedRow();
			int y = tblCustomer.getSelectedColumn();
			if (x != -1 && y != -1) {
				
				Customer customer = new Customer();

				customer.setPhone((String)tblCustomer.getValueAt(x, 0));
				customer.setName((String)tblCustomer.getValueAt(x, 1));				
				customer.setAddress((String)tblCustomer.getValueAt(x, 2));
				customer.setNote((String)tblCustomer.getValueAt(x, 3));
				
				switch (y) {
				case 0:
					customer.setPhone(JOptionPane.showInputDialog("Điện thoại", customer.getPhone()));
					break;
				case 1:
					customer.setName(JOptionPane.showInputDialog("Tên", customer.getName()));
					break;
				case 2:
					customer.setAddress(JOptionPane.showInputDialog("Địa chỉ", customer.getAddress()));
					break;
				case 3:
					customer.setNote(JOptionPane.showInputDialog("Ghi chú", customer.getNote()));
					break;
				}
				customerService.deleteCustomer(customer.getPhone());
				customerService.addCustomer(customer);	
			} else {
				JOptionPane.showMessageDialog(null, "Xin chọn ô cần sửa");
			}
			
			refresh();
		}		
	}
	
	private class findActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String key = txtFind.getText();
			List<Customer> list = customerService.findCustomer(key);
			Object[][] objects = new Object[list.size()][4];
			for (int i = 0; i < list.size(); i++) {
				objects[i][0] = list.get(i).getPhone();
				objects[i][1] = list.get(i).getName();
				objects[i][2] = list.get(i).getAddress();
				objects[i][3] = list.get(i).getNote();
			}
			tblCustomer.setModel(new DefaultTableModel(objects, tableHeader));
			tblCustomer.setColumnSelectionAllowed(true);
			scrollPane.setViewportView(tblCustomer);
		}
		
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmMain frame = new FrmMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	/**
	 * Create the frame.
	 */
	public FrmMain() {
		setTitle("Quản lý khách hàng");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 946, 489);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmAdd = new JMenuItem("Thêm");
		mntmAdd.addActionListener(new addActionListener());
		mntmAdd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		mnFile.add(mntmAdd);
		
		JMenuItem mntmXa = new JMenuItem("Xóa");
		mntmXa.addActionListener(new deleteActionListener());
		
		JMenuItem mntmSa = new JMenuItem("Sửa");
		mntmSa.addActionListener(new updateActionListener());
		mntmSa.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSa);
		
		JMenuItem mntmTmKim = new JMenuItem("Tìm kiếm");
		mntmTmKim.addActionListener(new findActionListener());
		mntmTmKim.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		mnFile.add(mntmTmKim);
		mntmXa.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
		mnFile.add(mntmXa);
		
		JMenuItem menuItem = new JMenuItem("Refresh");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		mnFile.add(menuItem);
		
		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);
		
		JMenuItem mntmAuthor = new JMenuItem("Author");
		mntmAuthor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String info = "Tác giả: Nguyễn Đức Tâm.\n";
				info += "Mọi đóng góp xin gửi về: nguyenductamlhp@gmail.com";
				JOptionPane.showMessageDialog(null, info);
			}
		});
		
		JMenuItem mntmHngDn = new JMenuItem("Hướng dẫn");
		mntmHngDn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String guide = "Hướng dẫn \n";
				guide += "Ctrl + A : Thêm mới \n";
				guide += "Ctrl + S : Sửa \n";
				guide += "Ctrl + D : Xóa \n";
				guide += "F5 : Refresh";
				JOptionPane.showMessageDialog(null, guide);
				
			}
		});
		mnAbout.add(mntmHngDn);
		mnAbout.add(mntmAuthor);
		
		JMenuItem mntmTool = new JMenuItem("Tool");
		mntmTool.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Customer Manager version 1.1");
			}
		});
		mnAbout.add(mntmTool);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		scrollPane.setBounds(30, 20, 596, 367);
		contentPane.add(scrollPane);
		
		tblCustomer = new JTable();
		tblCustomer.setCellSelectionEnabled(true);
		tblCustomer.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		tblCustomer.setModel(new DefaultTableModel(getTableData(), tableHeader));
		tblCustomer.setColumnSelectionAllowed(true);
		scrollPane.setViewportView(tblCustomer);
		
		JButton btnAdd = new JButton("Thêm");
		btnAdd.addActionListener(new addActionListener());
		btnAdd.setBounds(802, 364, 89, 23);
		contentPane.add(btnAdd);
		
		JButton btnUpdate = new JButton("Sửa");
		btnUpdate.addActionListener(new updateActionListener());
		btnUpdate.setBounds(80, 398, 89, 23);
		contentPane.add(btnUpdate);
		
		JButton btnXa = new JButton("Xóa");
		btnXa.addActionListener(new deleteActionListener());
		btnXa.setBounds(201, 399, 89, 23);
		
		contentPane.add(btnXa);
		
		txtPhone = new JTextField();
		txtPhone.setBounds(767, 220, 149, 20);
		contentPane.add(txtPhone);
		txtPhone.setColumns(10);
		
		txtName = new JTextField();
		txtName.setColumns(10);
		txtName.setBounds(767, 251, 149, 20);
		contentPane.add(txtName);
		
		txtAddress = new JTextField();
		txtAddress.setColumns(10);
		txtAddress.setBounds(767, 282, 149, 20);
		contentPane.add(txtAddress);
		
		txtNote = new JTextField();
		txtNote.setColumns(10);
		txtNote.setBounds(767, 320, 149, 20);
		contentPane.add(txtNote);
		
		JLabel lblSinThoi = new JLabel("Số điện thoại");
		lblSinThoi.setBounds(661, 220, 78, 20);
		contentPane.add(lblSinThoi);
		
		JLabel lblTn = new JLabel("Tên");
		lblTn.setBounds(661, 254, 78, 20);
		contentPane.add(lblTn);
		
		JLabel lblaCh = new JLabel("Địa  chỉ");
		lblaCh.setBounds(661, 285, 78, 20);
		contentPane.add(lblaCh);
		
		JLabel lblGhiCh = new JLabel("Ghi chú");
		lblGhiCh.setBounds(661, 323, 78, 20);
		contentPane.add(lblGhiCh);
		
		txtFind = new JTextField();
		txtFind.setText("0");
		txtFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String keyPhone = txtFind.getText();
				if (!keyPhone.isEmpty()) {
					
				}
			}
		});
		txtFind.setBounds(661, 39, 165, 20);
		contentPane.add(txtFind);
		txtFind.setColumns(10);
		
		JButton btnFind = new JButton("Find");
		btnFind.addActionListener(new findActionListener());
		btnFind.setBounds(836, 38, 70, 20);
		contentPane.add(btnFind);
	}
}
