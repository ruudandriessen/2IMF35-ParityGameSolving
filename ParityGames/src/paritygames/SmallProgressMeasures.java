package paritygames;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import paritygames.Vertex.Owner;

/**
 *
 * @author ruudandriessen
 */
public class SmallProgressMeasures {

    public static Pair<List<Vertex>, List<Vertex>> calculate(ParityGame pg, LiftStrategy strategy) throws IllegalTupleException {
        // Set default tuple size
        dTuple.setTupleSize(pg.maxPriority() + 1);

        // Set max tuple
        dTuple.setMaxTuple(maxTuple(pg));

        for (Vertex v : pg.getVertices()) {
            v.setTuple(new dTuple());
        }

        dTuple nRho, rho;
        while (!pg.isStable()) {
            // Lift the next vertex until stable
            Vertex v = strategy.next();
            
            rho = v.getTuple();
            nRho = lift(v, rho);
            
//            System.out.println(rho + " lt " + nRho);
            if (rho.lt(nRho)) {
                v.setStable(false);
            } else {
                v.setStable(true);
            }
            v.setTuple(new dTuple(nRho));
        }
        return results(pg);
    }

    public static dTuple lift(Vertex v, dTuple rho) throws IllegalTupleException {
//        System.out.println("-------");
        if (v.getOwner() == Owner.EVEN) {
//        System.out.println("Lift: " + v + " / even");
            // Initialize new d-tuple to T
            dTuple min = new dTuple();
            min.setTop(true);

            // Calculate the minimum of all prog(rho, v, w)
            for (Vertex w : v.getSuccessors()) {
                dTuple newTuple = prog(v, w);
//                System.out.println("Prog (" + v.getTuple() + ", " + v + ", " + w + "): " + newTuple);
                if (newTuple.lt(min)) {
//                    System.out.println(newTuple + " < " + min);
                    min = newTuple;
                } else {
//                    System.out.println(newTuple +" !> " + min);
                }
            }
//            System.out.println("Even minimum: " + min);
            return rho.gt(min) ? rho : min;
        } else { // v.getOwner() == Owner.ODD
//            System.out.println("Lift: " + v + " / odd");
            // Initialize new d-tuple to (0,...0)
            dTuple max = new dTuple();

            // Calculate the maximum of all prog(rho, v, w)
            for (Vertex w : v.getSuccessors()) {
                dTuple newTuple = prog(v, w);
//                System.out.println("Prog (" + v.getTuple() + ", " + v + ", " + w + "): " + newTuple);
                if (newTuple.gt(max)) {
//                    System.out.println(newTuple + " > " + max);
                    max = newTuple;
                } else {
//                    System.out.println(newTuple +" !> " + max);
                }
            }
//            System.out.println("Odd maximum: " + max);
            return rho.gt(max) ? rho : max;
        }
    }

    public static dTuple prog(Vertex v, Vertex w) throws IllegalTupleException {
        dTuple result = new dTuple();
        int vPriority = v.getPriority();
        dTuple wRho = w.getTuple();
        if (vPriority % 2 == 0) { // Even priority
            for (int i = vPriority + 1; i < dTuple.size(); i++) {
                // Minimize everything behind the priority value
                result.set(i, 0);
            }
            for (int i = 0; i <= vPriority; i++) {
                result.set(i, wRho.get(i));
            }
        } else { // Odd priority
            for (int i = vPriority + 1; i < dTuple.size(); i++) {
                // Minimize everything behind the priority value
                result.set(i, 0);
            }
            for (int i = 0; i <= vPriority; i++) {
                result.set(i, wRho.get(i));
            }
            result.increment(vPriority);
        }
        if (wRho.isTop()) {
            result.setTop(true);
        }
        return result;
    }

    public static dTuple maxTuple(ParityGame pg) throws IllegalTupleException {
        dTuple maxTuple = new dTuple();

        for (Vertex v : pg.getVertices()) {
            // Get priority of each vertex
            int priority = v.getPriority();

            if (priority % 2 != 0) {
                // Increase tuple value if priority is odd
                maxTuple.increment(priority);
            }
        }
        return maxTuple;
    }

    public static Pair<List<Vertex>, List<Vertex>> results(ParityGame pg) {
        List<Vertex> vEven = new ArrayList<>(),
                vOdd = new ArrayList<>();

        pg.getVertices().stream().forEach((v) -> {
            if (v.getTuple().isTop()) {
                // Odd won if the value is top
                vOdd.add(v);
            } else {
                // Even won if the value is not top
                vEven.add(v);
            }
        });

        return new Pair(vEven, vOdd);
    }
}
