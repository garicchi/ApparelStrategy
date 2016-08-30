# -*- coding: utf-8 -*-
import os

from doco.client import Client


class DocomoDialogAPI(object):

    def __init__(self, api_key=None):

#       api_key = os.environ.get('DOCOMO_DIALOGUE_API_KEY', api_key)
        script_dir = os.path.dirname(__file__)
        key_path = os.path.join(script_dir,'../../../key.txt')
        with open(key_path,"r") as f:
            lines = f.readlines()

        api_key = lines[3].rstrip("\n")
        self.__client = Client(apikey=api_key)

    def reply(self, text):
        response = self.__client.send(utt=text, apiname='Dialogue')
        utt = response['utt']

        return utt
