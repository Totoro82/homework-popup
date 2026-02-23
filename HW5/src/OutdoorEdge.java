// Edge that tracks outdoor vs indoor time separately
public class OutdoorEdge extends Edge {
    final int outdoorWeight;

    OutdoorEdge(int to, int weight, boolean outdoor) {
        super(to, weight);
        this.outdoorWeight = outdoor ? weight : 0;
    }
}
