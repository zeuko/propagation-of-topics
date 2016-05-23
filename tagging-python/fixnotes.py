from __future__ import print_function

import argparse
from collections import defaultdict

from pymongo import MongoClient

parser = argparse.ArgumentParser(description='Python popular topics extractor.')
parser.add_argument('-host', '--mongoDb_host', help='mongoDb host', required=False, default='localhost')
parser.add_argument('-p', '--mongoDb_port', help='mongoDb port', required=False, default=27017, type=int)
parser.add_argument('-v', '--verbose', dest='verbose', action='store_true')
parser.add_argument('--no-verbose', dest='verbose', action='store_false')
parser.set_defaults(verbose=False)

if __name__ == '__main__':
    args = parser.parse_args()
    client = MongoClient(args.mongoDb_host, args.mongoDb_port)

    notesCollection = client.notes.note
    notes = notesCollection.find({'language': 'EN'})

    for note in notes:
        tags = set()
        for tag in note['tags']:
            if tag == "ap" or tag == "monkeys":
                continue;
            try:
                tags.add(tag.lower())
            except:
                print(tag)
        note['tags'] = list(tags)
        notesCollection.update({'_id': note['_id']}, note)
