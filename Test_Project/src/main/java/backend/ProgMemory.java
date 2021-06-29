package backend;

public class ProgMemory {
    private int[] memory = new int[1024];

    public int[] getMemory(){
        return this.memory;
    }

    public void setCommand(int cell, int command){
        memory[cell] = command;
    }

    // TODO handle program counter overflow after 13 bit
}
