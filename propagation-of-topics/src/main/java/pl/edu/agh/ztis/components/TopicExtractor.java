package pl.edu.agh.ztis.components;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Sets;

import net.sf.javaml.clustering.mcl.MarkovClustering;
import net.sf.javaml.clustering.mcl.SparseMatrix;
import pl.edu.agh.ztis.model.Note;
import pl.edu.agh.ztis.model.Topic;
import pl.edu.agh.ztis.repositories.NoteRepository;

@Component
public class TopicExtractor {

    // Maximum difference between row elements and row square sum (measure of
    // idempotence)
    private double maxResidual = 0.001;

    // inflation exponent for Gamma operator
    private double pGamma = 2.0;

    // loopGain values for cycles
    private double loopGain = 0.;

    // maximum value considered zero for pruning operations
    private double maxZero = 0.001;

    @Autowired
    private NoteRepository noteRepository;
    private ConcurrentHashMap<String, Set<Note>> tagsToNotes = new ConcurrentHashMap<>();
    private BiMap<String, Integer> tagToId = HashBiMap.create();
    private Map<String, Map<String, AtomicInteger>> tagsOccurences = new ConcurrentHashMap<>();
    private List<List<Map.Entry<String, AtomicInteger>>> filteredOccurences;

    public List<Topic> extractTopics(List<Note> notes, int minWeight) {

        long time1 = System.currentTimeMillis();
        SparseMatrix graph = generateGraph(notes, minWeight);
        long time2 = System.currentTimeMillis();
        System.out.println("Graph loaded in " + 0.001 * (time2 - time1) + "s");

        SparseMatrix resultMatrix = new MarkovClustering()
                .run(graph, maxResidual, pGamma, loopGain, maxZero);
        long time3 = System.currentTimeMillis();
        System.out.println("Markov Clustering finished in " + 0.001 * (time3 - time2) + "s");
        List<Set<String>> topics = extractResults(resultMatrix);
        List<Topic> result = topics.stream()
                .map(topicTags -> {
                    List<Long> notesForThisTags = topicTags.stream()
                            .filter(s -> s!=null)
                            .map(tagsToNotes::get)
                            .filter(s -> s!=null)
                            .flatMap(Set::stream)
                            .filter(s -> s!=null)
                            .distinct()
                            .map(Note::getId)
                            .collect(Collectors.toList());
                    System.out.println(notesForThisTags.size() + " ----> " + topicTags.stream().collect(Collectors.joining(", ")));
                    return new Topic("", new LinkedList<>(topicTags), topicTags.size(), notesForThisTags, notesForThisTags.size(), "mcl-java", minWeight);
                }).collect(Collectors.toList());

        long time4 = System.currentTimeMillis();
        System.out.println("Topic extraction finished in " + 0.001 * (time4 - time1));
        return result;
    }

    private List<Set<String>> extractResults(SparseMatrix matrix) {

        // convert matrix to output dataset:
        int[] sparseMatrixSize = matrix.getSize();
        // find number of attractors (non zero values) in diagonal
        int attractors = 0;
        for (int i = 0; i < sparseMatrixSize[0]; i++) {
            double val = matrix.get(i, i);
            if (val != 0) {
                attractors++;
            }
        }
        // create cluster for each attractor with value close to 1
        List<Set<String>> finalClusters = new Vector<>();
        BiMap<Integer, String> idToTag = tagToId.inverse();

        for (int i = 0; i < sparseMatrixSize[0]; i++) {
            Set<String> cluster = new HashSet<>();
            double val = matrix.get(i, i);
            if (val >= 0.98) {
                for (int j = 0; j < sparseMatrixSize[0]; j++) {
                    double value = matrix.get(j, i);
                    if (value != 0) {
                        cluster.add(idToTag.get(j));
                    }
                }
                finalClusters.add(cluster);
            }
        }

        return finalClusters;
    }

    private SparseMatrix generateGraph(List<Note> notes, int minWeight) {
        notes.forEach(note -> {
            Set<String> allTags = note.getAllTags();
            for (String tag : allTags) {
                tagsToNotes.computeIfAbsent(tag, ignore -> Sets.newConcurrentHashSet())
                        .add(note);

                for (String tag2 : allTags) {
                    if (tag.compareTo(tag2) > 0) {
                        tagsOccurences.computeIfAbsent(tag, ignore -> new ConcurrentHashMap<>())
                                .computeIfAbsent(tag2, ignore -> new AtomicInteger())
                                .incrementAndGet();
                    }
                }
            }
        });
        SparseMatrix graph = new SparseMatrix();

        AtomicInteger tagIndex = new AtomicInteger();
        AtomicInteger edges = new AtomicInteger();
        tagsOccurences.forEach((tag1, connectedNodes) -> {
            connectedNodes.forEach((tag2, weight) -> {
                if (weight.get() >= minWeight) {
                    Integer id1 = tagToId.computeIfAbsent(tag1, ignore -> tagIndex.incrementAndGet());
                    Integer id2 = tagToId.computeIfAbsent(tag2, ignore -> tagIndex.incrementAndGet());

                    graph.add(id1, id2, weight.get());
                    graph.add(id2, id1, weight.get());
                    edges.incrementAndGet();
                }
            });
        });
        System.out.println(String.format("Generated graph with %d edges and %d vertexes", edges.get(), tagIndex.get()));
        return graph;
    }
}
