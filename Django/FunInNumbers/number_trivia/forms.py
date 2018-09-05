from django import forms
from .models import Trivia

class TriviaForm(forms.ModelForm):
    class Meta:
        model = Trivia
        fields = ['number']
