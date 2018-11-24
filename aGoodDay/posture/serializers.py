from rest_framework import serializers
from posture.models import Device



"""
@file   aGoodDay.posture.serializers
@brief  Convert query to json, and reverse
@author jeje(las9897@gmail.com)
"""
class DeviceSerializer(serializers.ModelSerializer):
    date = serializers.DateTimeField(format="%Y-%m-%d %H:%M:%S")
    class Meta:
        model = Device
        fields = ('device_id', 'posture', 'saX', 'saY', 'saZ', 'sgX', 'sgY', 'sgZ', 'xdegree', 'ydegree', 'zdegree', 'date')
