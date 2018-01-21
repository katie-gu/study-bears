from django.shortcuts import render
from studybearsapp.models import StudyGroups, Profile, Location, Date_And_Time
from django.contrib.auth.models import User 

# Create your views here.
from django.http import HttpResponse


def index(request):
    return render(request, 'studybearsapp/index.html')

#this request contains info from the front end enough to create an object
def form(request): 
	name_from_request = request.POST.get('name')
	prof_obj = Profile.objects.get(name= name_from_request)

	location_from_request = request.POST.get('location')
	new_location_model = Location.objects.create(address = location_from_request)
	prof_obj.potential_locations.add(new_location_model)

	strategies_from_request = request.POST.get('studystrategies')

	class_from_request = request.POST.get('class')

	time_from_request = request.POST.get('time')
	#time_model = 


	prof_obj.save()

	return render(request,'studybearsapp/form.html')

