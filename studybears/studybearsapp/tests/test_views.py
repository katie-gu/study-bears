from django.test import TestCase
from django.urls import reverse

# Create your tests here.
class ViewTests(TestCase):
    def test_activate(self): # Check if user is none or if token is invalid.
        response = client.get('http://127.0.0.1:8000/studybearsapp/post_formactivate/(%3FPb'NjE'%5B0-9A-Za-z_%5C-%5D+)/(%3FP4wp-78cd62d8b0fec2fc775c%5B0-9A-Za-z%5D%7B1,13%7D-%5B0-9A-Za-z%5D%7B1,20%7D)/')
        self.assertEqual(response.context[''], 61)
