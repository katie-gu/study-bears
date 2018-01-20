from django.test import TestCase
from studybearsapp.appskeleton import models

# Create your tests here.
class YourTestClass(TestCase):

    @classmethod
    def setUpTestData(cls):
        print("setUpTestData: Run once to set up non-modified data for all class methods.")
        pass

    def setUp(self):
        StudyGroups.objects.create(course="CS 70", location="Moffitt", size=0, capacity=3)
        User.objects.create(name="Katie", email="katiegu@berkeley.edu", phone_number=8185194728)
        User.objects.create(name="Vera", email="veraw@berkeley.edu", phone_number=123456789)
        User.objects.create(name="Jhinuk", email="jhinukb@berkeley.edu", phone_number=222333444)
        pass

    def test_add_member(self):
        self.assertTrue(study_group_1.is_open())
        study_group_1.add_member(katie)
        self.assertTrue(self.size == 1)
        study_group_1.add_member(vera)
        self.assertTrue(self.size == 2)
        study_group_1.add_member(jhinuk)
        self.assertTrue(self.size == 3)

    def tearDown(self):
        #Clean up run after every test method.
        pass

    def setUp(self):
        print("setUp: Run once for every test method to setup clean data.")
        pass

    def test_add_past_capacity(self):
        study_group_1 = StudyGroups(course="CS 70", location="Moffitt", size=0, capacity=3)
        self.assertTrue(study_group_1.is_open())
        katie = User(name="Katie", email="katiegu@berkeley.edu", phone_number=8185194728)
        study_group_1.add_member(katie)
        self.assertTrue(self.size == 1)
        vera = User(name="Vera", email="veraw@berkeley.edu", phone_number=123456789)
        study_group_1.add_member(vera)
        self.assertTrue(self.size == 2)
        jhinuk = User(name="Jhinuk", email="jhinukb@berkeley.edu", phone_number=222333444)
        study_group_1.add_member(jhinuk)
        self.assertTrue(self.size == 3)
        yaju = User(name="Yaju", email="yajum@berkeley.edu", phone_number=555666777)
        study_group_1.add_member(yaju)
        self.assertTrue(self.size == 3)

    def tearDown(self):
       #Clean up run after every test method.
       pass
