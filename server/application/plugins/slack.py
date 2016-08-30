# -*- coding: utf-8 -*-

from slackbot.bot import respond_to

from dialogue_system.bot import Bot
import json

bots = {}


def create_or_read(user_id):
    return bots[user_id] if user_id in bots else Bot()


def save_bot(bot, user_id):
    bots[user_id] = bot


@respond_to('(.*)')
def food(message, something):
    body = message.body
    text, ts, user_id = body['text'], body['ts'], body['user']
    bot = create_or_read(user_id)
    text_json = '{"speech":"'+text+'"}'
    replies = bot.reply(text_json)
    save_bot(bot, user_id)
    for rep in replies:
        json_obj = json.loads(rep)
        message.reply(json_obj['data'])
