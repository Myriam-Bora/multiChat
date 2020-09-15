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

//gui+send+rcv 클래스
class Chatt extends JFrame implements ActionListener, Runnable, WindowListener{
	
	// 첫번째 메인 

	private JPanel first_pan = new JPanel(new BorderLayout(5, 5));;
	private JTextField txtMultiChat;
	private JTextField nicknametxt = new JTextField();//닉네임
	private JTextField porttxt = new JTextField();//포트번호
	private JTextField iptxt = new JTextField();//아이피주소
	private JButton startbtn = new JButton("START");
	
	// 두번째 클라이언트
	
	private Container con;
	private CardLayout cl = new CardLayout();
	JPanel second_pan = new JPanel(new BorderLayout(5, 5));
	static JTextField chatText =  new JTextField();//채팅치는 곳
	static JTextArea textArea = new JTextArea(); // 메인텍스트 에어리어
	//JTextArea textArea_1 = new JTextArea(); // 참가 유저 목록
	JTextField scrollPane_2 = new JTextField();
	JLabel lblNewLabel = new JLabel("0");//입장인원 라벨
	JButton btnNewButton = new JButton("보내기");//보내기버튼
	JButton button_1 = new JButton("\uBA54\uC778\uD654\uBA74");//메인화면 버튼
	JButton btnNewButton_1 = new JButton("나가기");
		JButton button = new JButton("파일전송");
	
	///// 
	
	private Vector inwon_vc = new Vector(); // 들어온사람 닉네임 넣는 벡터
	private JList inwon_li = new JList(inwon_vc); // 닉네임 모여있는 벡터를 늘어놓는 리스트
	private JScrollPane inwon_jsp = new JScrollPane(inwon_li); // 참가 유저 목록
	
	private Socket soc;
	private PrintWriter out;
	private BufferedReader in;
	private Thread currentThread;
	//static Scanner in1 = new Scanner(System.in); // 클라이언트가 방가방가 입력
	
