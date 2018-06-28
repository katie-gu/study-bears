import os
import httplib2
from django.shortcuts import render, redirect
from studybearsapp.models import StudyGroups, Profile, Location, Date_And_Time, Classes
from django.contrib.auth.models import User
from django.views.decorators.csrf import csrf_exempt
from django.template import loader
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.forms import UserCreationForm
from studybearsapp.forms import SignUpForm, CreateGroupForm, CreateMeetingForm, AddMemberForm
from django.contrib.sites.shortcuts import get_current_site
from django.utils.encoding import force_bytes, force_text
from django.utils.http import urlsafe_base64_encode, urlsafe_base64_decode
from django.template.loader import render_to_string
from studybearsapp.tokens import account_activation_token
from django.shortcuts import get_object_or_404
# Create your views here.
from django.http import HttpResponse, HttpResponseRedirect
from django.contrib.auth.decorators import login_required
import google.oauth2.credentials
import google_auth_oauthlib.flow
import googleapiclient.discovery
from django.contrib.auth.decorators import login_required
from django.urls import reverse
CLIENT_SECRETS_FILE = os.path.join(os.path.dirname(__file__), "client_secret_661856853782-etoetkk761euisd0gcdk706sbnjvl84n.apps.googleusercontent.com.json")
SCOPES = ['https://www.googleapis.com/auth/calendar']
API_SERVICE_NAME = 'calendar'
API_VERSION = 'v3'

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
#@login_required
def profile_page(request, username):
    user = User.objects.get(username = username)
    return render(request, 'studybearsapp/profile_page.html', {'user': user})

@csrf_exempt
def login_page(request):
    username = request.POST['username']
    password = request.POST['password']
    user = authenticate(request, username=username, password=password)
    if user is not None:
        login(request, user)
        #Redirect to success page.
        username = User.objects.get(username__exact=request.user)
        url = reverse('profile_page', kwargs={'username': username.username})
        return HttpResponseRedirect(url)
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

def create_group(request):
    if request.method == 'POST':
        form = CreateGroupForm(request.POST)
        if form.is_valid():
            group_name = form.cleaned_data['group_name']
            course = form.cleaned_data['course']
            capacity = form.cleaned_data['capacity']
            study_strategies = form.cleaned_data['study_strategies']
            StudyGroups.objects.create(name=group_name, course=course,
                                        capacity=capacity, study_strategies=study_strategies)
            new_group = StudyGroups.objects.get(name__exact=group_name, course__exact=course,
                                        capacity__exact=capacity, study_strategies__exact=study_strategies)
            return redirect(new_group)
    else:
        form = CreateGroupForm()
    return render(request, 'studybearsapp/create_group.html', {'form': form})

def authorize(request, pk):
    group_id = get_object_or_404(StudyGroups, pk=pk)
    group_url = group_id.get_absolute_url()
    redirect_url = 'http://127.0.0.1:8000/studybearsapp/' + group_url + '/create_meeting/oauth2callback'
    flow = google_auth_oauthlib.flow.Flow.from_client_secrets_file(
                CLIENT_SECRETS_FILE,
                scopes=['https://www.googleapis.com/auth/calendar'],
                redirect_uri=redirect_url,
                )
    authorization_url, state = flow.authorization_url(access_type='offline', include_granted_scopes='true')
    request.session['state'] = state
    return redirect(authorization_url)

def oauth2callback(request, pk):
    group_id = get_object_or_404(StudyGroups, pk=pk)
    group_url = group_id.get_absolute_url()
    redirect_url = group_url + '/create_meeting/post'
    state = request.session['state']
    flow = google_auth_oauthlib.flow.Flow.from_client_secrets_file(
                CLIENT_SECRETS_FILE,
                scopes=['https://www.googleapis.com/auth/calendar'],
                redirect_uri='http://127.0.0.1:8000/studybearsapp/create_meeting/oauth2callback',
                )
    flow.fetch_token(code=request.GET['code'],
                    authorization_response='http://127.0.0.1:8000/studybearsapp/create_group/oauth2callback',
                    auth=False
                    )
    credentials = flow.credentials
    request.session['credentials'] = credentials_to_dict(credentials)
    return redirect(redirect_url)

