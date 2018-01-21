from django.urls import path

from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('form', views.form, name='form'),
    path('group', views.group, name='group'),
    path('homepage', views.homepage, name='homepage'),
    path('post_form', views.post_form, name='post_form'),
    path('post_group', views.post_group, name= 'post_group')
]
