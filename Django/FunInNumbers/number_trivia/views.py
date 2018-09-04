# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

# Create your views here.

def index(request):
    return render(request, 'index.html', {'trivia': trivia})

class Trivia:
    def __init__(self, text, number):
        self.text = text
        self.number = number

trivia = [
Trivia('42 is the number of gallons that one barrel of petroleum holds.', 42),
Trivia('42 is the number of kilometers in a marathon.', 42),
Trivia('23 is the number of crosses on Calvary in the Monty Python film Life Of Brian.', 23),
]
