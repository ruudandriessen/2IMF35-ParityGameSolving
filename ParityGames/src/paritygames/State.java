/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paritygames;

/**
 *
 * @author Hein
 */
public class State {
    private final int id;
    private final int priority;
    private final int owner;
    private final int[] successors;
    private final String name; 
    
    public State(Integer x, Integer y, Integer z, int[] suc, String n) {
        this.id = x;
        this.priority = y;
        this.owner = z;
        this.successors = suc;
        this.name = n;
    }
    
    public int getID() {
        return this.id;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!State.class.isAssignableFrom(obj.getClass())) return false;
        final State n = (State) obj;
        return (this.id == n.id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.id;
        return hash;
    }
    
    @Override
    public String toString() {
        String id = "ID: " + Integer.toString(this.id) + "\n";
        String priority = "Priority: " + Integer.toString(this.priority) + "\n";
        String owner = "Owner: " + Integer.toString(this.owner) + "\n";
        String successors = "Successors : ";
        for (int s : this.successors) {
            successors += Integer.toString(s) + " ";
        }
        successors += "\n";
        String name = "Name: " + this.name + "\n";
        return id + priority + owner + successors + name;
    }
}