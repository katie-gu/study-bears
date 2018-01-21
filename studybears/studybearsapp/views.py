from django.shortcuts import render
from studybearsapp.models import StudyGroups, Profile, Location, Date_And_Time, Classes
from django.contrib.auth.models import User 
from django.views.decorators.csrf import csrf_exempt
# Create your views here.
from django.http import HttpResponse

def index(request):
    return render(request, 'studybearsapp/index.html')

@csrf_exempt
def homepage(request):
		#print("Testing: does it get here")
        profile_email = request.POST.get('profile_email')
        profile_name = request.POST.get('profile_name')
        print("Testing homepage profile name:")
        print(profile_name)
        user = User.objects.create_user(profile_name, profile_email, 'x')
        Profile.objects.create(name=profile_name, email=profile_email, phone_number=2, user=user)
        return render(request, 'studybearsapp/homepage.html')

#this request contains info from the front end enough to create an object
@csrf_exempt
def form(request): 
	"""name_from_request = request.POST.get('user_name')
	prof_obj = Profile.objects.get(name= name_from_request)

	location_from_request = request.POST.get('user_location')
	new_location_model = Location.objects.create(address = location_from_request)
	prof_obj.potential_locations.add(new_location_model)

	strategies_from_request = request.POST.get('user_studystrategies')

	class_from_request = request.POST.get('user_class')
	class_model = Date_And_Time.objects.create(my_classes = class_from_request)
	proj_obj.classes.add(class_model)

	time_from_request = request.POST.get('user_time')
	time_model = Date_And_Time.objects.create(date_time = time_from_request)
	proj_obj.time_availabilities.add(time_model)	

	prof_obj.save()"""

	return render(request,'studybearsapp/form.html')

@csrf_exempt
def post_form(request): 
	name_from_request = request.POST.get('user_name')
	prof_obj = Profile.objects.get(name= name_from_request)

	location_from_request = request.POST.get('user_location')
	new_location_model = Location.objects.create(address = location_from_request)
	prof_obj.potential_locations.add(new_location_model)

	strategies_from_request = request.POST.get('user_studystrategies')

	class_from_request = request.POST.get('user_class')
	class_model = Classes.objects.create(my_classes = class_from_request)
	prof_obj.my_classes.add(class_model)

	time_from_request = request.POST.get('user_time')
	time_model = Date_And_Time.objects.create(date_time = time_from_request)
	prof_obj.time_availabilities.add(time_model)	

	prof_obj.save()

	return render(request, 'studybearsapp/post_form.html')

@csrf_exempt
def group(request): 

	#StudyGroups.objects.create(course=request.POST.get('group_class'), location=request.POST.get('group_location'), size=0, capacity=request.POST.get('group_capacity'))
	return render(request, 'studybearsapp/group.html')
	#add the user to this group

@csrf_exempt
def post_group(request): 
	StudyGroups.objects.create(course=request.POST.get('group_class'), location=request.POST.get('group_location'), size=0, capacity=request.POST.get('group_capacity'))
	return render(request, 'studybearsapp/post_group.html')


