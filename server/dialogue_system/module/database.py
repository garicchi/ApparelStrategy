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
    cloth_describe = ''

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
        cloth.cloth_describe = cols[7]
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

    def get_personal_from_id(self,point_id):
        """
        read personal data from point id
        :param point_id: search point id
        :return: personal object
        """

        with codecs.open(self.personal_path,'r','utf-8') as f:
            for line in f:
                personal = self.__struct_personal(line)
                if personal.point_id == point_id:
                    return personal

        return None

    def get_clothes_from_code(self, cloth_code):
        """
        read cloth data from cloth_code
        :param cloth_code: cloth code for searching
        :return: cloth object
        """

        with codecs.open(self.clothes_path, 'r', 'utf-8') as f:
            for line in f:
                cloth = self.__struct_cloth(line)
                if cloth.cloth_code == cloth_code:
                    return cloth

        return None

    def get_evaluate_from_code(self, cloth_code):
        """
        read evaluate(osyaredo) from cloth code
        :param cloth_code: cloth code for searching evaluate
        :return: evaluate object list
        """
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

    def get_clothes_from_name(self, contains_name):
        """
        read cloth data from keyword that contains cloth name
        :param contains_name: key contains cloth name
        :return: cloth object list
        """
        result = []
        with codecs.open(self.clothes_path, 'r', 'utf-8') as f:
            for line in f:
                cloth = self.__struct_cloth(line)
                if cloth.cloth_name.count(contains_name) > 0:
                    result.append(cloth)

        if len(result) > 0:
            return result
        else:
            return None

    def get_clothes_from_keys(self, key1,key2):
        """
        read cloth data from keyword that contains cloth name
        :param contains_name: key contains cloth name
        :return: cloth object list
        """
        result = []
        with codecs.open(self.clothes_path, 'r', 'utf-8') as f:
            for line in f:
                cloth = self.__struct_cloth(line)
                if cloth.cloth_describe.count(key1) > 0 and cloth.cloth_describe.count(key2) > 0:
                    result.append(cloth)

        if len(result) > 0:
            return result
        else:
            return None




if __name__ == '__main__':
    script_dir = os.path.dirname(__file__)
    data_path = os.path.join(script_dir,'../../data')
    manager = DataBaseManager(data_path)
    personal = manager.get_clothes_from_name('ズボン')
    for p in personal:
        print(p.cloth_name)
