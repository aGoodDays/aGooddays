/*
	project : GoodDay
	file : app.c
	author : Park Jung Sun
	email : jspark7373@naver.com
	brief : 메인 프로그램
*/

#include"/home/pi/GoodDay_control/include/sensing.h"
#include"/home/pi/GoodDay_control/include/sending.h"
#include"/home/pi/GoodDay_control/include/device.h"
#include"/home/pi/GoodDay_control/include/posture.h"
#include"/home/pi/GoodDay_control/include/bt_send.h"
#include<sys/types.h>
#include<sys/shm.h>

int *cal_num; // 제어를 위한 공유메모리 주소
int *pos;     // 자세 알람을 위한 공유메모리주소

int recv_control(); // 제어를 위한 서브 프로세스
int send_alarm();   // 실시간 알람을 위한 서브 프로세스

/*
	메인 프로그램
	-디바이스 셋팅
	-안드로이드 연결
	-서브 프로세스 생성
	-핵심기능
*/
int main(){
	int sens; // 사용자 민감도 설정
	int mode; // 사용자 모드 설정
	char id[20]; // id

	/* 디바이스 셋팅 */
	if(Ready_Device() == -1){
		printf("DEVICE IS NOT READY\n");
	}
	sprintf(id,"ID%d\r",GET_ID()); // ID 불러오기

	/* 안드로이드 접근 로그 생성 */
	local_Set();

	/* bluetooth 연결 */
	conn_bluetooth();
	bt_sending(id,sizeof(id));	// 안드로이드에 ID 전송
	while((mode = bt_recving()) < 0 ); // recv mode 1 -> 서버 | mode 2 -> 비서버
	while((sens = bt_recving()) < 0 ); // recv 민감도 1 -> 10 // 2 -> 20 // 3 -> 30

	/* 안드로이드 로그 확인 */
	local_logout();

	/* 민감도를 이용하여 디바이스 셋팅 */
	sensingSet(sens);
	printf("compelete setting %d\n",sensitive);

	/* 제어 프로세스, 실시간 알람 프로세스 생성*/
	recv_control();
	send_alarm();

	/* mode가 서버 연동 모드일 때, 서버 연결 */
	if(mode == 1){
		if(Server_connect() == -1){
			printf("CONNECT_ERROR\n");
		}else{
			sprintf(id,"%d",GET_ID());
  		Server_sending(id,sizeof(id)); // 서버에 ID 전송
		}
	}

	/* 핵심 기능 */
	while(1){
		/*디바이스 종료 신호 시 끝.*/
		if(*cal_num == 0){
			printf("POWER OFF\n");
			break;
		}

		/*디바이스 멈춤 신호 Block.*/
		if(*cal_num == 2) printf("STOP\n");
		while(*cal_num == 2){
			if(*cal_num == 1) break;
		}

		/*센서값 읽기, 각도 계산, 자세판단*/
		sampling();
		degree();
		posture();

		/*서버 모드시 서버에 로그 전송*/
		if(mode == 1) Server_logging();

		/*alarm 프로세스를 위한, 신호*/
		*pos = Posture;
		delay(100);
	}

	/*종료 옵션*/
	*pos = -1;
	Server_disconnect();
	disconn_bluetooth();
	Shutdown_Device();
	return 0;
}



int recv_control(){
		int shmid;
		int pid;
		void *shared_memory =(void*)0;

		/*제어를 위한 공유메모리 생성*/
		shmid = shmget((key_t)1234,sizeof(int),IPC_CREAT | 0666);
		if(shmid == -1){
			printf("shmid1\n");
			exit(0);
		}
		shared_memory = shmat(shmid,(void*)0,0);
		if(shared_memory == (void *)-1){
			printf("shmid2\n");
			exit(0);
		}
		cal_num = (int *)shared_memory;
		*cal_num = 1;


		/*제어를 위한 서브 프로세스 생성*/
		if((pid = fork()) == -1){
			printf("fork error\n");
			return -1;
		}
		if(pid == 0){
			char command[256];
			int read_Byte;
			int result;

			/*제어를 위한 공유메모리 생성*/
			shmid = shmget((key_t)1234,sizeof(int),IPC_CREAT | 0666);
			if(shmid == -1){
				printf("shmid1\n");
				exit(0);
			}
			shared_memory = shmat(shmid,(void*)0,0);
			if(shared_memory == (void *)-1){
				printf("shmid2\n");
				exit(0);
			}
			cal_num = (int *)shared_memory;

			/*안드로이드 블루투스 통신을 이용 제어신호 받기 */
			while(1){
				memset(command,0,sizeof(command));
				read_Byte = read(client,command,sizeof(command));
				switch(command[0]){
					case '0': *cal_num = 0;	exit(0); break;
					case '1': *cal_num = 1;	break;
					case '2': *cal_num = 2;	break;
					default : break;
				}
			}
		}
}


int send_alarm(){
		int shmid;
		int pid;
		void *shared_memory =(void*)0;

		/*알람을 위한 공유메모리 생성*/
		shmid = shmget((key_t)5678,sizeof(int),IPC_CREAT | 0666);
		if(shmid == -1){
			printf("shmid1\n");
			exit(0);
		}
		shared_memory = shmat(shmid,(void*)0,0);
		if(shared_memory == (void *)-1){
			printf("shmid2\n");
			exit(0);
		}
		pos = (int *)shared_memory;
		*pos = 0;

		/*알람을 위한 서브 프로세스 생성*/
		if((pid = fork()) == -1){
			printf("fork error\n");
			return -1;
		}
		if(pid == 0){
			/*알람을 위한 공유메모리 생성*/
			shmid = shmget((key_t)5678,sizeof(int),IPC_CREAT | 0666);
			if(shmid == -1){
				printf("shmid1\n");
				exit(0);
			}
			shared_memory = shmat(shmid,(void*)0,0);
			if(shared_memory == (void *)-1){
				printf("shmid2\n");
				exit(0);
			}
			pos = (int *)shared_memory;

			/*알람 신호 안드로이드로 전송*/
			char command[32] = {"bad posture catch!\r"};
			while(1){
				if (*pos != 0) {
					if((write(client,command,sizeof(command)))< 0){
						printf("write error\n");
						return -1;
					}
					if(*pos == -1){
						exit(0);
					}
				}
			}
		}
}
