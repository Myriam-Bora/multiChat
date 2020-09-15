package aa;

import java.io.*;
import java.net.*;
import java.util.*;


class ChatServer extends Thread {
	private ServerSocket ss;
	private Vector vc = new Vector();
	static int portnum = 486; // 포트번호 설정
	// 첫번째 접속자를 소켓에, 다음 벡터에 add() 이런식으로 두번째, 세번째 접속자를 무한 나열
	// Vector 등록되는 모든 데이터를 연속적으로 무한 나열 시킬 수 있다! 같은 위치 덮어쓰기 안됨!

	public ChatServer() {
		try {
			ss = new ServerSocket(portnum); // 서버소켓(포트번호) - 서버프로그램에서만 사용되는 소켓!
			this.start();
			System.out.println("서버를 시작!..클라이언트 접속 대기중...");
		} catch (IOException ee) {
			System.err.println("이미 사용중입니다.");
			System.exit(0);
		}
	}

	public void run() {
		while (true) { // 유저가 처음 입장했을 때
			try {
				Socket imsi = ss.accept(); // 큰 감시자. - 서버 소켓에 클라이언트를 기다리다가 물었다 하면...그냥 소켓생성...
				System.out.println("접속자 : " + imsi.toString());
				UserInfo ui = new UserInfo(imsi);
				for (int i = 0; i < vc.size(); i++) { // 방금들어온 사람만 빼고
					UserInfo uiui = (UserInfo) vc.elementAt(i); // i번째 userinfo 정보 uiui에 담음
					uiui.getOut().println("/p" + ui.getNickName()); // uiui 아웃풋스트림에 /p + 닉네임 담음
					uiui.getOut().flush();
				}
				vc.add(ui); // User 정보 저장.
				for (int i = 0; i < vc.size(); i++) {// 방금 들어온사람 포함해서
					UserInfo uiui = (UserInfo) vc.elementAt(i);
					ui.getOut().println("/o" + uiui.getNickName()); // uiui 아웃풋스티림에 /o + 닉네임 담음
					ui.getOut().flush();
				}
			} catch (IOException ee) {
			}
		}
	}

	// 유저 정보 담는 쓰레드
	class UserInfo extends Thread {
		private Socket soc;
		private String nickname;
		private PrintWriter out;
		private BufferedReader in;

		public UserInfo(Socket s) {
			this.soc = s;
			try {
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())));// 송신(출력) -
																											// 파일,콘솔이 아닌
																											// 네트워크 스트림!
				in = new BufferedReader(new InputStreamReader(soc.getInputStream())); // 수신(입력) - 파일,콘솔이 아닌 네트워크 스트림!
				nickname = in.readLine();
				this.start();// 작은 감시자 시작..
			} catch (IOException ee) {
			}
		}

		public String getNickName() {
			return nickname;
		}

		public PrintWriter getOut() {
			return out;
		}

		public void run() {// 작은 감시자. - 서버와 사용자를 연결
			while (true) {
				try {
					String str = in.readLine(); // 버퍼리더 읽은것 str 안에
					if (str.charAt(0) == '/') {
						if (str.charAt(1) == 'q') {
							for (int i = 0; i < vc.size(); i++) {
								UserInfo ui = (UserInfo) vc.elementAt(i); // ui 안에 i번째 userinfo 넣기
								if (ui.nickname.equals(nickname)) {
									vc.removeElementAt(i);	// 해당유저 목록에서 지우기
									break;
								}
							}
							for (int i = 0; i < vc.size(); i++) {
								UserInfo ui = (UserInfo) vc.elementAt(i);
								ui.out.println("/q" + nickname);
								ui.out.flush();
							}
							break; // 이 사람 Thread 종료...
						} else if (str.charAt(1) == 'w') { // 귓말 보내는 법 =>  /w상대방닉네임- 내용
							String dest = str.substring(2, str.indexOf(" "));
							String msg = str.substring(str.indexOf(" ") + 1);
							for (int i = 0; i < vc.size(); i++) {
								UserInfo ui = (UserInfo) vc.elementAt(i);
								if (ui.nickname.equals(dest)) { // 귓말 대상 닉네임 찾으면
									ui.out.println("from " + nickname + ">> " + msg); // 받는사람 입장
									ui.out.flush();
									out.println("to " + ui.nickname + ">> " + msg);
									// 보내는 사람입장
									out.flush();
									break;
								}
							}
						}
					} else {
						for (int i = 0; i < vc.size(); i++) {
							UserInfo ui = (UserInfo) vc.elementAt(i);
							String data = nickname + " > " + str;
							ui.out.println(data);
							ui.out.flush();
						}
					}
				} catch (IOException ee) {
				}
			}
		}
	}
}

public class ChattingServer {
	public static void main(String[] args) {
		ChatServer cs = new ChatServer();
	}
}
