from __future__ import print_function

import argparse

from mcl.mcl_clustering import networkx_mcl

from graphs import *

parser = argparse.ArgumentParser(
    description='Find clusters ("ideas") in notes repository. First looks for pairs of topis witch occurs in at least <WEIGHT> notes. ')
parser.add_argument('-l', '--language', help='language of tagged notes', required=True)
parser.add_argument('-host', '--mongoDb_host', help='mongoDb host', required=False, default='localhost')
parser.add_argument('-p', '--mongoDb_port', help='mongoDb port', required=False, default=27017, type=int)
parser.add_argument('-v', '--verbose', dest='verbose', action='store_true')
parser.add_argument('--no-verbose', dest='verbose', action='store_false')
parser.add_argument('-w', '--weight', help='minimum weight (tag have to occur in minimum <W> notes to be considered as part of cliqe)', required=True, type=int)
parser.add_argument('-c', '--clean', help='clean database before executing script', required=False, default=False, action='store_true')
parser.set_defaults(verbose=False)

if __name__ == '__main__':
    args = parser.parse_args()
    client, G, tagsToNotes = load_graph(**vars(args))
    ideasCollection = client.notes.ideas

    M, clusters = networkx_mcl(G)
    print("Found {} clusters".format(len(clusters)))
    for clique in clusters:
        hashed = hash(frozenset(clique + [args.weight, ]))

        correspondingNotes = list(set([note['_id'] for tag in clique for note in tagsToNotes[tag]]))
        idea = {
            'name': None,
            'tags': clique,
            'size': len(clique),
            'notes_ids': correspondingNotes,
            'notes_ids_size': len(correspondingNotes),
            'algorithm': 'mcl',
            'minCommonTopics': args.weight
        }
        # ideasCollection.update({'hash': int(hashed)}, idea, upsert=True)

        if args.verbose:
            print("Idea for {} notes: {}".format(len(correspondingNotes), clique))
