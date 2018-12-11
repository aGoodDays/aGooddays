/*
	project : GoodDay
	file : sensing.c
	author : Park Jung Sun
	email : jspark7373@naver.com
	brief : Raspberry Pi에서 MPU-6050 Sensor로 부터,
					I2C 통신을 이용하여 자이로-가속도 값을 읽어 데이터를 가공
*/

#include"/home/pi/GoodDay_control/include/sensing.h"
#include"/home/pi/GoodDay_control/include/posture.h"
#include"/home/pi/GoodDay_control/include/device.h"

/*
	MPU-6050 데이터 레지스터로 불러옴.
	-MPU-6050과 연결된 주소로부터 자이로-가속도 16bit의 Raw Data를 레지스터에 읽어들임.
*/
int read_word_2c(int addr){
	int val;
	val = wiringPiI2CReadReg8(fd,addr);
	val = val << 8;
	val += wiringPiI2CReadReg8(fd,addr+1);
	if(val >= 0x8000) val = -(65536 -val);
	return val;
}

/*
	센서를 이용하기 전 세팅
	-MPU-6050과 I2C 통신을 하기위한 주소 및 기본 셋팅.
	-사용자 기본 자세 셋팅 및 민감도 설정.
*/
void sensingSet(int i){
	fd = wiringPiI2CSetup(0x68);
	wiringPiI2CWriteReg8(fd,0x6B,0x00);
	delay(20000); // 센서 안정화
	standard_posture();
	set_sensitive(i);
	stable();
	return ;
}

/*
  레지스터에서 Raw Data를 읽어, Scale Vector 연산을 통해 사용가능한 값으로 가공
	-가속도 값은 0.xx ~ 1.xxg 값을, 자이로 값은 0.xx ~ 250.xx 값을 갖는다.
*/
void sensing(){
		accX = read_word_2c(0x3B);
		accY = read_word_2c(0x3D);
		accZ = read_word_2c(0x3F);
		gyX = read_word_2c(0x43);
		gyY = read_word_2c(0x45);
		gyZ = read_word_2c(0x47);
		accX_scaled = accX / 16384.0;
		accY_scaled = accY / 16384.0;
		accZ_scaled = accZ / 16384.0;
		gyX_scaled = gyX / 131.0;
		gyY_scaled = gyY / 131.0;
		gyZ_scaled = gyZ / 131.0;
}

/*
	센서 쓰레기값 제거 과정
	-Max Pooling 방식으로 50개의 데이터 중 상위 값 선택.
	-50번의 데이터 값 측정
*/
void sampling(){
		saX = 0.0; saY =0.0; saZ =0.0; sgX =0.0; sgY =0.0; sgZ =0.0;
		for(int i=0;i<50;i++){
			sensing();
			if(fabs(saX) < fabs(accX_scaled)) saX = accX_scaled;
			if(fabs(saY) < fabs(accY_scaled)) saY = accY_scaled;
			if(fabs(saZ) < fabs(accZ_scaled)) saZ = accZ_scaled;
			if(fabs(sgX) < fabs(gyX_scaled)) sgX = gyX_scaled;
			if(fabs(sgY) < fabs(gyY_scaled)) sgY = gyY_scaled;
			if(fabs(sgZ) < fabs(gyZ_scaled)) sgZ = gyZ_scaled;
		}
}

/*
	가속도 값을 이용해서 사용자 각도 계산.
*/
void degree(){
		R = sqrt(pow(saX,2)+pow(saY,2)+pow(saZ,2));
		xdegree = (acos(saX/R)*(180.0/M_PI)) - standx;
		ydegree = (acos(saY/R)*(180.0/M_PI)) - standy;
		zdegree = (acos(saZ/R)*(180.0/M_PI)) - standz;
}

/*
	사용자 처음 자세 측정. 샘플링된 안정화 된 자세 입력.
*/
void standard_posture(){
	sampling();
	degree();
	standx = xdegree;
	standy = ydegree;
	standz = zdegree;
}
