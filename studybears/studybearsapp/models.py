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
    size = models.IntegerField()
    capacity = models.IntegerField()
    study_strategies = models.TextField()
    members = models.ManyToManyField('User')
    "pending_requests = models.ManyToManyField('Request')"
    def is_open(self):
        return self.size <= self.capacity
    def add_member(self, user):
        if is_open(self):
            self.members.add(user)
            user.my_groups.add(self)
            self.size += 1
    def remove_member(self, user):
        user_name = user.name
        contains_user = self.members.filter(user__name__startswith="user_name")
        if contains_user != None:
            self.members.remove(user)
            user.my_groups.remove(self)
            size -= 1

class User(models.Model):
    """ my_requests are the user's requests to join other groups
    """
    name = models.CharField(max_length = 50)
    email = models.EmailField(max_length = 50)
    phone_number = models.IntegerField()
    potential_locations = models.ManyToManyField(Location)
    time_availabilities = models.ManyToManyField(Date_And_Time)
    my_groups = models.ManyToManyField(StudyGroups)

class Request(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)

class Meeting(models.Model):
    location = models.ForeignKey(Location, on_delete=models.CASCADE)
    date_time = models.ManyToManyField(Date_And_Time)
