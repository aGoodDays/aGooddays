from django.db import models


class Device(models.Model):
    device_id = models.CharField(max_length=10)
    posture = models.IntegerField(default=0)
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
        ordering = ('date',)
    
    def __str__(self):
        return self.device_id

class PostureData(models.Model): #collect posture data
    device = models.ForeignKey(Device, on_delete=models.DO_NOTHING)
    count = models.IntegerField(default=0)
    start_date = models.DateTimeField()
    end_date = models.DateTimeField()

    class Meta:
        ordering = ('device',)
