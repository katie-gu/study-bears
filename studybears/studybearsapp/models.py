from django.db import migrations, models
from django.contrib.auth.models import User 
from django.db.models.signals import post_save
from django.dispatch import receiver 

# Create your models here.
class Date_And_Time(models.Model):
    date_time = models.CharField(max_length=100)
    #date_time = models.DateTimeField()

class Classes(models.Model):
    my_classes = models.CharField(max_length=100)
    #date_time = models.DateTimeField()

class Location(models.Model):
    address = models.CharField(max_length = 100)
    #x_coordinate = models.CharField(max_length = 50)
    #y_coordinate = models.CharField(max_length = 50)


class StudyGroups(models.Model):
    course = models.CharField(max_length = 200)
    location = models.CharField(max_length = 200)
    date_times = models.ManyToManyField(Date_And_Time)
    size = models.IntegerField()
    capacity = models.IntegerField()
    "study_strategies = models.TextField()"
    "members = models.OneToManyField('User')"
    "pending_requests = models.ManyToManyField('Request')"
    def is_open(self):
        return self.size < self.capacity
    """def add_member(self, user):
        if self.is_open():
            self.members.add(user)
            user.my_groups.add(self)
            self.size += 1
        else:
           print("Error: Cannot add user because group is full.")"""
    def remove_member(self, user):
        user_name = user.name
        contains_user = self.members.filter(user__name__startswith="user_name")
        if contains_user != None:
            self.members.remove(user)
            user.my_groups.remove(self)
            size -= 1

class Profile(models.Model):
    """ my_requests are the user's requests to join other groups
    """
    name = models.CharField(max_length = 50)
    email = models.EmailField(max_length = 50)
    phone_number = models.IntegerField()
    potential_locations = models.ManyToManyField(Location)
    time_availabilities = models.ManyToManyField(Date_And_Time)
    my_groups = models.ManyToManyField(StudyGroups)
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    my_classes = models.ManyToManyField(Classes)


    

    # @receiver(post_save, sender=User)
    # def create_user_profile(sender, instance, created, **kwargs): 
    #     if created: 
    #         Profile.objects.create(user=instance)

    # @receiver(post_save, sender=User)
    # def save_user_profile(sender, instance, **kwargs): 
    #     instance.profile.save()

    def add_new_member(self, studygroup):
        if studygroup.is_open():
            self.my_groups.add(studygroup)
            studygroup.size = studygroup.size + 1
        else:
            print("Error: Cannot add user because group is full.")

   # "study_group = models.ForeignKey(StudyGroups, on_delete=models.CASCADE)"

class Request(models.Model):
    user = models.ForeignKey(Profile, on_delete=models.CASCADE)
