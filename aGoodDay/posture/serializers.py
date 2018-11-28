from rest_framework import serializers
from posture.models import Device, Posture



"""
@file   aGoodDay.posture.serializers
@brief  Convert query to json, and reverse
@author jeje(las9897@gmail.com)
"""
class DeviceSerializer(serializers.ModelSerializer):
    date = serializers.DateTimeField(format="%Y-%m-%d")
    class Meta:
        model = Device
        fields = ('device_id', 'posture', 'saX', 'saY', 'saZ', 'sgX', 'sgY', 'sgZ', 'xdegree', 'ydegree', 'zdegree', 'date')


class PostureSerializer(serializers.ModelSerializer):
    class Meta:
        model = Posture
        fields = '__all__'#('device_id', 'bad_count', 'all_count', 'ratio')
