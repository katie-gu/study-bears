from django import forms
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.models import User

class SignUpForm(UserCreationForm):
    first_name = forms.CharField(max_length=30, required=False, help_text='Optional.')
    last_name = forms.CharField(max_length=30, required=False, help_text='Optional.')
    email = forms.EmailField(max_length=254, help_text='Required. Please enter a valid email address.')

    class Meta:
        model = User
        fields = ('username', 'first_name', 'last_name', 'email', 'password1', 'password2',)

class CreateGroupForm(forms.Form):
    group_name = forms.CharField(label='Group Name', max_length=200, required=True)
    course = forms.CharField(label='Course', max_length=200, required=True)
    date_time = forms.DateTimeField(label='Meeting Date and Time', input_formats=['%m/%d/%y %H:%M'])
    location = forms.CharField(label='Meeting Location', max_length=200, required=True)
    capacity = forms.IntegerField(label='Capacity')
    study_strategies = forms.CharField(label='Study Strategies', max_length=500, required=True)
