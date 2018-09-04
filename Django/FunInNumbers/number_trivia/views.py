# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

# Create your views here.
def index(request):
    number = 42
    text = '42 is the number of gallons that one barrel of petroleum holds.'

    context = {
    'trivia_number': number,
    'trivia_text': text,
    }
    return render(request, 'index.html', context)
