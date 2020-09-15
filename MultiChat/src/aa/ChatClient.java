package aa;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

//import aa.TcpClient.ThreadClientRcvClass;
//import aa.TcpClient.ThreadClientSendClass;

//gui+send+rcv Ŭ����
class Chatt extends JFrame implements ActionListener, Runnable, WindowListener{
	
	// ù��° ���� 

	private JPanel first_pan = new JPanel(new BorderLayout(5, 5));;
	private JTextField txtMultiChat;
	private JTextField nicknametxt = new JTextField();//�г���
	private JTextField porttxt = new JTextField();//��Ʈ��ȣ
	private JTextField iptxt = new JTextField();//�������ּ�
	private JButton startbtn = new JButton("START");
	
	// �ι�° Ŭ���̾�Ʈ
	
	private Container con;
	private CardLayout cl = new CardLayout();
	JPanel second_pan = new JPanel(new BorderLayout(5, 5));
	static JTextField chatText =  new JTextField();//ä��ġ�� ��
	static JTextArea textArea = new JTextArea(); // �����ؽ�Ʈ �����
	//JTextArea textArea_1 = new JTextArea(); // ���� ���� ���
	JTextField scrollPane_2 = new JTextField();
	JLabel lblNewLabel = new JLabel("0");//�����ο� ��
	JButton btnNewButton = new JButton("������");//�������ư
	JButton button_1 = new JButton("\uBA54\uC778\uD654\uBA74");//����ȭ�� ��ư
	JButton btnNewButton_1 = new JButton("������");
		JButton button = new JButton("��������");
	
	///// 
	
	private Vector inwon_vc = new Vector(); // ���»�� �г��� �ִ� ����
	private JList inwon_li = new JList(inwon_vc); // �г��� ���ִ� ���͸� �þ���� ����Ʈ
	private JScrollPane inwon_jsp = new JScrollPane(inwon_li); // ���� ���� ���
	
	private Socket soc;
	private PrintWriter out;
	private BufferedReader in;
	private Thread currentThread;
	//static Scanner in1 = new Scanner(System.in); // Ŭ���̾�Ʈ�� �氡�氡 �Է�
	
	public Chatt() { // ����������
		
		super("ä�� ���α׷�!");
		this.Chat();
		this.start();
		this.setSize(900, 700);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frm = this.getSize();
		int xpos = (int) (screen.getWidth() / 2 - frm.getWidth() / 2);
		int ypos = (int) (screen.getHeight() / 2 - frm.getHeight() / 2);
		this.setLocation(xpos, ypos);
		this.setVisible(true);
	}


	public void Chat() {//������
		
		con = this.getContentPane();
		con.setLayout(cl);
		
		first_pan.setBorder(new EmptyBorder(5, 5, 5, 5));
		first_pan.setLayout(null);
		
		txtMultiChat = new JTextField();
		txtMultiChat.setEnabled(false);
		txtMultiChat.setEditable(false);
		txtMultiChat.setBackground(Color.gray);
		txtMultiChat.setFont(new Font("David Libre", Font.PLAIN, 40));
		txtMultiChat.setHorizontalAlignment(SwingConstants.CENTER);
		txtMultiChat.setText("Multi Chat");
		txtMultiChat.setBounds(233, 119, 412, 78);
		first_pan.add(txtMultiChat);
		txtMultiChat.setColumns(10);
		
		//�г���
		nicknametxt.setBackground(Color.GRAY);
		nicknametxt.setFont(new Font("Monospaced", Font.PLAIN, 16));
		nicknametxt.setText("�г���");
		nicknametxt.setCaretPosition(0);
		nicknametxt.setBounds(350, 290, 155, 45);
		first_pan.add(nicknametxt);
		
		//��Ʈ��ȣ
		porttxt.setText("��Ʈ��ȣ");
		porttxt.setFont(new Font("Monospaced", Font.PLAIN, 16));
		porttxt.setCaretPosition(0);
		porttxt.setBackground(Color.GRAY);
		porttxt.setBounds(350, 357, 155, 45);
		first_pan.add(porttxt);
		
		//������
		iptxt.setText("127.0.0.1");
		iptxt.setFont(new Font("Monospaced", Font.PLAIN, 16));
		iptxt.setCaretPosition(0);
		iptxt.setBackground(Color.GRAY);
		iptxt.setBounds(350, 425, 155, 45);
		first_pan.add(iptxt);
	
		startbtn.setBounds(359, 509, 135, 53);
		first_pan.add(startbtn);
		
		con.add("main", first_pan);
		
		///// first
		
		second_pan.setBorder(new EmptyBorder(5, 5, 5, 5));
		second_pan.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 35, 550, 477);
		second_pan.add(scrollPane);

