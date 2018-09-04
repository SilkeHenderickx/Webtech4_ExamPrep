# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
from .models import Trivia

# Create your views here.

def index(request):
    trivia = Trivia.objects.all()
    return render(request, 'index.html', {'trivia': trivia})
