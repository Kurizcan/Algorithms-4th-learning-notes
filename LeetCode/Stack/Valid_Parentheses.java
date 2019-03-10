/*
20. Given a string containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.

An input string is valid if:

Open brackets must be closed by the same type of brackets.
Open brackets must be closed in the correct order.
Note that an empty string is also considered valid.
*/

class Solution {
   public boolean isValid(String s) {
      int count = 0;
      Stack<Character> stack = new Stack();
      Map map = new HashMap();
      map.put(')', '(');
      map.put('}', '{');
      map.put(']', '[');
      while (count != s.length()) {
         if (!map.containsKey(s.charAt(count)))
            stack.push(s.charAt(count));
         else if (!stack.empty() && stack.peek().equals(map.get(s.charAt(count))))
            stack.pop();
         else
            return false;
         count++;
      }
      return stack.empty();
   }
}


class Solution {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<Character>();
        for (char c : s.toCharArray()) {
            if (c == '(')
                stack.push(')');
            else if (c == '{')
                stack.push('}');
            else if (c == '[')
                stack.push(']');
            else if (stack.isEmpty() || stack.pop() != c)
                return false;
        }
        return stack.isEmpty();
    }
}


class Solution:
    def isValid(self, s: str) -> bool:
        stack = []
        paren_map = {')':'(', ']':'[', '}':'{'}
        for c in s:
        	if c not in paren_map:
        		stack.append(c)
        	elif not stack or paren_map[c] != stack.pop():
        		return false
        return not stack