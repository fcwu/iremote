#!/usr/bin/env python2.7
from flask import Flask, jsonify
from flask import make_response
import subprocess

app = Flask(__name__)

keys = [
    {'k': '0', 's': '0'},
    {'k': '1', 's': '1'},
    {'k': '2', 's': '2'},
    {'k': '3', 's': '3'},
    {'k': '4', 's': '4'},
    {'k': '5', 's': '5'},
    {'k': '6', 's': '6'},
    {'k': '7', 's': '7'},
    {'k': '8', 's': '8'},
    {'k': '9', 's': '9'},
    {'k': 'p', 's': 'power'},
    {'k': 'q', 's': 'volume up'},
    {'k': 'a', 's': 'volume down'},
    {'k': 'w', 's': 'channel up'},
    {'k': 's', 's': 'channel down'},
    {'k': 'e', 's': 'source'},
    {'k': 'd', 's': 'enter'}
]

@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify( { 'error': 'Not found' } ), 404)

@app.route('/keys', methods = ['GET'])
def get_tasks():
    return jsonify( { 'keys': keys } )

@app.route('/keys/<string:key_ids>', methods = ['GET'])
def get_task(key_ids):
    ids = []
    for key_id in key_ids:
        key = filter(lambda t: t['k'] == key_id, keys)
        if len(key) == 0:
            continue
        ids.append(key[0]['k'])
    result = subprocess.call(['serial_tx'] + ids)
    return jsonify( { 'result': result } )

if __name__ == '__main__':
    pobj = subprocess.Popen(['serial_tx'])
    try:
        app.run(host='0.0.0.0', port=8850)
    finally:
        pobj.kill()
