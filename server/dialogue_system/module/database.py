# -*- coding: utf-8 -*-

## pip install mysqlclient

import MySQLdb
import json
import os
import codecs

class Personal:
    point_id = ''
    user_name = ''
    user_pronoun = ''
    sex = ''
    phone = ''
    email = ''
    address = ''

class Cloth:
    cloth_name = ''
    color_code = ''
    small_type = ''
    price = ''
    image_url = ''
    big_type = ''
    cloth_code = ''

class Osyaredo:
    clothes = []
    osyaredo = 0

class DataBaseManager:

    def __init__(self,data_dir):
        self.data_dir = data_dir
        self.clothes_path = os.path.join(data_dir,'clothes.csv')
        self.evaluate_path = os.path.join(data_dir,'evaluate.csv')
        self.personal_path = os.path.join(data_dir,'personal.csv')

    def __struct_personal(self,line):
        cols = line.replace('\n','').replace('"','').split(',')
        personal = Personal()
        personal.point_id = cols[0]
        personal.user_name = cols[1]
        personal.user_pronoun = cols[2]
        personal.sex = cols[3]
        personal.phone = cols[4]
        personal.email = cols[5]
        personal.address = cols[6]
        personal.age = cols[7]
        return personal

    def read_personal(self,point_id):

        with codecs.open(self.personal_path,'r','utf-8') as f:
            for line in f:
                personal = self.__struct_personal(line)
                if personal.point_id == point_id:
                    return personal

        return None



if __name__ == '__main__':
    script_dir = os.path.dirname(__file__)
    data_path = os.path.join(script_dir,'../../data')
    manager = DataBaseManager(data_path)
    personal = manager.read_personal('89b7306b90823849030c9314a527aa16')
    print(personal.address)
