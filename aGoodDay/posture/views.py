from django.shortcuts import render
#api_view, Response
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status
from posture.models import Snippet, Device
from posture.serializers import SnippetSerializer, DeviceSerializer
from django.http import Http404
from rest_framework.views import APIView
from rest_framework import mixins, generics

import logging
logger = logging.getLogger(__name__)

class DeviceDetail(generics.ListAPIView):
    #queryset = Device.objects.all()
    serializer_class = DeviceSerializer

    def get_queryset(self, start_date=None, end_date=None):
        device_id = self.kwargs['device_id']
        start_date = self.request.GET.get('start_date')
        end_date = self.request.GET.get('end_date')
        if (start_date is None or end_date is None):
            return Device.objects.filter(device_id=device_id)
        else:
            return Device.objects.filter(device_id=device_id, date__range=[start_date, end_date])

            

class SnippetList(generics.ListCreateAPIView):

    queryset = Snippet.objects.all()
    serializer_class = SnippetSerializer
'''
    def get(self, request, *args, **kwargs):
        return self.list(request, *args, **kwargs)

    def post(self, request, *args, **kwargs):
        return self.create(request, *args, **kwargs)
'''

class SnippetDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Snippet.objects.all()
    serializer_class = SnippetSerializer

'''
    def get(self, request, *args, **kwargs):
        return self.retrieve(requst, *args, **kwargs)
    def put(self, request, *args, **kwargs):
        return self.update(request, *args, **kwargs)
    def delete(self, reqeust, *args, **kwargs):
        return self.destroy(request, *args, **kwargs)
        '''

'''
class DeviceConnectData(APIView):
    def get_object(self, id):
        try:
            return Device.objects.filter(device_id = id)
        except Device.DoesNotExist:
            return Http404
    
    def get(self, request, id, format=None):
        device = self.get_object(id)
        serializer = DeviceSerializer(device, many=True)
        return Response(serializer.data)

    def post(self, request, id, format=None):
        serializer = DeviceSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

'''

'''
@api_view(['GET', 'POST'])
def device_connect_data(request, device_id, format=None):
    if request.method == 'GET':
        device = Device.objects.filter(device_id=device_id)
        serializer = DeviceSerializer(device, many=True)
        return Response(serializer.data)
    elif request.method=='POST':
        serializer = DeviceSerializer(data=request)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

'''
'''
@api_view(['GET', 'POST'])
def snippet_list(request, format=None):
    """
    List all code snippets, or create a new snippet.
    """
    if request.method == 'GET':
        snippets = Snippet.objects.all()
        serializer = SnippetSerializer(snippets, many=True)
        return Response(serializer.data, status=status.HTTP_201_CREATED)

    elif request.method == 'POST':
        serializer = SnippetSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['GET','PUT','DELETE'])
def snippet_detail(request, pk, format=None):
    """
    Retrieve, update or delete a code snippet.
    """
    try:
        snippet = Snippet.objects.get(pk=pk)
    except Snippet.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = SnippetSerializer(snippet)
        return Response(serializer.data)

    elif request.method == 'PUT':

        serializer = SnippetSerializer(snippet, data=data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
'''
'''
@csrf_exempt
def snippet_list(request):
    """
    List all code snippets, or create a new snippet.
    """
    if request.method == 'GET':
        snippets = Snippet.objects.all()
        serializer = SnippetSerializer(snippets, many=True)
        return JsonResponse(serializer.data, safe=False)

    elif request.method == 'POST':
        data = JSONParser().parse(request)
        serializer = SnippetSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        return JsonResponse(serializer.errors, status=400)

@csrf_exempt
def snippet_detail(request, pk):
    """
    Retrieve, update or delete a code snippet.
    """
    try:
        snippet = Snippet.objects.get(pk=pk)
    except Snippet.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'GET':
        serializer = SnippetSerializer(snippet)
        return JsonResponse(serializer.data)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = SnippetSerializer(snippet, data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data)
        return JsonResponse(serializer.errors, status=400)

    elif request.method == 'DELETE':
        snippet.delete()
        return HttpResponse(status=204)
'''