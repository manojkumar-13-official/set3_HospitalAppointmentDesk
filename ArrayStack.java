import java.util.Arrays;

public class ArrayStack<T> {

    private Object[] data = new Object[4];
    private int top = 0;

    public void push(T x) {
        if (top == data.length) data = Arrays.copyOf(data, data.length * 2);
        data[top++] = x;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (top == 0) throw new RuntimeException("Stack is empty");
        T v = (T) data[--top];
        data[top] = null;
        return v;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (top == 0) throw new RuntimeException("Stack is empty");
        return (T) data[top - 1];
    }

    public boolean isEmpty() { return top == 0; }

    public int size() { return top; }
}
