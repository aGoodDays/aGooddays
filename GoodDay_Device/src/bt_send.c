/*
	project : GoodDay
	file : bt_send.c
	author : Park Jung Sun
	email : jspark7373@naver.com
	brief : 안드로이드와 블루투스 통신 셋팅
*/

#include"/home/pi/GoodDay_control/include/bt_send.h"
#include"/home/pi/GoodDay_control/include/device.h"

char buf[1024] = { 0 };

/*
	블루투스 안드로이드 연결
*/
void conn_bluetooth(){
	struct sockaddr_rc loc_addr = { 0 }, device = { 0 };
	unsigned int opt = sizeof(device);

	server = socket(AF_BLUETOOTH, SOCK_STREAM, BTPROTO_RFCOMM);

	/* socket_addr setting */
	loc_addr.rc_family = AF_BLUETOOTH;
	loc_addr.rc_bdaddr = *BDADDR_ANY;
	loc_addr.rc_channel = (uint8_t) 1;

	/* bind, listen */
	bind(server,(struct sockaddr *)&loc_addr,sizeof(loc_addr));
	listen(server,1);
	client = accept(server,(struct sockaddr *)&device, &opt);

	/* 안드로이드 기기 인식 */
	ba2str(&device.rc_bdaddr, buf);
	printf("accept connection from %s\n",buf);
	fprintf(fp,"%s",buf);
	memset(buf,0,sizeof(buf));
}

/*
	블루투스 안드로이드 연결 종료
*/
void disconn_bluetooth(){
	close(client);
	close(server);
}

/*
	디바이스에서 안드로이드로 데이터 전송
	-id값 전송 및 알람 신호용
*/
int bt_sending(char* data,int size){
	memset(buf,0,sizeof(buf));
	strncpy(buf,data,size);
	if((write(client,buf,sizeof(buf)))< 0){
		printf("ERROR : BT_SENDING\n");
		return -1;
	}
	return 0;
}

/*
	디바이스 셋팅 값 받아오기
	setting 값 받아오기
*/
int bt_recving(){
	memset(buf,0,sizeof(buf));
	read(client,buf,sizeof(buf));
	switch(buf[0]){
		case '1': return 1;
		case '2': return 2;
		case '3': return 3;
		default : return -1;
	}
}
