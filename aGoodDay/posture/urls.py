from django.urls import path
from posture import views


urlpatterns = [
    path('posture/<str:device_id>/', views.device_connect_data),
    
    #path('posture/<int:pk>/', views.snippet    _detail),
]
