/*
	project : GoodDay
	file : sending.c
	author : Park Jung Sun
	email : jspark7373@naver.com
	brief : 자세판단(떨림, 기울임), 측정 값들을 서버에 전송.
*/

#include"/home/pi/GoodDay_control/include/sending.h"
#include"/home/pi/GoodDay_control/include/sensing.h"
#include"/home/pi/GoodDay_control/include/posture.h"
#include"/home/pi/GoodDay_control/include/bt_send.h"

/*
	서버와 소켓 통신 연결
*/
int Server_connect(){
	/* SUCCESS 0,FAIL -1 */
	struct sockaddr_in cin;

	client = socket(AF_INET,SOCK_STREAM,0);
	if(client == -1) return -1;

	/* sockaddr_in setting */
	memset((char *)&cin,'\0',sizeof(cin));
	cin.sin_family = AF_INET;
	cin.sin_port = htons(port);
	cin.sin_addr.s_addr = inet_addr(ip);
	memset(&(cin.sin_zero),0,8);

	/* connect to server */
	if(connect(client,(struct sockaddr *)&cin,sizeof(struct sockaddr)) == -1){
		return -1;
	}

	return 0;
}

/*
	매겨변수로 받은 BUF 값 서버로 전송
	-id 값 전송 및 Server_logging 과정에서 이용
*/
int Server_sending(char* buf,int size){ // SUCCESS 0, FAIL -1
	int sen;
	char data[256];
	if(sizeof(buf)==0) return -1;
	strncpy(data,buf,size);
	sen = send(client,data,size,0);
	if(sen == -1) return -1;
	return 0;
}

/*
	서버로 보낼 데이터 기록
*/
void Server_logging(){
	char buf[256];
	int i;
	sprintf(buf,"%d,%f,%f,%f,%f,%f,%f,%f,%f,%f\n\n",Posture,saX,saY,saZ,sgX,sgY,sgZ,xdegree,ydegree,zdegree);
	i = strlen(buf);
	buf[i] = '\0';
	Server_sending(buf,strlen(buf));
}

/*
	서버 연결 종료
*/
void Server_disconnect(){
	close(client);
}
