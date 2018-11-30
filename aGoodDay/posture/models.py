from django.db import models


"""
@file   aGoodDay.posture.models
@brief  Data class used to collect or display data 
        The primary key is omitted, but exists as an id.
        The Device is the data, the PostureData model is the processed data
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
        return self.device_id

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