def credentials_to_dict(credentials):
  return {'token': credentials.token,
          'refresh_token': credentials.refresh_token,
          'token_uri': credentials.token_uri,
          'client_id': credentials.client_id,
          'client_secret': credentials.client_secret,
          'scopes': credentials.scopes}

def groups(request):
    study_groups = StudyGroups.objects.all()
    return render(request, 'studybearsapp/groups.html', {'study_groups': study_groups})

def group_detail_page(request, pk):
     group_id = get_object_or_404(StudyGroups, pk=pk) #Get group, if it exists.
     return render(request, 'studybearsapp/group_detail_page.html', {'group': group_id, 'members': group_id.members.all()})

def group_update_page(request, pk):
    group_id = get_object_or_404(StudyGroups, pk=pk)
    if request.method == 'POST':
        form = CreateGroupForm(request.POST)
        if form.is_valid():
            group_name = form.cleaned_data['group_name']
            course = form.cleaned_data['course']
            date_time = form.cleaned_data['date_time']
            location = form.cleaned_data['location']
            capacity = form.cleaned_data['capacity']
            study_strategies = form.cleaned_data['study_strategies']
            if group_name != '' and group_id.name != group_name:
                group_id.name = group_name
            if course != '' and group_id.course != course:
                group_id.course = course
            if date_time != '' and group_id.date_time != date_time:
                group_id.date_time = date_time
            if location != '' and group_id.location != location:
                group_id.location = location
            if capacity != '' and group_id.capacity != capacity:
                group_id.capacity = capacity
            if study_strategies != '' and group_id.study_strategies != study_strategies:
                group_id.study_strategies = study_strategies
            group_id.save()
            return redirect(group_id)
    else:
        data = {'group_name': group_id.name,
                'course': group_id.course,
                'date_time': group_id.date_time,
                'location': group_id.location,
                'capacity': group_id.capacity,
                'study_strategies': group_id.study_strategies
                }
        form = CreateGroupForm(initial=data)
    return render(request, 'studybearsapp/update_group.html', {'form': form, 'group': group_id})

@login_required
def profiles_home(request):
    return HttpResponseRedirect(reverse('profile_page', args=[request.user.username]))

def group_delete_page(request, pk):
    group_id = get_object_or_404(StudyGroups, pk=pk)
    if request.method == 'POST':
        group_id.delete()  #Delete the group from the database.
        return redirect('groups')
    return render(request, 'studybearsapp/delete_group.html', {'group': group_id})

def create_meeting(request, pk):
    group_id = get_object_or_404(StudyGroups, pk=pk)
    if request.method == 'POST':
        form = CreateMeetingForm(request.POST)
        if form.is_valid():
            start_time = form.cleaned_data['start_time']
            end_time = form.cleaned_data['end_time']
            location = form.cleaned_data['location']
            description = form.cleaned_data['description']
            authorize(request, pk)
            if 'credentials' not in request.session:
                return redirect('authorize')

            credentials = google.oauth2.credentials.Credentials(
              **request.session['credentials'])
            calendar = googleapiclient.discovery.build(API_SERVICE_NAME, API_VERSION, credentials=credentials)
            event = {"summary": group_id.name + " Meeting",
                     "location": location,
                     "description": description,
                     "start": {
                        "dateTime": start_time.isoformat(),
                        "timeZone": "America/Los_Angeles"
                        },
                      "end": {
                        "dateTime": end_time.isoformat(),
                        "timeZone": "America/Los_Angeles"
                      }
                    }
            calendar.events().insert(calendarId='primary', body=event).execute()
            return render(request, 'studybearsapp/meeting_successfully_added.html', {'group': group_id})
    else:
        form = CreateMeetingForm()
    return render(request, 'studybearsapp/create_meeting.html', {'form': form, 'group': group_id})

def add_member(request, pk):
    group = get_object_or_404(StudyGroups, pk=pk)
    if request.method == 'POST':
        form = AddMemberForm(request.POST)
        if form.is_valid():
            username = form.cleaned_data['username']
            user = User.objects.get(username=username)
            if user is not None:
                group.members.add(user)
            else:
                return render(request, 'studybearsapp/add_member_error.html', {'username': username})
    else:
        form = AddMemberForm()
    return render(request, 'studybearsapp/add_member.html', {'group': group, 'form': form})

def remove_member(request, pk):
    return render(request, 'studybearsapp/remove_member.html')



 #def logout_view(request):
     #logout(request)
     #Redirect to a success page.
