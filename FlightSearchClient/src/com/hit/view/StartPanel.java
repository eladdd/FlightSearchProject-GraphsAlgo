package com.hit.view;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.LinkedList;
import javax.swing.JTextField;

public class StartPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JTextField txtSrcCountry;
	private JTextField txtDestCountry;
	private JTextField txtPriceFlight;

	/**
	 * Create the panel.
	 */
	public StartPanel(JPanel cardPanel, MyView myview) {
		setLayout(null);
		this.setBounds(100, 100, 670, 500);

		JButton btnSearchBFS = new JButton("Shrotest Flights");
		btnSearchBFS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myview.updateData(new String[] { "graphTypeBFS", null, null });
				myview.updateData(new String[] { "GETCOUNTRY", null, null });
				((CardLayout) cardPanel.getLayout()).show(cardPanel, "Shortest Flights");// show the panel
			}
		});
		btnSearchBFS.setBackground(new Color(255, 160, 122));
		btnSearchBFS.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSearchBFS.setIcon(new ImageIcon(StartPanel.class.getResource("/com/hit/resources/BFS.png")));
		btnSearchBFS.setBounds(27, 304, 152, 48);
		add(btnSearchBFS);

		JButton btnSearchDIJ = new JButton("Cheapest Flights");
		btnSearchDIJ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myview.updateData(new String[] { "graphTypeDIJ", null, null });
				myview.updateData(new String[] { "GETCOUNTRY", null, null });
				((CardLayout) cardPanel.getLayout()).show(cardPanel, "Cheapest Flights");// show the panel
			}
		});
		btnSearchDIJ.setBackground(new Color(240, 230, 140));
		btnSearchDIJ.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSearchDIJ.setIcon(new ImageIcon(StartPanel.class.getResource("/com/hit/resources/DIJ.png")));
		btnSearchDIJ.setBounds(241, 304, 152, 48);
		add(btnSearchDIJ);

		JButton btnDeleteFlight = new JButton("Delete");
		btnDeleteFlight.setIcon(new ImageIcon(StartPanel.class.getResource("/com/hit/resources/delete.png")));
		btnDeleteFlight.setBackground(new Color(240, 128, 128));
		btnDeleteFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
				if (row == -1) {
					if (table.getRowCount() == 0) {
						JOptionPane.showMessageDialog(null, "No data to select");
					} else {
						JOptionPane.showMessageDialog(null, "Please select a row ");
					}
				} else {
					String srcCountry = table.getValueAt(row, 1).toString();
					String destCountry = table.getValueAt(row, 2).toString();
					int check = JOptionPane.showConfirmDialog(null,
							"Request for deleting flight " + srcCountry + " to " + destCountry, "Delete Flight",
							JOptionPane.YES_NO_OPTION);
					if (check == 0) {
						myview.updateData(new String[] { "DELETE FLIGHT", srcCountry, destCountry });
						myview.updateData(new String[] { "LOAD", null, null });

					}
				}

			}
		});
		btnDeleteFlight.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.ITALIC, 14));
		btnDeleteFlight.setBounds(403, 126, 118, 23);
		btnDeleteFlight.setEnabled(false);
		add(btnDeleteFlight);

		JButton btnLoadData = new JButton("Load Data");
		btnLoadData.setIcon(new ImageIcon(StartPanel.class.getResource("/com/hit/resources/load.png")));
		btnLoadData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myview.updateData(new String[] { "LOAD", null, null });

			}
		});
		btnLoadData.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.ITALIC, 14));
		btnLoadData.setBackground(new Color(255, 222, 173));
		btnLoadData.setBounds(403, 92, 118, 23);
		add(btnLoadData);

		JButton btnAdd = new JButton("");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String src = txtSrcCountry.getText();
				String dest = txtDestCountry.getText();
				String price = txtPriceFlight.getText();
				if(validateInput(src,dest,price))
				{
					myview.updateData(new String[] { "ADD FLIGHT", src.toLowerCase(), dest.toLowerCase(), price }); //avoid multiple names as israel/IsrAel
					myview.updateData(new String[] { "LOAD", null, null });
				}
			}

		});
		btnAdd.setIcon(new ImageIcon(StartPanel.class.getResource("/com/hit/resources/add.png")));
		btnAdd.setBounds(327, 396, 47, 23);
		btnAdd.setVisible(false);
		add(btnAdd);

		JButton btnAddFlight = new JButton("Add Flight");
		btnAddFlight.setBackground(new Color(143, 188, 143));
		btnAddFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtSrcCountry.setVisible(true);
				txtDestCountry.setVisible(true);
				txtPriceFlight.setVisible(true);
				btnAdd.setVisible(true);
			}
		});
		btnAddFlight.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.ITALIC, 14));
		btnAddFlight.setBounds(27, 363, 87, 23);
		btnAddFlight.setEnabled(false);
		add(btnAddFlight);

		txtSrcCountry = new JTextField();
		txtSrcCountry.setToolTipText("source country");
		txtSrcCountry.setBounds(27, 397, 86, 20);
		txtSrcCountry.setVisible(false);
		add(txtSrcCountry);
		txtSrcCountry.setColumns(10);

		txtDestCountry = new JTextField();
		txtDestCountry.setToolTipText("Destination");
		txtDestCountry.setColumns(10);
		txtDestCountry.setBounds(121, 397, 86, 20);
		txtDestCountry.setVisible(false);
		add(txtDestCountry);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(23, 72, 370, 210);
		add(scrollPane);

		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // make same size for all columns
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Src", "Dest", "Price" }) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}) // make table uneditable
		;
		((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER); // header
																														// to
																														// center
		scrollPane.setViewportView(table);

		txtPriceFlight = new JTextField();
		txtPriceFlight.setToolTipText("price");
		txtPriceFlight.setColumns(10);
		txtPriceFlight.setBounds(220, 397, 86, 20);
		txtPriceFlight.setVisible(false);
		add(txtPriceFlight);

		JButton btnPremission = new JButton("");
		btnPremission.setToolTipText("Admin Premission");
		btnPremission.setIcon(new ImageIcon(StartPanel.class.getResource("/com/hit/resources/key.png")));
		btnPremission.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPasswordField pass = new JPasswordField(10);
				int log = JOptionPane.showConfirmDialog(null, pass, "Enter admin password: (1234)", JOptionPane.OK_CANCEL_OPTION);
				if (log == JOptionPane.OK_OPTION) // pressing OK button
				{
					String myPass=String.valueOf(pass.getPassword());
					if(myPass.equals("1234"))
					{
						btnAddFlight.setEnabled(true);
						btnDeleteFlight.setEnabled(true);
					}
					else {
						JOptionPane.showMessageDialog(null, "wrong password");
					}
				}

			}
		});
		btnPremission.setBounds(578, 396, 57, 31);
		add(btnPremission);

		JLabel lblTitle = new JLabel("Available");
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblTitle.setBounds(66, 30, 113, 31);
		add(lblTitle);

		JLabel lblTitle_1 = new JLabel("Flights");
		lblTitle_1.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblTitle_1.setBounds(175, 30, 83, 31);
		lblTitle_1.setForeground(new Color(255, 165, 0));
		add(lblTitle_1);

		JLabel lblBackGround = new JLabel("");
		lblBackGround.setIcon(new ImageIcon(StartPanel.class.getResource("/com/hit/resources/background2.jpg")));
		lblBackGround.setBounds(0, 0, 670, 500);
		add(lblBackGround);
	}

	/*
	 * this function insert the Flights DB into the table
	 */
	public void loadDataTableFlights(LinkedList<String[]> flights) {

		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		Object rowData[] = new Object[table.getColumnCount()];
		int i = 1;
		for (String[] data : flights) {
			rowData[0] = i;
			rowData[1] = data[0];
			rowData[2] = data[1];
			rowData[3] = data[2];
			i++;
			model.addRow(rowData);
		}
		// center the data for each column
		DefaultTableCellRenderer rendar = new DefaultTableCellRenderer();
		rendar.setHorizontalAlignment(SwingConstants.CENTER);
		for (int j = 0; j < rowData.length; j++) {
			table.getColumnModel().getColumn(j).setCellRenderer(rendar); // row data equal to number of columns
		}

	}
	
	private boolean validateInput(String src, String dest, String price) {
		boolean isNumeric;
		try {
			Double.parseDouble(price);
			isNumeric=true;
		} catch (NumberFormatException e) {
			isNumeric=false;
		}
		if (src.equals("") || dest.equals("") || price.equals("")) {
			JOptionPane.showMessageDialog(null, "some fields are missing.try again");

		} 
		else if(src.equals(dest))
		{
			JOptionPane.showMessageDialog(null, "Country source and destination are equals.try again");
		}
		else if(!isNumeric)
		{
			JOptionPane.showMessageDialog(null, "invalid price. please enter just numbers");
		}
		else {
			return true;
		}
		return false;
	}
}
