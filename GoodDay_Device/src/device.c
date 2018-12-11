/*
	project : GoodDay
	file : device.c
	author : Park Jung Sun
	email : jspark7373@naver.com
	brief : 디바이스에 대한 기본 세팅
*/

#include"/home/pi/GoodDay_control/include/device.h"
#include"/home/pi/GoodDay_control/include/sensing.h"
#include"/home/pi/GoodDay_control/include/posture.h"

/*
	디바이스 전원 ON 시 셋팅
	-GPIO SETTING, LED ON
*/
int Ready_Device(){
	if(wiringPiSetup() == -1) return -1;
	pinMode(READY_LED,OUTPUT);
	digitalWrite(READY_LED,HIGH);
	delay(1000);
	return 0;
}

/*
	자세가 안정된 상태 표시
	-LED OFF
*/
void stable(){
	digitalWrite(READY_LED,0);
}

/*
	자세가 안정된 상태 표시
	-LED ON
*/
void Instable(){
	digitalWrite(READY_LED,HIGH);
}

/*
	OFF 명령을 받을 때, 디바이스 끄기
*/
void Shutdown_Device(){
	digitalWrite(READY_LED,0);
	system("sudo shutdown -h now");
	return;
}

/*
	서버-안드로이드-디바이스간 인증을 위한 ID생성과정
	-ID가 없으면 생성
	-ID가 있으면 기존 ID 사용
*/
int GET_ID(){
	int fd;
	int ID;
	char buf[12];
	if((fd = open("/home/pi/GoodDay_control/setting/deviceID",O_RDWR|O_CREAT|O_EXCL,0644))>0){
		/* ID가 존재하지 않을시 날짜 기반으로 ID 생성 */
		struct tm *t;
		time_t timer = time(NULL);
		t = localtime(&timer);
		ID = (t->tm_year)*10000+(t->tm_mon)*100000000+(t->tm_mday)+(t->tm_hour)+(t->tm_min)+(t->tm_sec);
		sprintf(buf,"%d",ID);
		write(fd,buf,sizeof(buf));
	}else{
		/* ID가 존재시, 기존 ID 사용 */
		if((fd = open("/home/pi/GoodDay_control/setting/deviceID",O_RDONLY,0644))>0){
			read(fd,buf,sizeof(buf));
			ID = atoi(buf);
		}else{
			printf("ID_ERROR\n");
			exit(1);
		}
	}
	close(fd);
	return ID;
}

/*
	디바이스에 접근하는 안드로이드 기기의 정보 LOG
	-날짜 기반
*/
void local_Set(){
	char name[100];
	struct tm *t;
	time_t timer;
	timer = time(NULL);
	t = localtime(&timer);
	sprintf(name, "/home/pi/GoodDay_control/log/LOG_%d_%d_%d__%d_%d_%d", t->tm_year+1900,t->tm_mon+1,t->tm_mday,t->tm_hour, t->tm_min, t->tm_sec );
	fp = fopen(name, "w+");
}

/*
	디바이스 파일 셋팅 종료
*/
void local_logout(){
	fclose(fp);
}
