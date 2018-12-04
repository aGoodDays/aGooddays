from django.db import models


"""
@file   aGoodDay.posture.models
@brief  데이터를 수집하거나 표시하는데 사용되는 데이터 클래스
        기본 키는 생략되었으며, 테이블 상에서 ID로 존재합니다.
        Posture 테이블은 처리된 데이터를 보관하기 위해서 만들었습니다.
@author jeje(las9897@gmail.com)
"""

class Device(models.Model):
    device_id = models.IntegerField()
    posture = models.FloatField(default=0)
    saX = models.FloatField(default=0)
    saY = models.FloatField(default=0)
    saZ = models.FloatField(default=0)
    sgX = models.FloatField(default=0)
    sgY = models.FloatField(default=0)
    sgZ = models.FloatField(default=0)
    xdegree = models.FloatField(default=0)
    ydegree = models.FloatField(default=0)
    zdegree = models.FloatField(default=0)
    date = models.DateTimeField(auto_now_add=True)

    class Meta:
        ordering = ('-date',)
    
    def __str__(self):
        return str(self.device_id)

class Posture(models.Model):
    device_id = models.CharField(max_length=10)
    date = models.CharField(max_length=10)
    bad_count = models.IntegerField(default=0)
    all_count = models.IntegerField(default=0)
    ratio = models.FloatField(default=0)

    class Meta:
        ordering = ('-date',)

    def __str__(self):
        return self.device_id
