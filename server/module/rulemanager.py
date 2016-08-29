# -*- coding: utf-8 -*-

import os
import re

class Trigger:
    variable = ''
    expression = ''
    value = ''

    def __init__(self,variable,expression,value):
        self.variable = variable
        self.expression = expression
        self.value = value

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

    def __parse_rule(self,rule):
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

        return Trigger(''.join(variable),expression,''.join(value))

    def __parse_express(self,express):
        trigger_strs = express.split('&')
        triggers = []
        for i,s in enumerate(trigger_strs):
            trigger = self.__parse_rule(s)
            triggers.append(trigger)

        return triggers

    def load(self):
        self.rule_list = []
        with open(self.rule_path, 'r') as f:
            rules = f.readlines()
        for i,line in enumerate(rules):
            if i == 0:
                continue
            rule = line.split(',')[0]
            action = line.split(',')[1]
            rule_obj = Rule(self.__parse_express(rule),self.__parse_express(action))
            self.rule_list.append(rule_obj)

    def input_utterance(self,utterance):
        change = Trigger('u_u','=',utterance)
        system_utterance = ''
        self.variables[change.variable] = change.value
        self.__trigger(change)
        for j,raw in enumerate(self.rule_list):
            is_all_match = True
            for k,trigger in enumerate(raw.rules):
                if not (trigger.variable in self.variables):
                    self.variables[trigger.variable] = ''
                val = self.variables[trigger.variable]
                is_match = re.match(trigger.value,val)
                if trigger.expression == '=':
                    if not is_match:
                        is_all_match = False
                if trigger.expression == '!':
                    if is_match:
                        is_all_match = False
            if is_all_match:
                for l,action in enumerate(raw.actions):
                    self.variables[action.variable] = action.value
                    if action.variable == 's_u':
                        for var in self.variables.keys():
                            action.value = action.value.replace('{'+var+'}',self.variables[var])
                        system_utterance = action.value
                    self.__trigger(action)
        return system_utterance



    def __trigger(self,change_rule):
        print('trigger {0} = {1}'.format(change_rule.variable,change_rule.value))
        """
        if change_rule.variable == 'u_a' and change_rule.value == 'hello':
            self.variables['s_a'] = 'say-hello'
        """
if __name__ == '__main__':
    manager = RuleManager(os.path.join(os.path.dirname(__file__),'../../rule.csv'))
    manager.load()
    manager.input_utterance('こんにちは')