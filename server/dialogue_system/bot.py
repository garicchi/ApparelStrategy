# -*- coding: utf-8 -*-
from dialogue_system.dialogue_management.manager import DialogueManager
from dialogue_system.language_generation.generator import LanguageGenerator
from dialogue_system.language_understanding.language_understanding import RuleBasedLanguageUnderstanding
from dialogue_system.module.rulemanager import RuleManager
import os
import json
import base64
from dialogue_system.module.barcode import *
from dialogue_system.module.database import DataBaseManager
from dialogue_system.module.posttwitter import PostTwitter
import datetime
from dialogue_system.module.pixelpy import Pixel
import random


class Bot(object):
    isDebug = False
    isDemo = True

    def __init__(self):
        """
        self.generator = LanguageGenerator()
        self.language_understanding = RuleBasedLanguageUnderstanding()
        self.manager = DialogueManager()
        """
        rulepath = os.path.join(os.path.dirname(__file__),'../rule.csv')
        self.rule_manager = RuleManager(rulepath)
        self.rule_manager.load(self.__trigger)
        data_dir = os.path.join(os.path.dirname(__file__), '../data')
        self.data_manager = DataBaseManager(data_dir)

        # a user using system
        self.current_personal = None
        # some cloth list scanned by sota
        self.current_cloth_list = []
        # a level of osyare on current user
        self.current_osyare = None

        self.saturation = None

        self.recommend_cloth = None

        script_dir = os.path.dirname(__file__)
        key_path = os.path.join(script_dir, '../key.json')
        with open(key_path) as f:
            self.keys = json.load(f)

        self.twitter = PostTwitter(self.keys['consumer-key'],self.keys['consumer-secret'],self.keys['access-token'],self.keys['access-secret'])

        self.__print_current_variables()


    def __trigger(self, change, variables):
        """
        rulemanager.input_utterance内でコールされる変数のトリガー
        ここを変更することで対話ルール内にプログラムコードを介入できる
        変数辞書variableを必ず返さなければいけない
        :param change:変化があった変数のtriggerオブジェクト
        :param variables:変数辞書
        :return:新しい変数辞書
        """
        self.__print_debug('trigger  row: {0}  {1} = [{2}]'.format(change.row_num, change.variable, change.value))

        """変数を変更する場合はこう
        if change.variable == 'u_a' and change.value == 'hello':
            variable['s_a'] = 'say-hello'
        """

        # change.valueはアクションの言葉の変数置き換えてないバージョン
        # change.value_alternateが変数を置き換えたバージョン
        alter_value = change.value_alternate

        if change.variable == 'scan_point':
            self.current_personal = self.data_manager.get_personal_from_id(alter_value)
            self.rule_manager.variables['current_user'] = self.current_personal.user_pronoun
            self.rule_manager.variables['qr_data']= 'null'
            self.__print_demo('[scan point card] name:{0} address:{1}'.format(self.current_personal.user_name,self.current_personal.address))

        if change.variable == 'scan_cloth':
            cloth = self.data_manager.get_clothes_from_code(alter_value)
            self.current_cloth_list.append(cloth)
            self.rule_manager.variables['current_cloth'] = cloth.price
            self.rule_manager.variables['qr_data'] = 'null'

            self.__print_demo('[scan cloth] name:{0} price:{1}yen'.format(cloth.cloth_name,
                                                                              cloth.price))

            # test
            status = '【スキャンした服】 '+cloth.cloth_name + '  '+str(datetime.datetime.today())
            self.twitter.post_image_url(status,cloth.image_url)

        if change.variable == 'end_cloth':
            if len(self.current_cloth_list) == 0:
                return variables

            ev = self.data_manager.get_evaluate_from_codelist(self.current_cloth_list)
            self.rule_manager.variables['is_osyare'] = 'false'
            if ev is not None:
                ev_choice = random.choice(ev)
                self.rule_manager.variables['current_osyare'] = ev_choice.osyaredo
                self.__print_debug('osyaredo = {0}'.format(ev_choice.osyaredo))
                self.current_osyare = ev_choice

                # オシャレ度が50以上なら{is_osyare}をtrueにする
                if int(ev_choice.osyaredo) > 50:
                    self.rule_manager.variables['is_osyare'] = 'true'
            else:
                self.__print_debug('no match in osyaredo')
                self.current_osyare = None

            for c in self.current_cloth_list:
                self.__print_debug(c.cloth_name)

            self.rule_manager.variables['qr_data'] = 'null'

        if change.variable == 'search_db':
            season = self.rule_manager.variables['season']
            price = self.rule_manager.variables['price']
            data_list = self.data_manager.get_clothes_from_keys(season,price)

            if data_list is not None:
                choice = random.choice(data_list)
                self.recommend_cloth = choice

                status = '【おすすめの服】 '+choice.cloth_name+ '  '+str(datetime.datetime.today())
                self.twitter.post_image_url(status,choice.image_url)

                self.rule_manager.variables['recommend'] = choice.cloth_name
            else:
                self.rule_manager.variables['recommend'] = 'null'



        return variables

    def __get_speech_list(self,utterance_list):
        return_speech = []
        for system_action in utterance_list:
            if system_action == 'picture':
                return_speech.append('picture,picture')
            elif system_action == 'capture':
                return_speech.append('capture,capture')
            else:
                return_speech.append('speech,' + system_action)
        return return_speech

    def reply(self, sent):
        """
        dialogue_act = self.language_understanding.execute(sent)

        self.manager.update_dialogue_state(dialogue_act)
        sys_act_type = self.manager.select_action(dialogue_act)

        sent = self.generator.generate_sentence(sys_act_type)
        """
        return_speech = []
        messageObj = json.loads(sent)
        type = messageObj['type']
        data = messageObj['data']
        # sotaから来た発話
        # {s_u}にpictureという値を入れると写真をとってQRを読み取り、qr_dataという変数に読み取り結果を入れる
        if type == 'speech':
            system_action_list = self.rule_manager.input_utterance(data, self.__trigger)
            return_speech.extend(self.__get_speech_list(system_action_list))

        if type == 'scan':
            self.__print_debug('scan {0}'.format(data))
            system_action_list = self.rule_manager.input_variable('qr_data', data, self.__trigger)
            return_speech.extend(self.__get_speech_list(system_action_list))

        if type == 'picture':
            image_path = os.path.join(os.path.dirname(__file__), '../picture.jpg')
            file = base64.b64decode(data)
            with open(image_path, 'wb') as f:
                f.write(file)
            pixel = Pixel(image_path)
            self.saturation = pixel.get_saturation()
            self.__print_debug("saido = {0}".format(self.saturation))
            if self.current_osyare is None and self.saturation > 20 and self.saturation < 70:
                self.rule_manager.variables['is_osyare'] = 'true'
            else:
                self.rule_manager.variables['is_osyare'] = 'false'

        for r in return_speech:
            self.__print_debug('return speech is {0}'.format(r))

        self.__print_current_variables()

        return return_speech

    def __print_current_variables(self):
        result = 'variables [ '
        for k in self.rule_manager.variables.keys():
            v = self.rule_manager.variables[k]
            result = result + '{ '+k+' : '+v+' },'
        result = result + ' ]'
        self.__print_debug(result)

    def __print_debug(self,str):
        if self.isDebug:
            print(str)

    def __print_demo(self,str):
        if self.isDemo:
            print(str)
