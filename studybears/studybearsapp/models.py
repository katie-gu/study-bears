from django.db import migrations, models
from django.contrib.auth.models import User
from datetime import datetime
from django.db.models.signals import post_save
from django.dispatch import receiver
from django.urls import reverse

# Create your models here.
class Date_And_Time(models.Model):
    date_time = models.CharField(max_length=100)
    #date_time = models.DateTimeField()

class Classes(models.Model):
    my_classes = models.CharField(max_length=100)
    #date_time = models.DateTimeField()

class Location(models.Model):
    address = models.CharField(max_length = 100)


class StudyGroups(models.Model):
    name = models.CharField(max_length=200, default='')
    course = models.CharField(max_length = 200)
    "capacity = models.IntegerField()"
    study_strategies = models.TextField(max_length=500, default='')
    members = models.ManyToManyField(User)
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
    def get_absolute_url(self):
        """
        Returns the url to access a detail record for this group.
        """
        return reverse('group_detail_page', args=[str(self.id)])

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

    def compare_group_with_user(self, group, course, location):
        #current best group is g_one
        if group.is_open() != True:
            return 0
        if group.course != course:
            return 0
        if group.location != location:
            return 1
        else:
            return 2

    def find_best_group(self, course, location):
    #find the best group for a profile
        best_group = None
        best_num_attributes = 0
        for group in StudyGroups.objects.all():
            num_attributes = self.compare_group_with_user(group, course, location)
            if num_attributes == 2:
                #if returns True, that means the group is better than best_group
                # best_num_attributes = num_attributes
                return group
            elif best_num_attributes < num_attributes:
                best_num_attributes = num_attributes
                best_group = group
                #return early
        if best_group == None:
            print("No groups available currently.")
            return

        return best_group

class Request(models.Model):
    user = models.ForeignKey(Profile, on_delete=models.CASCADE)

class Profile1(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    email_confirmed = models.BooleanField(default=False)
    def __str__(self):
        return self.user.username

@receiver(post_save, sender=User)
def update_user_profile(sender, instance, created, **kwargs):
    if created:
        Profile1.objects.create(user=instance)
    instance.profile1.save()
