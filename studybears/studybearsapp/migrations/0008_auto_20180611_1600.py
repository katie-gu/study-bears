# Generated by Django 2.0.5 on 2018-06-11 16:00

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('studybearsapp', '0007_auto_20180611_1558'),
    ]

    operations = [
        migrations.AlterField(
            model_name='studygroups',
            name='date_time',
            field=models.DateTimeField(default='2018-01-01 00:00'),
        ),
    ]