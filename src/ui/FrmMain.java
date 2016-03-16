package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import model.data.Customer;
import model.service.CustomerService;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import jdk.nashorn.internal.scripts.JO;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class FrmMain extends JFrame {

	private JPanel contentPane;
	private JTable tblCustomer;
	JScrollPane scrollPane = new JScrollPane();
	
	CustomerService customerService = new CustomerService();
	
	private Object[][] getTableData() {		
		
		List<Customer> list = customerService.getAllCustomer();
		Object[][] objects = new Object[list.size()][6];
		for (int i = 0; i < list.size(); i++) {
			objects[i][0] = list.get(i).getPhone();
			objects[i][1] = list.get(i).getName();
			objects[i][2] = list.get(i).getAddress();
			objects[i][3] = list.get(i).getNote();
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
			customer.setPhone(JOptionPane.showInputDialog("Số điện thoại"));
			customer.setName(JOptionPane.showInputDialog("Họ tên"));
			customer.setAddress(JOptionPane.showInputDialog("Địa chỉ"));
			customer.setNote(JOptionPane.showInputDialog("Ghi chú"));
			
			customerService.addCustomer(customer);
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
				customerService.updateCustomer(customer);	
			} else {
				JOptionPane.showMessageDialog(null, "Xin chọn ô cần sửa");
			}
			
			refresh();
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
		setBounds(100, 100, 670, 343);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmAdd = new JMenuItem("Thêm");
		mntmAdd.addActionListener(new addActionListener());
		mntmAdd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		mnFile.add(mntmAdd);
		
		JMenuItem menuItem = new JMenuItem("Refresh");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		mnFile.add(menuItem);
		
		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);
		
		JMenuItem mntmAuthor = new JMenuItem("Author");
		mntmAuthor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Tác giả: Nguyễn Đức Tâm. Mọi đóng góp xin gửi về: nguyenductamlhp@gmail.com");
			}
		});
		mnAbout.add(mntmAuthor);
		
		JMenuItem mntmTool = new JMenuItem("Tool");
		mntmTool.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Customer Manager version 1.0");
			}
		});
		mnAbout.add(mntmTool);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		scrollPane.setBounds(30, 20, 500, 250);
		contentPane.add(scrollPane);
		
		tblCustomer = new JTable();
		tblCustomer.setModel(new DefaultTableModel(getTableData(), tableHeader));
		tblCustomer.setColumnSelectionAllowed(true);
		scrollPane.setViewportView(tblCustomer);
		
		JButton btnAdd = new JButton("Thêm");
		btnAdd.addActionListener(new addActionListener());
		btnAdd.setBounds(555, 35, 89, 23);
		contentPane.add(btnAdd);
		
		JButton btnUpdate = new JButton("Sửa");
		btnUpdate.addActionListener(new updateActionListener());
		btnUpdate.setBounds(555, 96, 89, 23);
		contentPane.add(btnUpdate);
		
		JButton btnXa = new JButton("Xóa");
		btnXa.addActionListener(new deleteActionListener());
		btnXa.setBounds(555, 151, 89, 23);
		contentPane.add(btnXa);
	}
}
