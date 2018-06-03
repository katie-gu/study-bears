from django.urls import path
from django.conf.urls import url
from . import views
from django.contrib import admin
from django.contrib.auth import views as auth_views

urlpatterns = [
    path('', views.index, name='index'),
    path('form', views.form, name='form'),
    path('group', views.group, name='group'),
    path('homepage', views.homepage, name='homepage'),
    path('post_form', views.post_form, name='post_form'),
    path('post_group', views.post_group, name= 'post_group'),
    path('profile_page', views.profile_page, name= 'profile_page'),
    path('login', auth_views.LoginView.as_view(template_name='studybearsapp/login.html')),
    path('sign_up', views.sign_up, name='sign_up'),
    path('account_activation_sent', views.account_activation_sent, name='account_activation_sent'),
    path('activate/(?P<uidb64>[0-9A-Za-z_\-]+)/(?P<token>[0-9A-Za-z]{1,13}-[0-9A-Za-z]{1,20})/', views.activate, name='activate'),
    path('logout', auth_views.LogoutView.as_view()),
]
