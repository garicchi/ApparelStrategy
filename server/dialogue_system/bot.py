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

class Bot(object):

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
        self.current_personal = None
        self.current_cloth_list = []
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
        #debug mode
        print('trigger  row: {0}  {1} = [{2}]'.format(change.row_num, change.variable, change.value))

        """変数を変更する場合はこう
        if change.variable == 'u_a' and change.value == 'hello':
            variable['s_a'] = 'say-hello'
        """
        alter_value = change.value_alternate

        if change.variable == 'scan_point':
            self.current_personal = self.data_manager.get_personal_from_id(alter_value)
            self.rule_manager.variables['current_user'] = self.current_personal.user_pronoun
            self.rule_manager.variables['qr_data']= 'null'

        if change.variable == 'scan_cloth':
            print("change value = {0}".format(alter_value))
            cloth = self.data_manager.get_clothes_from_code(alter_value)
            print('cloth code = {0}'.format(cloth.cloth_code))
            self.current_cloth_list.append(cloth)
            self.rule_manager.variables['current_cloth'] = cloth.price
            self.rule_manager.variables['qr_data'] = 'null'

        if change.variable == 'end_cloth':
            first = self.current_cloth_list[0]
            ev = self.data_manager.get_evaluate_from_code(first.cloth_code)
            if ev != None:
                self.rule_manager.variables['current_osyare'] = ev[0].osyaredo
                print('osyaredo = {0}'.format(ev[0].osyaredo))
            else:
                print('no match in osyaredo for '+first.cloth_code)

            for c in self.current_cloth_list:
                print(c.cloth_name)

            self.rule_manager.variables['qr_data'] = 'null'

        return variables

    def __get_speech_list(self,utterance_list):
        return_speech = []
        for system_action in utterance_list:
            if system_action == 'picture':
                return_speech.append('picture,picture')
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
            print('scan {0}'.format(data))
            system_action_list = self.rule_manager.input_variable('qr_data', data, self.__trigger)
            return_speech.extend(self.__get_speech_list(system_action_list))

        if type == 'picture':
            image_path = os.path.join(os.path.dirname(__file__), '../picture.jpg')
            file = base64.b64decode(data)
            with open(image_path, 'wb') as f:
                f.write(file)
            qr = read_qr(image_path)

            if qr == '':
                qr = 'null'

            # qrコードを読み取ると{qr_data}という変数に読み取り結果を入れる。nullなら読み取り失敗
            system_action_list =self.rule_manager.input_variable('qr_data',qr, self.__trigger)
            return_speech.extend(self.__get_speech_list(system_action_list))

        for r in return_speech:
            print('')
            print('return speech is {0}'.format(r))

        self.__print_current_variables()

        return return_speech

    def __print_current_variables(self):
        result = 'variables [ '
        for k in self.rule_manager.variables.keys():
            v = self.rule_manager.variables[k]
            result = result + '{ '+k+' : '+v+' },'
        result = result + ' ]'
        print(result)
