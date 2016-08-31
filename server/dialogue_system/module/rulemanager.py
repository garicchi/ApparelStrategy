# -*- coding: utf-8 -*-

import os
import re
import codecs

class Trigger:
    variable = ''
    expression = ''
    value = ''
    value_alternate = ''
    row_num = 0

    def __init__(self,variable,expression,value,row_num):
        self.variable = variable
        self.expression = expression
        self.value = value
        self.row_num = row_num
        self.value_alternate = ''

    def get_alternate_value(self,variables):
        self.value_alternate = self.value
        for var in variables.keys():
            self.value_alternate = self.value_alternate.replace('{' + var + '}', variables[var])

        return self.value_alternate

class Rule:
    rules = []
    actions = []

    def __init__(self,rules,actions):
        self.rules = rules
        self.actions = actions

class RuleManager:

    rule_path = ""
    rule_list = []
    variables = {}
    change_list = []

    def __init__(self,rule_file):
        self.rule_path = rule_file

    def __parse_rule(self,rule,row_num):
        variable = []
        is_variable = False
        value = []
        is_value = False
        expression = ''
        for i,c in enumerate(rule):
            if c == '}':
                is_variable = False
            if c == ']':
                is_value = False
            if is_variable:
                variable.append(c)
            if is_value:
                value.append(c)
            if c == '=' or c == '!':
                expression = c
            if c == '{' and not is_value:
                is_variable = True
            if c == '[' and not is_variable:
                is_value = True

        return Trigger(''.join(variable),expression,''.join(value),row_num)

    def __parse_express(self,express,row_num):
        trigger_strs = express.split('&')
        triggers = []
        for i,s in enumerate(trigger_strs):
            trigger = self.__parse_rule(s,row_num)
            triggers.append(trigger)

        return triggers

    def load(self,callback):
        self.rule_list = []
        with codecs.open(self.rule_path, 'r','utf-8') as f:
            rules = f.readlines()

        for i,line in enumerate(rules):
            if i == 0:
                continue
            if line.startswith('//'):
                continue
            rule = line.split(',')[0]
            action = line.split(',')[1]
            rule_obj = Rule(self.__parse_express(rule,i),self.__parse_express(action,i))
            self.rule_list.append(rule_obj)

        for i,raw in enumerate(self.rule_list):
            for j, r in enumerate(raw.rules):
                if r.variable == 'init' and r.value == '':
                    self.input_variable(r.variable,r.value,callback)
                    del self.rule_list[i]


    def input_utterance(self,utterance,callback):
        return self.input_variable('u_u',utterance,callback)

    def input_variable(self,var,value,callback):
        """
            ユーザー発話入力
            :param utterance:ユーザー発話
            :param callback:変数が変化したときのコールバック関数。triggerオブジェクトと変数辞書が渡される
            :return:システム発話
            """
        change = Trigger(var, '=', value,-1)
        system_utterance_list = []
        self.variables[change.variable] = change.value
        self.variables = callback(change, self.variables)
        hit_actions = []
        for j, raw in enumerate(self.rule_list):
            is_all_match = True
            for k, trigger in enumerate(raw.rules):
                if not (trigger.variable in self.variables):
                    self.variables[trigger.variable] = ''
                val = self.variables[trigger.variable]
                is_match = re.match(trigger.value, val)
                if trigger.expression == '=':
                    if not is_match:
                        is_all_match = False
                if trigger.expression == '!':
                    if is_match:
                        is_all_match = False
            if is_all_match:
                hit_actions.append(raw.actions)

        for i,hit in enumerate(hit_actions):
            for l, action in enumerate(hit):
                self.variables[action.variable] = action.value
                action.get_alternate_value(self.variables)
                if action.variable == 's_u':
                    system_utterance_list.append(action.value_alternate)
                self.variables = callback(action, self.variables)

        return system_utterance_list

def trigger(change,variables):
    """
    rulemanager.input_utterance内でコールされる変数のトリガー
    変数辞書を必ず返さなければいけない
    :param change:変化があった変数のtriggerオブジェクト
    :param variables:変数辞書
    :return:新しい変数辞書
    """
    print('trigger row:[{0}] var:[{1}] value:[{2}]'.format(change.row_num,change.variable, change.value))

    return variables

if __name__ == '__main__':
    manager = RuleManager(os.path.join(os.path.dirname(__file__),'../../rule.csv'))
    manager.load()
    system = manager.input_utterance('こんにちは',trigger)

    print(system)