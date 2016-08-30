# -*- coding: utf-8 -*-

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

class Evaluate:
    clothes = []
    osyaredo = 0

class DataBaseManager:

    def __init__(self,data_dir):
        self.data_dir = data_dir
        self.clothes_path = os.path.join(data_dir,'clothes.csv')
        self.evaluate_path = os.path.join(data_dir,'evaluate.csv')
        self.personal_path = os.path.join(data_dir,'personal.csv')

    def __split_csvline(self,line):
        return line.replace('\n','').replace('"','').split(',')

    def __struct_personal(self,line):
        cols = self.__split_csvline(line)
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

    def __struct_cloth(self,line):
        cols = self.__split_csvline(line)
        cloth = Cloth()
        cloth.cloth_name = cols[0]
        cloth.color_code = cols[1]
        cloth.small_type = cols[2]
        cloth.price = cols[3]
        cloth.image_url = cols[4]
        cloth.big_type = cols[5]
        cloth.cloth_code = cols[6]
        return cloth

    def __struct_evaluate(self,line):
        cols = self.__split_csvline(line)
        osyare = Evaluate()
        osyare.clothes = []
        for c in cols:
            if c == 'null':
                break
            else:
                osyare.clothes.append(c)
        osyare.osyaredo = cols[3]
        return osyare

    def read_personal(self,point_id):

        with codecs.open(self.personal_path,'r','utf-8') as f:
            for line in f:
                personal = self.__struct_personal(line)
                if personal.point_id == point_id:
                    return personal

        return None

    def read_clothes(self, cloth_code):

        with codecs.open(self.clothes_path, 'r', 'utf-8') as f:
            for line in f:
                cloth = self.__struct_cloth(line)
                if cloth.cloth_code == cloth_code:
                    return cloth

        return None

    def read_evaluate(self, cloth_code):
        result = []
        with codecs.open(self.evaluate_path, 'r', 'utf-8') as f:
            for line in f:
                ev = self.__struct_evaluate(line)
                if ev.clothes.count(cloth_code) > 0:
                    result.append(ev)
        if len(result) > 0:
            return result
        else:
            return None



if __name__ == '__main__':
    script_dir = os.path.dirname(__file__)
    data_path = os.path.join(script_dir,'../../data')
    manager = DataBaseManager(data_path)
    personal = manager.read_evaluate('mano-a-mano_141601m')
    print(personal[0].osyaredo)
