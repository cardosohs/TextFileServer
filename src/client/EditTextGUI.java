package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JTextArea;


public class EditTextGUI extends GUI {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton botao_gravar;
	private JTextArea textArea;
	private String text;
	private String filename;
	
	public EditTextGUI(Client client, String filename, String text) {
		super(client);
		this.text = text;
		this.filename = filename;
	}
	
	
	@Override
	void buildGUI() {
		
		setTitle("FileExplorer - EDIT FILE");
		setDefaultCloseOperation(0);
		setResizable(false);
		setBounds(30, 31, 580, 500);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea(text);
		textArea.setBounds(12, 13, 550, 408);
		contentPane.add(textArea);
		
		botao_gravar = new JButton("Gravar");
		botao_gravar.setBounds(241, 434, 97, 25);
		botao_gravar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String toSave = textArea.getText();
				try {
					client.remoteDir.getFile(filename).write(toSave);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				//client.notifyReleaseWriteLock(filename);
				new GUI(client).init();
				dispose();
			}
		});
		contentPane.add(botao_gravar);
		setResizable(false);
		
	}
}
