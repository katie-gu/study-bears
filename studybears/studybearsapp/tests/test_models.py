from django.test import TestCase
from studybearsapp.models import StudyGroups, Profile
from django.contrib.auth.models import User 
from django.contrib.auth import authenticate 

# Create your tests here.
class YourTestClass(TestCase):

    @classmethod
    def setUpTestData(cls):
        print("setUpTestData: Run once to set up non-modified data for all class methods.")
        pass

    def test_add_member(self):
        study_group_1 = StudyGroups.objects.get(course="CS 70", location="Moffitt", size=0, capacity=3)
        self.assertTrue(study_group_1.is_open())
        katie = Profile.objects.get(name="Katie", email="katiegu@berkeley.edu", phone_number=1)
        User.objects.create_user('katie', "katie.email", 'test_password')

        katie.add_new_member(study_group_1)
        print("katie has been added")
        self.assertTrue(study_group_1.size == 1)
        vera = Profile.objects.get(name="Vera", email="veraw@berkeley.edu", phone_number=2)
        vera.add_new_member(study_group_1)
        print("vera has been added")
        self.assertTrue(study_group_1.size == 2)
        # jhinuk = Profile.objects.get(name="Jhinuk", email="jhinukb@berkeley.edu", phone_number=3)
        # jhinuk.add_new_member(study_group_1)
        # self.assertTrue(study_group_1.size == 3)

    def test_authenticate(self):
        katie = Profile.objects.get(name="Katie", email="katiegu@berkeley.edu", phone_number=1)
        User.objects.create_user('katie', "katie.email", 'test_password')
        tester= authenticate(username = 'katie', password= 'test_password')
        if tester is not None: 
            print("authenticated correctly!")
        else: 
            print("didn't authenticate :(")

        #Test that logging into katie's account works, and that logging in with a wrong username/pw does not. 


    def tearDown(self):
        #Clean up run after every test method.
        pass

    def setUp(self):
        StudyGroups.objects.create(course="CS 70", location="Moffitt", size=0, capacity=3)
        katie = User.objects.create_user('katie_username', "katie_email", 'katie_password')
        Profile.objects.create(name="Katie", email="katiegu@berkeley.edu", phone_number=1, user=katie)
        vera = User.objects.create_user('v_username', "katie_email", 'katie_password')
        Profile.objects.create(name="Vera", email="veraw@berkeley.edu", phone_number=2, user=vera)
        jhinuk= User.objects.create_user('j_username', "katie_email", 'katie_password')
        Profile.objects.create(name="Jhinuk", email="jhinukb@berkeley.edu", phone_number=2, user=jhinuk)
        yaju= User.objects.create_user('y_username', "katie_email", 'katie_password')
        Profile.objects.create(name="Yaju", email="yajum@berkeley.edu", phone_number=5, user=yaju)


    def test_add_past_capacity(self):
        study_group_1 = StudyGroups.objects.get(course="CS 70", location="Moffitt", size=0, capacity=3)
        katie = Profile.objects.get(name="Katie")
        #, studyemail="katiegu@berkeley.edu", phone_number=8185194728)
        katie.add_new_member(study_group_1)
        self.assertTrue(study_group_1.size == 1)
        vera = Profile.objects.get(name="Vera")
        vera.add_new_member(study_group_1)
        self.assertTrue(study_group_1.size == 2)
        jhinuk = Profile.objects.get(name="Jhinuk")
        jhinuk.add_new_member(study_group_1)
        self.assertTrue(study_group_1.size == 3)
        yaju = Profile.objects.get(name="Yaju")
        yaju.add_new_member(study_group_1)
        self.assertTrue(study_group_1.size == 3)
        print("past capacity not allowed!")

    def tearDown(self):
       #Clean up run after every test method.
       pass

    def test_find_best_group(self):
        katie = Profile.objects.get(name="Katie")
        best_group_actual = katie.find_best_group("CS 70", "Moffitt")
        best_group_expected = StudyGroups.objects.get(course="CS 70", location="Moffitt", size=0, capacity=3)
        print(best_group_expected.course)
        print(best_group_actual.course)
        print(best_group_expected.size)
        print(best_group_actual.size)
        self.assertTrue(best_group_expected.id == best_group_actual.id)

    def tearDown(self):
       #Clean up run after every test method.
       pass