		//ä���� �ö���� ��
		textArea.setEnabled(false);
		textArea.setEditable(false);
		textArea.setBackground(Color.DARK_GRAY);
		scrollPane.setViewportView(textArea);
		//textArea.setText(inputChat);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(587, 189, 285, 440);
		second_pan.add(scrollPane_1);

		//�����ο� ���
		inwon_jsp.setEnabled(false);
		inwon_jsp.setBackground(Color.GRAY);
		scrollPane_1.setViewportView(inwon_jsp);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 522, 450, 107);
		second_pan.add(scrollPane_2);

		//ä��ġ�� ��
		scrollPane_2.setViewportView(chatText);
		//chatText.setColumns(10);
		chatText.setText("ä��ġ�°�");

		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBounds(587, 91, 285, 88);
		second_pan.add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("\uC785\uC7A5\uC778\uC6D0 :");
		label.setFont(new Font("����", Font.BOLD, 35));
		label.setForeground(Color.WHITE);
		label.setBounds(12, 10, 176, 68);
		panel.add(label);

		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("����", Font.BOLD, 30));
		lblNewLabel.setBounds(192, 10, 93, 68);
		panel.add(lblNewLabel);

		//������ ��ư
		btnNewButton.setFont(new Font("����", Font.BOLD, 15));
		btnNewButton.setBounds(474, 522, 97, 54);
		second_pan.add(btnNewButton);

		//������
		btnNewButton_1.setBounds(743, 20, 129, 54);
		second_pan.add(btnNewButton_1);

		//�������� ��ư
		button.setFont(new Font("����", Font.BOLD, 15));
		button.setBounds(474, 586, 97, 54);
		second_pan.add(button);
		
		button_1.setBounds(587, 20, 129, 54);
		second_pan.add(button_1);
		
		inwon_vc.add("[ ���� �ο� ��� ]");
		
		this.setTitle("�ڹٴ� Fun Fun Fun");
		this.setBounds(100, 100, 900, 700);
		this.setVisible(true);
		
		con.add("client", second_pan);
		
		/// second
		
	}
	
	public void start() {
		chatText.addActionListener(this); // ä�� ġ�°�
		
		btnNewButton.addActionListener(this); // ������ ��ư
		button_1.addActionListener(this); // ����ȭ������ ��ư 
		btnNewButton_1.addActionListener(this); // ������ ��ư
		this.addWindowListener(this);
		////
		startbtn.addActionListener(this); // ����ȭ�� ��ŸƮ��ư
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == chatText || e.getSource() == btnNewButton) { // ä���� ��
			String str = chatText.getText();
			if (str == null || str.trim().length() == 0) // str.trim()=> �յ� ������ ������ str
				return;
//			if (hide_jrb.isSelected()) {
//				String dest = (String) inwon_li.getSelectedValue();
//				if (dest == null)
//					return;
//				String data = "/w" + dest + "-" + str;
//				out.println(data);
//			} else {
//				out.println(str);
//			}

			out.println(str);

			out.flush();
			chatText.setText("");
			chatText.requestFocus(); // Ŀ�� �ٽ� ä��â����
			
		}  else if (e.getSource() == startbtn) { // ù��° ȭ�� �α��� ��ư and client ȭ�� ����
			String nickname = nicknametxt.getText();
			String port = "486";
			String ip = "192.168.200.115";
			
			if (nickname == null || nickname.trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "�г����� �����ּ���", "���", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (porttxt.getText() == null || porttxt.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "��Ʈ��ȣ�� ������ϴ�.", "���", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (iptxt.getText() == null || iptxt.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "ip�� ������ϴ�.", "���", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(!porttxt.getText().equals(port)) {
				JOptionPane.showMessageDialog(this, "��Ʈ��ȣ�� Ʋ�Ƚ��ϴ�.", "���", JOptionPane.ERROR_MESSAGE);
				iptxt.setText("");
				porttxt.setText("");
				return;
			} else if(!iptxt.getText().equals(ip)) {
				JOptionPane.showMessageDialog(this, "ip�� Ʋ�Ƚ��ϴ�.", "���", JOptionPane.ERROR_MESSAGE);
				iptxt.setText("");
				porttxt.setText("");
				return;
			} else{
				
				try {
					soc = new Socket(iptxt.getText(), Integer.parseInt(porttxt.getText())); // ������ ��Ʈ��ȣ �޾ƿ�

					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())));
					// �ƿ���Ʈ��
					in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
					// ��� ���.. ������ ���̱�.
					currentThread = new Thread(this);
					currentThread.start();
					out.println(nickname);
					out.flush();
					chatText.requestFocus();
				} catch (IOException ee) {
					System.err.println("���ӿ���!");
					return;
				}
				
				cl.show(con, "client"); // ä�� ȭ�� �ѱ�
			}
			
		} else if (e.getSource() == button_1) { // �������� ��ư ������

			porttxt.setText("486");
			iptxt.setText("192.168.0.30");
			cl.show(con, "main"); // ����ȭ�� �ѱ�
			out.println("/q");
			out.flush();
			currentThread.interrupt();
			currentThread = null;
			inwon_vc.clear();
			inwon_vc.add("[ ���� �ο� ��� ]");
			inwon_li.setListData(inwon_vc);
			lblNewLabel.setText("0");
			soc = null;
			out = null;
			in = null;

//			inwon_vc.clear();
//			inwon_vc.add("== Room Member ==");
//			inwon_li.setListData(inwon_vc);
//			inwon_tf.setText("0");
//			view_ta.setText("");
		} else if (e.getSource() == btnNewButton_1) {
			if (out != null) {
				out.println("/q");
				out.flush();
				currentThread.interrupt();
			}
			System.exit(0);
		}
	}
	
	@Override
	public void run() {
		textArea.setEnabled(false);
//		view_ta.setForeground(Color.blue);// ���� ����
//		view_ta.setDisabledTextColor(Color.blue);
		textArea.setText("### ��ȭ�濡 ���� �ϼ̽��ϴ�. ###\n\n");
		while (true) {
			try {
				String str = in.readLine();
				// if(str == null) beak;
				if (str.charAt(0) == '/') {
					if (str.charAt(1) == 'q') {
						String name = str.substring(2).trim(); // /q �ڸ��� �̸��� ����
						textArea.append("%%% " + name + "�Բ��� �����ϼ̽��ϴ�.%%%\n");
						for (int i = 0; i < inwon_vc.size(); i++) {
							String imsi = (String) inwon_vc.elementAt(i);
							if (imsi.equals(name)) {
								int pos = inwon_li.getSelectedIndex();
								inwon_vc.removeElementAt(i);
								inwon_li.setListData(inwon_vc);
								inwon_li.setSelectedIndex(pos);
								break;
							}
						}
						int xx = Integer.parseInt(lblNewLabel.getText()); // �����ο� ���ִ� ��
						lblNewLabel.setText(String.valueOf(--xx));
					} else if (str.charAt(1) == 'p') { // �۾տ� p �� ����� ��
						int pos = inwon_li.getSelectedIndex(); // ����Ʈ ũ����� int pos ����
						String user = str.substring(2).trim(); // /p ���� �̸��� user �ȿ� ����
//						if(inwon_vc.contains(user)) {
//							System.out.println("�ߺ��г�");
//							nickname_tf.setText("");
//						}
						inwon_vc.add(user); // ������Ͽ� ��� ���� �̸� �ְ�
						inwon_li.setListData(inwon_vc); // ���ŵ� ����� ����Ʈ�� �ٽ�����
						inwon_li.setSelectedIndex(pos); 
						textArea.append("*** " + user + "�Բ��� �����ϼ̽��ϴ�.***\n");
						int xx = Integer.parseInt(lblNewLabel.getText().trim());
						lblNewLabel.setText(String.valueOf(++xx)); // �ο� ���� 1�� �ø���
					} else if (str.charAt(1) == 'o') { // �۾տ� o �� ����� ��
						String user = str.substring(2).trim();
						inwon_vc.add(user);
						inwon_li.setListData(inwon_vc);
						int xx = Integer.parseInt(lblNewLabel.getText().trim());
						lblNewLabel.setText(String.valueOf(++xx));
					}
				} else { // �Ϲ� ä�ý�
					textArea.append(str + "\n");
					textArea.setCaretPosition(textArea.getText().trim().length() - str.trim().length() + 1);
				}
			} catch (IOException ee) {
				System.err.println("read error = " + ee.toString());
			}
		}
	}


	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowClosing(WindowEvent e) {
		
		if (out != null) {
			out.println("/q");
			out.flush();
			currentThread.interrupt();
		}
		System.exit(0);
		
	}


	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}

public class ChatClient {
	public static void main(String[] args) {
		
		Chatt tc = new Chatt();
	}
}

