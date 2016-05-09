from __future__ import print_function

import argparse
from trace import pickle

from pymongo import MongoClient

import tagger

parser = argparse.ArgumentParser(description='Python note tagger.')
parser.add_argument('-l', '--language', help='language of tagged notes', required=True)
parser.add_argument('-host', '--mongoDb_host', help='mongoDb host', required=False, default='localhost')
parser.add_argument('-p', '--mongoDb_port', help='mongoDb port', required=False, default=27017, type=int)
parser.add_argument('-d', '--dictionary', help='dictionary file', required=False, default='data/dict.pkl')
parser.add_argument('-r', '--print_only', help='print only', required=False, default=False, type=bool)
parser.add_argument('-v', '--verbose', dest='verbose', action='store_true')
parser.add_argument('--no-verbose', dest='verbose', action='store_false')
parser.set_defaults(verbose=False)


def getTagger(pkl='data/dict.pkl'):
    weights = pickle.load(open(pkl, 'rb'))
    return tagger.Tagger(tagger.Reader(), tagger.Stemmer(), tagger.Rater(weights))


if __name__ == '__main__':
    args = parser.parse_args()
    client = MongoClient(args.mongoDb_host, args.mongoDb_port)

    notesCollection = client.notes.note
    notes = notesCollection.find({'language': args.language})

    tagger = getTagger(args.dictionary)

    for note in notes:
        if args.print_only:
            print(note)
        else:
            text_to_tag = note['text1'] + "   " + note['text2']
            pythonTags = tagger(text_to_tag)
            tags = [tag.string for tag in pythonTags]
            # tags = [{'text': tag.stem, 'rating': tag.rating} for tag in pythonTags]
            note['pythonTags'] = tags
            notesCollection.update({'_id': note['_id']}, note)
            if args.verbose:
                print(u"Message: {} \n\ttagged as: {}".format(text_to_tag, tags))
