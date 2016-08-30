# -*- coding: utf-8 -*-
import os,sys
sys.path.append(os.pardir)
from dialogue_system.bot import Bot


def main():
    bot = Bot()
    bot.run()
 
if __name__ == "__main__":
    main()