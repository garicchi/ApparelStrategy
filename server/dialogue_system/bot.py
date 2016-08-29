# -*- coding: utf-8 -*-
from server.dialogue_system.dialogue_management.manager import DialogueManager
from server.dialogue_system.language_generation.generator import LanguageGenerator
from server.dialogue_system.language_understanding.language_understanding import RuleBasedLanguageUnderstanding
from server.dialogue_system.module.rulemanager import RuleManager
import os

class Bot(object):

    def __init__(self):
        """
        self.generator = LanguageGenerator()
        self.language_understanding = RuleBasedLanguageUnderstanding()
        self.manager = DialogueManager()
        """
        rulepath = os.path.join(os.path.dirname(__file__),'../../rule.csv')
        self.rule_manager = RuleManager(rulepath)
        self.rule_manager.load()

    def __trigger(self, change, variables):
        """
        rulemanager.input_utterance内でコールされる変数のトリガー
        変数辞書を必ず返さなければいけない
        :param change:変化があった変数のtriggerオブジェクト
        :param variables:変数辞書
        :return:新しい変数辞書
        """
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
        sent = self.rule_manager.input_utterance(sent,self.__trigger)
        return sent
