# -*- coding: utf-8 -*-
import tornado.ioloop
import tornado.web
import tornado.websocket
from tornado.options import options
import signal

from dialogue_system.bot import Bot

class IndexHandler(tornado.web.RequestHandler):

    def get(self):
        self.write('Hello World!')


class MessageServer(tornado.websocket.WebSocketHandler):
    bots = {}

    def check_origin(self, origin):
        return True

    def open(self):
        print('on open')
        self.bots[self] = Bot()
        self.write_message('何かしゃべってください')

    def on_message(self, message):
        print('on message')
        print(message)
        bot = self.bots[self]
        self.write_message(bot.reply(message))

    def on_close(self):
        print('on close')
        del self.bots[self]

application = tornado.web.Application([
    (r'/', IndexHandler),
    (r'/ws', MessageServer)
    ])

is_closing = False

def signal_handler(signum, frame):
    global is_closing
    print('exiting...')
    is_closing = True

def try_exit():
    global is_closing
    if is_closing:
        # clean up here
        tornado.ioloop.IOLoop.instance().stop()
        print('exit success')

if __name__ == '__main__':
    tornado.options.parse_command_line()
    signal.signal(signal.SIGINT, signal_handler)
    application.listen(8080)
    tornado.ioloop.PeriodicCallback(try_exit, 100).start()
    tornado.ioloop.IOLoop.current().start()