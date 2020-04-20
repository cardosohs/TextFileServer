package client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;


	public class GUI extends JFrame {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JPanel contentPane;
		Client client;
		JList<String> list;
		
/*
		public static void main(String[] args) {
			GUI frame = new GUI();
			frame.init();

		}	
*/
		public GUI (Client client) {
			this.client = client;
		}
		
		public void init() {
			buildGUI();
			//centerWindow();
			setResizable(false);
			setVisible(true);
		}


		void buildGUI() {
			
			setTitle("FileExplorer");
			setBounds(100, 100, 607, 697);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JButton botao_tamanho = new JButton("Tamanho");
			JFrame frame = this;
			botao_tamanho.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String selected = list.getSelectedValue();
					if(selected == null)
						JOptionPane.showMessageDialog(frame, "Selecione um ficheiro!");
					else
						JOptionPane.showMessageDialog(frame, "O tamanho do ficheiro é de " +
													client.remoteDir.length(selected) +
													" bytes");
				}
			});
			botao_tamanho.setBounds(28, 612, 97, 25);
			contentPane.add(botao_tamanho);
			
			JButton botao_exibir = new JButton("Exibir");
			botao_exibir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String selected = list.getSelectedValue();
					if(selected == null)
						JOptionPane.showMessageDialog(frame, "Selecione um ficheiro!");
					try {
						if (client.remoteDir.fileExists(selected)) {
							String text = client.remoteDir.getFile(selected).read();
							setVisible(false);
							new ShowTextGUI(client, selected, text).init();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
				}
			});
			botao_exibir.setBounds(137, 612, 97, 25);
			contentPane.add(botao_exibir);
			
			
			JButton botao_editar = new JButton("Editar");
			botao_editar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String selected = list.getSelectedValue();
					if(selected == null)
						JOptionPane.showMessageDialog(frame, "Selecione um ficheiro!");
					try {
						if (client.remoteDir.fileExists(selected)) {
							String text = client.remoteDir.getFile(selected).read();
							client.notifyAquireWriteLock(selected);
							setVisible(false);
							new EditTextGUI(client, selected, text).init();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
				}
			});
			botao_editar.setBounds(246, 612, 97, 25);
			contentPane.add(botao_editar);
			
			
			JButton botao_novo = new JButton("Novo");
			botao_novo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);
					new NewTextGUI(client).init();
				}
			});
			botao_novo.setBounds(355, 612, 97, 25);
			contentPane.add(botao_novo);
			
			JButton botao_apagar = new JButton("Apagar");
			botao_apagar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String selected = list.getSelectedValue();
					if(selected == null)
						JOptionPane.showMessageDialog(frame, "Selecione um ficheiro!");
					else {
						client.sendDeleteRequest(selected);
					}
				}
			});
			botao_apagar.setBounds(464, 612, 97, 25);
			contentPane.add(botao_apagar);
			
			String [] data = client.remoteDir.getDirectoryListing();
			list = new JList<String>(data);
			list.setBounds(12, 13, 565, 586);
			contentPane.add(list);
			
			addWindowListener(new WindowAdapter() {
			    @Override
			    public void windowClosing(WindowEvent windowEvent) {
			    	client.disconnectFromServer();
			    	dispose();
			    }
			});
		}
		
		
//		void centerWindow() {
//			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
//		    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
//		    int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
//		    this.setLocation(x, y);
//		}
		


}
