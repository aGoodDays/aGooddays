/*
	project : GoodDay socket server
	file : connectDB.c 
	author : Park Jung Sun
	email : jspark7373@naver.com
	brief : connect DB and store data
*/
#include"../include/connectDB.h"
/*db 연결*/
void connectDB(){
	mysql_init(NULL);
	if(!mysql_real_connect(&conn,"localhost","root","sun","agoodday",3306,(char*)NULL,0)){
		fprintf(stderr,"%s\n",mysql_error(&conn));
		exit(1);
	}
}
/*
	받은 데이터를 테이블에 맞게 가공후에  시간 날짜에 맞추어 DB에 저장한다.
*/
void insert(char* buf,int id){
	char* ptr;
	char insert[256];
	double data[10];
	ptr = strtok(buf,",");
	for(int z=0;ptr!=NULL;z++){
        	data[z] = atof(ptr);
        	ptr = strtok(NULL,",");
	}
	time_t timer = time(NULL);
	t = localtime(&timer);
	sprintf(insert,"INSERT INTO posture_device(device_id, posture, saX, saY, saZ, sgX, sgY, sgZ, xdegree, ydegree, zdegree, date)  VALUES (%d,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,'%d-%d-%d %d:%d:%d')",id,data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],t->tm_year+1900,t->tm_mon+1,t->tm_mday,t->tm_hour,t->tm_min,t->tm_sec);
	mysql_query(&conn,insert);
}
/*db 연결 해제*/
void disconnectDB(){ mysql_close(&conn);}
