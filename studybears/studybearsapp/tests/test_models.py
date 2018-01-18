from django.test import TestCase

# Create your tests here.
class YourTestClass(TestCase):

    @classmethod
    def setUpTestData(cls):
        print("setUpTestData: Run once to set up non-modified data for all class methods.")
        pass

    def setUp(self):
        print("setUp: Run once for every test method to setup clean data.")
        pass

    def test_add_member(self):
        study_group_1 = StudyGroups(course="CS 70", location="Moffitt", size=0, capacity=4)
        self.assertTrue(study_group_1.is_open())
        katie = User(name="Katie", email="katiegu@berkeley.edu", phone_number=8185194728)
        study_group_1.add_member(katie)
        self.assertTrue(self.size == 1)
