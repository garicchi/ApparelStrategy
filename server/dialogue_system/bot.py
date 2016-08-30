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


        return variables

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
        if type == 'speech':
            system_action_list = self.rule_manager.input_utterance(data, self.__trigger)
            for system_action in system_action_list:
                if system_action == 'picture':
                    return_speech.append('picture,picture')
                else:
                    return_speech.append('speech,'+system_action)
        if type == 'picture':
            image_path = os.path.join(os.path.dirname(__file__), '../picture.jpg')
            print('take picture {0}'.format(image_path))
            file = base64.b64decode(data)
            with open(image_path, 'wb') as f:
                f.write(file)
            qr = read_qr(image_path)
            print('qr is ' + qr)
            if self.rule_manager.variables['pic_mode'] == 'point':
                personal = self.data_manager.get_personal_from_id(qr)
                if qr != '':
                    return_speech.append('speech,' + personal.user_pronoun + 'さん、こんにちは')

            if self.rule_manager.variables['pic_mode'] == 'cloth':
                cloth = self.data_manager.get_clothes_from_code(qr)
                if qr != '':
                    return_speech.append('speech,' + cloth.price+'円')

            if qr == '':
                return_speech.append('speech,読み取りに失敗しました。もう一度かざしてね')
                return_speech.append('picture,picture')
        print("return = {0}".format(return_speech))

        return return_speech
