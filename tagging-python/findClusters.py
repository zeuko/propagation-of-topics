from __future__ import print_function

import argparse
import time
from collections import defaultdict

import networkx as nx
from networkx import find_cliques
from pymongo import MongoClient

parser = argparse.ArgumentParser(
    description='Find clusters ("ideas") in notes repository. First looks for pairs of topis witch occurs in at least <WEIGHT> notes. ')
parser.add_argument('-l', '--language', help='language of tagged notes', required=True)
parser.add_argument('-host', '--mongoDb_host', help='mongoDb host', required=False, default='localhost')
parser.add_argument('-p', '--mongoDb_port', help='mongoDb port', required=False, default=27017, type=int)
parser.add_argument('-v', '--verbose', dest='verbose', action='store_true')
parser.add_argument('--no-verbose', dest='verbose', action='store_false')
parser.add_argument('-w', '--weight', help='minimum weight (tag have to occur in minimum <W> notes to be considered as part of cliqe)', required=True, type=int)
parser.set_defaults(verbose=False)

if __name__ == '__main__':
    '''Wierzcholkiem grafu bedzie tag, krawedzia ich wspolne wystapienie'''
    args = parser.parse_args()
    client = MongoClient(args.mongoDb_host, args.mongoDb_port)

    notesCollection = client.notes.note
    cliquesCollection = client.notes.clieques
    notes = notesCollection.find({'language': args.language})

    G = nx.Graph()
    tagsOccurrences = defaultdict(lambda: defaultdict(int))
    tagsToNotes = defaultdict(list)

    filteredTagsOccurrences = {}
    time1 = time.time()
    for note in notes:
        for tag in note['pythonTags'] + note['tags']:
            tagsToNotes[tag].append(note)
            for tag2 in note['pythonTags'] + note['tags']:
                if tag != tag2 and max(tag, tag2) is tag:
                    tagsOccurrences[tag][tag2] += 1

    time2 = time.time()
    if args.verbose:
        print("Notes loaded to tagsOccurrences in {}".format(time2 - time1))

    for tag, tags2 in tagsOccurrences.items():
        connectedTags = [tag2 for tag2, occurrences in tags2.items() if occurrences >= args.weight]
        if len(connectedTags) > 0:
            filteredTagsOccurrences[tag] = connectedTags

    time3 = time.time()
    if args.verbose:
        print("Notes loaded to filteredTagsOccurrences in {} ".format(time2 - time3))

    G.add_nodes_from(filteredTagsOccurrences.keys())
    for tag, tags2 in filteredTagsOccurrences.items():
        for tag2 in tags2:
            if args.verbose:
                print("{} <-> {}".format(tag, tag2))
            G.add_edge(tag, tag2)

    for clique in find_cliques(G):

        hashed = hash(frozenset(clique + [args.weight, ]))

        correspondingNotes = [note['_id'] for tag in clique for note in tagsToNotes[tag]]
        idea = {
            'name': None,
            'tags': clique,
            'size': len(clique),
            'notes_id': correspondingNotes,
            'minCommonTopics': args.weight
        }
        cliquesCollection.update({'hash': int(hashed)}, idea, upsert=True)

        if args.verbose:
            print("Idea for {} notes: {}".format(len(correspondingNotes), clique))
