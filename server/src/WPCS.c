/*
	project : GoodDay socket server
	file : WPCS.c
	author : Park Jung Sun
	email : jspark7373@naver.com
	brief : socket server main
*/
#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include<unistd.h>
#include"../include/server_mod.h"

int main(){
	/* 해당 ip port 로 서버 프로그램 실행*/
	if(connecting_control_board(IP,PORT,BACKLOG) == -1){
		printf("ERROR : connecting_control_board\n");
		exit(1);
	}
	return 0;
}
