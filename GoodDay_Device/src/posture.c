/*
	project : GoodDay
	file : posture.c
	author : Park Jung Sun
	email : jspark7373@naver.com
	brief : 자세 판단 알고리즘
*/

#include"/home/pi/GoodDay_control/include/posture.h"
#include"/home/pi/GoodDay_control/include/sensing.h"
#include"/home/pi/GoodDay_control/include/device.h"

int count = 0;

/*
	사용자 민감도에 따른 민감도 입력
*/
void set_sensitive(int i){
	sensitive = i;
}

/*
	자세 판단 메인
*/
void posture(){
	Posture = 0;
	if(!is_stability()) return;
	is_goodposture();
}

/*
	기울어짐이 있는가?
*/
void is_goodposture(){
	int x,y,z;
	x = fabs(xdegree)/10;
	y = fabs(ydegree)/10;
	z = fabs(zdegree)/10;
	/*사용자 민감도 보다 기울어 졌는가?*/
	if(x > sensitive||y > sensitive || z>sensitive){
		printf("Bad Posture Catch!,%d,%d,%d\n",x,y,z);
		Posture = 2;
		Instable();
	}else{
		stable();
	}
}

/*
	떨림이 있는가?
*/
int is_stability(){
	/*8회 이상 떨림이 있을 시, 떨림으로 판단*/
	if(count > 8){ printf("Trumble is Catch!\n"); Posture = 1; Instable(); }
	if(fabs(sgX) > 50){ count++; return 0;}
	if(fabs(sgY) > 50){ count++; return 0;}
	if(fabs(sgZ) > 50){ count++; return 0;}
	count = 0;
	return 1;
}
