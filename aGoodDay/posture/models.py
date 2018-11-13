from django.db import models
from pygments.lexers import get_all_lexers
from pygments.styles import get_all_styles

LEXERS = [ item for item in get_all_lexers() if item[1]]
LANGUAGE_CHOICES = sorted([(item[1][0], item[0]) for item in LEXERS])
STYLE_CHOICES = sorted((item, item) for item in get_all_styles())


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
    date = models.DateField(auto_now_add=True)

    class Meta:
        ordering = ('date',)
    
    def __str__(self):
        return self.device_id

class PostureData(models.Model): #collect posture data
    device = models.ForeignKey(Device, on_delete=models.DO_NOTHING)
    count = models.IntegerField(default=0)
    start_date = models.DateField()
    end_date = models.DateField()

    class Meta:
        ordering = ('device',)

class Snippet(models.Model):
    created = models.DateTimeField(auto_now_add=True)
    title = models.CharField(max_length=100, blank=True, default='')
    code = models.TextField()
    linenos = models.BooleanField(default=False)
    language = models.CharField(choices=LANGUAGE_CHOICES, default='python', max_length=100)
    style = models.CharField(choices=STYLE_CHOICES, default='friendly', max_length=100)

    class Meta:
        ordering = ('created',)

