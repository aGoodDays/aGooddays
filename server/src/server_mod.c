/*
	project : GoodDay socket server
	file : server_mod.c
	author : Park Jung Sun
	email : jspark7373@naver.com
	brief : socket main function
*/
#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<arpa/inet.h> /*inet_addr htons*/
#include<unistd.h>
#include<time.h>
#include"../include/connectDB.h"
#include"../include/server_mod.h"

int connecting_control_board(char *ip, int port, int backlogs){
	int sd, cd, i;
	int deviceID;
	struct sockaddr_in sin;
	struct sockaddr_in cin;
	char buf[256];
	int rec, clen;
	pid_t pid;
	clen = sizeof(cin);	
	/* 소켓 관리자 생성*/
	sd = socket(AF_INET,SOCK_STREAM,0);
	if(sd == -1){
		printf("SERVER ERROR : socket()\n");
		return -1;
	}
	/* 주소 생성 */
	memset((char*)&sin,'\0',sizeof(sin));
	sin.sin_family = AF_INET;
	sin.sin_port = htons(port);
	sin.sin_addr.s_addr = inet_addr(ip);
	memset(&(sin.sin_zero),0,8);
	/* 소켓 bind */
	if((bind(sd,(struct sockaddr*)&sin,sizeof(struct sockaddr)))==-1){
		printf("SERVER ERROR : bind()\n");
		return -1;
	}
	/* 소켓 listen*/
	if((listen(sd,backlogs))==-1){
		printf("SERVER ERROR : listen()\n");
		return -1;
	}
	/* 서브 프로세스 생성*/
	for(i=0;i<backlogs;i++){
		/* 소켓 accept */
		if((cd = accept(sd, (struct sockaddr *)&cin, &(clen))) == -1){
			printf("SERVER ERROR : accept()\n");
			return -1;
		}
		printf("CONNECTED BY [%s:%d]\n",inet_ntoa(cin.sin_addr),ntohs(cin.sin_port));
		if((pid = fork()) == -1){
			printf("SERVER ERROR : fork()\n");
			return -1;
		}
		/* 자식 프로세스*/
		if(pid == 0){
			/* GET deviceID */
			connectDB();
			printf("[ps:%d][%s:%d]\n", getpid(),inet_ntoa(cin.sin_addr),ntohs(cin.sin_port));
			if(rec = recv(cd,buf,sizeof(buf),0) == -1) return -1;
			buf[11]= '\0'; deviceID = atoi(buf);
			printf("%d\n",deviceID);
			
			/* data select */
			while(rec = recv(cd,buf,sizeof(buf),0)){
				if(rec == -1){
                       		 	printf("SERVER ERROR : recv()\n");
                       			return -1;
                		}
				/* 받은 데이터 db에 저장*/
				insert(buf,deviceID);
				printf("[%s:%d]\n",inet_ntoa(cin.sin_addr),ntohs(cin.sin_port));
				memset(buf,'\0',sizeof(buf));
	        	}
			disconnectDB();
			printf("DISCONNECTED BY [%s:%d]\n",inet_ntoa(cin.sin_addr),ntohs(cin.sin_port));
			exit(1);
		}
		close(cd);
	}
	close(sd);
	return 0;
}
