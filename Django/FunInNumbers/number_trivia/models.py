# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

# Create your models here.
class Trivia(models.Model):
    text = models.CharField(max_length = 200)
    number = models.IntegerField()

def __str__(self):
    return self.text
