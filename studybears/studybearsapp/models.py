from django.db import models

# Create your models here.
class Date_And_Time(models.Model):
    date_time = models.DateTimeField()

class Location(models.Model):
    address = models.CharField(max_length = 100)
    x_coordinate = models.CharField(max_length = 50)
    y_coordinate = models.CharField(max_length = 50)

class StudyGroups(models.Model):
    course = models.CharField(max_length = 200)
    location = models.CharField(max_length = 200)
    date_times = models.ManyToManyField(Date_And_Time)
    num_members = models.IntegerField()
    study_strategies = models.TextField()

class Request(models.Model):
    user = models.ForeignKey(User)
    course = models.CharField(max_length = 200)

class User(models.Model):
    """ my_requests are the user's requests to join other groups
    """
    name = models.CharField(max_length = 50)
    email = models.EmailField(max_length = 50)
    phone_number = models.IntegerField()
    potential_locations = models.ManyToManyField(Location)
    time_availabilities = models.ManyToManyField(Date_And_Time)
    my_groups = models.ManyToManyField(StudyGroups)
    my_requests = models.ManyToManyField(Request)

class Meeting(models.Model):
    location = models.ForeignKey(Location)
    date_time = models.ManyToManyField(Date_And_Time)
