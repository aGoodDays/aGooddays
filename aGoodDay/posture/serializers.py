from rest_framework import serializers
from posture.models import Device, Posture



"""
@file   aGoodDay.posture.serializers
@brief  서버, 데이터베이스, 안드로이드 등 다양한 환경에서 데이터를 주고 받으려면 동일한 데이터 구조를 가져야하는데, Serializer는 동일한 데이터 구조로 만들어주는 역할을 한다.
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
        fields = ('bad_count', 'all_count', 'ratio')