	public Chatt() { // 메인프레임
		
		super("채팅 프로그램!");
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


	public void Chat() {//생성자
		
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
		
		//닉네임
		nicknametxt.setBackground(Color.GRAY);
		nicknametxt.setFont(new Font("Monospaced", Font.PLAIN, 16));
		nicknametxt.setText("닉네임");
		nicknametxt.setCaretPosition(0);
		nicknametxt.setBounds(350, 290, 155, 45);
		first_pan.add(nicknametxt);
		
		//포트번호
		porttxt.setText("포트번호");
		porttxt.setFont(new Font("Monospaced", Font.PLAIN, 16));
		porttxt.setCaretPosition(0);
		porttxt.setBackground(Color.GRAY);
		porttxt.setBounds(350, 357, 155, 45);
		first_pan.add(porttxt);
		
		//아이피
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

		//채팅이 올라오는 곳
		textArea.setEnabled(false);
		textArea.setEditable(false);
		textArea.setBackground(Color.DARK_GRAY);
		scrollPane.setViewportView(textArea);
		//textArea.setText(inputChat);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(587, 189, 285, 440);
		second_pan.add(scrollPane_1);

		//참가인원 목록
		inwon_jsp.setEnabled(false);
		inwon_jsp.setBackground(Color.GRAY);
		scrollPane_1.setViewportView(inwon_jsp);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 522, 450, 107);
		second_pan.add(scrollPane_2);

		//채팅치는 곳
		scrollPane_2.setViewportView(chatText);
		//chatText.setColumns(10);
		chatText.setText("채팅치는곳");

		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBounds(587, 91, 285, 88);
		second_pan.add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("\uC785\uC7A5\uC778\uC6D0 :");
		label.setFont(new Font("굴림", Font.BOLD, 35));
		label.setForeground(Color.WHITE);
		label.setBounds(12, 10, 176, 68);
		panel.add(label);

		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 30));
		lblNewLabel.setBounds(192, 10, 93, 68);
		panel.add(lblNewLabel);

		//보내기 버튼
		btnNewButton.setFont(new Font("굴림", Font.BOLD, 15));
		btnNewButton.setBounds(474, 522, 97, 54);
		second_pan.add(btnNewButton);

		//나가기
		btnNewButton_1.setBounds(743, 20, 129, 54);
		second_pan.add(btnNewButton_1);

		//파일전송 버튼
		button.setFont(new Font("굴림", Font.BOLD, 15));
		button.setBounds(474, 586, 97, 54);
		second_pan.add(button);
		
		button_1.setBounds(587, 20, 129, 54);
		second_pan.add(button_1);
		
		inwon_vc.add("[ 현재 인원 목록 ]");
		
		this.setTitle("자바는 Fun Fun Fun");
		this.setBounds(100, 100, 900, 700);
		this.setVisible(true);
		
		con.add("client", second_pan);
		
		/// second
		
	}
	
	public void start() {
		chatText.addActionListener(this); // 채팅 치는곳
		
		btnNewButton.addActionListener(this); // 보내기 버튼
		button_1.addActionListener(this); // 메인화면으로 버튼 
		btnNewButton_1.addActionListener(this); // 나가기 버튼
		this.addWindowListener(this);
		////
		startbtn.addActionListener(this); // 메인화면 스타트버튼
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == chatText || e.getSource() == btnNewButton) { // 채팅할 때
			String str = chatText.getText();
			if (str == null || str.trim().length() == 0) // str.trim()=> 앞뒤 공백을 제거한 str
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
			chatText.requestFocus(); // 커서 다시 채팅창으로
			
		}  else if (e.getSource() == startbtn) { // 첫번째 화면 로그인 버튼 and client 화면 실행
			String nickname = nicknametxt.getText();
			String port = "486";
			String ip = "192.168.200.115";
			
			if (nickname == null || nickname.trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "닉네임을 적어주세요", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (porttxt.getText() == null || porttxt.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "포트번호가 비었습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (iptxt.getText() == null || iptxt.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "ip가 비었습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(!porttxt.getText().equals(port)) {
				JOptionPane.showMessageDialog(this, "포트번호가 틀렸습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				iptxt.setText("");
				porttxt.setText("");
				return;
			} else if(!iptxt.getText().equals(ip)) {
				JOptionPane.showMessageDialog(this, "ip가 틀렸습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				iptxt.setText("");
				porttxt.setText("");
				return;
			} else{
				
				try {
					soc = new Socket(iptxt.getText(), Integer.parseInt(porttxt.getText())); // 서버의 포트번호 받아옴

					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())));
					// 아웃스트림
					in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
					// 계속 듣기.. 감시자 붙이기.
					currentThread = new Thread(this);
					currentThread.start();
					out.println(nickname);
					out.flush();
					chatText.requestFocus();
				} catch (IOException ee) {
					System.err.println("접속에러!");
					return;
				}
				
				cl.show(con, "client"); // 채팅 화면 켜기
			}
			
		} else if (e.getSource() == button_1) { // 메인으로 버튼 누를시

			porttxt.setText("486");
			iptxt.setText("192.168.0.30");
			cl.show(con, "main"); // 시작화면 켜기
			out.println("/q");
			out.flush();
			currentThread.interrupt();
			currentThread = null;
			inwon_vc.clear();
			inwon_vc.add("[ 현재 인원 목록 ]");
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
//		view_ta.setForeground(Color.blue);// 글자 색상
//		view_ta.setDisabledTextColor(Color.blue);
		textArea.setText("### 대화방에 입장 하셨습니다. ###\n\n");
		while (true) {
			try {
				String str = in.readLine();
				// if(str == null) beak;
				if (str.charAt(0) == '/') {
					if (str.charAt(1) == 'q') {
						String name = str.substring(2).trim(); // /q 자르고 이름에 저장
						textArea.append("%%% " + name + "님께서 퇴장하셨습니다.%%%\n");
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
						int xx = Integer.parseInt(lblNewLabel.getText()); // 입장인원 써있는 라벨
						lblNewLabel.setText(String.valueOf(--xx));
					} else if (str.charAt(1) == 'p') { // 글앞에 p 가 담겼을 시
						int pos = inwon_li.getSelectedIndex(); // 리스트 크기담은 int pos 만듬
						String user = str.substring(2).trim(); // /p 뺴고 이름만 user 안에 넣음
//						if(inwon_vc.contains(user)) {
//							System.out.println("중복닉넴");
//							nickname_tf.setText("");
//						}
						inwon_vc.add(user); // 유저목록에 방금 유저 이름 넣고
						inwon_li.setListData(inwon_vc); // 갱신된 목록을 리스트에 다시저장
						inwon_li.setSelectedIndex(pos); 
						textArea.append("*** " + user + "님께서 입장하셨습니다.***\n");
						int xx = Integer.parseInt(lblNewLabel.getText().trim());
						lblNewLabel.setText(String.valueOf(++xx)); // 인원 숫자 1명 늘리기
					} else if (str.charAt(1) == 'o') { // 글앞에 o 가 담겼을 시
						String user = str.substring(2).trim();
						inwon_vc.add(user);
						inwon_li.setListData(inwon_vc);
						int xx = Integer.parseInt(lblNewLabel.getText().trim());
						lblNewLabel.setText(String.valueOf(++xx));
					}
				} else { // 일반 채팅시
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

