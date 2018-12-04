from rest_framework import status
from posture.models import Device, Posture
from posture.serializers import DeviceSerializer, PostureSerializer
from rest_framework import  generics, status
from rest_framework.response import Response

from datetime import date, datetime, timedelta



"""
@file aGoodDay.posture.views
@brief views.py는 클라이언트의 요청을 처리하고, 그 결과를 사용자에게 보여줄 수 있도록 Templete에게 전달하는 역할을 한다. 보통 MVC 패턴의 Controller의 역할을 한다고 생각하면 편하다.
@author jeje(las9897@gmail.com)
"""

"""
@file .DeviceDetail
@brief ID가 device_id인 클라이언트가 Device 테이블의 데이터를 요청할 때 호출되는 클래스. Request 객체에 숫자형 date가 포함 되어있으면 오늘을 기준으로 date 일 전까지의 데이터를 반환한다. 또는 Request객체에 문자열 start_date, end_date가 있으면 이를 DATE 타입으로 변환 후, 그 사이에 있는 날짜에 해당하는 데이터를 반환한다.
"""
class DeviceDetail(generics.ListAPIView):
    serializer_class = DeviceSerializer

    def get_queryset(self):
        add_day = None
        start_date = None
        end_date = None
        if 'date' in self.request.GET:
            add_day = self.request.GET['date']
  
        if 'start_date' in self.request.GET and 'end_date' in self.request.GET:
            start_date = self.request.GET['start_date']
            end_date = self.request.GET['end_date']
    
        device_id = self.kwargs['device_id']
        #today = self.request.GET.get('today')
        today = date.today() + timedelta(days=1)
        
        if add_day is not None:
            pre_day = today - timedelta(days=int(add_day))
            query = Device.objects.filter(device_id=device_id, date__range=[pre_day, today])
        elif start_date is not None and end_date is not None:
            start_date = datetime.strptime(start_date, '%Y-%m-%d').date()
            end_date = datetime.strptime(end_date, '%Y-%m-%d').date()
            end_date = end_date + timedelta(days=1)
            query = Device.objects.filter(device_id=device_id, date__range=[start_date, end_date])
        return query

"""
@file .PostureDetail
@brief ID가 device_id인 클라이언트가 가공된 데이터를 요청할 때 사용된다.
"""

class PostureDetail(generics.ListCreateAPIView):
    serializer_class = PostureSerializer
    def get_queryset(self):
        device_id = self.kwargs['device_id']
        return Posture.objects.filter(device_id=device_id)
    
"""
@file .PostureUpdate
@brief Device 테이블의 데이터가 추가되어서, 가공 결과가 바뀌었을 때 호출되어 기존 Posture 테이블을 업데이트한다.
"""
class PostureUpdate(generics.RetrieveUpdateAPIView):
    queryset = Posture.objects.all()
    serializer_class = PostureSerializer
    lookup_field = 'device_id'

    def get_queryset(self):
        device_id = self.kwargs['device_id']
        date = self.request.GET['date']
        return Posture.objects.filter(device_id=device_id, date=date)
