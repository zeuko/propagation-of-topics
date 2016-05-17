"""Load notes from database and create graphs """
import time
from collections import defaultdict

import networkx as nx
from pymongo import MongoClient


def load_graph(mongoDb_host, mongoDb_port, clean, language, verbose, weight, **kwargs):
    client = MongoClient(mongoDb_host, mongoDb_port)

    notesCollection = client.notes.note

    if clean:
        client.notes.drop_collection('ideas')

    notes = notesCollection.find({'language': language})

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
    if verbose:
        print("Notes loaded to tagsOccurrences in {}".format(time2 - time1))

    for tag, tags2 in tagsOccurrences.items():
        connectedTags = [tag2 for tag2, occurrences in tags2.items() if occurrences >= weight]
        if len(connectedTags) > 0:
            filteredTagsOccurrences[tag] = connectedTags

    time3 = time.time()
    if verbose:
        print("Notes loaded to filteredTagsOccurrences in {} ".format(time3 - time2))

    G.add_nodes_from(filteredTagsOccurrences.keys())
    for tag, tags2 in filteredTagsOccurrences.items():
        for tag2 in tags2:
            G.add_edge(tag, tag2)

    return client, G, tagsToNotes
