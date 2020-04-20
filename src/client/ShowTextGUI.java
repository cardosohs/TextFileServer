package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JTextArea;


public class ShowTextGUI extends GUI {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton botao_fechar;
	private JTextArea textArea;
	private String text;
	private String filename;
	
	public ShowTextGUI(Client client, String filename, String text) {
		super(client);
		this.text = text;
		this.filename = filename;
	}
	
	
	@Override
	void buildGUI() {
		
		setTitle("FileExplorer - SHOW FILE");
		setDefaultCloseOperation(0);
		setResizable(false);
		setBounds(30, 31, 580, 500);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea(text);
		textArea.setBounds(12, 13, 550, 408);
		contentPane.add(textArea);
		
		botao_fechar = new JButton("Fechar");
		botao_fechar.setBounds(241, 434, 97, 25);
		botao_fechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				client.notifyReleaseReadLock(filename);
				new GUI(client).init();
				dispose();
			}
		});
		contentPane.add(botao_fechar);
		setResizable(false);
		
	}
}
