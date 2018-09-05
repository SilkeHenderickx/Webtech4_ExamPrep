# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
from .models import Trivia
from .forms import TriviaForm
import requests

# Create your views here.

def index(request):
    form = TriviaForm()
    return render(request, 'index.html')

def all_trivia(request):
    trivia = Trivia.objects.all()
    numbers = Trivia.objects.order_by('number').values('number').distinct()
    return render(request, 'all_trivia.html', {'trivia': trivia, 'numbers': numbers})

def post_trivia(request):
    n = request.POST.get('number', 0)
    r = requests.get("http://numbersapi.com/" + str(n) + "/trivia")

    try:
        entry = Trivia.objects.get(text = r.text)
    except Trivia.DoesNotExist:
        entry = None

    if entry != None:
        trivia = entry
    else:
        trivia = Trivia(text = r.text, number = n)
        trivia.save();

    return render(request, 'index.html', {'trivia': trivia})
