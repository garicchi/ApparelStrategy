# -*- coding: utf-8 -*-

import os,sys
sys.path.append(os.getcwd())

import dialogue_system.bot

def main():
    bot = dialogue_system.bot.Bot()
    bot.run()
 
if __name__ == "__main__":
    main()