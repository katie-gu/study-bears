from django.shortcuts import render, redirect
from studybearsapp.models import StudyGroups, Profile, Location, Date_And_Time, Classes
from django.contrib.auth.models import User
from django.views.decorators.csrf import csrf_exempt
from django.template import loader
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.forms import UserCreationForm
from  studybearsapp.forms import SignUpForm
from django.contrib.sites.shortcuts import get_current_site
from django.utils.encoding import force_bytes, force_text
from django.utils.http import urlsafe_base64_encode, urlsafe_base64_decode
from django.template.loader import render_to_string
from studybearsapp.tokens import account_activation_token
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

	best_group = prof_obj.find_best_group(class_from_request, location_from_request)
	if best_group:
		return render(request, 'studybearsapp/post_form.html', {'Name:': best_group.course, 'Time':best_group.date_times.date_time, 'Location': best_group.location.address, 'Capacity': best_group.capacity})
	else:
		return render(request, 'studybearsapp/post_group.html')
@csrf_exempt
def group(request):

	#StudyGroups.objects.create(course=request.POST.get('group_class'), location=request.POST.get('group_location'), size=0, capacity=request.POST.get('group_capacity'))
	return render(request, 'studybearsapp/group.html')
	#add the user to this group

@csrf_exempt
def post_group(request):
	StudyGroups.objects.create(course=request.POST.get('group_class'), location=request.POST.get('group_location'), size=0, capacity=request.POST.get('group_capacity'))
	return render(request, 'studybearsapp/post_group.html')

#Displays a list of groups the user belongs to.
def profile_page(request):
    latest_group_list = StudyGroups.objects.order_by('-course')[:5]
    output = ','.join([group.course for group in latest_group_list])
    context = {'latest_group_list':
                latest_group_list}
    return render(request, 'studybearsapp/profile_page.html', context)

def login_page(request):
    username = request.POST['username']
    password = request.POST['password']
    user = authenticate(request, username=username, password=password)
    if user is not None:
        login(request, user)
        #Redirect to success page.
        return render(request, 'studybearsapp/profile_page.html')
    else:
        #Return an 'invalid login' user message.
        return render(request, 'studybearsapp/invalid_login.html')

def sign_up(request):
    if request.method == 'POST':
        form = SignUpForm(request.POST)
        if form.is_valid():
            user = form.save(commit=False)
            user.is_active = False
            user.save()
            current_site = get_current_site(request)
            subject = 'Activate Your StudyBears Account'
            message = render_to_string('studybearsapp/account_activation_email.html', {
                'user': user,
                'domain': current_site.domain,
                'uid': urlsafe_base64_encode(force_bytes(user.pk)).decode(),
                'token': account_activation_token.make_token(user),
            })
            user.email_user(subject, message)
            return redirect('account_activation_sent')
    else:
        form = SignUpForm()
    return render(request, 'studybearsapp/sign_up.html', {'form': form})

def activate(request, uidb64, token):
    try:
        uid =  force_text(urlsafe_base64_decode(uidb64))
        user = User.objects.get(pk=uid)
    except (TypeError, ValueError, OverflowError, User.DoesNotExist):
        user = None
    if user is not None and account_activation_token.check_token(user, token):
        user.is_active = True
        user.profile1.email_confirmed = True
        user.save()
        login(request, user)
        return redirect('profile_page')

def account_activation_sent(request):
    return render(request, 'studybearsapp/account_activation_sent.html')

 #def logout_view(request):
     #logout(request)
     #Redirect to a success page.
