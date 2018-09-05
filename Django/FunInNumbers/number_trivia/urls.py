from django.conf.urls import url
from . import views

urlpatterns = [
url(r'^$', views.index),
url(r'all_trivia', views.all_trivia),
url(r'^post_trivia/$', views.post_trivia, name='post_trivia'),
]
