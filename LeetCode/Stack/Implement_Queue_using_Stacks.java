/*
232 Implement Queue using Stacks
*/

// 使用两个栈即可完成队列的操作

class MyQueue {

   Stack<Integer> stack_Input;
   Stack<Integer> stack_Output;
   private int front;

   /** Initialize your data structure here. */
   public MyQueue() {
      stack_Input = new Stack<>();
      stack_Output = new Stack<>();
   }

   /** Push element x to the back of queue. */
   public void push(int x) {
      if (stack_Input.empty())
         front = x;
      stack_Input.push(x);
   }

   /** Removes the element from in front of queue and returns that element. */
   public int pop() {
      if (stack_Output.empty()) {
         while (!stack_Input.empty())
            stack_Output.push(stack_Input.pop());
      }
      return stack_Output.pop();
   }

   /** Get the front element. */
   public int peek() {
      if (stack_Output.empty()) {
         while (!stack_Input.empty())
            stack_Output.push(stack_Input.pop());
      }
      return stack_Output.peek();
   }

   /** Returns whether the queue is empty. */
   public boolean empty() {
      return stack_Output.empty() && stack_Input.empty();
   }
}

/*

235 

class MyStack {

    /** Initialize your data structure here. */
    public MyStack() {
        
    }
    
    /** Push element x onto stack. */
    public void push(int x) {
        
    }
    
    /** Removes the element on top of the stack and returns that element. */
    public int pop() {
        
    }
    
    /** Get the top element. */
    public int top() {
        
    }
    
    /** Returns whether the stack is empty. */
    public boolean empty() {
        
    }
}