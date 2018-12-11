#
#	project : GoodDay
#	file : Makefile
#	author : Park Jung Sun
#	email : jspark7373@naver.com
#	brief : Makefile
#

CC = gcc
LIB = -lm -lwiringPi -lbluetooth
IDIR = include
SDIR = src
ODIR = obj

GoodDays: $(SDIR)/*.c
	$(CC) -o $@ $^ $(LIB)
