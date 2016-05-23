from __future__ import print_function

import argparse
from collections import defaultdict

from pymongo import MongoClient

parser = argparse.ArgumentParser(description='Python popular topics extractor.')
parser.add_argument('-t1', '--tag1', help='analysis for tag', required=True)
parser.add_argument('-t2', '--tag2', help='analysis for tag', required=True)
parser.add_argument('-t3', '--tag3', help='analysis for tag', required=False)
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

    tagsCount = defaultdict(int)

    for note in notes:
		try:
			note['tags'] = map(lambda x: x.toLower(), note['tags'])
		except:
			pass
		if args.tag1 in note['tags'] and args.tag2 in note['tags']: 
			for tag in note['tags']:
				tagsCount[tag.lower()] += 1
			print(note['text1'])
			print(note['text2'])
			print('\n')

    items = filter(lambda pair: pair[1] > 5,
                   sorted(tagsCount.items(), key=lambda pair: pair[1])
                  )
    for item in items:
        print(item)
