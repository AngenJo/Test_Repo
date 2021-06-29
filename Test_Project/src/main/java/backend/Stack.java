package backend;

public class Stack {
    private final int STACK_SIZE = 8;
    private int[] stack;
    private int stackPointer;

    public Stack() {
        this.stack = new int[STACK_SIZE];
        this.stackPointer = 0;
    }

    public void push(int addr) {
        this.stackPointer++;
        if (this.stackPointer == this.STACK_SIZE) {
            this.stackPointer = 0;
        }
        this.stack[this.stackPointer] = addr;
    }

    public int pop() {
        int tos = this.stack[this.stackPointer];
        this.stackPointer--;
        if (this.stackPointer < 0) {
            this.stackPointer = this.STACK_SIZE - 1;
        }
        return tos;
    }

    public int[] getStack() {
        int[] ret = new int[STACK_SIZE];
        for (int i = 0; i < STACK_SIZE; i++) {
            ret[i] = this.pop();
        }
        return ret;
    }

    public int peek() {
        return this.stack[this.stackPointer];
    }

}
