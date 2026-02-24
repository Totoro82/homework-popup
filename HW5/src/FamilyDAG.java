import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class FamilyDAG {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        HashMap<String, Set<String>> graph = new HashMap<>();
        boolean first = true;
        while((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line);
            String parent = st.nextToken();

            if(Objects.equals(parent, "done")){ // limpiar arbol genealogioc
                if (!first) System.out.println();
                first = false;

                Map<String, String> results = hillbillyOrParadox(graph);

                    StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> entry : results.entrySet()) {
                    sb.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
                }
                System.out.print(sb);

                graph.clear();
                continue;
            }

            st.nextToken();
            String child = st.nextToken();

            graph.computeIfAbsent(parent, v -> new HashSet<>()).add(child);

        }
    }

    private static Map<String, String> hillbillyOrParadox(HashMap<String, Set<String>> graph) {
        // Hillbilly detection
        Set<String> hillbillies = new HashSet<>();
        for (String node : graph.keySet()) {
            Set<String> visited = new HashSet<>();
            dfsHillbilly(node, graph, visited, hillbillies);
        }

        // Paradox detection
        Set<String> paradoxes = new HashSet<>();
        // WHITE (not in map) = not visited
        // GRAY (1) = currently being explored (in the current DFS path)
        // BLACK (2) = fully explored (all descendants visited)
        Map<String, Integer> color = new HashMap<>();
        for (String node : graph.keySet()) {
            if (!color.containsKey(node)) {
                dfsParadox(node, graph, color, paradoxes, new ArrayList<>());
            }
        }

        // Collect all people sorted lexicographically with their label
        TreeMap<String, String> results = new TreeMap<>();
        Set<String> allPeople = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : graph.entrySet()) {
            allPeople.add(entry.getKey());
            allPeople.addAll(entry.getValue());
        }
        for (String person : allPeople) {
            if (paradoxes.contains(person)) {
                results.put(person, "paradox");
            } else if (hillbillies.contains(person)) {
                results.put(person, "hillbilly");
            }
        }
        return results;
    }

    private static void dfsParadox(String node, HashMap<String, Set<String>> graph,
                                    Map<String, Integer> color, Set<String> paradoxes, List<String> path) {
        color.put(node, 1); // GRAY
        path.add(node);
        for (String child : graph.getOrDefault(node, Collections.emptySet())) {
            if (!color.containsKey(child)) {
                dfsParadox(child, graph, color, paradoxes, path);
            } else if (color.get(child) == 1) {
                // Cycle found: mark all nodes from child to current as paradox
                int idx = path.indexOf(child);
                for (int i = idx; i < path.size(); i++) {
                    paradoxes.add(path.get(i));
                }
            }
        }
        path.remove(path.size() - 1);
        color.put(node, 2); // BLACK
    }

    private static void dfsHillbilly(String node, HashMap<String, Set<String>> graph,
                                      Set<String> visited, Set<String> hillbillies) {
        for (String child : graph.getOrDefault(node, Collections.emptySet())) {
            if (!visited.add(child)) {
                // child already reachable by another path → hillbilly
                markAllDescendants(child, graph, hillbillies);
            } else {
                dfsHillbilly(child, graph, visited, hillbillies);
            }
        }
    }

    private static void markAllDescendants(String node, HashMap<String, Set<String>> graph,
                                            Set<String> hillbillies) {
        if (!hillbillies.add(node)) return; // already marked
        for (String child : graph.getOrDefault(node, Collections.emptySet())) {
            markAllDescendants(child, graph, hillbillies);
        }
    }
}
