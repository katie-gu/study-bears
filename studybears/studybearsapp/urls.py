from django.urls import path, re_path
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
    re_path(r'^profile_page/(?P<username>\w+)/$', views.profile_page, name= 'profile_page'),
    path('profiles/home', views.profiles_home, name= 'profiles_home'),
    path('login', auth_views.LoginView.as_view(template_name='studybearsapp/login.html')),
    path('sign_up', views.sign_up, name='sign_up'),
    path('account_activation_sent', views.account_activation_sent, name='account_activation_sent'),
    re_path(r'^activate/(?P<uidb64>[0-9A-Za-z_\-]+)/(?P<token>[0-9A-Za-z]{1,13}-[0-9A-Za-z]{1,20})/$', views.activate, name='activate'),
    path('password_reset', auth_views.PasswordResetView.as_view(template_name='studybearsapp/registration/password_reset_form.html'), name='password_reset'),
    path('password_reset/done', auth_views.PasswordResetDoneView.as_view(template_name='studybearsapp/registration/password_reset_done.html'), name='password_reset_done'),
    re_path(r'^reset/(?P<uidb64>[0-9A-Za-z_\-]+)/(?P<token>[0-9A-Za-z]{1,13}-[0-9A-Za-z]{1,20})/$', auth_views.PasswordResetConfirmView.as_view(template_name='studybearsapp/registration/password_reset_confirm.html'), name='password_reset_confirm'),
    path('reset/done', auth_views.PasswordResetCompleteView.as_view(template_name='studybearsapp/registration/password_reset_done.html'), name='password_reset_complete'),
    path('logout', auth_views.LogoutView.as_view()),
    path('create_group', views.create_group, name='create_group'),
    path('create_group_post', views.create_group, name='create_group_post'),
    path('groups', views.groups, name='groups'),
    path('group/<int:pk>', views.group_detail_page, name='group_detail_page'),
    path('group/<int:pk>/update', views.group_update_page, name='group_update_page'),
    path('group/<int:pk>/update/post', views.group_update_page, name='group_update_page_post'),
    path('group/<int:pk>/delete', views.group_delete_page, name='group_delete_page'),
    path('group/<int:pk>/delete/post', views.group_delete_page, name='group_delete_page_post'),
    path('authorize', views.authorize, name='authorize'),
    path('group/<int:pk>/create_meeting', views.create_meeting, name='create_meeting'),
    path('group/<int:pk>/create_meeting/oauth2callback', views.oauth2callback, name='oauth2callback'),
    path('group/<int:pk>/create_meeting/post', views.create_meeting, name='create_meeting_post'),
    path('group/<int:pk>/add_member', views.add_member, name='add_member'),
    path('group/<int:pk>/add_member/post', views.add_member, name='add_member_post'),
    path('group/<int:pk>/remove_member', views.remove_member, name='remove_member'),
    path('group/<int:pk>/remove_member/post', views.remove_member, name='remove_member_post')
]
