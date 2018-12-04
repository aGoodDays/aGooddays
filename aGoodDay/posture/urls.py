from django.urls import path
from posture import views
from rest_framework.urlpatterns import format_suffix_patterns

"""
@file   aGoodDay.posture.url
@brief  우리는 HTTP 통신을 이용하기 때문에, 들어오는 요청에 해당하는 응답을 해야한다. urls.py는 요청에 맞도록 매핑을 해주는 역할을 한다.
@author jeje (las9897@gmail.com)
"""
urlpatterns = [
    path('device/insert/', views.DeviceDetail.as_view()),
    path('device/<int:device_id>/',  views.DeviceDetail.as_view()),
    path('posture/<int:device_id>/', views.PostureDetail.as_view()),
    path('posture/<int:device_id>/update/', views.PostureUpdate.as_view()),
]

urlpatterns = format_suffix_patterns(urlpatterns)