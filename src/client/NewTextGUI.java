package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class NewTextGUI extends GUI {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton botao_guardar;
	private JButton botao_retroceder;
	private JTextField textField_name;
	
	public NewTextGUI(Client client) {
		super(client);
	}
	
	
	@Override
	void buildGUI() {
		
		JFrame frame = this;
		setTitle("FileExplorer - NEW FILE");
		setDefaultCloseOperation(0);
		setResizable(false);
		setBounds(30, 31, 500, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JLabel lblNomeDoFicheiro = new JLabel("Nome do ficheiro:");
		lblNomeDoFicheiro.setBounds(12, 22, 102, 16);
		contentPane.add(lblNomeDoFicheiro);
		
		textField_name = new JTextField();
		textField_name.setBounds(126, 13, 152, 34);
		contentPane.add(textField_name);
		textField_name.setColumns(10);
		
		
		botao_retroceder = new JButton("Retroceder");
		botao_retroceder.setBounds(327, 80, 97, 25);
		botao_retroceder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new GUI(client).init();
				dispose();
			}
		});
		contentPane.add(botao_retroceder);

		/* apenas permite guardar o ficheiro uma única vez */
		botao_guardar = new JButton("Guardar");
		botao_guardar.setBounds(50, 80, 97, 25);
		botao_guardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = textField_name.getText();
				if (name.equals(""))
					JOptionPane.showMessageDialog(frame, "Campo não preenchido!");
				client.sendNewFileName(textField_name.getText());
				botao_guardar.setEnabled(false);
			}
		});
		contentPane.add(botao_guardar);
		setResizable(false);
		
	}
}
