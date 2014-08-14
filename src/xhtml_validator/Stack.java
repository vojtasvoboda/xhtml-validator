package xhtml_validator;

/**
 *
 * @author Vojta
 */
public class Stack {

    private int size;
    private int top;
    private String[] values;

    public Stack(int size) {
        this.size = size;
        values = new String[size];
        top = -1;
    }

    public void push(String i) {
        if(!is_full()) {
            top++;
            values[top] = i;
        } else
            throw new UnsupportedOperationException("Stack is full!");
    }

    public String pop() {

        String i = "";
        if(!is_empty()) {
            i = values[top];
            top--;
        }
        return i;
    }

    public boolean is_empty() {

        if(top == -1)
            return true;
        else
            return false;
    }

    public boolean is_full() {

        if(top < size - 1)
            return false;
        else
            return true;
    }
}
