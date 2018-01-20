from django.test import TestCase
from studybearsapp.models import StudyGroups, User, Animal

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

    """def test_set_up(self):
        print(self.setUp())
        if self.setUp():
            print("Success")
        else:
            print("Error")"""

    def test_add_member(self):
        study_group_1 = StudyGroups.objects.get(course="CS 70", location="Moffitt", size=0, capacity=3)
        self.assertTrue(study_group_1.is_open())
        katie = User.objects.get(name="Katie", email="katiegu@berkeley.edu", phone_number=8185194728)
        katie.add_new_member(study_group_1)
        print("katie has been added")
        self.assertTrue(study_group_1.size == 1)
        vera = User.objects.get(name="Vera", email="veraw@berkeley.edu", phone_number=123456789)
        vera.add_new_member(study_group_1)
        print("vera has been added")
        self.assertTrue(study_group_1.size == 2)
        jhinuk = User.objects.get(name="Jhinuk", email="jhinukb@berkeley.edu", phone_number=222333444)
        jhinuk.add_new_member(study_group_1)
        self.assertTrue(study_group_1.size == 3)

    def tearDown(self):
        #Clean up run after every test method.
        pass

    def setUp(self):
        StudyGroups.objects.create(course="CS 70", location="Moffitt", size=0, capacity=3)
        User.objects.create(name="Katie", email="katiegu@berkeley.edu", phone_number=8185194728)
        User.objects.create(name="Vera", email="veraw@berkeley.edu", phone_number=123456789)
        User.objects.create(name="Jhinuk", email="jhinukb@berkeley.edu", phone_number=222333444)
        User.objects.create(name="Yaju", email="yajum@berkeley.edu", phone_number=555666777)

    def test_add_past_capacity(self):
        study_group_1 = StudyGroups.objects.get(course="CS 70", location="Moffitt", size=0, capacity=3)
        katie = User.objects.get(name="Katie", email="katiegu@berkeley.edu", phone_number=8185194728)
        katie.add_new_member(study_group_1)
        self.assertTrue(study_group_1.size == 1)
        vera = User.objects.get(name="Vera", email="veraw@berkeley.edu", phone_number=123456789)
        vera.add_new_member(study_group_1)
        self.assertTrue(study_group_1.size == 2)
        jhinuk = User.objects.get(name="Jhinuk", email="jhinukb@berkeley.edu", phone_number=222333444)
        jhinuk.add_new_member(study_group_1)
        self.assertTrue(study_group_1.size == 3)
        yaju = User.objects.get(name="Yaju", email="yajum@berkeley.edu", phone_number=555666777)
        yaju.add_new_member(study_group_1)
        self.assertTrue(study_group_1.size == 3)

    def tearDown(self):
       #Clean up run after every test method.
       pass
