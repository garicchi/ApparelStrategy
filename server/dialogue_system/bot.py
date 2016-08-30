# -*- coding: utf-8 -*-
from dialogue_system.dialogue_management.manager import DialogueManager
from dialogue_system.language_generation.generator import LanguageGenerator
from dialogue_system.language_understanding.language_understanding import RuleBasedLanguageUnderstanding
from dialogue_system.module.rulemanager import RuleManager
import os

class Bot(object):

    def __init__(self):
        """
        self.generator = LanguageGenerator()
        self.language_understanding = RuleBasedLanguageUnderstanding()
        self.manager = DialogueManager()
        """
        rulepath = os.path.join(os.path.dirname(__file__),'../rule.csv')
        self.rule_manager = RuleManager(rulepath)
        self.rule_manager.load()

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
        print('trigger {0} = {1}'.format(change.variable, change.value))

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
        print("before sent is {0}".format(sent))
        sent = self.rule_manager.input_utterance(sent,self.__trigger)
        print("after sent is {0}".format(sent))
        return sent
