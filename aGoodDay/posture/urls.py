from django.urls import path
from posture import views
from rest_framework.urlpatterns import format_suffix_patterns

"""
@file   aGoodDay.posture.url
@brief  Maps incoming requests to http://<serverhost>/ posture/.
@author jeje (las9897@gmail.com)
"""
urlpatterns = [
    path('posture/insert/', views.DeviceInsert.as_view()),
    path('posture/<str:device_id>/',  views.DeviceDetail.as_view()),
]

urlpatterns = format_suffix_patterns(urlpatterns)