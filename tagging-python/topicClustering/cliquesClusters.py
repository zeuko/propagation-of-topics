from __future__ import print_function

import argparse

from networkx import find_cliques

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
    '''Wierzcholkiem grafu bedzie tag, krawedzia ich wspolne wystapienie'''
    args = parser.parse_args()
    client, G, tagsToNotes = load_graph(**vars(args))
    ideasCollection = client.notes.ideas

    found_cliques = list(find_cliques(G))
    print("Found {} cliques".format(len(found_cliques)))
    loaded = 0
    for clique in found_cliques:
        hashed = hash(frozenset(clique + [args.weight, ]))

        correspondingNotes = list(set([note['_id'] for tag in clique for note in tagsToNotes[tag]]))
        idea = {
            'name': None,
            'tags': clique,
            'size': len(clique),
            'notes_ids': correspondingNotes,
            'notes_ids_size': len(correspondingNotes),
            'algorithm': 'find_cliques',
            'minCommonTopics': args.weight
        }
        ideasCollection.update({'hash': int(hashed)}, idea, upsert=True)

        loaded += 1
        if args.verbose:
            print("{}%: Idea for {} notes: {}".format(loaded / len(found_cliques), len(correspondingNotes), clique))
