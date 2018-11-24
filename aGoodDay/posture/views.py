from rest_framework import status
from posture.models import Device
from posture.serializers import DeviceSerializer
from rest_framework import  generics, status
from rest_framework.response import Response

class DeviceDetail(generics.ListAPIView):
    #queryset = Device.objects.all()
    serializer_class = DeviceSerializer

    def get_queryset(self, start_date=None, end_date=None):
        device_id = self.kwargs['device_id']
        start_date = self.request.GET.get('start_date')
        end_date = self.request.GET.get('end_date')
        if (start_date is None or end_date is None):
            return Device.objects.filter(device_id=device_id)[0:2]
        else:
            return Device.objects.filter(device_id=device_id, date__range=[start_date, end_date])

class DeviceInsert(generics.CreateAPIView):
    queryset = Device.objects.all()
    serializer_class = DeviceSerializer

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data, many=isinstance(request.data,list))
        serializer.is_valid(raise_exception=True)
        self.perform_create(serializer)
        headers = self.get_success_headers(serializer.data)
        return Response(serializer.data, status=status.HTTP_201_CREATED, headers=headers)