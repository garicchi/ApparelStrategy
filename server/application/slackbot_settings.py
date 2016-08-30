# -*- coding: utf-8 -*-
import os

#f = open('./../dialogue_system/backend/apis/key.txt').readlines()
#API_TOKEN = os.environ.get('SLACK_API_KEY', '')

script_dir = os.path.dirname(__file__)
key_path = os.path.join(script_dir,'../key.txt')
with open(key_path, "r") as f:
    lines = f.readlines()

API_TOKEN = lines[1].rstrip("\n")
 
default_reply = "スイマセン。其ノ言葉ワカリマセン"

PLUGINS = [
    'plugins',
]