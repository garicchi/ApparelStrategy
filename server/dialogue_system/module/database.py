# -*- coding: utf-8 -*-

## pip install mysqlclient

import MySQLdb
import json
import os

if __name__ == '__main__':
    keypath = os.path.join(os.path.dirname(__file__) ,'../../key.json')
    with open(keypath,'r') as f:
        json_obj = json.load(f)

    connector = MySQLdb.connect(
        user='root',
        passwd=json_obj['sql-pass'],
        host=json_obj['ec2-host'],
        db='ApparelStragegy'
        )

    cursor = connector.cursor()
    cursor.execute("select * from clothes")
    for row in cursor.fetchall():
        print("ID:" + str(row[0]) + "  NAME:" + row[1])
    cursor.close()